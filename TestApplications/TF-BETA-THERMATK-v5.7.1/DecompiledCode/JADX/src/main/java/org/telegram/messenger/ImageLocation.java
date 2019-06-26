package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatPhoto;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerChat;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC.TL_secureFile;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.UserProfilePhoto;

public class ImageLocation {
    public long access_hash;
    public int currentSize;
    public int dc_id;
    public Document document;
    public long documentId;
    public byte[] file_reference;
    /* renamed from: iv */
    public byte[] f53iv;
    public byte[] key;
    public TL_fileLocationToBeDeprecated location;
    public String path;
    public Photo photo;
    public long photoId;
    public InputPeer photoPeer;
    public boolean photoPeerBig;
    public PhotoSize photoSize;
    public SecureDocument secureDocument;
    public InputStickerSet stickerSet;
    public String thumbSize;
    public WebFile webFile;

    public static ImageLocation getForPath(String str) {
        if (str == null) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.path = str;
        return imageLocation;
    }

    public static ImageLocation getForSecureDocument(SecureDocument secureDocument) {
        if (secureDocument == null) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.secureDocument = secureDocument;
        return imageLocation;
    }

    public static ImageLocation getForDocument(Document document) {
        if (document == null) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.document = document;
        imageLocation.key = document.key;
        imageLocation.f53iv = document.f442iv;
        imageLocation.currentSize = document.size;
        return imageLocation;
    }

    public static ImageLocation getForWebFile(WebFile webFile) {
        if (webFile == null) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.webFile = webFile;
        imageLocation.currentSize = webFile.size;
        return imageLocation;
    }

    public static ImageLocation getForObject(PhotoSize photoSize, TLObject tLObject) {
        if (tLObject instanceof Photo) {
            return getForPhoto(photoSize, (Photo) tLObject);
        }
        return tLObject instanceof Document ? getForDocument(photoSize, (Document) tLObject) : null;
    }

    public static ImageLocation getForPhoto(PhotoSize photoSize, Photo photo) {
        if (photoSize instanceof TL_photoStrippedSize) {
            ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        } else if (photoSize == null || photo == null) {
            return null;
        } else {
            int i = photo.dc_id;
            if (i == 0) {
                i = photoSize.location.dc_id;
            }
            return getForPhoto(photoSize.location, photoSize.size, photo, null, null, false, i, null, photoSize.type);
        }
    }

    public static ImageLocation getForUser(User user, boolean z) {
        if (!(user == null || user.access_hash == 0)) {
            UserProfilePhoto userProfilePhoto = user.photo;
            if (userProfilePhoto != null) {
                FileLocation fileLocation = z ? userProfilePhoto.photo_big : userProfilePhoto.photo_small;
                if (fileLocation == null) {
                    return null;
                }
                TL_inputPeerUser tL_inputPeerUser = new TL_inputPeerUser();
                tL_inputPeerUser.user_id = user.f534id;
                tL_inputPeerUser.access_hash = user.access_hash;
                int i = user.photo.dc_id;
                if (i == 0) {
                    i = fileLocation.dc_id;
                }
                return getForPhoto(fileLocation, 0, null, null, tL_inputPeerUser, z, i, null, null);
            }
        }
        return null;
    }

    public static ImageLocation getForChat(Chat chat, boolean z) {
        if (chat != null) {
            ChatPhoto chatPhoto = chat.photo;
            if (chatPhoto != null) {
                FileLocation fileLocation = z ? chatPhoto.photo_big : chatPhoto.photo_small;
                if (fileLocation == null) {
                    return null;
                }
                InputPeer tL_inputPeerChat;
                if (!ChatObject.isChannel(chat)) {
                    tL_inputPeerChat = new TL_inputPeerChat();
                    tL_inputPeerChat.chat_id = chat.f434id;
                } else if (chat.access_hash == 0) {
                    return null;
                } else {
                    tL_inputPeerChat = new TL_inputPeerChannel();
                    tL_inputPeerChat.channel_id = chat.f434id;
                    tL_inputPeerChat.access_hash = chat.access_hash;
                }
                InputPeer inputPeer = tL_inputPeerChat;
                int i = chat.photo.dc_id;
                if (i == 0) {
                    i = fileLocation.dc_id;
                }
                return getForPhoto(fileLocation, 0, null, null, inputPeer, z, i, null, null);
            }
        }
        return null;
    }

    public static ImageLocation getForSticker(PhotoSize photoSize, Document document) {
        if (photoSize instanceof TL_photoStrippedSize) {
            ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        } else if (photoSize == null || document == null) {
            return null;
        } else {
            InputStickerSet inputStickerSet = DataQuery.getInputStickerSet(document);
            if (inputStickerSet == null) {
                return null;
            }
            return getForPhoto(photoSize.location, photoSize.size, null, null, null, false, document.dc_id, inputStickerSet, photoSize.type);
        }
    }

    public static ImageLocation getForDocument(PhotoSize photoSize, Document document) {
        if (photoSize instanceof TL_photoStrippedSize) {
            ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        } else if (photoSize == null || document == null) {
            return null;
        } else {
            return getForPhoto(photoSize.location, photoSize.size, null, document, null, false, document.dc_id, null, photoSize.type);
        }
    }

    public static ImageLocation getForLocal(FileLocation fileLocation) {
        if (fileLocation == null) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.location = new TL_fileLocationToBeDeprecated();
        TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = imageLocation.location;
        tL_fileLocationToBeDeprecated.local_id = fileLocation.local_id;
        tL_fileLocationToBeDeprecated.volume_id = fileLocation.volume_id;
        tL_fileLocationToBeDeprecated.secret = fileLocation.secret;
        tL_fileLocationToBeDeprecated.dc_id = fileLocation.dc_id;
        return imageLocation;
    }

    private static ImageLocation getForPhoto(FileLocation fileLocation, int i, Photo photo, Document document, InputPeer inputPeer, boolean z, int i2, InputStickerSet inputStickerSet, String str) {
        if (fileLocation == null || (photo == null && inputPeer == null && inputStickerSet == null && document == null)) {
            return null;
        }
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.dc_id = i2;
        imageLocation.photo = photo;
        imageLocation.currentSize = i;
        imageLocation.photoPeer = inputPeer;
        imageLocation.photoPeerBig = z;
        imageLocation.stickerSet = inputStickerSet;
        if (fileLocation instanceof TL_fileLocationToBeDeprecated) {
            imageLocation.location = (TL_fileLocationToBeDeprecated) fileLocation;
            if (photo != null) {
                imageLocation.file_reference = photo.file_reference;
                imageLocation.access_hash = photo.access_hash;
                imageLocation.photoId = photo.f463id;
                imageLocation.thumbSize = str;
            } else if (document != null) {
                imageLocation.file_reference = document.file_reference;
                imageLocation.access_hash = document.access_hash;
                imageLocation.documentId = document.f441id;
                imageLocation.thumbSize = str;
            }
        } else {
            imageLocation.location = new TL_fileLocationToBeDeprecated();
            TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = imageLocation.location;
            tL_fileLocationToBeDeprecated.local_id = fileLocation.local_id;
            tL_fileLocationToBeDeprecated.volume_id = fileLocation.volume_id;
            tL_fileLocationToBeDeprecated.secret = fileLocation.secret;
            imageLocation.dc_id = fileLocation.dc_id;
            imageLocation.file_reference = fileLocation.file_reference;
            imageLocation.key = fileLocation.key;
            imageLocation.f53iv = fileLocation.f447iv;
            imageLocation.access_hash = fileLocation.secret;
        }
        return imageLocation;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0022  */
    public static java.lang.String getStippedKey(java.lang.Object r3, java.lang.Object r4, java.lang.Object r5) {
        /*
        r0 = r3 instanceof org.telegram.tgnet.TLRPC.WebPage;
        r1 = "stripped";
        if (r0 == 0) goto L_0x00f0;
    L_0x0006:
        r0 = r4 instanceof org.telegram.messenger.ImageLocation;
        if (r0 == 0) goto L_0x001d;
    L_0x000a:
        r0 = r4;
        r0 = (org.telegram.messenger.ImageLocation) r0;
        r2 = r0.document;
        if (r2 == 0) goto L_0x0012;
    L_0x0011:
        goto L_0x001e;
    L_0x0012:
        r2 = r0.photoSize;
        if (r2 == 0) goto L_0x0017;
    L_0x0016:
        goto L_0x001e;
    L_0x0017:
        r0 = r0.photo;
        if (r0 == 0) goto L_0x001d;
    L_0x001b:
        r2 = r0;
        goto L_0x001e;
    L_0x001d:
        r2 = r4;
    L_0x001e:
        r4 = "_";
        if (r2 != 0) goto L_0x003c;
    L_0x0022:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r0.append(r3);
        r0.append(r4);
        r0.append(r5);
        r3 = r0.toString();
        return r3;
    L_0x003c:
        r5 = r2 instanceof org.telegram.tgnet.TLRPC.Document;
        if (r5 == 0) goto L_0x005e;
    L_0x0040:
        r2 = (org.telegram.tgnet.TLRPC.Document) r2;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r5.append(r3);
        r5.append(r4);
        r3 = r2.f441id;
        r5.append(r3);
        r3 = r5.toString();
        return r3;
    L_0x005e:
        r5 = r2 instanceof org.telegram.tgnet.TLRPC.Photo;
        if (r5 == 0) goto L_0x0080;
    L_0x0062:
        r2 = (org.telegram.tgnet.TLRPC.Photo) r2;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r5.append(r3);
        r5.append(r4);
        r3 = r2.f463id;
        r5.append(r3);
        r3 = r5.toString();
        return r3;
    L_0x0080:
        r5 = r2 instanceof org.telegram.tgnet.TLRPC.PhotoSize;
        if (r5 == 0) goto L_0x00c6;
    L_0x0084:
        r2 = (org.telegram.tgnet.TLRPC.PhotoSize) r2;
        r5 = r2.location;
        if (r5 == 0) goto L_0x00b2;
    L_0x008a:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r5.append(r3);
        r5.append(r4);
        r3 = r2.location;
        r3 = r3.local_id;
        r5.append(r3);
        r5.append(r4);
        r3 = r2.location;
        r3 = r3.volume_id;
        r5.append(r3);
        r3 = r5.toString();
        return r3;
    L_0x00b2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r4.append(r3);
        r3 = r4.toString();
        return r3;
    L_0x00c6:
        r5 = r2 instanceof org.telegram.tgnet.TLRPC.FileLocation;
        if (r5 == 0) goto L_0x00f0;
    L_0x00ca:
        r2 = (org.telegram.tgnet.TLRPC.FileLocation) r2;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r5.append(r3);
        r5.append(r4);
        r3 = r2.local_id;
        r5.append(r3);
        r5.append(r4);
        r3 = r2.volume_id;
        r5.append(r3);
        r3 = r5.toString();
        return r3;
    L_0x00f0:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r3 = org.telegram.messenger.FileRefController.getKeyForParentObject(r3);
        r4.append(r3);
        r3 = r4.toString();
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLocation.getStippedKey(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.String");
    }

    public String getKey(Object obj, Object obj2) {
        String str = "_";
        StringBuilder stringBuilder;
        if (this.secureDocument != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.secureDocument.secureFile.dc_id);
            stringBuilder.append(str);
            stringBuilder.append(this.secureDocument.secureFile.f561id);
            return stringBuilder.toString();
        }
        PhotoSize photoSize = this.photoSize;
        if (photoSize instanceof TL_photoStrippedSize) {
            if (photoSize.bytes.length > 0) {
                return getStippedKey(obj, obj2, photoSize);
            }
        } else if (this.location != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.location.volume_id);
            stringBuilder.append(str);
            stringBuilder.append(this.location.local_id);
            return stringBuilder.toString();
        } else {
            WebFile webFile = this.webFile;
            if (webFile != null) {
                return Utilities.MD5(webFile.url);
            }
            Document document = this.document;
            if (document == null) {
                String str2 = this.path;
                if (str2 != null) {
                    return Utilities.MD5(str2);
                }
            } else if (!(document.f441id == 0 || document.dc_id == 0)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.document.dc_id);
                stringBuilder.append(str);
                stringBuilder.append(this.document.f441id);
                return stringBuilder.toString();
            }
        }
        return null;
    }

    public boolean isEncrypted() {
        return this.key != null;
    }

    public int getSize() {
        PhotoSize photoSize = this.photoSize;
        if (photoSize != null) {
            return photoSize.size;
        }
        SecureDocument secureDocument = this.secureDocument;
        if (secureDocument != null) {
            TL_secureFile tL_secureFile = secureDocument.secureFile;
            if (tL_secureFile != null) {
                return tL_secureFile.size;
            }
        }
        Document document = this.document;
        if (document != null) {
            return document.size;
        }
        WebFile webFile = this.webFile;
        if (webFile != null) {
            return webFile.size;
        }
        return this.currentSize;
    }
}