// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.DialogInterface$OnCancelListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import android.content.DialogInterface;
import org.telegram.SQLite.SQLiteCursor;
import java.util.Locale;
import org.telegram.tgnet.AbstractSerializedData;
import android.util.LongSparseArray;
import org.telegram.messenger.support.SparseLongArray;
import android.content.DialogInterface$OnClickListener;
import java.math.BigInteger;
import org.telegram.tgnet.TLClassStore;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.tgnet.RequestDelegate;
import java.io.File;
import android.app.Activity;
import org.telegram.ui.ActionBar.AlertDialog;
import android.content.Context;
import org.telegram.tgnet.TLObject;
import java.nio.ByteBuffer;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.ConnectionsManager;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;

public class SecretChatHelper
{
    public static final int CURRENT_SECRET_CHAT_LAYER = 73;
    private static volatile SecretChatHelper[] Instance;
    private SparseArray<TLRPC.EncryptedChat> acceptingChats;
    private int currentAccount;
    public ArrayList<TLRPC.Update> delayedEncryptedChatUpdates;
    private ArrayList<Long> pendingEncMessagesToDelete;
    private SparseArray<ArrayList<TL_decryptedMessageHolder>> secretHolesQueue;
    private ArrayList<Integer> sendingNotifyLayer;
    private boolean startingSecretChat;
    
    static {
        SecretChatHelper.Instance = new SecretChatHelper[3];
    }
    
    public SecretChatHelper(final int currentAccount) {
        this.sendingNotifyLayer = new ArrayList<Integer>();
        this.secretHolesQueue = (SparseArray<ArrayList<TL_decryptedMessageHolder>>)new SparseArray();
        this.acceptingChats = (SparseArray<TLRPC.EncryptedChat>)new SparseArray();
        this.delayedEncryptedChatUpdates = new ArrayList<TLRPC.Update>();
        this.pendingEncMessagesToDelete = new ArrayList<Long>();
        this.startingSecretChat = false;
        this.currentAccount = currentAccount;
    }
    
    private void applyPeerLayer(final TLRPC.EncryptedChat encryptedChat, final int n) {
        final int peerLayerVersion = AndroidUtilities.getPeerLayerVersion(encryptedChat.layer);
        if (n <= peerLayerVersion) {
            return;
        }
        if (encryptedChat.key_hash.length == 16 && peerLayerVersion >= 46) {
            try {
                final byte[] computeSHA256 = Utilities.computeSHA256(encryptedChat.auth_key, 0, encryptedChat.auth_key.length);
                final byte[] key_hash = new byte[36];
                System.arraycopy(encryptedChat.key_hash, 0, key_hash, 0, 16);
                System.arraycopy(computeSHA256, 0, key_hash, 16, 20);
                encryptedChat.key_hash = key_hash;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        encryptedChat.layer = AndroidUtilities.setPeerLayerVersion(encryptedChat.layer, n);
        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatLayer(encryptedChat);
        if (peerLayerVersion < 73) {
            this.sendNotifyLayerMessage(encryptedChat, null);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo(this, encryptedChat));
    }
    
    private TLRPC.Message createDeleteMessage(final int n, final int seq_out, final int seq_in, final long n2, final TLRPC.EncryptedChat encryptedChat) {
        final TLRPC.TL_messageService tl_messageService = new TLRPC.TL_messageService();
        tl_messageService.action = new TLRPC.TL_messageEncryptedAction();
        tl_messageService.action.encryptedAction = new TLRPC.TL_decryptedMessageActionDeleteMessages();
        tl_messageService.action.encryptedAction.random_ids.add(n2);
        tl_messageService.id = n;
        tl_messageService.local_id = n;
        tl_messageService.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
        tl_messageService.unread = true;
        tl_messageService.out = true;
        tl_messageService.flags = 256;
        tl_messageService.dialog_id = (long)encryptedChat.id << 32;
        tl_messageService.to_id = new TLRPC.TL_peerUser();
        tl_messageService.send_state = 1;
        tl_messageService.seq_in = seq_in;
        tl_messageService.seq_out = seq_out;
        if (encryptedChat.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            tl_messageService.to_id.user_id = encryptedChat.admin_id;
        }
        else {
            tl_messageService.to_id.user_id = encryptedChat.participant_id;
        }
        tl_messageService.date = 0;
        tl_messageService.random_id = n2;
        return tl_messageService;
    }
    
    private TLRPC.TL_messageService createServiceSecretMessage(final TLRPC.EncryptedChat encryptedChat, final TLRPC.DecryptedMessageAction encryptedAction) {
        final TLRPC.TL_messageService e = new TLRPC.TL_messageService();
        e.action = new TLRPC.TL_messageEncryptedAction();
        e.action.encryptedAction = encryptedAction;
        final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
        e.id = newMessageId;
        e.local_id = newMessageId;
        e.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
        e.unread = true;
        e.out = true;
        e.flags = 256;
        e.dialog_id = (long)encryptedChat.id << 32;
        e.to_id = new TLRPC.TL_peerUser();
        e.send_state = 1;
        if (encryptedChat.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            e.to_id.user_id = encryptedChat.admin_id;
        }
        else {
            e.to_id.user_id = encryptedChat.participant_id;
        }
        if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
            e.date = 0;
        }
        else {
            e.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        }
        e.random_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        final ArrayList<TLRPC.Message> list = new ArrayList<TLRPC.Message>();
        list.add(e);
        MessagesStorage.getInstance(this.currentAccount).putMessages(list, false, true, true, 0);
        return e;
    }
    
    private boolean decryptWithMtProtoVersion(final NativeByteBuffer nativeByteBuffer, byte[] computeSHA1, final byte[] array, final int n, boolean b, final boolean b2) {
        if (n == 1) {
            b = false;
        }
        final MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(computeSHA1, array, b, n);
        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, false, false, 24, nativeByteBuffer.limit() - 24);
        final int int32 = nativeByteBuffer.readInt32(false);
        if (n == 2) {
            int n2;
            if (b) {
                n2 = 8;
            }
            else {
                n2 = 0;
            }
            final ByteBuffer buffer = nativeByteBuffer.buffer;
            if (!Utilities.arraysEquals(array, 0, Utilities.computeSHA256(computeSHA1, n2 + 88, 32, buffer, 24, buffer.limit()), 8)) {
                if (b2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                return false;
            }
        }
        else {
            final int n3 = int32 + 28;
            int limit;
            if (n3 < nativeByteBuffer.buffer.limit() - 15 || (limit = n3) > nativeByteBuffer.buffer.limit()) {
                limit = nativeByteBuffer.buffer.limit();
            }
            computeSHA1 = Utilities.computeSHA1(nativeByteBuffer.buffer, 24, limit);
            if (!Utilities.arraysEquals(array, 0, computeSHA1, computeSHA1.length - 16)) {
                if (b2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                return false;
            }
        }
        if (int32 > 0 && int32 <= nativeByteBuffer.limit() - 28) {
            final int n4 = nativeByteBuffer.limit() - 28 - int32;
            return (n != 2 || (n4 >= 12 && n4 <= 1024)) && (n != 1 || n4 <= 15);
        }
        return false;
    }
    
    public static SecretChatHelper getInstance(final int n) {
        final SecretChatHelper secretChatHelper;
        if ((secretChatHelper = SecretChatHelper.Instance[n]) == null) {
            synchronized (SecretChatHelper.class) {
                if (SecretChatHelper.Instance[n] == null) {
                    SecretChatHelper.Instance[n] = new SecretChatHelper(n);
                }
            }
        }
        return secretChatHelper;
    }
    
    public static boolean isSecretInvisibleMessage(final TLRPC.Message message) {
        final TLRPC.MessageAction action = message.action;
        if (action instanceof TLRPC.TL_messageEncryptedAction) {
            final TLRPC.DecryptedMessageAction encryptedAction = action.encryptedAction;
            if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSecretVisibleMessage(final TLRPC.Message message) {
        final TLRPC.MessageAction action = message.action;
        if (action instanceof TLRPC.TL_messageEncryptedAction) {
            final TLRPC.DecryptedMessageAction encryptedAction = action.encryptedAction;
            if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages || encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                return true;
            }
        }
        return false;
    }
    
    private void resendMessages(final int n, final int n2, final TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat != null) {
            if (n2 - n >= 0) {
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SecretChatHelper$UeLgl_NG4gDOs_Y4sJhukTdyyjM(this, n, encryptedChat, n2));
            }
        }
    }
    
    private void updateMediaPaths(final MessageObject messageObject, final TLRPC.EncryptedFile encryptedFile, final TLRPC.DecryptedMessage decryptedMessage, final String s) {
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        if (encryptedFile != null) {
            final TLRPC.MessageMedia media = messageOwner.media;
            if (media instanceof TLRPC.TL_messageMediaPhoto) {
                final TLRPC.Photo photo = media.photo;
                if (photo != null) {
                    final ArrayList<TLRPC.PhotoSize> sizes = photo.sizes;
                    final TLRPC.PhotoSize photoSize = sizes.get(sizes.size() - 1);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(photoSize.location.volume_id);
                    sb.append("_");
                    sb.append(photoSize.location.local_id);
                    final String string = sb.toString();
                    photoSize.location = new TLRPC.TL_fileEncryptedLocation();
                    final TLRPC.FileLocation location = photoSize.location;
                    final TLRPC.DecryptedMessageMedia media2 = decryptedMessage.media;
                    location.key = media2.key;
                    location.iv = media2.iv;
                    location.dc_id = encryptedFile.dc_id;
                    location.volume_id = encryptedFile.id;
                    location.secret = encryptedFile.access_hash;
                    location.local_id = encryptedFile.key_fingerprint;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(photoSize.location.volume_id);
                    sb2.append("_");
                    sb2.append(photoSize.location.local_id);
                    final String string2 = sb2.toString();
                    final File directory = FileLoader.getDirectory(4);
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(string);
                    sb3.append(".jpg");
                    new File(directory, sb3.toString()).renameTo(FileLoader.getPathToAttach(photoSize));
                    ImageLoader.getInstance().replaceImageInCache(string, string2, ImageLocation.getForPhoto(photoSize, messageOwner.media.photo), true);
                    final ArrayList<TLRPC.Message> list = new ArrayList<TLRPC.Message>();
                    list.add(messageOwner);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(list, false, true, false, 0);
                    return;
                }
            }
            final TLRPC.MessageMedia media3 = messageOwner.media;
            if (media3 instanceof TLRPC.TL_messageMediaDocument) {
                final TLRPC.Document document = media3.document;
                if (document != null) {
                    media3.document = new TLRPC.TL_documentEncrypted();
                    final TLRPC.Document document2 = messageOwner.media.document;
                    document2.id = encryptedFile.id;
                    document2.access_hash = encryptedFile.access_hash;
                    document2.date = document.date;
                    document2.attributes = document.attributes;
                    document2.mime_type = document.mime_type;
                    document2.size = encryptedFile.size;
                    final TLRPC.DecryptedMessageMedia media4 = decryptedMessage.media;
                    document2.key = media4.key;
                    document2.iv = media4.iv;
                    document2.thumbs = document.thumbs;
                    document2.dc_id = encryptedFile.dc_id;
                    if (document2.thumbs.isEmpty()) {
                        final TLRPC.TL_photoSizeEmpty e = new TLRPC.TL_photoSizeEmpty();
                        e.type = "s";
                        messageOwner.media.document.thumbs.add(e);
                    }
                    final String attachPath = messageOwner.attachPath;
                    if (attachPath != null && attachPath.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && new File(messageOwner.attachPath).renameTo(FileLoader.getPathToAttach(messageOwner.media.document))) {
                        messageObject.mediaExists = messageObject.attachPathExists;
                        messageObject.attachPathExists = false;
                        messageOwner.attachPath = "";
                    }
                    final ArrayList<TLRPC.Message> list2 = new ArrayList<TLRPC.Message>();
                    list2.add(messageOwner);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(list2, false, true, false, 0);
                }
            }
        }
    }
    
    public void acceptSecretChat(final TLRPC.EncryptedChat encryptedChat) {
        if (this.acceptingChats.get(encryptedChat.id) != null) {
            return;
        }
        this.acceptingChats.put(encryptedChat.id, (Object)encryptedChat);
        final TLRPC.TL_messages_getDhConfig tl_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tl_messages_getDhConfig.random_length = 256;
        tl_messages_getDhConfig.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getDhConfig, new _$$Lambda$SecretChatHelper$13Zq8UfkfLhsSEm3Hl6dWS1zGEc(this, encryptedChat));
    }
    
    public void checkSecretHoles(final TLRPC.EncryptedChat encryptedChat, final ArrayList<TLRPC.Message> list) {
        final ArrayList list2 = (ArrayList)this.secretHolesQueue.get(encryptedChat.id);
        if (list2 == null) {
            return;
        }
        Collections.sort((List<Object>)list2, (Comparator<? super Object>)_$$Lambda$SecretChatHelper$XChl_gDRHQHDfwtxghrPUY1XhL4.INSTANCE);
        boolean b = false;
        while (list2.size() > 0) {
            final TL_decryptedMessageHolder tl_decryptedMessageHolder = list2.get(0);
            final int out_seq_no = tl_decryptedMessageHolder.layer.out_seq_no;
            final int seq_in = encryptedChat.seq_in;
            if (out_seq_no != seq_in && seq_in != out_seq_no - 2) {
                break;
            }
            this.applyPeerLayer(encryptedChat, tl_decryptedMessageHolder.layer.layer);
            final TLRPC.TL_decryptedMessageLayer layer = tl_decryptedMessageHolder.layer;
            encryptedChat.seq_in = layer.out_seq_no;
            encryptedChat.in_seq_no = layer.in_seq_no;
            list2.remove(0);
            if (tl_decryptedMessageHolder.decryptedWithVersion == 2) {
                encryptedChat.mtproto_seq = Math.min(encryptedChat.mtproto_seq, encryptedChat.seq_in);
            }
            final TLRPC.Message processDecryptedObject = this.processDecryptedObject(encryptedChat, tl_decryptedMessageHolder.file, tl_decryptedMessageHolder.date, tl_decryptedMessageHolder.layer.message, tl_decryptedMessageHolder.new_key_used);
            if (processDecryptedObject != null) {
                list.add(processDecryptedObject);
            }
            b = true;
        }
        if (list2.isEmpty()) {
            this.secretHolesQueue.remove(encryptedChat.id);
        }
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(encryptedChat, true);
        }
    }
    
    public void cleanup() {
        this.sendingNotifyLayer.clear();
        this.acceptingChats.clear();
        this.secretHolesQueue.clear();
        this.delayedEncryptedChatUpdates.clear();
        this.pendingEncMessagesToDelete.clear();
        this.startingSecretChat = false;
    }
    
    public void declineSecretChat(final int chat_id) {
        final TLRPC.TL_messages_discardEncryption tl_messages_discardEncryption = new TLRPC.TL_messages_discardEncryption();
        tl_messages_discardEncryption.chat_id = chat_id;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_discardEncryption, (RequestDelegate)_$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M.INSTANCE);
    }
    
    protected ArrayList<TLRPC.Message> decryptMessage(final TLRPC.EncryptedMessage encryptedMessage) {
        final TLRPC.EncryptedChat encryptedChatDB = MessagesController.getInstance(this.currentAccount).getEncryptedChatDB(encryptedMessage.chat_id, true);
        if (encryptedChatDB != null) {
            if (!(encryptedChatDB instanceof TLRPC.TL_encryptedChatDiscarded)) {
                try {
                    final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(encryptedMessage.bytes.length);
                    nativeByteBuffer.writeBytes(encryptedMessage.bytes);
                    nativeByteBuffer.position(0);
                    final long int64 = nativeByteBuffer.readInt64(false);
                    byte[] array = null;
                    boolean new_key_used = false;
                    Label_0119: {
                        if (encryptedChatDB.key_fingerprint == int64) {
                            array = encryptedChatDB.auth_key;
                        }
                        else {
                            if (encryptedChatDB.future_key_fingerprint != 0L && encryptedChatDB.future_key_fingerprint == int64) {
                                array = encryptedChatDB.future_auth_key;
                                new_key_used = true;
                                break Label_0119;
                            }
                            array = null;
                        }
                        new_key_used = false;
                    }
                    int decryptedWithVersion;
                    if (AndroidUtilities.getPeerLayerVersion(encryptedChatDB.layer) >= 73) {
                        decryptedWithVersion = 2;
                    }
                    else {
                        decryptedWithVersion = 1;
                    }
                    if (array != null) {
                        final byte[] data = nativeByteBuffer.readData(16, false);
                        final boolean b = encryptedChatDB.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId();
                        final boolean b2 = decryptedWithVersion != 2 || encryptedChatDB.mtproto_seq == 0;
                        if (!this.decryptWithMtProtoVersion(nativeByteBuffer, array, data, decryptedWithVersion, b, b2)) {
                            if (decryptedWithVersion == 2) {
                                if (!b2 || !this.decryptWithMtProtoVersion(nativeByteBuffer, array, data, 1, b, false)) {
                                    return null;
                                }
                                decryptedWithVersion = 1;
                            }
                            else {
                                if (!this.decryptWithMtProtoVersion(nativeByteBuffer, array, data, 2, b, b2)) {
                                    return null;
                                }
                                decryptedWithVersion = 2;
                            }
                        }
                        final TLObject tLdeserialize = TLClassStore.Instance().TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                        nativeByteBuffer.reuse();
                        if (!new_key_used && AndroidUtilities.getPeerLayerVersion(encryptedChatDB.layer) >= 20) {
                            ++encryptedChatDB.key_use_count_in;
                        }
                        TLObject message = null;
                        Label_0924: {
                            if (!(tLdeserialize instanceof TLRPC.TL_decryptedMessageLayer)) {
                                if (tLdeserialize instanceof TLRPC.TL_decryptedMessageService) {
                                    message = tLdeserialize;
                                    if (((TLRPC.TL_decryptedMessageService)tLdeserialize).action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                                        break Label_0924;
                                    }
                                }
                                return null;
                            }
                            final TLRPC.TL_decryptedMessageLayer layer = (TLRPC.TL_decryptedMessageLayer)tLdeserialize;
                            if (encryptedChatDB.seq_in == 0 && encryptedChatDB.seq_out == 0) {
                                if (encryptedChatDB.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    encryptedChatDB.seq_out = 1;
                                    encryptedChatDB.seq_in = -2;
                                }
                                else {
                                    encryptedChatDB.seq_in = -1;
                                }
                            }
                            if (layer.random_bytes.length < 15) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("got random bytes less than needed");
                                }
                                return null;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("current chat in_seq = ");
                                sb.append(encryptedChatDB.seq_in);
                                sb.append(" out_seq = ");
                                sb.append(encryptedChatDB.seq_out);
                                FileLog.d(sb.toString());
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("got message with in_seq = ");
                                sb2.append(layer.in_seq_no);
                                sb2.append(" out_seq = ");
                                sb2.append(layer.out_seq_no);
                                FileLog.d(sb2.toString());
                            }
                            if (layer.out_seq_no <= encryptedChatDB.seq_in) {
                                return null;
                            }
                            if (decryptedWithVersion == 1 && encryptedChatDB.mtproto_seq != 0 && layer.out_seq_no >= encryptedChatDB.mtproto_seq) {
                                return null;
                            }
                            if (encryptedChatDB.seq_in != layer.out_seq_no - 2) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("got hole");
                                }
                                ArrayList<TL_decryptedMessageHolder> list;
                                if ((list = (ArrayList<TL_decryptedMessageHolder>)this.secretHolesQueue.get(encryptedChatDB.id)) == null) {
                                    list = new ArrayList<TL_decryptedMessageHolder>();
                                    this.secretHolesQueue.put(encryptedChatDB.id, (Object)list);
                                }
                                if (list.size() >= 4) {
                                    this.secretHolesQueue.remove(encryptedChatDB.id);
                                    final TLRPC.TL_encryptedChatDiscarded tl_encryptedChatDiscarded = new TLRPC.TL_encryptedChatDiscarded();
                                    tl_encryptedChatDiscarded.id = encryptedChatDB.id;
                                    tl_encryptedChatDiscarded.user_id = encryptedChatDB.user_id;
                                    tl_encryptedChatDiscarded.auth_key = encryptedChatDB.auth_key;
                                    tl_encryptedChatDiscarded.key_create_date = encryptedChatDB.key_create_date;
                                    tl_encryptedChatDiscarded.key_use_count_in = encryptedChatDB.key_use_count_in;
                                    tl_encryptedChatDiscarded.key_use_count_out = encryptedChatDB.key_use_count_out;
                                    tl_encryptedChatDiscarded.seq_in = encryptedChatDB.seq_in;
                                    tl_encryptedChatDiscarded.seq_out = encryptedChatDB.seq_out;
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$y__QKcAzTtKUDu_WddTe8KbVDxY(this, tl_encryptedChatDiscarded));
                                    this.declineSecretChat(encryptedChatDB.id);
                                    return null;
                                }
                                final TL_decryptedMessageHolder e = new TL_decryptedMessageHolder();
                                e.layer = layer;
                                e.file = encryptedMessage.file;
                                e.date = encryptedMessage.date;
                                e.new_key_used = new_key_used;
                                e.decryptedWithVersion = decryptedWithVersion;
                                list.add(e);
                                return null;
                            }
                            else {
                                if (decryptedWithVersion == 2) {
                                    encryptedChatDB.mtproto_seq = Math.min(encryptedChatDB.mtproto_seq, encryptedChatDB.seq_in);
                                }
                                this.applyPeerLayer(encryptedChatDB, layer.layer);
                                encryptedChatDB.seq_in = layer.out_seq_no;
                                encryptedChatDB.in_seq_no = layer.in_seq_no;
                                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(encryptedChatDB, true);
                                message = layer.message;
                            }
                        }
                        final ArrayList<TLRPC.Message> list2 = new ArrayList<TLRPC.Message>();
                        final TLRPC.Message processDecryptedObject = this.processDecryptedObject(encryptedChatDB, encryptedMessage.file, encryptedMessage.date, message, new_key_used);
                        if (processDecryptedObject != null) {
                            list2.add(processDecryptedObject);
                        }
                        this.checkSecretHoles(encryptedChatDB, list2);
                        return list2;
                    }
                    nativeByteBuffer.reuse();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e(String.format("fingerprint mismatch %x", int64));
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        return null;
    }
    
    protected void performSendEncryptedRequest(final TLRPC.DecryptedMessage decryptedMessage, final TLRPC.Message message, final TLRPC.EncryptedChat encryptedChat, final TLRPC.InputEncryptedFile inputEncryptedFile, final String s, final MessageObject messageObject) {
        if (decryptedMessage != null && encryptedChat.auth_key != null && !(encryptedChat instanceof TLRPC.TL_encryptedChatRequested) && !(encryptedChat instanceof TLRPC.TL_encryptedChatWaiting)) {
            SendMessagesHelper.getInstance(this.currentAccount).putToSendingMessages(message);
            Utilities.stageQueue.postRunnable(new _$$Lambda$SecretChatHelper$go4ClJO8kMeuzFvRQpPn8_EmO40(this, encryptedChat, decryptedMessage, message, inputEncryptedFile, messageObject, s));
        }
    }
    
    protected void performSendEncryptedRequest(final TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia, final SendMessagesHelper.DelayedMessage delayedMessage) {
        for (int i = 0; i < tl_messages_sendEncryptedMultiMedia.files.size(); ++i) {
            this.performSendEncryptedRequest(tl_messages_sendEncryptedMultiMedia.messages.get(i), delayedMessage.messages.get(i), delayedMessage.encryptedChat, tl_messages_sendEncryptedMultiMedia.files.get(i), delayedMessage.originalPaths.get(i), delayedMessage.messageObjects.get(i));
        }
    }
    
    public void processAcceptedSecretChat(final TLRPC.EncryptedChat encryptedChat) {
        final BigInteger m = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
        final BigInteger bigInteger = new BigInteger(1, encryptedChat.g_a_or_b);
        if (!Utilities.isGoodGaAndGb(bigInteger, m)) {
            this.declineSecretChat(encryptedChat.id);
            return;
        }
        final byte[] byteArray = bigInteger.modPow(new BigInteger(1, encryptedChat.a_or_b), m).toByteArray();
        byte[] auth_key;
        if (byteArray.length > 256) {
            auth_key = new byte[256];
            System.arraycopy(byteArray, byteArray.length - 256, auth_key, 0, 256);
        }
        else if (byteArray.length < 256) {
            final byte[] array = new byte[256];
            System.arraycopy(byteArray, 0, array, 256 - byteArray.length, byteArray.length);
            int n = 0;
            while (true) {
                auth_key = array;
                if (n >= 256 - byteArray.length) {
                    break;
                }
                array[n] = 0;
                ++n;
            }
        }
        else {
            auth_key = byteArray;
        }
        final byte[] computeSHA1 = Utilities.computeSHA1(auth_key);
        final byte[] array2 = new byte[8];
        System.arraycopy(computeSHA1, computeSHA1.length - 8, array2, 0, 8);
        if (encryptedChat.key_fingerprint == Utilities.bytesToLong(array2)) {
            encryptedChat.auth_key = auth_key;
            encryptedChat.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            encryptedChat.seq_in = -2;
            encryptedChat.seq_out = 1;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM(this, encryptedChat));
        }
        else {
            final TLRPC.TL_encryptedChatDiscarded tl_encryptedChatDiscarded = new TLRPC.TL_encryptedChatDiscarded();
            tl_encryptedChatDiscarded.id = encryptedChat.id;
            tl_encryptedChatDiscarded.user_id = encryptedChat.user_id;
            tl_encryptedChatDiscarded.auth_key = encryptedChat.auth_key;
            tl_encryptedChatDiscarded.key_create_date = encryptedChat.key_create_date;
            tl_encryptedChatDiscarded.key_use_count_in = encryptedChat.key_use_count_in;
            tl_encryptedChatDiscarded.key_use_count_out = encryptedChat.key_use_count_out;
            tl_encryptedChatDiscarded.seq_in = encryptedChat.seq_in;
            tl_encryptedChatDiscarded.seq_out = encryptedChat.seq_out;
            tl_encryptedChatDiscarded.admin_id = encryptedChat.admin_id;
            tl_encryptedChatDiscarded.mtproto_seq = encryptedChat.mtproto_seq;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(tl_encryptedChatDiscarded);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE(this, tl_encryptedChatDiscarded));
            this.declineSecretChat(encryptedChat.id);
        }
    }
    
    public TLRPC.Message processDecryptedObject(final TLRPC.EncryptedChat encryptedChat, final TLRPC.EncryptedFile encryptedFile, int n, final TLObject obj, final boolean b) {
        if (obj != null) {
            int n2;
            if ((n2 = encryptedChat.admin_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                n2 = encryptedChat.participant_id;
            }
            if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 20 && encryptedChat.exchange_id == 0L && encryptedChat.future_key_fingerprint == 0L && encryptedChat.key_use_count_in >= 120) {
                this.requestNewSecretChatKey(encryptedChat);
            }
            if (encryptedChat.exchange_id == 0L && encryptedChat.future_key_fingerprint != 0L && !b) {
                encryptedChat.future_auth_key = new byte[256];
                encryptedChat.future_key_fingerprint = 0L;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
            }
            else if (encryptedChat.exchange_id != 0L && b) {
                encryptedChat.key_fingerprint = encryptedChat.future_key_fingerprint;
                encryptedChat.auth_key = encryptedChat.future_auth_key;
                encryptedChat.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                encryptedChat.future_auth_key = new byte[256];
                encryptedChat.future_key_fingerprint = 0L;
                encryptedChat.key_use_count_in = 0;
                encryptedChat.key_use_count_out = 0;
                encryptedChat.exchange_id = 0L;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
            }
            if (obj instanceof TLRPC.TL_decryptedMessage) {
                final TLRPC.TL_decryptedMessage tl_decryptedMessage = (TLRPC.TL_decryptedMessage)obj;
                TLRPC.Message message;
                if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 17) {
                    message = new TLRPC.TL_message_secret();
                    message.ttl = tl_decryptedMessage.ttl;
                    message.entities = tl_decryptedMessage.entities;
                }
                else {
                    message = new TLRPC.TL_message();
                    message.ttl = encryptedChat.ttl;
                }
                message.message = tl_decryptedMessage.message;
                message.date = n;
                final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                message.id = newMessageId;
                message.local_id = newMessageId;
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                message.from_id = n2;
                message.to_id = new TLRPC.TL_peerUser();
                message.random_id = tl_decryptedMessage.random_id;
                message.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                message.unread = true;
                message.flags = 768;
                final String via_bot_name = tl_decryptedMessage.via_bot_name;
                if (via_bot_name != null && via_bot_name.length() > 0) {
                    message.via_bot_name = tl_decryptedMessage.via_bot_name;
                    message.flags |= 0x800;
                }
                final long grouped_id = tl_decryptedMessage.grouped_id;
                if (grouped_id != 0L) {
                    message.grouped_id = grouped_id;
                    message.flags |= 0x20000;
                }
                message.dialog_id = (long)encryptedChat.id << 32;
                final long reply_to_random_id = tl_decryptedMessage.reply_to_random_id;
                if (reply_to_random_id != 0L) {
                    message.reply_to_random_id = reply_to_random_id;
                    message.flags |= 0x8;
                }
                final TLRPC.DecryptedMessageMedia media = tl_decryptedMessage.media;
                Label_2852: {
                    if (media != null && !(media instanceof TLRPC.TL_decryptedMessageMediaEmpty)) {
                        if (media instanceof TLRPC.TL_decryptedMessageMediaWebPage) {
                            message.media = new TLRPC.TL_messageMediaWebPage();
                            message.media.webpage = new TLRPC.TL_webPageUrlPending();
                            message.media.webpage.url = tl_decryptedMessage.media.url;
                        }
                        else if (media instanceof TLRPC.TL_decryptedMessageMediaContact) {
                            message.media = new TLRPC.TL_messageMediaContact();
                            final TLRPC.MessageMedia media2 = message.media;
                            final TLRPC.DecryptedMessageMedia media3 = tl_decryptedMessage.media;
                            media2.last_name = media3.last_name;
                            media2.first_name = media3.first_name;
                            media2.phone_number = media3.phone_number;
                            media2.user_id = media3.user_id;
                            media2.vcard = "";
                        }
                        else if (media instanceof TLRPC.TL_decryptedMessageMediaGeoPoint) {
                            message.media = new TLRPC.TL_messageMediaGeo();
                            message.media.geo = new TLRPC.TL_geoPoint();
                            final TLRPC.GeoPoint geo = message.media.geo;
                            final TLRPC.DecryptedMessageMedia media4 = tl_decryptedMessage.media;
                            geo.lat = media4.lat;
                            geo._long = media4._long;
                        }
                        else {
                            if (media instanceof TLRPC.TL_decryptedMessageMediaPhoto) {
                                final byte[] key = media.key;
                                if (key != null && key.length == 32) {
                                    final byte[] iv = media.iv;
                                    if (iv != null) {
                                        if (iv.length == 32) {
                                            message.media = new TLRPC.TL_messageMediaPhoto();
                                            final TLRPC.MessageMedia media5 = message.media;
                                            media5.flags |= 0x3;
                                            String caption = tl_decryptedMessage.media.caption;
                                            if (caption == null) {
                                                caption = "";
                                            }
                                            message.message = caption;
                                            message.media.photo = new TLRPC.TL_photo();
                                            final TLRPC.Photo photo = message.media.photo;
                                            photo.file_reference = new byte[0];
                                            photo.date = message.date;
                                            final TLRPC.DecryptedMessageMedia media6 = tl_decryptedMessage.media;
                                            final byte[] thumb = ((TLRPC.TL_decryptedMessageMediaPhoto)media6).thumb;
                                            if (thumb != null && thumb.length != 0 && thumb.length <= 6000 && media6.thumb_w <= 100 && media6.thumb_h <= 100) {
                                                final TLRPC.TL_photoCachedSize e = new TLRPC.TL_photoCachedSize();
                                                final TLRPC.DecryptedMessageMedia media7 = tl_decryptedMessage.media;
                                                e.w = media7.thumb_w;
                                                e.h = media7.thumb_h;
                                                e.bytes = thumb;
                                                e.type = "s";
                                                e.location = new TLRPC.TL_fileLocationUnavailable();
                                                message.media.photo.sizes.add(e);
                                            }
                                            n = message.ttl;
                                            if (n != 0) {
                                                final TLRPC.MessageMedia media8 = message.media;
                                                media8.ttl_seconds = n;
                                                media8.flags |= 0x4;
                                            }
                                            final TLRPC.TL_photoSize e2 = new TLRPC.TL_photoSize();
                                            final TLRPC.DecryptedMessageMedia media9 = tl_decryptedMessage.media;
                                            e2.w = media9.w;
                                            e2.h = media9.h;
                                            e2.type = "x";
                                            e2.size = encryptedFile.size;
                                            e2.location = new TLRPC.TL_fileEncryptedLocation();
                                            final TLRPC.FileLocation location = e2.location;
                                            final TLRPC.DecryptedMessageMedia media10 = tl_decryptedMessage.media;
                                            location.key = media10.key;
                                            location.iv = media10.iv;
                                            location.dc_id = encryptedFile.dc_id;
                                            location.volume_id = encryptedFile.id;
                                            location.secret = encryptedFile.access_hash;
                                            location.local_id = encryptedFile.key_fingerprint;
                                            message.media.photo.sizes.add(e2);
                                            break Label_2852;
                                        }
                                    }
                                }
                                return null;
                            }
                            if (media instanceof TLRPC.TL_decryptedMessageMediaVideo) {
                                final byte[] key2 = media.key;
                                if (key2 != null && key2.length == 32) {
                                    final byte[] iv2 = media.iv;
                                    if (iv2 != null) {
                                        if (iv2.length == 32) {
                                            message.media = new TLRPC.TL_messageMediaDocument();
                                            final TLRPC.MessageMedia media11 = message.media;
                                            media11.flags |= 0x3;
                                            media11.document = new TLRPC.TL_documentEncrypted();
                                            final TLRPC.Document document = message.media.document;
                                            final TLRPC.DecryptedMessageMedia media12 = tl_decryptedMessage.media;
                                            document.key = media12.key;
                                            document.iv = media12.iv;
                                            document.dc_id = encryptedFile.dc_id;
                                            String caption2 = media12.caption;
                                            if (caption2 == null) {
                                                caption2 = "";
                                            }
                                            message.message = caption2;
                                            final TLRPC.Document document2 = message.media.document;
                                            document2.date = n;
                                            document2.size = encryptedFile.size;
                                            document2.id = encryptedFile.id;
                                            document2.access_hash = encryptedFile.access_hash;
                                            document2.mime_type = tl_decryptedMessage.media.mime_type;
                                            if (document2.mime_type == null) {
                                                document2.mime_type = "video/mp4";
                                            }
                                            final TLRPC.DecryptedMessageMedia media13 = tl_decryptedMessage.media;
                                            final byte[] thumb2 = ((TLRPC.TL_decryptedMessageMediaVideo)media13).thumb;
                                            TLRPC.PhotoSize e3;
                                            if (thumb2 != null && thumb2.length != 0 && thumb2.length <= 6000 && media13.thumb_w <= 100 && media13.thumb_h <= 100) {
                                                e3 = new TLRPC.TL_photoCachedSize();
                                                e3.bytes = thumb2;
                                                final TLRPC.DecryptedMessageMedia media14 = tl_decryptedMessage.media;
                                                e3.w = media14.thumb_w;
                                                e3.h = media14.thumb_h;
                                                e3.type = "s";
                                                e3.location = new TLRPC.TL_fileLocationUnavailable();
                                            }
                                            else {
                                                e3 = new TLRPC.TL_photoSizeEmpty();
                                                e3.type = "s";
                                            }
                                            message.media.document.thumbs.add(e3);
                                            final TLRPC.Document document3 = message.media.document;
                                            document3.flags |= 0x1;
                                            final TLRPC.TL_documentAttributeVideo e4 = new TLRPC.TL_documentAttributeVideo();
                                            final TLRPC.DecryptedMessageMedia media15 = tl_decryptedMessage.media;
                                            e4.w = media15.w;
                                            e4.h = media15.h;
                                            e4.duration = media15.duration;
                                            e4.supports_streaming = false;
                                            message.media.document.attributes.add(e4);
                                            n = message.ttl;
                                            if (n != 0) {
                                                final TLRPC.MessageMedia media16 = message.media;
                                                media16.ttl_seconds = n;
                                                media16.flags |= 0x4;
                                            }
                                            n = message.ttl;
                                            if (n != 0) {
                                                message.ttl = Math.max(tl_decryptedMessage.media.duration + 1, n);
                                            }
                                            break Label_2852;
                                        }
                                    }
                                }
                                return null;
                            }
                            if (media instanceof TLRPC.TL_decryptedMessageMediaDocument) {
                                final byte[] key3 = media.key;
                                if (key3 != null && key3.length == 32) {
                                    final byte[] iv3 = media.iv;
                                    if (iv3 != null) {
                                        if (iv3.length == 32) {
                                            message.media = new TLRPC.TL_messageMediaDocument();
                                            final TLRPC.MessageMedia media17 = message.media;
                                            media17.flags |= 0x3;
                                            String caption3 = tl_decryptedMessage.media.caption;
                                            if (caption3 == null) {
                                                caption3 = "";
                                            }
                                            message.message = caption3;
                                            message.media.document = new TLRPC.TL_documentEncrypted();
                                            final TLRPC.Document document4 = message.media.document;
                                            document4.id = encryptedFile.id;
                                            document4.access_hash = encryptedFile.access_hash;
                                            document4.date = n;
                                            final TLRPC.DecryptedMessageMedia media18 = tl_decryptedMessage.media;
                                            if (media18 instanceof TLRPC.TL_decryptedMessageMediaDocument_layer8) {
                                                final TLRPC.TL_documentAttributeFilename e5 = new TLRPC.TL_documentAttributeFilename();
                                                e5.file_name = tl_decryptedMessage.media.file_name;
                                                message.media.document.attributes.add(e5);
                                            }
                                            else {
                                                document4.attributes = media18.attributes;
                                            }
                                            final TLRPC.Document document5 = message.media.document;
                                            final TLRPC.DecryptedMessageMedia media19 = tl_decryptedMessage.media;
                                            document5.mime_type = media19.mime_type;
                                            n = media19.size;
                                            if (n != 0) {
                                                n = Math.min(n, encryptedFile.size);
                                            }
                                            else {
                                                n = encryptedFile.size;
                                            }
                                            document5.size = n;
                                            final TLRPC.Document document6 = message.media.document;
                                            final TLRPC.DecryptedMessageMedia media20 = tl_decryptedMessage.media;
                                            document6.key = media20.key;
                                            document6.iv = media20.iv;
                                            if (document6.mime_type == null) {
                                                document6.mime_type = "";
                                            }
                                            final TLRPC.DecryptedMessageMedia media21 = tl_decryptedMessage.media;
                                            final byte[] thumb3 = ((TLRPC.TL_decryptedMessageMediaDocument)media21).thumb;
                                            TLRPC.PhotoSize e6;
                                            if (thumb3 != null && thumb3.length != 0 && thumb3.length <= 6000 && media21.thumb_w <= 100 && media21.thumb_h <= 100) {
                                                e6 = new TLRPC.TL_photoCachedSize();
                                                e6.bytes = thumb3;
                                                final TLRPC.DecryptedMessageMedia media22 = tl_decryptedMessage.media;
                                                e6.w = media22.thumb_w;
                                                e6.h = media22.thumb_h;
                                                e6.type = "s";
                                                e6.location = new TLRPC.TL_fileLocationUnavailable();
                                            }
                                            else {
                                                e6 = new TLRPC.TL_photoSizeEmpty();
                                                e6.type = "s";
                                            }
                                            message.media.document.thumbs.add(e6);
                                            final TLRPC.Document document7 = message.media.document;
                                            document7.flags |= 0x1;
                                            document7.dc_id = encryptedFile.dc_id;
                                            if (MessageObject.isVoiceMessage(message) || MessageObject.isRoundVideoMessage(message)) {
                                                message.media_unread = true;
                                            }
                                            break Label_2852;
                                        }
                                    }
                                }
                                return null;
                            }
                            if (media instanceof TLRPC.TL_decryptedMessageMediaExternalDocument) {
                                message.media = new TLRPC.TL_messageMediaDocument();
                                final TLRPC.MessageMedia media23 = message.media;
                                media23.flags |= 0x3;
                                message.message = "";
                                media23.document = new TLRPC.TL_document();
                                final TLRPC.Document document8 = message.media.document;
                                final TLRPC.DecryptedMessageMedia media24 = tl_decryptedMessage.media;
                                document8.id = media24.id;
                                document8.access_hash = media24.access_hash;
                                document8.file_reference = new byte[0];
                                document8.date = media24.date;
                                document8.attributes = media24.attributes;
                                document8.mime_type = media24.mime_type;
                                document8.dc_id = media24.dc_id;
                                document8.size = media24.size;
                                document8.thumbs.add(((TLRPC.TL_decryptedMessageMediaExternalDocument)media24).thumb);
                                final TLRPC.Document document9 = message.media.document;
                                document9.flags |= 0x1;
                                if (document9.mime_type == null) {
                                    document9.mime_type = "";
                                }
                            }
                            else {
                                if (media instanceof TLRPC.TL_decryptedMessageMediaAudio) {
                                    final byte[] key4 = media.key;
                                    if (key4 != null && key4.length == 32) {
                                        final byte[] iv4 = media.iv;
                                        if (iv4 != null) {
                                            if (iv4.length == 32) {
                                                message.media = new TLRPC.TL_messageMediaDocument();
                                                final TLRPC.MessageMedia media25 = message.media;
                                                media25.flags |= 0x3;
                                                media25.document = new TLRPC.TL_documentEncrypted();
                                                final TLRPC.Document document10 = message.media.document;
                                                final TLRPC.DecryptedMessageMedia media26 = tl_decryptedMessage.media;
                                                document10.key = media26.key;
                                                document10.iv = media26.iv;
                                                document10.id = encryptedFile.id;
                                                document10.access_hash = encryptedFile.access_hash;
                                                document10.date = n;
                                                document10.size = encryptedFile.size;
                                                document10.dc_id = encryptedFile.dc_id;
                                                document10.mime_type = media26.mime_type;
                                                String caption4 = media26.caption;
                                                if (caption4 == null) {
                                                    caption4 = "";
                                                }
                                                message.message = caption4;
                                                final TLRPC.Document document11 = message.media.document;
                                                if (document11.mime_type == null) {
                                                    document11.mime_type = "audio/ogg";
                                                }
                                                final TLRPC.TL_documentAttributeAudio e7 = new TLRPC.TL_documentAttributeAudio();
                                                e7.duration = tl_decryptedMessage.media.duration;
                                                e7.voice = true;
                                                message.media.document.attributes.add(e7);
                                                n = message.ttl;
                                                if (n != 0) {
                                                    message.ttl = Math.max(tl_decryptedMessage.media.duration + 1, n);
                                                }
                                                if (message.media.document.thumbs.isEmpty()) {
                                                    final TLRPC.TL_photoSizeEmpty e8 = new TLRPC.TL_photoSizeEmpty();
                                                    e8.type = "s";
                                                    message.media.document.thumbs.add(e8);
                                                }
                                                break Label_2852;
                                            }
                                        }
                                    }
                                    return null;
                                }
                                if (!(media instanceof TLRPC.TL_decryptedMessageMediaVenue)) {
                                    return null;
                                }
                                message.media = new TLRPC.TL_messageMediaVenue();
                                message.media.geo = new TLRPC.TL_geoPoint();
                                final TLRPC.MessageMedia media27 = message.media;
                                final TLRPC.GeoPoint geo2 = media27.geo;
                                final TLRPC.DecryptedMessageMedia media28 = tl_decryptedMessage.media;
                                geo2.lat = media28.lat;
                                geo2._long = media28._long;
                                media27.title = media28.title;
                                media27.address = media28.address;
                                media27.provider = media28.provider;
                                media27.venue_id = media28.venue_id;
                                media27.venue_type = "";
                            }
                        }
                    }
                    else {
                        message.media = new TLRPC.TL_messageMediaEmpty();
                    }
                }
                n = message.ttl;
                if (n != 0) {
                    final TLRPC.MessageMedia media29 = message.media;
                    if (media29.ttl_seconds == 0) {
                        media29.ttl_seconds = n;
                        media29.flags |= 0x4;
                    }
                }
                return message;
            }
            if (obj instanceof TLRPC.TL_decryptedMessageService) {
                final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = (TLRPC.TL_decryptedMessageService)obj;
                final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
                if (action instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL || action instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                    final TLRPC.TL_messageService tl_messageService = new TLRPC.TL_messageService();
                    final TLRPC.DecryptedMessageAction action2 = tl_decryptedMessageService.action;
                    if (action2 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                        tl_messageService.action = new TLRPC.TL_messageEncryptedAction();
                        final int ttl_seconds = tl_decryptedMessageService.action.ttl_seconds;
                        if (ttl_seconds < 0 || ttl_seconds > 31536000) {
                            tl_decryptedMessageService.action.ttl_seconds = 31536000;
                        }
                        final TLRPC.DecryptedMessageAction action3 = tl_decryptedMessageService.action;
                        encryptedChat.ttl = action3.ttl_seconds;
                        tl_messageService.action.encryptedAction = action3;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatTTL(encryptedChat);
                    }
                    else if (action2 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                        tl_messageService.action = new TLRPC.TL_messageEncryptedAction();
                        tl_messageService.action.encryptedAction = tl_decryptedMessageService.action;
                    }
                    final int newMessageId2 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    tl_messageService.id = newMessageId2;
                    tl_messageService.local_id = newMessageId2;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    tl_messageService.unread = true;
                    tl_messageService.flags = 256;
                    tl_messageService.date = n;
                    tl_messageService.from_id = n2;
                    tl_messageService.to_id = new TLRPC.TL_peerUser();
                    tl_messageService.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    tl_messageService.dialog_id = (long)encryptedChat.id << 32;
                    return tl_messageService;
                }
                if (action instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$OJ7QWMtzGqvNXSkDUc1v2NRta0c(this, (long)encryptedChat.id << 32));
                    return null;
                }
                if (action instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
                    if (!action.random_ids.isEmpty()) {
                        this.pendingEncMessagesToDelete.addAll(tl_decryptedMessageService.action.random_ids);
                    }
                    return null;
                }
                if (action instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
                    if (!action.random_ids.isEmpty()) {
                        n = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                        MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(encryptedChat.id, n, n, 1, tl_decryptedMessageService.action.random_ids);
                    }
                }
                else if (action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                    this.applyPeerLayer(encryptedChat, action.layer);
                }
                else if (action instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                    final long exchange_id = encryptedChat.exchange_id;
                    if (exchange_id != 0L) {
                        if (exchange_id > action.exchange_id) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("we already have request key with higher exchange_id");
                            }
                            return null;
                        }
                        this.sendAbortKeyMessage(encryptedChat, null, exchange_id);
                    }
                    final byte[] magnitude = new byte[256];
                    Utilities.random.nextBytes(magnitude);
                    final BigInteger bigInteger = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                    final BigInteger modPow = BigInteger.valueOf(MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, magnitude), bigInteger);
                    final BigInteger bigInteger2 = new BigInteger(1, tl_decryptedMessageService.action.g_a);
                    if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                        this.sendAbortKeyMessage(encryptedChat, null, tl_decryptedMessageService.action.exchange_id);
                        return null;
                    }
                    byte[] byteArray;
                    final byte[] array = byteArray = modPow.toByteArray();
                    if (array.length > 256) {
                        byteArray = new byte[256];
                        System.arraycopy(array, 1, byteArray, 0, 256);
                    }
                    final byte[] byteArray2 = bigInteger2.modPow(new BigInteger(1, magnitude), bigInteger).toByteArray();
                    byte[] future_auth_key;
                    if (byteArray2.length > 256) {
                        future_auth_key = new byte[256];
                        System.arraycopy(byteArray2, byteArray2.length - 256, future_auth_key, 0, 256);
                    }
                    else if (byteArray2.length < 256) {
                        final byte[] array2 = new byte[256];
                        System.arraycopy(byteArray2, 0, array2, 256 - byteArray2.length, byteArray2.length);
                        n = 0;
                        while (true) {
                            future_auth_key = array2;
                            if (n >= 256 - byteArray2.length) {
                                break;
                            }
                            array2[n] = 0;
                            ++n;
                        }
                    }
                    else {
                        future_auth_key = byteArray2;
                    }
                    final byte[] computeSHA1 = Utilities.computeSHA1(future_auth_key);
                    final byte[] array3 = new byte[8];
                    System.arraycopy(computeSHA1, computeSHA1.length - 8, array3, 0, 8);
                    encryptedChat.exchange_id = tl_decryptedMessageService.action.exchange_id;
                    encryptedChat.future_auth_key = future_auth_key;
                    encryptedChat.future_key_fingerprint = Utilities.bytesToLong(array3);
                    encryptedChat.g_a_or_b = byteArray;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                    this.sendAcceptKeyMessage(encryptedChat, null);
                }
                else if (action instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                    if (encryptedChat.exchange_id == action.exchange_id) {
                        final BigInteger m = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                        final BigInteger bigInteger3 = new BigInteger(1, tl_decryptedMessageService.action.g_b);
                        if (!Utilities.isGoodGaAndGb(bigInteger3, m)) {
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                            this.sendAbortKeyMessage(encryptedChat, null, tl_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        final byte[] byteArray3 = bigInteger3.modPow(new BigInteger(1, encryptedChat.a_or_b), m).toByteArray();
                        byte[] future_auth_key2;
                        if (byteArray3.length > 256) {
                            future_auth_key2 = new byte[256];
                            System.arraycopy(byteArray3, byteArray3.length - 256, future_auth_key2, 0, 256);
                        }
                        else if (byteArray3.length < 256) {
                            final byte[] array4 = new byte[256];
                            System.arraycopy(byteArray3, 0, array4, 256 - byteArray3.length, byteArray3.length);
                            n = 0;
                            while (true) {
                                future_auth_key2 = array4;
                                if (n >= 256 - byteArray3.length) {
                                    break;
                                }
                                array4[n] = 0;
                                ++n;
                            }
                        }
                        else {
                            future_auth_key2 = byteArray3;
                        }
                        final byte[] computeSHA2 = Utilities.computeSHA1(future_auth_key2);
                        final byte[] array5 = new byte[8];
                        System.arraycopy(computeSHA2, computeSHA2.length - 8, array5, 0, 8);
                        final long bytesToLong = Utilities.bytesToLong(array5);
                        if (tl_decryptedMessageService.action.key_fingerprint == bytesToLong) {
                            encryptedChat.future_auth_key = future_auth_key2;
                            encryptedChat.future_key_fingerprint = bytesToLong;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                            this.sendCommitKeyMessage(encryptedChat, null);
                        }
                        else {
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                            this.sendAbortKeyMessage(encryptedChat, null, tl_decryptedMessageService.action.exchange_id);
                        }
                    }
                    else {
                        encryptedChat.future_auth_key = new byte[256];
                        encryptedChat.future_key_fingerprint = 0L;
                        encryptedChat.exchange_id = 0L;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                        this.sendAbortKeyMessage(encryptedChat, null, tl_decryptedMessageService.action.exchange_id);
                    }
                }
                else if (action instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                    if (encryptedChat.exchange_id == action.exchange_id) {
                        final long future_key_fingerprint = encryptedChat.future_key_fingerprint;
                        if (future_key_fingerprint == action.key_fingerprint) {
                            final long key_fingerprint = encryptedChat.key_fingerprint;
                            final byte[] auth_key = encryptedChat.auth_key;
                            encryptedChat.key_fingerprint = future_key_fingerprint;
                            encryptedChat.auth_key = encryptedChat.future_auth_key;
                            encryptedChat.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                            encryptedChat.future_auth_key = auth_key;
                            encryptedChat.future_key_fingerprint = key_fingerprint;
                            encryptedChat.key_use_count_in = 0;
                            encryptedChat.key_use_count_out = 0;
                            encryptedChat.exchange_id = 0L;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                            this.sendNoopMessage(encryptedChat, null);
                            return null;
                        }
                    }
                    encryptedChat.future_auth_key = new byte[256];
                    encryptedChat.future_key_fingerprint = 0L;
                    encryptedChat.exchange_id = 0L;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                    this.sendAbortKeyMessage(encryptedChat, null, tl_decryptedMessageService.action.exchange_id);
                }
                else if (action instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                    if (encryptedChat.exchange_id == action.exchange_id) {
                        encryptedChat.future_auth_key = new byte[256];
                        encryptedChat.future_key_fingerprint = 0L;
                        encryptedChat.exchange_id = 0L;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                    }
                }
                else if (!(action instanceof TLRPC.TL_decryptedMessageActionNoop)) {
                    if (action instanceof TLRPC.TL_decryptedMessageActionResend) {
                        n = action.end_seq_no;
                        final int in_seq_no = encryptedChat.in_seq_no;
                        if (n >= in_seq_no) {
                            final int start_seq_no = action.start_seq_no;
                            if (n >= start_seq_no) {
                                if (start_seq_no < in_seq_no) {
                                    action.start_seq_no = in_seq_no;
                                }
                                final TLRPC.DecryptedMessageAction action4 = tl_decryptedMessageService.action;
                                this.resendMessages(action4.start_seq_no, action4.end_seq_no, encryptedChat);
                                return null;
                            }
                        }
                        return null;
                    }
                    return null;
                }
            }
            else if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown message ");
                sb.append(obj);
                FileLog.e(sb.toString());
            }
        }
        else if (BuildVars.LOGS_ENABLED) {
            FileLog.e("unknown TLObject");
        }
        return null;
    }
    
    protected void processPendingEncMessages() {
        if (!this.pendingEncMessagesToDelete.isEmpty()) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU(this, new ArrayList((Collection<? extends E>)this.pendingEncMessagesToDelete)));
            MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeletedByRandoms(new ArrayList<Long>(this.pendingEncMessagesToDelete));
            this.pendingEncMessagesToDelete.clear();
        }
    }
    
    protected void processUpdateEncryption(final TLRPC.TL_updateEncryption e, final ConcurrentHashMap<Integer, TLRPC.User> concurrentHashMap) {
        final TLRPC.EncryptedChat chat = e.chat;
        final long n = chat.id;
        final TLRPC.EncryptedChat encryptedChatDB = MessagesController.getInstance(this.currentAccount).getEncryptedChatDB(chat.id, false);
        if (chat instanceof TLRPC.TL_encryptedChatRequested && encryptedChatDB == null) {
            int user_id;
            if ((user_id = chat.participant_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                user_id = chat.admin_id;
            }
            TLRPC.User user;
            if ((user = MessagesController.getInstance(this.currentAccount).getUser(user_id)) == null) {
                user = concurrentHashMap.get(user_id);
            }
            chat.user_id = user_id;
            final TLRPC.TL_dialog tl_dialog = new TLRPC.TL_dialog();
            tl_dialog.id = n << 32;
            tl_dialog.unread_count = 0;
            tl_dialog.top_message = 0;
            tl_dialog.last_message_date = e.date;
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(chat, false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$IkT_sRYXQIirGnmDeh6b889eh_A(this, tl_dialog));
            MessagesStorage.getInstance(this.currentAccount).putEncryptedChat(chat, user, tl_dialog);
            this.acceptSecretChat(chat);
        }
        else if (chat instanceof TLRPC.TL_encryptedChat) {
            if (encryptedChatDB instanceof TLRPC.TL_encryptedChatWaiting) {
                final byte[] auth_key = encryptedChatDB.auth_key;
                if (auth_key == null || auth_key.length == 1) {
                    chat.a_or_b = encryptedChatDB.a_or_b;
                    chat.user_id = encryptedChatDB.user_id;
                    this.processAcceptedSecretChat(chat);
                    return;
                }
            }
            if (encryptedChatDB == null && this.startingSecretChat) {
                this.delayedEncryptedChatUpdates.add(e);
            }
        }
        else {
            if (encryptedChatDB != null) {
                chat.user_id = encryptedChatDB.user_id;
                chat.auth_key = encryptedChatDB.auth_key;
                chat.key_create_date = encryptedChatDB.key_create_date;
                chat.key_use_count_in = encryptedChatDB.key_use_count_in;
                chat.key_use_count_out = encryptedChatDB.key_use_count_out;
                chat.ttl = encryptedChatDB.ttl;
                chat.seq_in = encryptedChatDB.seq_in;
                chat.seq_out = encryptedChatDB.seq_out;
                chat.admin_id = encryptedChatDB.admin_id;
                chat.mtproto_seq = encryptedChatDB.mtproto_seq;
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$G9V6FvkI_PnA0UumuoB_kxH2lOM(this, encryptedChatDB, chat));
        }
    }
    
    public void requestNewSecretChatKey(final TLRPC.EncryptedChat encryptedChat) {
        if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) < 20) {
            return;
        }
        final byte[] a_or_b = new byte[256];
        Utilities.random.nextBytes(a_or_b);
        byte[] byteArray;
        final byte[] array = byteArray = BigInteger.valueOf(MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, a_or_b), new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes())).toByteArray();
        if (array.length > 256) {
            byteArray = new byte[256];
            System.arraycopy(array, 1, byteArray, 0, 256);
        }
        encryptedChat.exchange_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
        encryptedChat.a_or_b = a_or_b;
        encryptedChat.g_a = byteArray;
        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
        this.sendRequestKeyMessage(encryptedChat, null);
    }
    
    public void sendAbortKeyMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage, final long exchange_id) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionAbortKey();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.exchange_id = exchange_id;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendAcceptKeyMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionAcceptKey();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.exchange_id = encryptedChat.exchange_id;
            action.key_fingerprint = encryptedChat.future_key_fingerprint;
            action.g_b = encryptedChat.g_a_or_b;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendClearHistoryMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionFlushHistory();
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, tl_decryptedMessageService.action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendCommitKeyMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionCommitKey();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.exchange_id = encryptedChat.exchange_id;
            action.key_fingerprint = encryptedChat.future_key_fingerprint;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendMessagesDeleteMessage(final TLRPC.EncryptedChat encryptedChat, final ArrayList<Long> random_ids, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionDeleteMessages();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.random_ids = random_ids;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendMessagesReadMessage(final TLRPC.EncryptedChat encryptedChat, final ArrayList<Long> random_ids, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionReadMessages();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.random_ids = random_ids;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendNoopMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionNoop();
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, tl_decryptedMessageService.action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendNotifyLayerMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        if (this.sendingNotifyLayer.contains(encryptedChat.id)) {
            return;
        }
        this.sendingNotifyLayer.add(encryptedChat.id);
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionNotifyLayer();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.layer = 73;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendRequestKeyMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionRequestKey();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.exchange_id = encryptedChat.exchange_id;
            action.g_a = encryptedChat.g_a;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendScreenshotMessage(final TLRPC.EncryptedChat encryptedChat, final ArrayList<Long> random_ids, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionScreenshotMessages();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.random_ids = random_ids;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
            final MessageObject e = new MessageObject(this.currentAccount, serviceSecretMessage, false);
            e.messageOwner.send_state = 1;
            final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
            list.add(e);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(serviceSecretMessage.dialog_id, list);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void sendTTLMessage(final TLRPC.EncryptedChat encryptedChat, TLRPC.Message serviceSecretMessage) {
        if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
            return;
        }
        final TLRPC.TL_decryptedMessageService tl_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
        if (serviceSecretMessage != null) {
            tl_decryptedMessageService.action = serviceSecretMessage.action.encryptedAction;
        }
        else {
            tl_decryptedMessageService.action = new TLRPC.TL_decryptedMessageActionSetMessageTTL();
            final TLRPC.DecryptedMessageAction action = tl_decryptedMessageService.action;
            action.ttl_seconds = encryptedChat.ttl;
            serviceSecretMessage = this.createServiceSecretMessage(encryptedChat, action);
            final MessageObject e = new MessageObject(this.currentAccount, serviceSecretMessage, false);
            e.messageOwner.send_state = 1;
            final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
            list.add(e);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(serviceSecretMessage.dialog_id, list);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        tl_decryptedMessageService.random_id = serviceSecretMessage.random_id;
        this.performSendEncryptedRequest(tl_decryptedMessageService, serviceSecretMessage, encryptedChat, null, null, null);
    }
    
    public void startSecretChat(final Context context, final TLRPC.User user) {
        if (user == null) {
            return;
        }
        if (context == null) {
            return;
        }
        this.startingSecretChat = true;
        final AlertDialog alertDialog = new AlertDialog(context, 3);
        final TLRPC.TL_messages_getDhConfig tl_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tl_messages_getDhConfig.random_length = 256;
        tl_messages_getDhConfig.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
        alertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$SecretChatHelper$FOvPRdYvHRkFV5NyZdXuhMMalnM(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getDhConfig, new _$$Lambda$SecretChatHelper$CyJGexOzIKpecJNn32s1dWnqMhg(this, context, alertDialog, user), 2)));
        try {
            alertDialog.show();
        }
        catch (Exception ex) {}
    }
    
    public static class TL_decryptedMessageHolder extends TLObject
    {
        public static int constructor = 1431655929;
        public int date;
        public int decryptedWithVersion;
        public TLRPC.EncryptedFile file;
        public TLRPC.TL_decryptedMessageLayer layer;
        public boolean new_key_used;
        
        @Override
        public void readParams(final AbstractSerializedData abstractSerializedData, final boolean b) {
            abstractSerializedData.readInt64(b);
            this.date = abstractSerializedData.readInt32(b);
            this.layer = TLRPC.TL_decryptedMessageLayer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(b), b);
            if (abstractSerializedData.readBool(b)) {
                this.file = TLRPC.EncryptedFile.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(b), b);
            }
            this.new_key_used = abstractSerializedData.readBool(b);
        }
        
        @Override
        public void serializeToStream(final AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeInt32(TL_decryptedMessageHolder.constructor);
            abstractSerializedData.writeInt64(0L);
            abstractSerializedData.writeInt32(this.date);
            this.layer.serializeToStream(abstractSerializedData);
            abstractSerializedData.writeBool(this.file != null);
            final TLRPC.EncryptedFile file = this.file;
            if (file != null) {
                file.serializeToStream(abstractSerializedData);
            }
            abstractSerializedData.writeBool(this.new_key_used);
        }
    }
}
