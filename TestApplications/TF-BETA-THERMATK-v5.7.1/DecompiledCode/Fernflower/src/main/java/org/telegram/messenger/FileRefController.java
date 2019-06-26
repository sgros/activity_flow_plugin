package org.telegram.messenger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class FileRefController {
   private static volatile FileRefController[] Instance = new FileRefController[3];
   private int currentAccount;
   private long lastCleanupTime = SystemClock.uptimeMillis();
   private HashMap locationRequester = new HashMap();
   private HashMap multiMediaCache = new HashMap();
   private HashMap parentRequester = new HashMap();
   private HashMap responseCache = new HashMap();

   public FileRefController(int var1) {
      this.currentAccount = var1;
   }

   private void cleanupCache() {
      if (Math.abs(SystemClock.uptimeMillis() - this.lastCleanupTime) >= 600000L) {
         this.lastCleanupTime = SystemClock.uptimeMillis();
         ArrayList var1 = null;
         Iterator var2 = this.responseCache.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            FileRefController.CachedResult var4 = (FileRefController.CachedResult)var3.getValue();
            if (Math.abs(SystemClock.uptimeMillis() - var4.firstQueryTime) >= 600000L) {
               ArrayList var7 = var1;
               if (var1 == null) {
                  var7 = new ArrayList();
               }

               var7.add(var3.getKey());
               var1 = var7;
            }
         }

         if (var1 != null) {
            int var5 = 0;

            for(int var6 = var1.size(); var5 < var6; ++var5) {
               this.responseCache.remove(var1.get(var5));
            }
         }

      }
   }

   private FileRefController.CachedResult getCachedResponse(String var1) {
      FileRefController.CachedResult var2 = (FileRefController.CachedResult)this.responseCache.get(var1);
      FileRefController.CachedResult var3 = var2;
      if (var2 != null) {
         var3 = var2;
         if (Math.abs(SystemClock.uptimeMillis() - var2.firstQueryTime) >= 600000L) {
            this.responseCache.remove(var1);
            var3 = null;
         }
      }

      return var3;
   }

   private byte[] getFileReference(TLRPC.Chat var1, TLRPC.InputFileLocation var2, boolean[] var3, TLRPC.InputFileLocation[] var4) {
      if (var1 != null) {
         TLRPC.ChatPhoto var5 = var1.photo;
         if (var5 != null && var2 instanceof TLRPC.TL_inputFileLocation) {
            byte[] var6 = this.getFileReference(var5.photo_small, var2, var3);
            if (this.getPeerReferenceReplacement((TLRPC.User)null, var1, false, var2, var4, var3)) {
               return new byte[0];
            }

            byte[] var7 = var6;
            if (var6 == null) {
               var7 = this.getFileReference(var1.photo.photo_big, var2, var3);
               if (this.getPeerReferenceReplacement((TLRPC.User)null, var1, true, var2, var4, var3)) {
                  return new byte[0];
               }
            }

            return var7;
         }
      }

      return null;
   }

   private byte[] getFileReference(TLRPC.Document var1, TLRPC.InputFileLocation var2, boolean[] var3, TLRPC.InputFileLocation[] var4) {
      if (var1 != null && var2 != null) {
         if (var2 instanceof TLRPC.TL_inputDocumentFileLocation) {
            if (var1.id == var2.id) {
               return var1.file_reference;
            }
         } else {
            int var5 = var1.thumbs.size();

            for(int var6 = 0; var6 < var5; ++var6) {
               TLRPC.PhotoSize var7 = (TLRPC.PhotoSize)var1.thumbs.get(var6);
               byte[] var8 = this.getFileReference(var7, var2, var3);
               if (var3 != null && var3[0]) {
                  var4[0] = new TLRPC.TL_inputDocumentFileLocation();
                  var4[0].id = var1.id;
                  var4[0].volume_id = var2.volume_id;
                  var4[0].local_id = var2.local_id;
                  var4[0].access_hash = var1.access_hash;
                  var2 = var4[0];
                  byte[] var9 = var1.file_reference;
                  var2.file_reference = var9;
                  var4[0].thumb_size = var7.type;
                  return var9;
               }

               if (var8 != null) {
                  return var8;
               }
            }
         }
      }

      return null;
   }

   private byte[] getFileReference(TLRPC.FileLocation var1, TLRPC.InputFileLocation var2, boolean[] var3) {
      if (var1 != null && var2 instanceof TLRPC.TL_inputFileLocation && var1.local_id == var2.local_id && var1.volume_id == var2.volume_id) {
         if (var1.file_reference == null && var3 != null) {
            var3[0] = true;
         }

         return var1.file_reference;
      } else {
         return null;
      }
   }

   private byte[] getFileReference(TLRPC.Photo var1, TLRPC.InputFileLocation var2, boolean[] var3, TLRPC.InputFileLocation[] var4) {
      TLRPC.PhotoSize var5 = null;
      if (var1 == null) {
         return null;
      } else if (var2 instanceof TLRPC.TL_inputPhotoFileLocation) {
         byte[] var10 = (byte[])var5;
         if (var1.id == var2.id) {
            var10 = var1.file_reference;
         }

         return var10;
      } else {
         if (var2 instanceof TLRPC.TL_inputFileLocation) {
            int var6 = var1.sizes.size();

            for(int var7 = 0; var7 < var6; ++var7) {
               var5 = (TLRPC.PhotoSize)var1.sizes.get(var7);
               byte[] var8 = this.getFileReference(var5, var2, var3);
               if (var3 != null && var3[0]) {
                  var4[0] = new TLRPC.TL_inputPhotoFileLocation();
                  var4[0].id = var1.id;
                  var4[0].volume_id = var2.volume_id;
                  var4[0].local_id = var2.local_id;
                  var4[0].access_hash = var1.access_hash;
                  var2 = var4[0];
                  byte[] var9 = var1.file_reference;
                  var2.file_reference = var9;
                  var4[0].thumb_size = var5.type;
                  return var9;
               }

               if (var8 != null) {
                  return var8;
               }
            }
         }

         return null;
      }
   }

   private byte[] getFileReference(TLRPC.PhotoSize var1, TLRPC.InputFileLocation var2, boolean[] var3) {
      return var1 != null && var2 instanceof TLRPC.TL_inputFileLocation ? this.getFileReference(var1.location, var2, var3) : null;
   }

   private byte[] getFileReference(TLRPC.User var1, TLRPC.InputFileLocation var2, boolean[] var3, TLRPC.InputFileLocation[] var4) {
      if (var1 != null) {
         TLRPC.UserProfilePhoto var5 = var1.photo;
         if (var5 != null && var2 instanceof TLRPC.TL_inputFileLocation) {
            byte[] var6 = this.getFileReference(var5.photo_small, var2, var3);
            if (this.getPeerReferenceReplacement(var1, (TLRPC.Chat)null, false, var2, var4, var3)) {
               return new byte[0];
            }

            byte[] var7 = var6;
            if (var6 == null) {
               var7 = this.getFileReference(var1.photo.photo_big, var2, var3);
               if (this.getPeerReferenceReplacement(var1, (TLRPC.Chat)null, true, var2, var4, var3)) {
                  return new byte[0];
               }
            }

            return var7;
         }
      }

      return null;
   }

   private byte[] getFileReference(TLRPC.WebPage var1, TLRPC.InputFileLocation var2, boolean[] var3, TLRPC.InputFileLocation[] var4) {
      byte[] var5 = this.getFileReference(var1.document, var2, var3, var4);
      if (var5 != null) {
         return var5;
      } else {
         var5 = this.getFileReference(var1.photo, var2, var3, var4);
         if (var5 != null) {
            return var5;
         } else {
            if (var5 == null) {
               TLRPC.Page var9 = var1.cached_page;
               if (var9 != null) {
                  int var6 = var9.documents.size();
                  byte var7 = 0;

                  int var8;
                  for(var8 = 0; var8 < var6; ++var8) {
                     var5 = this.getFileReference((TLRPC.Document)var1.cached_page.documents.get(var8), var2, var3, var4);
                     if (var5 != null) {
                        return var5;
                     }
                  }

                  var6 = var1.cached_page.photos.size();

                  for(var8 = var7; var8 < var6; ++var8) {
                     var5 = this.getFileReference((TLRPC.Photo)var1.cached_page.photos.get(var8), var2, var3, var4);
                     if (var5 != null) {
                        return var5;
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   public static FileRefController getInstance(int var0) {
      FileRefController var1 = Instance[var0];
      FileRefController var2 = var1;
      if (var1 == null) {
         synchronized(FileRefController.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               FileRefController[] var23;
               try {
                  var23 = Instance;
                  var2 = new FileRefController(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   public static String getKeyForParentObject(Object var0) {
      int var1;
      StringBuilder var2;
      if (var0 instanceof MessageObject) {
         MessageObject var13 = (MessageObject)var0;
         var1 = var13.getChannelId();
         var2 = new StringBuilder();
         var2.append("message");
         var2.append(var13.getRealId());
         var2.append("_");
         var2.append(var1);
         return var2.toString();
      } else if (var0 instanceof TLRPC.Message) {
         TLRPC.Message var12 = (TLRPC.Message)var0;
         TLRPC.Peer var15 = var12.to_id;
         if (var15 != null) {
            var1 = var15.channel_id;
         } else {
            var1 = 0;
         }

         var2 = new StringBuilder();
         var2.append("message");
         var2.append(var12.id);
         var2.append("_");
         var2.append(var1);
         return var2.toString();
      } else if (var0 instanceof TLRPC.WebPage) {
         TLRPC.WebPage var10 = (TLRPC.WebPage)var0;
         var2 = new StringBuilder();
         var2.append("webpage");
         var2.append(var10.id);
         return var2.toString();
      } else {
         StringBuilder var4;
         if (var0 instanceof TLRPC.User) {
            TLRPC.User var14 = (TLRPC.User)var0;
            var4 = new StringBuilder();
            var4.append("user");
            var4.append(var14.id);
            return var4.toString();
         } else if (var0 instanceof TLRPC.Chat) {
            TLRPC.Chat var8 = (TLRPC.Chat)var0;
            var2 = new StringBuilder();
            var2.append("chat");
            var2.append(var8.id);
            return var2.toString();
         } else if (var0 instanceof String) {
            String var11 = (String)var0;
            var4 = new StringBuilder();
            var4.append("str");
            var4.append(var11);
            return var4.toString();
         } else if (var0 instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet var9 = (TLRPC.TL_messages_stickerSet)var0;
            var4 = new StringBuilder();
            var4.append("set");
            var4.append(var9.set.id);
            return var4.toString();
         } else if (var0 instanceof TLRPC.StickerSetCovered) {
            TLRPC.StickerSetCovered var6 = (TLRPC.StickerSetCovered)var0;
            var2 = new StringBuilder();
            var2.append("set");
            var2.append(var6.set.id);
            return var2.toString();
         } else if (var0 instanceof TLRPC.InputStickerSet) {
            TLRPC.InputStickerSet var5 = (TLRPC.InputStickerSet)var0;
            var2 = new StringBuilder();
            var2.append("set");
            var2.append(var5.id);
            return var2.toString();
         } else if (var0 instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper var7 = (TLRPC.TL_wallPaper)var0;
            var4 = new StringBuilder();
            var4.append("wallpaper");
            var4.append(var7.id);
            return var4.toString();
         } else {
            String var3;
            if (var0 != null) {
               var2 = new StringBuilder();
               var2.append("");
               var2.append(var0);
               var3 = var2.toString();
            } else {
               var3 = null;
            }

            return var3;
         }
      }
   }

   private boolean getPeerReferenceReplacement(TLRPC.User var1, TLRPC.Chat var2, boolean var3, TLRPC.InputFileLocation var4, TLRPC.InputFileLocation[] var5, boolean[] var6) {
      if (var6 != null && var6[0]) {
         var5[0] = new TLRPC.TL_inputPeerPhotoFileLocation();
         TLRPC.InputFileLocation var11 = var5[0];
         long var7 = var4.volume_id;
         var11.id = var7;
         var5[0].volume_id = var7;
         var5[0].local_id = var4.local_id;
         var5[0].big = var3;
         Object var9;
         if (var1 != null) {
            TLRPC.TL_inputPeerUser var10 = new TLRPC.TL_inputPeerUser();
            var10.user_id = var1.id;
            var10.access_hash = var1.access_hash;
            var9 = var10;
         } else if (ChatObject.isChannel(var2)) {
            var9 = new TLRPC.TL_inputPeerChat();
            ((TLRPC.InputPeer)var9).chat_id = var2.id;
         } else {
            var9 = new TLRPC.TL_inputPeerChannel();
            ((TLRPC.InputPeer)var9).channel_id = var2.id;
            ((TLRPC.InputPeer)var9).access_hash = var2.access_hash;
         }

         var5[0].peer = (TLRPC.InputPeer)var9;
         return true;
      } else {
         return false;
      }
   }

   public static boolean isFileRefError(String var0) {
      boolean var1;
      if ("FILEREF_EXPIRED".equals(var0) || "FILE_REFERENCE_EXPIRED".equals(var0) || "FILE_REFERENCE_EMPTY".equals(var0) || var0 != null && var0.startsWith("FILE_REFERENCE_")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   static void lambda$onUpdateObjectReference$18(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$onUpdateObjectReference$19(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$onUpdateObjectReference$20(TLObject var0, TLRPC.TL_error var1) {
   }

   private boolean onRequestComplete(String var1, String var2, TLObject var3, boolean var4) {
      ArrayList var6;
      int var7;
      int var8;
      boolean var9;
      boolean var11;
      label321: {
         boolean var5 = false;
         if (var2 != null) {
            var6 = (ArrayList)this.parentRequester.get(var2);
            if (var6 != null) {
               var7 = var6.size();
               var8 = 0;

               for(var9 = false; var8 < var7; ++var8) {
                  FileRefController.Requester var10 = (FileRefController.Requester)var6.get(var8);
                  if (!var10.completed) {
                     String var26 = var10.locationKey;
                     if (var4 && !var9) {
                        var11 = true;
                     } else {
                        var11 = false;
                     }

                     if (this.onRequestComplete(var26, (String)null, var3, var11)) {
                        var9 = true;
                     }
                  }
               }

               if (var9) {
                  this.putReponseToCache(var2, var3);
               }

               this.parentRequester.remove(var2);
               break label321;
            }
         }

         var9 = false;
      }

      var6 = (ArrayList)this.locationRequester.get(var1);
      if (var6 == null) {
         return var9;
      } else {
         int var12 = var6.size();
         boolean[] var13 = null;
         Object var14 = var13;
         Object var21 = var13;
         var8 = 0;

         ArrayList var37;
         for(var11 = var9; var8 < var12; var6 = var37) {
            FileRefController.Requester var15 = (FileRefController.Requester)var6.get(var8);
            if (var15.completed) {
               var37 = var6;
            } else {
               if (var15.location instanceof TLRPC.TL_inputFileLocation) {
                  var14 = new TLRPC.InputFileLocation[1];
                  var13 = new boolean[1];
               }

               Object var25;
               label294: {
                  var15.completed = true;
                  int var17;
                  ArrayList var22;
                  Object var27;
                  ArrayList var32;
                  TLRPC.Chat var44;
                  if (var3 instanceof TLRPC.messages_Messages) {
                     TLRPC.messages_Messages var16 = (TLRPC.messages_Messages)var3;
                     if (!var16.messages.isEmpty()) {
                        var17 = var16.messages.size();
                        var7 = 0;

                        label259: {
                           while(true) {
                              var27 = var21;
                              if (var7 >= var17) {
                                 break;
                              }

                              TLRPC.Message var18 = (TLRPC.Message)var16.messages.get(var7);
                              TLRPC.MessageMedia var28 = var18.media;
                              if (var28 != null) {
                                 TLRPC.Document var19 = var28.document;
                                 if (var19 != null) {
                                    var21 = this.getFileReference((TLRPC.Document)var19, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 } else {
                                    TLRPC.TL_game var40 = var28.game;
                                    if (var40 != null) {
                                       byte[] var29 = this.getFileReference((TLRPC.Document)var40.document, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                       var21 = var29;
                                       if (var29 == null) {
                                          var21 = this.getFileReference((TLRPC.Photo)var18.media.game.photo, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                       }
                                    } else {
                                       TLRPC.Photo var41 = var28.photo;
                                       if (var41 != null) {
                                          var21 = this.getFileReference((TLRPC.Photo)var41, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                       } else {
                                          TLRPC.WebPage var30 = var28.webpage;
                                          if (var30 != null) {
                                             var21 = this.getFileReference((TLRPC.WebPage)var30, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 TLRPC.MessageAction var31 = var18.action;
                                 if (var31 instanceof TLRPC.TL_messageActionChatEditPhoto) {
                                    var21 = this.getFileReference((TLRPC.Photo)var31.photo, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 }
                              }

                              if (var21 != null) {
                                 var27 = var21;
                                 if (var4) {
                                    label249: {
                                       TLRPC.Peer var43 = var18.to_id;
                                       var32 = var6;
                                       if (var43 != null) {
                                          var32 = var6;
                                          if (var43.channel_id != 0) {
                                             var17 = var16.chats.size();
                                             var7 = 0;

                                             while(true) {
                                                var32 = var6;
                                                if (var7 >= var17) {
                                                   break;
                                                }

                                                var44 = (TLRPC.Chat)var16.chats.get(var7);
                                                int var20 = var44.id;
                                                var32 = var6;
                                                if (var20 == var18.to_id.channel_id) {
                                                   var6 = var6;
                                                   if (var44.megagroup) {
                                                      var18.flags |= Integer.MIN_VALUE;
                                                      var6 = var32;
                                                   }
                                                   break label249;
                                                }

                                                ++var7;
                                                var6 = var6;
                                             }
                                          }
                                       }

                                       var6 = var32;
                                    }

                                    MessagesStorage.getInstance(this.currentAccount).replaceMessageIfExists(var18, this.currentAccount, var16.users, var16.chats, false);
                                    var27 = var21;
                                    var22 = var6;
                                    break label259;
                                 }
                                 break;
                              }

                              ++var7;
                           }

                           var22 = var6;
                        }

                        var25 = var27;
                        var32 = var22;
                        if (var27 == null) {
                           MessagesStorage.getInstance(this.currentAccount).replaceMessageIfExists((TLRPC.Message)var16.messages.get(0), this.currentAccount, var16.users, var16.chats, true);
                           var25 = var27;
                           var32 = var22;
                           if (BuildVars.DEBUG_VERSION) {
                              FileLog.d("file ref not found in messages, replacing message");
                              var25 = var27;
                              var32 = var22;
                           }
                        }
                     } else {
                        var32 = var6;
                        var25 = var21;
                     }
                  } else {
                     ArrayList var39 = var6;
                     if (var3 instanceof TLRPC.WebPage) {
                        var25 = this.getFileReference((TLRPC.WebPage)((TLRPC.WebPage)var3), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                        var32 = var6;
                     } else if (var3 instanceof TLRPC.TL_account_wallPapers) {
                        TLRPC.TL_account_wallPapers var33 = (TLRPC.TL_account_wallPapers)var3;
                        var17 = var33.wallpapers.size();

                        for(var7 = 0; var7 < var17; ++var7) {
                           var21 = this.getFileReference((TLRPC.Document)((TLRPC.TL_wallPaper)var33.wallpapers.get(var7)).document, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                           if (var21 != null) {
                              break;
                           }
                        }

                        var25 = var21;
                        var32 = var6;
                        if (var21 != null) {
                           var25 = var21;
                           var32 = var6;
                           if (var4) {
                              MessagesStorage.getInstance(this.currentAccount).putWallpapers(var33.wallpapers, 1);
                              var25 = var21;
                              var32 = var6;
                           }
                        }
                     } else if (var3 instanceof TLRPC.TL_wallPaper) {
                        TLRPC.TL_wallPaper var34 = (TLRPC.TL_wallPaper)var3;
                        byte[] var23 = this.getFileReference((TLRPC.Document)var34.document, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                        var25 = var23;
                        var32 = var6;
                        if (var23 != null) {
                           var25 = var23;
                           var32 = var6;
                           if (var4) {
                              var6 = new ArrayList();
                              var6.add(var34);
                              MessagesStorage.getInstance(this.currentAccount).putWallpapers(var6, 0);
                              var25 = var23;
                              var32 = var39;
                           }
                        }
                     } else if (var3 instanceof TLRPC.Vector) {
                        TLRPC.Vector var35 = (TLRPC.Vector)var3;
                        var25 = var21;
                        var32 = var6;
                        if (!var35.objects.isEmpty()) {
                           var7 = var35.objects.size();
                           var17 = 0;

                           while(true) {
                              var25 = var21;
                              var32 = var39;
                              if (var17 >= var7) {
                                 break;
                              }

                              var27 = var35.objects.get(var17);
                              if (var27 instanceof TLRPC.User) {
                                 TLRPC.User var42 = (TLRPC.User)var27;
                                 var25 = this.getFileReference((TLRPC.User)var42, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 if (var4 && var25 != null) {
                                    var22 = new ArrayList();
                                    var22.add(var42);
                                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var22, (ArrayList)null, true, true);
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$_CZgXfaxqSfurGMxYgHRkXa2trY(this, var42));
                                 }
                              } else {
                                 var25 = var21;
                                 if (var27 instanceof TLRPC.Chat) {
                                    TLRPC.Chat var24 = (TLRPC.Chat)var27;
                                    var25 = this.getFileReference((TLRPC.Chat)var24, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                    if (var4 && var25 != null) {
                                       var32 = new ArrayList();
                                       var32.add(var24);
                                       MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var32, true, true);
                                       AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$ezrB_EEVIghp6y7yWEa40dOLdLU(this, var24));
                                    }
                                 }
                              }

                              if (var25 != null) {
                                 var32 = var39;
                                 break;
                              }

                              ++var17;
                              var21 = var25;
                           }
                        }
                     } else {
                        if (!(var3 instanceof TLRPC.TL_messages_chats)) {
                           if (var3 instanceof TLRPC.TL_messages_savedGifs) {
                              TLRPC.TL_messages_savedGifs var46 = (TLRPC.TL_messages_savedGifs)var3;
                              var17 = var46.gifs.size();

                              for(var7 = 0; var7 < var17; ++var7) {
                                 var21 = this.getFileReference((TLRPC.Document)((TLRPC.Document)var46.gifs.get(var7)), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 if (var21 != null) {
                                    break;
                                 }
                              }

                              var25 = var21;
                              var37 = var6;
                              if (var4) {
                                 DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(0, var46.gifs, true, 0, true);
                                 var25 = var21;
                                 var37 = var6;
                              }
                           } else if (var3 instanceof TLRPC.TL_messages_stickerSet) {
                              TLRPC.TL_messages_stickerSet var45 = (TLRPC.TL_messages_stickerSet)var3;
                              var27 = var21;
                              if (var21 == null) {
                                 var17 = var45.documents.size();
                                 var7 = 0;

                                 while(true) {
                                    var27 = var21;
                                    if (var7 >= var17) {
                                       break;
                                    }

                                    var21 = this.getFileReference((TLRPC.Document)((TLRPC.Document)var45.documents.get(var7)), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                    if (var21 != null) {
                                       var27 = var21;
                                       break;
                                    }

                                    ++var7;
                                 }
                              }

                              var25 = var27;
                              var37 = var6;
                              if (var4) {
                                 AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$FlgFGmJyAwG8D7Z8OYWnK63ajJo(this, var45));
                                 var25 = var27;
                                 var37 = var6;
                              }
                           } else if (var3 instanceof TLRPC.TL_messages_recentStickers) {
                              TLRPC.TL_messages_recentStickers var47 = (TLRPC.TL_messages_recentStickers)var3;
                              var17 = var47.stickers.size();

                              for(var7 = 0; var7 < var17; ++var7) {
                                 var21 = this.getFileReference((TLRPC.Document)((TLRPC.Document)var47.stickers.get(var7)), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 if (var21 != null) {
                                    break;
                                 }
                              }

                              var25 = var21;
                              var37 = var6;
                              if (var4) {
                                 DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(0, var47.stickers, false, 0, true);
                                 var25 = var21;
                                 var37 = var6;
                              }
                           } else if (var3 instanceof TLRPC.TL_messages_favedStickers) {
                              TLRPC.TL_messages_favedStickers var48 = (TLRPC.TL_messages_favedStickers)var3;
                              var17 = var48.stickers.size();

                              for(var7 = 0; var7 < var17; ++var7) {
                                 var21 = this.getFileReference((TLRPC.Document)((TLRPC.Document)var48.stickers.get(var7)), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                 if (var21 != null) {
                                    break;
                                 }
                              }

                              var25 = var21;
                              var37 = var6;
                              if (var4) {
                                 DataQuery.getInstance(this.currentAccount).processLoadedRecentDocuments(2, var48.stickers, false, 0, true);
                                 var25 = var21;
                                 var37 = var6;
                              }
                           } else {
                              var25 = var21;
                              var37 = var6;
                              if (var3 instanceof TLRPC.photos_Photos) {
                                 TLRPC.photos_Photos var49 = (TLRPC.photos_Photos)var3;
                                 var17 = var49.photos.size();
                                 var7 = 0;

                                 while(true) {
                                    var25 = var21;
                                    var37 = var39;
                                    if (var7 >= var17) {
                                       break label294;
                                    }

                                    var21 = this.getFileReference((TLRPC.Photo)((TLRPC.Photo)var49.photos.get(var7)), var15.location, var13, (TLRPC.InputFileLocation[])var14);
                                    if (var21 != null) {
                                       var25 = var21;
                                       var37 = var39;
                                       break label294;
                                    }

                                    ++var7;
                                 }
                              }
                           }
                           break label294;
                        }

                        TLRPC.TL_messages_chats var36 = (TLRPC.TL_messages_chats)var3;
                        var25 = var21;
                        var32 = var6;
                        if (!var36.chats.isEmpty()) {
                           var17 = var36.chats.size();
                           var7 = 0;

                           while(true) {
                              var25 = var21;
                              var32 = var39;
                              if (var7 >= var17) {
                                 break;
                              }

                              var44 = (TLRPC.Chat)var36.chats.get(var7);
                              var21 = this.getFileReference((TLRPC.Chat)var44, var15.location, var13, (TLRPC.InputFileLocation[])var14);
                              if (var21 != null) {
                                 var25 = var21;
                                 var32 = var39;
                                 if (var4) {
                                    var6 = new ArrayList();
                                    var6.add(var44);
                                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var6, true, true);
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$FileRefController$wxZbkcK98NrwAinOuNo_DdhwDyk(this, var44));
                                    var25 = var21;
                                    var37 = var39;
                                    break label294;
                                 }
                                 break;
                              }

                              ++var7;
                           }
                        }
                     }
                  }

                  var37 = var32;
               }

               var9 = false;
               if (var25 != null) {
                  if (var14 != null) {
                     var21 = ((Object[])var14)[0];
                  } else {
                     var21 = null;
                  }

                  this.onUpdateObjectReference(var15, (byte[])var25, (TLRPC.InputFileLocation)var21);
                  var11 = true;
                  var21 = var25;
               } else {
                  this.sendErrorToObject(var15.args, 1);
                  var21 = var25;
               }
            }

            ++var8;
         }

         this.locationRequester.remove(var1);
         if (var11) {
            this.putReponseToCache(var1, var3);
         }

         return var11;
      }
   }

   private void onUpdateObjectReference(FileRefController.Requester var1, byte[] var2, TLRPC.InputFileLocation var3) {
      if (BuildVars.DEBUG_VERSION) {
         StringBuilder var4 = new StringBuilder();
         var4.append("fileref updated for ");
         var4.append(var1.args[0]);
         var4.append(" ");
         var4.append(var1.locationKey);
         FileLog.d(var4.toString());
      }

      if (var1.args[0] instanceof TLRPC.TL_inputSingleMedia) {
         TLRPC.TL_messages_sendMultiMedia var13 = (TLRPC.TL_messages_sendMultiMedia)var1.args[1];
         Object[] var15 = (Object[])this.multiMediaCache.get(var13);
         if (var15 == null) {
            return;
         }

         TLRPC.TL_inputSingleMedia var8 = (TLRPC.TL_inputSingleMedia)var1.args[0];
         TLRPC.InputMedia var5 = var8.media;
         if (var5 instanceof TLRPC.TL_inputMediaDocument) {
            ((TLRPC.TL_inputMediaDocument)var5).id.file_reference = var2;
         } else if (var5 instanceof TLRPC.TL_inputMediaPhoto) {
            ((TLRPC.TL_inputMediaPhoto)var5).id.file_reference = var2;
         }

         int var6 = var13.multi_media.indexOf(var8);
         if (var6 < 0) {
            return;
         }

         ArrayList var9 = (ArrayList)var15[3];
         var9.set(var6, (Object)null);
         var6 = 0;

         boolean var7;
         for(var7 = true; var6 < var9.size(); ++var6) {
            if (var9.get(var6) != null) {
               var7 = false;
            }
         }

         if (var7) {
            this.multiMediaCache.remove(var13);
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(var13, (ArrayList)var15[1], (ArrayList)var15[2], (ArrayList)null, (SendMessagesHelper.DelayedMessage)var15[4]);
         }
      } else {
         TLRPC.InputMedia var14;
         if (var1.args[0] instanceof TLRPC.TL_messages_sendMedia) {
            var14 = ((TLRPC.TL_messages_sendMedia)var1.args[0]).media;
            if (var14 instanceof TLRPC.TL_inputMediaDocument) {
               ((TLRPC.TL_inputMediaDocument)var14).id.file_reference = var2;
            } else if (var14 instanceof TLRPC.TL_inputMediaPhoto) {
               ((TLRPC.TL_inputMediaPhoto)var14).id.file_reference = var2;
            }

            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)var1.args[0], (MessageObject)var1.args[1], (String)var1.args[2], (SendMessagesHelper.DelayedMessage)var1.args[3], (Boolean)var1.args[4], (SendMessagesHelper.DelayedMessage)var1.args[5], (Object)null);
         } else if (var1.args[0] instanceof TLRPC.TL_messages_editMessage) {
            var14 = ((TLRPC.TL_messages_editMessage)var1.args[0]).media;
            if (var14 instanceof TLRPC.TL_inputMediaDocument) {
               ((TLRPC.TL_inputMediaDocument)var14).id.file_reference = var2;
            } else if (var14 instanceof TLRPC.TL_inputMediaPhoto) {
               ((TLRPC.TL_inputMediaPhoto)var14).id.file_reference = var2;
            }

            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)var1.args[0], (MessageObject)var1.args[1], (String)var1.args[2], (SendMessagesHelper.DelayedMessage)var1.args[3], (Boolean)var1.args[4], (SendMessagesHelper.DelayedMessage)var1.args[5], (Object)null);
         } else if (var1.args[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif var10 = (TLRPC.TL_messages_saveGif)var1.args[0];
            var10.id.file_reference = var2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, _$$Lambda$FileRefController$glsZ_ebv4_mT6CRmECvMkMDX4tM.INSTANCE);
         } else if (var1.args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker var11 = (TLRPC.TL_messages_saveRecentSticker)var1.args[0];
            var11.id.file_reference = var2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, _$$Lambda$FileRefController$7dnf8o_vZU8kWj_oHiGfTHxk_5E.INSTANCE);
         } else if (var1.args[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker var12 = (TLRPC.TL_messages_faveSticker)var1.args[0];
            var12.id.file_reference = var2;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, _$$Lambda$FileRefController$2YbOQ_Rvo_LvdJ__ALCga2DKRrU.INSTANCE);
         } else if (var1.args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.TL_messages_getAttachedStickers var17 = (TLRPC.TL_messages_getAttachedStickers)var1.args[0];
            TLRPC.InputStickeredMedia var16 = var17.media;
            if (var16 instanceof TLRPC.TL_inputStickeredMediaDocument) {
               ((TLRPC.TL_inputStickeredMediaDocument)var16).id.file_reference = var2;
            } else if (var16 instanceof TLRPC.TL_inputStickeredMediaPhoto) {
               ((TLRPC.TL_inputStickeredMediaPhoto)var16).id.file_reference = var2;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var17, (RequestDelegate)var1.args[1]);
         } else if (var1.args[1] instanceof FileLoadOperation) {
            FileLoadOperation var18 = (FileLoadOperation)var1.args[1];
            if (var3 != null) {
               var18.location = var3;
            } else {
               var1.location.file_reference = var2;
            }

            var18.requestingReference = false;
            var18.startDownloadRequest();
         }
      }

   }

   private void putReponseToCache(String var1, TLObject var2) {
      FileRefController.CachedResult var3 = (FileRefController.CachedResult)this.responseCache.get(var1);
      FileRefController.CachedResult var4 = var3;
      if (var3 == null) {
         var4 = new FileRefController.CachedResult();
         var4.response = var2;
         var4.firstQueryTime = SystemClock.uptimeMillis();
         this.responseCache.put(var1, var4);
      }

      var4.lastQueryTime = SystemClock.uptimeMillis();
   }

   private void requestReferenceFromServer(Object var1, String var2, String var3, Object[] var4) {
      int var5;
      TLRPC.TL_channels_getMessages var16;
      TLRPC.TL_messages_getMessages var18;
      if (var1 instanceof MessageObject) {
         MessageObject var7 = (MessageObject)var1;
         var5 = var7.getChannelId();
         if (var5 != 0) {
            var16 = new TLRPC.TL_channels_getMessages();
            var16.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var5);
            var16.id.add(var7.getRealId());
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var16, new _$$Lambda$FileRefController$bJSiLyN_Loo2lNffdEwzSUZ6du0(this, var2, var3));
         } else {
            var18 = new TLRPC.TL_messages_getMessages();
            var18.id.add(var7.getRealId());
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$FileRefController$iH_RpP96708b_YCozHWKOTcxbr4(this, var2, var3));
         }
      } else if (var1 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var20 = (TLRPC.TL_wallPaper)var1;
         TLRPC.TL_account_getWallPaper var6 = new TLRPC.TL_account_getWallPaper();
         TLRPC.TL_inputWallPaper var8 = new TLRPC.TL_inputWallPaper();
         var8.id = var20.id;
         var8.access_hash = var20.access_hash;
         var6.wallpaper = var8;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$FileRefController$l7gUgVrfRVPQOPlPV4_4bUexFYw(this, var2, var3));
      } else if (var1 instanceof TLRPC.WebPage) {
         TLRPC.WebPage var22 = (TLRPC.WebPage)var1;
         TLRPC.TL_messages_getWebPage var9 = new TLRPC.TL_messages_getWebPage();
         var9.url = var22.url;
         var9.hash = 0;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, new _$$Lambda$FileRefController$ZZalHmw878mE45n2TIDtpdup6rk(this, var2, var3));
      } else if (var1 instanceof TLRPC.User) {
         TLRPC.User var24 = (TLRPC.User)var1;
         TLRPC.TL_users_getUsers var10 = new TLRPC.TL_users_getUsers();
         var10.id.add(MessagesController.getInstance(this.currentAccount).getInputUser(var24));
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$FileRefController$PPQViQbtvXgiD0HkQf0oK3_ZBkE(this, var2, var3));
      } else if (var1 instanceof TLRPC.Chat) {
         TLRPC.Chat var11 = (TLRPC.Chat)var1;
         if (var11 instanceof TLRPC.TL_chat) {
            TLRPC.TL_messages_getChats var26 = new TLRPC.TL_messages_getChats();
            var26.id.add(var11.id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var26, new _$$Lambda$FileRefController$7qx1abCFn6GfdxglKDbFuGfloVQ(this, var2, var3));
         } else if (var11 instanceof TLRPC.TL_channel) {
            TLRPC.TL_channels_getChannels var28 = new TLRPC.TL_channels_getChannels();
            var28.id.add(MessagesController.getInputChannel(var11));
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var28, new _$$Lambda$FileRefController$MN7WJPmFdFnXGwTYeLjLskDZA_s(this, var2, var3));
         }
      } else if (var1 instanceof String) {
         String var12 = (String)var1;
         if ("wallpaper".equals(var12)) {
            TLRPC.TL_account_getWallPapers var13 = new TLRPC.TL_account_getWallPapers();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var13, new _$$Lambda$FileRefController$hU__PNtgoV5UVJ0PgXNS0w6t_Ck(this, var2, var3));
         } else if (var12.startsWith("gif")) {
            TLRPC.TL_messages_getSavedGifs var14 = new TLRPC.TL_messages_getSavedGifs();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var14, new _$$Lambda$FileRefController$XmvlggjDShdh6wwrH2NBXtZN860(this, var2, var3));
         } else if ("recent".equals(var12)) {
            TLRPC.TL_messages_getRecentStickers var15 = new TLRPC.TL_messages_getRecentStickers();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var15, new _$$Lambda$FileRefController$1aFkU4DGV4no_EoB8k2dbx7DLBY(this, var2, var3));
         } else if ("fav".equals(var12)) {
            TLRPC.TL_messages_getFavedStickers var17 = new TLRPC.TL_messages_getFavedStickers();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var17, new _$$Lambda$FileRefController$4w92eKYUWTkjdDZrk7Ab6kZgCz4(this, var2, var3));
         } else if (var12.startsWith("avatar_")) {
            var5 = Utilities.parseInt(var12);
            if (var5 > 0) {
               TLRPC.TL_photos_getUserPhotos var19 = new TLRPC.TL_photos_getUserPhotos();
               var19.limit = 80;
               var19.offset = 0;
               var19.max_id = 0L;
               var19.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(var5);
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var19, new _$$Lambda$FileRefController$lc6KWNmCjlTmUjUWy2DU6yide1g(this, var2, var3));
            } else {
               TLRPC.TL_messages_search var21 = new TLRPC.TL_messages_search();
               var21.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
               var21.limit = 80;
               var21.offset_id = 0;
               var21.q = "";
               var21.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var5);
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var21, new _$$Lambda$FileRefController$WYAn7hVey6Q_NHtwvjaWCOxf8sA(this, var2, var3));
            }
         } else if (var12.startsWith("sent_")) {
            String[] var23 = var12.split("_");
            if (var23.length == 3) {
               var5 = Utilities.parseInt(var23[1]);
               if (var5 != 0) {
                  var16 = new TLRPC.TL_channels_getMessages();
                  var16.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var5);
                  var16.id.add(Utilities.parseInt(var23[2]));
                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var16, new _$$Lambda$FileRefController$AUpNheLWJQs7kyRgJ6tIGIQTBNY(this, var2, var3));
               } else {
                  var18 = new TLRPC.TL_messages_getMessages();
                  var18.id.add(Utilities.parseInt(var23[2]));
                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$FileRefController$oHc4Ko0S36uJ174mUmftFNV5oEU(this, var2, var3));
               }
            } else {
               this.sendErrorToObject(var4, 0);
            }
         } else {
            this.sendErrorToObject(var4, 0);
         }
      } else {
         TLRPC.TL_messages_getStickerSet var25;
         TLRPC.StickerSet var29;
         TLRPC.InputStickerSet var31;
         if (var1 instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet var27 = (TLRPC.TL_messages_stickerSet)var1;
            var25 = new TLRPC.TL_messages_getStickerSet();
            var25.stickerset = new TLRPC.TL_inputStickerSetID();
            var31 = var25.stickerset;
            var29 = var27.set;
            var31.id = var29.id;
            var31.access_hash = var29.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var25, new _$$Lambda$FileRefController$exbLp78Kychyc6GzkLXULN8LNGg(this, var2, var3));
         } else if (var1 instanceof TLRPC.StickerSetCovered) {
            TLRPC.StickerSetCovered var30 = (TLRPC.StickerSetCovered)var1;
            var25 = new TLRPC.TL_messages_getStickerSet();
            var25.stickerset = new TLRPC.TL_inputStickerSetID();
            var31 = var25.stickerset;
            var29 = var30.set;
            var31.id = var29.id;
            var31.access_hash = var29.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var25, new _$$Lambda$FileRefController$oQwONz9oH9KbjBHI1GVgQcuvYoU(this, var2, var3));
         } else if (var1 instanceof TLRPC.InputStickerSet) {
            TLRPC.TL_messages_getStickerSet var32 = new TLRPC.TL_messages_getStickerSet();
            var32.stickerset = (TLRPC.InputStickerSet)var1;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var32, new _$$Lambda$FileRefController$taVyElfntghqykv41aff7_1_UM8(this, var2, var3));
         } else {
            this.sendErrorToObject(var4, 0);
         }
      }

   }

   private void sendErrorToObject(Object[] var1, int var2) {
      if (var1[0] instanceof TLRPC.TL_inputSingleMedia) {
         TLRPC.TL_messages_sendMultiMedia var5 = (TLRPC.TL_messages_sendMultiMedia)var1[1];
         Object[] var3 = (Object[])this.multiMediaCache.get(var5);
         if (var3 != null) {
            this.multiMediaCache.remove(var5);
            SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequestMulti(var5, (ArrayList)var3[1], (ArrayList)var3[2], (ArrayList)null, (SendMessagesHelper.DelayedMessage)var3[4]);
         }
      } else if (!(var1[0] instanceof TLRPC.TL_messages_sendMedia) && !(var1[0] instanceof TLRPC.TL_messages_editMessage)) {
         if (var1[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif var6 = (TLRPC.TL_messages_saveGif)var1[0];
         } else if (var1[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker var7 = (TLRPC.TL_messages_saveRecentSticker)var1[0];
         } else if (var1[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker var8 = (TLRPC.TL_messages_faveSticker)var1[0];
         } else if (var1[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.TL_messages_getAttachedStickers var10 = (TLRPC.TL_messages_getAttachedStickers)var1[0];
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, (RequestDelegate)var1[1]);
         } else if (var2 == 0) {
            TLRPC.TL_error var11 = new TLRPC.TL_error();
            var11.text = "not found parent object to request reference";
            var11.code = 400;
            if (var1[1] instanceof FileLoadOperation) {
               FileLoadOperation var4 = (FileLoadOperation)var1[1];
               var4.requestingReference = false;
               var4.processRequestResult((FileLoadOperation.RequestInfo)var1[2], var11);
            }
         } else if (var2 == 1 && var1[1] instanceof FileLoadOperation) {
            FileLoadOperation var9 = (FileLoadOperation)var1[1];
            var9.requestingReference = false;
            var9.onFail(false, 0);
         }
      } else {
         SendMessagesHelper.getInstance(this.currentAccount).performSendMessageRequest((TLObject)var1[0], (MessageObject)var1[1], (String)var1[2], (SendMessagesHelper.DelayedMessage)var1[3], (Boolean)var1[4], (SendMessagesHelper.DelayedMessage)var1[5], (Object)null);
      }

   }

   // $FF: synthetic method
   public void lambda$onRequestComplete$21$FileRefController(TLRPC.User var1) {
      MessagesController.getInstance(this.currentAccount).putUser(var1, false);
   }

   // $FF: synthetic method
   public void lambda$onRequestComplete$22$FileRefController(TLRPC.Chat var1) {
      MessagesController.getInstance(this.currentAccount).putChat(var1, false);
   }

   // $FF: synthetic method
   public void lambda$onRequestComplete$23$FileRefController(TLRPC.Chat var1) {
      MessagesController.getInstance(this.currentAccount).putChat(var1, false);
   }

   // $FF: synthetic method
   public void lambda$onRequestComplete$24$FileRefController(TLRPC.TL_messages_stickerSet var1) {
      DataQuery.getInstance(this.currentAccount).replaceStickerSet(var1);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$0$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$1$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$10$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$11$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$12$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$13$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, false);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$14$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, false);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$15$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$16$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$17$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$2$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$3$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$4$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$5$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$6$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$7$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$8$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$requestReferenceFromServer$9$FileRefController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      this.onRequestComplete(var1, var2, var3, true);
   }

   public void requestReference(Object var1, Object... var2) {
      StringBuilder var3;
      if (BuildVars.LOGS_ENABLED) {
         var3 = new StringBuilder();
         var3.append("start loading request reference for parent = ");
         var3.append(var1);
         var3.append(" args = ");
         var3.append(var2[0]);
         FileLog.d(var3.toString());
      }

      boolean var4 = var2[0] instanceof TLRPC.TL_inputSingleMedia;
      byte var5 = 1;
      TLRPC.TL_inputMediaDocument var6;
      Object var7;
      int var8;
      ArrayList var11;
      TLRPC.InputMedia var13;
      String var15;
      TLRPC.TL_inputMediaPhoto var17;
      if (var4) {
         var13 = ((TLRPC.TL_inputSingleMedia)var2[0]).media;
         if (var13 instanceof TLRPC.TL_inputMediaDocument) {
            var6 = (TLRPC.TL_inputMediaDocument)var13;
            var3 = new StringBuilder();
            var3.append("file_");
            var3.append(var6.id.id);
            var15 = var3.toString();
            var7 = new TLRPC.TL_inputDocumentFileLocation();
            ((TLRPC.InputFileLocation)var7).id = var6.id.id;
         } else {
            if (!(var13 instanceof TLRPC.TL_inputMediaPhoto)) {
               this.sendErrorToObject(var2, 0);
               return;
            }

            var17 = (TLRPC.TL_inputMediaPhoto)var13;
            var3 = new StringBuilder();
            var3.append("photo_");
            var3.append(var17.id.id);
            var15 = var3.toString();
            var7 = new TLRPC.TL_inputPhotoFileLocation();
            ((TLRPC.InputFileLocation)var7).id = var17.id.id;
         }
      } else {
         if (var2[0] instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia var29 = (TLRPC.TL_messages_sendMultiMedia)var2[0];
            var11 = (ArrayList)var1;
            this.multiMediaCache.put(var29, var2);
            var8 = var29.multi_media.size();

            for(int var16 = 0; var16 < var8; ++var16) {
               TLRPC.TL_inputSingleMedia var28 = (TLRPC.TL_inputSingleMedia)var29.multi_media.get(var16);
               Object var12 = var11.get(var16);
               if (var12 != null) {
                  this.requestReference(var12, var28, var29);
               }
            }

            return;
         }

         if (var2[0] instanceof TLRPC.TL_messages_sendMedia) {
            var13 = ((TLRPC.TL_messages_sendMedia)var2[0]).media;
            if (var13 instanceof TLRPC.TL_inputMediaDocument) {
               var6 = (TLRPC.TL_inputMediaDocument)var13;
               var3 = new StringBuilder();
               var3.append("file_");
               var3.append(var6.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputDocumentFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var6.id.id;
            } else {
               if (!(var13 instanceof TLRPC.TL_inputMediaPhoto)) {
                  this.sendErrorToObject(var2, 0);
                  return;
               }

               var17 = (TLRPC.TL_inputMediaPhoto)var13;
               var3 = new StringBuilder();
               var3.append("photo_");
               var3.append(var17.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputPhotoFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var17.id.id;
            }
         } else if (var2[0] instanceof TLRPC.TL_messages_editMessage) {
            var13 = ((TLRPC.TL_messages_editMessage)var2[0]).media;
            if (var13 instanceof TLRPC.TL_inputMediaDocument) {
               var6 = (TLRPC.TL_inputMediaDocument)var13;
               var3 = new StringBuilder();
               var3.append("file_");
               var3.append(var6.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputDocumentFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var6.id.id;
            } else {
               if (!(var13 instanceof TLRPC.TL_inputMediaPhoto)) {
                  this.sendErrorToObject(var2, 0);
                  return;
               }

               var17 = (TLRPC.TL_inputMediaPhoto)var13;
               var3 = new StringBuilder();
               var3.append("photo_");
               var3.append(var17.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputPhotoFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var17.id.id;
            }
         } else if (var2[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif var18 = (TLRPC.TL_messages_saveGif)var2[0];
            var3 = new StringBuilder();
            var3.append("file_");
            var3.append(var18.id.id);
            var15 = var3.toString();
            var7 = new TLRPC.TL_inputDocumentFileLocation();
            ((TLRPC.InputFileLocation)var7).id = var18.id.id;
         } else if (var2[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker var19 = (TLRPC.TL_messages_saveRecentSticker)var2[0];
            var3 = new StringBuilder();
            var3.append("file_");
            var3.append(var19.id.id);
            var15 = var3.toString();
            var7 = new TLRPC.TL_inputDocumentFileLocation();
            ((TLRPC.InputFileLocation)var7).id = var19.id.id;
         } else if (var2[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker var20 = (TLRPC.TL_messages_faveSticker)var2[0];
            var3 = new StringBuilder();
            var3.append("file_");
            var3.append(var20.id.id);
            var15 = var3.toString();
            var7 = new TLRPC.TL_inputDocumentFileLocation();
            ((TLRPC.InputFileLocation)var7).id = var20.id.id;
         } else if (var2[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.InputStickeredMedia var26 = ((TLRPC.TL_messages_getAttachedStickers)var2[0]).media;
            if (var26 instanceof TLRPC.TL_inputStickeredMediaDocument) {
               TLRPC.TL_inputStickeredMediaDocument var21 = (TLRPC.TL_inputStickeredMediaDocument)var26;
               var3 = new StringBuilder();
               var3.append("file_");
               var3.append(var21.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputDocumentFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var21.id.id;
            } else {
               if (!(var26 instanceof TLRPC.TL_inputStickeredMediaPhoto)) {
                  this.sendErrorToObject(var2, 0);
                  return;
               }

               TLRPC.TL_inputStickeredMediaPhoto var23 = (TLRPC.TL_inputStickeredMediaPhoto)var26;
               var3 = new StringBuilder();
               var3.append("photo_");
               var3.append(var23.id.id);
               var15 = var3.toString();
               var7 = new TLRPC.TL_inputPhotoFileLocation();
               ((TLRPC.InputFileLocation)var7).id = var23.id.id;
            }
         } else if (var2[0] instanceof TLRPC.TL_inputFileLocation) {
            var7 = (TLRPC.TL_inputFileLocation)var2[0];
            var3 = new StringBuilder();
            var3.append("loc_");
            var3.append(((TLRPC.InputFileLocation)var7).local_id);
            var3.append("_");
            var3.append(((TLRPC.InputFileLocation)var7).volume_id);
            var15 = var3.toString();
         } else if (var2[0] instanceof TLRPC.TL_inputDocumentFileLocation) {
            var7 = (TLRPC.TL_inputDocumentFileLocation)var2[0];
            var3 = new StringBuilder();
            var3.append("file_");
            var3.append(((TLRPC.InputFileLocation)var7).id);
            var15 = var3.toString();
         } else {
            if (!(var2[0] instanceof TLRPC.TL_inputPhotoFileLocation)) {
               this.sendErrorToObject(var2, 0);
               return;
            }

            var7 = (TLRPC.TL_inputPhotoFileLocation)var2[0];
            var3 = new StringBuilder();
            var3.append("photo_");
            var3.append(((TLRPC.InputFileLocation)var7).id);
            var15 = var3.toString();
         }
      }

      Object var24 = var1;
      if (var1 instanceof MessageObject) {
         MessageObject var9 = (MessageObject)var1;
         var24 = var1;
         if (var9.getRealId() < 0) {
            TLRPC.WebPage var22 = var9.messageOwner.media.webpage;
            var24 = var1;
            if (var22 != null) {
               var24 = var22;
            }
         }
      }

      String var25 = getKeyForParentObject(var24);
      if (var25 == null) {
         this.sendErrorToObject(var2, 0);
      } else {
         FileRefController.Requester var10 = new FileRefController.Requester();
         var10.args = var2;
         var10.location = (TLRPC.InputFileLocation)var7;
         var10.locationKey = var15;
         var11 = (ArrayList)this.locationRequester.get(var15);
         if (var11 == null) {
            var11 = new ArrayList();
            this.locationRequester.put(var15, var11);
         } else {
            var5 = 0;
         }

         var11.add(var10);
         ArrayList var27 = (ArrayList)this.parentRequester.get(var25);
         var8 = var5;
         var11 = var27;
         if (var27 == null) {
            var11 = new ArrayList();
            this.parentRequester.put(var25, var11);
            var8 = var5 + 1;
         }

         var11.add(var10);
         if (var8 == 2) {
            this.cleanupCache();
            FileRefController.CachedResult var14 = this.getCachedResponse(var15);
            if (var14 != null) {
               if (this.onRequestComplete(var15, var25, var14.response, false)) {
                  return;
               }

               this.responseCache.remove(var15);
            } else {
               var14 = this.getCachedResponse(var25);
               if (var14 != null) {
                  if (this.onRequestComplete(var15, var25, var14.response, false)) {
                     return;
                  }

                  this.responseCache.remove(var25);
               }
            }

            this.requestReferenceFromServer(var24, var15, var25, var2);
         }
      }
   }

   private class CachedResult {
      private long firstQueryTime;
      private long lastQueryTime;
      private TLObject response;

      private CachedResult() {
      }

      // $FF: synthetic method
      CachedResult(Object var2) {
         this();
      }
   }

   private class Requester {
      private Object[] args;
      private boolean completed;
      private TLRPC.InputFileLocation location;
      private String locationKey;

      private Requester() {
      }

      // $FF: synthetic method
      Requester(Object var2) {
         this();
      }
   }
}
