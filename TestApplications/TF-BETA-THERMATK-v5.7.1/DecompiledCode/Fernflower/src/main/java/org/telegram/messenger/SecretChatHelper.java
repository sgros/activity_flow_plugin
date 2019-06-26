package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLClassStore;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

public class SecretChatHelper {
   public static final int CURRENT_SECRET_CHAT_LAYER = 73;
   private static volatile SecretChatHelper[] Instance = new SecretChatHelper[3];
   private SparseArray acceptingChats = new SparseArray();
   private int currentAccount;
   public ArrayList delayedEncryptedChatUpdates = new ArrayList();
   private ArrayList pendingEncMessagesToDelete = new ArrayList();
   private SparseArray secretHolesQueue = new SparseArray();
   private ArrayList sendingNotifyLayer = new ArrayList();
   private boolean startingSecretChat = false;

   public SecretChatHelper(int var1) {
      this.currentAccount = var1;
   }

   private void applyPeerLayer(TLRPC.EncryptedChat var1, int var2) {
      int var3 = AndroidUtilities.getPeerLayerVersion(var1.layer);
      if (var2 > var3) {
         if (var1.key_hash.length == 16 && var3 >= 46) {
            try {
               byte[] var4 = Utilities.computeSHA256(var1.auth_key, 0, var1.auth_key.length);
               byte[] var5 = new byte[36];
               System.arraycopy(var1.key_hash, 0, var5, 0, 16);
               System.arraycopy(var4, 0, var5, 16, 20);
               var1.key_hash = var5;
               MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
            } catch (Throwable var6) {
               FileLog.e(var6);
            }
         }

         var1.layer = AndroidUtilities.setPeerLayerVersion(var1.layer, var2);
         MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatLayer(var1);
         if (var3 < 73) {
            this.sendNotifyLayerMessage(var1, (TLRPC.Message)null);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo(this, var1));
      }
   }

   private TLRPC.Message createDeleteMessage(int var1, int var2, int var3, long var4, TLRPC.EncryptedChat var6) {
      TLRPC.TL_messageService var7 = new TLRPC.TL_messageService();
      var7.action = new TLRPC.TL_messageEncryptedAction();
      var7.action.encryptedAction = new TLRPC.TL_decryptedMessageActionDeleteMessages();
      var7.action.encryptedAction.random_ids.add(var4);
      var7.id = var1;
      var7.local_id = var1;
      var7.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
      var7.unread = true;
      var7.out = true;
      var7.flags = 256;
      var7.dialog_id = (long)var6.id << 32;
      var7.to_id = new TLRPC.TL_peerUser();
      var7.send_state = 1;
      var7.seq_in = var3;
      var7.seq_out = var2;
      if (var6.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
         var7.to_id.user_id = var6.admin_id;
      } else {
         var7.to_id.user_id = var6.participant_id;
      }

      var7.date = 0;
      var7.random_id = var4;
      return var7;
   }

   private TLRPC.TL_messageService createServiceSecretMessage(TLRPC.EncryptedChat var1, TLRPC.DecryptedMessageAction var2) {
      TLRPC.TL_messageService var3 = new TLRPC.TL_messageService();
      var3.action = new TLRPC.TL_messageEncryptedAction();
      var3.action.encryptedAction = var2;
      int var4 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
      var3.id = var4;
      var3.local_id = var4;
      var3.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
      var3.unread = true;
      var3.out = true;
      var3.flags = 256;
      var3.dialog_id = (long)var1.id << 32;
      var3.to_id = new TLRPC.TL_peerUser();
      var3.send_state = 1;
      if (var1.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
         var3.to_id.user_id = var1.admin_id;
      } else {
         var3.to_id.user_id = var1.participant_id;
      }

      if (!(var2 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(var2 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
         var3.date = 0;
      } else {
         var3.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
      }

      var3.random_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
      ArrayList var5 = new ArrayList();
      var5.add(var3);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var5, false, true, true, 0);
      return var3;
   }

   private boolean decryptWithMtProtoVersion(NativeByteBuffer var1, byte[] var2, byte[] var3, int var4, boolean var5, boolean var6) {
      if (var4 == 1) {
         var5 = false;
      }

      MessageKeyData var7 = MessageKeyData.generateMessageKeyData(var2, var3, var5, var4);
      Utilities.aesIgeEncryption(var1.buffer, var7.aesKey, var7.aesIv, false, false, 24, var1.limit() - 24);
      int var8 = var1.readInt32(false);
      int var12;
      if (var4 == 2) {
         byte var9;
         if (var5) {
            var9 = 8;
         } else {
            var9 = 0;
         }

         ByteBuffer var10 = var1.buffer;
         if (!Utilities.arraysEquals(var3, 0, Utilities.computeSHA256(var2, var9 + 88, 32, var10, 24, var10.limit()), 8)) {
            if (var6) {
               Utilities.aesIgeEncryption(var1.buffer, var7.aesKey, var7.aesIv, true, false, 24, var1.limit() - 24);
               var1.position(24);
            }

            return false;
         }
      } else {
         label58: {
            int var11 = var8 + 28;
            if (var11 >= var1.buffer.limit() - 15) {
               var12 = var11;
               if (var11 <= var1.buffer.limit()) {
                  break label58;
               }
            }

            var12 = var1.buffer.limit();
         }

         var2 = Utilities.computeSHA1((ByteBuffer)var1.buffer, 24, var12);
         if (!Utilities.arraysEquals(var3, 0, var2, var2.length - 16)) {
            if (var6) {
               Utilities.aesIgeEncryption(var1.buffer, var7.aesKey, var7.aesIv, true, false, 24, var1.limit() - 24);
               var1.position(24);
            }

            return false;
         }
      }

      if (var8 > 0 && var8 <= var1.limit() - 28) {
         var12 = var1.limit() - 28 - var8;
         return (var4 != 2 || var12 >= 12 && var12 <= 1024) && (var4 != 1 || var12 <= 15);
      } else {
         return false;
      }
   }

   public static SecretChatHelper getInstance(int var0) {
      SecretChatHelper var1 = Instance[var0];
      SecretChatHelper var2 = var1;
      if (var1 == null) {
         synchronized(SecretChatHelper.class){}

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
               SecretChatHelper[] var23;
               try {
                  var23 = Instance;
                  var2 = new SecretChatHelper(var0);
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

   public static boolean isSecretInvisibleMessage(TLRPC.Message var0) {
      TLRPC.MessageAction var2 = var0.action;
      boolean var1;
      if (var2 instanceof TLRPC.TL_messageEncryptedAction) {
         TLRPC.DecryptedMessageAction var3 = var2.encryptedAction;
         if (!(var3 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) && !(var3 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   public static boolean isSecretVisibleMessage(TLRPC.Message var0) {
      TLRPC.MessageAction var2 = var0.action;
      boolean var1;
      if (var2 instanceof TLRPC.TL_messageEncryptedAction) {
         TLRPC.DecryptedMessageAction var3 = var2.encryptedAction;
         if (var3 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages || var3 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   // $FF: synthetic method
   static int lambda$checkSecretHoles$15(SecretChatHelper.TL_decryptedMessageHolder var0, SecretChatHelper.TL_decryptedMessageHolder var1) {
      int var2 = var0.layer.out_seq_no;
      int var3 = var1.layer.out_seq_no;
      if (var2 > var3) {
         return 1;
      } else {
         return var2 < var3 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$declineSecretChat$19(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$null$12(TLRPC.Message var0, TLRPC.Message var1) {
      return AndroidUtilities.compare(var0.seq_out, var1.seq_out);
   }

   // $FF: synthetic method
   static void lambda$null$23(Context var0, AlertDialog var1) {
      try {
         if (!((Activity)var0).isFinishing()) {
            var1.dismiss();
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   private void resendMessages(int var1, int var2, TLRPC.EncryptedChat var3) {
      if (var3 != null && var2 - var1 >= 0) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SecretChatHelper$UeLgl_NG4gDOs_Y4sJhukTdyyjM(this, var1, var3, var2));
      }

   }

   private void updateMediaPaths(MessageObject var1, TLRPC.EncryptedFile var2, TLRPC.DecryptedMessage var3, String var4) {
      TLRPC.Message var14 = var1.messageOwner;
      if (var2 != null) {
         TLRPC.MessageMedia var5 = var14.media;
         ArrayList var7;
         TLRPC.DecryptedMessageMedia var12;
         if (var5 instanceof TLRPC.TL_messageMediaPhoto) {
            TLRPC.Photo var15 = var5.photo;
            if (var15 != null) {
               var7 = var15.sizes;
               TLRPC.PhotoSize var8 = (TLRPC.PhotoSize)var7.get(var7.size() - 1);
               StringBuilder var17 = new StringBuilder();
               var17.append(var8.location.volume_id);
               var17.append("_");
               var17.append(var8.location.local_id);
               String var18 = var17.toString();
               var8.location = new TLRPC.TL_fileEncryptedLocation();
               TLRPC.FileLocation var20 = var8.location;
               var12 = var3.media;
               var20.key = var12.key;
               var20.iv = var12.iv;
               var20.dc_id = var2.dc_id;
               var20.volume_id = var2.id;
               var20.secret = var2.access_hash;
               var20.local_id = var2.key_fingerprint;
               StringBuilder var11 = new StringBuilder();
               var11.append(var8.location.volume_id);
               var11.append("_");
               var11.append(var8.location.local_id);
               String var13 = var11.toString();
               File var21 = FileLoader.getDirectory(4);
               var11 = new StringBuilder();
               var11.append(var18);
               var11.append(".jpg");
               (new File(var21, var11.toString())).renameTo(FileLoader.getPathToAttach(var8));
               ImageLoader.getInstance().replaceImageInCache(var18, var13, ImageLocation.getForPhoto(var8, var14.media.photo), true);
               var7 = new ArrayList();
               var7.add(var14);
               MessagesStorage.getInstance(this.currentAccount).putMessages(var7, false, true, false, 0);
               return;
            }
         }

         TLRPC.MessageMedia var6 = var14.media;
         if (var6 instanceof TLRPC.TL_messageMediaDocument) {
            TLRPC.Document var16 = var6.document;
            if (var16 != null) {
               var6.document = new TLRPC.TL_documentEncrypted();
               TLRPC.Document var19 = var14.media.document;
               var19.id = var2.id;
               var19.access_hash = var2.access_hash;
               var19.date = var16.date;
               var19.attributes = var16.attributes;
               var19.mime_type = var16.mime_type;
               var19.size = var2.size;
               var12 = var3.media;
               var19.key = var12.key;
               var19.iv = var12.iv;
               var19.thumbs = var16.thumbs;
               var19.dc_id = var2.dc_id;
               if (var19.thumbs.isEmpty()) {
                  TLRPC.TL_photoSizeEmpty var9 = new TLRPC.TL_photoSizeEmpty();
                  var9.type = "s";
                  var14.media.document.thumbs.add(var9);
               }

               String var10 = var14.attachPath;
               if (var10 != null && var10.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && (new File(var14.attachPath)).renameTo(FileLoader.getPathToAttach(var14.media.document))) {
                  var1.mediaExists = var1.attachPathExists;
                  var1.attachPathExists = false;
                  var14.attachPath = "";
               }

               var7 = new ArrayList();
               var7.add(var14);
               MessagesStorage.getInstance(this.currentAccount).putMessages(var7, false, true, false, 0);
            }
         }
      }

   }

   public void acceptSecretChat(TLRPC.EncryptedChat var1) {
      if (this.acceptingChats.get(var1.id) == null) {
         this.acceptingChats.put(var1.id, var1);
         TLRPC.TL_messages_getDhConfig var2 = new TLRPC.TL_messages_getDhConfig();
         var2.random_length = 256;
         var2.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$SecretChatHelper$13Zq8UfkfLhsSEm3Hl6dWS1zGEc(this, var1));
      }
   }

   public void checkSecretHoles(TLRPC.EncryptedChat var1, ArrayList var2) {
      ArrayList var3 = (ArrayList)this.secretHolesQueue.get(var1.id);
      if (var3 != null) {
         Collections.sort(var3, _$$Lambda$SecretChatHelper$XChl_gDRHQHDfwtxghrPUY1XhL4.INSTANCE);

         boolean var4;
         for(var4 = false; var3.size() > 0; var4 = true) {
            SecretChatHelper.TL_decryptedMessageHolder var5 = (SecretChatHelper.TL_decryptedMessageHolder)var3.get(0);
            int var6 = var5.layer.out_seq_no;
            int var7 = var1.seq_in;
            if (var6 != var7 && var7 != var6 - 2) {
               break;
            }

            this.applyPeerLayer(var1, var5.layer.layer);
            TLRPC.TL_decryptedMessageLayer var8 = var5.layer;
            var1.seq_in = var8.out_seq_no;
            var1.in_seq_no = var8.in_seq_no;
            var3.remove(0);
            if (var5.decryptedWithVersion == 2) {
               var1.mtproto_seq = Math.min(var1.mtproto_seq, var1.seq_in);
            }

            TLRPC.Message var9 = this.processDecryptedObject(var1, var5.file, var5.date, var5.layer.message, var5.new_key_used);
            if (var9 != null) {
               var2.add(var9);
            }
         }

         if (var3.isEmpty()) {
            this.secretHolesQueue.remove(var1.id);
         }

         if (var4) {
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(var1, true);
         }

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

   public void declineSecretChat(int var1) {
      TLRPC.TL_messages_discardEncryption var2 = new TLRPC.TL_messages_discardEncryption();
      var2.chat_id = var1;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, _$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M.INSTANCE);
   }

   protected ArrayList decryptMessage(TLRPC.EncryptedMessage var1) {
      TLRPC.EncryptedChat var2 = MessagesController.getInstance(this.currentAccount).getEncryptedChatDB(var1.chat_id, true);
      if (var2 != null && !(var2 instanceof TLRPC.TL_encryptedChatDiscarded)) {
         Exception var10000;
         label316: {
            NativeByteBuffer var3;
            long var4;
            byte[] var6;
            boolean var7;
            boolean var10001;
            label310: {
               label309: {
                  label317: {
                     try {
                        var3 = new NativeByteBuffer(var1.bytes.length);
                        var3.writeBytes(var1.bytes);
                        var3.position(0);
                        var4 = var3.readInt64(false);
                        if (var2.key_fingerprint == var4) {
                           var6 = var2.auth_key;
                           break label317;
                        }
                     } catch (Exception var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label316;
                     }

                     try {
                        if (var2.future_key_fingerprint != 0L && var2.future_key_fingerprint == var4) {
                           var6 = var2.future_auth_key;
                           break label309;
                        }
                     } catch (Exception var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label316;
                     }

                     var6 = null;
                  }

                  var7 = false;
                  break label310;
               }

               var7 = true;
            }

            byte var8;
            label294: {
               label293: {
                  try {
                     if (AndroidUtilities.getPeerLayerVersion(var2.layer) >= 73) {
                        break label293;
                     }
                  } catch (Exception var39) {
                     var10000 = var39;
                     var10001 = false;
                     break label316;
                  }

                  var8 = 1;
                  break label294;
               }

               var8 = 2;
            }

            if (var6 == null) {
               try {
                  var3.reuse();
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e(String.format("fingerprint mismatch %x", var4));
                  }

                  return null;
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
               }
            } else {
               label327: {
                  byte[] var9;
                  boolean var10;
                  label286: {
                     label285: {
                        try {
                           var9 = var3.readData(16, false);
                           if (var2.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                              break label285;
                           }
                        } catch (Exception var38) {
                           var10000 = var38;
                           var10001 = false;
                           break label327;
                        }

                        var10 = false;
                        break label286;
                     }

                     var10 = true;
                  }

                  boolean var11;
                  label279: {
                     label278: {
                        if (var8 == 2) {
                           try {
                              if (var2.mtproto_seq != 0) {
                                 break label278;
                              }
                           } catch (Exception var37) {
                              var10000 = var37;
                              var10001 = false;
                              break label327;
                           }
                        }

                        var11 = true;
                        break label279;
                     }

                     var11 = false;
                  }

                  label318: {
                     try {
                        if (this.decryptWithMtProtoVersion(var3, var6, var9, var8, var10, var11)) {
                           break label318;
                        }
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break label327;
                     }

                     if (var8 == 2) {
                        label261: {
                           if (var11) {
                              try {
                                 if (this.decryptWithMtProtoVersion(var3, var6, var9, 1, var10, false)) {
                                    break label261;
                                 }
                              } catch (Exception var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label327;
                              }
                           }

                           return null;
                        }

                        var8 = 1;
                     } else {
                        try {
                           if (!this.decryptWithMtProtoVersion(var3, var6, var9, 2, var10, var11)) {
                              return null;
                           }
                        } catch (Exception var36) {
                           var10000 = var36;
                           var10001 = false;
                           break label327;
                        }

                        var8 = 2;
                     }
                  }

                  TLObject var49;
                  try {
                     var49 = TLClassStore.Instance().TLdeserialize(var3, var3.readInt32(false), false);
                     var3.reuse();
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label327;
                  }

                  if (!var7) {
                     try {
                        if (AndroidUtilities.getPeerLayerVersion(var2.layer) >= 20) {
                           var2.key_use_count_in = (short)((short)(var2.key_use_count_in + 1));
                        }
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label327;
                     }
                  }

                  TLRPC.TL_decryptedMessageLayer var45;
                  ArrayList var51;
                  label319: {
                     Object var47;
                     label320: {
                        label321: {
                           label333: {
                              try {
                                 label322: {
                                    if (!(var49 instanceof TLRPC.TL_decryptedMessageLayer)) {
                                       break label333;
                                    }

                                    var45 = (TLRPC.TL_decryptedMessageLayer)var49;
                                    if (var2.seq_in == 0 && var2.seq_out == 0) {
                                       if (var2.admin_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                          break label322;
                                       }

                                       var2.seq_out = 1;
                                       var2.seq_in = -2;
                                    }
                                    break label321;
                                 }
                              } catch (Exception var33) {
                                 var10000 = var33;
                                 var10001 = false;
                                 break label327;
                              }

                              try {
                                 var2.seq_in = -1;
                                 break label321;
                              } catch (Exception var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break label327;
                              }
                           }

                           label323: {
                              try {
                                 if (!(var49 instanceof TLRPC.TL_decryptedMessageService)) {
                                    break label323;
                                 }
                              } catch (Exception var32) {
                                 var10000 = var32;
                                 var10001 = false;
                                 break label327;
                              }

                              var47 = var49;

                              try {
                                 if (((TLRPC.TL_decryptedMessageService)var49).action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                                    break label320;
                                 }
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label327;
                              }
                           }

                           try {
                              return null;
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label327;
                           }
                        }

                        try {
                           if (var45.random_bytes.length < 15) {
                              if (BuildVars.LOGS_ENABLED) {
                                 FileLog.e("got random bytes less than needed");
                              }

                              return null;
                           }
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label327;
                        }

                        try {
                           if (BuildVars.LOGS_ENABLED) {
                              StringBuilder var46 = new StringBuilder();
                              var46.append("current chat in_seq = ");
                              var46.append(var2.seq_in);
                              var46.append(" out_seq = ");
                              var46.append(var2.seq_out);
                              FileLog.d(var46.toString());
                              var46 = new StringBuilder();
                              var46.append("got message with in_seq = ");
                              var46.append(var45.in_seq_no);
                              var46.append(" out_seq = ");
                              var46.append(var45.out_seq_no);
                              FileLog.d(var46.toString());
                           }
                        } catch (Exception var31) {
                           var10000 = var31;
                           var10001 = false;
                           break label327;
                        }

                        try {
                           if (var45.out_seq_no <= var2.seq_in) {
                              return null;
                           }
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label327;
                        }

                        if (var8 == 1) {
                           try {
                              if (var2.mtproto_seq != 0 && var45.out_seq_no >= var2.mtproto_seq) {
                                 return null;
                              }
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label327;
                           }
                        }

                        try {
                           if (var2.seq_in != var45.out_seq_no - 2) {
                              if (BuildVars.LOGS_ENABLED) {
                                 FileLog.e("got hole");
                              }
                              break label319;
                           }
                        } catch (Exception var30) {
                           var10000 = var30;
                           var10001 = false;
                           break label327;
                        }

                        if (var8 == 2) {
                           try {
                              var2.mtproto_seq = Math.min(var2.mtproto_seq, var2.seq_in);
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label327;
                           }
                        }

                        try {
                           this.applyPeerLayer(var2, var45.layer);
                           var2.seq_in = var45.out_seq_no;
                           var2.in_seq_no = var45.in_seq_no;
                           MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(var2, true);
                           var47 = var45.message;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label327;
                        }
                     }

                     TLRPC.Message var43;
                     try {
                        var51 = new ArrayList();
                        var43 = this.processDecryptedObject(var2, var1.file, var1.date, (TLObject)var47, var7);
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label327;
                     }

                     if (var43 != null) {
                        try {
                           var51.add(var43);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label327;
                        }
                     }

                     try {
                        this.checkSecretHoles(var2, var51);
                        return var51;
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label327;
                     }
                  }

                  try {
                     var51 = (ArrayList)this.secretHolesQueue.get(var2.id);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label327;
                  }

                  ArrayList var48 = var51;
                  if (var51 == null) {
                     try {
                        var48 = new ArrayList();
                        this.secretHolesQueue.put(var2.id, var48);
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label327;
                     }
                  }

                  try {
                     if (var48.size() >= 4) {
                        this.secretHolesQueue.remove(var2.id);
                        TLRPC.TL_encryptedChatDiscarded var42 = new TLRPC.TL_encryptedChatDiscarded();
                        var42.id = var2.id;
                        var42.user_id = var2.user_id;
                        var42.auth_key = var2.auth_key;
                        var42.key_create_date = var2.key_create_date;
                        var42.key_use_count_in = (short)var2.key_use_count_in;
                        var42.key_use_count_out = (short)var2.key_use_count_out;
                        var42.seq_in = var2.seq_in;
                        var42.seq_out = var2.seq_out;
                        _$$Lambda$SecretChatHelper$y__QKcAzTtKUDu_WddTe8KbVDxY var50 = new _$$Lambda$SecretChatHelper$y__QKcAzTtKUDu_WddTe8KbVDxY(this, var42);
                        AndroidUtilities.runOnUIThread(var50);
                        this.declineSecretChat(var2.id);
                        return null;
                     }
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label327;
                  }

                  try {
                     SecretChatHelper.TL_decryptedMessageHolder var52 = new SecretChatHelper.TL_decryptedMessageHolder();
                     var52.layer = var45;
                     var52.file = var1.file;
                     var52.date = var1.date;
                     var52.new_key_used = var7;
                     var52.decryptedWithVersion = var8;
                     var48.add(var52);
                     return null;
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                  }
               }
            }
         }

         Exception var44 = var10000;
         FileLog.e((Throwable)var44);
      }

      return null;
   }

   // $FF: synthetic method
   public void lambda$acceptSecretChat$22$SecretChatHelper(TLRPC.EncryptedChat var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         TLRPC.messages_DhConfig var10 = (TLRPC.messages_DhConfig)var2;
         if (var2 instanceof TLRPC.TL_messages_dhConfig) {
            if (!Utilities.isGoodPrime(var10.p, var10.g)) {
               this.acceptingChats.remove(var1.id);
               this.declineSecretChat(var1.id);
               return;
            }

            MessagesStorage.getInstance(this.currentAccount).setSecretPBytes(var10.p);
            MessagesStorage.getInstance(this.currentAccount).setSecretG(var10.g);
            MessagesStorage.getInstance(this.currentAccount).setLastSecretVersion(var10.version);
            MessagesStorage.getInstance(this.currentAccount).saveSecretParams(MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion(), MessagesStorage.getInstance(this.currentAccount).getSecretG(), MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
         }

         byte[] var4 = new byte[256];

         int var5;
         for(var5 = 0; var5 < 256; ++var5) {
            var4[var5] = (byte)((byte)((byte)((int)(Utilities.random.nextDouble() * 256.0D)) ^ var10.random[var5]));
         }

         var1.a_or_b = var4;
         var1.seq_in = -1;
         var1.seq_out = 0;
         BigInteger var6 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
         BigInteger var8 = BigInteger.valueOf((long)MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, var4), var6);
         BigInteger var7 = new BigInteger(1, var1.g_a);
         if (!Utilities.isGoodGaAndGb(var7, var6)) {
            this.acceptingChats.remove(var1.id);
            this.declineSecretChat(var1.id);
            return;
         }

         byte[] var9 = var8.toByteArray();
         byte[] var11 = var9;
         if (var9.length > 256) {
            var11 = new byte[256];
            System.arraycopy(var9, 1, var11, 0, 256);
         }

         var4 = var7.modPow(new BigInteger(1, var4), var6).toByteArray();
         byte[] var14;
         if (var4.length > 256) {
            var9 = new byte[256];
            System.arraycopy(var4, var4.length - 256, var9, 0, 256);
         } else if (var4.length < 256) {
            var14 = new byte[256];
            System.arraycopy(var4, 0, var14, 256 - var4.length, var4.length);
            var5 = 0;

            while(true) {
               var9 = var14;
               if (var5 >= 256 - var4.length) {
                  break;
               }

               var14[var5] = (byte)0;
               ++var5;
            }
         } else {
            var9 = var4;
         }

         var14 = Utilities.computeSHA1(var9);
         var4 = new byte[8];
         System.arraycopy(var14, var14.length - 8, var4, 0, 8);
         var1.auth_key = var9;
         var1.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         TLRPC.TL_messages_acceptEncryption var12 = new TLRPC.TL_messages_acceptEncryption();
         var12.g_b = var11;
         var12.peer = new TLRPC.TL_inputEncryptedChat();
         TLRPC.TL_inputEncryptedChat var13 = var12.peer;
         var13.chat_id = var1.id;
         var13.access_hash = var1.access_hash;
         var12.key_fingerprint = Utilities.bytesToLong(var4);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$SecretChatHelper$48_HP_7TQqG7f2oWszU7R3aCenM(this, var1));
      } else {
         this.acceptingChats.remove(var1.id);
      }

   }

   // $FF: synthetic method
   public void lambda$applyPeerLayer$8$SecretChatHelper(TLRPC.EncryptedChat var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var1);
   }

   // $FF: synthetic method
   public void lambda$decryptMessage$16$SecretChatHelper(TLRPC.TL_encryptedChatDiscarded var1) {
      MessagesController.getInstance(this.currentAccount).putEncryptedChat(var1, false);
      MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var1);
   }

   // $FF: synthetic method
   public void lambda$null$10$SecretChatHelper(long var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$zouLqc6zy27lFYX8g6pcFXGkhsk(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$13$SecretChatHelper(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         TLRPC.Message var3 = (TLRPC.Message)var1.get(var2);
         MessageObject var4 = new MessageObject(this.currentAccount, var3, false);
         var4.resendAsIs = true;
         SendMessagesHelper.getInstance(this.currentAccount).retrySendMessage(var4, true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$20$SecretChatHelper(TLRPC.EncryptedChat var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var1);
      this.sendNotifyLayerMessage(var1, (TLRPC.Message)null);
   }

   // $FF: synthetic method
   public void lambda$null$21$SecretChatHelper(TLRPC.EncryptedChat var1, TLObject var2, TLRPC.TL_error var3) {
      this.acceptingChats.remove(var1.id);
      if (var3 == null) {
         TLRPC.EncryptedChat var4 = (TLRPC.EncryptedChat)var2;
         var4.auth_key = var1.auth_key;
         var4.user_id = var1.user_id;
         var4.seq_in = var1.seq_in;
         var4.seq_out = var1.seq_out;
         var4.key_create_date = var1.key_create_date;
         var4.key_use_count_in = (short)var1.key_use_count_in;
         var4.key_use_count_out = (short)var1.key_use_count_out;
         MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var4);
         MessagesController.getInstance(this.currentAccount).putEncryptedChat(var4, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$_jYoIAhmqiUWDV4t_Jnx1LLNrA8(this, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$null$24$SecretChatHelper() {
      if (!this.delayedEncryptedChatUpdates.isEmpty()) {
         MessagesController.getInstance(this.currentAccount).processUpdateArray(this.delayedEncryptedChatUpdates, (ArrayList)null, (ArrayList)null, false);
         this.delayedEncryptedChatUpdates.clear();
      }

   }

   // $FF: synthetic method
   public void lambda$null$25$SecretChatHelper(Context var1, AlertDialog var2, TLObject var3, byte[] var4, TLRPC.User var5) {
      this.startingSecretChat = false;
      if (!((Activity)var1).isFinishing()) {
         try {
            var2.dismiss();
         } catch (Exception var6) {
            FileLog.e((Throwable)var6);
         }
      }

      TLRPC.EncryptedChat var7 = (TLRPC.EncryptedChat)var3;
      var7.user_id = var7.participant_id;
      var7.seq_in = -2;
      var7.seq_out = 1;
      var7.a_or_b = var4;
      MessagesController.getInstance(this.currentAccount).putEncryptedChat(var7, false);
      TLRPC.TL_dialog var8 = new TLRPC.TL_dialog();
      var8.id = DialogObject.makeSecretDialogId(var7.id);
      var8.unread_count = 0;
      var8.top_message = 0;
      var8.last_message_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
      MessagesController.getInstance(this.currentAccount).dialogs_dict.put(var8.id, var8);
      MessagesController.getInstance(this.currentAccount).allDialogs.add(var8);
      MessagesController.getInstance(this.currentAccount).sortDialogs((SparseArray)null);
      MessagesStorage.getInstance(this.currentAccount).putEncryptedChat(var7, var5, var8);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatCreated, var7);
      Utilities.stageQueue.postRunnable(new _$$Lambda$SecretChatHelper$PyQ6La6w_4bJYWv3ojsQqv921CM(this));
   }

   // $FF: synthetic method
   public void lambda$null$26$SecretChatHelper(Context var1, AlertDialog var2) {
      if (!((Activity)var1).isFinishing()) {
         this.startingSecretChat = false;

         try {
            var2.dismiss();
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         AlertDialog.Builder var4 = new AlertDialog.Builder(var1);
         var4.setTitle(LocaleController.getString("AppName", 2131558635));
         var4.setMessage(LocaleController.getString("CreateEncryptedChatError", 2131559167));
         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var4.show().setCanceledOnTouchOutside(true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$27$SecretChatHelper(Context var1, AlertDialog var2, byte[] var3, TLRPC.User var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$0tV_MJuVJAhZ10ST8ytcL1B6vB4(this, var1, var2, var5, var3, var4));
      } else {
         this.delayedEncryptedChatUpdates.clear();
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$pNz_sskaVL96CTdZukhE4ke04i8(this, var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$null$28$SecretChatHelper(Context var1, AlertDialog var2) {
      this.startingSecretChat = false;
      if (!((Activity)var1).isFinishing()) {
         try {
            var2.dismiss();
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$SecretChatHelper(TLRPC.Message var1, int var2, String var3) {
      var1.send_state = 0;
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var1.id, var1.id, var1, var1.dialog_id, 0L, var2);
      SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(var1.id);
      if (MessageObject.isVideoMessage(var1) || MessageObject.isNewGifMessage(var1) || MessageObject.isRoundVideoMessage(var1)) {
         SendMessagesHelper.getInstance(this.currentAccount).stopVideoService(var3);
      }

      SendMessagesHelper.getInstance(this.currentAccount).removeFromSendingMessages(var1.id);
   }

   // $FF: synthetic method
   public void lambda$null$4$SecretChatHelper(TLRPC.Message var1, TLRPC.messages_SentEncryptedMessage var2, int var3, String var4) {
      if (isSecretInvisibleMessage(var1)) {
         var2.date = 0;
      }

      MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(var1.random_id, var1.id, var1.id, var2.date, false, 0);
      AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$6RH9f8axwCUH9UKWqF8EeUaUF1g(this, var1, var3, var4));
   }

   // $FF: synthetic method
   public void lambda$null$5$SecretChatHelper(TLRPC.Message var1) {
      var1.send_state = 2;
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var1.id);
      SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(var1.id);
      if (MessageObject.isVideoMessage(var1) || MessageObject.isNewGifMessage(var1) || MessageObject.isRoundVideoMessage(var1)) {
         SendMessagesHelper.getInstance(this.currentAccount).stopVideoService(var1.attachPath);
      }

      SendMessagesHelper.getInstance(this.currentAccount).removeFromSendingMessages(var1.id);
   }

   // $FF: synthetic method
   public void lambda$null$6$SecretChatHelper(TLRPC.DecryptedMessage var1, TLRPC.EncryptedChat var2, TLRPC.Message var3, MessageObject var4, String var5, TLObject var6, TLRPC.TL_error var7) {
      if (var7 == null && var1.action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
         TLRPC.EncryptedChat var8 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var2.id);
         TLRPC.EncryptedChat var9 = var8;
         if (var8 == null) {
            var9 = var2;
         }

         if (var9.key_hash == null) {
            var9.key_hash = AndroidUtilities.calcAuthKeyHash(var9.auth_key);
         }

         if (AndroidUtilities.getPeerLayerVersion(var9.layer) >= 46 && var9.key_hash.length == 16) {
            try {
               byte[] var16 = Utilities.computeSHA256(var2.auth_key, 0, var2.auth_key.length);
               byte[] var10 = new byte[36];
               System.arraycopy(var2.key_hash, 0, var10, 0, 16);
               System.arraycopy(var16, 0, var10, 16, 20);
               var9.key_hash = var10;
               MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var9);
            } catch (Throwable var12) {
               FileLog.e(var12);
            }
         }

         this.sendingNotifyLayer.remove(var9.id);
         var9.layer = AndroidUtilities.setMyLayerVersion(var9.layer, 73);
         MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatLayer(var9);
      }

      if (var3 != null) {
         if (var7 == null) {
            String var13 = var3.attachPath;
            TLRPC.messages_SentEncryptedMessage var15 = (TLRPC.messages_SentEncryptedMessage)var6;
            if (isSecretVisibleMessage(var3)) {
               var3.date = var15.date;
            }

            int var11;
            label43: {
               if (var4 != null) {
                  TLRPC.EncryptedFile var14 = var15.file;
                  if (var14 instanceof TLRPC.TL_encryptedFile) {
                     this.updateMediaPaths(var4, var14, var1, var5);
                     var11 = var4.getMediaExistanceFlags();
                     break label43;
                  }
               }

               var11 = 0;
            }

            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SecretChatHelper$kXyPCeVLRJzYiyabGiQYTza0QCE(this, var3, var15, var11, var13));
         } else {
            MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(var3);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$KXmAZjrV_o2jJvsCy8wRyFby6VI(this, var3));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$SecretChatHelper(long var1) {
      NotificationsController.getInstance(this.currentAccount).processReadMessages((SparseLongArray)null, var1, 0, Integer.MAX_VALUE, false);
      LongSparseArray var3 = new LongSparseArray(1);
      var3.put(var1, 0);
      NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var3);
   }

   // $FF: synthetic method
   public void lambda$performSendEncryptedRequest$7$SecretChatHelper(TLRPC.EncryptedChat var1, TLRPC.DecryptedMessage var2, TLRPC.Message var3, TLRPC.InputEncryptedFile var4, MessageObject var5, String var6) {
      Exception var10000;
      label245: {
         TLRPC.TL_decryptedMessageLayer var7;
         int var8;
         boolean var10001;
         try {
            var7 = new TLRPC.TL_decryptedMessageLayer();
            var7.layer = Math.min(Math.max(46, AndroidUtilities.getMyLayerVersion(var1.layer)), Math.max(46, AndroidUtilities.getPeerLayerVersion(var1.layer)));
            var7.message = var2;
            var7.random_bytes = new byte[15];
            Utilities.random.nextBytes(var7.random_bytes);
            var8 = AndroidUtilities.getPeerLayerVersion(var1.layer);
         } catch (Exception var41) {
            var10000 = var41;
            var10001 = false;
            break label245;
         }

         boolean var9 = true;
         byte var10;
         if (var8 >= 73) {
            var10 = 2;
         } else {
            var10 = 1;
         }

         label253: {
            label235:
            try {
               if (var1.seq_in == 0 && var1.seq_out == 0) {
                  if (var1.admin_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                     break label235;
                  }

                  var1.seq_out = 1;
                  var1.seq_in = -2;
               }
               break label253;
            } catch (Exception var40) {
               var10000 = var40;
               var10001 = false;
               break label245;
            }

            try {
               var1.seq_in = -1;
            } catch (Exception var39) {
               var10000 = var39;
               var10001 = false;
               break label245;
            }
         }

         label247: {
            label222: {
               label254: {
                  try {
                     if (var3.seq_in != 0 || var3.seq_out != 0) {
                        break label254;
                     }

                     if (var1.seq_in > 0) {
                        var8 = var1.seq_in;
                        break label222;
                     }
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label245;
                  }

                  try {
                     var8 = var1.seq_in + 2;
                     break label222;
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label245;
                  }
               }

               try {
                  var7.in_seq_no = var3.seq_in;
                  var7.out_seq_no = var3.seq_out;
                  break label247;
               } catch (Exception var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label245;
               }
            }

            label249: {
               try {
                  var7.in_seq_no = var8;
                  var7.out_seq_no = var1.seq_out;
                  var1.seq_out += 2;
                  if (AndroidUtilities.getPeerLayerVersion(var1.layer) < 20) {
                     break label249;
                  }

                  if (var1.key_create_date == 0) {
                     var1.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                  }
               } catch (Exception var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label245;
               }

               try {
                  var1.key_use_count_out = (short)((short)(var1.key_use_count_out + 1));
                  if (var1.key_use_count_out < 100 && var1.key_create_date >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - 604800) {
                     break label249;
                  }
               } catch (Exception var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label245;
               }

               try {
                  if (var1.exchange_id == 0L && var1.future_key_fingerprint == 0L) {
                     this.requestNewSecretChatKey(var1);
                  }
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label245;
               }
            }

            try {
               MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatSeq(var1, false);
            } catch (Exception var33) {
               var10000 = var33;
               var10001 = false;
               break label245;
            }

            if (var3 != null) {
               try {
                  var3.seq_in = var7.in_seq_no;
                  var3.seq_out = var7.out_seq_no;
                  MessagesStorage.getInstance(this.currentAccount).setMessageSeq(var3.id, var3.seq_in, var3.seq_out);
               } catch (Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label245;
               }
            }
         }

         try {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var11 = new StringBuilder();
               var11.append(var2);
               var11.append(" send message with in_seq = ");
               var11.append(var7.in_seq_no);
               var11.append(" out_seq = ");
               var11.append(var7.out_seq_no);
               FileLog.d(var11.toString());
            }
         } catch (Exception var30) {
            var10000 = var30;
            var10001 = false;
            break label245;
         }

         NativeByteBuffer var12;
         int var13;
         try {
            var8 = var7.getObjectSize();
            var12 = new NativeByteBuffer(var8 + 4);
            var12.writeInt32(var8);
            var7.serializeToStream(var12);
            var13 = var12.length();
         } catch (Exception var29) {
            var10000 = var29;
            var10001 = false;
            break label245;
         }

         if (var13 % 16 != 0) {
            var8 = 16 - var13 % 16;
         } else {
            var8 = 0;
         }

         int var14 = var8;
         if (var10 == 2) {
            try {
               var14 = var8 + (Utilities.random.nextInt(3) + 2) * 16;
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label245;
            }
         }

         NativeByteBuffer var44;
         try {
            var44 = new NativeByteBuffer(var13 + var14);
            var12.position(0);
            var44.writeBytes(var12);
         } catch (Exception var27) {
            var10000 = var27;
            var10001 = false;
            break label245;
         }

         byte[] var49;
         if (var14 != 0) {
            try {
               var49 = new byte[var14];
               Utilities.random.nextBytes(var49);
               var44.writeBytes(var49);
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label245;
            }
         }

         try {
            var49 = new byte[16];
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break label245;
         }

         label165: {
            if (var10 == 2) {
               try {
                  if (var1.admin_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                     break label165;
                  }
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label245;
               }
            }

            var9 = false;
         }

         byte[] var15;
         if (var10 == 2) {
            try {
               var15 = var1.auth_key;
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label245;
            }

            byte var48;
            if (var9) {
               var48 = 8;
            } else {
               var48 = 0;
            }

            try {
               System.arraycopy(Utilities.computeSHA256(var15, var48 + 88, 32, var44.buffer, 0, var44.buffer.limit()), 8, var49, 0, 16);
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label245;
            }
         } else {
            try {
               var15 = Utilities.computeSHA1(var12.buffer);
               System.arraycopy(var15, var15.length - 16, var49, 0, 16);
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label245;
            }
         }

         try {
            var12.reuse();
            MessageKeyData var51 = MessageKeyData.generateMessageKeyData(var1.auth_key, var49, var9, var10);
            Utilities.aesIgeEncryption(var44.buffer, var51.aesKey, var51.aesIv, true, false, 0, var44.limit());
            var12 = new NativeByteBuffer(var49.length + 8 + var44.length());
            var44.position(0);
            var12.writeInt64(var1.key_fingerprint);
            var12.writeBytes(var49);
            var12.writeBytes(var44);
            var44.reuse();
            var12.position(0);
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label245;
         }

         Object var43;
         if (var4 == null) {
            label255: {
               TLRPC.TL_inputEncryptedChat var45;
               try {
                  if (var2 instanceof TLRPC.TL_decryptedMessageService) {
                     var43 = new TLRPC.TL_messages_sendEncryptedService();
                     ((TLRPC.TL_messages_sendEncryptedService)var43).data = var12;
                     ((TLRPC.TL_messages_sendEncryptedService)var43).random_id = var2.random_id;
                     var45 = new TLRPC.TL_inputEncryptedChat();
                     ((TLRPC.TL_messages_sendEncryptedService)var43).peer = var45;
                     ((TLRPC.TL_messages_sendEncryptedService)var43).peer.chat_id = var1.id;
                     ((TLRPC.TL_messages_sendEncryptedService)var43).peer.access_hash = var1.access_hash;
                     break label255;
                  }
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label245;
               }

               try {
                  var43 = new TLRPC.TL_messages_sendEncrypted();
                  ((TLRPC.TL_messages_sendEncrypted)var43).data = var12;
                  ((TLRPC.TL_messages_sendEncrypted)var43).random_id = var2.random_id;
                  var45 = new TLRPC.TL_inputEncryptedChat();
                  ((TLRPC.TL_messages_sendEncrypted)var43).peer = var45;
                  ((TLRPC.TL_messages_sendEncrypted)var43).peer.chat_id = var1.id;
                  ((TLRPC.TL_messages_sendEncrypted)var43).peer.access_hash = var1.access_hash;
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label245;
               }
            }
         } else {
            TLRPC.TL_messages_sendEncryptedFile var46;
            try {
               var46 = new TLRPC.TL_messages_sendEncryptedFile();
               var46.data = var12;
               var46.random_id = var2.random_id;
               TLRPC.TL_inputEncryptedChat var50 = new TLRPC.TL_inputEncryptedChat();
               var46.peer = var50;
               var46.peer.chat_id = var1.id;
               var46.peer.access_hash = var1.access_hash;
               var46.file = var4;
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label245;
            }

            var43 = var46;
         }

         try {
            ConnectionsManager var47 = ConnectionsManager.getInstance(this.currentAccount);
            _$$Lambda$SecretChatHelper$NeIJyTvVk2g1G3EFM6ENqFtjkw0 var52 = new _$$Lambda$SecretChatHelper$NeIJyTvVk2g1G3EFM6ENqFtjkw0(this, var2, var1, var3, var5, var6);
            var47.sendRequest((TLObject)var43, var52, 64);
            return;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
         }
      }

      Exception var42 = var10000;
      FileLog.e((Throwable)var42);
   }

   // $FF: synthetic method
   public void lambda$processAcceptedSecretChat$17$SecretChatHelper(TLRPC.EncryptedChat var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var1);
      this.sendNotifyLayerMessage(var1, (TLRPC.Message)null);
   }

   // $FF: synthetic method
   public void lambda$processAcceptedSecretChat$18$SecretChatHelper(TLRPC.TL_encryptedChatDiscarded var1) {
      MessagesController.getInstance(this.currentAccount).putEncryptedChat(var1, false);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var1);
   }

   // $FF: synthetic method
   public void lambda$processDecryptedObject$11$SecretChatHelper(long var1) {
      TLRPC.Dialog var3 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(var1);
      if (var3 != null) {
         var3.unread_count = 0;
         MessagesController.getInstance(this.currentAccount).dialogMessage.remove(var3.id);
      }

      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SecretChatHelper$kHVODiP7b77JSefUc0MrQJDvu_o(this, var1));
      MessagesStorage.getInstance(this.currentAccount).deleteDialog(var1, 1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, var1, false);
   }

   // $FF: synthetic method
   public void lambda$processPendingEncMessages$0$SecretChatHelper(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         MessageObject var3 = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessagesByRandomIds.get((Long)var1.get(var2));
         if (var3 != null) {
            var3.deleted = true;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$processUpdateEncryption$1$SecretChatHelper(TLRPC.Dialog var1) {
      MessagesController.getInstance(this.currentAccount).dialogs_dict.put(var1.id, var1);
      MessagesController.getInstance(this.currentAccount).allDialogs.add(var1);
      MessagesController.getInstance(this.currentAccount).sortDialogs((SparseArray)null);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$processUpdateEncryption$2$SecretChatHelper(TLRPC.EncryptedChat var1, TLRPC.EncryptedChat var2) {
      if (var1 != null) {
         MessagesController.getInstance(this.currentAccount).putEncryptedChat(var2, false);
      }

      MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.encryptedChatUpdated, var2);
   }

   // $FF: synthetic method
   public void lambda$resendMessages$14$SecretChatHelper(int var1, TLRPC.EncryptedChat var2, int var3) {
      Exception var10000;
      label136: {
         boolean var10001;
         label131: {
            try {
               if (var2.admin_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  break label131;
               }
            } catch (Exception var33) {
               var10000 = var33;
               var10001 = false;
               break label136;
            }

            if (var1 % 2 == 0) {
               ++var1;
            }
         }

         boolean var5;
         try {
            SQLiteCursor var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT uid FROM requested_holes WHERE uid = %d AND ((seq_out_start >= %d AND %d <= seq_out_end) OR (seq_out_start >= %d AND %d <= seq_out_end))", var2.id, var1, var1, var3, var3));
            var5 = var4.next();
            var4.dispose();
         } catch (Exception var32) {
            var10000 = var32;
            var10001 = false;
            break label136;
         }

         if (var5) {
            return;
         }

         long var6;
         SparseArray var8;
         ArrayList var35;
         try {
            var6 = (long)var2.id << 32;
            var8 = new SparseArray();
            var35 = new ArrayList();
         } catch (Exception var31) {
            var10000 = var31;
            var10001 = false;
            break label136;
         }

         int var9;
         for(var9 = var1; var9 < var3; var9 += 2) {
            try {
               var8.put(var9, (Object)null);
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label136;
            }
         }

         SQLiteCursor var10;
         try {
            var10 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, r.random_id, s.seq_in, s.seq_out, m.ttl, s.mid FROM messages_seq as s LEFT JOIN randoms as r ON r.mid = s.mid LEFT JOIN messages as m ON m.mid = s.mid WHERE m.uid = %d AND m.out = 1 AND s.seq_out >= %d AND s.seq_out <= %d ORDER BY seq_out ASC", var6, var1, var3));
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label136;
         }

         while(true) {
            long var11;
            try {
               if (!var10.next()) {
                  break;
               }

               var11 = var10.longValue(1);
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label136;
            }

            long var13 = var11;
            if (var11 == 0L) {
               try {
                  var13 = Utilities.random.nextLong();
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label136;
               }
            }

            int var15;
            int var16;
            NativeByteBuffer var17;
            try {
               var15 = var10.intValue(2);
               var9 = var10.intValue(3);
               var16 = var10.intValue(5);
               var17 = var10.byteBufferValue(0);
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label136;
            }

            TLRPC.Message var18;
            if (var17 != null) {
               try {
                  var18 = TLRPC.Message.TLdeserialize(var17, var17.readInt32(false), false);
                  var18.readAttachPath(var17, UserConfig.getInstance(this.currentAccount).clientUserId);
                  var17.reuse();
                  var18.random_id = var13;
                  var18.dialog_id = var6;
                  var18.seq_in = var15;
                  var18.seq_out = var9;
                  var18.ttl = var10.intValue(4);
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label136;
               }
            } else {
               try {
                  var18 = this.createDeleteMessage(var16, var9, var15, var13, var2);
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label136;
               }
            }

            try {
               var35.add(var18);
               var8.remove(var9);
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label136;
            }
         }

         label139: {
            try {
               var10.dispose();
               if (var8.size() == 0) {
                  break label139;
               }
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label136;
            }

            var9 = 0;

            while(true) {
               try {
                  if (var9 >= var8.size()) {
                     break;
                  }

                  var35.add(this.createDeleteMessage(UserConfig.getInstance(this.currentAccount).getNewMessageId(), var8.keyAt(var9), 0, Utilities.random.nextLong(), var2));
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label136;
               }

               ++var9;
            }

            try {
               UserConfig.getInstance(this.currentAccount).saveConfig(false);
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label136;
            }
         }

         try {
            Collections.sort(var35, _$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc.INSTANCE);
            ArrayList var36 = new ArrayList();
            var36.add(var2);
            _$$Lambda$SecretChatHelper$VQkryblefkb_35jNwuFnQ2KSKkw var39 = new _$$Lambda$SecretChatHelper$VQkryblefkb_35jNwuFnQ2KSKkw(this, var35);
            AndroidUtilities.runOnUIThread(var39);
            SendMessagesHelper var40 = SendMessagesHelper.getInstance(this.currentAccount);
            ArrayList var37 = new ArrayList();
            ArrayList var38 = new ArrayList();
            var40.processUnsentMessages(var35, var37, var38, var36);
            MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast(String.format(Locale.US, "REPLACE INTO requested_holes VALUES(%d, %d, %d)", var2.id, var1, var3)).stepThis().dispose();
            return;
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
         }
      }

      Exception var34 = var10000;
      FileLog.e((Throwable)var34);
   }

   // $FF: synthetic method
   public void lambda$startSecretChat$29$SecretChatHelper(Context var1, AlertDialog var2, TLRPC.User var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         TLRPC.messages_DhConfig var9 = (TLRPC.messages_DhConfig)var4;
         if (var4 instanceof TLRPC.TL_messages_dhConfig) {
            if (!Utilities.isGoodPrime(var9.p, var9.g)) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$V_xN6On4v0MEOO6_YoYKk7r9DOk(var1, var2));
               return;
            }

            MessagesStorage.getInstance(this.currentAccount).setSecretPBytes(var9.p);
            MessagesStorage.getInstance(this.currentAccount).setSecretG(var9.g);
            MessagesStorage.getInstance(this.currentAccount).setLastSecretVersion(var9.version);
            MessagesStorage.getInstance(this.currentAccount).saveSecretParams(MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion(), MessagesStorage.getInstance(this.currentAccount).getSecretG(), MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
         }

         byte[] var6 = new byte[256];

         for(int var7 = 0; var7 < 256; ++var7) {
            var6[var7] = (byte)((byte)((byte)((int)(Utilities.random.nextDouble() * 256.0D)) ^ var9.random[var7]));
         }

         byte[] var10 = BigInteger.valueOf((long)MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, var6), new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes())).toByteArray();
         byte[] var8 = var10;
         if (var10.length > 256) {
            var8 = new byte[256];
            System.arraycopy(var10, 1, var8, 0, 256);
         }

         TLRPC.TL_messages_requestEncryption var11 = new TLRPC.TL_messages_requestEncryption();
         var11.g_a = var8;
         var11.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(var3);
         var11.random_id = Utilities.random.nextInt();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$SecretChatHelper$BEU71I5KzZcukvSdHQ6G9fRsUbQ(this, var1, var2, var6, var3), 2);
      } else {
         this.delayedEncryptedChatUpdates.clear();
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$TZa6lzq7a_IHyrpxBJVmRAmKQE8(this, var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$startSecretChat$30$SecretChatHelper(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var1, true);
   }

   protected void performSendEncryptedRequest(TLRPC.DecryptedMessage var1, TLRPC.Message var2, TLRPC.EncryptedChat var3, TLRPC.InputEncryptedFile var4, String var5, MessageObject var6) {
      if (var1 != null && var3.auth_key != null && !(var3 instanceof TLRPC.TL_encryptedChatRequested) && !(var3 instanceof TLRPC.TL_encryptedChatWaiting)) {
         SendMessagesHelper.getInstance(this.currentAccount).putToSendingMessages(var2);
         Utilities.stageQueue.postRunnable(new _$$Lambda$SecretChatHelper$go4ClJO8kMeuzFvRQpPn8_EmO40(this, var3, var1, var2, var4, var6, var5));
      }
   }

   protected void performSendEncryptedRequest(TLRPC.TL_messages_sendEncryptedMultiMedia var1, SendMessagesHelper.DelayedMessage var2) {
      for(int var3 = 0; var3 < var1.files.size(); ++var3) {
         this.performSendEncryptedRequest((TLRPC.DecryptedMessage)var1.messages.get(var3), (TLRPC.Message)var2.messages.get(var3), var2.encryptedChat, (TLRPC.InputEncryptedFile)var1.files.get(var3), (String)var2.originalPaths.get(var3), (MessageObject)var2.messageObjects.get(var3));
      }

   }

   public void processAcceptedSecretChat(TLRPC.EncryptedChat var1) {
      BigInteger var2 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
      BigInteger var3 = new BigInteger(1, var1.g_a_or_b);
      if (!Utilities.isGoodGaAndGb(var3, var2)) {
         this.declineSecretChat(var1.id);
      } else {
         byte[] var9 = var3.modPow(new BigInteger(1, var1.a_or_b), var2).toByteArray();
         byte[] var4;
         byte[] var8;
         if (var9.length > 256) {
            var8 = new byte[256];
            System.arraycopy(var9, var9.length - 256, var8, 0, 256);
         } else if (var9.length < 256) {
            var4 = new byte[256];
            System.arraycopy(var9, 0, var4, 256 - var9.length, var9.length);
            int var5 = 0;

            while(true) {
               var8 = var4;
               if (var5 >= 256 - var9.length) {
                  break;
               }

               var4[var5] = (byte)0;
               ++var5;
            }
         } else {
            var8 = var9;
         }

         var4 = Utilities.computeSHA1(var8);
         var9 = new byte[8];
         System.arraycopy(var4, var4.length - 8, var9, 0, 8);
         long var6 = Utilities.bytesToLong(var9);
         if (var1.key_fingerprint == var6) {
            var1.auth_key = var8;
            var1.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            var1.seq_in = -2;
            var1.seq_out = 1;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
            MessagesController.getInstance(this.currentAccount).putEncryptedChat(var1, false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM(this, var1));
         } else {
            TLRPC.TL_encryptedChatDiscarded var10 = new TLRPC.TL_encryptedChatDiscarded();
            var10.id = var1.id;
            var10.user_id = var1.user_id;
            var10.auth_key = var1.auth_key;
            var10.key_create_date = var1.key_create_date;
            var10.key_use_count_in = (short)var1.key_use_count_in;
            var10.key_use_count_out = (short)var1.key_use_count_out;
            var10.seq_in = var1.seq_in;
            var10.seq_out = var1.seq_out;
            var10.admin_id = var1.admin_id;
            var10.mtproto_seq = var1.mtproto_seq;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var10);
            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE(this, var10));
            this.declineSecretChat(var1.id);
         }

      }
   }

   public TLRPC.Message processDecryptedObject(TLRPC.EncryptedChat var1, TLRPC.EncryptedFile var2, int var3, TLObject var4, boolean var5) {
      if (var4 == null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("unknown TLObject");
         }
      } else {
         int var6 = var1.admin_id;
         int var7 = var6;
         if (var6 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            var7 = var1.participant_id;
         }

         if (AndroidUtilities.getPeerLayerVersion(var1.layer) >= 20 && var1.exchange_id == 0L && var1.future_key_fingerprint == 0L && var1.key_use_count_in >= 120) {
            this.requestNewSecretChatKey(var1);
         }

         if (var1.exchange_id == 0L && var1.future_key_fingerprint != 0L && !var5) {
            var1.future_auth_key = new byte[256];
            var1.future_key_fingerprint = 0L;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
         } else if (var1.exchange_id != 0L && var5) {
            var1.key_fingerprint = var1.future_key_fingerprint;
            var1.auth_key = var1.future_auth_key;
            var1.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            var1.future_auth_key = new byte[256];
            var1.future_key_fingerprint = 0L;
            var1.key_use_count_in = (short)0;
            var1.key_use_count_out = (short)0;
            var1.exchange_id = 0L;
            MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
         }

         long var10;
         byte[] var22;
         byte[] var37;
         if (var4 instanceof TLRPC.TL_decryptedMessage) {
            TLRPC.TL_decryptedMessage var38 = (TLRPC.TL_decryptedMessage)var4;
            Object var31;
            if (AndroidUtilities.getPeerLayerVersion(var1.layer) >= 17) {
               var31 = new TLRPC.TL_message_secret();
               ((TLRPC.Message)var31).ttl = var38.ttl;
               ((TLRPC.Message)var31).entities = var38.entities;
            } else {
               var31 = new TLRPC.TL_message();
               ((TLRPC.Message)var31).ttl = var1.ttl;
            }

            ((TLRPC.Message)var31).message = var38.message;
            ((TLRPC.Message)var31).date = var3;
            var6 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            ((TLRPC.Message)var31).id = var6;
            ((TLRPC.Message)var31).local_id = var6;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            ((TLRPC.Message)var31).from_id = var7;
            ((TLRPC.Message)var31).to_id = new TLRPC.TL_peerUser();
            ((TLRPC.Message)var31).random_id = var38.random_id;
            ((TLRPC.Message)var31).to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            ((TLRPC.Message)var31).unread = true;
            ((TLRPC.Message)var31).flags = 768;
            String var40 = var38.via_bot_name;
            if (var40 != null && var40.length() > 0) {
               ((TLRPC.Message)var31).via_bot_name = var38.via_bot_name;
               ((TLRPC.Message)var31).flags |= 2048;
            }

            var10 = var38.grouped_id;
            if (var10 != 0L) {
               ((TLRPC.Message)var31).grouped_id = var10;
               ((TLRPC.Message)var31).flags |= 131072;
            }

            ((TLRPC.Message)var31).dialog_id = (long)var1.id << 32;
            var10 = var38.reply_to_random_id;
            if (var10 != 0L) {
               ((TLRPC.Message)var31).reply_to_random_id = var10;
               ((TLRPC.Message)var31).flags |= 8;
            }

            TLRPC.DecryptedMessageMedia var17 = var38.media;
            TLRPC.MessageMedia var21;
            if (var17 != null && !(var17 instanceof TLRPC.TL_decryptedMessageMediaEmpty)) {
               if (var17 instanceof TLRPC.TL_decryptedMessageMediaWebPage) {
                  ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaWebPage();
                  ((TLRPC.Message)var31).media.webpage = new TLRPC.TL_webPageUrlPending();
                  ((TLRPC.Message)var31).media.webpage.url = var38.media.url;
               } else {
                  TLRPC.DecryptedMessageMedia var34;
                  if (var17 instanceof TLRPC.TL_decryptedMessageMediaContact) {
                     ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaContact();
                     var21 = ((TLRPC.Message)var31).media;
                     var34 = var38.media;
                     var21.last_name = var34.last_name;
                     var21.first_name = var34.first_name;
                     var21.phone_number = var34.phone_number;
                     var21.user_id = var34.user_id;
                     var21.vcard = "";
                  } else {
                     TLRPC.GeoPoint var18;
                     if (var17 instanceof TLRPC.TL_decryptedMessageMediaGeoPoint) {
                        ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaGeo();
                        ((TLRPC.Message)var31).media.geo = new TLRPC.TL_geoPoint();
                        var18 = ((TLRPC.Message)var31).media.geo;
                        var34 = var38.media;
                        var18.lat = var34.lat;
                        var18._long = var34._long;
                     } else {
                        byte[] var20;
                        String var25;
                        TLRPC.DecryptedMessageMedia var41;
                        TLRPC.DecryptedMessageMedia var42;
                        if (var17 instanceof TLRPC.TL_decryptedMessageMediaPhoto) {
                           var37 = var17.key;
                           if (var37 == null || var37.length != 32) {
                              return null;
                           }

                           var20 = var17.iv;
                           if (var20 == null || var20.length != 32) {
                              return null;
                           }

                           ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaPhoto();
                           var21 = ((TLRPC.Message)var31).media;
                           var21.flags |= 3;
                           var25 = var38.media.caption;
                           if (var25 == null) {
                              var25 = "";
                           }

                           ((TLRPC.Message)var31).message = var25;
                           ((TLRPC.Message)var31).media.photo = new TLRPC.TL_photo();
                           TLRPC.Photo var47 = ((TLRPC.Message)var31).media.photo;
                           var47.file_reference = new byte[0];
                           var47.date = ((TLRPC.Message)var31).date;
                           var42 = var38.media;
                           var20 = ((TLRPC.TL_decryptedMessageMediaPhoto)var42).thumb;
                           if (var20 != null && var20.length != 0 && var20.length <= 6000 && var42.thumb_w <= 100 && var42.thumb_h <= 100) {
                              TLRPC.TL_photoCachedSize var45 = new TLRPC.TL_photoCachedSize();
                              TLRPC.DecryptedMessageMedia var44 = var38.media;
                              var45.w = var44.thumb_w;
                              var45.h = var44.thumb_h;
                              var45.bytes = var20;
                              var45.type = "s";
                              var45.location = new TLRPC.TL_fileLocationUnavailable();
                              ((TLRPC.Message)var31).media.photo.sizes.add(var45);
                           }

                           var3 = ((TLRPC.Message)var31).ttl;
                           if (var3 != 0) {
                              var21 = ((TLRPC.Message)var31).media;
                              var21.ttl_seconds = var3;
                              var21.flags |= 4;
                           }

                           TLRPC.TL_photoSize var48 = new TLRPC.TL_photoSize();
                           var42 = var38.media;
                           var48.w = var42.w;
                           var48.h = var42.h;
                           var48.type = "x";
                           var48.size = var2.size;
                           var48.location = new TLRPC.TL_fileEncryptedLocation();
                           TLRPC.FileLocation var46 = var48.location;
                           var41 = var38.media;
                           var46.key = var41.key;
                           var46.iv = var41.iv;
                           var46.dc_id = var2.dc_id;
                           var46.volume_id = var2.id;
                           var46.secret = var2.access_hash;
                           var46.local_id = var2.key_fingerprint;
                           ((TLRPC.Message)var31).media.photo.sizes.add(var48);
                        } else {
                           TLRPC.Document var23;
                           Object var39;
                           TLRPC.Document var43;
                           if (var17 instanceof TLRPC.TL_decryptedMessageMediaVideo) {
                              label329: {
                                 var37 = var17.key;
                                 if (var37 != null && var37.length == 32) {
                                    var20 = var17.iv;
                                    if (var20 != null && var20.length == 32) {
                                       ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaDocument();
                                       var21 = ((TLRPC.Message)var31).media;
                                       var21.flags |= 3;
                                       var21.document = new TLRPC.TL_documentEncrypted();
                                       var43 = ((TLRPC.Message)var31).media.document;
                                       var17 = var38.media;
                                       var43.key = var17.key;
                                       var43.iv = var17.iv;
                                       var43.dc_id = var2.dc_id;
                                       var25 = var17.caption;
                                       if (var25 == null) {
                                          var25 = "";
                                       }

                                       ((TLRPC.Message)var31).message = var25;
                                       var23 = ((TLRPC.Message)var31).media.document;
                                       var23.date = var3;
                                       var23.size = var2.size;
                                       var23.id = var2.id;
                                       var23.access_hash = var2.access_hash;
                                       var23.mime_type = var38.media.mime_type;
                                       if (var23.mime_type == null) {
                                          var23.mime_type = "video/mp4";
                                       }

                                       var17 = var38.media;
                                       var22 = ((TLRPC.TL_decryptedMessageMediaVideo)var17).thumb;
                                       if (var22 != null && var22.length != 0 && var22.length <= 6000 && var17.thumb_w <= 100 && var17.thumb_h <= 100) {
                                          var39 = new TLRPC.TL_photoCachedSize();
                                          ((TLRPC.PhotoSize)var39).bytes = var22;
                                          var34 = var38.media;
                                          ((TLRPC.PhotoSize)var39).w = var34.thumb_w;
                                          ((TLRPC.PhotoSize)var39).h = var34.thumb_h;
                                          ((TLRPC.PhotoSize)var39).type = "s";
                                          ((TLRPC.PhotoSize)var39).location = new TLRPC.TL_fileLocationUnavailable();
                                       } else {
                                          var39 = new TLRPC.TL_photoSizeEmpty();
                                          ((TLRPC.PhotoSize)var39).type = "s";
                                       }

                                       ((TLRPC.Message)var31).media.document.thumbs.add(var39);
                                       var23 = ((TLRPC.Message)var31).media.document;
                                       var23.flags |= 1;
                                       TLRPC.TL_documentAttributeVideo var36 = new TLRPC.TL_documentAttributeVideo();
                                       var17 = var38.media;
                                       var36.w = var17.w;
                                       var36.h = var17.h;
                                       var36.duration = var17.duration;
                                       var36.supports_streaming = false;
                                       ((TLRPC.Message)var31).media.document.attributes.add(var36);
                                       var3 = ((TLRPC.Message)var31).ttl;
                                       if (var3 != 0) {
                                          var21 = ((TLRPC.Message)var31).media;
                                          var21.ttl_seconds = var3;
                                          var21.flags |= 4;
                                       }

                                       var3 = ((TLRPC.Message)var31).ttl;
                                       if (var3 != 0) {
                                          ((TLRPC.Message)var31).ttl = Math.max(var38.media.duration + 1, var3);
                                       }
                                       break label329;
                                    }
                                 }

                                 return null;
                              }
                           } else if (var17 instanceof TLRPC.TL_decryptedMessageMediaDocument) {
                              var37 = var17.key;
                              if (var37 == null || var37.length != 32) {
                                 return null;
                              }

                              var20 = var17.iv;
                              if (var20 == null || var20.length != 32) {
                                 return null;
                              }

                              ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaDocument();
                              var21 = ((TLRPC.Message)var31).media;
                              var21.flags |= 3;
                              var25 = var38.media.caption;
                              if (var25 == null) {
                                 var25 = "";
                              }

                              ((TLRPC.Message)var31).message = var25;
                              ((TLRPC.Message)var31).media.document = new TLRPC.TL_documentEncrypted();
                              var23 = ((TLRPC.Message)var31).media.document;
                              var23.id = var2.id;
                              var23.access_hash = var2.access_hash;
                              var23.date = var3;
                              var42 = var38.media;
                              if (var42 instanceof TLRPC.TL_decryptedMessageMediaDocument_layer8) {
                                 TLRPC.TL_documentAttributeFilename var35 = new TLRPC.TL_documentAttributeFilename();
                                 var35.file_name = var38.media.file_name;
                                 ((TLRPC.Message)var31).media.document.attributes.add(var35);
                              } else {
                                 var23.attributes = var42.attributes;
                              }

                              var23 = ((TLRPC.Message)var31).media.document;
                              var42 = var38.media;
                              var23.mime_type = var42.mime_type;
                              var3 = var42.size;
                              if (var3 != 0) {
                                 var3 = Math.min(var3, var2.size);
                              } else {
                                 var3 = var2.size;
                              }

                              var23.size = var3;
                              var43 = ((TLRPC.Message)var31).media.document;
                              var17 = var38.media;
                              var43.key = var17.key;
                              var43.iv = var17.iv;
                              if (var43.mime_type == null) {
                                 var43.mime_type = "";
                              }

                              var17 = var38.media;
                              var37 = ((TLRPC.TL_decryptedMessageMediaDocument)var17).thumb;
                              if (var37 != null && var37.length != 0 && var37.length <= 6000 && var17.thumb_w <= 100 && var17.thumb_h <= 100) {
                                 var39 = new TLRPC.TL_photoCachedSize();
                                 ((TLRPC.PhotoSize)var39).bytes = var37;
                                 var41 = var38.media;
                                 ((TLRPC.PhotoSize)var39).w = var41.thumb_w;
                                 ((TLRPC.PhotoSize)var39).h = var41.thumb_h;
                                 ((TLRPC.PhotoSize)var39).type = "s";
                                 ((TLRPC.PhotoSize)var39).location = new TLRPC.TL_fileLocationUnavailable();
                              } else {
                                 var39 = new TLRPC.TL_photoSizeEmpty();
                                 ((TLRPC.PhotoSize)var39).type = "s";
                              }

                              ((TLRPC.Message)var31).media.document.thumbs.add(var39);
                              var23 = ((TLRPC.Message)var31).media.document;
                              var23.flags |= 1;
                              var23.dc_id = var2.dc_id;
                              if (MessageObject.isVoiceMessage((TLRPC.Message)var31) || MessageObject.isRoundVideoMessage((TLRPC.Message)var31)) {
                                 ((TLRPC.Message)var31).media_unread = true;
                              }
                           } else if (var17 instanceof TLRPC.TL_decryptedMessageMediaExternalDocument) {
                              ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaDocument();
                              var21 = ((TLRPC.Message)var31).media;
                              var21.flags |= 3;
                              ((TLRPC.Message)var31).message = "";
                              var21.document = new TLRPC.TL_document();
                              var23 = ((TLRPC.Message)var31).media.document;
                              var34 = var38.media;
                              var23.id = var34.id;
                              var23.access_hash = var34.access_hash;
                              var23.file_reference = new byte[0];
                              var23.date = var34.date;
                              var23.attributes = var34.attributes;
                              var23.mime_type = var34.mime_type;
                              var23.dc_id = var34.dc_id;
                              var23.size = var34.size;
                              var23.thumbs.add(((TLRPC.TL_decryptedMessageMediaExternalDocument)var34).thumb);
                              var23 = ((TLRPC.Message)var31).media.document;
                              var23.flags |= 1;
                              if (var23.mime_type == null) {
                                 var23.mime_type = "";
                              }
                           } else if (var17 instanceof TLRPC.TL_decryptedMessageMediaAudio) {
                              label334: {
                                 var37 = var17.key;
                                 if (var37 != null && var37.length == 32) {
                                    var20 = var17.iv;
                                    if (var20 != null && var20.length == 32) {
                                       ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaDocument();
                                       var21 = ((TLRPC.Message)var31).media;
                                       var21.flags |= 3;
                                       var21.document = new TLRPC.TL_documentEncrypted();
                                       var23 = ((TLRPC.Message)var31).media.document;
                                       var42 = var38.media;
                                       var23.key = var42.key;
                                       var23.iv = var42.iv;
                                       var23.id = var2.id;
                                       var23.access_hash = var2.access_hash;
                                       var23.date = var3;
                                       var23.size = var2.size;
                                       var23.dc_id = var2.dc_id;
                                       var23.mime_type = var42.mime_type;
                                       var25 = var42.caption;
                                       if (var25 == null) {
                                          var25 = "";
                                       }

                                       ((TLRPC.Message)var31).message = var25;
                                       var23 = ((TLRPC.Message)var31).media.document;
                                       if (var23.mime_type == null) {
                                          var23.mime_type = "audio/ogg";
                                       }

                                       TLRPC.TL_documentAttributeAudio var28 = new TLRPC.TL_documentAttributeAudio();
                                       var28.duration = var38.media.duration;
                                       var28.voice = true;
                                       ((TLRPC.Message)var31).media.document.attributes.add(var28);
                                       var3 = ((TLRPC.Message)var31).ttl;
                                       if (var3 != 0) {
                                          ((TLRPC.Message)var31).ttl = Math.max(var38.media.duration + 1, var3);
                                       }

                                       if (((TLRPC.Message)var31).media.document.thumbs.isEmpty()) {
                                          TLRPC.TL_photoSizeEmpty var29 = new TLRPC.TL_photoSizeEmpty();
                                          var29.type = "s";
                                          ((TLRPC.Message)var31).media.document.thumbs.add(var29);
                                       }
                                       break label334;
                                    }
                                 }

                                 return null;
                              }
                           } else {
                              if (!(var17 instanceof TLRPC.TL_decryptedMessageMediaVenue)) {
                                 return null;
                              }

                              ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaVenue();
                              ((TLRPC.Message)var31).media.geo = new TLRPC.TL_geoPoint();
                              TLRPC.MessageMedia var33 = ((TLRPC.Message)var31).media;
                              var18 = var33.geo;
                              var41 = var38.media;
                              var18.lat = var41.lat;
                              var18._long = var41._long;
                              var33.title = var41.title;
                              var33.address = var41.address;
                              var33.provider = var41.provider;
                              var33.venue_id = var41.venue_id;
                              var33.venue_type = "";
                           }
                        }
                     }
                  }
               }
            } else {
               ((TLRPC.Message)var31).media = new TLRPC.TL_messageMediaEmpty();
            }

            var3 = ((TLRPC.Message)var31).ttl;
            if (var3 != 0) {
               var21 = ((TLRPC.Message)var31).media;
               if (var21.ttl_seconds == 0) {
                  var21.ttl_seconds = var3;
                  var21.flags |= 4;
               }
            }

            return (TLRPC.Message)var31;
         }

         if (var4 instanceof TLRPC.TL_decryptedMessageService) {
            TLRPC.TL_decryptedMessageService var12 = (TLRPC.TL_decryptedMessageService)var4;
            TLRPC.DecryptedMessageAction var19 = var12.action;
            if (var19 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL || var19 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
               TLRPC.TL_messageService var32 = new TLRPC.TL_messageService();
               TLRPC.DecryptedMessageAction var30 = var12.action;
               if (var30 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                  var32.action = new TLRPC.TL_messageEncryptedAction();
                  var6 = var12.action.ttl_seconds;
                  if (var6 < 0 || var6 > 31536000) {
                     var12.action.ttl_seconds = 31536000;
                  }

                  var30 = var12.action;
                  var1.ttl = var30.ttl_seconds;
                  var32.action.encryptedAction = var30;
                  MessagesStorage.getInstance(this.currentAccount).updateEncryptedChatTTL(var1);
               } else if (var30 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                  var32.action = new TLRPC.TL_messageEncryptedAction();
                  var32.action.encryptedAction = var12.action;
               }

               var6 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
               var32.id = var6;
               var32.local_id = var6;
               UserConfig.getInstance(this.currentAccount).saveConfig(false);
               var32.unread = true;
               var32.flags = 256;
               var32.date = var3;
               var32.from_id = var7;
               var32.to_id = new TLRPC.TL_peerUser();
               var32.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
               var32.dialog_id = (long)var1.id << 32;
               return var32;
            }

            if (var19 instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$OJ7QWMtzGqvNXSkDUc1v2NRta0c(this, (long)var1.id << 32));
               return null;
            }

            if (var19 instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
               if (!var19.random_ids.isEmpty()) {
                  this.pendingEncMessagesToDelete.addAll(var12.action.random_ids);
               }

               return null;
            }

            if (var19 instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
               if (!var19.random_ids.isEmpty()) {
                  var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                  MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(var1.id, var3, var3, 1, var12.action.random_ids);
               }
            } else if (var19 instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
               this.applyPeerLayer(var1, var19.layer);
            } else {
               byte[] var8;
               BigInteger var24;
               byte[] var27;
               if (var19 instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                  var10 = var1.exchange_id;
                  if (var10 != 0L) {
                     if (var10 > var19.exchange_id) {
                        if (BuildVars.LOGS_ENABLED) {
                           FileLog.d("we already have request key with higher exchange_id");
                        }

                        return null;
                     }

                     this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var10);
                  }

                  var8 = new byte[256];
                  Utilities.random.nextBytes(var8);
                  BigInteger var9 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                  var24 = BigInteger.valueOf((long)MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, var8), var9);
                  BigInteger var13 = new BigInteger(1, var12.action.g_a);
                  if (!Utilities.isGoodGaAndGb(var13, var9)) {
                     this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var12.action.exchange_id);
                     return null;
                  }

                  var22 = var24.toByteArray();
                  var27 = var22;
                  if (var22.length > 256) {
                     var27 = new byte[256];
                     System.arraycopy(var22, 1, var27, 0, 256);
                  }

                  var8 = var13.modPow(new BigInteger(1, var8), var9).toByteArray();
                  if (var8.length > 256) {
                     var22 = new byte[256];
                     System.arraycopy(var8, var8.length - 256, var22, 0, 256);
                  } else if (var8.length < 256) {
                     var37 = new byte[256];
                     System.arraycopy(var8, 0, var37, 256 - var8.length, var8.length);
                     var3 = 0;

                     while(true) {
                        var22 = var37;
                        if (var3 >= 256 - var8.length) {
                           break;
                        }

                        var37[var3] = (byte)0;
                        ++var3;
                     }
                  } else {
                     var22 = var8;
                  }

                  var37 = Utilities.computeSHA1(var22);
                  var8 = new byte[8];
                  System.arraycopy(var37, var37.length - 8, var8, 0, 8);
                  var1.exchange_id = var12.action.exchange_id;
                  var1.future_auth_key = var22;
                  var1.future_key_fingerprint = Utilities.bytesToLong(var8);
                  var1.g_a_or_b = var27;
                  MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                  this.sendAcceptKeyMessage(var1, (TLRPC.Message)null);
               } else if (var19 instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                  if (var1.exchange_id == var19.exchange_id) {
                     BigInteger var26 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
                     var24 = new BigInteger(1, var12.action.g_b);
                     if (!Utilities.isGoodGaAndGb(var24, var26)) {
                        var1.future_auth_key = new byte[256];
                        var1.future_key_fingerprint = 0L;
                        var1.exchange_id = 0L;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                        this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var12.action.exchange_id);
                        return null;
                     }

                     var27 = var24.modPow(new BigInteger(1, var1.a_or_b), var26).toByteArray();
                     if (var27.length > 256) {
                        var22 = new byte[256];
                        System.arraycopy(var27, var27.length - 256, var22, 0, 256);
                     } else if (var27.length < 256) {
                        var8 = new byte[256];
                        System.arraycopy(var27, 0, var8, 256 - var27.length, var27.length);
                        var3 = 0;

                        while(true) {
                           var22 = var8;
                           if (var3 >= 256 - var27.length) {
                              break;
                           }

                           var8[var3] = (byte)0;
                           ++var3;
                        }
                     } else {
                        var22 = var27;
                     }

                     var27 = Utilities.computeSHA1(var22);
                     var8 = new byte[8];
                     System.arraycopy(var27, var27.length - 8, var8, 0, 8);
                     var10 = Utilities.bytesToLong(var8);
                     if (var12.action.key_fingerprint == var10) {
                        var1.future_auth_key = var22;
                        var1.future_key_fingerprint = var10;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                        this.sendCommitKeyMessage(var1, (TLRPC.Message)null);
                     } else {
                        var1.future_auth_key = new byte[256];
                        var1.future_key_fingerprint = 0L;
                        var1.exchange_id = 0L;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                        this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var12.action.exchange_id);
                     }
                  } else {
                     var1.future_auth_key = new byte[256];
                     var1.future_key_fingerprint = 0L;
                     var1.exchange_id = 0L;
                     MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                     this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var12.action.exchange_id);
                  }
               } else if (var19 instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                  if (var1.exchange_id == var19.exchange_id) {
                     var10 = var1.future_key_fingerprint;
                     if (var10 == var19.key_fingerprint) {
                        long var14 = var1.key_fingerprint;
                        var22 = var1.auth_key;
                        var1.key_fingerprint = var10;
                        var1.auth_key = var1.future_auth_key;
                        var1.key_create_date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                        var1.future_auth_key = var22;
                        var1.future_key_fingerprint = var14;
                        var1.key_use_count_in = (short)0;
                        var1.key_use_count_out = (short)0;
                        var1.exchange_id = 0L;
                        MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                        this.sendNoopMessage(var1, (TLRPC.Message)null);
                        return null;
                     }
                  }

                  var1.future_auth_key = new byte[256];
                  var1.future_key_fingerprint = 0L;
                  var1.exchange_id = 0L;
                  MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                  this.sendAbortKeyMessage(var1, (TLRPC.Message)null, var12.action.exchange_id);
               } else if (var19 instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                  if (var1.exchange_id == var19.exchange_id) {
                     var1.future_auth_key = new byte[256];
                     var1.future_key_fingerprint = 0L;
                     var1.exchange_id = 0L;
                     MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
                  }
               } else if (!(var19 instanceof TLRPC.TL_decryptedMessageActionNoop)) {
                  if (!(var19 instanceof TLRPC.TL_decryptedMessageActionResend)) {
                     return null;
                  }

                  var3 = var19.end_seq_no;
                  var7 = var1.in_seq_no;
                  if (var3 >= var7) {
                     var6 = var19.start_seq_no;
                     if (var3 >= var6) {
                        if (var6 < var7) {
                           var19.start_seq_no = var7;
                        }

                        var19 = var12.action;
                        this.resendMessages(var19.start_seq_no, var19.end_seq_no, var1);
                        return null;
                     }
                  }

                  return null;
               }
            }
         } else if (BuildVars.LOGS_ENABLED) {
            StringBuilder var16 = new StringBuilder();
            var16.append("unknown message ");
            var16.append(var4);
            FileLog.e(var16.toString());
         }
      }

      return null;
   }

   protected void processPendingEncMessages() {
      if (!this.pendingEncMessagesToDelete.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU(this, new ArrayList(this.pendingEncMessagesToDelete)));
         ArrayList var1 = new ArrayList(this.pendingEncMessagesToDelete);
         MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeletedByRandoms(var1);
         this.pendingEncMessagesToDelete.clear();
      }

   }

   protected void processUpdateEncryption(TLRPC.TL_updateEncryption var1, ConcurrentHashMap var2) {
      TLRPC.EncryptedChat var3 = var1.chat;
      long var4 = (long)var3.id;
      TLRPC.EncryptedChat var6 = MessagesController.getInstance(this.currentAccount).getEncryptedChatDB(var3.id, false);
      if (var3 instanceof TLRPC.TL_encryptedChatRequested && var6 == null) {
         int var7 = var3.participant_id;
         int var8 = var7;
         if (var7 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            var8 = var3.admin_id;
         }

         TLRPC.User var9 = MessagesController.getInstance(this.currentAccount).getUser(var8);
         TLRPC.User var12 = var9;
         if (var9 == null) {
            var12 = (TLRPC.User)var2.get(var8);
         }

         var3.user_id = var8;
         TLRPC.TL_dialog var11 = new TLRPC.TL_dialog();
         var11.id = var4 << 32;
         var11.unread_count = 0;
         var11.top_message = 0;
         var11.last_message_date = var1.date;
         MessagesController.getInstance(this.currentAccount).putEncryptedChat(var3, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$IkT_sRYXQIirGnmDeh6b889eh_A(this, var11));
         MessagesStorage.getInstance(this.currentAccount).putEncryptedChat(var3, var12, var11);
         this.acceptSecretChat(var3);
      } else if (var3 instanceof TLRPC.TL_encryptedChat) {
         if (var6 instanceof TLRPC.TL_encryptedChatWaiting) {
            byte[] var10 = var6.auth_key;
            if (var10 == null || var10.length == 1) {
               var3.a_or_b = var6.a_or_b;
               var3.user_id = var6.user_id;
               this.processAcceptedSecretChat(var3);
               return;
            }
         }

         if (var6 == null && this.startingSecretChat) {
            this.delayedEncryptedChatUpdates.add(var1);
         }
      } else {
         if (var6 != null) {
            var3.user_id = var6.user_id;
            var3.auth_key = var6.auth_key;
            var3.key_create_date = var6.key_create_date;
            var3.key_use_count_in = (short)var6.key_use_count_in;
            var3.key_use_count_out = (short)var6.key_use_count_out;
            var3.ttl = var6.ttl;
            var3.seq_in = var6.seq_in;
            var3.seq_out = var6.seq_out;
            var3.admin_id = var6.admin_id;
            var3.mtproto_seq = var6.mtproto_seq;
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$SecretChatHelper$G9V6FvkI_PnA0UumuoB_kxH2lOM(this, var6, var3));
      }

   }

   public void requestNewSecretChatKey(TLRPC.EncryptedChat var1) {
      if (AndroidUtilities.getPeerLayerVersion(var1.layer) >= 20) {
         byte[] var2 = new byte[256];
         Utilities.random.nextBytes(var2);
         byte[] var3 = BigInteger.valueOf((long)MessagesStorage.getInstance(this.currentAccount).getSecretG()).modPow(new BigInteger(1, var2), new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes())).toByteArray();
         byte[] var4 = var3;
         if (var3.length > 256) {
            var4 = new byte[256];
            System.arraycopy(var3, 1, var4, 0, 256);
         }

         var1.exchange_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
         var1.a_or_b = var2;
         var1.g_a = var4;
         MessagesStorage.getInstance(this.currentAccount).updateEncryptedChat(var1);
         this.sendRequestKeyMessage(var1, (TLRPC.Message)null);
      }
   }

   public void sendAbortKeyMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2, long var3) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var5 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var5.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var5.action = new TLRPC.TL_decryptedMessageActionAbortKey();
            TLRPC.DecryptedMessageAction var6 = var5.action;
            var6.exchange_id = var3;
            var2 = this.createServiceSecretMessage(var1, var6);
         }

         var5.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var5, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendAcceptKeyMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionAcceptKey();
            TLRPC.DecryptedMessageAction var4 = var3.action;
            var4.exchange_id = var1.exchange_id;
            var4.key_fingerprint = var1.future_key_fingerprint;
            var4.g_b = var1.g_a_or_b;
            var2 = this.createServiceSecretMessage(var1, var4);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendClearHistoryMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionFlushHistory();
            var2 = this.createServiceSecretMessage(var1, var3.action);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendCommitKeyMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionCommitKey();
            TLRPC.DecryptedMessageAction var4 = var3.action;
            var4.exchange_id = var1.exchange_id;
            var4.key_fingerprint = var1.future_key_fingerprint;
            var2 = this.createServiceSecretMessage(var1, var4);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendMessagesDeleteMessage(TLRPC.EncryptedChat var1, ArrayList var2, TLRPC.Message var3) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var4 = new TLRPC.TL_decryptedMessageService();
         if (var3 != null) {
            var4.action = ((TLRPC.Message)var3).action.encryptedAction;
         } else {
            var4.action = new TLRPC.TL_decryptedMessageActionDeleteMessages();
            TLRPC.DecryptedMessageAction var5 = var4.action;
            var5.random_ids = var2;
            var3 = this.createServiceSecretMessage(var1, var5);
         }

         var4.random_id = ((TLRPC.Message)var3).random_id;
         this.performSendEncryptedRequest(var4, (TLRPC.Message)var3, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendMessagesReadMessage(TLRPC.EncryptedChat var1, ArrayList var2, TLRPC.Message var3) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var4 = new TLRPC.TL_decryptedMessageService();
         if (var3 != null) {
            var4.action = ((TLRPC.Message)var3).action.encryptedAction;
         } else {
            var4.action = new TLRPC.TL_decryptedMessageActionReadMessages();
            TLRPC.DecryptedMessageAction var5 = var4.action;
            var5.random_ids = var2;
            var3 = this.createServiceSecretMessage(var1, var5);
         }

         var4.random_id = ((TLRPC.Message)var3).random_id;
         this.performSendEncryptedRequest(var4, (TLRPC.Message)var3, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendNoopMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionNoop();
            var2 = this.createServiceSecretMessage(var1, var3.action);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendNotifyLayerMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         if (!this.sendingNotifyLayer.contains(var1.id)) {
            this.sendingNotifyLayer.add(var1.id);
            TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
            if (var2 != null) {
               var3.action = ((TLRPC.Message)var2).action.encryptedAction;
            } else {
               var3.action = new TLRPC.TL_decryptedMessageActionNotifyLayer();
               TLRPC.DecryptedMessageAction var4 = var3.action;
               var4.layer = 73;
               var2 = this.createServiceSecretMessage(var1, var4);
            }

            var3.random_id = ((TLRPC.Message)var2).random_id;
            this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
         }
      }
   }

   public void sendRequestKeyMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionRequestKey();
            TLRPC.DecryptedMessageAction var4 = var3.action;
            var4.exchange_id = var1.exchange_id;
            var4.g_a = var1.g_a;
            var2 = this.createServiceSecretMessage(var1, var4);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendScreenshotMessage(TLRPC.EncryptedChat var1, ArrayList var2, TLRPC.Message var3) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var4 = new TLRPC.TL_decryptedMessageService();
         if (var3 != null) {
            var4.action = ((TLRPC.Message)var3).action.encryptedAction;
         } else {
            var4.action = new TLRPC.TL_decryptedMessageActionScreenshotMessages();
            TLRPC.DecryptedMessageAction var6 = var4.action;
            var6.random_ids = var2;
            var3 = this.createServiceSecretMessage(var1, var6);
            MessageObject var5 = new MessageObject(this.currentAccount, (TLRPC.Message)var3, false);
            var5.messageOwner.send_state = 1;
            var2 = new ArrayList();
            var2.add(var5);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(((TLRPC.Message)var3).dialog_id, var2);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         }

         var4.random_id = ((TLRPC.Message)var3).random_id;
         this.performSendEncryptedRequest(var4, (TLRPC.Message)var3, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void sendTTLMessage(TLRPC.EncryptedChat var1, TLRPC.Message var2) {
      if (var1 instanceof TLRPC.TL_encryptedChat) {
         TLRPC.TL_decryptedMessageService var3 = new TLRPC.TL_decryptedMessageService();
         if (var2 != null) {
            var3.action = ((TLRPC.Message)var2).action.encryptedAction;
         } else {
            var3.action = new TLRPC.TL_decryptedMessageActionSetMessageTTL();
            TLRPC.DecryptedMessageAction var6 = var3.action;
            var6.ttl_seconds = var1.ttl;
            var2 = this.createServiceSecretMessage(var1, var6);
            MessageObject var4 = new MessageObject(this.currentAccount, (TLRPC.Message)var2, false);
            var4.messageOwner.send_state = 1;
            ArrayList var5 = new ArrayList();
            var5.add(var4);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(((TLRPC.Message)var2).dialog_id, var5);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         }

         var3.random_id = ((TLRPC.Message)var2).random_id;
         this.performSendEncryptedRequest(var3, (TLRPC.Message)var2, var1, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)null);
      }
   }

   public void startSecretChat(Context var1, TLRPC.User var2) {
      if (var2 != null && var1 != null) {
         this.startingSecretChat = true;
         AlertDialog var3 = new AlertDialog(var1, 3);
         TLRPC.TL_messages_getDhConfig var4 = new TLRPC.TL_messages_getDhConfig();
         var4.random_length = 256;
         var4.version = MessagesStorage.getInstance(this.currentAccount).getLastSecretVersion();
         var3.setOnCancelListener(new _$$Lambda$SecretChatHelper$FOvPRdYvHRkFV5NyZdXuhMMalnM(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$SecretChatHelper$CyJGexOzIKpecJNn32s1dWnqMhg(this, var1, var3, var2), 2)));

         try {
            var3.show();
         } catch (Exception var5) {
         }
      }

   }

   public static class TL_decryptedMessageHolder extends TLObject {
      public static int constructor;
      public int date;
      public int decryptedWithVersion;
      public TLRPC.EncryptedFile file;
      public TLRPC.TL_decryptedMessageLayer layer;
      public boolean new_key_used;

      public void readParams(AbstractSerializedData var1, boolean var2) {
         var1.readInt64(var2);
         this.date = var1.readInt32(var2);
         this.layer = TLRPC.TL_decryptedMessageLayer.TLdeserialize(var1, var1.readInt32(var2), var2);
         if (var1.readBool(var2)) {
            this.file = TLRPC.EncryptedFile.TLdeserialize(var1, var1.readInt32(var2), var2);
         }

         this.new_key_used = var1.readBool(var2);
      }

      public void serializeToStream(AbstractSerializedData var1) {
         var1.writeInt32(constructor);
         var1.writeInt64(0L);
         var1.writeInt32(this.date);
         this.layer.serializeToStream(var1);
         boolean var2;
         if (this.file != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1.writeBool(var2);
         TLRPC.EncryptedFile var3 = this.file;
         if (var3 != null) {
            var3.serializeToStream(var1);
         }

         var1.writeBool(this.new_key_used);
      }
   }
}
