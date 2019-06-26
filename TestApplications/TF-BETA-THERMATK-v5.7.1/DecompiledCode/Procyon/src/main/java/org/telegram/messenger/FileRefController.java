// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import java.util.Iterator;
import java.util.Map;
import android.os.SystemClock;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import java.util.HashMap;

public class FileRefController
{
    private static volatile FileRefController[] Instance;
    private int currentAccount;
    private long lastCleanupTime;
    private HashMap<String, ArrayList<Requester>> locationRequester;
    private HashMap<TLRPC.TL_messages_sendMultiMedia, Object[]> multiMediaCache;
    private HashMap<String, ArrayList<Requester>> parentRequester;
    private HashMap<String, CachedResult> responseCache;
    
    static {
        FileRefController.Instance = new FileRefController[3];
    }
    
    public FileRefController(final int currentAccount) {
        this.locationRequester = new HashMap<String, ArrayList<Requester>>();
        this.parentRequester = new HashMap<String, ArrayList<Requester>>();
        this.responseCache = new HashMap<String, CachedResult>();
        this.multiMediaCache = new HashMap<TLRPC.TL_messages_sendMultiMedia, Object[]>();
        this.lastCleanupTime = SystemClock.uptimeMillis();
        this.currentAccount = currentAccount;
    }
    
    private void cleanupCache() {
        if (Math.abs(SystemClock.uptimeMillis() - this.lastCleanupTime) < 600000L) {
            return;
        }
        this.lastCleanupTime = SystemClock.uptimeMillis();
        ArrayList<String> list = null;
        for (final Map.Entry<String, CachedResult> entry : this.responseCache.entrySet()) {
            if (Math.abs(SystemClock.uptimeMillis() - entry.getValue().firstQueryTime) >= 600000L) {
                ArrayList<String> list2;
                if ((list2 = list) == null) {
                    list2 = new ArrayList<String>();
                }
                list2.add(entry.getKey());
                list = list2;
            }
        }
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                this.responseCache.remove(list.get(i));
            }
        }
    }
    
    private CachedResult getCachedResponse(final String s) {
        CachedResult cachedResult2;
        final CachedResult cachedResult = cachedResult2 = this.responseCache.get(s);
        if (cachedResult != null) {
            cachedResult2 = cachedResult;
            if (Math.abs(SystemClock.uptimeMillis() - cachedResult.firstQueryTime) >= 600000L) {
                this.responseCache.remove(s);
                cachedResult2 = null;
            }
        }
        return cachedResult2;
    }
    
    private byte[] getFileReference(final TLRPC.Chat chat, final TLRPC.InputFileLocation inputFileLocation, final boolean[] array, final TLRPC.InputFileLocation[] array2) {
        if (chat != null) {
            final TLRPC.ChatPhoto photo = chat.photo;
            if (photo != null) {
                if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
                    final byte[] fileReference = this.getFileReference(photo.photo_small, inputFileLocation, array);
                    if (this.getPeerReferenceReplacement(null, chat, false, inputFileLocation, array2, array)) {
                        return new byte[0];
                    }
                    byte[] fileReference2;
                    if ((fileReference2 = fileReference) == null) {
                        fileReference2 = this.getFileReference(chat.photo.photo_big, inputFileLocation, array);
                        if (this.getPeerReferenceReplacement(null, chat, true, inputFileLocation, array2, array)) {
                            return new byte[0];
                        }
                    }
                    return fileReference2;
                }
            }
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.Document document, TLRPC.InputFileLocation inputFileLocation, final boolean[] array, final TLRPC.InputFileLocation[] array2) {
        if (document != null) {
            if (inputFileLocation != null) {
                if (inputFileLocation instanceof TLRPC.TL_inputDocumentFileLocation) {
                    if (document.id == inputFileLocation.id) {
                        return document.file_reference;
                    }
                }
                else {
                    for (int size = document.thumbs.size(), i = 0; i < size; ++i) {
                        final TLRPC.PhotoSize photoSize = document.thumbs.get(i);
                        final byte[] fileReference = this.getFileReference(photoSize, inputFileLocation, array);
                        if (array != null && array[0]) {
                            array2[0] = new TLRPC.TL_inputDocumentFileLocation();
                            array2[0].id = document.id;
                            array2[0].volume_id = inputFileLocation.volume_id;
                            array2[0].local_id = inputFileLocation.local_id;
                            array2[0].access_hash = document.access_hash;
                            inputFileLocation = array2[0];
                            final byte[] file_reference = document.file_reference;
                            inputFileLocation.file_reference = file_reference;
                            array2[0].thumb_size = photoSize.type;
                            return file_reference;
                        }
                        if (fileReference != null) {
                            return fileReference;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.FileLocation fileLocation, final TLRPC.InputFileLocation inputFileLocation, final boolean[] array) {
        if (fileLocation != null) {
            if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
                if (fileLocation.local_id == inputFileLocation.local_id && fileLocation.volume_id == inputFileLocation.volume_id) {
                    if (fileLocation.file_reference == null && array != null) {
                        array[0] = true;
                    }
                    return fileLocation.file_reference;
                }
            }
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.Photo photo, TLRPC.InputFileLocation inputFileLocation, final boolean[] array, final TLRPC.InputFileLocation[] array2) {
        final byte[] array3 = null;
        if (photo == null) {
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputPhotoFileLocation) {
            byte[] file_reference = array3;
            if (photo.id == inputFileLocation.id) {
                file_reference = photo.file_reference;
            }
            return file_reference;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
            for (int size = photo.sizes.size(), i = 0; i < size; ++i) {
                final TLRPC.PhotoSize photoSize = photo.sizes.get(i);
                final byte[] fileReference = this.getFileReference(photoSize, inputFileLocation, array);
                if (array != null && array[0]) {
                    array2[0] = new TLRPC.TL_inputPhotoFileLocation();
                    array2[0].id = photo.id;
                    array2[0].volume_id = inputFileLocation.volume_id;
                    array2[0].local_id = inputFileLocation.local_id;
                    array2[0].access_hash = photo.access_hash;
                    inputFileLocation = array2[0];
                    final byte[] file_reference2 = photo.file_reference;
                    inputFileLocation.file_reference = file_reference2;
                    array2[0].thumb_size = photoSize.type;
                    return file_reference2;
                }
                if (fileReference != null) {
                    return fileReference;
                }
            }
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.PhotoSize photoSize, final TLRPC.InputFileLocation inputFileLocation, final boolean[] array) {
        if (photoSize != null && inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
            return this.getFileReference(photoSize.location, inputFileLocation, array);
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.User user, final TLRPC.InputFileLocation inputFileLocation, final boolean[] array, final TLRPC.InputFileLocation[] array2) {
        if (user != null) {
            final TLRPC.UserProfilePhoto photo = user.photo;
            if (photo != null) {
                if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
                    final byte[] fileReference = this.getFileReference(photo.photo_small, inputFileLocation, array);
                    if (this.getPeerReferenceReplacement(user, null, false, inputFileLocation, array2, array)) {
                        return new byte[0];
                    }
                    byte[] fileReference2;
                    if ((fileReference2 = fileReference) == null) {
                        fileReference2 = this.getFileReference(user.photo.photo_big, inputFileLocation, array);
                        if (this.getPeerReferenceReplacement(user, null, true, inputFileLocation, array2, array)) {
                            return new byte[0];
                        }
                    }
                    return fileReference2;
                }
            }
        }
        return null;
    }
    
    private byte[] getFileReference(final TLRPC.WebPage webPage, final TLRPC.InputFileLocation inputFileLocation, final boolean[] array, final TLRPC.InputFileLocation[] array2) {
        final byte[] fileReference = this.getFileReference(webPage.document, inputFileLocation, array, array2);
        if (fileReference != null) {
            return fileReference;
        }
        final byte[] fileReference2 = this.getFileReference(webPage.photo, inputFileLocation, array, array2);
        if (fileReference2 != null) {
            return fileReference2;
        }
        if (fileReference2 == null) {
            final TLRPC.Page cached_page = webPage.cached_page;
            if (cached_page != null) {
                final int size = cached_page.documents.size();
                final int n = 0;
                for (int i = 0; i < size; ++i) {
                    final byte[] fileReference3 = this.getFileReference(webPage.cached_page.documents.get(i), inputFileLocation, array, array2);
                    if (fileReference3 != null) {
                        return fileReference3;
                    }
                }
                for (int size2 = webPage.cached_page.photos.size(), j = n; j < size2; ++j) {
                    final byte[] fileReference4 = this.getFileReference(webPage.cached_page.photos.get(j), inputFileLocation, array, array2);
                    if (fileReference4 != null) {
                        return fileReference4;
                    }
                }
            }
        }
        return null;
    }
    
    public static FileRefController getInstance(final int n) {
        final FileRefController fileRefController;
        if ((fileRefController = FileRefController.Instance[n]) == null) {
            synchronized (FileRefController.class) {
                if (FileRefController.Instance[n] == null) {
                    FileRefController.Instance[n] = new FileRefController(n);
                }
            }
        }
        return fileRefController;
    }
    
    public static String getKeyForParentObject(final Object obj) {
        if (obj instanceof MessageObject) {
            final MessageObject messageObject = (MessageObject)obj;
            final int channelId = messageObject.getChannelId();
            final StringBuilder sb = new StringBuilder();
            sb.append("message");
            sb.append(messageObject.getRealId());
            sb.append("_");
            sb.append(channelId);
            return sb.toString();
        }
        if (obj instanceof TLRPC.Message) {
            final TLRPC.Message message = (TLRPC.Message)obj;
            final TLRPC.Peer to_id = message.to_id;
            int channel_id;
            if (to_id != null) {
                channel_id = to_id.channel_id;
            }
            else {
                channel_id = 0;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("message");
            sb2.append(message.id);
            sb2.append("_");
            sb2.append(channel_id);
            return sb2.toString();
        }
        if (obj instanceof TLRPC.WebPage) {
            final TLRPC.WebPage webPage = (TLRPC.WebPage)obj;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("webpage");
            sb3.append(webPage.id);
            return sb3.toString();
        }
        if (obj instanceof TLRPC.User) {
            final TLRPC.User user = (TLRPC.User)obj;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("user");
            sb4.append(user.id);
            return sb4.toString();
        }
        if (obj instanceof TLRPC.Chat) {
            final TLRPC.Chat chat = (TLRPC.Chat)obj;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("chat");
            sb5.append(chat.id);
            return sb5.toString();
        }
        if (obj instanceof String) {
            final String str = (String)obj;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("str");
            sb6.append(str);
            return sb6.toString();
        }
        if (obj instanceof TLRPC.TL_messages_stickerSet) {
            final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)obj;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("set");
            sb7.append(set.set.id);
            return sb7.toString();
        }
        if (obj instanceof TLRPC.StickerSetCovered) {
            final TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered)obj;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("set");
            sb8.append(stickerSetCovered.set.id);
            return sb8.toString();
        }
        if (obj instanceof TLRPC.InputStickerSet) {
            final TLRPC.InputStickerSet set2 = (TLRPC.InputStickerSet)obj;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("set");
            sb9.append(set2.id);
            return sb9.toString();
        }
        if (obj instanceof TLRPC.TL_wallPaper) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)obj;
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("wallpaper");
            sb10.append(tl_wallPaper.id);
            return sb10.toString();
        }
        String string;
        if (obj != null) {
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("");
            sb11.append(obj);
            string = sb11.toString();
        }
        else {
            string = null;
        }
        return string;
    }
    
    private boolean getPeerReferenceReplacement(final TLRPC.User user, final TLRPC.Chat chat, final boolean big, final TLRPC.InputFileLocation inputFileLocation, final TLRPC.InputFileLocation[] array, final boolean[] array2) {
        if (array2 != null && array2[0]) {
            array[0] = new TLRPC.TL_inputPeerPhotoFileLocation();
            final TLRPC.InputFileLocation inputFileLocation2 = array[0];
            final long volume_id = inputFileLocation.volume_id;
            inputFileLocation2.id = volume_id;
            array[0].volume_id = volume_id;
            array[0].local_id = inputFileLocation.local_id;
            array[0].big = big;
            TLRPC.InputPeer peer;
            if (user != null) {
                final TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
                tl_inputPeerUser.user_id = user.id;
                tl_inputPeerUser.access_hash = user.access_hash;
                peer = tl_inputPeerUser;
            }
            else if (ChatObject.isChannel(chat)) {
                peer = new TLRPC.TL_inputPeerChat();
                peer.chat_id = chat.id;
            }
            else {
                peer = new TLRPC.TL_inputPeerChannel();
                peer.channel_id = chat.id;
                peer.access_hash = chat.access_hash;
            }
            array[0].peer = peer;
            return true;
        }
        return false;
    }
    
    public static boolean isFileRefError(final String anObject) {
        return "FILEREF_EXPIRED".equals(anObject) || "FILE_REFERENCE_EXPIRED".equals(anObject) || "FILE_REFERENCE_EMPTY".equals(anObject) || (anObject != null && anObject.startsWith("FILE_REFERENCE_"));
    }
    
    private boolean onRequestComplete(final String s, final String s2, final TLObject tlObject, final boolean b) {
        int n = 0;
        Label_0142: {
            if (s2 != null) {
                final ArrayList<Requester> list = this.parentRequester.get(s2);
                if (list != null) {
                    final int size = list.size();
                    int i = 0;
                    n = 0;
                    while (i < size) {
                        final Requester requester = list.get(i);
                        if (!requester.completed) {
                            if (this.onRequestComplete(requester.locationKey, null, tlObject, b && n == 0)) {
                                n = 1;
                            }
                        }
                        ++i;
                    }
                    if (n != 0) {
                        this.putReponseToCache(s2, tlObject);
                    }
                    this.parentRequester.remove(s2);
                    break Label_0142;
                }
            }
            n = 0;
        }
        ArrayList<Requester> list2 = this.locationRequester.get(s);
        if (list2 == null) {
            return n != 0;
        }
        final int size2 = list2.size();
        boolean[] array = null;
        int[] array2;
        Object o = array2 = (int[])array;
        int j = 0;
        boolean b2 = n != 0;
        while (j < size2) {
            final Requester requester2 = list2.get(j);
            ArrayList<Requester> list3;
            if (requester2.completed) {
                list3 = list2;
            }
            else {
                if (requester2.location instanceof TLRPC.TL_inputFileLocation) {
                    o = new TLRPC.InputFileLocation[] { null };
                    array = new boolean[] { false };
                }
                requester2.completed = true;
                int[] array5 = null;
                Label_2063: {
                    ArrayList<Requester> list8 = null;
                    Label_0844: {
                        if (tlObject instanceof TLRPC.messages_Messages) {
                            final TLRPC.messages_Messages messages_Messages = (TLRPC.messages_Messages)tlObject;
                            if (!messages_Messages.messages.isEmpty()) {
                                final int size3 = messages_Messages.messages.size();
                                int index = 0;
                                int[] array3 = null;
                                ArrayList<Requester> list7 = null;
                                Label_0718: {
                                    while (true) {
                                        array3 = array2;
                                        if (index >= size3) {
                                            break;
                                        }
                                        final TLRPC.Message message = messages_Messages.messages.get(index);
                                        final TLRPC.MessageMedia media = message.media;
                                        if (media != null) {
                                            final TLRPC.Document document = media.document;
                                            if (document != null) {
                                                array2 = (int[])this.getFileReference(document, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                            }
                                            else {
                                                final TLRPC.TL_game game = media.game;
                                                if (game != null) {
                                                    if ((array2 = (int[])this.getFileReference(game.document, requester2.location, array, (TLRPC.InputFileLocation[])o)) == null) {
                                                        array2 = (int[])this.getFileReference(message.media.game.photo, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                                    }
                                                }
                                                else {
                                                    final TLRPC.Photo photo = media.photo;
                                                    if (photo != null) {
                                                        array2 = (int[])this.getFileReference(photo, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                                    }
                                                    else {
                                                        final TLRPC.WebPage webpage = media.webpage;
                                                        if (webpage != null) {
                                                            array2 = (int[])this.getFileReference(webpage, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            final TLRPC.MessageAction action = message.action;
                                            if (action instanceof TLRPC.TL_messageActionChatEditPhoto) {
                                                array2 = (int[])this.getFileReference(action.photo, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                            }
                                        }
                                        if (array2 != null) {
                                            array3 = array2;
                                            if (b) {
                                                final TLRPC.Peer to_id = message.to_id;
                                                ArrayList<Requester> list4 = list2;
                                                ArrayList<Requester> list6 = null;
                                                Label_0673: {
                                                    if (to_id != null) {
                                                        list4 = list2;
                                                        if (to_id.channel_id != 0) {
                                                            final int size4 = messages_Messages.chats.size();
                                                            int index2 = 0;
                                                            while (true) {
                                                                list4 = list2;
                                                                if (index2 >= size4) {
                                                                    break;
                                                                }
                                                                final TLRPC.Chat chat = messages_Messages.chats.get(index2);
                                                                final int id = chat.id;
                                                                final ArrayList<Requester> list5 = list2;
                                                                if (id == message.to_id.channel_id) {
                                                                    list6 = list5;
                                                                    if (chat.megagroup) {
                                                                        message.flags |= Integer.MIN_VALUE;
                                                                        list6 = list5;
                                                                    }
                                                                    break Label_0673;
                                                                }
                                                                else {
                                                                    ++index2;
                                                                    list2 = list5;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    list6 = list4;
                                                }
                                                MessagesStorage.getInstance(this.currentAccount).replaceMessageIfExists(message, this.currentAccount, messages_Messages.users, messages_Messages.chats, false);
                                                array3 = array2;
                                                list7 = list6;
                                                break Label_0718;
                                            }
                                            break;
                                        }
                                        else {
                                            ++index;
                                        }
                                    }
                                    list7 = list2;
                                }
                                final int[] array4 = array5 = array3;
                                list8 = list7;
                                if (array4 == null) {
                                    MessagesStorage.getInstance(this.currentAccount).replaceMessageIfExists(messages_Messages.messages.get(0), this.currentAccount, messages_Messages.users, messages_Messages.chats, true);
                                    array5 = array4;
                                    list8 = list7;
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("file ref not found in messages, replacing message");
                                        array5 = array4;
                                        list8 = list7;
                                    }
                                }
                            }
                            else {
                                list8 = list2;
                                array5 = array2;
                            }
                        }
                        else {
                            final ArrayList<Requester> list9 = list2;
                            if (tlObject instanceof TLRPC.WebPage) {
                                array5 = (int[])this.getFileReference((TLRPC.WebPage)tlObject, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                list8 = list9;
                            }
                            else if (tlObject instanceof TLRPC.TL_account_wallPapers) {
                                final TLRPC.TL_account_wallPapers tl_account_wallPapers = (TLRPC.TL_account_wallPapers)tlObject;
                                for (int size5 = tl_account_wallPapers.wallpapers.size(), k = 0; k < size5; ++k) {
                                    array2 = (int[])this.getFileReference(((TLRPC.TL_wallPaper)tl_account_wallPapers.wallpapers.get(k)).document, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                    if (array2 != null) {
                                        break;
                                    }
                                }
                                array5 = array2;
                                list8 = list9;
                                if (array2 != null) {
                                    array5 = array2;
                                    list8 = list9;
                                    if (b) {
                                        MessagesStorage.getInstance(this.currentAccount).putWallpapers(tl_account_wallPapers.wallpapers, 1);
                                        array5 = array2;
                                        list8 = list9;
                                    }
                                }
                            }
                            else if (tlObject instanceof TLRPC.TL_wallPaper) {
                                final TLRPC.TL_wallPaper e = (TLRPC.TL_wallPaper)tlObject;
                                final byte[] array6 = (byte[])(array5 = (int[])this.getFileReference(e.document, requester2.location, array, (TLRPC.InputFileLocation[])o));
                                list8 = list9;
                                if (array6 != null) {
                                    array5 = (int[])array6;
                                    list8 = list9;
                                    if (b) {
                                        final ArrayList<TLRPC.WallPaper> list10 = new ArrayList<TLRPC.WallPaper>();
                                        list10.add(e);
                                        MessagesStorage.getInstance(this.currentAccount).putWallpapers(list10, 0);
                                        array5 = (int[])array6;
                                        list8 = list9;
                                    }
                                }
                            }
                            else if (tlObject instanceof TLRPC.Vector) {
                                final TLRPC.Vector vector = (TLRPC.Vector)tlObject;
                                array5 = array2;
                                list8 = list9;
                                if (!vector.objects.isEmpty()) {
                                    final int size6 = vector.objects.size();
                                    int index3 = 0;
                                    while (true) {
                                        array5 = array2;
                                        list8 = list9;
                                        if (index3 >= size6) {
                                            break Label_0844;
                                        }
                                        final Object value = vector.objects.get(index3);
                                        if (value instanceof TLRPC.User) {
                                            final TLRPC.User e2 = (TLRPC.User)value;
                                            array5 = (int[])this.getFileReference(e2, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                            if (b && array5 != null) {
                                                final ArrayList<TLRPC.User> list11 = new ArrayList<TLRPC.User>();
                                                list11.add(e2);
                                                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(list11, null, true, true);
                                                AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$_CZgXfaxqSfurGMxYgHRkXa2trY(this, e2));
                                            }
                                        }
                                        else {
                                            array5 = array2;
                                            if (value instanceof TLRPC.Chat) {
                                                final TLRPC.Chat e3 = (TLRPC.Chat)value;
                                                array5 = (int[])this.getFileReference(e3, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                                if (b && array5 != null) {
                                                    final ArrayList<TLRPC.Chat> list12 = new ArrayList<TLRPC.Chat>();
                                                    list12.add(e3);
                                                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list12, true, true);
                                                    AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$ezrB_EEVIghp6y7yWEa40dOLdLU(this, e3));
                                                }
                                            }
                                        }
                                        if (array5 != null) {
                                            break;
                                        }
                                        ++index3;
                                        array2 = array5;
                                    }
                                    list8 = list9;
                                }
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_chats) {
                                final TLRPC.TL_messages_chats tl_messages_chats = (TLRPC.TL_messages_chats)tlObject;
                                array5 = array2;
                                list8 = list9;
                                if (!tl_messages_chats.chats.isEmpty()) {
                                    final int size7 = tl_messages_chats.chats.size();
                                    int index4 = 0;
                                    TLRPC.Chat e4;
                                    while (true) {
                                        array5 = array2;
                                        list8 = list9;
                                        if (index4 >= size7) {
                                            break Label_0844;
                                        }
                                        e4 = tl_messages_chats.chats.get(index4);
                                        array2 = (int[])this.getFileReference(e4, requester2.location, array, (TLRPC.InputFileLocation[])o);
                                        if (array2 != null) {
                                            break;
                                        }
                                        ++index4;
                                    }
                                    array5 = array2;
                                    list8 = list9;
                                    if (b) {
                                        final ArrayList<TLRPC.Chat> list13 = new ArrayList<TLRPC.Chat>();
                                        list13.add(e4);
                                        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list13, true, true);
                                        AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$wxZbkcK98NrwAinOuNo_DdhwDyk(this, e4));
                                        array5 = array2;
                                        list3 = list9;
                                        break Label_2063;
                                    }
                                }
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_savedGifs) {
                                final TLRPC.TL_messages_savedGifs tl_messages_savedGifs = (TLRPC.TL_messages_savedGifs)tlObject;
                                for (int size8 = tl_messages_savedGifs.gifs.size(), l = 0; l < size8; ++l) {
                                    array2 = (int[])this.getFileReference(tl_messages_savedGifs.gifs.get(l), requester2.location, array, (TLRPC.InputFileLocation[])o);
                                    if (array2 != null) {
                                        break;
                                    }
                                }
                                array5 = array2;
                                list3 = list9;
                                if (b) {
                                    DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(0, tl_messages_savedGifs.gifs, true, 0, true);
                                    array5 = array2;
                                    list3 = list9;
                                }
                                break Label_2063;
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_stickerSet) {
                                final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)tlObject;
                                int[] array7;
                                if ((array7 = array2) == null) {
                                    final int size9 = set.documents.size();
                                    int index5 = 0;
                                    while (true) {
                                        array7 = array2;
                                        if (index5 >= size9) {
                                            break;
                                        }
                                        array2 = (int[])this.getFileReference(set.documents.get(index5), requester2.location, array, (TLRPC.InputFileLocation[])o);
                                        if (array2 != null) {
                                            array7 = array2;
                                            break;
                                        }
                                        ++index5;
                                    }
                                }
                                array5 = array7;
                                list3 = list9;
                                if (b) {
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$FlgFGmJyAwG8D7Z8OYWnK63ajJo(this, set));
                                    array5 = array7;
                                    list3 = list9;
                                }
                                break Label_2063;
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_recentStickers) {
                                final TLRPC.TL_messages_recentStickers tl_messages_recentStickers = (TLRPC.TL_messages_recentStickers)tlObject;
                                for (int size10 = tl_messages_recentStickers.stickers.size(), index6 = 0; index6 < size10; ++index6) {
                                    array2 = (int[])this.getFileReference(tl_messages_recentStickers.stickers.get(index6), requester2.location, array, (TLRPC.InputFileLocation[])o);
                                    if (array2 != null) {
                                        break;
                                    }
                                }
                                array5 = array2;
                                list3 = list9;
                                if (b) {
                                    DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(0, tl_messages_recentStickers.stickers, false, 0, true);
                                    array5 = array2;
                                    list3 = list9;
                                }
                                break Label_2063;
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_favedStickers) {
                                final TLRPC.TL_messages_favedStickers tl_messages_favedStickers = (TLRPC.TL_messages_favedStickers)tlObject;
                                for (int size11 = tl_messages_favedStickers.stickers.size(), index7 = 0; index7 < size11; ++index7) {
                                    array2 = (int[])this.getFileReference(tl_messages_favedStickers.stickers.get(index7), requester2.location, array, (TLRPC.InputFileLocation[])o);
                                    if (array2 != null) {
                                        break;
                                    }
                                }
                                array5 = array2;
                                list3 = list9;
                                if (b) {
                                    DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(2, tl_messages_favedStickers.stickers, false, 0, true);
                                    array5 = array2;
                                    list3 = list9;
                                }
                                break Label_2063;
                            }
                            else {
                                array5 = array2;
                                list3 = list9;
                                if (!(tlObject instanceof TLRPC.photos_Photos)) {
                                    break Label_2063;
                                }
                                final TLRPC.photos_Photos photos_Photos = (TLRPC.photos_Photos)tlObject;
                                final int size12 = photos_Photos.photos.size();
                                int index8 = 0;
                                while (true) {
                                    array5 = array2;
                                    list3 = list9;
                                    if (index8 >= size12) {
                                        break Label_2063;
                                    }
                                    array2 = (int[])this.getFileReference(photos_Photos.photos.get(index8), requester2.location, array, (TLRPC.InputFileLocation[])o);
                                    if (array2 != null) {
                                        array5 = array2;
                                        list3 = list9;
                                        break Label_2063;
                                    }
                                    ++index8;
                                }
                            }
                        }
                    }
                    list3 = list8;
                }
                if (array5 != null) {
                    TLRPC.InputFileLocation inputFileLocation;
                    if (o != null) {
                        inputFileLocation = o[0];
                    }
                    else {
                        inputFileLocation = null;
                    }
                    this.onUpdateObjectReference(requester2, (byte[])array5, inputFileLocation);
                    b2 = true;
                    array2 = array5;
                }
                else {
                    this.sendErrorToObject(requester2.args, 1);
                    array2 = array5;
                }
            }
            ++j;
            list2 = list3;
        }
        this.locationRequester.remove(s);
        if (b2) {
            this.putReponseToCache(s, tlObject);
        }
        return b2;
    }
    
    private void onUpdateObjectReference(final Requester requester, final byte[] array, final TLRPC.InputFileLocation location) {
        if (BuildVars.DEBUG_VERSION) {
            final StringBuilder sb = new StringBuilder();
            sb.append("fileref updated for ");
            sb.append(requester.args[0]);
            sb.append(" ");
            sb.append(requester.locationKey);
            FileLog.d(sb.toString());
        }
        if (requester.args[0] instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tl_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia)requester.args[1];
            final Object[] array2 = this.multiMediaCache.get(tl_messages_sendMultiMedia);
            if (array2 == null) {
                return;
            }
            final TLRPC.TL_inputSingleMedia o = (TLRPC.TL_inputSingleMedia)requester.args[0];
            final TLRPC.InputMedia media = o.media;
            if (media instanceof TLRPC.TL_inputMediaDocument) {
                ((TLRPC.TL_inputMediaDocument)media).id.file_reference = array;
            }
            else if (media instanceof TLRPC.TL_inputMediaPhoto) {
                ((TLRPC.TL_inputMediaPhoto)media).id.file_reference = array;
            }
            final int index = tl_messages_sendMultiMedia.multi_media.indexOf(o);
            if (index < 0) {
                return;
            }
            final ArrayList list = (ArrayList)array2[3];
            list.set(index, null);
            int i = 0;
            boolean b = true;
            while (i < list.size()) {
                if (list.get(i) != null) {
                    b = false;
                }
                ++i;
            }
            if (b) {
                this.multiMediaCache.remove(tl_messages_sendMultiMedia);
                SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(tl_messages_sendMultiMedia, (ArrayList<MessageObject>)array2[1], (ArrayList<String>)array2[2], null, (SendMessagesHelper.DelayedMessage)array2[4]);
            }
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_sendMedia) {
            final TLRPC.InputMedia media2 = ((TLRPC.TL_messages_sendMedia)requester.args[0]).media;
            if (media2 instanceof TLRPC.TL_inputMediaDocument) {
                ((TLRPC.TL_inputMediaDocument)media2).id.file_reference = array;
            }
            else if (media2 instanceof TLRPC.TL_inputMediaPhoto) {
                ((TLRPC.TL_inputMediaPhoto)media2).id.file_reference = array;
            }
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)requester.args[0], (MessageObject)requester.args[1], (String)requester.args[2], (SendMessagesHelper.DelayedMessage)requester.args[3], (boolean)requester.args[4], (SendMessagesHelper.DelayedMessage)requester.args[5], null);
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_editMessage) {
            final TLRPC.InputMedia media3 = ((TLRPC.TL_messages_editMessage)requester.args[0]).media;
            if (media3 instanceof TLRPC.TL_inputMediaDocument) {
                ((TLRPC.TL_inputMediaDocument)media3).id.file_reference = array;
            }
            else if (media3 instanceof TLRPC.TL_inputMediaPhoto) {
                ((TLRPC.TL_inputMediaPhoto)media3).id.file_reference = array;
            }
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)requester.args[0], (MessageObject)requester.args[1], (String)requester.args[2], (SendMessagesHelper.DelayedMessage)requester.args[3], (boolean)requester.args[4], (SendMessagesHelper.DelayedMessage)requester.args[5], null);
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_saveGif) {
            final TLRPC.TL_messages_saveGif tl_messages_saveGif = (TLRPC.TL_messages_saveGif)requester.args[0];
            tl_messages_saveGif.id.file_reference = array;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveGif, (RequestDelegate)_$$Lambda$FileRefController$glsZ_ebv4_mT6CRmECvMkMDX4tM.INSTANCE);
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            final TLRPC.TL_messages_saveRecentSticker tl_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker)requester.args[0];
            tl_messages_saveRecentSticker.id.file_reference = array;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveRecentSticker, (RequestDelegate)_$$Lambda$FileRefController$7dnf8o_vZU8kWj_oHiGfTHxk_5E.INSTANCE);
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_faveSticker) {
            final TLRPC.TL_messages_faveSticker tl_messages_faveSticker = (TLRPC.TL_messages_faveSticker)requester.args[0];
            tl_messages_faveSticker.id.file_reference = array;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_faveSticker, (RequestDelegate)_$$Lambda$FileRefController$2YbOQ_Rvo_LvdJ__ALCga2DKRrU.INSTANCE);
        }
        else if (requester.args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            final TLRPC.TL_messages_getAttachedStickers tl_messages_getAttachedStickers = (TLRPC.TL_messages_getAttachedStickers)requester.args[0];
            final TLRPC.InputStickeredMedia media4 = tl_messages_getAttachedStickers.media;
            if (media4 instanceof TLRPC.TL_inputStickeredMediaDocument) {
                ((TLRPC.TL_inputStickeredMediaDocument)media4).id.file_reference = array;
            }
            else if (media4 instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                ((TLRPC.TL_inputStickeredMediaPhoto)media4).id.file_reference = array;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getAttachedStickers, (RequestDelegate)requester.args[1]);
        }
        else if (requester.args[1] instanceof FileLoadOperation) {
            final FileLoadOperation fileLoadOperation = (FileLoadOperation)requester.args[1];
            if (location != null) {
                fileLoadOperation.location = location;
            }
            else {
                requester.location.file_reference = array;
            }
            fileLoadOperation.requestingReference = false;
            fileLoadOperation.startDownloadRequest();
        }
    }
    
    private void putReponseToCache(final String s, final TLObject tlObject) {
        CachedResult value;
        if ((value = this.responseCache.get(s)) == null) {
            value = new CachedResult();
            value.response = tlObject;
            value.firstQueryTime = SystemClock.uptimeMillis();
            this.responseCache.put(s, value);
        }
        value.lastQueryTime = SystemClock.uptimeMillis();
    }
    
    private void requestReferenceFromServer(final Object o, final String s, final String s2, final Object[] array) {
        if (o instanceof MessageObject) {
            final MessageObject messageObject = (MessageObject)o;
            final int channelId = messageObject.getChannelId();
            if (channelId != 0) {
                final TLRPC.TL_channels_getMessages tl_channels_getMessages = new TLRPC.TL_channels_getMessages();
                tl_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(channelId);
                tl_channels_getMessages.id.add(messageObject.getRealId());
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getMessages, new _$$Lambda$FileRefController$bJSiLyN_Loo2lNffdEwzSUZ6du0(this, s, s2));
            }
            else {
                final TLRPC.TL_messages_getMessages tl_messages_getMessages = new TLRPC.TL_messages_getMessages();
                tl_messages_getMessages.id.add(messageObject.getRealId());
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getMessages, new _$$Lambda$FileRefController$iH_RpP96708b_YCozHWKOTcxbr4(this, s, s2));
            }
        }
        else if (o instanceof TLRPC.TL_wallPaper) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)o;
            final TLRPC.TL_account_getWallPaper tl_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
            final TLRPC.TL_inputWallPaper wallpaper = new TLRPC.TL_inputWallPaper();
            wallpaper.id = tl_wallPaper.id;
            wallpaper.access_hash = tl_wallPaper.access_hash;
            tl_account_getWallPaper.wallpaper = wallpaper;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_getWallPaper, new _$$Lambda$FileRefController$l7gUgVrfRVPQOPlPV4_4bUexFYw(this, s, s2));
        }
        else if (o instanceof TLRPC.WebPage) {
            final TLRPC.WebPage webPage = (TLRPC.WebPage)o;
            final TLRPC.TL_messages_getWebPage tl_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
            tl_messages_getWebPage.url = webPage.url;
            tl_messages_getWebPage.hash = 0;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getWebPage, new _$$Lambda$FileRefController$ZZalHmw878mE45n2TIDtpdup6rk(this, s, s2));
        }
        else if (o instanceof TLRPC.User) {
            final TLRPC.User user = (TLRPC.User)o;
            final TLRPC.TL_users_getUsers tl_users_getUsers = new TLRPC.TL_users_getUsers();
            tl_users_getUsers.id.add(MessagesController.getInstance(this.currentAccount).getInputUser(user));
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_users_getUsers, new _$$Lambda$FileRefController$PPQViQbtvXgiD0HkQf0oK3_ZBkE(this, s, s2));
        }
        else if (o instanceof TLRPC.Chat) {
            final TLRPC.Chat chat = (TLRPC.Chat)o;
            if (chat instanceof TLRPC.TL_chat) {
                final TLRPC.TL_messages_getChats tl_messages_getChats = new TLRPC.TL_messages_getChats();
                tl_messages_getChats.id.add(chat.id);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getChats, new _$$Lambda$FileRefController$7qx1abCFn6GfdxglKDbFuGfloVQ(this, s, s2));
            }
            else if (chat instanceof TLRPC.TL_channel) {
                final TLRPC.TL_channels_getChannels tl_channels_getChannels = new TLRPC.TL_channels_getChannels();
                tl_channels_getChannels.id.add(MessagesController.getInputChannel(chat));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getChannels, new _$$Lambda$FileRefController$MN7WJPmFdFnXGwTYeLjLskDZA_s(this, s, s2));
            }
        }
        else if (o instanceof String) {
            final String anObject = (String)o;
            if ("wallpaper".equals(anObject)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getWallPapers(), new _$$Lambda$FileRefController$hU__PNtgoV5UVJ0PgXNS0w6t_Ck(this, s, s2));
            }
            else if (anObject.startsWith("gif")) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getSavedGifs(), new _$$Lambda$FileRefController$XmvlggjDShdh6wwrH2NBXtZN860(this, s, s2));
            }
            else if ("recent".equals(anObject)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getRecentStickers(), new _$$Lambda$FileRefController$1aFkU4DGV4no_EoB8k2dbx7DLBY(this, s, s2));
            }
            else if ("fav".equals(anObject)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getFavedStickers(), new _$$Lambda$FileRefController$4w92eKYUWTkjdDZrk7Ab6kZgCz4(this, s, s2));
            }
            else if (anObject.startsWith("avatar_")) {
                final int intValue = Utilities.parseInt(anObject);
                if (intValue > 0) {
                    final TLRPC.TL_photos_getUserPhotos tl_photos_getUserPhotos = new TLRPC.TL_photos_getUserPhotos();
                    tl_photos_getUserPhotos.limit = 80;
                    tl_photos_getUserPhotos.offset = 0;
                    tl_photos_getUserPhotos.max_id = 0L;
                    tl_photos_getUserPhotos.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(intValue);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_photos_getUserPhotos, new _$$Lambda$FileRefController$lc6KWNmCjlTmUjUWy2DU6yide1g(this, s, s2));
                }
                else {
                    final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
                    tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
                    tl_messages_search.limit = 80;
                    tl_messages_search.offset_id = 0;
                    tl_messages_search.q = "";
                    tl_messages_search.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(intValue);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$FileRefController$WYAn7hVey6Q_NHtwvjaWCOxf8sA(this, s, s2));
                }
            }
            else if (anObject.startsWith("sent_")) {
                final String[] split = anObject.split("_");
                if (split.length == 3) {
                    final int intValue2 = Utilities.parseInt(split[1]);
                    if (intValue2 != 0) {
                        final TLRPC.TL_channels_getMessages tl_channels_getMessages2 = new TLRPC.TL_channels_getMessages();
                        tl_channels_getMessages2.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(intValue2);
                        tl_channels_getMessages2.id.add(Utilities.parseInt(split[2]));
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getMessages2, new _$$Lambda$FileRefController$AUpNheLWJQs7kyRgJ6tIGIQTBNY(this, s, s2));
                    }
                    else {
                        final TLRPC.TL_messages_getMessages tl_messages_getMessages2 = new TLRPC.TL_messages_getMessages();
                        tl_messages_getMessages2.id.add(Utilities.parseInt(split[2]));
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getMessages2, new _$$Lambda$FileRefController$oHc4Ko0S36uJ174mUmftFNV5oEU(this, s, s2));
                    }
                }
                else {
                    this.sendErrorToObject(array, 0);
                }
            }
            else {
                this.sendErrorToObject(array, 0);
            }
        }
        else if (o instanceof TLRPC.TL_messages_stickerSet) {
            final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)o;
            final TLRPC.TL_messages_getStickerSet set2 = new TLRPC.TL_messages_getStickerSet();
            set2.stickerset = new TLRPC.TL_inputStickerSetID();
            final TLRPC.InputStickerSet stickerset = set2.stickerset;
            final TLRPC.StickerSet set3 = set.set;
            stickerset.id = set3.id;
            stickerset.access_hash = set3.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set2, new _$$Lambda$FileRefController$exbLp78Kychyc6GzkLXULN8LNGg(this, s, s2));
        }
        else if (o instanceof TLRPC.StickerSetCovered) {
            final TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered)o;
            final TLRPC.TL_messages_getStickerSet set4 = new TLRPC.TL_messages_getStickerSet();
            set4.stickerset = new TLRPC.TL_inputStickerSetID();
            final TLRPC.InputStickerSet stickerset2 = set4.stickerset;
            final TLRPC.StickerSet set5 = stickerSetCovered.set;
            stickerset2.id = set5.id;
            stickerset2.access_hash = set5.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set4, new _$$Lambda$FileRefController$oQwONz9oH9KbjBHI1GVgQcuvYoU(this, s, s2));
        }
        else if (o instanceof TLRPC.InputStickerSet) {
            final TLRPC.TL_messages_getStickerSet set6 = new TLRPC.TL_messages_getStickerSet();
            set6.stickerset = (TLRPC.InputStickerSet)o;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(set6, new _$$Lambda$FileRefController$taVyElfntghqykv41aff7_1_UM8(this, s, s2));
        }
        else {
            this.sendErrorToObject(array, 0);
        }
    }
    
    private void sendErrorToObject(final Object[] array, final int n) {
        if (array[0] instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tl_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia)array[1];
            final Object[] array2 = this.multiMediaCache.get(tl_messages_sendMultiMedia);
            if (array2 != null) {
                this.multiMediaCache.remove(tl_messages_sendMultiMedia);
                SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(tl_messages_sendMultiMedia, (ArrayList<MessageObject>)array2[1], (ArrayList<String>)array2[2], null, (SendMessagesHelper.DelayedMessage)array2[4]);
            }
        }
        else if (!(array[0] instanceof TLRPC.TL_messages_sendMedia) && !(array[0] instanceof TLRPC.TL_messages_editMessage)) {
            if (array[0] instanceof TLRPC.TL_messages_saveGif) {
                final TLRPC.TL_messages_saveGif tl_messages_saveGif = (TLRPC.TL_messages_saveGif)array[0];
            }
            else if (array[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
                final TLRPC.TL_messages_saveRecentSticker tl_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker)array[0];
            }
            else if (array[0] instanceof TLRPC.TL_messages_faveSticker) {
                final TLRPC.TL_messages_faveSticker tl_messages_faveSticker = (TLRPC.TL_messages_faveSticker)array[0];
            }
            else if (array[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)array[0], (RequestDelegate)array[1]);
            }
            else if (n == 0) {
                final TLRPC.TL_error tl_error = new TLRPC.TL_error();
                tl_error.text = "not found parent object to request reference";
                tl_error.code = 400;
                if (array[1] instanceof FileLoadOperation) {
                    final FileLoadOperation fileLoadOperation = (FileLoadOperation)array[1];
                    fileLoadOperation.requestingReference = false;
                    fileLoadOperation.processRequestResult((FileLoadOperation.RequestInfo)array[2], tl_error);
                }
            }
            else if (n == 1 && array[1] instanceof FileLoadOperation) {
                final FileLoadOperation fileLoadOperation2 = (FileLoadOperation)array[1];
                fileLoadOperation2.onFail(fileLoadOperation2.requestingReference = false, 0);
            }
        }
        else {
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)array[0], (MessageObject)array[1], (String)array[2], (SendMessagesHelper.DelayedMessage)array[3], (boolean)array[4], (SendMessagesHelper.DelayedMessage)array[5], null);
        }
    }
    
    public void requestReference(Object value, final Object... value2) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("start loading request reference for parent = ");
            sb.append(value);
            sb.append(" args = ");
            sb.append(value2[0]);
            FileLog.d(sb.toString());
        }
        final boolean b = value2[0] instanceof TLRPC.TL_inputSingleMedia;
        int n = 1;
        String key;
        TLRPC.InputFileLocation inputFileLocation;
        if (b) {
            final TLRPC.InputMedia media = ((TLRPC.TL_inputSingleMedia)value2[0]).media;
            if (media instanceof TLRPC.TL_inputMediaDocument) {
                final TLRPC.TL_inputMediaDocument tl_inputMediaDocument = (TLRPC.TL_inputMediaDocument)media;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("file_");
                sb2.append(tl_inputMediaDocument.id.id);
                key = sb2.toString();
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                inputFileLocation.id = tl_inputMediaDocument.id.id;
            }
            else {
                if (!(media instanceof TLRPC.TL_inputMediaPhoto)) {
                    this.sendErrorToObject(value2, 0);
                    return;
                }
                final TLRPC.TL_inputMediaPhoto tl_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto)media;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("photo_");
                sb3.append(tl_inputMediaPhoto.id.id);
                key = sb3.toString();
                inputFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                inputFileLocation.id = tl_inputMediaPhoto.id.id;
            }
        }
        else {
            if (value2[0] instanceof TLRPC.TL_messages_sendMultiMedia) {
                final TLRPC.TL_messages_sendMultiMedia key2 = (TLRPC.TL_messages_sendMultiMedia)value2[0];
                final ArrayList list = (ArrayList)value;
                this.multiMediaCache.put(key2, value2);
                for (int size = key2.multi_media.size(), i = 0; i < size; ++i) {
                    final TLRPC.TL_inputSingleMedia tl_inputSingleMedia = key2.multi_media.get(i);
                    final Object value3 = list.get(i);
                    if (value3 != null) {
                        this.requestReference(value3, tl_inputSingleMedia, key2);
                    }
                }
                return;
            }
            if (value2[0] instanceof TLRPC.TL_messages_sendMedia) {
                final TLRPC.InputMedia media2 = ((TLRPC.TL_messages_sendMedia)value2[0]).media;
                if (media2 instanceof TLRPC.TL_inputMediaDocument) {
                    final TLRPC.TL_inputMediaDocument tl_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument)media2;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("file_");
                    sb4.append(tl_inputMediaDocument2.id.id);
                    key = sb4.toString();
                    inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                    inputFileLocation.id = tl_inputMediaDocument2.id.id;
                }
                else {
                    if (!(media2 instanceof TLRPC.TL_inputMediaPhoto)) {
                        this.sendErrorToObject(value2, 0);
                        return;
                    }
                    final TLRPC.TL_inputMediaPhoto tl_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto)media2;
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("photo_");
                    sb5.append(tl_inputMediaPhoto2.id.id);
                    key = sb5.toString();
                    inputFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    inputFileLocation.id = tl_inputMediaPhoto2.id.id;
                }
            }
            else if (value2[0] instanceof TLRPC.TL_messages_editMessage) {
                final TLRPC.InputMedia media3 = ((TLRPC.TL_messages_editMessage)value2[0]).media;
                if (media3 instanceof TLRPC.TL_inputMediaDocument) {
                    final TLRPC.TL_inputMediaDocument tl_inputMediaDocument3 = (TLRPC.TL_inputMediaDocument)media3;
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("file_");
                    sb6.append(tl_inputMediaDocument3.id.id);
                    key = sb6.toString();
                    inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                    inputFileLocation.id = tl_inputMediaDocument3.id.id;
                }
                else {
                    if (!(media3 instanceof TLRPC.TL_inputMediaPhoto)) {
                        this.sendErrorToObject(value2, 0);
                        return;
                    }
                    final TLRPC.TL_inputMediaPhoto tl_inputMediaPhoto3 = (TLRPC.TL_inputMediaPhoto)media3;
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("photo_");
                    sb7.append(tl_inputMediaPhoto3.id.id);
                    key = sb7.toString();
                    inputFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    inputFileLocation.id = tl_inputMediaPhoto3.id.id;
                }
            }
            else if (value2[0] instanceof TLRPC.TL_messages_saveGif) {
                final TLRPC.TL_messages_saveGif tl_messages_saveGif = (TLRPC.TL_messages_saveGif)value2[0];
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("file_");
                sb8.append(tl_messages_saveGif.id.id);
                key = sb8.toString();
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                inputFileLocation.id = tl_messages_saveGif.id.id;
            }
            else if (value2[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
                final TLRPC.TL_messages_saveRecentSticker tl_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker)value2[0];
                final StringBuilder sb9 = new StringBuilder();
                sb9.append("file_");
                sb9.append(tl_messages_saveRecentSticker.id.id);
                key = sb9.toString();
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                inputFileLocation.id = tl_messages_saveRecentSticker.id.id;
            }
            else if (value2[0] instanceof TLRPC.TL_messages_faveSticker) {
                final TLRPC.TL_messages_faveSticker tl_messages_faveSticker = (TLRPC.TL_messages_faveSticker)value2[0];
                final StringBuilder sb10 = new StringBuilder();
                sb10.append("file_");
                sb10.append(tl_messages_faveSticker.id.id);
                key = sb10.toString();
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                inputFileLocation.id = tl_messages_faveSticker.id.id;
            }
            else if (value2[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
                final TLRPC.InputStickeredMedia media4 = ((TLRPC.TL_messages_getAttachedStickers)value2[0]).media;
                if (media4 instanceof TLRPC.TL_inputStickeredMediaDocument) {
                    final TLRPC.TL_inputStickeredMediaDocument tl_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument)media4;
                    final StringBuilder sb11 = new StringBuilder();
                    sb11.append("file_");
                    sb11.append(tl_inputStickeredMediaDocument.id.id);
                    key = sb11.toString();
                    inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                    inputFileLocation.id = tl_inputStickeredMediaDocument.id.id;
                }
                else {
                    if (!(media4 instanceof TLRPC.TL_inputStickeredMediaPhoto)) {
                        this.sendErrorToObject(value2, 0);
                        return;
                    }
                    final TLRPC.TL_inputStickeredMediaPhoto tl_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto)media4;
                    final StringBuilder sb12 = new StringBuilder();
                    sb12.append("photo_");
                    sb12.append(tl_inputStickeredMediaPhoto.id.id);
                    key = sb12.toString();
                    inputFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    inputFileLocation.id = tl_inputStickeredMediaPhoto.id.id;
                }
            }
            else if (value2[0] instanceof TLRPC.TL_inputFileLocation) {
                inputFileLocation = (TLRPC.TL_inputFileLocation)value2[0];
                final StringBuilder sb13 = new StringBuilder();
                sb13.append("loc_");
                sb13.append(inputFileLocation.local_id);
                sb13.append("_");
                sb13.append(inputFileLocation.volume_id);
                key = sb13.toString();
            }
            else if (value2[0] instanceof TLRPC.TL_inputDocumentFileLocation) {
                inputFileLocation = (TLRPC.TL_inputDocumentFileLocation)value2[0];
                final StringBuilder sb14 = new StringBuilder();
                sb14.append("file_");
                sb14.append(inputFileLocation.id);
                key = sb14.toString();
            }
            else {
                if (!(value2[0] instanceof TLRPC.TL_inputPhotoFileLocation)) {
                    this.sendErrorToObject(value2, 0);
                    return;
                }
                inputFileLocation = (TLRPC.TL_inputPhotoFileLocation)value2[0];
                final StringBuilder sb15 = new StringBuilder();
                sb15.append("photo_");
                sb15.append(inputFileLocation.id);
                key = sb15.toString();
            }
        }
        Object o = value;
        if (value instanceof MessageObject) {
            final MessageObject messageObject = (MessageObject)value;
            o = value;
            if (messageObject.getRealId() < 0) {
                final TLRPC.WebPage webpage = messageObject.messageOwner.media.webpage;
                o = value;
                if (webpage != null) {
                    o = webpage;
                }
            }
        }
        final String keyForParentObject = getKeyForParentObject(o);
        if (keyForParentObject == null) {
            this.sendErrorToObject(value2, 0);
            return;
        }
        final Requester requester = new Requester();
        requester.args = value2;
        requester.location = inputFileLocation;
        requester.locationKey = key;
        value = this.locationRequester.get(key);
        if (value == null) {
            value = new ArrayList();
            this.locationRequester.put(key, (ArrayList<Requester>)value);
        }
        else {
            n = 0;
        }
        ((ArrayList<Requester>)value).add(requester);
        final ArrayList<Requester> list2 = this.parentRequester.get(keyForParentObject);
        int n2 = n;
        value = list2;
        if (list2 == null) {
            value = new ArrayList();
            this.parentRequester.put(keyForParentObject, (ArrayList<Requester>)value);
            n2 = n + 1;
        }
        ((ArrayList<Requester>)value).add(requester);
        if (n2 != 2) {
            return;
        }
        this.cleanupCache();
        final CachedResult cachedResponse = this.getCachedResponse(key);
        if (cachedResponse != null) {
            if (this.onRequestComplete(key, keyForParentObject, cachedResponse.response, false)) {
                return;
            }
            this.responseCache.remove(key);
        }
        else {
            final CachedResult cachedResponse2 = this.getCachedResponse(keyForParentObject);
            if (cachedResponse2 != null) {
                if (this.onRequestComplete(key, keyForParentObject, cachedResponse2.response, false)) {
                    return;
                }
                this.responseCache.remove(keyForParentObject);
            }
        }
        this.requestReferenceFromServer(o, key, keyForParentObject, value2);
    }
    
    private class CachedResult
    {
        private long firstQueryTime;
        private long lastQueryTime;
        private TLObject response;
    }
    
    private class Requester
    {
        private Object[] args;
        private boolean completed;
        private TLRPC.InputFileLocation location;
        private String locationKey;
    }
}
