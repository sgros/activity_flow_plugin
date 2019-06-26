// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class ImageLocation
{
    public long access_hash;
    public int currentSize;
    public int dc_id;
    public TLRPC.Document document;
    public long documentId;
    public byte[] file_reference;
    public byte[] iv;
    public byte[] key;
    public TLRPC.TL_fileLocationToBeDeprecated location;
    public String path;
    public TLRPC.Photo photo;
    public long photoId;
    public TLRPC.InputPeer photoPeer;
    public boolean photoPeerBig;
    public TLRPC.PhotoSize photoSize;
    public SecureDocument secureDocument;
    public TLRPC.InputStickerSet stickerSet;
    public String thumbSize;
    public WebFile webFile;
    
    public static ImageLocation getForChat(final TLRPC.Chat chat, final boolean b) {
        if (chat != null) {
            final TLRPC.ChatPhoto photo = chat.photo;
            if (photo != null) {
                TLRPC.FileLocation fileLocation;
                if (b) {
                    fileLocation = photo.photo_big;
                }
                else {
                    fileLocation = photo.photo_small;
                }
                if (fileLocation == null) {
                    return null;
                }
                TLRPC.InputPeer inputPeer;
                if (ChatObject.isChannel(chat)) {
                    if (chat.access_hash == 0L) {
                        return null;
                    }
                    inputPeer = new TLRPC.TL_inputPeerChannel();
                    inputPeer.channel_id = chat.id;
                    inputPeer.access_hash = chat.access_hash;
                }
                else {
                    inputPeer = new TLRPC.TL_inputPeerChat();
                    inputPeer.chat_id = chat.id;
                }
                int n = chat.photo.dc_id;
                if (n == 0) {
                    n = fileLocation.dc_id;
                }
                return getForPhoto(fileLocation, 0, null, null, inputPeer, b, n, null, null);
            }
        }
        return null;
    }
    
    public static ImageLocation getForDocument(final TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        final ImageLocation imageLocation = new ImageLocation();
        imageLocation.document = document;
        imageLocation.key = document.key;
        imageLocation.iv = document.iv;
        imageLocation.currentSize = document.size;
        return imageLocation;
    }
    
    public static ImageLocation getForDocument(final TLRPC.PhotoSize photoSize, final TLRPC.Document document) {
        if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
            final ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        }
        if (photoSize != null && document != null) {
            return getForPhoto(photoSize.location, photoSize.size, null, document, null, false, document.dc_id, null, photoSize.type);
        }
        return null;
    }
    
    public static ImageLocation getForLocal(final TLRPC.FileLocation fileLocation) {
        if (fileLocation == null) {
            return null;
        }
        final ImageLocation imageLocation = new ImageLocation();
        imageLocation.location = new TLRPC.TL_fileLocationToBeDeprecated();
        final TLRPC.TL_fileLocationToBeDeprecated location = imageLocation.location;
        location.local_id = fileLocation.local_id;
        location.volume_id = fileLocation.volume_id;
        location.secret = fileLocation.secret;
        location.dc_id = fileLocation.dc_id;
        return imageLocation;
    }
    
    public static ImageLocation getForObject(final TLRPC.PhotoSize photoSize, final TLObject tlObject) {
        if (tlObject instanceof TLRPC.Photo) {
            return getForPhoto(photoSize, (TLRPC.Photo)tlObject);
        }
        if (tlObject instanceof TLRPC.Document) {
            return getForDocument(photoSize, (TLRPC.Document)tlObject);
        }
        return null;
    }
    
    public static ImageLocation getForPath(final String path) {
        if (path == null) {
            return null;
        }
        final ImageLocation imageLocation = new ImageLocation();
        imageLocation.path = path;
        return imageLocation;
    }
    
    private static ImageLocation getForPhoto(final TLRPC.FileLocation fileLocation, final int currentSize, final TLRPC.Photo photo, final TLRPC.Document document, final TLRPC.InputPeer photoPeer, final boolean photoPeerBig, final int dc_id, final TLRPC.InputStickerSet stickerSet, final String s) {
        if (fileLocation != null && (photo != null || photoPeer != null || stickerSet != null || document != null)) {
            final ImageLocation imageLocation = new ImageLocation();
            imageLocation.dc_id = dc_id;
            imageLocation.photo = photo;
            imageLocation.currentSize = currentSize;
            imageLocation.photoPeer = photoPeer;
            imageLocation.photoPeerBig = photoPeerBig;
            imageLocation.stickerSet = stickerSet;
            if (fileLocation instanceof TLRPC.TL_fileLocationToBeDeprecated) {
                imageLocation.location = (TLRPC.TL_fileLocationToBeDeprecated)fileLocation;
                if (photo != null) {
                    imageLocation.file_reference = photo.file_reference;
                    imageLocation.access_hash = photo.access_hash;
                    imageLocation.photoId = photo.id;
                    imageLocation.thumbSize = s;
                }
                else if (document != null) {
                    imageLocation.file_reference = document.file_reference;
                    imageLocation.access_hash = document.access_hash;
                    imageLocation.documentId = document.id;
                    imageLocation.thumbSize = s;
                }
            }
            else {
                imageLocation.location = new TLRPC.TL_fileLocationToBeDeprecated();
                final TLRPC.TL_fileLocationToBeDeprecated location = imageLocation.location;
                location.local_id = fileLocation.local_id;
                location.volume_id = fileLocation.volume_id;
                location.secret = fileLocation.secret;
                imageLocation.dc_id = fileLocation.dc_id;
                imageLocation.file_reference = fileLocation.file_reference;
                imageLocation.key = fileLocation.key;
                imageLocation.iv = fileLocation.iv;
                imageLocation.access_hash = fileLocation.secret;
            }
            return imageLocation;
        }
        return null;
    }
    
    public static ImageLocation getForPhoto(final TLRPC.PhotoSize photoSize, final TLRPC.Photo photo) {
        if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
            final ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        }
        if (photoSize != null && photo != null) {
            int n = photo.dc_id;
            if (n == 0) {
                n = photoSize.location.dc_id;
            }
            return getForPhoto(photoSize.location, photoSize.size, photo, null, null, false, n, null, photoSize.type);
        }
        return null;
    }
    
    public static ImageLocation getForSecureDocument(final SecureDocument secureDocument) {
        if (secureDocument == null) {
            return null;
        }
        final ImageLocation imageLocation = new ImageLocation();
        imageLocation.secureDocument = secureDocument;
        return imageLocation;
    }
    
    public static ImageLocation getForSticker(final TLRPC.PhotoSize photoSize, final TLRPC.Document document) {
        if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
            final ImageLocation imageLocation = new ImageLocation();
            imageLocation.photoSize = photoSize;
            return imageLocation;
        }
        if (photoSize == null || document == null) {
            return null;
        }
        final TLRPC.InputStickerSet inputStickerSet = DataQuery.getInputStickerSet(document);
        if (inputStickerSet == null) {
            return null;
        }
        return getForPhoto(photoSize.location, photoSize.size, null, null, null, false, document.dc_id, inputStickerSet, photoSize.type);
    }
    
    public static ImageLocation getForUser(final TLRPC.User user, final boolean b) {
        if (user != null && user.access_hash != 0L) {
            final TLRPC.UserProfilePhoto photo = user.photo;
            if (photo != null) {
                TLRPC.FileLocation fileLocation;
                if (b) {
                    fileLocation = photo.photo_big;
                }
                else {
                    fileLocation = photo.photo_small;
                }
                if (fileLocation == null) {
                    return null;
                }
                final TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
                tl_inputPeerUser.user_id = user.id;
                tl_inputPeerUser.access_hash = user.access_hash;
                int n = user.photo.dc_id;
                if (n == 0) {
                    n = fileLocation.dc_id;
                }
                return getForPhoto(fileLocation, 0, null, null, tl_inputPeerUser, b, n, null, null);
            }
        }
        return null;
    }
    
    public static ImageLocation getForWebFile(final WebFile webFile) {
        if (webFile == null) {
            return null;
        }
        final ImageLocation imageLocation = new ImageLocation();
        imageLocation.webFile = webFile;
        imageLocation.currentSize = webFile.size;
        return imageLocation;
    }
    
    public static String getStippedKey(final Object o, Object o2, Object obj) {
        if (o instanceof TLRPC.WebPage) {
            if (o2 instanceof ImageLocation) {
                final ImageLocation imageLocation = (ImageLocation)o2;
                final TLRPC.Document document = imageLocation.document;
                if (document != null) {
                    o2 = document;
                }
                else {
                    final TLRPC.PhotoSize photoSize = imageLocation.photoSize;
                    if (photoSize != null) {
                        o2 = photoSize;
                    }
                    else {
                        final TLRPC.Photo photo = imageLocation.photo;
                        if (photo != null) {
                            o2 = photo;
                        }
                    }
                }
            }
            if (o2 == null) {
                o2 = new StringBuilder();
                ((StringBuilder)o2).append("stripped");
                ((StringBuilder)o2).append(FileRefController.getKeyForParentObject(o));
                ((StringBuilder)o2).append("_");
                ((StringBuilder)o2).append(obj);
                return ((StringBuilder)o2).toString();
            }
            if (o2 instanceof TLRPC.Document) {
                final TLRPC.Document document2 = (TLRPC.Document)o2;
                obj = new StringBuilder();
                ((StringBuilder)obj).append("stripped");
                ((StringBuilder)obj).append(FileRefController.getKeyForParentObject(o));
                ((StringBuilder)obj).append("_");
                ((StringBuilder)obj).append(document2.id);
                return ((StringBuilder)obj).toString();
            }
            if (o2 instanceof TLRPC.Photo) {
                final TLRPC.Photo photo2 = (TLRPC.Photo)o2;
                o2 = new StringBuilder();
                ((StringBuilder)o2).append("stripped");
                ((StringBuilder)o2).append(FileRefController.getKeyForParentObject(o));
                ((StringBuilder)o2).append("_");
                ((StringBuilder)o2).append(photo2.id);
                return ((StringBuilder)o2).toString();
            }
            if (o2 instanceof TLRPC.PhotoSize) {
                final TLRPC.PhotoSize photoSize2 = (TLRPC.PhotoSize)o2;
                if (photoSize2.location != null) {
                    o2 = new StringBuilder();
                    ((StringBuilder)o2).append("stripped");
                    ((StringBuilder)o2).append(FileRefController.getKeyForParentObject(o));
                    ((StringBuilder)o2).append("_");
                    ((StringBuilder)o2).append(photoSize2.location.local_id);
                    ((StringBuilder)o2).append("_");
                    ((StringBuilder)o2).append(photoSize2.location.volume_id);
                    return ((StringBuilder)o2).toString();
                }
                o2 = new StringBuilder();
                ((StringBuilder)o2).append("stripped");
                ((StringBuilder)o2).append(FileRefController.getKeyForParentObject(o));
                return ((StringBuilder)o2).toString();
            }
            else if (o2 instanceof TLRPC.FileLocation) {
                final TLRPC.FileLocation fileLocation = (TLRPC.FileLocation)o2;
                obj = new StringBuilder();
                ((StringBuilder)obj).append("stripped");
                ((StringBuilder)obj).append(FileRefController.getKeyForParentObject(o));
                ((StringBuilder)obj).append("_");
                ((StringBuilder)obj).append(fileLocation.local_id);
                ((StringBuilder)obj).append("_");
                ((StringBuilder)obj).append(fileLocation.volume_id);
                return ((StringBuilder)obj).toString();
            }
        }
        o2 = new StringBuilder();
        ((StringBuilder)o2).append("stripped");
        ((StringBuilder)o2).append(FileRefController.getKeyForParentObject(o));
        return ((StringBuilder)o2).toString();
    }
    
    public String getKey(final Object o, final Object o2) {
        if (this.secureDocument != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.secureDocument.secureFile.dc_id);
            sb.append("_");
            sb.append(this.secureDocument.secureFile.id);
            return sb.toString();
        }
        final TLRPC.PhotoSize photoSize = this.photoSize;
        if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
            if (photoSize.bytes.length > 0) {
                return getStippedKey(o, o2, photoSize);
            }
        }
        else {
            if (this.location != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.location.volume_id);
                sb2.append("_");
                sb2.append(this.location.local_id);
                return sb2.toString();
            }
            final WebFile webFile = this.webFile;
            if (webFile != null) {
                return Utilities.MD5(webFile.url);
            }
            final TLRPC.Document document = this.document;
            if (document != null) {
                if (document.id != 0L && document.dc_id != 0) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.document.dc_id);
                    sb3.append("_");
                    sb3.append(this.document.id);
                    return sb3.toString();
                }
            }
            else {
                final String path = this.path;
                if (path != null) {
                    return Utilities.MD5(path);
                }
            }
        }
        return null;
    }
    
    public int getSize() {
        final TLRPC.PhotoSize photoSize = this.photoSize;
        if (photoSize != null) {
            return photoSize.size;
        }
        final SecureDocument secureDocument = this.secureDocument;
        if (secureDocument != null) {
            final TLRPC.TL_secureFile secureFile = secureDocument.secureFile;
            if (secureFile != null) {
                return secureFile.size;
            }
        }
        else {
            final TLRPC.Document document = this.document;
            if (document != null) {
                return document.size;
            }
            final WebFile webFile = this.webFile;
            if (webFile != null) {
                return webFile.size;
            }
        }
        return this.currentSize;
    }
    
    public boolean isEncrypted() {
        return this.key != null;
    }
}
