package org.telegram.messenger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatPhoto;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputFileLocation;
import org.telegram.tgnet.TLRPC.InputMedia;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.InputStickeredMedia;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.Page;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.StickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;
import org.telegram.tgnet.TLRPC.TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC.TL_account_getWallPapers;
import org.telegram.tgnet.TLRPC.TL_channel;
import org.telegram.tgnet.TLRPC.TL_channels_getChannels;
import org.telegram.tgnet.TLRPC.TL_channels_getMessages;
import org.telegram.tgnet.TLRPC.TL_chat;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputDocumentFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputMediaDocument;
import org.telegram.tgnet.TLRPC.TL_inputMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_inputMessagesFilterChatPhotos;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerChat;
import org.telegram.tgnet.TLRPC.TL_inputPeerPhotoFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_inputPhotoFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputSingleMedia;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC.TL_inputStickeredMediaDocument;
import org.telegram.tgnet.TLRPC.TL_inputStickeredMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_inputWallPaper;
import org.telegram.tgnet.TLRPC.TL_messages_editMessage;
import org.telegram.tgnet.TLRPC.TL_messages_faveSticker;
import org.telegram.tgnet.TLRPC.TL_messages_getAttachedStickers;
import org.telegram.tgnet.TLRPC.TL_messages_getChats;
import org.telegram.tgnet.TLRPC.TL_messages_getFavedStickers;
import org.telegram.tgnet.TLRPC.TL_messages_getMessages;
import org.telegram.tgnet.TLRPC.TL_messages_getRecentStickers;
import org.telegram.tgnet.TLRPC.TL_messages_getSavedGifs;
import org.telegram.tgnet.TLRPC.TL_messages_getStickerSet;
import org.telegram.tgnet.TLRPC.TL_messages_getWebPage;
import org.telegram.tgnet.TLRPC.TL_messages_saveGif;
import org.telegram.tgnet.TLRPC.TL_messages_saveRecentSticker;
import org.telegram.tgnet.TLRPC.TL_messages_search;
import org.telegram.tgnet.TLRPC.TL_messages_sendMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendMultiMedia;
import org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC.TL_photos_getUserPhotos;
import org.telegram.tgnet.TLRPC.TL_users_getUsers;
import org.telegram.tgnet.TLRPC.TL_wallPaper;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.UserProfilePhoto;
import org.telegram.tgnet.TLRPC.WebPage;

public class FileRefController {
    private static volatile FileRefController[] Instance = new FileRefController[3];
    private int currentAccount;
    private long lastCleanupTime = SystemClock.uptimeMillis();
    private HashMap<String, ArrayList<Requester>> locationRequester = new HashMap();
    private HashMap<TL_messages_sendMultiMedia, Object[]> multiMediaCache = new HashMap();
    private HashMap<String, ArrayList<Requester>> parentRequester = new HashMap();
    private HashMap<String, CachedResult> responseCache = new HashMap();

    private class CachedResult {
        private long firstQueryTime;
        private long lastQueryTime;
        private TLObject response;

        private CachedResult() {
        }
    }

    private class Requester {
        private Object[] args;
        private boolean completed;
        private InputFileLocation location;
        private String locationKey;

        private Requester() {
        }
    }

    static /* synthetic */ void lambda$onUpdateObjectReference$18(TLObject tLObject, TL_error tL_error) {
    }

    static /* synthetic */ void lambda$onUpdateObjectReference$19(TLObject tLObject, TL_error tL_error) {
    }

    static /* synthetic */ void lambda$onUpdateObjectReference$20(TLObject tLObject, TL_error tL_error) {
    }

    public static FileRefController getInstance(int i) {
        FileRefController fileRefController = Instance[i];
        if (fileRefController == null) {
            synchronized (FileRefController.class) {
                fileRefController = Instance[i];
                if (fileRefController == null) {
                    FileRefController[] fileRefControllerArr = Instance;
                    FileRefController fileRefController2 = new FileRefController(i);
                    fileRefControllerArr[i] = fileRefController2;
                    fileRefController = fileRefController2;
                }
            }
        }
        return fileRefController;
    }

    public FileRefController(int i) {
        this.currentAccount = i;
    }

    public static String getKeyForParentObject(Object obj) {
        String str = "_";
        String str2 = "message";
        int channelId;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String str3;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            channelId = messageObject.getChannelId();
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(messageObject.getRealId());
            stringBuilder.append(str);
            stringBuilder.append(channelId);
            return stringBuilder.toString();
        } else if (obj instanceof Message) {
            Message message = (Message) obj;
            Peer peer = message.to_id;
            channelId = peer != null ? peer.channel_id : 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(message.f461id);
            stringBuilder.append(str);
            stringBuilder.append(channelId);
            return stringBuilder.toString();
        } else if (obj instanceof WebPage) {
            WebPage webPage = (WebPage) obj;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("webpage");
            stringBuilder2.append(webPage.f539id);
            return stringBuilder2.toString();
        } else if (obj instanceof User) {
            User user = (User) obj;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("user");
            stringBuilder2.append(user.f534id);
            return stringBuilder2.toString();
        } else if (obj instanceof Chat) {
            Chat chat = (Chat) obj;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("chat");
            stringBuilder2.append(chat.f434id);
            return stringBuilder2.toString();
        } else if (obj instanceof String) {
            str3 = (String) obj;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("str");
            stringBuilder2.append(str3);
            return stringBuilder2.toString();
        } else {
            str = "set";
            if (obj instanceof TL_messages_stickerSet) {
                TL_messages_stickerSet tL_messages_stickerSet = (TL_messages_stickerSet) obj;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(tL_messages_stickerSet.set.f466id);
                return stringBuilder2.toString();
            } else if (obj instanceof StickerSetCovered) {
                StickerSetCovered stickerSetCovered = (StickerSetCovered) obj;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(stickerSetCovered.set.f466id);
                return stringBuilder2.toString();
            } else if (obj instanceof InputStickerSet) {
                InputStickerSet inputStickerSet = (InputStickerSet) obj;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(inputStickerSet.f460id);
                return stringBuilder2.toString();
            } else if (obj instanceof TL_wallPaper) {
                TL_wallPaper tL_wallPaper = (TL_wallPaper) obj;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("wallpaper");
                stringBuilder2.append(tL_wallPaper.f569id);
                return stringBuilder2.toString();
            } else {
                if (obj != null) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("");
                    stringBuilder2.append(obj);
                    str3 = stringBuilder2.toString();
                } else {
                    str3 = null;
                }
                return str3;
            }
        }
    }

    public void requestReference(Object obj, Object... objArr) {
        StringBuilder stringBuilder;
        InputFileLocation tL_inputDocumentFileLocation;
        if (BuildVars.LOGS_ENABLED) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("start loading request reference for parent = ");
            stringBuilder.append(obj);
            stringBuilder.append(" args = ");
            stringBuilder.append(objArr[0]);
            FileLog.m27d(stringBuilder.toString());
        }
        int i = 1;
        String str = "photo_";
        String str2 = "file_";
        InputMedia inputMedia;
        TL_inputMediaDocument tL_inputMediaDocument;
        StringBuilder stringBuilder2;
        TL_inputMediaPhoto tL_inputMediaPhoto;
        StringBuilder stringBuilder3;
        if (objArr[0] instanceof TL_inputSingleMedia) {
            inputMedia = ((TL_inputSingleMedia) objArr[0]).media;
            if (inputMedia instanceof TL_inputMediaDocument) {
                tL_inputMediaDocument = (TL_inputMediaDocument) inputMedia;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(tL_inputMediaDocument.f545id.f452id);
                str = stringBuilder2.toString();
                tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaDocument.f545id.f452id;
            } else if (inputMedia instanceof TL_inputMediaPhoto) {
                tL_inputMediaPhoto = (TL_inputMediaPhoto) inputMedia;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(tL_inputMediaPhoto.f547id.f459id);
                str = stringBuilder3.toString();
                tL_inputDocumentFileLocation = new TL_inputPhotoFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaPhoto.f547id.f459id;
            } else {
                sendErrorToObject(objArr, 0);
                return;
            }
        } else if (objArr[0] instanceof TL_messages_sendMultiMedia) {
            TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TL_messages_sendMultiMedia) objArr[0];
            ArrayList arrayList = (ArrayList) obj;
            this.multiMediaCache.put(tL_messages_sendMultiMedia, objArr);
            int size = tL_messages_sendMultiMedia.multi_media.size();
            for (int i2 = 0; i2 < size; i2++) {
                TL_inputSingleMedia tL_inputSingleMedia = (TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2);
                Object obj2 = arrayList.get(i2);
                if (obj2 != null) {
                    requestReference(obj2, tL_inputSingleMedia, tL_messages_sendMultiMedia);
                }
            }
            return;
        } else if (objArr[0] instanceof TL_messages_sendMedia) {
            inputMedia = ((TL_messages_sendMedia) objArr[0]).media;
            if (inputMedia instanceof TL_inputMediaDocument) {
                tL_inputMediaDocument = (TL_inputMediaDocument) inputMedia;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(tL_inputMediaDocument.f545id.f452id);
                str = stringBuilder2.toString();
                tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaDocument.f545id.f452id;
            } else if (inputMedia instanceof TL_inputMediaPhoto) {
                tL_inputMediaPhoto = (TL_inputMediaPhoto) inputMedia;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(tL_inputMediaPhoto.f547id.f459id);
                str = stringBuilder3.toString();
                tL_inputDocumentFileLocation = new TL_inputPhotoFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaPhoto.f547id.f459id;
            } else {
                sendErrorToObject(objArr, 0);
                return;
            }
        } else if (objArr[0] instanceof TL_messages_editMessage) {
            inputMedia = ((TL_messages_editMessage) objArr[0]).media;
            if (inputMedia instanceof TL_inputMediaDocument) {
                tL_inputMediaDocument = (TL_inputMediaDocument) inputMedia;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(tL_inputMediaDocument.f545id.f452id);
                str = stringBuilder2.toString();
                tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaDocument.f545id.f452id;
            } else if (inputMedia instanceof TL_inputMediaPhoto) {
                tL_inputMediaPhoto = (TL_inputMediaPhoto) inputMedia;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(tL_inputMediaPhoto.f547id.f459id);
                str = stringBuilder3.toString();
                tL_inputDocumentFileLocation = new TL_inputPhotoFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputMediaPhoto.f547id.f459id;
            } else {
                sendErrorToObject(objArr, 0);
                return;
            }
        } else if (objArr[0] instanceof TL_messages_saveGif) {
            TL_messages_saveGif tL_messages_saveGif = (TL_messages_saveGif) objArr[0];
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(tL_messages_saveGif.f511id.f452id);
            str = stringBuilder2.toString();
            tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation.f455id = tL_messages_saveGif.f511id.f452id;
        } else if (objArr[0] instanceof TL_messages_saveRecentSticker) {
            TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TL_messages_saveRecentSticker) objArr[0];
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(tL_messages_saveRecentSticker.f512id.f452id);
            str = stringBuilder2.toString();
            tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation.f455id = tL_messages_saveRecentSticker.f512id.f452id;
        } else if (objArr[0] instanceof TL_messages_faveSticker) {
            TL_messages_faveSticker tL_messages_faveSticker = (TL_messages_faveSticker) objArr[0];
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(tL_messages_faveSticker.f499id.f452id);
            str = stringBuilder2.toString();
            tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation.f455id = tL_messages_faveSticker.f499id.f452id;
        } else if (objArr[0] instanceof TL_messages_getAttachedStickers) {
            InputStickeredMedia inputStickeredMedia = ((TL_messages_getAttachedStickers) objArr[0]).media;
            if (inputStickeredMedia instanceof TL_inputStickeredMediaDocument) {
                TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TL_inputStickeredMediaDocument) inputStickeredMedia;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(tL_inputStickeredMediaDocument.f550id.f452id);
                str = stringBuilder2.toString();
                tL_inputDocumentFileLocation = new TL_inputDocumentFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputStickeredMediaDocument.f550id.f452id;
            } else if (inputStickeredMedia instanceof TL_inputStickeredMediaPhoto) {
                TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TL_inputStickeredMediaPhoto) inputStickeredMedia;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(tL_inputStickeredMediaPhoto.f551id.f459id);
                str = stringBuilder3.toString();
                tL_inputDocumentFileLocation = new TL_inputPhotoFileLocation();
                tL_inputDocumentFileLocation.f455id = tL_inputStickeredMediaPhoto.f551id.f459id;
            } else {
                sendErrorToObject(objArr, 0);
                return;
            }
        } else if (objArr[0] instanceof TL_inputFileLocation) {
            tL_inputDocumentFileLocation = (TL_inputFileLocation) objArr[0];
            stringBuilder = new StringBuilder();
            stringBuilder.append("loc_");
            stringBuilder.append(tL_inputDocumentFileLocation.local_id);
            stringBuilder.append("_");
            stringBuilder.append(tL_inputDocumentFileLocation.volume_id);
            str = stringBuilder.toString();
        } else if (objArr[0] instanceof TL_inputDocumentFileLocation) {
            InputFileLocation inputFileLocation = (TL_inputDocumentFileLocation) objArr[0];
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(inputFileLocation.f455id);
            str = stringBuilder2.toString();
            tL_inputDocumentFileLocation = inputFileLocation;
        } else if (objArr[0] instanceof TL_inputPhotoFileLocation) {
            tL_inputDocumentFileLocation = (TL_inputPhotoFileLocation) objArr[0];
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(tL_inputDocumentFileLocation.f455id);
            str = stringBuilder.toString();
        } else {
            sendErrorToObject(objArr, 0);
            return;
        }
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getRealId() < 0) {
                WebPage webPage = messageObject.messageOwner.media.webpage;
                if (webPage != null) {
                    obj = webPage;
                }
            }
        }
        String keyForParentObject = getKeyForParentObject(obj);
        if (keyForParentObject == null) {
            sendErrorToObject(objArr, 0);
            return;
        }
        Requester requester = new Requester();
        requester.args = objArr;
        requester.location = tL_inputDocumentFileLocation;
        requester.locationKey = str;
        ArrayList arrayList2 = (ArrayList) this.locationRequester.get(str);
        if (arrayList2 == null) {
            arrayList2 = new ArrayList();
            this.locationRequester.put(str, arrayList2);
        } else {
            i = 0;
        }
        arrayList2.add(requester);
        arrayList2 = (ArrayList) this.parentRequester.get(keyForParentObject);
        if (arrayList2 == null) {
            arrayList2 = new ArrayList();
            this.parentRequester.put(keyForParentObject, arrayList2);
            i++;
        }
        arrayList2.add(requester);
        if (i == 2) {
            cleanupCache();
            CachedResult cachedResponse = getCachedResponse(str);
            if (cachedResponse == null) {
                cachedResponse = getCachedResponse(keyForParentObject);
                if (cachedResponse != null) {
                    if (!onRequestComplete(str, keyForParentObject, cachedResponse.response, false)) {
                        this.responseCache.remove(keyForParentObject);
                    } else {
                        return;
                    }
                }
            } else if (!onRequestComplete(str, keyForParentObject, cachedResponse.response, false)) {
                this.responseCache.remove(str);
            } else {
                return;
            }
            requestReferenceFromServer(obj, str, keyForParentObject, objArr);
        }
    }

    private void requestReferenceFromServer(Object obj, String str, String str2, Object[] objArr) {
        int channelId;
        TL_messages_getMessages tL_messages_getMessages;
        TL_messages_getStickerSet tL_messages_getStickerSet;
        InputStickerSet inputStickerSet;
        StickerSet stickerSet;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            channelId = messageObject.getChannelId();
            if (channelId != 0) {
                TL_channels_getMessages tL_channels_getMessages = new TL_channels_getMessages();
                tL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(channelId);
                tL_channels_getMessages.f475id.add(Integer.valueOf(messageObject.getRealId()));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getMessages, new C3410-$$Lambda$FileRefController$bJSiLyN_Loo2lNffdEwzSUZ6du0(this, str, str2));
                return;
            }
            tL_messages_getMessages = new TL_messages_getMessages();
            tL_messages_getMessages.f506id.add(Integer.valueOf(messageObject.getRealId()));
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getMessages, new C3414-$$Lambda$FileRefController$iH_RpP96708b-YCozHWKOTcxbr4(this, str, str2));
        } else if (obj instanceof TL_wallPaper) {
            TL_wallPaper tL_wallPaper = (TL_wallPaper) obj;
            TL_account_getWallPaper tL_account_getWallPaper = new TL_account_getWallPaper();
            TL_inputWallPaper tL_inputWallPaper = new TL_inputWallPaper();
            tL_inputWallPaper.f552id = tL_wallPaper.f569id;
            tL_inputWallPaper.access_hash = tL_wallPaper.access_hash;
            tL_account_getWallPaper.wallpaper = tL_inputWallPaper;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getWallPaper, new C3415-$$Lambda$FileRefController$l7gUgVrfRVPQOPlPV4-4bUexFYw(this, str, str2));
        } else if (obj instanceof WebPage) {
            WebPage webPage = (WebPage) obj;
            TL_messages_getWebPage tL_messages_getWebPage = new TL_messages_getWebPage();
            tL_messages_getWebPage.url = webPage.url;
            tL_messages_getWebPage.hash = 0;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getWebPage, new C3409-$$Lambda$FileRefController$ZZalHmw878mE45n2TIDtpdup6rk(this, str, str2));
        } else if (obj instanceof User) {
            User user = (User) obj;
            TL_users_getUsers tL_users_getUsers = new TL_users_getUsers();
            tL_users_getUsers.f531id.add(MessagesController.getInstance(this.currentAccount).getInputUser(user));
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_users_getUsers, new C3406-$$Lambda$FileRefController$PPQViQbtvXgiD0HkQf0oK3_ZBkE(this, str, str2));
        } else if (obj instanceof Chat) {
            Chat chat = (Chat) obj;
            if (chat instanceof TL_chat) {
                TL_messages_getChats tL_messages_getChats = new TL_messages_getChats();
                tL_messages_getChats.f502id.add(Integer.valueOf(chat.f434id));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getChats, new C3403-$$Lambda$FileRefController$7qx1abCFn6GfdxglKDbFuGfloVQ(this, str, str2));
            } else if (chat instanceof TL_channel) {
                TL_channels_getChannels tL_channels_getChannels = new TL_channels_getChannels();
                tL_channels_getChannels.f474id.add(MessagesController.getInputChannel(chat));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getChannels, new C3405-$$Lambda$FileRefController$MN7WJPmFdFnXGwTYeLjLskDZA_s(this, str, str2));
            }
        } else if (obj instanceof String) {
            String str3 = (String) obj;
            if ("wallpaper".equals(str3)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getWallPapers(), new C3413-$$Lambda$FileRefController$hU-_PNtgoV5UVJ0PgXNS0w6t-Ck(this, str, str2));
            } else if (str3.startsWith("gif")) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getSavedGifs(), new C3408-$$Lambda$FileRefController$XmvlggjDShdh6wwrH2NBXtZN860(this, str, str2));
            } else if ("recent".equals(str3)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getRecentStickers(), new C3399-$$Lambda$FileRefController$1aFkU4DGV4no_EoB8k2dbx7DLBY(this, str, str2));
            } else if ("fav".equals(str3)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getFavedStickers(), new C3401-$$Lambda$FileRefController$4w92eKYUWTkjdDZrk7Ab6kZgCz4(this, str, str2));
            } else if (str3.startsWith("avatar_")) {
                int intValue = Utilities.parseInt(str3).intValue();
                if (intValue > 0) {
                    TL_photos_getUserPhotos tL_photos_getUserPhotos = new TL_photos_getUserPhotos();
                    tL_photos_getUserPhotos.limit = 80;
                    tL_photos_getUserPhotos.offset = 0;
                    tL_photos_getUserPhotos.max_id = 0;
                    tL_photos_getUserPhotos.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(intValue);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_photos_getUserPhotos, new C3416-$$Lambda$FileRefController$lc6KWNmCjlTmUjUWy2DU6yide1g(this, str, str2));
                    return;
                }
                TL_messages_search tL_messages_search = new TL_messages_search();
                tL_messages_search.filter = new TL_inputMessagesFilterChatPhotos();
                tL_messages_search.limit = 80;
                tL_messages_search.offset_id = 0;
                tL_messages_search.f513q = "";
                tL_messages_search.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(intValue);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_search, new C3407-$$Lambda$FileRefController$WYAn7hVey6Q-NHtwvjaWCOxf8sA(this, str, str2));
            } else if (str3.startsWith("sent_")) {
                String[] split = str3.split("_");
                if (split.length == 3) {
                    channelId = Utilities.parseInt(split[1]).intValue();
                    if (channelId != 0) {
                        TL_channels_getMessages tL_channels_getMessages2 = new TL_channels_getMessages();
                        tL_channels_getMessages2.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(channelId);
                        tL_channels_getMessages2.f475id.add(Utilities.parseInt(split[2]));
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getMessages2, new C3404-$$Lambda$FileRefController$AUpNheLWJQs7kyRgJ6tIGIQTBNY(this, str, str2));
                        return;
                    }
                    tL_messages_getMessages = new TL_messages_getMessages();
                    tL_messages_getMessages.f506id.add(Utilities.parseInt(split[2]));
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getMessages, new C3417-$$Lambda$FileRefController$oHc4Ko0S36uJ174mUmftFNV5oEU(this, str, str2));
                    return;
                }
                sendErrorToObject(objArr, 0);
            } else {
                sendErrorToObject(objArr, 0);
            }
        } else if (obj instanceof TL_messages_stickerSet) {
            TL_messages_stickerSet tL_messages_stickerSet = (TL_messages_stickerSet) obj;
            tL_messages_getStickerSet = new TL_messages_getStickerSet();
            tL_messages_getStickerSet.stickerset = new TL_inputStickerSetID();
            inputStickerSet = tL_messages_getStickerSet.stickerset;
            stickerSet = tL_messages_stickerSet.set;
            inputStickerSet.f460id = stickerSet.f466id;
            inputStickerSet.access_hash = stickerSet.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickerSet, new C3411-$$Lambda$FileRefController$exbLp78Kychyc6GzkLXULN8LNGg(this, str, str2));
        } else if (obj instanceof StickerSetCovered) {
            StickerSetCovered stickerSetCovered = (StickerSetCovered) obj;
            tL_messages_getStickerSet = new TL_messages_getStickerSet();
            tL_messages_getStickerSet.stickerset = new TL_inputStickerSetID();
            inputStickerSet = tL_messages_getStickerSet.stickerset;
            stickerSet = stickerSetCovered.set;
            inputStickerSet.f460id = stickerSet.f466id;
            inputStickerSet.access_hash = stickerSet.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickerSet, new C3418-$$Lambda$FileRefController$oQwONz9oH9KbjBHI1GVgQcuvYoU(this, str, str2));
        } else if (obj instanceof InputStickerSet) {
            tL_messages_getStickerSet = new TL_messages_getStickerSet();
            tL_messages_getStickerSet.stickerset = (InputStickerSet) obj;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickerSet, new C3419-$$Lambda$FileRefController$taVyElfntghqykv41aff7_1-UM8(this, str, str2));
        } else {
            sendErrorToObject(objArr, 0);
        }
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$0$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$1$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$2$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$3$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$4$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$5$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$6$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$7$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$8$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$9$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$10$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$11$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$12$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$13$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, false);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$14$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, false);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$15$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$16$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    public /* synthetic */ void lambda$requestReferenceFromServer$17$FileRefController(String str, String str2, TLObject tLObject, TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, true);
    }

    private void onUpdateObjectReference(Requester requester, byte[] bArr, InputFileLocation inputFileLocation) {
        byte[] bArr2 = bArr;
        InputFileLocation inputFileLocation2 = inputFileLocation;
        if (BuildVars.DEBUG_VERSION) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fileref updated for ");
            stringBuilder.append(requester.args[0]);
            stringBuilder.append(" ");
            stringBuilder.append(requester.locationKey);
            FileLog.m27d(stringBuilder.toString());
        }
        InputMedia inputMedia;
        if (requester.args[0] instanceof TL_inputSingleMedia) {
            TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TL_messages_sendMultiMedia) requester.args[1];
            Object[] objArr = (Object[]) this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr != null) {
                TL_inputSingleMedia tL_inputSingleMedia = (TL_inputSingleMedia) requester.args[0];
                InputMedia inputMedia2 = tL_inputSingleMedia.media;
                if (inputMedia2 instanceof TL_inputMediaDocument) {
                    ((TL_inputMediaDocument) inputMedia2).f545id.file_reference = bArr2;
                } else if (inputMedia2 instanceof TL_inputMediaPhoto) {
                    ((TL_inputMediaPhoto) inputMedia2).f547id.file_reference = bArr2;
                }
                int indexOf = tL_messages_sendMultiMedia.multi_media.indexOf(tL_inputSingleMedia);
                if (indexOf >= 0) {
                    ArrayList arrayList = (ArrayList) objArr[3];
                    arrayList.set(indexOf, null);
                    Object obj = 1;
                    for (indexOf = 0; indexOf < arrayList.size(); indexOf++) {
                        if (arrayList.get(indexOf) != null) {
                            obj = null;
                        }
                    }
                    if (obj != null) {
                        this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                        SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (DelayedMessage) objArr[4]);
                    }
                }
            }
        } else if (requester.args[0] instanceof TL_messages_sendMedia) {
            inputMedia = ((TL_messages_sendMedia) requester.args[0]).media;
            if (inputMedia instanceof TL_inputMediaDocument) {
                ((TL_inputMediaDocument) inputMedia).f545id.file_reference = bArr2;
            } else if (inputMedia instanceof TL_inputMediaPhoto) {
                ((TL_inputMediaPhoto) inputMedia).f547id.file_reference = bArr2;
            }
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (DelayedMessage) requester.args[5], null);
        } else if (requester.args[0] instanceof TL_messages_editMessage) {
            inputMedia = ((TL_messages_editMessage) requester.args[0]).media;
            if (inputMedia instanceof TL_inputMediaDocument) {
                ((TL_inputMediaDocument) inputMedia).f545id.file_reference = bArr2;
            } else if (inputMedia instanceof TL_inputMediaPhoto) {
                ((TL_inputMediaPhoto) inputMedia).f547id.file_reference = bArr2;
            }
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (DelayedMessage) requester.args[5], null);
        } else if (requester.args[0] instanceof TL_messages_saveGif) {
            TL_messages_saveGif tL_messages_saveGif = (TL_messages_saveGif) requester.args[0];
            tL_messages_saveGif.f511id.file_reference = bArr2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_saveGif, C3412-$$Lambda$FileRefController$glsZ-ebv4-mT6CRmECvMkMDX4tM.INSTANCE);
        } else if (requester.args[0] instanceof TL_messages_saveRecentSticker) {
            TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TL_messages_saveRecentSticker) requester.args[0];
            tL_messages_saveRecentSticker.f512id.file_reference = bArr2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_saveRecentSticker, C3402-$$Lambda$FileRefController$7dnf8o-vZU8kWj-oHiGfTHxk_5E.INSTANCE);
        } else if (requester.args[0] instanceof TL_messages_faveSticker) {
            TL_messages_faveSticker tL_messages_faveSticker = (TL_messages_faveSticker) requester.args[0];
            tL_messages_faveSticker.f499id.file_reference = bArr2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_faveSticker, C3400-$$Lambda$FileRefController$2YbOQ-Rvo_LvdJ_-ALCga2DKRrU.INSTANCE);
        } else if (requester.args[0] instanceof TL_messages_getAttachedStickers) {
            TL_messages_getAttachedStickers tL_messages_getAttachedStickers = (TL_messages_getAttachedStickers) requester.args[0];
            InputStickeredMedia inputStickeredMedia = tL_messages_getAttachedStickers.media;
            if (inputStickeredMedia instanceof TL_inputStickeredMediaDocument) {
                ((TL_inputStickeredMediaDocument) inputStickeredMedia).f550id.file_reference = bArr2;
            } else if (inputStickeredMedia instanceof TL_inputStickeredMediaPhoto) {
                ((TL_inputStickeredMediaPhoto) inputStickeredMedia).f551id.file_reference = bArr2;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getAttachedStickers, (RequestDelegate) requester.args[1]);
        } else if (requester.args[1] instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) requester.args[1];
            if (inputFileLocation2 != null) {
                fileLoadOperation.location = inputFileLocation2;
            } else {
                requester.location.file_reference = bArr2;
            }
            fileLoadOperation.requestingReference = false;
            fileLoadOperation.startDownloadRequest();
        }
    }

    private void sendErrorToObject(Object[] objArr, int i) {
        if (objArr[0] instanceof TL_inputSingleMedia) {
            TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TL_messages_sendMultiMedia) objArr[1];
            objArr = (Object[]) this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr != null) {
                this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (DelayedMessage) objArr[4]);
            }
        } else if ((objArr[0] instanceof TL_messages_sendMedia) || (objArr[0] instanceof TL_messages_editMessage)) {
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject) objArr[0], (MessageObject) objArr[1], (String) objArr[2], (DelayedMessage) objArr[3], ((Boolean) objArr[4]).booleanValue(), (DelayedMessage) objArr[5], null);
        } else if (objArr[0] instanceof TL_messages_saveGif) {
            TL_messages_saveGif tL_messages_saveGif = (TL_messages_saveGif) objArr[0];
        } else if (objArr[0] instanceof TL_messages_saveRecentSticker) {
            TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TL_messages_saveRecentSticker) objArr[0];
        } else if (objArr[0] instanceof TL_messages_faveSticker) {
            TL_messages_faveSticker tL_messages_faveSticker = (TL_messages_faveSticker) objArr[0];
        } else if (objArr[0] instanceof TL_messages_getAttachedStickers) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TL_messages_getAttachedStickers) objArr[0], (RequestDelegate) objArr[1]);
        } else if (i == 0) {
            TL_error tL_error = new TL_error();
            tL_error.text = "not found parent object to request reference";
            tL_error.code = 400;
            if (objArr[1] instanceof FileLoadOperation) {
                FileLoadOperation fileLoadOperation = (FileLoadOperation) objArr[1];
                fileLoadOperation.requestingReference = false;
                fileLoadOperation.processRequestResult((RequestInfo) objArr[2], tL_error);
            }
        } else if (i == 1 && (objArr[1] instanceof FileLoadOperation)) {
            FileLoadOperation fileLoadOperation2 = (FileLoadOperation) objArr[1];
            fileLoadOperation2.requestingReference = false;
            fileLoadOperation2.onFail(false, 0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:78:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0155 A:{LOOP_END, LOOP:2: B:37:0x009b->B:74:0x0155} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0103 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0103 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0155 A:{LOOP_END, LOOP:2: B:37:0x009b->B:74:0x0155} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x040c  */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x03fe  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0056 A:{RETURN} */
    private boolean onRequestComplete(java.lang.String r25, java.lang.String r26, org.telegram.tgnet.TLObject r27, boolean r28) {
        /*
        r24 = this;
        r0 = r24;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r4 = 0;
        r6 = 1;
        if (r2 == 0) goto L_0x004b;
    L_0x000c:
        r7 = r0.parentRequester;
        r7 = r7.get(r2);
        r7 = (java.util.ArrayList) r7;
        if (r7 == 0) goto L_0x004b;
    L_0x0016:
        r8 = r7.size();
        r9 = 0;
        r10 = 0;
    L_0x001c:
        if (r9 >= r8) goto L_0x0040;
    L_0x001e:
        r11 = r7.get(r9);
        r11 = (org.telegram.messenger.FileRefController.Requester) r11;
        r12 = r11.completed;
        if (r12 == 0) goto L_0x002b;
    L_0x002a:
        goto L_0x003d;
    L_0x002b:
        r11 = r11.locationKey;
        if (r28 == 0) goto L_0x0035;
    L_0x0031:
        if (r10 != 0) goto L_0x0035;
    L_0x0033:
        r12 = 1;
        goto L_0x0036;
    L_0x0035:
        r12 = 0;
    L_0x0036:
        r11 = r0.onRequestComplete(r11, r4, r3, r12);
        if (r11 == 0) goto L_0x003d;
    L_0x003c:
        r10 = 1;
    L_0x003d:
        r9 = r9 + 1;
        goto L_0x001c;
    L_0x0040:
        if (r10 == 0) goto L_0x0045;
    L_0x0042:
        r0.putReponseToCache(r2, r3);
    L_0x0045:
        r7 = r0.parentRequester;
        r7.remove(r2);
        goto L_0x004c;
    L_0x004b:
        r10 = 0;
    L_0x004c:
        r2 = r0.locationRequester;
        r2 = r2.get(r1);
        r2 = (java.util.ArrayList) r2;
        if (r2 != 0) goto L_0x0057;
    L_0x0056:
        return r10;
    L_0x0057:
        r7 = r2.size();
        r9 = r4;
        r11 = r9;
        r12 = r11;
        r8 = 0;
    L_0x005f:
        if (r8 >= r7) goto L_0x041d;
    L_0x0061:
        r13 = r2.get(r8);
        r13 = (org.telegram.messenger.FileRefController.Requester) r13;
        r14 = r13.completed;
        if (r14 == 0) goto L_0x0074;
    L_0x006d:
        r26 = r2;
        r14 = r4;
        r2 = 0;
        r5 = 1;
        goto L_0x0415;
    L_0x0074:
        r14 = r13.location;
        r14 = r14 instanceof org.telegram.tgnet.TLRPC.TL_inputFileLocation;
        if (r14 == 0) goto L_0x0080;
    L_0x007c:
        r11 = new org.telegram.tgnet.TLRPC.InputFileLocation[r6];
        r9 = new boolean[r6];
    L_0x0080:
        r13.completed = r6;
        r14 = r3 instanceof org.telegram.tgnet.TLRPC.messages_Messages;
        if (r14 == 0) goto L_0x0192;
    L_0x0087:
        r14 = r3;
        r14 = (org.telegram.tgnet.TLRPC.messages_Messages) r14;
        r15 = r14.messages;
        r15 = r15.isEmpty();
        if (r15 != 0) goto L_0x018f;
    L_0x0092:
        r15 = r14.messages;
        r15 = r15.size();
        r16 = r12;
        r12 = 0;
    L_0x009b:
        if (r12 >= r15) goto L_0x015d;
    L_0x009d:
        r4 = r14.messages;
        r4 = r4.get(r12);
        r4 = (org.telegram.tgnet.TLRPC.Message) r4;
        r6 = r4.media;
        if (r6 == 0) goto L_0x00ef;
    L_0x00a9:
        r5 = r6.document;
        if (r5 == 0) goto L_0x00b6;
    L_0x00ad:
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
        goto L_0x00ff;
    L_0x00b6:
        r5 = r6.game;
        if (r5 == 0) goto L_0x00d5;
    L_0x00ba:
        r5 = r5.document;
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
        if (r5 != 0) goto L_0x00ff;
    L_0x00c6:
        r5 = r4.media;
        r5 = r5.game;
        r5 = r5.photo;
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
        goto L_0x00ff;
    L_0x00d5:
        r5 = r6.photo;
        if (r5 == 0) goto L_0x00e2;
    L_0x00d9:
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
        goto L_0x00ff;
    L_0x00e2:
        r5 = r6.webpage;
        if (r5 == 0) goto L_0x0101;
    L_0x00e6:
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
        goto L_0x00ff;
    L_0x00ef:
        r5 = r4.action;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
        if (r6 == 0) goto L_0x0101;
    L_0x00f5:
        r5 = r5.photo;
        r6 = r13.location;
        r5 = r0.getFileReference(r5, r6, r9, r11);
    L_0x00ff:
        r16 = r5;
    L_0x0101:
        if (r16 == 0) goto L_0x0155;
    L_0x0103:
        if (r28 == 0) goto L_0x015d;
    L_0x0105:
        r5 = r4.to_id;
        if (r5 == 0) goto L_0x0139;
    L_0x0109:
        r5 = r5.channel_id;
        if (r5 == 0) goto L_0x0139;
    L_0x010d:
        r5 = r14.chats;
        r5 = r5.size();
        r6 = 0;
    L_0x0114:
        if (r6 >= r5) goto L_0x0139;
    L_0x0116:
        r12 = r14.chats;
        r12 = r12.get(r6);
        r12 = (org.telegram.tgnet.TLRPC.Chat) r12;
        r15 = r12.f434id;
        r26 = r2;
        r2 = r4.to_id;
        r2 = r2.channel_id;
        if (r15 != r2) goto L_0x0134;
    L_0x0128:
        r2 = r12.megagroup;
        if (r2 == 0) goto L_0x013b;
    L_0x012c:
        r2 = r4.flags;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2 = r2 | r5;
        r4.flags = r2;
        goto L_0x013b;
    L_0x0134:
        r6 = r6 + 1;
        r2 = r26;
        goto L_0x0114;
    L_0x0139:
        r26 = r2;
    L_0x013b:
        r2 = r0.currentAccount;
        r18 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r2 = r0.currentAccount;
        r5 = r14.users;
        r6 = r14.chats;
        r23 = 0;
        r19 = r4;
        r20 = r2;
        r21 = r5;
        r22 = r6;
        r18.replaceMessageIfExists(r19, r20, r21, r22, r23);
        goto L_0x015f;
    L_0x0155:
        r26 = r2;
        r12 = r12 + 1;
        r4 = 0;
        r6 = 1;
        goto L_0x009b;
    L_0x015d:
        r26 = r2;
    L_0x015f:
        r12 = r16;
        if (r12 != 0) goto L_0x01a3;
    L_0x0163:
        r2 = r0.currentAccount;
        r18 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r2 = r14.messages;
        r4 = 0;
        r2 = r2.get(r4);
        r19 = r2;
        r19 = (org.telegram.tgnet.TLRPC.Message) r19;
        r2 = r0.currentAccount;
        r4 = r14.users;
        r5 = r14.chats;
        r23 = 1;
        r20 = r2;
        r21 = r4;
        r22 = r5;
        r18.replaceMessageIfExists(r19, r20, r21, r22, r23);
        r2 = org.telegram.messenger.BuildVars.DEBUG_VERSION;
        if (r2 == 0) goto L_0x01a3;
    L_0x0189:
        r2 = "file ref not found in messages, replacing message";
        org.telegram.messenger.FileLog.m27d(r2);
        goto L_0x01a3;
    L_0x018f:
        r26 = r2;
        goto L_0x01a3;
    L_0x0192:
        r26 = r2;
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.WebPage;
        if (r2 == 0) goto L_0x01a6;
    L_0x0198:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.WebPage) r2;
        r4 = r13.location;
        r12 = r0.getFileReference(r2, r4, r9, r11);
    L_0x01a3:
        r14 = 0;
        goto L_0x03fc;
    L_0x01a6:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_account_wallPapers;
        if (r2 == 0) goto L_0x01df;
    L_0x01aa:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_account_wallPapers) r2;
        r4 = r2.wallpapers;
        r4 = r4.size();
        r5 = 0;
    L_0x01b4:
        if (r5 >= r4) goto L_0x01ce;
    L_0x01b6:
        r6 = r2.wallpapers;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.TL_wallPaper) r6;
        r6 = r6.document;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x01cb;
    L_0x01ca:
        goto L_0x01ce;
    L_0x01cb:
        r5 = r5 + 1;
        goto L_0x01b4;
    L_0x01ce:
        if (r12 == 0) goto L_0x01a3;
    L_0x01d0:
        if (r28 == 0) goto L_0x01a3;
    L_0x01d2:
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r2 = r2.wallpapers;
        r5 = 1;
        r4.putWallpapers(r2, r5);
        goto L_0x01a3;
    L_0x01df:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_wallPaper;
        if (r2 == 0) goto L_0x0207;
    L_0x01e3:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_wallPaper) r2;
        r4 = r2.document;
        r5 = r13.location;
        r12 = r0.getFileReference(r4, r5, r9, r11);
        if (r12 == 0) goto L_0x01a3;
    L_0x01f2:
        if (r28 == 0) goto L_0x01a3;
    L_0x01f4:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r4.add(r2);
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r5 = 0;
        r2.putWallpapers(r4, r5);
        goto L_0x01a3;
    L_0x0207:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.Vector;
        if (r2 == 0) goto L_0x0299;
    L_0x020b:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.Vector) r2;
        r4 = r2.objects;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x01a3;
    L_0x0216:
        r4 = r2.objects;
        r4 = r4.size();
        r5 = 0;
    L_0x021d:
        if (r5 >= r4) goto L_0x01a3;
    L_0x021f:
        r6 = r2.objects;
        r6 = r6.get(r5);
        r14 = r6 instanceof org.telegram.tgnet.TLRPC.User;
        if (r14 == 0) goto L_0x025c;
    L_0x0229:
        r6 = (org.telegram.tgnet.TLRPC.User) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r28 == 0) goto L_0x0257;
    L_0x0235:
        if (r12 == 0) goto L_0x0257;
    L_0x0237:
        r14 = new java.util.ArrayList;
        r14.<init>();
        r14.add(r6);
        r15 = r0.currentAccount;
        r15 = org.telegram.messenger.MessagesStorage.getInstance(r15);
        r16 = r2;
        r18 = r4;
        r2 = 0;
        r4 = 1;
        r15.putUsersAndChats(r14, r2, r4, r4);
        r2 = new org.telegram.messenger.-$$Lambda$FileRefController$-CZgXfaxqSfurGMxYgHRkXa2trY;
        r2.<init>(r0, r6);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        goto L_0x028e;
    L_0x0257:
        r16 = r2;
        r18 = r4;
        goto L_0x028e;
    L_0x025c:
        r16 = r2;
        r18 = r4;
        r2 = r6 instanceof org.telegram.tgnet.TLRPC.Chat;
        if (r2 == 0) goto L_0x028e;
    L_0x0264:
        r6 = (org.telegram.tgnet.TLRPC.Chat) r6;
        r2 = r13.location;
        r2 = r0.getFileReference(r6, r2, r9, r11);
        if (r28 == 0) goto L_0x028d;
    L_0x0270:
        if (r2 == 0) goto L_0x028d;
    L_0x0272:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r4.add(r6);
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.MessagesStorage.getInstance(r12);
        r14 = 0;
        r15 = 1;
        r12.putUsersAndChats(r14, r4, r15, r15);
        r4 = new org.telegram.messenger.-$$Lambda$FileRefController$ezrB_EEVIghp6y7yWEa40dOLdLU;
        r4.<init>(r0, r6);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
    L_0x028d:
        r12 = r2;
    L_0x028e:
        if (r12 == 0) goto L_0x0292;
    L_0x0290:
        goto L_0x01a3;
    L_0x0292:
        r5 = r5 + 1;
        r2 = r16;
        r4 = r18;
        goto L_0x021d;
    L_0x0299:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messages_chats;
        if (r2 == 0) goto L_0x02e6;
    L_0x029d:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_messages_chats) r2;
        r4 = r2.chats;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x01a3;
    L_0x02a8:
        r4 = r2.chats;
        r4 = r4.size();
        r5 = 0;
    L_0x02af:
        if (r5 >= r4) goto L_0x01a3;
    L_0x02b1:
        r6 = r2.chats;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Chat) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x02e2;
    L_0x02c3:
        if (r28 == 0) goto L_0x01a3;
    L_0x02c5:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r2.add(r6);
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r5 = 1;
        r14 = 0;
        r4.putUsersAndChats(r14, r2, r5, r5);
        r2 = new org.telegram.messenger.-$$Lambda$FileRefController$wxZbkcK98NrwAinOuNo_DdhwDyk;
        r2.<init>(r0, r6);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        goto L_0x03fc;
    L_0x02e2:
        r14 = 0;
        r5 = r5 + 1;
        goto L_0x02af;
    L_0x02e6:
        r14 = 0;
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messages_savedGifs;
        if (r2 == 0) goto L_0x0326;
    L_0x02eb:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_messages_savedGifs) r2;
        r4 = r2.gifs;
        r4 = r4.size();
        r5 = 0;
    L_0x02f5:
        if (r5 >= r4) goto L_0x030d;
    L_0x02f7:
        r6 = r2.gifs;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Document) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x030a;
    L_0x0309:
        goto L_0x030d;
    L_0x030a:
        r5 = r5 + 1;
        goto L_0x02f5;
    L_0x030d:
        if (r28 == 0) goto L_0x03fc;
    L_0x030f:
        r4 = r0.currentAccount;
        r15 = org.telegram.messenger.DataQuery.getInstance(r4);
        r16 = 0;
        r2 = r2.gifs;
        r18 = 1;
        r19 = 0;
        r20 = 1;
        r17 = r2;
        r15.processLoadedRecentDocuments(r16, r17, r18, r19, r20);
        goto L_0x03fc;
    L_0x0326:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
        if (r2 == 0) goto L_0x035a;
    L_0x032a:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r2;
        if (r12 != 0) goto L_0x034e;
    L_0x032f:
        r4 = r2.documents;
        r4 = r4.size();
        r5 = 0;
    L_0x0336:
        if (r5 >= r4) goto L_0x034e;
    L_0x0338:
        r6 = r2.documents;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Document) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x034b;
    L_0x034a:
        goto L_0x034e;
    L_0x034b:
        r5 = r5 + 1;
        goto L_0x0336;
    L_0x034e:
        if (r28 == 0) goto L_0x03fc;
    L_0x0350:
        r4 = new org.telegram.messenger.-$$Lambda$FileRefController$FlgFGmJyAwG8D7Z8OYWnK63ajJo;
        r4.<init>(r0, r2);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x03fc;
    L_0x035a:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messages_recentStickers;
        if (r2 == 0) goto L_0x0398;
    L_0x035e:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_messages_recentStickers) r2;
        r4 = r2.stickers;
        r4 = r4.size();
        r5 = 0;
    L_0x0368:
        if (r5 >= r4) goto L_0x0380;
    L_0x036a:
        r6 = r2.stickers;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Document) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x037d;
    L_0x037c:
        goto L_0x0380;
    L_0x037d:
        r5 = r5 + 1;
        goto L_0x0368;
    L_0x0380:
        if (r28 == 0) goto L_0x03fc;
    L_0x0382:
        r4 = r0.currentAccount;
        r15 = org.telegram.messenger.DataQuery.getInstance(r4);
        r16 = 0;
        r2 = r2.stickers;
        r18 = 0;
        r19 = 0;
        r20 = 1;
        r17 = r2;
        r15.processLoadedRecentDocuments(r16, r17, r18, r19, r20);
        goto L_0x03fc;
    L_0x0398:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messages_favedStickers;
        if (r2 == 0) goto L_0x03d6;
    L_0x039c:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.TL_messages_favedStickers) r2;
        r4 = r2.stickers;
        r4 = r4.size();
        r5 = 0;
    L_0x03a6:
        if (r5 >= r4) goto L_0x03be;
    L_0x03a8:
        r6 = r2.stickers;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Document) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x03bb;
    L_0x03ba:
        goto L_0x03be;
    L_0x03bb:
        r5 = r5 + 1;
        goto L_0x03a6;
    L_0x03be:
        if (r28 == 0) goto L_0x03fc;
    L_0x03c0:
        r4 = r0.currentAccount;
        r15 = org.telegram.messenger.DataQuery.getInstance(r4);
        r16 = 2;
        r2 = r2.stickers;
        r18 = 0;
        r19 = 0;
        r20 = 1;
        r17 = r2;
        r15.processLoadedRecentDocuments(r16, r17, r18, r19, r20);
        goto L_0x03fc;
    L_0x03d6:
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.photos_Photos;
        if (r2 == 0) goto L_0x03fc;
    L_0x03da:
        r2 = r3;
        r2 = (org.telegram.tgnet.TLRPC.photos_Photos) r2;
        r4 = r2.photos;
        r4 = r4.size();
        r5 = 0;
    L_0x03e4:
        if (r5 >= r4) goto L_0x03fc;
    L_0x03e6:
        r6 = r2.photos;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.Photo) r6;
        r12 = r13.location;
        r12 = r0.getFileReference(r6, r12, r9, r11);
        if (r12 == 0) goto L_0x03f9;
    L_0x03f8:
        goto L_0x03fc;
    L_0x03f9:
        r5 = r5 + 1;
        goto L_0x03e4;
    L_0x03fc:
        if (r12 == 0) goto L_0x040c;
    L_0x03fe:
        if (r11 == 0) goto L_0x0404;
    L_0x0400:
        r2 = 0;
        r4 = r11[r2];
        goto L_0x0406;
    L_0x0404:
        r2 = 0;
        r4 = r14;
    L_0x0406:
        r0.onUpdateObjectReference(r13, r12, r4);
        r5 = 1;
        r10 = 1;
        goto L_0x0415;
    L_0x040c:
        r2 = 0;
        r4 = r13.args;
        r5 = 1;
        r0.sendErrorToObject(r4, r5);
    L_0x0415:
        r8 = r8 + 1;
        r2 = r26;
        r4 = r14;
        r6 = 1;
        goto L_0x005f;
    L_0x041d:
        r2 = r0.locationRequester;
        r2.remove(r1);
        if (r10 == 0) goto L_0x0427;
    L_0x0424:
        r0.putReponseToCache(r1, r3);
    L_0x0427:
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileRefController.onRequestComplete(java.lang.String, java.lang.String, org.telegram.tgnet.TLObject, boolean):boolean");
    }

    public /* synthetic */ void lambda$onRequestComplete$21$FileRefController(User user) {
        MessagesController.getInstance(this.currentAccount).putUser(user, false);
    }

    public /* synthetic */ void lambda$onRequestComplete$22$FileRefController(Chat chat) {
        MessagesController.getInstance(this.currentAccount).putChat(chat, false);
    }

    public /* synthetic */ void lambda$onRequestComplete$23$FileRefController(Chat chat) {
        MessagesController.getInstance(this.currentAccount).putChat(chat, false);
    }

    public /* synthetic */ void lambda$onRequestComplete$24$FileRefController(TL_messages_stickerSet tL_messages_stickerSet) {
        DataQuery.getInstance(this.currentAccount).replaceStickerSet(tL_messages_stickerSet);
    }

    private void cleanupCache() {
        if (Math.abs(SystemClock.uptimeMillis() - this.lastCleanupTime) >= 600000) {
            this.lastCleanupTime = SystemClock.uptimeMillis();
            ArrayList arrayList = null;
            for (Entry entry : this.responseCache.entrySet()) {
                if (Math.abs(SystemClock.uptimeMillis() - ((CachedResult) entry.getValue()).firstQueryTime) >= 600000) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(entry.getKey());
                }
            }
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    this.responseCache.remove(arrayList.get(i));
                }
            }
        }
    }

    private CachedResult getCachedResponse(String str) {
        CachedResult cachedResult = (CachedResult) this.responseCache.get(str);
        if (cachedResult == null || Math.abs(SystemClock.uptimeMillis() - cachedResult.firstQueryTime) < 600000) {
            return cachedResult;
        }
        this.responseCache.remove(str);
        return null;
    }

    private void putReponseToCache(String str, TLObject tLObject) {
        CachedResult cachedResult = (CachedResult) this.responseCache.get(str);
        if (cachedResult == null) {
            cachedResult = new CachedResult();
            cachedResult.response = tLObject;
            cachedResult.firstQueryTime = SystemClock.uptimeMillis();
            this.responseCache.put(str, cachedResult);
        }
        cachedResult.lastQueryTime = SystemClock.uptimeMillis();
    }

    private byte[] getFileReference(Document document, InputFileLocation inputFileLocation, boolean[] zArr, InputFileLocation[] inputFileLocationArr) {
        if (!(document == null || inputFileLocation == null)) {
            if (!(inputFileLocation instanceof TL_inputDocumentFileLocation)) {
                int size = document.thumbs.size();
                int i = 0;
                while (i < size) {
                    PhotoSize photoSize = (PhotoSize) document.thumbs.get(i);
                    byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                    if (zArr != null && zArr[0]) {
                        inputFileLocationArr[0] = new TL_inputDocumentFileLocation();
                        inputFileLocationArr[0].f455id = document.f441id;
                        inputFileLocationArr[0].volume_id = inputFileLocation.volume_id;
                        inputFileLocationArr[0].local_id = inputFileLocation.local_id;
                        inputFileLocationArr[0].access_hash = document.access_hash;
                        inputFileLocation = inputFileLocationArr[0];
                        byte[] bArr = document.file_reference;
                        inputFileLocation.file_reference = bArr;
                        inputFileLocationArr[0].thumb_size = photoSize.type;
                        return bArr;
                    } else if (fileReference != null) {
                        return fileReference;
                    } else {
                        i++;
                    }
                }
            } else if (document.f441id == inputFileLocation.f455id) {
                return document.file_reference;
            }
        }
        return null;
    }

    private boolean getPeerReferenceReplacement(User user, Chat chat, boolean z, InputFileLocation inputFileLocation, InputFileLocation[] inputFileLocationArr, boolean[] zArr) {
        if (zArr == null || !zArr[0]) {
            return false;
        }
        InputPeer inputPeer;
        inputFileLocationArr[0] = new TL_inputPeerPhotoFileLocation();
        InputFileLocation inputFileLocation2 = inputFileLocationArr[0];
        long j = inputFileLocation.volume_id;
        inputFileLocation2.f455id = j;
        inputFileLocationArr[0].volume_id = j;
        inputFileLocationArr[0].local_id = inputFileLocation.local_id;
        inputFileLocationArr[0].big = z;
        if (user != null) {
            InputPeer tL_inputPeerUser = new TL_inputPeerUser();
            tL_inputPeerUser.user_id = user.f534id;
            tL_inputPeerUser.access_hash = user.access_hash;
            inputPeer = tL_inputPeerUser;
        } else if (ChatObject.isChannel(chat)) {
            inputPeer = new TL_inputPeerChat();
            inputPeer.chat_id = chat.f434id;
        } else {
            inputPeer = new TL_inputPeerChannel();
            inputPeer.channel_id = chat.f434id;
            inputPeer.access_hash = chat.access_hash;
        }
        inputFileLocationArr[0].peer = inputPeer;
        return true;
    }

    private byte[] getFileReference(User user, InputFileLocation inputFileLocation, boolean[] zArr, InputFileLocation[] inputFileLocationArr) {
        if (user != null) {
            UserProfilePhoto userProfilePhoto = user.photo;
            if (userProfilePhoto != null && (inputFileLocation instanceof TL_inputFileLocation)) {
                byte[] fileReference = getFileReference(userProfilePhoto.photo_small, inputFileLocation, zArr);
                if (getPeerReferenceReplacement(user, null, false, inputFileLocation, inputFileLocationArr, zArr)) {
                    return new byte[0];
                }
                if (fileReference == null) {
                    fileReference = getFileReference(user.photo.photo_big, inputFileLocation, zArr);
                    if (getPeerReferenceReplacement(user, null, true, inputFileLocation, inputFileLocationArr, zArr)) {
                        return new byte[0];
                    }
                }
                return fileReference;
            }
        }
        return null;
    }

    private byte[] getFileReference(Chat chat, InputFileLocation inputFileLocation, boolean[] zArr, InputFileLocation[] inputFileLocationArr) {
        if (chat != null) {
            ChatPhoto chatPhoto = chat.photo;
            if (chatPhoto != null && (inputFileLocation instanceof TL_inputFileLocation)) {
                byte[] fileReference = getFileReference(chatPhoto.photo_small, inputFileLocation, zArr);
                if (getPeerReferenceReplacement(null, chat, false, inputFileLocation, inputFileLocationArr, zArr)) {
                    return new byte[0];
                }
                if (fileReference == null) {
                    fileReference = getFileReference(chat.photo.photo_big, inputFileLocation, zArr);
                    if (getPeerReferenceReplacement(null, chat, true, inputFileLocation, inputFileLocationArr, zArr)) {
                        return new byte[0];
                    }
                }
                return fileReference;
            }
        }
        return null;
    }

    private byte[] getFileReference(Photo photo, InputFileLocation inputFileLocation, boolean[] zArr, InputFileLocation[] inputFileLocationArr) {
        byte[] bArr = null;
        if (photo == null) {
            return null;
        }
        if (inputFileLocation instanceof TL_inputPhotoFileLocation) {
            if (photo.f463id == inputFileLocation.f455id) {
                bArr = photo.file_reference;
            }
            return bArr;
        }
        if (inputFileLocation instanceof TL_inputFileLocation) {
            int size = photo.sizes.size();
            int i = 0;
            while (i < size) {
                PhotoSize photoSize = (PhotoSize) photo.sizes.get(i);
                byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                if (zArr != null && zArr[0]) {
                    inputFileLocationArr[0] = new TL_inputPhotoFileLocation();
                    inputFileLocationArr[0].f455id = photo.f463id;
                    inputFileLocationArr[0].volume_id = inputFileLocation.volume_id;
                    inputFileLocationArr[0].local_id = inputFileLocation.local_id;
                    inputFileLocationArr[0].access_hash = photo.access_hash;
                    inputFileLocation = inputFileLocationArr[0];
                    byte[] bArr2 = photo.file_reference;
                    inputFileLocation.file_reference = bArr2;
                    inputFileLocationArr[0].thumb_size = photoSize.type;
                    return bArr2;
                } else if (fileReference != null) {
                    return fileReference;
                } else {
                    i++;
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(PhotoSize photoSize, InputFileLocation inputFileLocation, boolean[] zArr) {
        return (photoSize == null || !(inputFileLocation instanceof TL_inputFileLocation)) ? null : getFileReference(photoSize.location, inputFileLocation, zArr);
    }

    private byte[] getFileReference(FileLocation fileLocation, InputFileLocation inputFileLocation, boolean[] zArr) {
        if (fileLocation == null || !(inputFileLocation instanceof TL_inputFileLocation) || fileLocation.local_id != inputFileLocation.local_id || fileLocation.volume_id != inputFileLocation.volume_id) {
            return null;
        }
        if (fileLocation.file_reference == null && zArr != null) {
            zArr[0] = true;
        }
        return fileLocation.file_reference;
    }

    private byte[] getFileReference(WebPage webPage, InputFileLocation inputFileLocation, boolean[] zArr, InputFileLocation[] inputFileLocationArr) {
        byte[] fileReference = getFileReference(webPage.document, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference != null) {
            return fileReference;
        }
        fileReference = getFileReference(webPage.photo, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference != null) {
            return fileReference;
        }
        if (fileReference == null) {
            Page page = webPage.cached_page;
            if (page != null) {
                int size = page.documents.size();
                for (int i = 0; i < size; i++) {
                    byte[] fileReference2 = getFileReference((Document) webPage.cached_page.documents.get(i), inputFileLocation, zArr, inputFileLocationArr);
                    if (fileReference2 != null) {
                        return fileReference2;
                    }
                }
                size = webPage.cached_page.photos.size();
                for (int i2 = 0; i2 < size; i2++) {
                    byte[] fileReference3 = getFileReference((Photo) webPage.cached_page.photos.get(i2), inputFileLocation, zArr, inputFileLocationArr);
                    if (fileReference3 != null) {
                        return fileReference3;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isFileRefError(String str) {
        return "FILEREF_EXPIRED".equals(str) || "FILE_REFERENCE_EXPIRED".equals(str) || "FILE_REFERENCE_EMPTY".equals(str) || (str != null && str.startsWith("FILE_REFERENCE_"));
    }
}