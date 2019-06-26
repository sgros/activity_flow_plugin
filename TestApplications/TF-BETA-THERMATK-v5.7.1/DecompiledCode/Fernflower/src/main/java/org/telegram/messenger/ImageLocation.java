package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class ImageLocation {
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

   public static ImageLocation getForChat(TLRPC.Chat var0, boolean var1) {
      if (var0 != null) {
         TLRPC.ChatPhoto var2 = var0.photo;
         if (var2 != null) {
            TLRPC.FileLocation var5;
            if (var1) {
               var5 = var2.photo_big;
            } else {
               var5 = var2.photo_small;
            }

            if (var5 == null) {
               return null;
            }

            Object var3;
            if (ChatObject.isChannel(var0)) {
               if (var0.access_hash == 0L) {
                  return null;
               }

               var3 = new TLRPC.TL_inputPeerChannel();
               ((TLRPC.InputPeer)var3).channel_id = var0.id;
               ((TLRPC.InputPeer)var3).access_hash = var0.access_hash;
            } else {
               var3 = new TLRPC.TL_inputPeerChat();
               ((TLRPC.InputPeer)var3).chat_id = var0.id;
            }

            int var4 = var0.photo.dc_id;
            if (var4 == 0) {
               var4 = var5.dc_id;
            }

            return getForPhoto(var5, 0, (TLRPC.Photo)null, (TLRPC.Document)null, (TLRPC.InputPeer)var3, var1, var4, (TLRPC.InputStickerSet)null, (String)null);
         }
      }

      return null;
   }

   public static ImageLocation getForDocument(TLRPC.Document var0) {
      if (var0 == null) {
         return null;
      } else {
         ImageLocation var1 = new ImageLocation();
         var1.document = var0;
         var1.key = var0.key;
         var1.iv = var0.iv;
         var1.currentSize = var0.size;
         return var1;
      }
   }

   public static ImageLocation getForDocument(TLRPC.PhotoSize var0, TLRPC.Document var1) {
      if (var0 instanceof TLRPC.TL_photoStrippedSize) {
         ImageLocation var2 = new ImageLocation();
         var2.photoSize = var0;
         return var2;
      } else {
         return var0 != null && var1 != null ? getForPhoto(var0.location, var0.size, (TLRPC.Photo)null, var1, (TLRPC.InputPeer)null, false, var1.dc_id, (TLRPC.InputStickerSet)null, var0.type) : null;
      }
   }

   public static ImageLocation getForLocal(TLRPC.FileLocation var0) {
      if (var0 == null) {
         return null;
      } else {
         ImageLocation var1 = new ImageLocation();
         var1.location = new TLRPC.TL_fileLocationToBeDeprecated();
         TLRPC.TL_fileLocationToBeDeprecated var2 = var1.location;
         var2.local_id = var0.local_id;
         var2.volume_id = var0.volume_id;
         var2.secret = var0.secret;
         var2.dc_id = var0.dc_id;
         return var1;
      }
   }

   public static ImageLocation getForObject(TLRPC.PhotoSize var0, TLObject var1) {
      if (var1 instanceof TLRPC.Photo) {
         return getForPhoto(var0, (TLRPC.Photo)var1);
      } else {
         return var1 instanceof TLRPC.Document ? getForDocument(var0, (TLRPC.Document)var1) : null;
      }
   }

   public static ImageLocation getForPath(String var0) {
      if (var0 == null) {
         return null;
      } else {
         ImageLocation var1 = new ImageLocation();
         var1.path = var0;
         return var1;
      }
   }

   private static ImageLocation getForPhoto(TLRPC.FileLocation var0, int var1, TLRPC.Photo var2, TLRPC.Document var3, TLRPC.InputPeer var4, boolean var5, int var6, TLRPC.InputStickerSet var7, String var8) {
      if (var0 == null || var2 == null && var4 == null && var7 == null && var3 == null) {
         return null;
      } else {
         ImageLocation var9 = new ImageLocation();
         var9.dc_id = var6;
         var9.photo = var2;
         var9.currentSize = var1;
         var9.photoPeer = var4;
         var9.photoPeerBig = var5;
         var9.stickerSet = var7;
         if (var0 instanceof TLRPC.TL_fileLocationToBeDeprecated) {
            var9.location = (TLRPC.TL_fileLocationToBeDeprecated)var0;
            if (var2 != null) {
               var9.file_reference = var2.file_reference;
               var9.access_hash = var2.access_hash;
               var9.photoId = var2.id;
               var9.thumbSize = var8;
            } else if (var3 != null) {
               var9.file_reference = var3.file_reference;
               var9.access_hash = var3.access_hash;
               var9.documentId = var3.id;
               var9.thumbSize = var8;
            }
         } else {
            var9.location = new TLRPC.TL_fileLocationToBeDeprecated();
            TLRPC.TL_fileLocationToBeDeprecated var10 = var9.location;
            var10.local_id = var0.local_id;
            var10.volume_id = var0.volume_id;
            var10.secret = var0.secret;
            var9.dc_id = var0.dc_id;
            var9.file_reference = var0.file_reference;
            var9.key = var0.key;
            var9.iv = var0.iv;
            var9.access_hash = var0.secret;
         }

         return var9;
      }
   }

   public static ImageLocation getForPhoto(TLRPC.PhotoSize var0, TLRPC.Photo var1) {
      if (var0 instanceof TLRPC.TL_photoStrippedSize) {
         ImageLocation var3 = new ImageLocation();
         var3.photoSize = var0;
         return var3;
      } else if (var0 != null && var1 != null) {
         int var2 = var1.dc_id;
         if (var2 == 0) {
            var2 = var0.location.dc_id;
         }

         return getForPhoto(var0.location, var0.size, var1, (TLRPC.Document)null, (TLRPC.InputPeer)null, false, var2, (TLRPC.InputStickerSet)null, var0.type);
      } else {
         return null;
      }
   }

   public static ImageLocation getForSecureDocument(SecureDocument var0) {
      if (var0 == null) {
         return null;
      } else {
         ImageLocation var1 = new ImageLocation();
         var1.secureDocument = var0;
         return var1;
      }
   }

   public static ImageLocation getForSticker(TLRPC.PhotoSize var0, TLRPC.Document var1) {
      if (var0 instanceof TLRPC.TL_photoStrippedSize) {
         ImageLocation var3 = new ImageLocation();
         var3.photoSize = var0;
         return var3;
      } else if (var0 != null && var1 != null) {
         TLRPC.InputStickerSet var2 = DataQuery.getInputStickerSet(var1);
         return var2 == null ? null : getForPhoto(var0.location, var0.size, (TLRPC.Photo)null, (TLRPC.Document)null, (TLRPC.InputPeer)null, false, var1.dc_id, var2, var0.type);
      } else {
         return null;
      }
   }

   public static ImageLocation getForUser(TLRPC.User var0, boolean var1) {
      if (var0 != null && var0.access_hash != 0L) {
         TLRPC.UserProfilePhoto var2 = var0.photo;
         if (var2 != null) {
            TLRPC.FileLocation var5;
            if (var1) {
               var5 = var2.photo_big;
            } else {
               var5 = var2.photo_small;
            }

            if (var5 == null) {
               return null;
            }

            TLRPC.TL_inputPeerUser var3 = new TLRPC.TL_inputPeerUser();
            var3.user_id = var0.id;
            var3.access_hash = var0.access_hash;
            int var4 = var0.photo.dc_id;
            if (var4 == 0) {
               var4 = var5.dc_id;
            }

            return getForPhoto(var5, 0, (TLRPC.Photo)null, (TLRPC.Document)null, var3, var1, var4, (TLRPC.InputStickerSet)null, (String)null);
         }
      }

      return null;
   }

   public static ImageLocation getForWebFile(WebFile var0) {
      if (var0 == null) {
         return null;
      } else {
         ImageLocation var1 = new ImageLocation();
         var1.webFile = var0;
         var1.currentSize = var0.size;
         return var1;
      }
   }

   public static String getStippedKey(Object var0, Object var1, Object var2) {
      StringBuilder var6;
      if (var0 instanceof TLRPC.WebPage) {
         if (var1 instanceof ImageLocation) {
            ImageLocation var3 = (ImageLocation)var1;
            TLRPC.Document var4 = var3.document;
            if (var4 != null) {
               var1 = var4;
            } else {
               TLRPC.PhotoSize var10 = var3.photoSize;
               if (var10 != null) {
                  var1 = var10;
               } else {
                  TLRPC.Photo var12 = var3.photo;
                  if (var12 != null) {
                     var1 = var12;
                  }
               }
            }
         }

         if (var1 == null) {
            var6 = new StringBuilder();
            var6.append("stripped");
            var6.append(FileRefController.getKeyForParentObject(var0));
            var6.append("_");
            var6.append(var2);
            return var6.toString();
         }

         StringBuilder var5;
         if (var1 instanceof TLRPC.Document) {
            TLRPC.Document var11 = (TLRPC.Document)var1;
            var5 = new StringBuilder();
            var5.append("stripped");
            var5.append(FileRefController.getKeyForParentObject(var0));
            var5.append("_");
            var5.append(var11.id);
            return var5.toString();
         }

         if (var1 instanceof TLRPC.Photo) {
            TLRPC.Photo var8 = (TLRPC.Photo)var1;
            var6 = new StringBuilder();
            var6.append("stripped");
            var6.append(FileRefController.getKeyForParentObject(var0));
            var6.append("_");
            var6.append(var8.id);
            return var6.toString();
         }

         if (var1 instanceof TLRPC.PhotoSize) {
            TLRPC.PhotoSize var7 = (TLRPC.PhotoSize)var1;
            if (var7.location != null) {
               var6 = new StringBuilder();
               var6.append("stripped");
               var6.append(FileRefController.getKeyForParentObject(var0));
               var6.append("_");
               var6.append(var7.location.local_id);
               var6.append("_");
               var6.append(var7.location.volume_id);
               return var6.toString();
            }

            var6 = new StringBuilder();
            var6.append("stripped");
            var6.append(FileRefController.getKeyForParentObject(var0));
            return var6.toString();
         }

         if (var1 instanceof TLRPC.FileLocation) {
            TLRPC.FileLocation var9 = (TLRPC.FileLocation)var1;
            var5 = new StringBuilder();
            var5.append("stripped");
            var5.append(FileRefController.getKeyForParentObject(var0));
            var5.append("_");
            var5.append(var9.local_id);
            var5.append("_");
            var5.append(var9.volume_id);
            return var5.toString();
         }
      }

      var6 = new StringBuilder();
      var6.append("stripped");
      var6.append(FileRefController.getKeyForParentObject(var0));
      return var6.toString();
   }

   public String getKey(Object var1, Object var2) {
      StringBuilder var6;
      if (this.secureDocument != null) {
         var6 = new StringBuilder();
         var6.append(this.secureDocument.secureFile.dc_id);
         var6.append("_");
         var6.append(this.secureDocument.secureFile.id);
         return var6.toString();
      } else {
         TLRPC.PhotoSize var3 = this.photoSize;
         if (var3 instanceof TLRPC.TL_photoStrippedSize) {
            if (var3.bytes.length > 0) {
               return getStippedKey(var1, var2, var3);
            }
         } else {
            if (this.location != null) {
               var6 = new StringBuilder();
               var6.append(this.location.volume_id);
               var6.append("_");
               var6.append(this.location.local_id);
               return var6.toString();
            }

            WebFile var4 = this.webFile;
            if (var4 != null) {
               return Utilities.MD5(var4.url);
            }

            TLRPC.Document var5 = this.document;
            if (var5 != null) {
               if (var5.id != 0L && var5.dc_id != 0) {
                  var6 = new StringBuilder();
                  var6.append(this.document.dc_id);
                  var6.append("_");
                  var6.append(this.document.id);
                  return var6.toString();
               }
            } else {
               String var7 = this.path;
               if (var7 != null) {
                  return Utilities.MD5(var7);
               }
            }
         }

         return null;
      }
   }

   public int getSize() {
      TLRPC.PhotoSize var1 = this.photoSize;
      if (var1 != null) {
         return var1.size;
      } else {
         SecureDocument var2 = this.secureDocument;
         if (var2 != null) {
            TLRPC.TL_secureFile var3 = var2.secureFile;
            if (var3 != null) {
               return var3.size;
            }
         } else {
            TLRPC.Document var4 = this.document;
            if (var4 != null) {
               return var4.size;
            }

            WebFile var5 = this.webFile;
            if (var5 != null) {
               return var5.size;
            }
         }

         return this.currentSize;
      }
   }

   public boolean isEncrypted() {
      boolean var1;
      if (this.key != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
