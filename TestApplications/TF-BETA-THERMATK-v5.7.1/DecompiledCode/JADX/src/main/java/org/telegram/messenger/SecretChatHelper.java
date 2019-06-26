package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.LongSparseArray;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.DecryptedMessage;
import org.telegram.tgnet.TLRPC.DecryptedMessageAction;
import org.telegram.tgnet.TLRPC.DecryptedMessageMedia;
import org.telegram.tgnet.TLRPC.Dialog;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.EncryptedFile;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.GeoPoint;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageAction;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_decryptedMessage;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionAbortKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionAcceptKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionCommitKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionDeleteMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionFlushHistory;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionNoop;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionNotifyLayer;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionReadMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionRequestKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionResend;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageLayer;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaAudio;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaContact;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument_layer8;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaExternalDocument;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaGeoPoint;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVideo;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageService;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_documentEncrypted;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;
import org.telegram.tgnet.TLRPC.TL_encryptedChatRequested;
import org.telegram.tgnet.TLRPC.TL_encryptedChatWaiting;
import org.telegram.tgnet.TLRPC.TL_encryptedFile;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_fileEncryptedLocation;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_geoPoint;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedChat;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_message_secret;
import org.telegram.tgnet.TLRPC.TL_messages_acceptEncryption;
import org.telegram.tgnet.TLRPC.TL_messages_dhConfig;
import org.telegram.tgnet.TLRPC.TL_messages_discardEncryption;
import org.telegram.tgnet.TLRPC.TL_messages_getDhConfig;
import org.telegram.tgnet.TLRPC.TL_messages_requestEncryption;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncrypted;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedFile;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedMultiMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedService;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_photoSize;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_updateEncryption;
import org.telegram.tgnet.TLRPC.TL_webPageUrlPending;
import org.telegram.tgnet.TLRPC.Update;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.messages_DhConfig;
import org.telegram.tgnet.TLRPC.messages_SentEncryptedMessage;

public class SecretChatHelper {
    public static final int CURRENT_SECRET_CHAT_LAYER = 73;
    private static volatile SecretChatHelper[] Instance = new SecretChatHelper[3];
    private SparseArray<EncryptedChat> acceptingChats = new SparseArray();
    private int currentAccount;
    public ArrayList<Update> delayedEncryptedChatUpdates = new ArrayList();
    private ArrayList<Long> pendingEncMessagesToDelete = new ArrayList();
    private SparseArray<ArrayList<TL_decryptedMessageHolder>> secretHolesQueue = new SparseArray();
    private ArrayList<Integer> sendingNotifyLayer = new ArrayList();
    private boolean startingSecretChat = false;

    public static class TL_decryptedMessageHolder extends TLObject {
        public static int constructor = 1431655929;
        public int date;
        public int decryptedWithVersion;
        public EncryptedFile file;
        public TL_decryptedMessageLayer layer;
        public boolean new_key_used;

        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
            abstractSerializedData.readInt64(z);
            this.date = abstractSerializedData.readInt32(z);
            this.layer = TL_decryptedMessageLayer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (abstractSerializedData.readBool(z)) {
                this.file = EncryptedFile.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            }
            this.new_key_used = abstractSerializedData.readBool(z);
        }

        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeInt32(constructor);
            abstractSerializedData.writeInt64(0);
            abstractSerializedData.writeInt32(this.date);
            this.layer.serializeToStream(abstractSerializedData);
            abstractSerializedData.writeBool(this.file != null);
            EncryptedFile encryptedFile = this.file;
            if (encryptedFile != null) {
                encryptedFile.serializeToStream(abstractSerializedData);
            }
            abstractSerializedData.writeBool(this.new_key_used);
        }
    }

    static /* synthetic */ void lambda$declineSecretChat$19(TLObject tLObject, TL_error tL_error) {
    }

    public static SecretChatHelper getInstance(int i) {
        SecretChatHelper secretChatHelper = Instance[i];
        if (secretChatHelper == null) {
            synchronized (SecretChatHelper.class) {
                secretChatHelper = Instance[i];
                if (secretChatHelper == null) {
                    SecretChatHelper[] secretChatHelperArr = Instance;
                    SecretChatHelper secretChatHelper2 = new SecretChatHelper(i);
                    secretChatHelperArr[i] = secretChatHelper2;
                    secretChatHelper = secretChatHelper2;
                }
            }
        }
        return secretChatHelper;
    }

    public SecretChatHelper(int i) {
        this.currentAccount = i;
    }

    public void cleanup() {
        this.sendingNotifyLayer.clear();
        this.acceptingChats.clear();
        this.secretHolesQueue.clear();
        this.delayedEncryptedChatUpdates.clear();
        this.pendingEncMessagesToDelete.clear();
        this.startingSecretChat = false;
    }

    /* Access modifiers changed, original: protected */
    public void processPendingEncMessages() {
        if (!this.pendingEncMessagesToDelete.isEmpty()) {
            AndroidUtilities.runOnUIThread(new C0947-$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU(this, new ArrayList(this.pendingEncMessagesToDelete)));
            MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeletedByRandoms(new ArrayList(this.pendingEncMessagesToDelete));
            this.pendingEncMessagesToDelete.clear();
        }
    }

    public /* synthetic */ void lambda$processPendingEncMessages$0$SecretChatHelper(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = (MessageObject) MessagesController.getInstance(this.currentAccount).dialogMessagesByRandomIds.get(((Long) arrayList.get(i)).longValue());
            if (messageObject != null) {
                messageObject.deleted = true;
            }
        }
    }

    private TL_messageService createServiceSecretMessage(EncryptedChat encryptedChat, DecryptedMessageAction decryptedMessageAction) {
        TL_messageService tL_messageService = new TL_messageService();
        tL_messageService.action = new TL_messageEncryptedAction();
        tL_messageService.action.encryptedAction = decryptedMessageAction;
        int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
        tL_messageService.f461id = newMessageId;
        tL_messageService.local_id = newMessageId;
        tL_messageService.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
        tL_messageService.unread = true;
        tL_messageService.out = true;
        tL_messageService.flags = 256;
        tL_messageService.dialog_id = ((long) encryptedChat.f445id) << 32;
        tL_messageService.to_id = new TL_peerUser();
        tL_messageService.send_state = 1;
        if (encryptedChat.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            tL_messageService.to_id.user_id = encryptedChat.admin_id;
        } else {
            tL_messageService.to_id.user_id = encryptedChat.participant_id;
        }
        if ((decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL)) {
            tL_messageService.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        } else {
            tL_messageService.date = 0;
        }
        tL_messageService.random_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(tL_messageService);
        MessagesStorage.getInstance(this.currentAccount).putMessages(arrayList, false, true, true, 0);
        return tL_messageService;
    }

    public void sendMessagesReadMessage(EncryptedChat encryptedChat, ArrayList<Long> arrayList, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionReadMessages();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.random_ids = arrayList;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    /* Access modifiers changed, original: protected */
    public void processUpdateEncryption(TL_updateEncryption tL_updateEncryption, ConcurrentHashMap<Integer, User> concurrentHashMap) {
        EncryptedChat encryptedChat = tL_updateEncryption.chat;
        long j = ((long) encryptedChat.f445id) << 32;
        EncryptedChat encryptedChatDB = MessagesController.getInstance(this.currentAccount).getEncryptedChatDB(encryptedChat.f445id, false);
        if ((encryptedChat instanceof TL_encryptedChatRequested) && encryptedChatDB == null) {
            int i = encryptedChat.participant_id;
            if (i == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                i = encryptedChat.admin_id;
            }
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(i));
            if (user == null) {
                user = (User) concurrentHashMap.get(Integer.valueOf(i));
            }
            encryptedChat.user_id = i;
            TL_dialog tL_dialog = new TL_dialog();
            tL_dialog.f440id = j;
            tL_dialog.unread_count = 0;
            tL_dialog.top_message = 0;
            tL_dialog.last_message_date = tL_updateEncryption.date;
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, false);
            AndroidUtilities.runOnUIThread(new C0937-$$Lambda$SecretChatHelper$IkT-sRYXQIirGnmDeh6b889eh-A(this, tL_dialog));
            MessagesStorage.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, user, tL_dialog);
            acceptSecretChat(encryptedChat);
        } else if (encryptedChat instanceof TL_encryptedChat) {
            if (encryptedChatDB instanceof TL_encryptedChatWaiting) {
                byte[] bArr = encryptedChatDB.auth_key;
                if (bArr == null || bArr.length == 1) {
                    encryptedChat.a_or_b = encryptedChatDB.a_or_b;
                    encryptedChat.user_id = encryptedChatDB.user_id;
                    processAcceptedSecretChat(encryptedChat);
                    return;
                }
            }
            if (encryptedChatDB == null && this.startingSecretChat) {
                this.delayedEncryptedChatUpdates.add(tL_updateEncryption);
            }
        } else {
            if (encryptedChatDB != null) {
                encryptedChat.user_id = encryptedChatDB.user_id;
                encryptedChat.auth_key = encryptedChatDB.auth_key;
                encryptedChat.key_create_date = encryptedChatDB.key_create_date;
                encryptedChat.key_use_count_in = encryptedChatDB.key_use_count_in;
                encryptedChat.key_use_count_out = encryptedChatDB.key_use_count_out;
                encryptedChat.ttl = encryptedChatDB.ttl;
                encryptedChat.seq_in = encryptedChatDB.seq_in;
                encryptedChat.seq_out = encryptedChatDB.seq_out;
                encryptedChat.admin_id = encryptedChatDB.admin_id;
                encryptedChat.mtproto_seq = encryptedChatDB.mtproto_seq;
            }
            AndroidUtilities.runOnUIThread(new C0936-$$Lambda$SecretChatHelper$G9V6FvkI-PnA0UumuoB_kxH2lOM(this, encryptedChatDB, encryptedChat));
        }
    }

    public /* synthetic */ void lambda$processUpdateEncryption$1$SecretChatHelper(Dialog dialog) {
        MessagesController.getInstance(this.currentAccount).dialogs_dict.put(dialog.f440id, dialog);
        MessagesController.getInstance(this.currentAccount).allDialogs.add(dialog);
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }

    public /* synthetic */ void lambda$processUpdateEncryption$2$SecretChatHelper(EncryptedChat encryptedChat, EncryptedChat encryptedChat2) {
        if (encryptedChat != null) {
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat2, false);
        }
        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, encryptedChat2);
    }

    public void sendMessagesDeleteMessage(EncryptedChat encryptedChat, ArrayList<Long> arrayList, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionDeleteMessages();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.random_ids = arrayList;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendClearHistoryMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionFlushHistory();
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageService.action);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendNotifyLayerMessage(EncryptedChat encryptedChat, Message message) {
        if ((encryptedChat instanceof TL_encryptedChat) && !this.sendingNotifyLayer.contains(Integer.valueOf(encryptedChat.f445id))) {
            this.sendingNotifyLayer.add(Integer.valueOf(encryptedChat.f445id));
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionNotifyLayer();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.layer = 73;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendRequestKeyMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionRequestKey();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.exchange_id = encryptedChat.exchange_id;
                decryptedMessageAction.g_a = encryptedChat.g_a;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendAcceptKeyMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionAcceptKey();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.exchange_id = encryptedChat.exchange_id;
                decryptedMessageAction.key_fingerprint = encryptedChat.future_key_fingerprint;
                decryptedMessageAction.g_b = encryptedChat.g_a_or_b;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendCommitKeyMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionCommitKey();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.exchange_id = encryptedChat.exchange_id;
                decryptedMessageAction.key_fingerprint = encryptedChat.future_key_fingerprint;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendAbortKeyMessage(EncryptedChat encryptedChat, Message message, long j) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionAbortKey();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.exchange_id = j;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendNoopMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionNoop();
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageService.action);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendTTLMessage(EncryptedChat encryptedChat, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionSetMessageTTL();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.ttl_seconds = encryptedChat.ttl;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
                MessageObject messageObject = new MessageObject(this.currentAccount, message, false);
                messageObject.messageOwner.send_state = 1;
                ArrayList arrayList = new ArrayList();
                arrayList.add(messageObject);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(message.dialog_id, arrayList);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendScreenshotMessage(EncryptedChat encryptedChat, ArrayList<Long> arrayList, Message message) {
        if (encryptedChat instanceof TL_encryptedChat) {
            TL_decryptedMessageService tL_decryptedMessageService = new TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                tL_decryptedMessageService.action = new TL_decryptedMessageActionScreenshotMessages();
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                decryptedMessageAction.random_ids = arrayList;
                message = createServiceSecretMessage(encryptedChat, decryptedMessageAction);
                MessageObject messageObject = new MessageObject(this.currentAccount, message, false);
                messageObject.messageOwner.send_state = 1;
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(messageObject);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(message.dialog_id, arrayList2);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    private void updateMediaPaths(MessageObject messageObject, EncryptedFile encryptedFile, DecryptedMessage decryptedMessage, String str) {
        Message message = messageObject.messageOwner;
        if (encryptedFile != null) {
            DecryptedMessageMedia decryptedMessageMedia;
            String stringBuilder;
            MessageMedia messageMedia = message.media;
            if (messageMedia instanceof TL_messageMediaPhoto) {
                Photo photo = messageMedia.photo;
                if (photo != null) {
                    ArrayList arrayList = photo.sizes;
                    PhotoSize photoSize = (PhotoSize) arrayList.get(arrayList.size() - 1);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(photoSize.location.volume_id);
                    String str2 = "_";
                    stringBuilder2.append(str2);
                    stringBuilder2.append(photoSize.location.local_id);
                    String stringBuilder3 = stringBuilder2.toString();
                    photoSize.location = new TL_fileEncryptedLocation();
                    FileLocation fileLocation = photoSize.location;
                    decryptedMessageMedia = decryptedMessage.media;
                    fileLocation.key = decryptedMessageMedia.key;
                    fileLocation.f447iv = decryptedMessageMedia.f438iv;
                    fileLocation.dc_id = encryptedFile.dc_id;
                    fileLocation.volume_id = encryptedFile.f446id;
                    fileLocation.secret = encryptedFile.access_hash;
                    fileLocation.local_id = encryptedFile.key_fingerprint;
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(photoSize.location.volume_id);
                    stringBuilder4.append(str2);
                    stringBuilder4.append(photoSize.location.local_id);
                    stringBuilder = stringBuilder4.toString();
                    File directory = FileLoader.getDirectory(4);
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(stringBuilder3);
                    stringBuilder5.append(".jpg");
                    new File(directory, stringBuilder5.toString()).renameTo(FileLoader.getPathToAttach(photoSize));
                    ImageLoader.getInstance().replaceImageInCache(stringBuilder3, stringBuilder, ImageLocation.getForPhoto(photoSize, message.media.photo), true);
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(message);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(arrayList2, false, true, false, 0);
                    return;
                }
            }
            messageMedia = message.media;
            if (messageMedia instanceof TL_messageMediaDocument) {
                Document document = messageMedia.document;
                if (document != null) {
                    messageMedia.document = new TL_documentEncrypted();
                    Document document2 = message.media.document;
                    document2.f441id = encryptedFile.f446id;
                    document2.access_hash = encryptedFile.access_hash;
                    document2.date = document.date;
                    document2.attributes = document.attributes;
                    document2.mime_type = document.mime_type;
                    document2.size = encryptedFile.size;
                    decryptedMessageMedia = decryptedMessage.media;
                    document2.key = decryptedMessageMedia.key;
                    document2.f442iv = decryptedMessageMedia.f438iv;
                    document2.thumbs = document.thumbs;
                    document2.dc_id = encryptedFile.dc_id;
                    if (document2.thumbs.isEmpty()) {
                        TL_photoSizeEmpty tL_photoSizeEmpty = new TL_photoSizeEmpty();
                        tL_photoSizeEmpty.type = "s";
                        message.media.document.thumbs.add(tL_photoSizeEmpty);
                    }
                    stringBuilder = message.attachPath;
                    if (stringBuilder != null && stringBuilder.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && new File(message.attachPath).renameTo(FileLoader.getPathToAttach(message.media.document))) {
                        messageObject.mediaExists = messageObject.attachPathExists;
                        messageObject.attachPathExists = false;
                        message.attachPath = "";
                    }
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(message);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(arrayList3, false, true, false, 0);
                }
            }
        }
    }

    public static boolean isSecretVisibleMessage(Message message) {
        MessageAction messageAction = message.action;
        if (messageAction instanceof TL_messageEncryptedAction) {
            DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
            if ((decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSecretInvisibleMessage(Message message) {
        MessageAction messageAction = message.action;
        if (messageAction instanceof TL_messageEncryptedAction) {
            DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
            if (!((decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL))) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void performSendEncryptedRequest(TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia, DelayedMessage delayedMessage) {
        for (int i = 0; i < tL_messages_sendEncryptedMultiMedia.files.size(); i++) {
            performSendEncryptedRequest((DecryptedMessage) tL_messages_sendEncryptedMultiMedia.messages.get(i), (Message) delayedMessage.messages.get(i), delayedMessage.encryptedChat, (InputEncryptedFile) tL_messages_sendEncryptedMultiMedia.files.get(i), (String) delayedMessage.originalPaths.get(i), (MessageObject) delayedMessage.messageObjects.get(i));
        }
    }

    /* Access modifiers changed, original: protected */
    public void performSendEncryptedRequest(DecryptedMessage decryptedMessage, Message message, EncryptedChat encryptedChat, InputEncryptedFile inputEncryptedFile, String str, MessageObject messageObject) {
        EncryptedChat encryptedChat2 = encryptedChat;
        if (decryptedMessage == null || encryptedChat2.auth_key == null || (encryptedChat2 instanceof TL_encryptedChatRequested) || (encryptedChat2 instanceof TL_encryptedChatWaiting)) {
            return;
        }
        Message message2 = message;
        SendMessagesHelper.getInstance(this.currentAccount).putToSendingMessages(message);
        Utilities.stageQueue.postRunnable(new C0949-$$Lambda$SecretChatHelper$go4ClJO8kMeuzFvRQpPn8-EmO40(this, encryptedChat, decryptedMessage, message2, inputEncryptedFile, messageObject, str));
    }

    public /* synthetic */ void lambda$performSendEncryptedRequest$7$SecretChatHelper(EncryptedChat encryptedChat, DecryptedMessage decryptedMessage, Message message, InputEncryptedFile inputEncryptedFile, MessageObject messageObject, String str) {
        EncryptedChat encryptedChat2 = encryptedChat;
        DecryptedMessage decryptedMessage2 = decryptedMessage;
        Message message2 = message;
        InputEncryptedFile inputEncryptedFile2 = inputEncryptedFile;
        try {
            byte[] bArr;
            TLObject tLObject;
            TL_decryptedMessageLayer tL_decryptedMessageLayer = new TL_decryptedMessageLayer();
            tL_decryptedMessageLayer.layer = Math.min(Math.max(46, AndroidUtilities.getMyLayerVersion(encryptedChat2.layer)), Math.max(46, AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer)));
            tL_decryptedMessageLayer.message = decryptedMessage2;
            tL_decryptedMessageLayer.random_bytes = new byte[15];
            Utilities.random.nextBytes(tL_decryptedMessageLayer.random_bytes);
            boolean z = true;
            int i = AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer) >= 73 ? 2 : 1;
            if (encryptedChat2.seq_in == 0 && encryptedChat2.seq_out == 0) {
                if (encryptedChat2.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    encryptedChat2.seq_out = 1;
                    encryptedChat2.seq_in = -2;
                } else {
                    encryptedChat2.seq_in = -1;
                }
            }
            if (message2.seq_in == 0 && message2.seq_out == 0) {
                tL_decryptedMessageLayer.in_seq_no = encryptedChat2.seq_in > 0 ? encryptedChat2.seq_in : encryptedChat2.seq_in + 2;
                tL_decryptedMessageLayer.out_seq_no = encryptedChat2.seq_out;
                encryptedChat2.seq_out += 2;
                if (AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer) >= 20) {
                    if (encryptedChat2.key_create_date == 0) {
                        encryptedChat2.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    }
                    encryptedChat2.key_use_count_out = (short) (encryptedChat2.key_use_count_out + 1);
                    if ((encryptedChat2.key_use_count_out >= (short) 100 || encryptedChat2.key_create_date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - 604800) && encryptedChat2.exchange_id == 0 && encryptedChat2.future_key_fingerprint == 0) {
                        requestNewSecretChatKey(encryptedChat);
                    }
                }
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(encryptedChat2, false);
                if (message2 != null) {
                    message2.seq_in = tL_decryptedMessageLayer.in_seq_no;
                    message2.seq_out = tL_decryptedMessageLayer.out_seq_no;
                    MessagesStorage.getInstance(this.currentAccount).setMessageSeq(message2.f461id, message2.seq_in, message2.seq_out);
                }
            } else {
                tL_decryptedMessageLayer.in_seq_no = message2.seq_in;
                tL_decryptedMessageLayer.out_seq_no = message2.seq_out;
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(decryptedMessage2);
                stringBuilder.append(" send message with in_seq = ");
                stringBuilder.append(tL_decryptedMessageLayer.in_seq_no);
                stringBuilder.append(" out_seq = ");
                stringBuilder.append(tL_decryptedMessageLayer.out_seq_no);
                FileLog.m27d(stringBuilder.toString());
            }
            int objectSize = tL_decryptedMessageLayer.getObjectSize();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(objectSize + 4);
            nativeByteBuffer.writeInt32(objectSize);
            tL_decryptedMessageLayer.serializeToStream(nativeByteBuffer);
            int length = nativeByteBuffer.length();
            objectSize = length % 16 != 0 ? 16 - (length % 16) : 0;
            if (i == 2) {
                objectSize += (Utilities.random.nextInt(3) + 2) * 16;
            }
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(length + objectSize);
            nativeByteBuffer.position(0);
            nativeByteBuffer2.writeBytes(nativeByteBuffer);
            if (objectSize != 0) {
                bArr = new byte[objectSize];
                Utilities.random.nextBytes(bArr);
                nativeByteBuffer2.writeBytes(bArr);
            }
            bArr = new byte[16];
            if (i != 2 || encryptedChat2.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                z = false;
            }
            if (i == 2) {
                System.arraycopy(Utilities.computeSHA256(encryptedChat2.auth_key, (z ? 8 : 0) + 88, 32, nativeByteBuffer2.buffer, 0, nativeByteBuffer2.buffer.limit()), 8, bArr, 0, 16);
            } else {
                byte[] computeSHA1 = Utilities.computeSHA1(nativeByteBuffer.buffer);
                System.arraycopy(computeSHA1, computeSHA1.length - 16, bArr, 0, 16);
            }
            nativeByteBuffer.reuse();
            MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(encryptedChat2.auth_key, bArr, z, i);
            Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 0, nativeByteBuffer2.limit());
            NativeByteBuffer nativeByteBuffer3 = new NativeByteBuffer((bArr.length + 8) + nativeByteBuffer2.length());
            nativeByteBuffer2.position(0);
            nativeByteBuffer3.writeInt64(encryptedChat2.key_fingerprint);
            nativeByteBuffer3.writeBytes(bArr);
            nativeByteBuffer3.writeBytes(nativeByteBuffer2);
            nativeByteBuffer2.reuse();
            nativeByteBuffer3.position(0);
            if (inputEncryptedFile2 == null) {
                TLObject tL_messages_sendEncryptedService;
                if (decryptedMessage2 instanceof TL_decryptedMessageService) {
                    tL_messages_sendEncryptedService = new TL_messages_sendEncryptedService();
                    tL_messages_sendEncryptedService.data = nativeByteBuffer3;
                    tL_messages_sendEncryptedService.random_id = decryptedMessage2.random_id;
                    tL_messages_sendEncryptedService.peer = new TL_inputEncryptedChat();
                    tL_messages_sendEncryptedService.peer.chat_id = encryptedChat2.f445id;
                    tL_messages_sendEncryptedService.peer.access_hash = encryptedChat2.access_hash;
                } else {
                    tL_messages_sendEncryptedService = new TL_messages_sendEncrypted();
                    tL_messages_sendEncryptedService.data = nativeByteBuffer3;
                    tL_messages_sendEncryptedService.random_id = decryptedMessage2.random_id;
                    tL_messages_sendEncryptedService.peer = new TL_inputEncryptedChat();
                    tL_messages_sendEncryptedService.peer.chat_id = encryptedChat2.f445id;
                    tL_messages_sendEncryptedService.peer.access_hash = encryptedChat2.access_hash;
                }
                tLObject = tL_messages_sendEncryptedService;
            } else {
                TLObject tL_messages_sendEncryptedFile = new TL_messages_sendEncryptedFile();
                tL_messages_sendEncryptedFile.data = nativeByteBuffer3;
                tL_messages_sendEncryptedFile.random_id = decryptedMessage2.random_id;
                tL_messages_sendEncryptedFile.peer = new TL_inputEncryptedChat();
                tL_messages_sendEncryptedFile.peer.chat_id = encryptedChat2.f445id;
                tL_messages_sendEncryptedFile.peer.access_hash = encryptedChat2.access_hash;
                tL_messages_sendEncryptedFile.file = inputEncryptedFile2;
                tLObject = tL_messages_sendEncryptedFile;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLObject, new C3537-$$Lambda$SecretChatHelper$NeIJyTvVk2g1G3EFM6ENqFtjkw0(this, decryptedMessage, encryptedChat, message, messageObject, str), 64);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$null$6$SecretChatHelper(DecryptedMessage decryptedMessage, EncryptedChat encryptedChat, Message message, MessageObject messageObject, String str, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null && (decryptedMessage.action instanceof TL_decryptedMessageActionNotifyLayer)) {
            EncryptedChat encryptedChat2 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(encryptedChat.f445id));
            if (encryptedChat2 == null) {
                encryptedChat2 = encryptedChat;
            }
            if (encryptedChat2.key_hash == null) {
                encryptedChat2.key_hash = AndroidUtilities.calcAuthKeyHash(encryptedChat2.auth_key);
            }
            if (AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer) >= 46 && encryptedChat2.key_hash.length == 16) {
                try {
                    byte[] computeSHA256 = Utilities.computeSHA256(encryptedChat.auth_key, 0, encryptedChat.auth_key.length);
                    byte[] bArr = new byte[36];
                    System.arraycopy(encryptedChat.key_hash, 0, bArr, 0, 16);
                    System.arraycopy(computeSHA256, 0, bArr, 16, 20);
                    encryptedChat2.key_hash = bArr;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                } catch (Throwable th) {
                    FileLog.m30e(th);
                }
            }
            this.sendingNotifyLayer.remove(Integer.valueOf(encryptedChat2.f445id));
            encryptedChat2.layer = AndroidUtilities.setMyLayerVersion(encryptedChat2.layer, 73);
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatLayer(encryptedChat2);
        }
        if (message == null) {
            return;
        }
        if (tL_error == null) {
            int mediaExistanceFlags;
            String str2 = message.attachPath;
            messages_SentEncryptedMessage messages_sentencryptedmessage = (messages_SentEncryptedMessage) tLObject;
            if (isSecretVisibleMessage(message)) {
                message.date = messages_sentencryptedmessage.date;
            }
            if (messageObject != null) {
                EncryptedFile encryptedFile = messages_sentencryptedmessage.file;
                if (encryptedFile instanceof TL_encryptedFile) {
                    updateMediaPaths(messageObject, encryptedFile, decryptedMessage, str);
                    mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                    MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new C0951-$$Lambda$SecretChatHelper$kXyPCeVLRJzYiyabGiQYTza0QCE(this, message, messages_sentencryptedmessage, mediaExistanceFlags, str2));
                    return;
                }
            }
            mediaExistanceFlags = 0;
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new C0951-$$Lambda$SecretChatHelper$kXyPCeVLRJzYiyabGiQYTza0QCE(this, message, messages_sentencryptedmessage, mediaExistanceFlags, str2));
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(message);
        AndroidUtilities.runOnUIThread(new C0938-$$Lambda$SecretChatHelper$KXmAZjrV-o2jJvsCy8wRyFby6VI(this, message));
    }

    public /* synthetic */ void lambda$null$4$SecretChatHelper(Message message, messages_SentEncryptedMessage messages_sentencryptedmessage, int i, String str) {
        if (isSecretInvisibleMessage(message)) {
            messages_sentencryptedmessage.date = 0;
        }
        MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(message.random_id, Integer.valueOf(message.f461id), message.f461id, messages_sentencryptedmessage.date, false, 0);
        AndroidUtilities.runOnUIThread(new C0933-$$Lambda$SecretChatHelper$6RH9f8axwCUH9UKWqF8EeUaUF1g(this, message, i, str));
    }

    public /* synthetic */ void lambda$null$3$SecretChatHelper(Message message, int i, String str) {
        message.send_state = 0;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(message.f461id), Integer.valueOf(message.f461id), message, Long.valueOf(message.dialog_id), Long.valueOf(0), Integer.valueOf(i));
        SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(message.f461id);
        if (MessageObject.isVideoMessage(message) || MessageObject.isNewGifMessage(message) || MessageObject.isRoundVideoMessage(message)) {
            SendMessagesHelper.getInstance(this.currentAccount).stopVideoService(str);
        }
        SendMessagesHelper.getInstance(this.currentAccount).removeFromSendingMessages(message.f461id);
    }

    public /* synthetic */ void lambda$null$5$SecretChatHelper(Message message) {
        message.send_state = 2;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(message.f461id));
        SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(message.f461id);
        if (MessageObject.isVideoMessage(message) || MessageObject.isNewGifMessage(message) || MessageObject.isRoundVideoMessage(message)) {
            SendMessagesHelper.getInstance(this.currentAccount).stopVideoService(message.attachPath);
        }
        SendMessagesHelper.getInstance(this.currentAccount).removeFromSendingMessages(message.f461id);
    }

    private void applyPeerLayer(EncryptedChat encryptedChat, int i) {
        int peerLayerVersion = AndroidUtilities.getPeerLayerVersion(encryptedChat.layer);
        if (i > peerLayerVersion) {
            if (encryptedChat.key_hash.length == 16 && peerLayerVersion >= 46) {
                try {
                    byte[] computeSHA256 = Utilities.computeSHA256(encryptedChat.auth_key, 0, encryptedChat.auth_key.length);
                    byte[] bArr = new byte[36];
                    System.arraycopy(encryptedChat.key_hash, 0, bArr, 0, 16);
                    System.arraycopy(computeSHA256, 0, bArr, 16, 20);
                    encryptedChat.key_hash = bArr;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                } catch (Throwable th) {
                    FileLog.m30e(th);
                }
            }
            encryptedChat.layer = AndroidUtilities.setPeerLayerVersion(encryptedChat.layer, i);
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatLayer(encryptedChat);
            if (peerLayerVersion < 73) {
                sendNotifyLayerMessage(encryptedChat, null);
            }
            AndroidUtilities.runOnUIThread(new C0952-$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo(this, encryptedChat));
        }
    }

    public /* synthetic */ void lambda$applyPeerLayer$8$SecretChatHelper(EncryptedChat encryptedChat) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, encryptedChat);
    }

    public Message processDecryptedObject(EncryptedChat encryptedChat, EncryptedFile encryptedFile, int i, TLObject tLObject, boolean z) {
        EncryptedChat encryptedChat2 = encryptedChat;
        EncryptedFile encryptedFile2 = encryptedFile;
        int i2 = i;
        TLObject tLObject2 = tLObject;
        if (tLObject2 != null) {
            int i3 = encryptedChat2.admin_id;
            if (i3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                i3 = encryptedChat2.participant_id;
            }
            if (AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer) >= 20 && encryptedChat2.exchange_id == 0 && encryptedChat2.future_key_fingerprint == 0 && encryptedChat2.key_use_count_in >= (short) 120) {
                requestNewSecretChatKey(encryptedChat);
            }
            if (encryptedChat2.exchange_id == 0 && encryptedChat2.future_key_fingerprint != 0 && !z) {
                encryptedChat2.future_auth_key = new byte[256];
                encryptedChat2.future_key_fingerprint = 0;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
            } else if (encryptedChat2.exchange_id != 0 && z) {
                encryptedChat2.key_fingerprint = encryptedChat2.future_key_fingerprint;
                encryptedChat2.auth_key = encryptedChat2.future_auth_key;
                encryptedChat2.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                encryptedChat2.future_auth_key = new byte[256];
                encryptedChat2.future_key_fingerprint = 0;
                encryptedChat2.key_use_count_in = (short) 0;
                encryptedChat2.key_use_count_out = (short) 0;
                encryptedChat2.exchange_id = 0;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
            }
            long j;
            byte[] bArr;
            byte[] bArr2;
            if (tLObject2 instanceof TL_decryptedMessage) {
                Message tL_message_secret;
                int i4;
                MessageMedia messageMedia;
                TL_decryptedMessage tL_decryptedMessage = (TL_decryptedMessage) tLObject2;
                if (AndroidUtilities.getPeerLayerVersion(encryptedChat2.layer) >= 17) {
                    tL_message_secret = new TL_message_secret();
                    tL_message_secret.ttl = tL_decryptedMessage.ttl;
                    tL_message_secret.entities = tL_decryptedMessage.entities;
                } else {
                    tL_message_secret = new TL_message();
                    tL_message_secret.ttl = encryptedChat2.ttl;
                }
                tL_message_secret.message = tL_decryptedMessage.message;
                tL_message_secret.date = i2;
                int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                tL_message_secret.f461id = newMessageId;
                tL_message_secret.local_id = newMessageId;
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                tL_message_secret.from_id = i3;
                tL_message_secret.to_id = new TL_peerUser();
                tL_message_secret.random_id = tL_decryptedMessage.random_id;
                tL_message_secret.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                tL_message_secret.unread = true;
                tL_message_secret.flags = 768;
                String str = tL_decryptedMessage.via_bot_name;
                if (str != null && str.length() > 0) {
                    tL_message_secret.via_bot_name = tL_decryptedMessage.via_bot_name;
                    tL_message_secret.flags |= 2048;
                }
                j = tL_decryptedMessage.grouped_id;
                if (j != 0) {
                    tL_message_secret.grouped_id = j;
                    tL_message_secret.flags |= MessagesController.UPDATE_MASK_REORDER;
                }
                tL_message_secret.dialog_id = ((long) encryptedChat2.f445id) << 32;
                j = tL_decryptedMessage.reply_to_random_id;
                if (j != 0) {
                    tL_message_secret.reply_to_random_id = j;
                    tL_message_secret.flags |= 8;
                }
                DecryptedMessageMedia decryptedMessageMedia = tL_decryptedMessage.media;
                if (decryptedMessageMedia == null || (decryptedMessageMedia instanceof TL_decryptedMessageMediaEmpty)) {
                    tL_message_secret.media = new TL_messageMediaEmpty();
                } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaWebPage) {
                    tL_message_secret.media = new TL_messageMediaWebPage();
                    tL_message_secret.media.webpage = new TL_webPageUrlPending();
                    tL_message_secret.media.webpage.url = tL_decryptedMessage.media.url;
                } else {
                    String str2 = "";
                    MessageMedia messageMedia2;
                    DecryptedMessageMedia decryptedMessageMedia2;
                    if (decryptedMessageMedia instanceof TL_decryptedMessageMediaContact) {
                        tL_message_secret.media = new TL_messageMediaContact();
                        messageMedia2 = tL_message_secret.media;
                        decryptedMessageMedia2 = tL_decryptedMessage.media;
                        messageMedia2.last_name = decryptedMessageMedia2.last_name;
                        messageMedia2.first_name = decryptedMessageMedia2.first_name;
                        messageMedia2.phone_number = decryptedMessageMedia2.phone_number;
                        messageMedia2.user_id = decryptedMessageMedia2.user_id;
                        messageMedia2.vcard = str2;
                    } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaGeoPoint) {
                        tL_message_secret.media = new TL_messageMediaGeo();
                        tL_message_secret.media.geo = new TL_geoPoint();
                        GeoPoint geoPoint = tL_message_secret.media.geo;
                        decryptedMessageMedia2 = tL_decryptedMessage.media;
                        geoPoint.lat = decryptedMessageMedia2.lat;
                        geoPoint._long = decryptedMessageMedia2._long;
                    } else {
                        String str3 = "s";
                        byte[] bArr3;
                        String str4;
                        DecryptedMessageMedia decryptedMessageMedia3;
                        DecryptedMessageMedia decryptedMessageMedia4;
                        Document document;
                        Object tL_photoSizeEmpty;
                        if (decryptedMessageMedia instanceof TL_decryptedMessageMediaPhoto) {
                            bArr = decryptedMessageMedia.key;
                            if (bArr != null && bArr.length == 32) {
                                bArr3 = decryptedMessageMedia.f438iv;
                                if (bArr3 != null && bArr3.length == 32) {
                                    tL_message_secret.media = new TL_messageMediaPhoto();
                                    messageMedia2 = tL_message_secret.media;
                                    messageMedia2.flags |= 3;
                                    str4 = tL_decryptedMessage.media.caption;
                                    if (str4 == null) {
                                        str4 = str2;
                                    }
                                    tL_message_secret.message = str4;
                                    tL_message_secret.media.photo = new TL_photo();
                                    Photo photo = tL_message_secret.media.photo;
                                    photo.file_reference = new byte[0];
                                    photo.date = tL_message_secret.date;
                                    decryptedMessageMedia = tL_decryptedMessage.media;
                                    bArr = ((TL_decryptedMessageMediaPhoto) decryptedMessageMedia).thumb;
                                    if (bArr != null && bArr.length != 0 && bArr.length <= 6000 && decryptedMessageMedia.thumb_w <= 100 && decryptedMessageMedia.thumb_h <= 100) {
                                        TL_photoCachedSize tL_photoCachedSize = new TL_photoCachedSize();
                                        decryptedMessageMedia3 = tL_decryptedMessage.media;
                                        tL_photoCachedSize.f465w = decryptedMessageMedia3.thumb_w;
                                        tL_photoCachedSize.f464h = decryptedMessageMedia3.thumb_h;
                                        tL_photoCachedSize.bytes = bArr;
                                        tL_photoCachedSize.type = str3;
                                        tL_photoCachedSize.location = new TL_fileLocationUnavailable();
                                        tL_message_secret.media.photo.sizes.add(tL_photoCachedSize);
                                    }
                                    i4 = tL_message_secret.ttl;
                                    if (i4 != 0) {
                                        MessageMedia messageMedia3 = tL_message_secret.media;
                                        messageMedia3.ttl_seconds = i4;
                                        messageMedia3.flags |= 4;
                                    }
                                    TL_photoSize tL_photoSize = new TL_photoSize();
                                    decryptedMessageMedia4 = tL_decryptedMessage.media;
                                    tL_photoSize.f465w = decryptedMessageMedia4.f439w;
                                    tL_photoSize.f464h = decryptedMessageMedia4.f436h;
                                    tL_photoSize.type = "x";
                                    tL_photoSize.size = encryptedFile2.size;
                                    tL_photoSize.location = new TL_fileEncryptedLocation();
                                    FileLocation fileLocation = tL_photoSize.location;
                                    DecryptedMessageMedia decryptedMessageMedia5 = tL_decryptedMessage.media;
                                    fileLocation.key = decryptedMessageMedia5.key;
                                    fileLocation.f447iv = decryptedMessageMedia5.f438iv;
                                    fileLocation.dc_id = encryptedFile2.dc_id;
                                    fileLocation.volume_id = encryptedFile2.f446id;
                                    fileLocation.secret = encryptedFile2.access_hash;
                                    fileLocation.local_id = encryptedFile2.key_fingerprint;
                                    tL_message_secret.media.photo.sizes.add(tL_photoSize);
                                }
                            }
                            return null;
                        } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaVideo) {
                            bArr2 = decryptedMessageMedia.key;
                            if (bArr2 != null && bArr2.length == 32) {
                                bArr3 = decryptedMessageMedia.f438iv;
                                if (bArr3 != null && bArr3.length == 32) {
                                    tL_message_secret.media = new TL_messageMediaDocument();
                                    messageMedia2 = tL_message_secret.media;
                                    messageMedia2.flags |= 3;
                                    messageMedia2.document = new TL_documentEncrypted();
                                    document = tL_message_secret.media.document;
                                    decryptedMessageMedia3 = tL_decryptedMessage.media;
                                    document.key = decryptedMessageMedia3.key;
                                    document.f442iv = decryptedMessageMedia3.f438iv;
                                    document.dc_id = encryptedFile2.dc_id;
                                    str4 = decryptedMessageMedia3.caption;
                                    if (str4 == null) {
                                        str4 = str2;
                                    }
                                    tL_message_secret.message = str4;
                                    document = tL_message_secret.media.document;
                                    document.date = i2;
                                    document.size = encryptedFile2.size;
                                    document.f441id = encryptedFile2.f446id;
                                    document.access_hash = encryptedFile2.access_hash;
                                    document.mime_type = tL_decryptedMessage.media.mime_type;
                                    if (document.mime_type == null) {
                                        document.mime_type = MimeTypes.VIDEO_MP4;
                                    }
                                    decryptedMessageMedia = tL_decryptedMessage.media;
                                    byte[] bArr4 = ((TL_decryptedMessageMediaVideo) decryptedMessageMedia).thumb;
                                    if (bArr4 == null || bArr4.length == 0 || bArr4.length > 6000 || decryptedMessageMedia.thumb_w > 100 || decryptedMessageMedia.thumb_h > 100) {
                                        tL_photoSizeEmpty = new TL_photoSizeEmpty();
                                        tL_photoSizeEmpty.type = str3;
                                    } else {
                                        tL_photoSizeEmpty = new TL_photoCachedSize();
                                        tL_photoSizeEmpty.bytes = bArr4;
                                        decryptedMessageMedia2 = tL_decryptedMessage.media;
                                        tL_photoSizeEmpty.f465w = decryptedMessageMedia2.thumb_w;
                                        tL_photoSizeEmpty.f464h = decryptedMessageMedia2.thumb_h;
                                        tL_photoSizeEmpty.type = str3;
                                        tL_photoSizeEmpty.location = new TL_fileLocationUnavailable();
                                    }
                                    tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty);
                                    document = tL_message_secret.media.document;
                                    document.flags |= 1;
                                    TL_documentAttributeVideo tL_documentAttributeVideo = new TL_documentAttributeVideo();
                                    decryptedMessageMedia2 = tL_decryptedMessage.media;
                                    tL_documentAttributeVideo.f444w = decryptedMessageMedia2.f439w;
                                    tL_documentAttributeVideo.f443h = decryptedMessageMedia2.f436h;
                                    tL_documentAttributeVideo.duration = decryptedMessageMedia2.duration;
                                    tL_documentAttributeVideo.supports_streaming = false;
                                    tL_message_secret.media.document.attributes.add(tL_documentAttributeVideo);
                                    i4 = tL_message_secret.ttl;
                                    if (i4 != 0) {
                                        messageMedia = tL_message_secret.media;
                                        messageMedia.ttl_seconds = i4;
                                        messageMedia.flags |= 4;
                                    }
                                    i4 = tL_message_secret.ttl;
                                    if (i4 != 0) {
                                        tL_message_secret.ttl = Math.max(tL_decryptedMessage.media.duration + 1, i4);
                                    }
                                }
                            }
                            return null;
                        } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaDocument) {
                            bArr2 = decryptedMessageMedia.key;
                            if (bArr2 != null && bArr2.length == 32) {
                                bArr3 = decryptedMessageMedia.f438iv;
                                if (bArr3 != null && bArr3.length == 32) {
                                    tL_message_secret.media = new TL_messageMediaDocument();
                                    messageMedia2 = tL_message_secret.media;
                                    messageMedia2.flags |= 3;
                                    str4 = tL_decryptedMessage.media.caption;
                                    if (str4 == null) {
                                        str4 = str2;
                                    }
                                    tL_message_secret.message = str4;
                                    tL_message_secret.media.document = new TL_documentEncrypted();
                                    document = tL_message_secret.media.document;
                                    document.f441id = encryptedFile2.f446id;
                                    document.access_hash = encryptedFile2.access_hash;
                                    document.date = i2;
                                    decryptedMessageMedia4 = tL_decryptedMessage.media;
                                    if (decryptedMessageMedia4 instanceof TL_decryptedMessageMediaDocument_layer8) {
                                        TL_documentAttributeFilename tL_documentAttributeFilename = new TL_documentAttributeFilename();
                                        tL_documentAttributeFilename.file_name = tL_decryptedMessage.media.file_name;
                                        tL_message_secret.media.document.attributes.add(tL_documentAttributeFilename);
                                    } else {
                                        document.attributes = decryptedMessageMedia4.attributes;
                                    }
                                    document = tL_message_secret.media.document;
                                    decryptedMessageMedia4 = tL_decryptedMessage.media;
                                    document.mime_type = decryptedMessageMedia4.mime_type;
                                    i2 = decryptedMessageMedia4.size;
                                    document.size = i2 != 0 ? Math.min(i2, encryptedFile2.size) : encryptedFile2.size;
                                    document = tL_message_secret.media.document;
                                    decryptedMessageMedia4 = tL_decryptedMessage.media;
                                    document.key = decryptedMessageMedia4.key;
                                    document.f442iv = decryptedMessageMedia4.f438iv;
                                    if (document.mime_type == null) {
                                        document.mime_type = str2;
                                    }
                                    decryptedMessageMedia = tL_decryptedMessage.media;
                                    bArr = ((TL_decryptedMessageMediaDocument) decryptedMessageMedia).thumb;
                                    if (bArr == null || bArr.length == 0 || bArr.length > 6000 || decryptedMessageMedia.thumb_w > 100 || decryptedMessageMedia.thumb_h > 100) {
                                        tL_photoSizeEmpty = new TL_photoSizeEmpty();
                                        tL_photoSizeEmpty.type = str3;
                                    } else {
                                        tL_photoSizeEmpty = new TL_photoCachedSize();
                                        tL_photoSizeEmpty.bytes = bArr;
                                        decryptedMessageMedia4 = tL_decryptedMessage.media;
                                        tL_photoSizeEmpty.f465w = decryptedMessageMedia4.thumb_w;
                                        tL_photoSizeEmpty.f464h = decryptedMessageMedia4.thumb_h;
                                        tL_photoSizeEmpty.type = str3;
                                        tL_photoSizeEmpty.location = new TL_fileLocationUnavailable();
                                    }
                                    tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty);
                                    document = tL_message_secret.media.document;
                                    document.flags |= 1;
                                    document.dc_id = encryptedFile2.dc_id;
                                    if (MessageObject.isVoiceMessage(tL_message_secret) || MessageObject.isRoundVideoMessage(tL_message_secret)) {
                                        tL_message_secret.media_unread = true;
                                    }
                                }
                            }
                            return null;
                        } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaExternalDocument) {
                            tL_message_secret.media = new TL_messageMediaDocument();
                            messageMedia2 = tL_message_secret.media;
                            messageMedia2.flags |= 3;
                            tL_message_secret.message = str2;
                            messageMedia2.document = new TL_document();
                            document = tL_message_secret.media.document;
                            decryptedMessageMedia2 = tL_decryptedMessage.media;
                            document.f441id = decryptedMessageMedia2.f437id;
                            document.access_hash = decryptedMessageMedia2.access_hash;
                            document.file_reference = new byte[0];
                            document.date = decryptedMessageMedia2.date;
                            document.attributes = decryptedMessageMedia2.attributes;
                            document.mime_type = decryptedMessageMedia2.mime_type;
                            document.dc_id = decryptedMessageMedia2.dc_id;
                            document.size = decryptedMessageMedia2.size;
                            document.thumbs.add(((TL_decryptedMessageMediaExternalDocument) decryptedMessageMedia2).thumb);
                            document = tL_message_secret.media.document;
                            document.flags |= 1;
                            if (document.mime_type == null) {
                                document.mime_type = str2;
                            }
                        } else if (decryptedMessageMedia instanceof TL_decryptedMessageMediaAudio) {
                            bArr2 = decryptedMessageMedia.key;
                            if (bArr2 != null && bArr2.length == 32) {
                                bArr3 = decryptedMessageMedia.f438iv;
                                if (bArr3 != null && bArr3.length == 32) {
                                    tL_message_secret.media = new TL_messageMediaDocument();
                                    messageMedia2 = tL_message_secret.media;
                                    messageMedia2.flags |= 3;
                                    messageMedia2.document = new TL_documentEncrypted();
                                    document = tL_message_secret.media.document;
                                    decryptedMessageMedia3 = tL_decryptedMessage.media;
                                    document.key = decryptedMessageMedia3.key;
                                    document.f442iv = decryptedMessageMedia3.f438iv;
                                    document.f441id = encryptedFile2.f446id;
                                    document.access_hash = encryptedFile2.access_hash;
                                    document.date = i2;
                                    document.size = encryptedFile2.size;
                                    document.dc_id = encryptedFile2.dc_id;
                                    document.mime_type = decryptedMessageMedia3.mime_type;
                                    str4 = decryptedMessageMedia3.caption;
                                    if (str4 == null) {
                                        str4 = str2;
                                    }
                                    tL_message_secret.message = str4;
                                    document = tL_message_secret.media.document;
                                    if (document.mime_type == null) {
                                        document.mime_type = "audio/ogg";
                                    }
                                    TL_documentAttributeAudio tL_documentAttributeAudio = new TL_documentAttributeAudio();
                                    tL_documentAttributeAudio.duration = tL_decryptedMessage.media.duration;
                                    tL_documentAttributeAudio.voice = true;
                                    tL_message_secret.media.document.attributes.add(tL_documentAttributeAudio);
                                    i4 = tL_message_secret.ttl;
                                    if (i4 != 0) {
                                        tL_message_secret.ttl = Math.max(tL_decryptedMessage.media.duration + 1, i4);
                                    }
                                    if (tL_message_secret.media.document.thumbs.isEmpty()) {
                                        TL_photoSizeEmpty tL_photoSizeEmpty2 = new TL_photoSizeEmpty();
                                        tL_photoSizeEmpty2.type = str3;
                                        tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty2);
                                    }
                                }
                            }
                            return null;
                        } else if (!(decryptedMessageMedia instanceof TL_decryptedMessageMediaVenue)) {
                            return null;
                        } else {
                            tL_message_secret.media = new TL_messageMediaVenue();
                            tL_message_secret.media.geo = new TL_geoPoint();
                            messageMedia2 = tL_message_secret.media;
                            GeoPoint geoPoint2 = messageMedia2.geo;
                            decryptedMessageMedia4 = tL_decryptedMessage.media;
                            geoPoint2.lat = decryptedMessageMedia4.lat;
                            geoPoint2._long = decryptedMessageMedia4._long;
                            messageMedia2.title = decryptedMessageMedia4.title;
                            messageMedia2.address = decryptedMessageMedia4.address;
                            messageMedia2.provider = decryptedMessageMedia4.provider;
                            messageMedia2.venue_id = decryptedMessageMedia4.venue_id;
                            messageMedia2.venue_type = str2;
                        }
                    }
                }
                i4 = tL_message_secret.ttl;
                if (i4 != 0) {
                    messageMedia = tL_message_secret.media;
                    if (messageMedia.ttl_seconds == 0) {
                        messageMedia.ttl_seconds = i4;
                        messageMedia.flags |= 4;
                    }
                }
                return tL_message_secret;
            } else if (tLObject2 instanceof TL_decryptedMessageService) {
                TL_decryptedMessageService tL_decryptedMessageService = (TL_decryptedMessageService) tLObject2;
                DecryptedMessageAction decryptedMessageAction = tL_decryptedMessageService.action;
                int i5;
                DecryptedMessageAction decryptedMessageAction2;
                BigInteger bigInteger;
                byte[] bArr5;
                if ((decryptedMessageAction instanceof TL_decryptedMessageActionSetMessageTTL) || (decryptedMessageAction instanceof TL_decryptedMessageActionScreenshotMessages)) {
                    TL_messageService tL_messageService = new TL_messageService();
                    DecryptedMessageAction decryptedMessageAction3 = tL_decryptedMessageService.action;
                    if (decryptedMessageAction3 instanceof TL_decryptedMessageActionSetMessageTTL) {
                        tL_messageService.action = new TL_messageEncryptedAction();
                        i5 = tL_decryptedMessageService.action.ttl_seconds;
                        if (i5 < 0 || i5 > 31536000) {
                            tL_decryptedMessageService.action.ttl_seconds = 31536000;
                        }
                        decryptedMessageAction2 = tL_decryptedMessageService.action;
                        encryptedChat2.ttl = decryptedMessageAction2.ttl_seconds;
                        tL_messageService.action.encryptedAction = decryptedMessageAction2;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatTTL(encryptedChat2);
                    } else if (decryptedMessageAction3 instanceof TL_decryptedMessageActionScreenshotMessages) {
                        tL_messageService.action = new TL_messageEncryptedAction();
                        tL_messageService.action.encryptedAction = tL_decryptedMessageService.action;
                    }
                    int newMessageId2 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    tL_messageService.f461id = newMessageId2;
                    tL_messageService.local_id = newMessageId2;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    tL_messageService.unread = true;
                    tL_messageService.flags = 256;
                    tL_messageService.date = i2;
                    tL_messageService.from_id = i3;
                    tL_messageService.to_id = new TL_peerUser();
                    tL_messageService.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    tL_messageService.dialog_id = ((long) encryptedChat2.f445id) << 32;
                    return tL_messageService;
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionFlushHistory) {
                    AndroidUtilities.runOnUIThread(new C0940-$$Lambda$SecretChatHelper$OJ7QWMtzGqvNXSkDUc1v2NRta0c(this, ((long) encryptedChat2.f445id) << 32));
                    return null;
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionDeleteMessages) {
                    if (!decryptedMessageAction.random_ids.isEmpty()) {
                        this.pendingEncMessagesToDelete.addAll(tL_decryptedMessageService.action.random_ids);
                    }
                    return null;
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionReadMessages) {
                    if (!decryptedMessageAction.random_ids.isEmpty()) {
                        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                        MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(encryptedChat2.f445id, currentTime, currentTime, 1, tL_decryptedMessageService.action.random_ids);
                    }
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionNotifyLayer) {
                    applyPeerLayer(encryptedChat2, decryptedMessageAction.layer);
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionRequestKey) {
                    j = encryptedChat2.exchange_id;
                    if (j != 0) {
                        if (j > decryptedMessageAction.exchange_id) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.m27d("we already have request key with higher exchange_id");
                            }
                            return null;
                        }
                        sendAbortKeyMessage(encryptedChat2, null, j);
                    }
                    bArr = new byte[256];
                    Utilities.random.nextBytes(bArr);
                    bigInteger = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                    BigInteger modPow = BigInteger.valueOf((long) MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, bArr), bigInteger);
                    BigInteger bigInteger2 = new BigInteger(1, tL_decryptedMessageService.action.g_a);
                    if (Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                        bArr2 = modPow.toByteArray();
                        if (bArr2.length > 256) {
                            byte[] bArr6 = new byte[256];
                            System.arraycopy(bArr2, 1, bArr6, 0, 256);
                            bArr2 = bArr6;
                        }
                        bArr = bigInteger2.modPow(new BigInteger(1, bArr), bigInteger).toByteArray();
                        if (bArr.length > 256) {
                            bArr5 = new byte[256];
                            System.arraycopy(bArr, bArr.length - 256, bArr5, 0, 256);
                        } else if (bArr.length < 256) {
                            bArr5 = new byte[256];
                            System.arraycopy(bArr, 0, bArr5, 256 - bArr.length, bArr.length);
                            for (i3 = 0; i3 < 256 - bArr.length; i3++) {
                                bArr5[i3] = (byte) 0;
                            }
                        } else {
                            bArr5 = bArr;
                        }
                        bArr = Utilities.computeSHA1(bArr5);
                        byte[] bArr7 = new byte[8];
                        System.arraycopy(bArr, bArr.length - 8, bArr7, 0, 8);
                        encryptedChat2.exchange_id = tL_decryptedMessageService.action.exchange_id;
                        encryptedChat2.future_auth_key = bArr5;
                        encryptedChat2.future_key_fingerprint = Utilities.bytesToLong(bArr7);
                        encryptedChat2.g_a_or_b = bArr2;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                        sendAcceptKeyMessage(encryptedChat2, null);
                    } else {
                        sendAbortKeyMessage(encryptedChat2, null, tL_decryptedMessageService.action.exchange_id);
                        return null;
                    }
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionAcceptKey) {
                    if (encryptedChat2.exchange_id == decryptedMessageAction.exchange_id) {
                        BigInteger bigInteger3 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                        bigInteger = new BigInteger(1, tL_decryptedMessageService.action.g_b);
                        if (Utilities.isGoodGaAndGb(bigInteger, bigInteger3)) {
                            bArr = bigInteger.modPow(new BigInteger(1, encryptedChat2.a_or_b), bigInteger3).toByteArray();
                            if (bArr.length > 256) {
                                bArr5 = new byte[256];
                                System.arraycopy(bArr, bArr.length - 256, bArr5, 0, 256);
                            } else if (bArr.length < 256) {
                                bArr5 = new byte[256];
                                System.arraycopy(bArr, 0, bArr5, 256 - bArr.length, bArr.length);
                                for (i5 = 0; i5 < 256 - bArr.length; i5++) {
                                    bArr5[i5] = (byte) 0;
                                }
                            } else {
                                bArr5 = bArr;
                            }
                            bArr = Utilities.computeSHA1(bArr5);
                            bArr2 = new byte[8];
                            System.arraycopy(bArr, bArr.length - 8, bArr2, 0, 8);
                            j = Utilities.bytesToLong(bArr2);
                            if (tL_decryptedMessageService.action.key_fingerprint == j) {
                                encryptedChat2.future_auth_key = bArr5;
                                encryptedChat2.future_key_fingerprint = j;
                                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                                sendCommitKeyMessage(encryptedChat2, null);
                            } else {
                                encryptedChat2.future_auth_key = new byte[256];
                                encryptedChat2.future_key_fingerprint = 0;
                                encryptedChat2.exchange_id = 0;
                                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                                sendAbortKeyMessage(encryptedChat2, null, tL_decryptedMessageService.action.exchange_id);
                            }
                        } else {
                            encryptedChat2.future_auth_key = new byte[256];
                            encryptedChat2.future_key_fingerprint = 0;
                            encryptedChat2.exchange_id = 0;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                            sendAbortKeyMessage(encryptedChat2, null, tL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                    }
                    encryptedChat2.future_auth_key = new byte[256];
                    encryptedChat2.future_key_fingerprint = 0;
                    encryptedChat2.exchange_id = 0;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                    sendAbortKeyMessage(encryptedChat2, null, tL_decryptedMessageService.action.exchange_id);
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionCommitKey) {
                    if (encryptedChat2.exchange_id == decryptedMessageAction.exchange_id) {
                        j = encryptedChat2.future_key_fingerprint;
                        if (j == decryptedMessageAction.key_fingerprint) {
                            long j2 = encryptedChat2.key_fingerprint;
                            bArr5 = encryptedChat2.auth_key;
                            encryptedChat2.key_fingerprint = j;
                            encryptedChat2.auth_key = encryptedChat2.future_auth_key;
                            encryptedChat2.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                            encryptedChat2.future_auth_key = bArr5;
                            encryptedChat2.future_key_fingerprint = j2;
                            encryptedChat2.key_use_count_in = (short) 0;
                            encryptedChat2.key_use_count_out = (short) 0;
                            encryptedChat2.exchange_id = 0;
                            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                            sendNoopMessage(encryptedChat2, null);
                        }
                    }
                    encryptedChat2.future_auth_key = new byte[256];
                    encryptedChat2.future_key_fingerprint = 0;
                    encryptedChat2.exchange_id = 0;
                    MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                    sendAbortKeyMessage(encryptedChat2, null, tL_decryptedMessageService.action.exchange_id);
                } else if (decryptedMessageAction instanceof TL_decryptedMessageActionAbortKey) {
                    if (encryptedChat2.exchange_id == decryptedMessageAction.exchange_id) {
                        encryptedChat2.future_auth_key = new byte[256];
                        encryptedChat2.future_key_fingerprint = 0;
                        encryptedChat2.exchange_id = 0;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
                    }
                } else if (!(decryptedMessageAction instanceof TL_decryptedMessageActionNoop)) {
                    if (!(decryptedMessageAction instanceof TL_decryptedMessageActionResend)) {
                        return null;
                    }
                    i2 = decryptedMessageAction.end_seq_no;
                    i5 = encryptedChat2.in_seq_no;
                    if (i2 >= i5) {
                        i3 = decryptedMessageAction.start_seq_no;
                        if (i2 >= i3) {
                            if (i3 < i5) {
                                decryptedMessageAction.start_seq_no = i5;
                            }
                            decryptedMessageAction2 = tL_decryptedMessageService.action;
                            resendMessages(decryptedMessageAction2.start_seq_no, decryptedMessageAction2.end_seq_no, encryptedChat2);
                        }
                    }
                    return null;
                }
            } else if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unknown message ");
                stringBuilder.append(tLObject2);
                FileLog.m28e(stringBuilder.toString());
            }
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.m28e("unknown TLObject");
        }
        return null;
    }

    public /* synthetic */ void lambda$processDecryptedObject$11$SecretChatHelper(long j) {
        Dialog dialog = (Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
        if (dialog != null) {
            dialog.unread_count = 0;
            MessagesController.getInstance(this.currentAccount).dialogMessage.remove(dialog.f440id);
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new C0950-$$Lambda$SecretChatHelper$kHVODiP7b77JSefUc0MrQJDvu_o(this, j));
        MessagesStorage.getInstance(this.currentAccount).deleteDialog(j, 1);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(j), Boolean.valueOf(false));
    }

    public /* synthetic */ void lambda$null$10$SecretChatHelper(long j) {
        AndroidUtilities.runOnUIThread(new C0956-$$Lambda$SecretChatHelper$zouLqc6zy27lFYX8g6pcFXGkhsk(this, j));
    }

    public /* synthetic */ void lambda$null$9$SecretChatHelper(long j) {
        NotificationsController.getInstance(this.currentAccount).processReadMessages(null, j, 0, Integer.MAX_VALUE, false);
        LongSparseArray longSparseArray = new LongSparseArray(1);
        longSparseArray.put(j, Integer.valueOf(0));
        NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(longSparseArray);
    }

    private Message createDeleteMessage(int i, int i2, int i3, long j, EncryptedChat encryptedChat) {
        TL_messageService tL_messageService = new TL_messageService();
        tL_messageService.action = new TL_messageEncryptedAction();
        tL_messageService.action.encryptedAction = new TL_decryptedMessageActionDeleteMessages();
        tL_messageService.action.encryptedAction.random_ids.add(Long.valueOf(j));
        tL_messageService.f461id = i;
        tL_messageService.local_id = i;
        tL_messageService.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
        tL_messageService.unread = true;
        tL_messageService.out = true;
        tL_messageService.flags = 256;
        tL_messageService.dialog_id = ((long) encryptedChat.f445id) << 32;
        tL_messageService.to_id = new TL_peerUser();
        tL_messageService.send_state = 1;
        tL_messageService.seq_in = i3;
        tL_messageService.seq_out = i2;
        if (encryptedChat.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            tL_messageService.to_id.user_id = encryptedChat.admin_id;
        } else {
            tL_messageService.to_id.user_id = encryptedChat.participant_id;
        }
        tL_messageService.date = 0;
        tL_messageService.random_id = j;
        return tL_messageService;
    }

    private void resendMessages(int i, int i2, EncryptedChat encryptedChat) {
        if (encryptedChat != null && i2 - i >= 0) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new C0943-$$Lambda$SecretChatHelper$UeLgl-NG4gDOs_Y4sJhukTdyyjM(this, i, encryptedChat, i2));
        }
    }

    public /* synthetic */ void lambda$resendMessages$14$SecretChatHelper(int i, EncryptedChat encryptedChat, int i2) {
        EncryptedChat encryptedChat2 = encryptedChat;
        try {
            int i3 = (encryptedChat2.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId() && i % 2 == 0) ? i + 1 : i;
            SQLiteDatabase database = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            Object[] objArr = new Object[5];
            int i4 = 0;
            objArr[0] = Integer.valueOf(encryptedChat2.f445id);
            int i5 = 1;
            objArr[1] = Integer.valueOf(i3);
            int i6 = 2;
            objArr[2] = Integer.valueOf(i3);
            int i7 = 3;
            objArr[3] = Integer.valueOf(i2);
            objArr[4] = Integer.valueOf(i2);
            SQLiteCursor queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT uid FROM requested_holes WHERE uid = %d AND ((seq_out_start >= %d AND %d <= seq_out_end) OR (seq_out_start >= %d AND %d <= seq_out_end))", objArr), new Object[0]);
            boolean next = queryFinalized.next();
            queryFinalized.dispose();
            if (!next) {
                ArrayList arrayList;
                SparseArray sparseArray;
                long j = ((long) encryptedChat2.f445id) << 32;
                SparseArray sparseArray2 = new SparseArray();
                ArrayList arrayList2 = new ArrayList();
                int i8 = i2;
                for (int i9 = i3; i9 < i8; i9 += 2) {
                    sparseArray2.put(i9, null);
                }
                SQLiteCursor queryFinalized2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, r.random_id, s.seq_in, s.seq_out, m.ttl, s.mid FROM messages_seq as s LEFT JOIN randoms as r ON r.mid = s.mid LEFT JOIN messages as m ON m.mid = s.mid WHERE m.uid = %d AND m.out = 1 AND s.seq_out >= %d AND s.seq_out <= %d ORDER BY seq_out ASC", new Object[]{Long.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i2)}), new Object[0]);
                while (queryFinalized2.next()) {
                    Object obj;
                    long j2;
                    long longValue = queryFinalized2.longValue(i5);
                    if (longValue == 0) {
                        longValue = Utilities.random.nextLong();
                    }
                    int intValue = queryFinalized2.intValue(i6);
                    i6 = queryFinalized2.intValue(i7);
                    int intValue2 = queryFinalized2.intValue(5);
                    NativeByteBuffer byteBufferValue = queryFinalized2.byteBufferValue(i4);
                    if (byteBufferValue != null) {
                        Message TLdeserialize = Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(i4), i4);
                        TLdeserialize.readAttachPath(byteBufferValue, UserConfig.getInstance(this.currentAccount).clientUserId);
                        byteBufferValue.reuse();
                        TLdeserialize.random_id = longValue;
                        TLdeserialize.dialog_id = j;
                        TLdeserialize.seq_in = intValue;
                        TLdeserialize.seq_out = i6;
                        TLdeserialize.ttl = queryFinalized2.intValue(4);
                        arrayList = arrayList2;
                        sparseArray = sparseArray2;
                        obj = TLdeserialize;
                        j2 = j;
                    } else {
                        arrayList = arrayList2;
                        j2 = j;
                        sparseArray = sparseArray2;
                        obj = createDeleteMessage(intValue2, i6, intValue, longValue, encryptedChat);
                    }
                    arrayList.add(obj);
                    sparseArray.remove(i6);
                    i8 = i2;
                    sparseArray2 = sparseArray;
                    arrayList2 = arrayList;
                    j = j2;
                    i4 = 0;
                    i5 = 1;
                    i6 = 2;
                    i7 = 3;
                }
                arrayList = arrayList2;
                sparseArray = sparseArray2;
                queryFinalized2.dispose();
                if (sparseArray.size() != 0) {
                    for (int i10 = 0; i10 < sparseArray.size(); i10++) {
                        arrayList.add(createDeleteMessage(UserConfig.getInstance(this.currentAccount).getNewMessageId(), sparseArray.keyAt(i10), 0, Utilities.random.nextLong(), encryptedChat));
                    }
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                Collections.sort(arrayList, C0939-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc.INSTANCE);
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(encryptedChat2);
                AndroidUtilities.runOnUIThread(new C0945-$$Lambda$SecretChatHelper$VQkryblefkb-35jNwuFnQ2KSKkw(this, arrayList));
                SendMessagesHelper.getInstance(this.currentAccount).processUnsentMessages(arrayList, new ArrayList(), new ArrayList(), arrayList3);
                MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast(String.format(Locale.US, "REPLACE INTO requested_holes VALUES(%d, %d, %d)", new Object[]{Integer.valueOf(encryptedChat2.f445id), Integer.valueOf(i3), Integer.valueOf(i2)})).stepThis().dispose();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$null$13$SecretChatHelper(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (Message) arrayList.get(i), false);
            messageObject.resendAsIs = true;
            SendMessagesHelper.getInstance(this.currentAccount).retrySendMessage(messageObject, true);
        }
    }

    public void checkSecretHoles(EncryptedChat encryptedChat, ArrayList<Message> arrayList) {
        ArrayList arrayList2 = (ArrayList) this.secretHolesQueue.get(encryptedChat.f445id);
        if (arrayList2 != null) {
            Collections.sort(arrayList2, C0946-$$Lambda$SecretChatHelper$XChl_gDRHQHDfwtxghrPUY1XhL4.INSTANCE);
            Object obj = null;
            while (arrayList2.size() > 0) {
                TL_decryptedMessageHolder tL_decryptedMessageHolder = (TL_decryptedMessageHolder) arrayList2.get(0);
                int i = tL_decryptedMessageHolder.layer.out_seq_no;
                int i2 = encryptedChat.seq_in;
                if (i != i2 && i2 != i - 2) {
                    break;
                }
                applyPeerLayer(encryptedChat, tL_decryptedMessageHolder.layer.layer);
                TL_decryptedMessageLayer tL_decryptedMessageLayer = tL_decryptedMessageHolder.layer;
                encryptedChat.seq_in = tL_decryptedMessageLayer.out_seq_no;
                encryptedChat.in_seq_no = tL_decryptedMessageLayer.in_seq_no;
                arrayList2.remove(0);
                if (tL_decryptedMessageHolder.decryptedWithVersion == 2) {
                    encryptedChat.mtproto_seq = Math.min(encryptedChat.mtproto_seq, encryptedChat.seq_in);
                }
                Message processDecryptedObject = processDecryptedObject(encryptedChat, tL_decryptedMessageHolder.file, tL_decryptedMessageHolder.date, tL_decryptedMessageHolder.layer.message, tL_decryptedMessageHolder.new_key_used);
                if (processDecryptedObject != null) {
                    arrayList.add(processDecryptedObject);
                }
                obj = 1;
            }
            if (arrayList2.isEmpty()) {
                this.secretHolesQueue.remove(encryptedChat.f445id);
            }
            if (obj != null) {
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(encryptedChat, true);
            }
        }
    }

    static /* synthetic */ int lambda$checkSecretHoles$15(TL_decryptedMessageHolder tL_decryptedMessageHolder, TL_decryptedMessageHolder tL_decryptedMessageHolder2) {
        int i = tL_decryptedMessageHolder.layer.out_seq_no;
        int i2 = tL_decryptedMessageHolder2.layer.out_seq_no;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    private boolean decryptWithMtProtoVersion(NativeByteBuffer nativeByteBuffer, byte[] bArr, byte[] bArr2, int i, boolean z, boolean z2) {
        byte[] bArr3;
        boolean z3;
        NativeByteBuffer nativeByteBuffer2 = nativeByteBuffer;
        byte[] bArr4 = bArr2;
        int i2 = i;
        if (i2 == 1) {
            bArr3 = bArr;
            z3 = false;
        } else {
            bArr3 = bArr;
            z3 = z;
        }
        MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(bArr3, bArr4, z3, i2);
        Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, false, false, 24, nativeByteBuffer.limit() - 24);
        int readInt32 = nativeByteBuffer2.readInt32(false);
        if (i2 == 2) {
            int i3 = (z3 ? 8 : 0) + 88;
            ByteBuffer byteBuffer = nativeByteBuffer2.buffer;
            if (!Utilities.arraysEquals(bArr4, 0, Utilities.computeSHA256(bArr, i3, 32, byteBuffer, 24, byteBuffer.limit()), 8)) {
                if (z2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer2.position(24);
                }
                return false;
            }
        }
        int i4 = readInt32 + 28;
        if (i4 < nativeByteBuffer2.buffer.limit() - 15 || i4 > nativeByteBuffer2.buffer.limit()) {
            i4 = nativeByteBuffer2.buffer.limit();
        }
        bArr3 = Utilities.computeSHA1(nativeByteBuffer2.buffer, 24, i4);
        if (!Utilities.arraysEquals(bArr4, 0, bArr3, bArr3.length - 16)) {
            if (z2) {
                Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                nativeByteBuffer2.position(24);
            }
            return false;
        }
        if (readInt32 <= 0 || readInt32 > nativeByteBuffer.limit() - 28) {
            return false;
        }
        int limit = (nativeByteBuffer.limit() - 28) - readInt32;
        if ((i2 != 2 || (limit >= 12 && limit <= 1024)) && (i2 != 1 || limit <= 15)) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x005d A:{Catch:{ Exception -> 0x0263 }} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b A:{Catch:{ Exception -> 0x0263 }} */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0249 A:{Catch:{ Exception -> 0x0263 }} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0060 A:{Catch:{ Exception -> 0x0263 }} */
    public java.util.ArrayList<org.telegram.tgnet.TLRPC.Message> decryptMessage(org.telegram.tgnet.TLRPC.EncryptedMessage r22) {
        /*
        r21 = this;
        r8 = r21;
        r0 = r22;
        r9 = " out_seq = ";
        r1 = r8.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r0.chat_id;
        r10 = 1;
        r11 = r1.getEncryptedChatDB(r2, r10);
        r12 = 0;
        if (r11 == 0) goto L_0x0267;
    L_0x0016:
        r1 = r11 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;
        if (r1 == 0) goto L_0x001c;
    L_0x001a:
        goto L_0x0267;
    L_0x001c:
        r13 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Exception -> 0x0263 }
        r1 = r0.bytes;	 Catch:{ Exception -> 0x0263 }
        r1 = r1.length;	 Catch:{ Exception -> 0x0263 }
        r13.<init>(r1);	 Catch:{ Exception -> 0x0263 }
        r1 = r0.bytes;	 Catch:{ Exception -> 0x0263 }
        r13.writeBytes(r1);	 Catch:{ Exception -> 0x0263 }
        r14 = 0;
        r13.position(r14);	 Catch:{ Exception -> 0x0263 }
        r1 = r13.readInt64(r14);	 Catch:{ Exception -> 0x0263 }
        r3 = r11.key_fingerprint;	 Catch:{ Exception -> 0x0263 }
        r5 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r5 != 0) goto L_0x003b;
    L_0x0037:
        r3 = r11.auth_key;	 Catch:{ Exception -> 0x0263 }
        r15 = r3;
        goto L_0x004f;
    L_0x003b:
        r3 = r11.future_key_fingerprint;	 Catch:{ Exception -> 0x0263 }
        r5 = 0;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x004e;
    L_0x0043:
        r3 = r11.future_key_fingerprint;	 Catch:{ Exception -> 0x0263 }
        r5 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r5 != 0) goto L_0x004e;
    L_0x0049:
        r3 = r11.future_auth_key;	 Catch:{ Exception -> 0x0263 }
        r15 = r3;
        r7 = 1;
        goto L_0x0050;
    L_0x004e:
        r15 = r12;
    L_0x004f:
        r7 = 0;
    L_0x0050:
        r3 = r11.layer;	 Catch:{ Exception -> 0x0263 }
        r3 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r3);	 Catch:{ Exception -> 0x0263 }
        r4 = 73;
        r6 = 2;
        if (r3 < r4) goto L_0x005d;
    L_0x005b:
        r5 = 2;
        goto L_0x005e;
    L_0x005d:
        r5 = 1;
    L_0x005e:
        if (r15 == 0) goto L_0x0249;
    L_0x0060:
        r1 = 16;
        r16 = r13.readData(r1, r14);	 Catch:{ Exception -> 0x0263 }
        r1 = r11.admin_id;	 Catch:{ Exception -> 0x0263 }
        r2 = r8.currentAccount;	 Catch:{ Exception -> 0x0263 }
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x0263 }
        r2 = r2.getClientUserId();	 Catch:{ Exception -> 0x0263 }
        if (r1 != r2) goto L_0x0077;
    L_0x0074:
        r17 = 1;
        goto L_0x0079;
    L_0x0077:
        r17 = 0;
    L_0x0079:
        if (r5 != r6) goto L_0x0082;
    L_0x007b:
        r1 = r11.mtproto_seq;	 Catch:{ Exception -> 0x0263 }
        if (r1 == 0) goto L_0x0082;
    L_0x007f:
        r18 = 0;
        goto L_0x0084;
    L_0x0082:
        r18 = 1;
    L_0x0084:
        r1 = r21;
        r2 = r13;
        r3 = r15;
        r4 = r16;
        r19 = r5;
        r10 = 2;
        r6 = r17;
        r20 = r7;
        r7 = r18;
        r1 = r1.decryptWithMtProtoVersion(r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0263 }
        if (r1 != 0) goto L_0x00c7;
    L_0x0099:
        r6 = r19;
        if (r6 != r10) goto L_0x00b3;
    L_0x009d:
        if (r18 == 0) goto L_0x00b2;
    L_0x009f:
        r5 = 1;
        r7 = 0;
        r1 = r21;
        r2 = r13;
        r3 = r15;
        r4 = r16;
        r6 = r17;
        r1 = r1.decryptWithMtProtoVersion(r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0263 }
        if (r1 != 0) goto L_0x00b0;
    L_0x00af:
        goto L_0x00b2;
    L_0x00b0:
        r6 = 1;
        goto L_0x00c9;
    L_0x00b2:
        return r12;
    L_0x00b3:
        r5 = 2;
        r1 = r21;
        r2 = r13;
        r3 = r15;
        r4 = r16;
        r6 = r17;
        r7 = r18;
        r1 = r1.decryptWithMtProtoVersion(r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0263 }
        if (r1 != 0) goto L_0x00c5;
    L_0x00c4:
        return r12;
    L_0x00c5:
        r6 = 2;
        goto L_0x00c9;
    L_0x00c7:
        r6 = r19;
    L_0x00c9:
        r1 = org.telegram.tgnet.TLClassStore.Instance();	 Catch:{ Exception -> 0x0263 }
        r2 = r13.readInt32(r14);	 Catch:{ Exception -> 0x0263 }
        r1 = r1.TLdeserialize(r13, r2, r14);	 Catch:{ Exception -> 0x0263 }
        r13.reuse();	 Catch:{ Exception -> 0x0263 }
        r14 = r20;
        if (r14 != 0) goto L_0x00ed;
    L_0x00dc:
        r2 = r11.layer;	 Catch:{ Exception -> 0x0263 }
        r2 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r2);	 Catch:{ Exception -> 0x0263 }
        r3 = 20;
        if (r2 < r3) goto L_0x00ed;
    L_0x00e6:
        r2 = r11.key_use_count_in;	 Catch:{ Exception -> 0x0263 }
        r3 = 1;
        r2 = r2 + r3;
        r2 = (short) r2;	 Catch:{ Exception -> 0x0263 }
        r11.key_use_count_in = r2;	 Catch:{ Exception -> 0x0263 }
    L_0x00ed:
        r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageLayer;	 Catch:{ Exception -> 0x0263 }
        if (r2 == 0) goto L_0x021f;
    L_0x00f1:
        r1 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageLayer) r1;	 Catch:{ Exception -> 0x0263 }
        r2 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        if (r2 != 0) goto L_0x0113;
    L_0x00f7:
        r2 = r11.seq_out;	 Catch:{ Exception -> 0x0263 }
        if (r2 != 0) goto L_0x0113;
    L_0x00fb:
        r2 = r11.admin_id;	 Catch:{ Exception -> 0x0263 }
        r3 = r8.currentAccount;	 Catch:{ Exception -> 0x0263 }
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0263 }
        r3 = r3.getClientUserId();	 Catch:{ Exception -> 0x0263 }
        if (r2 != r3) goto L_0x0110;
    L_0x0109:
        r2 = 1;
        r11.seq_out = r2;	 Catch:{ Exception -> 0x0263 }
        r2 = -2;
        r11.seq_in = r2;	 Catch:{ Exception -> 0x0263 }
        goto L_0x0113;
    L_0x0110:
        r2 = -1;
        r11.seq_in = r2;	 Catch:{ Exception -> 0x0263 }
    L_0x0113:
        r2 = r1.random_bytes;	 Catch:{ Exception -> 0x0263 }
        r2 = r2.length;	 Catch:{ Exception -> 0x0263 }
        r3 = 15;
        if (r2 >= r3) goto L_0x0124;
    L_0x011a:
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0263 }
        if (r0 == 0) goto L_0x0123;
    L_0x011e:
        r0 = "got random bytes less than needed";
        org.telegram.messenger.FileLog.m28e(r0);	 Catch:{ Exception -> 0x0263 }
    L_0x0123:
        return r12;
    L_0x0124:
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0263 }
        if (r2 == 0) goto L_0x0164;
    L_0x0128:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0263 }
        r2.<init>();	 Catch:{ Exception -> 0x0263 }
        r3 = "current chat in_seq = ";
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r3 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r2.append(r9);	 Catch:{ Exception -> 0x0263 }
        r3 = r11.seq_out;	 Catch:{ Exception -> 0x0263 }
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0263 }
        org.telegram.messenger.FileLog.m27d(r2);	 Catch:{ Exception -> 0x0263 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0263 }
        r2.<init>();	 Catch:{ Exception -> 0x0263 }
        r3 = "got message with in_seq = ";
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r3 = r1.in_seq_no;	 Catch:{ Exception -> 0x0263 }
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r2.append(r9);	 Catch:{ Exception -> 0x0263 }
        r3 = r1.out_seq_no;	 Catch:{ Exception -> 0x0263 }
        r2.append(r3);	 Catch:{ Exception -> 0x0263 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0263 }
        org.telegram.messenger.FileLog.m27d(r2);	 Catch:{ Exception -> 0x0263 }
    L_0x0164:
        r2 = r1.out_seq_no;	 Catch:{ Exception -> 0x0263 }
        r3 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        if (r2 > r3) goto L_0x016b;
    L_0x016a:
        return r12;
    L_0x016b:
        r2 = 1;
        if (r6 != r2) goto L_0x0179;
    L_0x016e:
        r2 = r11.mtproto_seq;	 Catch:{ Exception -> 0x0263 }
        if (r2 == 0) goto L_0x0179;
    L_0x0172:
        r2 = r1.out_seq_no;	 Catch:{ Exception -> 0x0263 }
        r3 = r11.mtproto_seq;	 Catch:{ Exception -> 0x0263 }
        if (r2 < r3) goto L_0x0179;
    L_0x0178:
        return r12;
    L_0x0179:
        r2 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        r3 = r1.out_seq_no;	 Catch:{ Exception -> 0x0263 }
        r3 = r3 - r10;
        if (r2 == r3) goto L_0x01f9;
    L_0x0180:
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0263 }
        if (r2 == 0) goto L_0x0189;
    L_0x0184:
        r2 = "got hole";
        org.telegram.messenger.FileLog.m28e(r2);	 Catch:{ Exception -> 0x0263 }
    L_0x0189:
        r2 = r8.secretHolesQueue;	 Catch:{ Exception -> 0x0263 }
        r3 = r11.f445id;	 Catch:{ Exception -> 0x0263 }
        r2 = r2.get(r3);	 Catch:{ Exception -> 0x0263 }
        r2 = (java.util.ArrayList) r2;	 Catch:{ Exception -> 0x0263 }
        if (r2 != 0) goto L_0x01a1;
    L_0x0195:
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0263 }
        r2.<init>();	 Catch:{ Exception -> 0x0263 }
        r3 = r8.secretHolesQueue;	 Catch:{ Exception -> 0x0263 }
        r4 = r11.f445id;	 Catch:{ Exception -> 0x0263 }
        r3.put(r4, r2);	 Catch:{ Exception -> 0x0263 }
    L_0x01a1:
        r3 = r2.size();	 Catch:{ Exception -> 0x0263 }
        r4 = 4;
        if (r3 < r4) goto L_0x01e2;
    L_0x01a8:
        r0 = r8.secretHolesQueue;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.f445id;	 Catch:{ Exception -> 0x0263 }
        r0.remove(r1);	 Catch:{ Exception -> 0x0263 }
        r0 = new org.telegram.tgnet.TLRPC$TL_encryptedChatDiscarded;	 Catch:{ Exception -> 0x0263 }
        r0.<init>();	 Catch:{ Exception -> 0x0263 }
        r1 = r11.f445id;	 Catch:{ Exception -> 0x0263 }
        r0.f445id = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.user_id;	 Catch:{ Exception -> 0x0263 }
        r0.user_id = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.auth_key;	 Catch:{ Exception -> 0x0263 }
        r0.auth_key = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.key_create_date;	 Catch:{ Exception -> 0x0263 }
        r0.key_create_date = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.key_use_count_in;	 Catch:{ Exception -> 0x0263 }
        r0.key_use_count_in = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.key_use_count_out;	 Catch:{ Exception -> 0x0263 }
        r0.key_use_count_out = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        r0.seq_in = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r11.seq_out;	 Catch:{ Exception -> 0x0263 }
        r0.seq_out = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = new org.telegram.messenger.-$$Lambda$SecretChatHelper$y_-QKcAzTtKUDu-WddTe8KbVDxY;	 Catch:{ Exception -> 0x0263 }
        r1.<init>(r8, r0);	 Catch:{ Exception -> 0x0263 }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r1);	 Catch:{ Exception -> 0x0263 }
        r0 = r11.f445id;	 Catch:{ Exception -> 0x0263 }
        r8.declineSecretChat(r0);	 Catch:{ Exception -> 0x0263 }
        return r12;
    L_0x01e2:
        r3 = new org.telegram.messenger.SecretChatHelper$TL_decryptedMessageHolder;	 Catch:{ Exception -> 0x0263 }
        r3.<init>();	 Catch:{ Exception -> 0x0263 }
        r3.layer = r1;	 Catch:{ Exception -> 0x0263 }
        r1 = r0.file;	 Catch:{ Exception -> 0x0263 }
        r3.file = r1;	 Catch:{ Exception -> 0x0263 }
        r0 = r0.date;	 Catch:{ Exception -> 0x0263 }
        r3.date = r0;	 Catch:{ Exception -> 0x0263 }
        r3.new_key_used = r14;	 Catch:{ Exception -> 0x0263 }
        r3.decryptedWithVersion = r6;	 Catch:{ Exception -> 0x0263 }
        r2.add(r3);	 Catch:{ Exception -> 0x0263 }
        return r12;
    L_0x01f9:
        if (r6 != r10) goto L_0x0205;
    L_0x01fb:
        r2 = r11.mtproto_seq;	 Catch:{ Exception -> 0x0263 }
        r3 = r11.seq_in;	 Catch:{ Exception -> 0x0263 }
        r2 = java.lang.Math.min(r2, r3);	 Catch:{ Exception -> 0x0263 }
        r11.mtproto_seq = r2;	 Catch:{ Exception -> 0x0263 }
    L_0x0205:
        r2 = r1.layer;	 Catch:{ Exception -> 0x0263 }
        r8.applyPeerLayer(r11, r2);	 Catch:{ Exception -> 0x0263 }
        r2 = r1.out_seq_no;	 Catch:{ Exception -> 0x0263 }
        r11.seq_in = r2;	 Catch:{ Exception -> 0x0263 }
        r2 = r1.in_seq_no;	 Catch:{ Exception -> 0x0263 }
        r11.in_seq_no = r2;	 Catch:{ Exception -> 0x0263 }
        r2 = r8.currentAccount;	 Catch:{ Exception -> 0x0263 }
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Exception -> 0x0263 }
        r3 = 1;
        r2.updateEncryptedChatSeq(r11, r3);	 Catch:{ Exception -> 0x0263 }
        r1 = r1.message;	 Catch:{ Exception -> 0x0263 }
        goto L_0x022d;
    L_0x021f:
        r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageService;	 Catch:{ Exception -> 0x0263 }
        if (r2 == 0) goto L_0x0248;
    L_0x0223:
        r2 = r1;
        r2 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageService) r2;	 Catch:{ Exception -> 0x0263 }
        r2 = r2.action;	 Catch:{ Exception -> 0x0263 }
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionNotifyLayer;	 Catch:{ Exception -> 0x0263 }
        if (r2 != 0) goto L_0x022d;
    L_0x022c:
        goto L_0x0248;
    L_0x022d:
        r5 = r1;
        r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0263 }
        r7.<init>();	 Catch:{ Exception -> 0x0263 }
        r3 = r0.file;	 Catch:{ Exception -> 0x0263 }
        r4 = r0.date;	 Catch:{ Exception -> 0x0263 }
        r1 = r21;
        r2 = r11;
        r6 = r14;
        r0 = r1.processDecryptedObject(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0263 }
        if (r0 == 0) goto L_0x0244;
    L_0x0241:
        r7.add(r0);	 Catch:{ Exception -> 0x0263 }
    L_0x0244:
        r8.checkSecretHoles(r11, r7);	 Catch:{ Exception -> 0x0263 }
        return r7;
    L_0x0248:
        return r12;
    L_0x0249:
        r13.reuse();	 Catch:{ Exception -> 0x0263 }
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0263 }
        if (r0 == 0) goto L_0x0267;
    L_0x0250:
        r0 = "fingerprint mismatch %x";
        r3 = 1;
        r3 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0263 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0263 }
        r3[r14] = r1;	 Catch:{ Exception -> 0x0263 }
        r0 = java.lang.String.format(r0, r3);	 Catch:{ Exception -> 0x0263 }
        org.telegram.messenger.FileLog.m28e(r0);	 Catch:{ Exception -> 0x0263 }
        goto L_0x0267;
    L_0x0263:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0267:
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SecretChatHelper.decryptMessage(org.telegram.tgnet.TLRPC$EncryptedMessage):java.util.ArrayList");
    }

    public /* synthetic */ void lambda$decryptMessage$16$SecretChatHelper(TL_encryptedChatDiscarded tL_encryptedChatDiscarded) {
        MessagesController.getInstance(this.currentAccount).putEncryptedChat(tL_encryptedChatDiscarded, false);
        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(tL_encryptedChatDiscarded);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, tL_encryptedChatDiscarded);
    }

    public void requestNewSecretChatKey(EncryptedChat encryptedChat) {
        if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 20) {
            byte[] bArr = new byte[256];
            Utilities.random.nextBytes(bArr);
            byte[] toByteArray = BigInteger.valueOf((long) MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes())).toByteArray();
            if (toByteArray.length > 256) {
                byte[] bArr2 = new byte[256];
                System.arraycopy(toByteArray, 1, bArr2, 0, 256);
                toByteArray = bArr2;
            }
            encryptedChat.exchange_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
            encryptedChat.a_or_b = bArr;
            encryptedChat.g_a = toByteArray;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
            sendRequestKeyMessage(encryptedChat, null);
        }
    }

    public void processAcceptedSecretChat(EncryptedChat encryptedChat) {
        BigInteger bigInteger = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
        BigInteger bigInteger2 = new BigInteger(1, encryptedChat.g_a_or_b);
        if (Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
            byte[] bArr;
            byte[] toByteArray = bigInteger2.modPow(new BigInteger(1, encryptedChat.a_or_b), bigInteger).toByteArray();
            if (toByteArray.length > 256) {
                bArr = new byte[256];
                System.arraycopy(toByteArray, toByteArray.length - 256, bArr, 0, 256);
            } else if (toByteArray.length < 256) {
                bArr = new byte[256];
                System.arraycopy(toByteArray, 0, bArr, 256 - toByteArray.length, toByteArray.length);
                for (int i = 0; i < 256 - toByteArray.length; i++) {
                    bArr[i] = (byte) 0;
                }
            } else {
                bArr = toByteArray;
            }
            toByteArray = Utilities.computeSHA1(bArr);
            byte[] bArr2 = new byte[8];
            System.arraycopy(toByteArray, toByteArray.length - 8, bArr2, 0, 8);
            if (encryptedChat.key_fingerprint == Utilities.bytesToLong(bArr2)) {
                encryptedChat.auth_key = bArr;
                encryptedChat.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                encryptedChat.seq_in = -2;
                encryptedChat.seq_out = 1;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat);
                MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, false);
                AndroidUtilities.runOnUIThread(new C0954-$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM(this, encryptedChat));
            } else {
                TL_encryptedChatDiscarded tL_encryptedChatDiscarded = new TL_encryptedChatDiscarded();
                tL_encryptedChatDiscarded.f445id = encryptedChat.f445id;
                tL_encryptedChatDiscarded.user_id = encryptedChat.user_id;
                tL_encryptedChatDiscarded.auth_key = encryptedChat.auth_key;
                tL_encryptedChatDiscarded.key_create_date = encryptedChat.key_create_date;
                tL_encryptedChatDiscarded.key_use_count_in = encryptedChat.key_use_count_in;
                tL_encryptedChatDiscarded.key_use_count_out = encryptedChat.key_use_count_out;
                tL_encryptedChatDiscarded.seq_in = encryptedChat.seq_in;
                tL_encryptedChatDiscarded.seq_out = encryptedChat.seq_out;
                tL_encryptedChatDiscarded.admin_id = encryptedChat.admin_id;
                tL_encryptedChatDiscarded.mtproto_seq = encryptedChat.mtproto_seq;
                MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(tL_encryptedChatDiscarded);
                AndroidUtilities.runOnUIThread(new C0934-$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE(this, tL_encryptedChatDiscarded));
                declineSecretChat(encryptedChat.f445id);
            }
            return;
        }
        declineSecretChat(encryptedChat.f445id);
    }

    public /* synthetic */ void lambda$processAcceptedSecretChat$17$SecretChatHelper(EncryptedChat encryptedChat) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, encryptedChat);
        sendNotifyLayerMessage(encryptedChat, null);
    }

    public /* synthetic */ void lambda$processAcceptedSecretChat$18$SecretChatHelper(TL_encryptedChatDiscarded tL_encryptedChatDiscarded) {
        MessagesController.getInstance(this.currentAccount).putEncryptedChat(tL_encryptedChatDiscarded, false);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, tL_encryptedChatDiscarded);
    }

    public void declineSecretChat(int i) {
        TL_messages_discardEncryption tL_messages_discardEncryption = new TL_messages_discardEncryption();
        tL_messages_discardEncryption.chat_id = i;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_discardEncryption, C3538-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M.INSTANCE);
    }

    public void acceptSecretChat(EncryptedChat encryptedChat) {
        if (this.acceptingChats.get(encryptedChat.f445id) == null) {
            this.acceptingChats.put(encryptedChat.f445id, encryptedChat);
            TL_messages_getDhConfig tL_messages_getDhConfig = new TL_messages_getDhConfig();
            tL_messages_getDhConfig.random_length = 256;
            tL_messages_getDhConfig.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getDhConfig, new C3533-$$Lambda$SecretChatHelper$13Zq8UfkfLhsSEm3Hl6dWS1zGEc(this, encryptedChat));
        }
    }

    public /* synthetic */ void lambda$acceptSecretChat$22$SecretChatHelper(EncryptedChat encryptedChat, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            messages_DhConfig messages_dhconfig = (messages_DhConfig) tLObject;
            if (tLObject instanceof TL_messages_dhConfig) {
                if (Utilities.isGoodPrime(messages_dhconfig.f541p, messages_dhconfig.f540g)) {
                    MessagesStorage.getInstance(this.currentAccount).setSecretPBytes(messages_dhconfig.f541p);
                    MessagesStorage.getInstance(this.currentAccount).setSecretG(messages_dhconfig.f540g);
                    MessagesStorage.getInstance(this.currentAccount).setLastSecretVersion(messages_dhconfig.version);
                    MessagesStorage.getInstance(this.currentAccount).saveSecretParams(MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion(), MessagesStorage.getInstance(this.currentAccount).getSecretG(), MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                } else {
                    this.acceptingChats.remove(encryptedChat.f445id);
                    declineSecretChat(encryptedChat.f445id);
                    return;
                }
            }
            byte[] bArr = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr[i] = (byte) (((byte) ((int) (Utilities.random.nextDouble() * 256.0d))) ^ messages_dhconfig.random[i]);
            }
            encryptedChat.a_or_b = bArr;
            encryptedChat.seq_in = -1;
            encryptedChat.seq_out = 0;
            BigInteger bigInteger = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
            BigInteger modPow = BigInteger.valueOf((long) MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, bArr), bigInteger);
            BigInteger bigInteger2 = new BigInteger(1, encryptedChat.g_a);
            if (Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                byte[] toByteArray = modPow.toByteArray();
                if (toByteArray.length > 256) {
                    byte[] bArr2 = new byte[256];
                    System.arraycopy(toByteArray, 1, bArr2, 0, 256);
                    toByteArray = bArr2;
                }
                byte[] toByteArray2 = bigInteger2.modPow(new BigInteger(1, bArr), bigInteger).toByteArray();
                if (toByteArray2.length > 256) {
                    bArr = new byte[256];
                    System.arraycopy(toByteArray2, toByteArray2.length - 256, bArr, 0, 256);
                } else if (toByteArray2.length < 256) {
                    bArr = new byte[256];
                    System.arraycopy(toByteArray2, 0, bArr, 256 - toByteArray2.length, toByteArray2.length);
                    for (int i2 = 0; i2 < 256 - toByteArray2.length; i2++) {
                        bArr[i2] = (byte) 0;
                    }
                } else {
                    bArr = toByteArray2;
                }
                byte[] computeSHA1 = Utilities.computeSHA1(bArr);
                byte[] bArr3 = new byte[8];
                System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr3, 0, 8);
                encryptedChat.auth_key = bArr;
                encryptedChat.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                TL_messages_acceptEncryption tL_messages_acceptEncryption = new TL_messages_acceptEncryption();
                tL_messages_acceptEncryption.g_b = toByteArray;
                tL_messages_acceptEncryption.peer = new TL_inputEncryptedChat();
                TL_inputEncryptedChat tL_inputEncryptedChat = tL_messages_acceptEncryption.peer;
                tL_inputEncryptedChat.chat_id = encryptedChat.f445id;
                tL_inputEncryptedChat.access_hash = encryptedChat.access_hash;
                tL_messages_acceptEncryption.key_fingerprint = Utilities.bytesToLong(bArr3);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_acceptEncryption, new C3534-$$Lambda$SecretChatHelper$48_HP_7TQqG7f2oWszU7R3aCenM(this, encryptedChat));
            } else {
                this.acceptingChats.remove(encryptedChat.f445id);
                declineSecretChat(encryptedChat.f445id);
                return;
            }
        }
        this.acceptingChats.remove(encryptedChat.f445id);
    }

    public /* synthetic */ void lambda$null$21$SecretChatHelper(EncryptedChat encryptedChat, TLObject tLObject, TL_error tL_error) {
        this.acceptingChats.remove(encryptedChat.f445id);
        if (tL_error == null) {
            EncryptedChat encryptedChat2 = (EncryptedChat) tLObject;
            encryptedChat2.auth_key = encryptedChat.auth_key;
            encryptedChat2.user_id = encryptedChat.user_id;
            encryptedChat2.seq_in = encryptedChat.seq_in;
            encryptedChat2.seq_out = encryptedChat.seq_out;
            encryptedChat2.key_create_date = encryptedChat.key_create_date;
            encryptedChat2.key_use_count_in = encryptedChat.key_use_count_in;
            encryptedChat2.key_use_count_out = encryptedChat.key_use_count_out;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(encryptedChat2);
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat2, false);
            AndroidUtilities.runOnUIThread(new C0948-$$Lambda$SecretChatHelper$_jYoIAhmqiUWDV4t-Jnx1LLNrA8(this, encryptedChat2));
        }
    }

    public /* synthetic */ void lambda$null$20$SecretChatHelper(EncryptedChat encryptedChat) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, encryptedChat);
        sendNotifyLayerMessage(encryptedChat, null);
    }

    public void startSecretChat(Context context, User user) {
        if (user != null && context != null) {
            this.startingSecretChat = true;
            AlertDialog alertDialog = new AlertDialog(context, 3);
            TL_messages_getDhConfig tL_messages_getDhConfig = new TL_messages_getDhConfig();
            tL_messages_getDhConfig.random_length = 256;
            tL_messages_getDhConfig.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
            alertDialog.setOnCancelListener(new C0935-$$Lambda$SecretChatHelper$FOvPRdYvHRkFV5NyZdXuhMMalnM(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getDhConfig, new C3536-$$Lambda$SecretChatHelper$CyJGexOzIKpecJNn32s1dWnqMhg(this, context, alertDialog, user), 2)));
            try {
                alertDialog.show();
            } catch (Exception unused) {
            }
        }
    }

    public /* synthetic */ void lambda$startSecretChat$29$SecretChatHelper(Context context, AlertDialog alertDialog, User user, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            messages_DhConfig messages_dhconfig = (messages_DhConfig) tLObject;
            if (tLObject instanceof TL_messages_dhConfig) {
                if (Utilities.isGoodPrime(messages_dhconfig.f541p, messages_dhconfig.f540g)) {
                    MessagesStorage.getInstance(this.currentAccount).setSecretPBytes(messages_dhconfig.f541p);
                    MessagesStorage.getInstance(this.currentAccount).setSecretG(messages_dhconfig.f540g);
                    MessagesStorage.getInstance(this.currentAccount).setLastSecretVersion(messages_dhconfig.version);
                    MessagesStorage.getInstance(this.currentAccount).saveSecretParams(MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion(), MessagesStorage.getInstance(this.currentAccount).getSecretG(), MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                } else {
                    AndroidUtilities.runOnUIThread(new C0944-$$Lambda$SecretChatHelper$V-xN6On4v0MEOO6-YoYKk7r9DOk(context, alertDialog));
                    return;
                }
            }
            byte[] bArr = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr[i] = (byte) (((byte) ((int) (Utilities.random.nextDouble() * 256.0d))) ^ messages_dhconfig.random[i]);
            }
            byte[] toByteArray = BigInteger.valueOf((long) MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes())).toByteArray();
            if (toByteArray.length > 256) {
                byte[] bArr2 = new byte[256];
                System.arraycopy(toByteArray, 1, bArr2, 0, 256);
                toByteArray = bArr2;
            }
            TL_messages_requestEncryption tL_messages_requestEncryption = new TL_messages_requestEncryption();
            tL_messages_requestEncryption.g_a = toByteArray;
            tL_messages_requestEncryption.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(user);
            tL_messages_requestEncryption.random_id = Utilities.random.nextInt();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_requestEncryption, new C3535-$$Lambda$SecretChatHelper$BEU71I5KzZcukvSdHQ6G9fRsUbQ(this, context, alertDialog, bArr, user), 2);
        } else {
            this.delayedEncryptedChatUpdates.clear();
            AndroidUtilities.runOnUIThread(new C0942-$$Lambda$SecretChatHelper$TZa6lzq7a-IHyrpxBJVmRAmKQE8(this, context, alertDialog));
        }
    }

    static /* synthetic */ void lambda$null$23(Context context, AlertDialog alertDialog) {
        try {
            if (!((Activity) context).isFinishing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$null$27$SecretChatHelper(Context context, AlertDialog alertDialog, byte[] bArr, User user, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            AndroidUtilities.runOnUIThread(new C0932-$$Lambda$SecretChatHelper$0tV_MJuVJAhZ10ST8ytcL1B6vB4(this, context, alertDialog, tLObject, bArr, user));
            return;
        }
        this.delayedEncryptedChatUpdates.clear();
        AndroidUtilities.runOnUIThread(new C0953-$$Lambda$SecretChatHelper$pNz_sskaVL96CTdZukhE4ke04i8(this, context, alertDialog));
    }

    public /* synthetic */ void lambda$null$25$SecretChatHelper(Context context, AlertDialog alertDialog, TLObject tLObject, byte[] bArr, User user) {
        this.startingSecretChat = false;
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        EncryptedChat encryptedChat = (EncryptedChat) tLObject;
        encryptedChat.user_id = encryptedChat.participant_id;
        encryptedChat.seq_in = -2;
        encryptedChat.seq_out = 1;
        encryptedChat.a_or_b = bArr;
        MessagesController.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, false);
        TL_dialog tL_dialog = new TL_dialog();
        tL_dialog.f440id = DialogObject.makeSecretDialogId(encryptedChat.f445id);
        tL_dialog.unread_count = 0;
        tL_dialog.top_message = 0;
        tL_dialog.last_message_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        MessagesController.getInstance(this.currentAccount).dialogs_dict.put(tL_dialog.f440id, tL_dialog);
        MessagesController.getInstance(this.currentAccount).allDialogs.add(tL_dialog);
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        MessagesStorage.getInstance(this.currentAccount).putEncryptedChat(encryptedChat, user, tL_dialog);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatCreated, encryptedChat);
        Utilities.stageQueue.postRunnable(new C0941-$$Lambda$SecretChatHelper$PyQ6La6w_4bJYWv3ojsQqv921CM(this));
    }

    public /* synthetic */ void lambda$null$24$SecretChatHelper() {
        if (!this.delayedEncryptedChatUpdates.isEmpty()) {
            MessagesController.getInstance(this.currentAccount).processUpdateArray(this.delayedEncryptedChatUpdates, null, null, false);
            this.delayedEncryptedChatUpdates.clear();
        }
    }

    public /* synthetic */ void lambda$null$26$SecretChatHelper(Context context, AlertDialog alertDialog) {
        if (!((Activity) context).isFinishing()) {
            this.startingSecretChat = false;
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            Builder builder = new Builder(context);
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            builder.setMessage(LocaleController.getString("CreateEncryptedChatError", C1067R.string.CreateEncryptedChatError));
            builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
            builder.show().setCanceledOnTouchOutside(true);
        }
    }

    public /* synthetic */ void lambda$null$28$SecretChatHelper(Context context, AlertDialog alertDialog) {
        this.startingSecretChat = false;
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public /* synthetic */ void lambda$startSecretChat$30$SecretChatHelper(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }
}