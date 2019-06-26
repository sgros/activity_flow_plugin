package org.telegram.messenger;

import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class MessagesStorage {
   private static volatile MessagesStorage[] Instance = new MessagesStorage[3];
   private static final int LAST_DB_VERSION = 60;
   private File cacheFile;
   private int currentAccount;
   private SQLiteDatabase database;
   private int lastDateValue = 0;
   private int lastPtsValue = 0;
   private int lastQtsValue = 0;
   private int lastSavedDate = 0;
   private int lastSavedPts = 0;
   private int lastSavedQts = 0;
   private int lastSavedSeq = 0;
   private int lastSecretVersion = 0;
   private int lastSeqValue = 0;
   private AtomicLong lastTaskId = new AtomicLong(System.currentTimeMillis());
   private CountDownLatch openSync = new CountDownLatch(1);
   private int secretG = 0;
   private byte[] secretPBytes = null;
   private File shmCacheFile;
   private DispatchQueue storageQueue = new DispatchQueue("storageQueue");
   private File walCacheFile;

   public MessagesStorage(int var1) {
      this.currentAccount = var1;
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$l_EMBf4E_fIloSDlPGVY7ULYtZw(this));
   }

   public static void addUsersAndChatsFromMessage(TLRPC.Message var0, ArrayList var1, ArrayList var2) {
      int var3 = var0.from_id;
      if (var3 != 0) {
         if (var3 > 0) {
            if (!var1.contains(var3)) {
               var1.add(var0.from_id);
            }
         } else if (!var2.contains(-var3)) {
            var2.add(-var0.from_id);
         }
      }

      var3 = var0.via_bot_id;
      if (var3 != 0 && !var1.contains(var3)) {
         var1.add(var0.via_bot_id);
      }

      TLRPC.MessageAction var4 = var0.action;
      byte var5 = 0;
      if (var4 != null) {
         var3 = var4.user_id;
         if (var3 != 0 && !var1.contains(var3)) {
            var1.add(var0.action.user_id);
         }

         var3 = var0.action.channel_id;
         if (var3 != 0 && !var2.contains(var3)) {
            var2.add(var0.action.channel_id);
         }

         var3 = var0.action.chat_id;
         if (var3 != 0 && !var2.contains(var3)) {
            var2.add(var0.action.chat_id);
         }

         if (!var0.action.users.isEmpty()) {
            for(var3 = 0; var3 < var0.action.users.size(); ++var3) {
               Integer var6 = (Integer)var0.action.users.get(var3);
               if (!var1.contains(var6)) {
                  var1.add(var6);
               }
            }
         }
      }

      if (!var0.entities.isEmpty()) {
         for(var3 = var5; var3 < var0.entities.size(); ++var3) {
            TLRPC.MessageEntity var7 = (TLRPC.MessageEntity)var0.entities.get(var3);
            if (var7 instanceof TLRPC.TL_messageEntityMentionName) {
               var1.add(((TLRPC.TL_messageEntityMentionName)var7).user_id);
            } else if (var7 instanceof TLRPC.TL_inputMessageEntityMentionName) {
               var1.add(((TLRPC.TL_inputMessageEntityMentionName)var7).user_id.user_id);
            }
         }
      }

      TLRPC.MessageMedia var8 = var0.media;
      if (var8 != null) {
         var3 = var8.user_id;
         if (var3 != 0 && !var1.contains(var3)) {
            var1.add(var0.media.user_id);
         }
      }

      TLRPC.MessageFwdHeader var9 = var0.fwd_from;
      if (var9 != null) {
         var3 = var9.from_id;
         if (var3 != 0 && !var1.contains(var3)) {
            var1.add(var0.fwd_from.from_id);
         }

         var3 = var0.fwd_from.channel_id;
         if (var3 != 0 && !var2.contains(var3)) {
            var2.add(var0.fwd_from.channel_id);
         }

         TLRPC.Peer var10 = var0.fwd_from.saved_from_peer;
         if (var10 != null) {
            var3 = var10.user_id;
            if (var3 != 0) {
               if (!var2.contains(var3)) {
                  var1.add(var0.fwd_from.saved_from_peer.user_id);
               }
            } else {
               var3 = var10.channel_id;
               if (var3 != 0) {
                  if (!var2.contains(var3)) {
                     var2.add(var0.fwd_from.saved_from_peer.channel_id);
                  }
               } else {
                  var3 = var10.chat_id;
                  if (var3 != 0 && !var2.contains(var3)) {
                     var2.add(var0.fwd_from.saved_from_peer.chat_id);
                  }
               }
            }
         }
      }

      var3 = var0.ttl;
      if (var3 < 0 && !var2.contains(-var3)) {
         var2.add(-var0.ttl);
      }

   }

   private void checkIfFolderEmptyInternal(int var1) {
      try {
         SQLiteCursor var2 = this.database.queryFinalized("SELECT did FROM dialogs WHERE folder_id = ?", var1);
         if (!var2.next()) {
            _$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc_msXC__TJg var3 = new _$$Lambda$MessagesStorage$PjPiLXNm6_EsiAsuc_msXC__TJg(this, var1);
            AndroidUtilities.runOnUIThread(var3);
            SQLiteDatabase var6 = this.database;
            StringBuilder var4 = new StringBuilder();
            var4.append("DELETE FROM dialogs WHERE did = ");
            var4.append(DialogObject.makeFolderDialogId(var1));
            var6.executeFast(var4.toString()).stepThis().dispose();
         }

         var2.dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   private void cleanupInternal(boolean var1) {
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
      SQLiteDatabase var2 = this.database;
      if (var2 != null) {
         var2.close();
         this.database = null;
      }

      if (var1) {
         File var3 = this.cacheFile;
         if (var3 != null) {
            var3.delete();
            this.cacheFile = null;
         }

         var3 = this.walCacheFile;
         if (var3 != null) {
            var3.delete();
            this.walCacheFile = null;
         }

         var3 = this.shmCacheFile;
         if (var3 != null) {
            var3.delete();
            this.shmCacheFile = null;
         }
      }

   }

   private void closeHolesInTable(String var1, long var2, int var4, int var5) {
      Exception var10000;
      label139: {
         SQLiteDatabase var6;
         boolean var10001;
         SQLiteCursor var30;
         try {
            var6 = this.database;
            Locale var7 = Locale.US;
            StringBuilder var8 = new StringBuilder();
            var8.append("SELECT start, end FROM ");
            var8.append(var1);
            var8.append(" WHERE uid = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))");
            var30 = var6.queryFinalized(String.format(var7, var8.toString(), var2, var4, var5, var4, var5, var4, var5, var4, var5));
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label139;
         }

         ArrayList var34 = null;

         int var9;
         int var10;
         while(true) {
            try {
               if (!var30.next()) {
                  break;
               }
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label139;
            }

            ArrayList var32 = var34;
            if (var34 == null) {
               try {
                  var32 = new ArrayList();
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label139;
               }
            }

            try {
               var9 = var30.intValue(0);
               var10 = var30.intValue(1);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label139;
            }

            if (var9 == var10 && var9 == 1) {
               var34 = var32;
            } else {
               try {
                  MessagesStorage.Hole var35 = new MessagesStorage.Hole(var9, var10);
                  var32.add(var35);
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label139;
               }

               var34 = var32;
            }
         }

         try {
            var30.dispose();
         } catch (Exception var23) {
            var10000 = var23;
            var10001 = false;
            break label139;
         }

         if (var34 == null) {
            return;
         }

         var10 = 0;

         while(true) {
            MessagesStorage.Hole var36;
            try {
               if (var10 >= var34.size()) {
                  return;
               }

               var36 = (MessagesStorage.Hole)var34.get(var10);
               var9 = var36.end;
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }

            label142: {
               Locale var39;
               StringBuilder var40;
               if (var5 >= var9 - 1) {
                  try {
                     if (var4 <= var36.start + 1) {
                        var6 = this.database;
                        var39 = Locale.US;
                        var40 = new StringBuilder();
                        var40.append("DELETE FROM ");
                        var40.append(var1);
                        var40.append(" WHERE uid = %d AND start = %d AND end = %d");
                        var6.executeFast(String.format(var39, var40.toString(), var2, var36.start, var36.end)).stepThis().dispose();
                        break label142;
                     }
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var9 = var36.end;
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break;
               }

               Exception var37;
               if (var5 >= var9 - 1) {
                  try {
                     var9 = var36.end;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break;
                  }

                  if (var9 != var4) {
                     try {
                        var6 = this.database;
                        Locale var12 = Locale.US;
                        StringBuilder var11 = new StringBuilder();
                        var11.append("UPDATE ");
                        var11.append(var1);
                        var11.append(" SET end = %d WHERE uid = %d AND start = %d AND end = %d");
                        var6.executeFast(String.format(var12, var11.toString(), var4, var2, var36.start, var36.end)).stepThis().dispose();
                     } catch (Exception var17) {
                        var37 = var17;

                        try {
                           FileLog.e((Throwable)var37);
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               } else {
                  label101: {
                     label100: {
                        try {
                           if (var4 > var36.start + 1) {
                              break label100;
                           }

                           var9 = var36.start;
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break;
                        }

                        if (var9 != var5) {
                           try {
                              SQLiteDatabase var38 = this.database;
                              Locale var31 = Locale.US;
                              var40 = new StringBuilder();
                              var40.append("UPDATE ");
                              var40.append(var1);
                              var40.append(" SET start = %d WHERE uid = %d AND start = %d AND end = %d");
                              var38.executeFast(String.format(var31, var40.toString(), var5, var2, var36.start, var36.end)).stepThis().dispose();
                           } catch (Exception var15) {
                              var37 = var15;

                              try {
                                 FileLog.e((Throwable)var37);
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break;
                              }
                           }
                        }
                        break label101;
                     }

                     try {
                        var6 = this.database;
                        var39 = Locale.US;
                        var40 = new StringBuilder();
                        var40.append("DELETE FROM ");
                        var40.append(var1);
                        var40.append(" WHERE uid = %d AND start = %d AND end = %d");
                        var6.executeFast(String.format(var39, var40.toString(), var2, var36.start, var36.end)).stepThis().dispose();
                        var6 = this.database;
                        var40 = new StringBuilder();
                        var40.append("REPLACE INTO ");
                        var40.append(var1);
                        var40.append(" VALUES(?, ?, ?)");
                        SQLitePreparedStatement var33 = var6.executeFast(var40.toString());
                        var33.requery();
                        var33.bindLong(1, var2);
                        var33.bindInteger(2, var36.start);
                        var33.bindInteger(3, var4);
                        var33.step();
                        var33.requery();
                        var33.bindLong(1, var2);
                        var33.bindInteger(2, var5);
                        var33.bindInteger(3, var36.end);
                        var33.step();
                        var33.dispose();
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }
               }
            }

            ++var10;
         }
      }

      Exception var29 = var10000;
      FileLog.e((Throwable)var29);
   }

   public static void createFirstHoles(long var0, SQLitePreparedStatement var2, SQLitePreparedStatement var3, int var4) throws Exception {
      var2.requery();
      var2.bindLong(1, var0);
      byte var5;
      if (var4 == 1) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      var2.bindInteger(2, var5);
      var2.bindInteger(3, var4);
      var2.step();

      for(int var7 = 0; var7 < 5; ++var7) {
         var3.requery();
         var3.bindLong(1, var0);
         var3.bindInteger(2, var7);
         byte var6;
         if (var4 == 1) {
            var6 = 1;
         } else {
            var6 = 0;
         }

         var3.bindInteger(3, var6);
         var3.bindInteger(4, var4);
         var3.step();
      }

   }

   private void doneHolesInTable(String var1, long var2, int var4) throws Exception {
      SQLiteDatabase var5;
      StringBuilder var7;
      if (var4 == 0) {
         var5 = this.database;
         Locale var6 = Locale.US;
         var7 = new StringBuilder();
         var7.append("DELETE FROM ");
         var7.append(var1);
         var7.append(" WHERE uid = %d");
         var5.executeFast(String.format(var6, var7.toString(), var2)).stepThis().dispose();
      } else {
         var5 = this.database;
         Locale var11 = Locale.US;
         StringBuilder var9 = new StringBuilder();
         var9.append("DELETE FROM ");
         var9.append(var1);
         var9.append(" WHERE uid = %d AND start = 0");
         var5.executeFast(String.format(var11, var9.toString(), var2)).stepThis().dispose();
      }

      SQLiteDatabase var10 = this.database;
      var7 = new StringBuilder();
      var7.append("REPLACE INTO ");
      var7.append(var1);
      var7.append(" VALUES(?, ?, ?)");
      SQLitePreparedStatement var8 = var10.executeFast(var7.toString());
      var8.requery();
      var8.bindLong(1, var2);
      var8.bindInteger(2, 1);
      var8.bindInteger(3, 1);
      var8.step();
      var8.dispose();
   }

   private void ensureOpened() {
      try {
         this.openSync.await();
      } catch (Throwable var2) {
      }

   }

   private void fixNotificationSettings() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$H7ZjmdIrsBPnbbqD1cn5Lti86iY(this));
   }

   private void fixUnsupportedMedia(TLRPC.Message var1) {
      if (var1 != null) {
         TLRPC.MessageMedia var2 = var1.media;
         if (var2 instanceof TLRPC.TL_messageMediaUnsupported_old) {
            if (var2.bytes.length == 0) {
               var2.bytes = new byte[1];
               var2.bytes[0] = (byte)100;
            }
         } else if (var2 instanceof TLRPC.TL_messageMediaUnsupported) {
            var1.media = new TLRPC.TL_messageMediaUnsupported_old();
            var2 = var1.media;
            var2.bytes = new byte[1];
            var2.bytes[0] = (byte)100;
            var1.flags |= 512;
         }

      }
   }

   private String formatUserSearchName(TLRPC.User var1) {
      StringBuilder var2 = new StringBuilder();
      String var3 = var1.first_name;
      if (var3 != null && var3.length() > 0) {
         var2.append(var1.first_name);
      }

      var3 = var1.last_name;
      if (var3 != null && var3.length() > 0) {
         if (var2.length() > 0) {
            var2.append(" ");
         }

         var2.append(var1.last_name);
      }

      var2.append(";;;");
      var3 = var1.username;
      if (var3 != null && var3.length() > 0) {
         var2.append(var1.username);
      }

      return var2.toString().toLowerCase();
   }

   public static MessagesStorage getInstance(int var0) {
      MessagesStorage var1 = Instance[var0];
      MessagesStorage var2 = var1;
      if (var1 == null) {
         synchronized(MessagesStorage.class){}

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
               MessagesStorage[] var23;
               try {
                  var23 = Instance;
                  var2 = new MessagesStorage(var0);
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

   private int getMessageMediaType(TLRPC.Message var1) {
      if (var1 instanceof TLRPC.TL_message_secret) {
         if (var1.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isGifMessage(var1)) {
            int var2 = var1.ttl;
            if (var2 > 0 && var2 <= 60) {
               return 1;
            }
         }

         if (!MessageObject.isVoiceMessage(var1) && !MessageObject.isVideoMessage(var1) && !MessageObject.isRoundVideoMessage(var1)) {
            if (!(var1.media instanceof TLRPC.TL_messageMediaPhoto) && !MessageObject.isVideoMessage(var1)) {
               return -1;
            } else {
               return 0;
            }
         } else {
            return 1;
         }
      } else {
         if (var1 instanceof TLRPC.TL_message) {
            TLRPC.MessageMedia var3 = var1.media;
            if ((var3 instanceof TLRPC.TL_messageMediaPhoto || var3 instanceof TLRPC.TL_messageMediaDocument) && var1.media.ttl_seconds != 0) {
               return 1;
            }
         }

         if (var1.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isVideoMessage(var1)) {
            return 0;
         } else {
            return -1;
         }
      }
   }

   private static boolean isEmpty(LongSparseArray var0) {
      boolean var1;
      if (var0 != null && var0.size() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isEmpty(SparseArray var0) {
      boolean var1;
      if (var0 != null && var0.size() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isEmpty(SparseIntArray var0) {
      boolean var1;
      if (var0 != null && var0.size() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isEmpty(List var0) {
      boolean var1;
      if (var0 != null && !var0.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isEmpty(SparseLongArray var0) {
      boolean var1;
      if (var0 != null && var0.size() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isValidKeyboardToSave(TLRPC.Message var1) {
      TLRPC.ReplyMarkup var2 = var1.reply_markup;
      boolean var3;
      if (var2 == null || var2 instanceof TLRPC.TL_replyInlineMarkup || var2.selective && !var1.mentioned) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   // $FF: synthetic method
   static void lambda$null$136(int var0, MessageObject var1, ArrayList var2) {
      NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.replaceMessagesObjects, var1.getDialogId(), var2);
   }

   // $FF: synthetic method
   static void lambda$null$140(MessagesStorage.IntCallback var0, int var1) {
      var0.run(var1);
   }

   // $FF: synthetic method
   static void lambda$null$31(ArrayList var0) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.wallpapersDidLoad, var0);
   }

   // $FF: synthetic method
   static int lambda$null$51(LongSparseArray var0, Long var1, Long var2) {
      Integer var4 = (Integer)var0.get(var1);
      Integer var3 = (Integer)var0.get(var2);
      if (var4 < var3) {
         return 1;
      } else {
         return var4 > var3 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$null$95(MessagesStorage.IntCallback var0, int var1) {
      var0.run(var1);
   }

   // $FF: synthetic method
   static void lambda$null$97(MessagesStorage.IntCallback var0, int var1) {
      var0.run(var1);
   }

   // $FF: synthetic method
   static int lambda$null$99(TLRPC.Message var0, TLRPC.Message var1) {
      int var2 = var0.id;
      int var3;
      if (var2 > 0) {
         var3 = var1.id;
         if (var3 > 0) {
            if (var2 > var3) {
               return -1;
            }

            if (var2 < var3) {
               return 1;
            }

            return 0;
         }
      }

      var3 = var0.id;
      if (var3 < 0) {
         var2 = var1.id;
         if (var2 < 0) {
            if (var3 < var2) {
               return -1;
            }

            if (var3 > var2) {
               return 1;
            }

            return 0;
         }
      }

      var3 = var0.date;
      var2 = var1.date;
      if (var3 > var2) {
         return -1;
      } else if (var3 < var2) {
         return 1;
      } else {
         return 0;
      }
   }

   private void loadPendingTasks() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Mh1_3ksiRZzSQICRWjWNRIrSkV4(this));
   }

   private ArrayList markMessagesAsDeletedInternal(int var1, int var2) {
      Exception var10000;
      Exception var43;
      label236: {
         ArrayList var3;
         LongSparseArray var4;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = new LongSparseArray();
         } catch (Exception var37) {
            var10000 = var37;
            var10001 = false;
            break label236;
         }

         long var5 = (long)var2 | (long)var1 << 32;

         ArrayList var7;
         SQLiteDatabase var8;
         Locale var9;
         try {
            var7 = new ArrayList();
            var2 = UserConfig.getInstance(this.currentAccount).getClientUserId();
            var8 = this.database;
            var9 = Locale.US;
         } catch (Exception var36) {
            var10000 = var36;
            var10001 = false;
            break label236;
         }

         int var10 = -var1;

         SQLiteCursor var11;
         try {
            var11 = var8.queryFinalized(String.format(var9, "SELECT uid, data, read_state, out, mention FROM messages WHERE uid = %d AND mid <= %d", var10, var5));
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label236;
         }

         long var12;
         int var14;
         Integer[] var39;
         label237: {
            label223:
            while(true) {
               try {
                  if (!var11.next()) {
                     break label237;
                  }

                  var12 = var11.longValue(0);
               } catch (Exception var35) {
                  var10000 = var35;
                  var10001 = false;
                  break;
               }

               if (var12 != (long)var2) {
                  label239: {
                     Integer[] var40;
                     try {
                        var1 = var11.intValue(2);
                        if (var11.intValue(3) != 0) {
                           break label239;
                        }

                        var40 = (Integer[])var4.get(var12);
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break;
                     }

                     var39 = var40;
                     if (var40 == null) {
                        try {
                           var39 = new Integer[]{0, 0};
                           var4.put(var12, var39);
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break;
                        }
                     }

                     Integer var41;
                     if (var1 < 2) {
                        var41 = var39[1];

                        try {
                           var39[1] = var39[1] + 1;
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break;
                        }
                     }

                     if (var1 == 0 || var1 == 2) {
                        var41 = var39[0];

                        try {
                           var39[0] = var39[0] + 1;
                        } catch (Exception var31) {
                           var10000 = var31;
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  if ((int)var12 == 0) {
                     NativeByteBuffer var44;
                     try {
                        var44 = var11.byteBufferValue(1);
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break;
                     }

                     if (var44 != null) {
                        TLRPC.Message var42;
                        try {
                           var42 = TLRPC.Message.TLdeserialize(var44, var44.readInt32(false), false);
                           var42.readAttachPath(var44, UserConfig.getInstance(this.currentAccount).clientUserId);
                           var44.reuse();
                        } catch (Exception var29) {
                           var10000 = var29;
                           var10001 = false;
                           break;
                        }

                        if (var42 != null) {
                           File var45;
                           label240: {
                              try {
                                 if (var42.media instanceof TLRPC.TL_messageMediaPhoto) {
                                    var14 = var42.media.photo.sizes.size();
                                    break label240;
                                 }
                              } catch (Exception var28) {
                                 var10000 = var28;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (!(var42.media instanceof TLRPC.TL_messageMediaDocument)) {
                                    continue;
                                 }

                                 var45 = FileLoader.getPathToAttach(var42.media.document);
                              } catch (Exception var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break;
                              }

                              if (var45 != null) {
                                 try {
                                    if (var45.toString().length() > 0) {
                                       var7.add(var45);
                                    }
                                 } catch (Exception var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break;
                                 }
                              }

                              try {
                                 var14 = var42.media.document.thumbs.size();
                              } catch (Exception var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break;
                              }

                              var1 = 0;

                              while(true) {
                                 if (var1 >= var14) {
                                    continue label223;
                                 }

                                 try {
                                    var45 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var42.media.document.thumbs.get(var1));
                                 } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label223;
                                 }

                                 if (var45 != null) {
                                    try {
                                       if (var45.toString().length() > 0) {
                                          var7.add(var45);
                                       }
                                    } catch (Exception var23) {
                                       var10000 = var23;
                                       var10001 = false;
                                       break label223;
                                    }
                                 }

                                 ++var1;
                              }
                           }

                           for(var1 = 0; var1 < var14; ++var1) {
                              try {
                                 var45 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var42.media.photo.sizes.get(var1));
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break label223;
                              }

                              if (var45 != null) {
                                 try {
                                    if (var45.toString().length() > 0) {
                                       var7.add(var45);
                                    }
                                 } catch (Exception var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                    break label223;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var43 = var10000;

            try {
               FileLog.e((Throwable)var43);
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label236;
            }
         }

         try {
            var11.dispose();
            FileLoader.getInstance(this.currentAccount).deleteFiles(var7, 0);
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label236;
         }

         var1 = 0;

         while(true) {
            SQLiteCursor var47;
            label130: {
               try {
                  if (var1 >= var4.size()) {
                     break;
                  }

                  var12 = var4.keyAt(var1);
                  var39 = (Integer[])var4.valueAt(var1);
                  SQLiteDatabase var38 = this.database;
                  StringBuilder var46 = new StringBuilder();
                  var46.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                  var46.append(var12);
                  var47 = var38.queryFinalized(var46.toString());
                  if (var47.next()) {
                     var2 = var47.intValue(0);
                     var14 = var47.intValue(1);
                     break label130;
                  }
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label236;
               }

               var2 = 0;
               var14 = 0;
            }

            try {
               var47.dispose();
               var3.add(var12);
               SQLitePreparedStatement var48 = this.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
               var48.requery();
               var48.bindInteger(1, Math.max(0, var2 - var39[0]));
               var48.bindInteger(2, Math.max(0, var14 - var39[1]));
               var48.bindLong(3, var12);
               var48.step();
               var48.dispose();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label236;
            }

            ++var1;
         }

         try {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE uid = %d AND mid <= %d", var10, var5)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE uid = %d AND mid <= %d", var10, var5)).stepThis().dispose();
            this.database.executeFast(String.format(Locale.US, "UPDATE media_counts_v2 SET old = 1 WHERE uid = %d", var10)).stepThis().dispose();
            return var3;
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
         }
      }

      var43 = var10000;
      FileLog.e((Throwable)var43);
      return null;
   }

   private ArrayList markMessagesAsDeletedInternal(ArrayList var1, int var2) {
      Exception var10000;
      label424: {
         ArrayList var3;
         ArrayList var4;
         LongSparseArray var5;
         boolean var10001;
         try {
            var3 = new ArrayList(var1);
            var4 = new ArrayList();
            var5 = new LongSparseArray();
         } catch (Exception var64) {
            var10000 = var64;
            var10001 = false;
            break label424;
         }

         int var7;
         long var8;
         String var69;
         if (var2 != 0) {
            StringBuilder var6;
            try {
               var6 = new StringBuilder(var1.size());
            } catch (Exception var61) {
               var10000 = var61;
               var10001 = false;
               break label424;
            }

            var7 = 0;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  var8 = (long)(Integer)var1.get(var7);
               } catch (Exception var62) {
                  var10000 = var62;
                  var10001 = false;
                  break label424;
               }

               long var10 = (long)var2;

               try {
                  if (var6.length() > 0) {
                     var6.append(',');
                  }
               } catch (Exception var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label424;
               }

               try {
                  var6.append(var8 | var10 << 32);
               } catch (Exception var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label424;
               }

               ++var7;
            }

            try {
               var69 = var6.toString();
            } catch (Exception var59) {
               var10000 = var59;
               var10001 = false;
               break label424;
            }
         } else {
            try {
               var69 = TextUtils.join(",", var1);
            } catch (Exception var58) {
               var10000 = var58;
               var10001 = false;
               break label424;
            }
         }

         ArrayList var12;
         int var13;
         SQLiteCursor var14;
         try {
            var12 = new ArrayList();
            var13 = UserConfig.getInstance(this.currentAccount).getClientUserId();
            var14 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, data, read_state, out, mention, mid FROM messages WHERE mid IN(%s)", var69));
         } catch (Exception var42) {
            var10000 = var42;
            var10001 = false;
            break label424;
         }

         int var17;
         label393: {
            Exception var16;
            label392:
            while(true) {
               try {
                  if (!var14.next()) {
                     break label393;
                  }

                  var8 = var14.longValue(0);
                  var3.remove(var14.intValue(5));
               } catch (Exception var57) {
                  var16 = var57;
                  break;
               }

               if (var8 != (long)var13) {
                  label428: {
                     label429: {
                        Integer[] var15;
                        try {
                           var7 = var14.intValue(2);
                           if (var14.intValue(3) != 0) {
                              break label429;
                           }

                           var15 = (Integer[])var5.get(var8);
                        } catch (Exception var56) {
                           var10000 = var56;
                           var10001 = false;
                           break label428;
                        }

                        Integer[] var75 = var15;
                        if (var15 == null) {
                           try {
                              var75 = new Integer[]{0, 0};
                              var5.put(var8, var75);
                           } catch (Exception var55) {
                              var10000 = var55;
                              var10001 = false;
                              break label428;
                           }
                        }

                        Integer var73;
                        if (var7 < 2) {
                           var73 = var75[1];

                           try {
                              var75[1] = var75[1] + 1;
                           } catch (Exception var54) {
                              var10000 = var54;
                              var10001 = false;
                              break label428;
                           }
                        }

                        if (var7 == 0 || var7 == 2) {
                           var73 = var75[0];

                           try {
                              var75[0] = var75[0] + 1;
                           } catch (Exception var53) {
                              var10000 = var53;
                              var10001 = false;
                              break label428;
                           }
                        }
                     }

                     if ((int)var8 != 0) {
                        continue;
                     }

                     NativeByteBuffer var74;
                     try {
                        var74 = var14.byteBufferValue(1);
                     } catch (Exception var52) {
                        var10000 = var52;
                        var10001 = false;
                        break label428;
                     }

                     if (var74 == null) {
                        continue;
                     }

                     TLRPC.Message var78;
                     try {
                        var78 = TLRPC.Message.TLdeserialize(var74, var74.readInt32(false), false);
                        var78.readAttachPath(var74, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var74.reuse();
                     } catch (Exception var51) {
                        var10000 = var51;
                        var10001 = false;
                        break label428;
                     }

                     if (var78 == null) {
                        continue;
                     }

                     File var76;
                     label430: {
                        try {
                           if (var78.media instanceof TLRPC.TL_messageMediaPhoto) {
                              var17 = var78.media.photo.sizes.size();
                              break label430;
                           }
                        } catch (Exception var50) {
                           var10000 = var50;
                           var10001 = false;
                           break label428;
                        }

                        try {
                           if (!(var78.media instanceof TLRPC.TL_messageMediaDocument)) {
                              continue;
                           }

                           var76 = FileLoader.getPathToAttach(var78.media.document);
                        } catch (Exception var49) {
                           var10000 = var49;
                           var10001 = false;
                           break label428;
                        }

                        if (var76 != null) {
                           try {
                              if (var76.toString().length() > 0) {
                                 var12.add(var76);
                              }
                           } catch (Exception var48) {
                              var10000 = var48;
                              var10001 = false;
                              break label428;
                           }
                        }

                        try {
                           var17 = var78.media.document.thumbs.size();
                        } catch (Exception var47) {
                           var10000 = var47;
                           var10001 = false;
                           break label428;
                        }

                        var7 = 0;

                        while(true) {
                           if (var7 >= var17) {
                              continue label392;
                           }

                           try {
                              var76 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var78.media.document.thumbs.get(var7));
                           } catch (Exception var46) {
                              var10000 = var46;
                              var10001 = false;
                              break label428;
                           }

                           if (var76 != null) {
                              try {
                                 if (var76.toString().length() > 0) {
                                    var12.add(var76);
                                 }
                              } catch (Exception var45) {
                                 var10000 = var45;
                                 var10001 = false;
                                 break label428;
                              }
                           }

                           ++var7;
                        }
                     }

                     var7 = 0;

                     while(true) {
                        if (var7 >= var17) {
                           continue label392;
                        }

                        try {
                           var76 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var78.media.photo.sizes.get(var7));
                        } catch (Exception var44) {
                           var10000 = var44;
                           var10001 = false;
                           break;
                        }

                        if (var76 != null) {
                           try {
                              if (var76.toString().length() > 0) {
                                 var12.add(var76);
                              }
                           } catch (Exception var43) {
                              var10000 = var43;
                              var10001 = false;
                              break;
                           }
                        }

                        ++var7;
                     }
                  }

                  var16 = var10000;
                  break;
               }
            }

            try {
               FileLog.e((Throwable)var16);
            } catch (Exception var41) {
               var10000 = var41;
               var10001 = false;
               break label424;
            }
         }

         String var18 = var69;

         try {
            var14.dispose();
            FileLoader.getInstance(this.currentAccount).deleteFiles(var12, 0);
         } catch (Exception var40) {
            var10000 = var40;
            var10001 = false;
            break label424;
         }

         var7 = 0;

         SQLitePreparedStatement var82;
         while(true) {
            Integer[] var70;
            SQLiteCursor var81;
            label297: {
               try {
                  if (var7 >= var5.size()) {
                     break;
                  }

                  var8 = var5.keyAt(var7);
                  var70 = (Integer[])var5.valueAt(var7);
                  SQLiteDatabase var77 = this.database;
                  StringBuilder var80 = new StringBuilder();
                  var80.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                  var80.append(var8);
                  var81 = var77.queryFinalized(var80.toString());
                  if (var81.next()) {
                     var17 = var81.intValue(0);
                     var13 = var81.intValue(1);
                     break label297;
                  }
               } catch (Exception var39) {
                  var10000 = var39;
                  var10001 = false;
                  break label424;
               }

               var17 = 0;
               var13 = 0;
            }

            try {
               var81.dispose();
               var4.add(var8);
               var82 = this.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
               var82.requery();
               var82.bindInteger(1, Math.max(0, var17 - var70[0]));
               var82.bindInteger(2, Math.max(0, var13 - var70[1]));
               var82.bindLong(3, var8);
               var82.step();
               var82.dispose();
            } catch (Exception var38) {
               var10000 = var38;
               var10001 = false;
               break label424;
            }

            ++var7;
         }

         label435: {
            SQLiteCursor var68;
            label282: {
               try {
                  this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE mid IN(%s)", var18)).stepThis().dispose();
                  this.database.executeFast(String.format(Locale.US, "DELETE FROM polls WHERE mid IN(%s)", var18)).stepThis().dispose();
                  this.database.executeFast(String.format(Locale.US, "DELETE FROM bot_keyboard WHERE mid IN(%s)", var18)).stepThis().dispose();
                  this.database.executeFast(String.format(Locale.US, "DELETE FROM messages_seq WHERE mid IN(%s)", var18)).stepThis().dispose();
                  if (var3.isEmpty()) {
                     var68 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, type FROM media_v2 WHERE mid IN(%s)", var18));
                     break label282;
                  }
               } catch (Exception var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label424;
               }

               if (var2 == 0) {
                  try {
                     this.database.executeFast("UPDATE media_counts_v2 SET old = 1 WHERE 1").stepThis().dispose();
                     break label435;
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label424;
                  }
               } else {
                  try {
                     this.database.executeFast(String.format(Locale.US, "UPDATE media_counts_v2 SET old = 1 WHERE uid = %d", -var2)).stepThis().dispose();
                     break label435;
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label424;
                  }
               }
            }

            SparseArray var71 = null;

            LongSparseArray var79;
            while(true) {
               try {
                  if (!var68.next()) {
                     break;
                  }

                  var8 = var68.longValue(0);
                  var2 = var68.intValue(1);
               } catch (Exception var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label424;
               }

               SparseArray var83 = var71;
               if (var71 == null) {
                  try {
                     var83 = new SparseArray();
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label424;
                  }
               }

               try {
                  var79 = (LongSparseArray)var83.get(var2);
               } catch (Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label424;
               }

               Integer var72;
               if (var79 == null) {
                  try {
                     var79 = new LongSparseArray();
                     var72 = 0;
                     var83.put(var2, var79);
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label424;
                  }
               } else {
                  try {
                     var72 = (Integer)var79.get(var8);
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label424;
                  }
               }

               Integer var66 = var72;
               if (var72 == null) {
                  try {
                     var66 = 0;
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label424;
                  }
               }

               try {
                  var79.put(var8, var66 + 1);
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label424;
               }

               var71 = var83;
            }

            try {
               var68.dispose();
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label424;
            }

            if (var71 != null) {
               try {
                  var82 = this.database.executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label424;
               }

               var2 = 0;

               while(true) {
                  int var19;
                  try {
                     if (var2 >= var71.size()) {
                        break;
                     }

                     var19 = var71.keyAt(var2);
                     var79 = (LongSparseArray)var71.valueAt(var2);
                  } catch (Exception var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label424;
                  }

                  var7 = 0;

                  while(true) {
                     SQLiteCursor var67;
                     label257: {
                        try {
                           if (var7 >= var79.size()) {
                              break;
                           }

                           var8 = var79.keyAt(var7);
                           var67 = this.database.queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", var8, var19));
                           if (var67.next()) {
                              var17 = var67.intValue(0);
                              var13 = var67.intValue(1);
                              break label257;
                           }
                        } catch (Exception var35) {
                           var10000 = var35;
                           var10001 = false;
                           break label424;
                        }

                        var13 = 0;
                        var17 = -1;
                     }

                     try {
                        var67.dispose();
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label424;
                     }

                     if (var17 != -1) {
                        try {
                           var82.requery();
                           var17 = Math.max(0, var17 - (Integer)var79.valueAt(var7));
                           var82.bindLong(1, var8);
                           var82.bindInteger(2, var19);
                           var82.bindInteger(3, var17);
                           var82.bindInteger(4, var13);
                           var82.step();
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label424;
                        }
                     }

                     ++var7;
                  }

                  ++var2;
               }

               try {
                  var82.dispose();
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label424;
               }
            }
         }

         try {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE mid IN(%s)", var18)).stepThis().dispose();
            DataQuery.getInstance(this.currentAccount).clearBotKeyboard(0L, var1);
            return var4;
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
         }
      }

      Exception var65 = var10000;
      FileLog.e((Throwable)var65);
      return null;
   }

   private void markMessagesAsReadInternal(SparseLongArray var1, SparseLongArray var2, SparseIntArray var3) {
      Exception var10000;
      label88: {
         boolean var4;
         boolean var10001;
         try {
            var4 = isEmpty(var1);
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label88;
         }

         byte var5 = 0;
         int var7;
         int var8;
         long var9;
         if (!var4) {
            SQLitePreparedStatement var6;
            try {
               var6 = this.database.executeFast("DELETE FROM unread_push_messages WHERE uid = ? AND mid <= ?");
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label88;
            }

            var7 = 0;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  var8 = var1.keyAt(var7);
                  var9 = var1.get(var8);
                  this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 0", var8, var9)).stepThis().dispose();
                  var6.requery();
                  var6.bindLong(1, (long)var8);
                  var6.bindLong(2, var9);
                  var6.step();
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label88;
               }

               ++var7;
            }

            try {
               var6.dispose();
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label88;
            }
         }

         label90: {
            try {
               if (isEmpty(var2)) {
                  break label90;
               }
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label88;
            }

            var7 = 0;

            while(true) {
               try {
                  if (var7 >= var2.size()) {
                     break;
                  }

                  var8 = var2.keyAt(var7);
                  var9 = var2.get(var8);
                  this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 1", var8, var9)).stepThis().dispose();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label88;
               }

               ++var7;
            }
         }

         if (var3 == null) {
            return;
         }

         try {
            if (isEmpty(var3)) {
               return;
            }
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label88;
         }

         var7 = var5;

         while(true) {
            try {
               if (var7 >= var3.size()) {
                  return;
               }

               var9 = (long)var3.keyAt(var7);
               int var21 = var3.valueAt(var7);
               SQLitePreparedStatement var20 = this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND date <= ? AND read_state IN(0,2) AND out = 1");
               var20.requery();
               var20.bindLong(1, var9 << 32);
               var20.bindInteger(2, var21);
               var20.step();
               var20.dispose();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            ++var7;
         }
      }

      Exception var19 = var10000;
      FileLog.e((Throwable)var19);
   }

   private void putChatsInternal(ArrayList var1) throws Exception {
      if (var1 != null && !var1.isEmpty()) {
         SQLitePreparedStatement var2 = this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            TLRPC.Chat var4 = (TLRPC.Chat)var1.get(var3);
            TLRPC.Chat var5 = var4;
            if (var4.min) {
               SQLiteCursor var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid = %d", var4.id));
               var5 = var4;
               if (var6.next()) {
                  label87: {
                     TLRPC.Chat var8;
                     label86: {
                        Exception var10000;
                        label101: {
                           NativeByteBuffer var7;
                           boolean var10001;
                           try {
                              var7 = var6.byteBufferValue(0);
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label101;
                           }

                           var5 = var4;
                           if (var7 == null) {
                              break label87;
                           }

                           try {
                              var8 = TLRPC.Chat.TLdeserialize(var7, var7.readInt32(false), false);
                              var7.reuse();
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label101;
                           }

                           var5 = var4;
                           if (var8 == null) {
                              break label87;
                           }

                           try {
                              var8.title = var4.title;
                              var8.photo = var4.photo;
                              var8.broadcast = var4.broadcast;
                              var8.verified = var4.verified;
                              var8.megagroup = var4.megagroup;
                              if (var4.default_banned_rights != null) {
                                 var8.default_banned_rights = var4.default_banned_rights;
                                 var8.flags |= 262144;
                              }
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label101;
                           }

                           try {
                              if (var4.admin_rights != null) {
                                 var8.admin_rights = var4.admin_rights;
                                 var8.flags |= 16384;
                              }
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label101;
                           }

                           try {
                              if (var4.banned_rights != null) {
                                 var8.banned_rights = var4.banned_rights;
                                 var8.flags |= 32768;
                              }
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label101;
                           }

                           try {
                              if (var4.username != null) {
                                 var8.username = var4.username;
                                 var8.flags |= 64;
                                 break label86;
                              }
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label101;
                           }

                           try {
                              var8.username = null;
                              var8.flags &= -65;
                              break label86;
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                           }
                        }

                        Exception var17 = var10000;
                        FileLog.e((Throwable)var17);
                        var5 = var4;
                        break label87;
                     }

                     var5 = var8;
                  }
               }

               var6.dispose();
            }

            var2.requery();
            NativeByteBuffer var16 = new NativeByteBuffer(var5.getObjectSize());
            var5.serializeToStream(var16);
            var2.bindInteger(1, var5.id);
            String var18 = var5.title;
            if (var18 != null) {
               var2.bindString(2, var18.toLowerCase());
            } else {
               var2.bindString(2, "");
            }

            var2.bindByteBuffer(3, (NativeByteBuffer)var16);
            var2.step();
            var16.reuse();
         }

         var2.dispose();
      }

   }

   private void putDialogsInternal(TLRPC.messages_Dialogs var1, int var2) {
      MessagesStorage var3 = this;
      TLRPC.messages_Dialogs var4 = var1;

      Exception var59;
      label334: {
         ArrayList var63;
         label333: {
            Exception var10000;
            label339: {
               LongSparseArray var5;
               boolean var10001;
               try {
                  var3.database.beginTransaction();
                  var5 = new LongSparseArray(var4.messages.size());
               } catch (Exception var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label339;
               }

               int var6 = 0;

               while(true) {
                  try {
                     if (var6 >= var4.messages.size()) {
                        break;
                     }

                     TLRPC.Message var7 = (TLRPC.Message)var4.messages.get(var6);
                     var5.put(MessageObject.getDialogId(var7), var7);
                  } catch (Exception var57) {
                     var10000 = var57;
                     var10001 = false;
                     break label339;
                  }

                  ++var6;
               }

               boolean var8;
               try {
                  var8 = var4.dialogs.isEmpty();
               } catch (Exception var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label339;
               }

               label316: {
                  if (!var8) {
                     SQLitePreparedStatement var9;
                     SQLitePreparedStatement var10;
                     SQLitePreparedStatement var11;
                     SQLitePreparedStatement var12;
                     SQLitePreparedStatement var61;
                     SQLitePreparedStatement var64;
                     try {
                        var9 = var3.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
                        var10 = var3.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        var11 = var3.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                        var64 = var3.database.executeFast("REPLACE INTO dialog_settings VALUES(?, ?)");
                        var61 = var3.database.executeFast("REPLACE INTO messages_holes VALUES(?, ?, ?)");
                        var12 = var3.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label316;
                     }

                     var6 = 0;
                     SQLitePreparedStatement var60 = null;

                     while(true) {
                        MessagesStorage var14 = this;
                        TLRPC.messages_Dialogs var15 = var1;

                        TLRPC.Dialog var16;
                        try {
                           if (var6 >= var15.dialogs.size()) {
                              break;
                           }

                           var16 = (TLRPC.Dialog)var15.dialogs.get(var6);
                           DialogObject.initDialog(var16);
                        } catch (Exception var44) {
                           var10000 = var44;
                           var10001 = false;
                           break label316;
                        }

                        SQLitePreparedStatement var70;
                        label347: {
                           SQLiteCursor var69;
                           if (var2 == 1) {
                              try {
                                 SQLiteDatabase var17 = var14.database;
                                 StringBuilder var68 = new StringBuilder();
                                 var68.append("SELECT did FROM dialogs WHERE did = ");
                                 var68.append(var16.id);
                                 var69 = var17.queryFinalized(var68.toString());
                                 var8 = var69.next();
                                 var69.dispose();
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break label339;
                              }

                              if (var8) {
                                 var70 = var60;
                                 break label347;
                              }
                           } else {
                              try {
                                 var8 = var16.pinned;
                              } catch (Exception var41) {
                                 var10000 = var41;
                                 var10001 = false;
                                 break label316;
                              }

                              if (var8 && var2 == 2) {
                                 try {
                                    SQLiteDatabase var71 = var14.database;
                                    StringBuilder var73 = new StringBuilder();
                                    var73.append("SELECT pinned FROM dialogs WHERE did = ");
                                    var73.append(var16.id);
                                    var69 = var71.queryFinalized(var73.toString());
                                    if (var69.next()) {
                                       var16.pinnedNum = var69.intValue(0);
                                    }
                                 } catch (Exception var55) {
                                    var10000 = var55;
                                    var10001 = false;
                                    break label339;
                                 }

                                 try {
                                    var69.dispose();
                                 } catch (Exception var30) {
                                    var10000 = var30;
                                    var10001 = false;
                                    break label339;
                                 }
                              }
                           }

                           SQLitePreparedStatement var75 = var64;

                           TLRPC.Message var72;
                           try {
                              var72 = (TLRPC.Message)var5.get(var16.id);
                           } catch (Exception var40) {
                              var10000 = var40;
                              var10001 = false;
                              break label316;
                           }

                           byte var13;
                           long var20;
                           int var65;
                           if (var72 != null) {
                              int var18;
                              try {
                                 var18 = Math.max(var72.date, 0);
                                 if (var14.isValidKeyboardToSave(var72)) {
                                    DataQuery.getInstance(var14.currentAccount).putBotKeyboard(var16.id, var72);
                                 }
                              } catch (Exception var54) {
                                 var10000 = var54;
                                 var10001 = false;
                                 break label339;
                              }

                              NativeByteBuffer var19;
                              try {
                                 var14.fixUnsupportedMedia(var72);
                                 var19 = new NativeByteBuffer(var72.getObjectSize());
                                 var72.serializeToStream(var19);
                                 var20 = (long)var72.id;
                                 if (var72.to_id.channel_id != 0) {
                                    var20 |= (long)var72.to_id.channel_id << 32;
                                 }
                              } catch (Exception var53) {
                                 var10000 = var53;
                                 var10001 = false;
                                 break label339;
                              }

                              label283: {
                                 label282: {
                                    try {
                                       var9.requery();
                                       var9.bindLong(1, var20);
                                       var9.bindLong(2, var16.id);
                                       var9.bindInteger(3, MessageObject.getUnreadFlags(var72));
                                       var9.bindInteger(4, var72.send_state);
                                       var9.bindInteger(5, var72.date);
                                       var9.bindByteBuffer(6, (NativeByteBuffer)var19);
                                       if (MessageObject.isOut(var72)) {
                                          break label282;
                                       }
                                    } catch (Exception var52) {
                                       var10000 = var52;
                                       var10001 = false;
                                       break label339;
                                    }

                                    var13 = 0;
                                    break label283;
                                 }

                                 var13 = 1;
                              }

                              label275: {
                                 try {
                                    var9.bindInteger(7, var13);
                                    var9.bindInteger(8, 0);
                                    if ((var72.flags & 1024) != 0) {
                                       var65 = var72.views;
                                       break label275;
                                    }
                                 } catch (Exception var51) {
                                    var10000 = var51;
                                    var10001 = false;
                                    break label339;
                                 }

                                 var65 = 0;
                              }

                              label267: {
                                 label266: {
                                    try {
                                       var9.bindInteger(9, var65);
                                       var9.bindInteger(10, 0);
                                       if (var72.mentioned) {
                                          break label266;
                                       }
                                    } catch (Exception var50) {
                                       var10000 = var50;
                                       var10001 = false;
                                       break label339;
                                    }

                                    var13 = 0;
                                    break label267;
                                 }

                                 var13 = 1;
                              }

                              try {
                                 var9.bindInteger(11, var13);
                                 var9.step();
                                 if (DataQuery.canAddMessageToMedia(var72)) {
                                    var11.requery();
                                    var11.bindLong(1, var20);
                                    var11.bindLong(2, var16.id);
                                    var11.bindInteger(3, var72.date);
                                    var11.bindInteger(4, DataQuery.getMediaType(var72));
                                    var11.bindByteBuffer(5, (NativeByteBuffer)var19);
                                    var11.step();
                                 }
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label339;
                              }

                              label344: {
                                 try {
                                    var19.reuse();
                                    if (!(var72.media instanceof TLRPC.TL_messageMediaPoll)) {
                                       break label344;
                                    }
                                 } catch (Exception var43) {
                                    var10000 = var43;
                                    var10001 = false;
                                    break label339;
                                 }

                                 if (var60 == null) {
                                    try {
                                       var60 = var14.database.executeFast("REPLACE INTO polls VALUES(?, ?)");
                                    } catch (Exception var28) {
                                       var10000 = var28;
                                       var10001 = false;
                                       break label339;
                                    }
                                 }

                                 try {
                                    TLRPC.TL_messageMediaPoll var66 = (TLRPC.TL_messageMediaPoll)var72.media;
                                    var60.requery();
                                    var60.bindLong(1, var20);
                                    var60.bindLong(2, var66.poll.id);
                                    var60.step();
                                 } catch (Exception var27) {
                                    var10000 = var27;
                                    var10001 = false;
                                    break label339;
                                 }
                              }

                              try {
                                 createFirstHoles(var16.id, var61, var12, var72.id);
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break label339;
                              }

                              var65 = var18;
                           } else {
                              var65 = 0;
                           }

                           SQLitePreparedStatement var67 = var61;

                           long var22;
                           try {
                              var22 = (long)var16.top_message;
                           } catch (Exception var39) {
                              var10000 = var39;
                              var10001 = false;
                              break label316;
                           }

                           var20 = var22;

                           label345: {
                              try {
                                 if (var16.peer == null) {
                                    break label345;
                                 }
                              } catch (Exception var49) {
                                 var10000 = var49;
                                 var10001 = false;
                                 break label316;
                              }

                              var20 = var22;

                              try {
                                 if (var16.peer.channel_id != 0) {
                                    var20 = var22 | (long)var16.peer.channel_id << 32;
                                 }
                              } catch (Exception var38) {
                                 var10000 = var38;
                                 var10001 = false;
                                 break label316;
                              }
                           }

                           label254: {
                              label253: {
                                 try {
                                    var10.requery();
                                    var10.bindLong(1, var16.id);
                                    var10.bindInteger(2, var65);
                                    var10.bindInteger(3, var16.unread_count);
                                    var10.bindLong(4, var20);
                                    var10.bindInteger(5, var16.read_inbox_max_id);
                                    var10.bindInteger(6, var16.read_outbox_max_id);
                                    var10.bindLong(7, 0L);
                                    var10.bindInteger(8, var16.unread_mentions_count);
                                    var10.bindInteger(9, var16.pts);
                                    var10.bindInteger(10, 0);
                                    var10.bindInteger(11, var16.pinnedNum);
                                    if (!var16.unread_mark) {
                                       break label253;
                                    }
                                 } catch (Exception var48) {
                                    var10000 = var48;
                                    var10001 = false;
                                    break label316;
                                 }

                                 var13 = 1;
                                 break label254;
                              }

                              var13 = 0;
                           }

                           NativeByteBuffer var62;
                           label346: {
                              try {
                                 var10.bindInteger(12, var13);
                                 var10.bindInteger(13, var16.folder_id);
                                 if (var16 instanceof TLRPC.TL_dialogFolder) {
                                    TLRPC.TL_dialogFolder var74 = (TLRPC.TL_dialogFolder)var16;
                                    var62 = new NativeByteBuffer(var74.folder.getObjectSize());
                                    var74.folder.serializeToStream(var62);
                                    var10.bindByteBuffer(14, (NativeByteBuffer)var62);
                                    break label346;
                                 }
                              } catch (Exception var47) {
                                 var10000 = var47;
                                 var10001 = false;
                                 break label316;
                              }

                              try {
                                 var10.bindNull(14);
                              } catch (Exception var37) {
                                 var10000 = var37;
                                 var10001 = false;
                                 break label316;
                              }

                              var62 = null;
                           }

                           try {
                              var10.step();
                           } catch (Exception var36) {
                              var10000 = var36;
                              var10001 = false;
                              break label316;
                           }

                           if (var62 != null) {
                              try {
                                 var62.reuse();
                              } catch (Exception var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label316;
                              }
                           }

                           var61 = var61;
                           var70 = var60;

                           try {
                              if (var16.notify_settings == null) {
                                 break label347;
                              }

                              var75.requery();
                              var20 = var16.id;
                           } catch (Exception var45) {
                              var10000 = var45;
                              var10001 = false;
                              break label316;
                           }

                           var13 = 1;

                           label233: {
                              try {
                                 var75.bindLong(1, var20);
                                 if (var16.notify_settings.mute_until != 0) {
                                    break label233;
                                 }
                              } catch (Exception var46) {
                                 var10000 = var46;
                                 var10001 = false;
                                 break label316;
                              }

                              var13 = 0;
                           }

                           try {
                              var75.bindInteger(2, var13);
                              var75.step();
                           } catch (Exception var34) {
                              var10000 = var34;
                              var10001 = false;
                              break label316;
                           }

                           var70 = var60;
                           var61 = var67;
                        }

                        ++var6;
                        var60 = var70;
                     }

                     try {
                        var9.dispose();
                        var10.dispose();
                        var11.dispose();
                        var64.dispose();
                        var61.dispose();
                        var12.dispose();
                     } catch (Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label316;
                     }

                     if (var60 != null) {
                        try {
                           var60.dispose();
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label316;
                        }
                     }
                  }

                  try {
                     var63 = var1.users;
                     break label333;
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                  }
               }

               var59 = var10000;
               break label334;
            }

            var59 = var10000;
            break label334;
         }

         try {
            this.putUsersInternal(var63);
            this.putChatsInternal(var1.chats);
            this.database.commitTransaction();
            return;
         } catch (Exception var24) {
            var59 = var24;
         }
      }

      FileLog.e((Throwable)var59);
   }

   private void putMessagesInternal(ArrayList var1, boolean var2, boolean var3, int var4, boolean var5) {
      Exception var164;
      label1147: {
         Exception var10000;
         label1150: {
            MessagesStorage var6 = this;
            int var10;
            SQLiteCursor var168;
            boolean var10001;
            if (var5) {
               TLRPC.Message var7;
               try {
                  var7 = (TLRPC.Message)var1.get(0);
                  if (var7.dialog_id == 0L) {
                     MessageObject.getDialogId(var7);
                  }
               } catch (Exception var101) {
                  var10000 = var101;
                  var10001 = false;
                  break label1150;
               }

               label1141: {
                  try {
                     SQLiteDatabase var8 = var6.database;
                     StringBuilder var9 = new StringBuilder();
                     var9.append("SELECT last_mid FROM dialogs WHERE did = ");
                     var9.append(var7.dialog_id);
                     var168 = var8.queryFinalized(var9.toString());
                     if (var168.next()) {
                        var10 = var168.intValue(0);
                        break label1141;
                     }
                  } catch (Exception var161) {
                     var10000 = var161;
                     var10001 = false;
                     break label1150;
                  }

                  var10 = -1;
               }

               try {
                  var168.dispose();
               } catch (Exception var100) {
                  var10000 = var100;
                  var10001 = false;
                  break label1150;
               }

               if (var10 != 0) {
                  return;
               }
            }

            if (var2) {
               try {
                  var6.database.beginTransaction();
               } catch (Exception var99) {
                  var10000 = var99;
                  var10001 = false;
                  break label1150;
               }
            }

            LongSparseArray var11;
            LongSparseArray var12;
            LongSparseArray var13;
            StringBuilder var14;
            LongSparseArray var15;
            LongSparseArray var16;
            LongSparseArray var17;
            SQLitePreparedStatement var18;
            SQLitePreparedStatement var19;
            SQLitePreparedStatement var20;
            SQLitePreparedStatement var21;
            LongSparseArray var170;
            try {
               var11 = new LongSparseArray();
               var170 = new LongSparseArray();
               var12 = new LongSparseArray();
               var13 = new LongSparseArray();
               var14 = new StringBuilder();
               var15 = new LongSparseArray();
               var16 = new LongSparseArray();
               var17 = new LongSparseArray();
               var18 = var6.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
               var19 = var6.database.executeFast("REPLACE INTO randoms VALUES(?, ?)");
               var20 = var6.database.executeFast("REPLACE INTO download_queue VALUES(?, ?, ?, ?, ?)");
               var21 = var6.database.executeFast("REPLACE INTO webpage_pending VALUES(?, ?)");
            } catch (Exception var98) {
               var10000 = var98;
               var10001 = false;
               break label1150;
            }

            LongSparseArray var169 = null;
            LongSparseArray var173 = null;
            var10 = 0;
            StringBuilder var22 = null;

            long var24;
            long var26;
            while(true) {
               TLRPC.Message var23;
               try {
                  if (var10 >= var1.size()) {
                     break;
                  }

                  var23 = (TLRPC.Message)var1.get(var10);
                  var24 = (long)var23.id;
                  if (var23.dialog_id == 0L) {
                     MessageObject.getDialogId(var23);
                  }
               } catch (Exception var151) {
                  var10000 = var151;
                  var10001 = false;
                  break label1150;
               }

               var26 = var24;

               try {
                  if (var23.to_id.channel_id != 0) {
                     var26 = var24 | (long)var23.to_id.channel_id << 32;
                  }
               } catch (Exception var97) {
                  var10000 = var97;
                  var10001 = false;
                  break label1150;
               }

               try {
                  if (var23.mentioned && var23.media_unread) {
                     var17.put(var26, var23.dialog_id);
                  }
               } catch (Exception var160) {
                  var10000 = var160;
                  var10001 = false;
                  break label1150;
               }

               try {
                  var5 = var23.action instanceof TLRPC.TL_messageActionHistoryClear;
               } catch (Exception var96) {
                  var10000 = var96;
                  var10001 = false;
                  break label1150;
               }

               if (!var5) {
                  label1203: {
                     try {
                        if (MessageObject.isOut(var23) || var23.id <= 0 && !MessageObject.isUnread(var23)) {
                           break label1203;
                        }
                     } catch (Exception var159) {
                        var10000 = var159;
                        var10001 = false;
                        break label1150;
                     }

                     Integer var28;
                     try {
                        var28 = (Integer)var15.get(var23.dialog_id);
                     } catch (Exception var95) {
                        var10000 = var95;
                        var10001 = false;
                        break label1150;
                     }

                     if (var28 == null) {
                        SQLiteCursor var211;
                        label1108: {
                           try {
                              SQLiteDatabase var210 = var6.database;
                              StringBuilder var29 = new StringBuilder();
                              var29.append("SELECT inbox_max FROM dialogs WHERE did = ");
                              var29.append(var23.dialog_id);
                              var211 = var210.queryFinalized(var29.toString());
                              if (var211.next()) {
                                 var28 = var211.intValue(0);
                                 break label1108;
                              }
                           } catch (Exception var158) {
                              var10000 = var158;
                              var10001 = false;
                              break label1150;
                           }

                           try {
                              var28 = 0;
                           } catch (Exception var94) {
                              var10000 = var94;
                              var10001 = false;
                              break label1150;
                           }
                        }

                        try {
                           var211.dispose();
                           var15.put(var23.dialog_id, var28);
                        } catch (Exception var93) {
                           var10000 = var93;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     try {
                        if (var23.id >= 0 && var28 >= var23.id) {
                           break label1203;
                        }
                     } catch (Exception var157) {
                        var10000 = var157;
                        var10001 = false;
                        break label1150;
                     }

                     try {
                        if (var14.length() > 0) {
                           var14.append(",");
                        }
                     } catch (Exception var156) {
                        var10000 = var156;
                        var10001 = false;
                        break label1150;
                     }

                     try {
                        var14.append(var26);
                        var16.put(var26, var23.dialog_id);
                     } catch (Exception var92) {
                        var10000 = var92;
                        var10001 = false;
                        break label1150;
                     }
                  }
               }

               label1157: {
                  try {
                     if (!DataQuery.canAddMessageToMedia(var23)) {
                        break label1157;
                     }
                  } catch (Exception var155) {
                     var10000 = var155;
                     var10001 = false;
                     break label1150;
                  }

                  if (var22 == null) {
                     try {
                        var22 = new StringBuilder();
                        var169 = new LongSparseArray();
                        var173 = new LongSparseArray();
                     } catch (Exception var91) {
                        var10000 = var91;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  try {
                     if (var22.length() > 0) {
                        var22.append(",");
                     }
                  } catch (Exception var154) {
                     var10000 = var154;
                     var10001 = false;
                     break label1150;
                  }

                  try {
                     var22.append(var26);
                     var169.put(var26, var23.dialog_id);
                     var173.put(var26, DataQuery.getMediaType(var23));
                  } catch (Exception var90) {
                     var10000 = var90;
                     var10001 = false;
                     break label1150;
                  }
               }

               label1158: {
                  TLRPC.Message var212;
                  try {
                     if (!var6.isValidKeyboardToSave(var23)) {
                        break label1158;
                     }

                     var212 = (TLRPC.Message)var13.get(var23.dialog_id);
                  } catch (Exception var153) {
                     var10000 = var153;
                     var10001 = false;
                     break label1150;
                  }

                  if (var212 != null) {
                     try {
                        if (var212.id >= var23.id) {
                           break label1158;
                        }
                     } catch (Exception var152) {
                        var10000 = var152;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  try {
                     var13.put(var23.dialog_id, var23);
                  } catch (Exception var89) {
                     var10000 = var89;
                     var10001 = false;
                     break label1150;
                  }
               }

               ++var10;
            }

            LongSparseArray var205 = var173;
            var173 = var12;
            var12 = var17;
            LongSparseArray var213 = var169;
            var10 = 0;

            while(true) {
               try {
                  if (var10 >= var13.size()) {
                     break;
                  }

                  DataQuery.getInstance(var6.currentAccount).putBotKeyboard(var13.keyAt(var10), (TLRPC.Message)var13.valueAt(var10));
               } catch (Exception var150) {
                  var10000 = var150;
                  var10001 = false;
                  break label1150;
               }

               ++var10;
            }

            int var30;
            Integer var196;
            LongSparseArray var206;
            SparseArray var214;
            if (var22 != null) {
               SQLiteCursor var204;
               try {
                  SQLiteDatabase var171 = var6.database;
                  StringBuilder var195 = new StringBuilder();
                  var195.append("SELECT mid, type FROM media_v2 WHERE mid IN(");
                  var195.append(var22.toString());
                  var195.append(")");
                  var204 = var171.queryFinalized(var195.toString());
               } catch (Exception var88) {
                  var10000 = var88;
                  var10001 = false;
                  break label1150;
               }

               var169 = null;
               var17 = var205;

               label1048:
               while(true) {
                  while(true) {
                     try {
                        if (!var204.next()) {
                           break label1048;
                        }

                        var26 = var204.longValue(0);
                        var10 = var204.intValue(1);
                        if (var10 == (Integer)var17.get(var26)) {
                           var213.remove(var26);
                           continue;
                        }
                     } catch (Exception var149) {
                        var10000 = var149;
                        var10001 = false;
                        break label1150;
                     }

                     var11 = var169;
                     if (var169 == null) {
                        try {
                           var11 = new LongSparseArray();
                        } catch (Exception var87) {
                           var10000 = var87;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     try {
                        var11.put(var26, var10);
                     } catch (Exception var86) {
                        var10000 = var86;
                        var10001 = false;
                        break label1150;
                     }

                     var169 = var11;
                  }
               }

               SparseArray var182;
               try {
                  var204.dispose();
                  var182 = new SparseArray();
               } catch (Exception var85) {
                  var10000 = var85;
                  var10001 = false;
                  break label1150;
               }

               var10 = 0;
               var11 = var17;
               var206 = var213;

               while(true) {
                  Integer var207;
                  try {
                     if (var10 >= var206.size()) {
                        break;
                     }

                     var26 = var206.keyAt(var10);
                     var24 = (Long)var206.valueAt(var10);
                     var207 = (Integer)var11.get(var26);
                     var213 = (LongSparseArray)var182.get(var207);
                  } catch (Exception var148) {
                     var10000 = var148;
                     var10001 = false;
                     break label1150;
                  }

                  if (var213 == null) {
                     try {
                        var213 = new LongSparseArray();
                        var196 = 0;
                        var182.put(var207, var213);
                     } catch (Exception var84) {
                        var10000 = var84;
                        var10001 = false;
                        break label1150;
                     }
                  } else {
                     try {
                        var196 = (Integer)var213.get(var24);
                     } catch (Exception var83) {
                        var10000 = var83;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  var207 = var196;
                  if (var196 == null) {
                     try {
                        var207 = 0;
                     } catch (Exception var82) {
                        var10000 = var82;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  try {
                     var213.put(var24, var207 + 1);
                  } catch (Exception var81) {
                     var10000 = var81;
                     var10001 = false;
                     break label1150;
                  }

                  if (var169 != null) {
                     try {
                        var30 = (Integer)var169.get(var26, -1);
                     } catch (Exception var80) {
                        var10000 = var80;
                        var10001 = false;
                        break label1150;
                     }

                     if (var30 >= 0) {
                        try {
                           var213 = (LongSparseArray)var182.get(var30);
                        } catch (Exception var79) {
                           var10000 = var79;
                           var10001 = false;
                           break label1150;
                        }

                        if (var213 == null) {
                           try {
                              var213 = new LongSparseArray();
                              var196 = 0;
                              var182.put(var30, var213);
                           } catch (Exception var78) {
                              var10000 = var78;
                              var10001 = false;
                              break label1150;
                           }
                        } else {
                           try {
                              var196 = (Integer)var213.get(var24);
                           } catch (Exception var77) {
                              var10000 = var77;
                              var10001 = false;
                              break label1150;
                           }
                        }

                        var207 = var196;
                        if (var196 == null) {
                           try {
                              var207 = 0;
                           } catch (Exception var76) {
                              var10000 = var76;
                              var10001 = false;
                              break label1150;
                           }
                        }

                        try {
                           var213.put(var24, var207 - 1);
                        } catch (Exception var75) {
                           var10000 = var75;
                           var10001 = false;
                           break label1150;
                        }
                     }
                  }

                  ++var10;
               }

               var214 = var182;
            } else {
               var214 = null;
            }

            SQLiteDatabase var199;
            label1027: {
               label1164: {
                  try {
                     if (var14.length() <= 0) {
                        break label1164;
                     }

                     var199 = var6.database;
                     StringBuilder var174 = new StringBuilder();
                     var174.append("SELECT mid FROM messages WHERE mid IN(");
                     var174.append(var14.toString());
                     var174.append(")");
                     var168 = var199.queryFinalized(var174.toString());
                  } catch (Exception var147) {
                     var10000 = var147;
                     var10001 = false;
                     break label1150;
                  }

                  while(true) {
                     try {
                        if (!var168.next()) {
                           break;
                        }

                        var26 = var168.longValue(0);
                        var16.remove(var26);
                        var12.remove(var26);
                     } catch (Exception var146) {
                        var10000 = var146;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  try {
                     var168.dispose();
                  } catch (Exception var74) {
                     var10000 = var74;
                     var10001 = false;
                     break label1150;
                  }

                  var10 = 0;

                  Integer var175;
                  while(true) {
                     try {
                        if (var10 >= var16.size()) {
                           break;
                        }

                        var26 = (Long)var16.valueAt(var10);
                        var196 = (Integer)var170.get(var26);
                     } catch (Exception var145) {
                        var10000 = var145;
                        var10001 = false;
                        break label1150;
                     }

                     var175 = var196;
                     if (var196 == null) {
                        try {
                           var175 = 0;
                        } catch (Exception var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     try {
                        var170.put(var26, var175 + 1);
                     } catch (Exception var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label1150;
                     }

                     ++var10;
                  }

                  var10 = 0;

                  while(true) {
                     var206 = var170;
                     var169 = var173;

                     try {
                        if (var10 >= var12.size()) {
                           break label1027;
                        }

                        var26 = (Long)var12.valueAt(var10);
                        var196 = (Integer)var173.get(var26);
                     } catch (Exception var144) {
                        var10000 = var144;
                        var10001 = false;
                        break label1150;
                     }

                     var175 = var196;
                     if (var196 == null) {
                        try {
                           var175 = 0;
                        } catch (Exception var71) {
                           var10000 = var71;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     try {
                        var173.put(var26, var175 + 1);
                     } catch (Exception var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label1150;
                     }

                     ++var10;
                  }
               }

               var169 = var173;
               var206 = var170;
            }

            int var31 = 0;
            SQLitePreparedStatement var172 = null;
            var13 = null;
            var30 = 0;
            SQLitePreparedStatement var209 = var18;
            var173 = var11;
            SQLitePreparedStatement var179 = var19;
            SQLitePreparedStatement var201 = var21;
            var18 = var20;
            SQLitePreparedStatement var181 = var13;

            while(true) {
               TLRPC.Message var198;
               try {
                  if (var31 >= var1.size()) {
                     break;
                  }

                  var198 = (TLRPC.Message)var1.get(var31);
                  var6.fixUnsupportedMedia(var198);
                  var209.requery();
                  var26 = (long)var198.id;
               } catch (Exception var123) {
                  var10000 = var123;
                  var10001 = false;
                  break label1150;
               }

               try {
                  if (var198.local_id != 0) {
                     var26 = (long)var198.local_id;
                  }
               } catch (Exception var143) {
                  var10000 = var143;
                  var10001 = false;
                  break label1150;
               }

               var24 = var26;

               try {
                  if (var198.to_id.channel_id != 0) {
                     var24 = var26 | (long)var198.to_id.channel_id << 32;
                  }
               } catch (Exception var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label1150;
               }

               boolean var191;
               NativeByteBuffer var192;
               label986: {
                  label985: {
                     try {
                        var192 = new NativeByteBuffer(var198.getObjectSize());
                        var198.serializeToStream(var192);
                        if (var198.action instanceof TLRPC.TL_messageEncryptedAction && !(var198.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) && !(var198.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages)) {
                           break label985;
                        }
                     } catch (Exception var142) {
                        var10000 = var142;
                        var10001 = false;
                        break label1150;
                     }

                     var191 = true;
                     break label986;
                  }

                  var191 = false;
               }

               if (var191) {
                  label1168: {
                     try {
                        var26 = var198.dialog_id;
                     } catch (Exception var68) {
                        var10000 = var68;
                        var10001 = false;
                        break label1150;
                     }

                     LongSparseArray var200 = var173;

                     TLRPC.Message var202;
                     try {
                        var202 = (TLRPC.Message)var200.get(var26);
                     } catch (Exception var67) {
                        var10000 = var67;
                        var10001 = false;
                        break label1150;
                     }

                     if (var202 != null) {
                        label971: {
                           try {
                              if (var198.date > var202.date || var202.id > 0 && var198.id > var202.id) {
                                 break label971;
                              }
                           } catch (Exception var141) {
                              var10000 = var141;
                              var10001 = false;
                              break label1150;
                           }

                           try {
                              if (var202.id >= 0 || var198.id >= var202.id) {
                                 break label1168;
                              }
                           } catch (Exception var140) {
                              var10000 = var140;
                              var10001 = false;
                              break label1150;
                           }
                        }
                     }

                     try {
                        var200.put(var198.dialog_id, var198);
                     } catch (Exception var66) {
                        var10000 = var66;
                        var10001 = false;
                        break label1150;
                     }
                  }
               }

               byte var193;
               label956: {
                  label955: {
                     try {
                        var209.bindLong(1, var24);
                        var209.bindLong(2, var198.dialog_id);
                        var209.bindInteger(3, MessageObject.getUnreadFlags(var198));
                        var209.bindInteger(4, var198.send_state);
                        var209.bindInteger(5, var198.date);
                        var209.bindByteBuffer(6, (NativeByteBuffer)var192);
                        if (MessageObject.isOut(var198)) {
                           break label955;
                        }
                     } catch (Exception var139) {
                        var10000 = var139;
                        var10001 = false;
                        break label1150;
                     }

                     var193 = 0;
                     break label956;
                  }

                  var193 = 1;
               }

               label948: {
                  try {
                     var209.bindInteger(7, var193);
                     var209.bindInteger(8, var198.ttl);
                     if ((var198.flags & 1024) != 0) {
                        var209.bindInteger(9, var198.views);
                        break label948;
                     }
                  } catch (Exception var138) {
                     var10000 = var138;
                     var10001 = false;
                     break label1150;
                  }

                  try {
                     var209.bindInteger(9, var6.getMessageMediaType(var198));
                  } catch (Exception var65) {
                     var10000 = var65;
                     var10001 = false;
                     break label1150;
                  }
               }

               label940: {
                  label939: {
                     try {
                        var209.bindInteger(10, 0);
                        if (var198.mentioned) {
                           break label939;
                        }
                     } catch (Exception var137) {
                        var10000 = var137;
                        var10001 = false;
                        break label1150;
                     }

                     var193 = 0;
                     break label940;
                  }

                  var193 = 1;
               }

               label1170: {
                  try {
                     var209.bindInteger(11, var193);
                     var209.step();
                     if (var198.random_id == 0L) {
                        break label1170;
                     }

                     var179.requery();
                     var26 = var198.random_id;
                  } catch (Exception var136) {
                     var10000 = var136;
                     var10001 = false;
                     break label1150;
                  }

                  var20 = var179;

                  try {
                     var20.bindLong(1, var26);
                     var20.bindLong(2, var24);
                     var20.step();
                  } catch (Exception var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label1150;
                  }
               }

               var20 = var172;

               label1171: {
                  try {
                     if (!DataQuery.canAddMessageToMedia(var198)) {
                        break label1171;
                     }
                  } catch (Exception var135) {
                     var10000 = var135;
                     var10001 = false;
                     break label1150;
                  }

                  var20 = var172;
                  if (var172 == null) {
                     try {
                        var20 = var6.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                     } catch (Exception var63) {
                        var10000 = var63;
                        var10001 = false;
                        break label1150;
                     }
                  }

                  try {
                     var20.requery();
                     var20.bindLong(1, var24);
                     var20.bindLong(2, var198.dialog_id);
                     var20.bindInteger(3, var198.date);
                     var20.bindInteger(4, DataQuery.getMediaType(var198));
                     var20.bindByteBuffer(5, (NativeByteBuffer)var192);
                     var20.step();
                  } catch (Exception var62) {
                     var10000 = var62;
                     var10001 = false;
                     break label1150;
                  }
               }

               label1172: {
                  label920: {
                     label1173: {
                        label1174: {
                           try {
                              if (!(var198.media instanceof TLRPC.TL_messageMediaPoll)) {
                                 break label1174;
                              }
                           } catch (Exception var134) {
                              var10000 = var134;
                              var10001 = false;
                              break label1150;
                           }

                           var172 = var181;
                           if (var181 == null) {
                              try {
                                 var172 = var6.database.executeFast("REPLACE INTO polls VALUES(?, ?)");
                              } catch (Exception var61) {
                                 var10000 = var61;
                                 var10001 = false;
                                 break label1150;
                              }
                           }

                           try {
                              TLRPC.TL_messageMediaPoll var187 = (TLRPC.TL_messageMediaPoll)var198.media;
                              var172.requery();
                              var172.bindLong(1, var24);
                              var172.bindLong(2, var187.poll.id);
                              var172.step();
                              break label1173;
                           } catch (Exception var60) {
                              var10000 = var60;
                              var10001 = false;
                              break label1150;
                           }
                        }

                        var172 = var181;

                        try {
                           if (var198.media instanceof TLRPC.TL_messageMediaWebPage) {
                              var201.requery();
                              var26 = var198.media.webpage.id;
                              break label920;
                           }
                        } catch (Exception var133) {
                           var10000 = var133;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     var21 = var172;
                     break label1172;
                  }

                  var172 = var201;

                  try {
                     var172.bindLong(1, var26);
                     var172.bindLong(2, var24);
                     var172.step();
                  } catch (Exception var59) {
                     var10000 = var59;
                     var10001 = false;
                     break label1150;
                  }

                  var21 = var181;
               }

               try {
                  var192.reuse();
               } catch (Exception var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label1150;
               }

               if (var4 != 0) {
                  label1175: {
                     try {
                        if (var198.to_id.channel_id != 0 && !var198.post) {
                           break label1175;
                        }
                     } catch (Exception var132) {
                        var10000 = var132;
                        var10001 = false;
                        break label1150;
                     }

                     try {
                        if (var198.date < ConnectionsManager.getInstance(var6.currentAccount).getCurrentTime() - 3600 || DownloadController.getInstance(var6.currentAccount).canDownloadMedia(var198) != 1 || !(var198.media instanceof TLRPC.TL_messageMediaPhoto) && !(var198.media instanceof TLRPC.TL_messageMediaDocument) && !(var198.media instanceof TLRPC.TL_messageMediaWebPage)) {
                           break label1175;
                        }
                     } catch (Exception var131) {
                        var10000 = var131;
                        var10001 = false;
                        break label1150;
                     }

                     Object var190;
                     label885: {
                        label1177: {
                           TLRPC.Document var176;
                           TLRPC.Photo var189;
                           try {
                              var176 = MessageObject.getDocument(var198);
                              var189 = MessageObject.getPhoto(var198);
                              if (MessageObject.isVoiceMessage(var198)) {
                                 var26 = var176.id;
                                 var190 = new TLRPC.TL_messageMediaDocument();
                                 ((TLRPC.MessageMedia)var190).document = var176;
                                 ((TLRPC.MessageMedia)var190).flags |= 1;
                                 break label1177;
                              }
                           } catch (Exception var130) {
                              var10000 = var130;
                              var10001 = false;
                              break label1150;
                           }

                           label1178: {
                              try {
                                 if (MessageObject.isStickerMessage(var198)) {
                                    var26 = var176.id;
                                    var190 = new TLRPC.TL_messageMediaDocument();
                                    ((TLRPC.MessageMedia)var190).document = var176;
                                    ((TLRPC.MessageMedia)var190).flags |= 1;
                                    break label1178;
                                 }
                              } catch (Exception var127) {
                                 var10000 = var127;
                                 var10001 = false;
                                 break label1150;
                              }

                              label1196: {
                                 try {
                                    if (!MessageObject.isVideoMessage(var198) && !MessageObject.isRoundVideoMessage(var198) && !MessageObject.isGifMessage(var198)) {
                                       break label1196;
                                    }
                                 } catch (Exception var129) {
                                    var10000 = var129;
                                    var10001 = false;
                                    break label1150;
                                 }

                                 try {
                                    var26 = var176.id;
                                    var190 = new TLRPC.TL_messageMediaDocument();
                                    ((TLRPC.MessageMedia)var190).document = var176;
                                    ((TLRPC.MessageMedia)var190).flags |= 1;
                                 } catch (Exception var56) {
                                    var10000 = var56;
                                    var10001 = false;
                                    break label1150;
                                 }

                                 var193 = 4;
                                 break label885;
                              }

                              if (var176 != null) {
                                 try {
                                    var26 = var176.id;
                                    var190 = new TLRPC.TL_messageMediaDocument();
                                    ((TLRPC.MessageMedia)var190).document = var176;
                                    ((TLRPC.MessageMedia)var190).flags |= 1;
                                 } catch (Exception var57) {
                                    var10000 = var57;
                                    var10001 = false;
                                    break label1150;
                                 }

                                 var193 = 8;
                                 break label885;
                              }

                              TLRPC.TL_messageMediaPhoto var177;
                              label862: {
                                 if (var189 != null) {
                                    try {
                                       if (FileLoader.getClosestPhotoSizeWithSize(var189.sizes, AndroidUtilities.getPhotoSize()) != null) {
                                          var24 = var189.id;
                                          var177 = new TLRPC.TL_messageMediaPhoto();
                                          var177.photo = var189;
                                          var177.flags |= 1;
                                          break label862;
                                       }
                                    } catch (Exception var128) {
                                       var10000 = var128;
                                       var10001 = false;
                                       break label1150;
                                    }
                                 }

                                 var193 = 0;
                                 var26 = 0L;
                                 var190 = null;
                                 break label885;
                              }

                              var26 = var24;
                              var190 = var177;

                              try {
                                 if (!(var198.media instanceof TLRPC.TL_messageMediaWebPage)) {
                                    break label1178;
                                 }

                                 var177.flags |= Integer.MIN_VALUE;
                              } catch (Exception var126) {
                                 var10000 = var126;
                                 var10001 = false;
                                 break label1150;
                              }

                              var26 = var24;
                              var190 = var177;
                           }

                           var193 = 1;
                           break label885;
                        }

                        var193 = 2;
                     }

                     if (var190 != null) {
                        try {
                           if (var198.media.ttl_seconds != 0) {
                              ((TLRPC.MessageMedia)var190).ttl_seconds = var198.media.ttl_seconds;
                              ((TLRPC.MessageMedia)var190).flags |= 4;
                           }
                        } catch (Exception var125) {
                           var10000 = var125;
                           var10001 = false;
                           break label1150;
                        }

                        var30 |= var193;

                        NativeByteBuffer var178;
                        try {
                           var18.requery();
                           var178 = new NativeByteBuffer(((TLObject)var190).getObjectSize());
                           ((TLObject)var190).serializeToStream(var178);
                        } catch (Exception var55) {
                           var10000 = var55;
                           var10001 = false;
                           break label1150;
                        }

                        var181 = var18;

                        StringBuilder var194;
                        label835: {
                           try {
                              var181.bindLong(1, var26);
                              var181.bindInteger(2, var193);
                              var181.bindInteger(3, var198.date);
                              var181.bindByteBuffer(4, (NativeByteBuffer)var178);
                              var194 = new StringBuilder();
                              var194.append("sent_");
                              if (var198.to_id != null) {
                                 var10 = var198.to_id.channel_id;
                                 break label835;
                              }
                           } catch (Exception var124) {
                              var10000 = var124;
                              var10001 = false;
                              break label1150;
                           }

                           var10 = 0;
                        }

                        try {
                           var194.append(var10);
                           var194.append("_");
                           var194.append(var198.id);
                           var181.bindString(5, var194.toString());
                           var181.step();
                           var178.reuse();
                        } catch (Exception var54) {
                           var10000 = var54;
                           var10001 = false;
                           break label1150;
                        }
                     }
                  }
               }

               ++var31;
               var172 = var20;
               var181 = var21;
            }

            LongSparseArray var162 = var173;
            var173 = var206;

            try {
               var209.dispose();
            } catch (Exception var53) {
               var10000 = var53;
               var10001 = false;
               break label1150;
            }

            if (var172 != null) {
               try {
                  var172.dispose();
               } catch (Exception var52) {
                  var10000 = var52;
                  var10001 = false;
                  break label1150;
               }
            }

            if (var181 != null) {
               try {
                  var181.dispose();
               } catch (Exception var51) {
                  var10000 = var51;
                  var10001 = false;
                  break label1150;
               }
            }

            try {
               var179.dispose();
               var18.dispose();
               var201.dispose();
               var179 = var6.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
               var18 = var6.database.executeFast("UPDATE dialogs SET date = ?, unread_count = ?, last_mid = ?, unread_count_i = ? WHERE did = ?");
            } catch (Exception var50) {
               var10000 = var50;
               var10001 = false;
               break label1150;
            }

            var10 = 0;
            var169 = var169;
            var170 = var162;
            SQLitePreparedStatement var163 = var179;

            int var34;
            int var35;
            int var36;
            while(true) {
               MessagesStorage var183 = this;

               try {
                  var4 = var170.size();
               } catch (Exception var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label1150;
               }

               if (var10 < var4) {
                  label1183: {
                     long var32;
                     try {
                        var32 = var170.keyAt(var10);
                     } catch (Exception var121) {
                        var10000 = var121;
                        var10001 = false;
                        break label1183;
                     }

                     if (var32 != 0L) {
                        TLRPC.Message var208;
                        try {
                           var208 = (TLRPC.Message)var170.valueAt(var10);
                        } catch (Exception var120) {
                           var10000 = var120;
                           var10001 = false;
                           break label1183;
                        }

                        if (var208 != null) {
                           try {
                              var31 = var208.to_id.channel_id;
                           } catch (Exception var49) {
                              var10000 = var49;
                              var10001 = false;
                              break label1150;
                           }
                        } else {
                           var31 = 0;
                        }

                        SQLiteCursor var203;
                        try {
                           var199 = var183.database;
                           StringBuilder var184 = new StringBuilder();
                           var184.append("SELECT date, unread_count, last_mid, unread_count_i FROM dialogs WHERE did = ");
                           var184.append(var32);
                           var203 = var199.queryFinalized(var184.toString());
                           var5 = var203.next();
                        } catch (Exception var119) {
                           var10000 = var119;
                           var10001 = false;
                           break label1183;
                        }

                        if (var5) {
                           try {
                              var4 = var203.intValue(0);
                              var34 = Math.max(0, var203.intValue(1));
                              var35 = var203.intValue(2);
                              var36 = Math.max(0, var203.intValue(3));
                           } catch (Exception var48) {
                              var10000 = var48;
                              var10001 = false;
                              break label1150;
                           }
                        } else {
                           if (var31 != 0) {
                              try {
                                 MessagesController.getInstance(var183.currentAccount).checkChannelInviter(var31);
                              } catch (Exception var47) {
                                 var10000 = var47;
                                 var10001 = false;
                                 break label1150;
                              }
                           }

                           var34 = 0;
                           var36 = 0;
                           var35 = 0;
                           var4 = 0;
                        }

                        try {
                           var203.dispose();
                        } catch (Exception var118) {
                           var10000 = var118;
                           var10001 = false;
                           break label1183;
                        }

                        var17 = var169;

                        Integer var185;
                        Integer var186;
                        try {
                           var185 = (Integer)var17.get(var32);
                           var186 = (Integer)var173.get(var32);
                        } catch (Exception var117) {
                           var10000 = var117;
                           var10001 = false;
                           break label1183;
                        }

                        if (var186 == null) {
                           try {
                              var186 = 0;
                           } catch (Exception var46) {
                              var10000 = var46;
                              var10001 = false;
                              break label1150;
                           }
                        } else {
                           try {
                              var173.put(var32, var186 + var34);
                           } catch (Exception var116) {
                              var10000 = var116;
                              var10001 = false;
                              break label1183;
                           }
                        }

                        if (var185 == null) {
                           try {
                              var196 = 0;
                           } catch (Exception var115) {
                              var10000 = var115;
                              var10001 = false;
                              break label1183;
                           }
                        } else {
                           try {
                              var17.put(var32, var185 + var36);
                           } catch (Exception var114) {
                              var10000 = var114;
                              var10001 = false;
                              break label1183;
                           }

                           var196 = var185;
                        }

                        if (var208 != null) {
                           try {
                              var24 = (long)var208.id;
                           } catch (Exception var113) {
                              var10000 = var113;
                              var10001 = false;
                              break label1183;
                           }
                        } else {
                           var24 = (long)var35;
                        }

                        var26 = var24;
                        if (var208 != null) {
                           var26 = var24;

                           try {
                              if (var208.local_id != 0) {
                                 var26 = (long)var208.local_id;
                              }
                           } catch (Exception var112) {
                              var10000 = var112;
                              var10001 = false;
                              break label1183;
                           }
                        }

                        if (var31 != 0) {
                           var26 |= (long)var31 << 32;
                        }

                        if (var5) {
                           try {
                              var18.requery();
                           } catch (Exception var111) {
                              var10000 = var111;
                              var10001 = false;
                              break label1183;
                           }

                           var31 = var4;
                           if (var208 != null) {
                              label1201: {
                                 if (var3) {
                                    var31 = var4;
                                    if (var4 != 0) {
                                       break label1201;
                                    }
                                 }

                                 try {
                                    var31 = var208.date;
                                 } catch (Exception var110) {
                                    var10000 = var110;
                                    var10001 = false;
                                    break label1183;
                                 }
                              }
                           }

                           try {
                              var18.bindInteger(1, var31);
                              var18.bindInteger(2, var34 + var186);
                              var18.bindLong(3, var26);
                              var18.bindInteger(4, var36 + var196);
                              var18.bindLong(5, var32);
                              var18.step();
                           } catch (Exception var109) {
                              var10000 = var109;
                              var10001 = false;
                              break label1183;
                           }
                        } else {
                           try {
                              var163.requery();
                           } catch (Exception var108) {
                              var10000 = var108;
                              var10001 = false;
                              break label1183;
                           }

                           try {
                              var163.bindLong(1, var32);
                           } catch (Exception var107) {
                              var10000 = var107;
                              var10001 = false;
                              break label1183;
                           }

                           var35 = var4;
                           if (var208 != null) {
                              label1202: {
                                 if (var3) {
                                    var35 = var4;
                                    if (var4 != 0) {
                                       break label1202;
                                    }
                                 }

                                 try {
                                    var35 = var208.date;
                                 } catch (Exception var106) {
                                    var10000 = var106;
                                    var10001 = false;
                                    break label1183;
                                 }
                              }
                           }

                           try {
                              var163.bindInteger(2, var35);
                              var163.bindInteger(3, var34 + var186);
                              var163.bindLong(4, var26);
                              var163.bindInteger(5, 0);
                              var163.bindInteger(6, 0);
                              var163.bindLong(7, 0L);
                              var163.bindInteger(8, var36 + var196);
                           } catch (Exception var105) {
                              var10000 = var105;
                              var10001 = false;
                              break label1183;
                           }

                           byte var167;
                           if (var31 != 0) {
                              var167 = 1;
                           } else {
                              var167 = 0;
                           }

                           try {
                              var163.bindInteger(9, var167);
                              var163.bindInteger(10, 0);
                              var163.bindInteger(11, 0);
                              var163.bindInteger(12, 0);
                              var163.bindInteger(13, 0);
                              var163.bindNull(14);
                              var163.step();
                           } catch (Exception var104) {
                              var10000 = var104;
                              var10001 = false;
                              break label1183;
                           }
                        }
                     }

                     ++var10;
                     continue;
                  }
               } else {
                  try {
                     var18.dispose();
                     var163.dispose();
                     break;
                  } catch (Exception var122) {
                     var10000 = var122;
                     var10001 = false;
                  }
               }

               var164 = var10000;
               break label1147;
            }

            if (var214 != null) {
               MessagesStorage var180 = this;

               try {
                  var179 = var180.database.executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
               } catch (Exception var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label1150;
               }

               var4 = 0;
               SparseArray var165 = var214;

               while(true) {
                  try {
                     if (var4 >= var165.size()) {
                        break;
                     }
                  } catch (Exception var103) {
                     var10000 = var103;
                     var10001 = false;
                     break label1150;
                  }

                  try {
                     var36 = var165.keyAt(var4);
                     var17 = (LongSparseArray)var165.valueAt(var4);
                  } catch (Exception var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label1150;
                  }

                  var10 = 0;

                  while(true) {
                     SQLiteCursor var188;
                     label739: {
                        try {
                           if (var10 >= var17.size()) {
                              break;
                           }

                           var26 = var17.keyAt(var10);
                           var188 = var180.database.queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", var26, var36));
                           if (var188.next()) {
                              var34 = var188.intValue(0);
                              var31 = var188.intValue(1);
                              break label739;
                           }
                        } catch (Exception var102) {
                           var10000 = var102;
                           var10001 = false;
                           break label1150;
                        }

                        var34 = -1;
                        var31 = 0;
                     }

                     try {
                        var188.dispose();
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label1150;
                     }

                     if (var34 != -1) {
                        try {
                           var179.requery();
                           var35 = (Integer)var17.valueAt(var10);
                           var179.bindLong(1, var26);
                           var179.bindInteger(2, var36);
                           var179.bindInteger(3, Math.max(0, var34 + var35));
                           var179.bindInteger(4, var31);
                           var179.step();
                        } catch (Exception var41) {
                           var10000 = var41;
                           var10001 = false;
                           break label1150;
                        }
                     }

                     ++var10;
                  }

                  ++var4;
               }

               try {
                  var179.dispose();
               } catch (Exception var40) {
                  var10000 = var40;
                  var10001 = false;
                  break label1150;
               }
            }

            MessagesStorage var166 = this;
            if (var2) {
               try {
                  var166.database.commitTransaction();
               } catch (Exception var39) {
                  var10000 = var39;
                  var10001 = false;
                  break label1150;
               }
            }

            try {
               MessagesController.getInstance(var166.currentAccount).processDialogsUpdateRead(var173, var169);
            } catch (Exception var38) {
               var10000 = var38;
               var10001 = false;
               break label1150;
            }

            if (var30 == 0) {
               return;
            }

            try {
               _$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM var197 = new _$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM(var166, var30);
               AndroidUtilities.runOnUIThread(var197);
               return;
            } catch (Exception var37) {
               var10000 = var37;
               var10001 = false;
            }
         }

         var164 = var10000;
      }

      FileLog.e((Throwable)var164);
   }

   private void putUsersAndChatsInternal(ArrayList var1, ArrayList var2, boolean var3) {
      Exception var10000;
      label33: {
         boolean var10001;
         if (var3) {
            try {
               this.database.beginTransaction();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label33;
            }
         }

         try {
            this.putUsersInternal(var1);
            this.putChatsInternal(var2);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label33;
         }

         if (!var3) {
            return;
         }

         try {
            this.database.commitTransaction();
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var7 = var10000;
      FileLog.e((Throwable)var7);
   }

   private void putUsersInternal(ArrayList var1) throws Exception {
      if (var1 != null && !var1.isEmpty()) {
         SQLitePreparedStatement var2 = this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            TLRPC.User var4 = (TLRPC.User)var1.get(var3);
            TLRPC.User var5 = var4;
            if (var4.min) {
               SQLiteCursor var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM users WHERE uid = %d", var4.id));
               var5 = var4;
               if (var6.next()) {
                  label91: {
                     TLRPC.User var8;
                     label90: {
                        Exception var10000;
                        label105: {
                           NativeByteBuffer var7;
                           boolean var10001;
                           try {
                              var7 = var6.byteBufferValue(0);
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label105;
                           }

                           var5 = var4;
                           if (var7 == null) {
                              break label91;
                           }

                           try {
                              var8 = TLRPC.User.TLdeserialize(var7, var7.readInt32(false), false);
                              var7.reuse();
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label105;
                           }

                           var5 = var4;
                           if (var8 == null) {
                              break label91;
                           }

                           label108: {
                              try {
                                 if (var4.username != null) {
                                    var8.username = var4.username;
                                    var8.flags |= 8;
                                    break label108;
                                 }
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label105;
                              }

                              try {
                                 var8.username = null;
                                 var8.flags &= -9;
                              } catch (Exception var10) {
                                 var10000 = var10;
                                 var10001 = false;
                                 break label105;
                              }
                           }

                           try {
                              if (var4.photo != null) {
                                 var8.photo = var4.photo;
                                 var8.flags |= 32;
                                 break label90;
                              }
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label105;
                           }

                           try {
                              var8.photo = null;
                              var8.flags &= -33;
                              break label90;
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                           }
                        }

                        Exception var16 = var10000;
                        FileLog.e((Throwable)var16);
                        var5 = var4;
                        break label91;
                     }

                     var5 = var8;
                  }
               }

               var6.dispose();
            }

            var2.requery();
            NativeByteBuffer var17 = new NativeByteBuffer(var5.getObjectSize());
            var5.serializeToStream(var17);
            var2.bindInteger(1, var5.id);
            var2.bindString(2, this.formatUserSearchName(var5));
            TLRPC.UserStatus var15 = var5.status;
            if (var15 != null) {
               if (var15 instanceof TLRPC.TL_userStatusRecently) {
                  var15.expires = -100;
               } else if (var15 instanceof TLRPC.TL_userStatusLastWeek) {
                  var15.expires = -101;
               } else if (var15 instanceof TLRPC.TL_userStatusLastMonth) {
                  var15.expires = -102;
               }

               var2.bindInteger(3, var5.status.expires);
            } else {
               var2.bindInteger(3, 0);
            }

            var2.bindByteBuffer(4, (NativeByteBuffer)var17);
            var2.step();
            var17.reuse();
         }

         var2.dispose();
      }

   }

   private void saveDiffParamsInternal(int var1, int var2, int var3, int var4) {
      try {
         if (this.lastSavedSeq == var1 && this.lastSavedPts == var2 && this.lastSavedDate == var3 && this.lastQtsValue == var4) {
            return;
         }

         SQLitePreparedStatement var5 = this.database.executeFast("UPDATE params SET seq = ?, pts = ?, date = ?, qts = ? WHERE id = 1");
         var5.bindInteger(1, var1);
         var5.bindInteger(2, var2);
         var5.bindInteger(3, var3);
         var5.bindInteger(4, var4);
         var5.step();
         var5.dispose();
         this.lastSavedSeq = var1;
         this.lastSavedPts = var2;
         this.lastSavedDate = var3;
         this.lastSavedQts = var4;
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   private void updateDbToLastVersion(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$UniaOWI4GiF_wLDJANnyTaeI1I0(this, var1));
   }

   private void updateDialogsWithDeletedMessagesInternal(ArrayList var1, ArrayList var2, int var3) {
      if (Thread.currentThread().getId() == this.storageQueue.getId()) {
         Exception var10000;
         label319: {
            ArrayList var4;
            boolean var5;
            boolean var10001;
            try {
               var4 = new ArrayList();
               var5 = var1.isEmpty();
            } catch (Exception var49) {
               var10000 = var49;
               var10001 = false;
               break label319;
            }

            long var6;
            int var8;
            String var50;
            if (!var5) {
               SQLitePreparedStatement var52;
               if (var3 != 0) {
                  var6 = (long)(-var3);

                  try {
                     var4.add(var6);
                     var52 = this.database.executeFast("UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ?)) WHERE did = ?");
                  } catch (Exception var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label319;
                  }
               } else {
                  SQLiteCursor var51;
                  try {
                     var50 = TextUtils.join(",", var1);
                     var51 = this.database.queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs WHERE last_mid IN(%s)", var50));
                  } catch (Exception var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label319;
                  }

                  while(true) {
                     try {
                        if (!var51.next()) {
                           break;
                        }

                        var4.add(var51.longValue(0));
                     } catch (Exception var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label319;
                     }
                  }

                  try {
                     var51.dispose();
                     var52 = this.database.executeFast("UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ? AND date != 0)) WHERE did = ?");
                  } catch (Exception var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label319;
                  }
               }

               try {
                  this.database.beginTransaction();
               } catch (Exception var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label319;
               }

               var8 = 0;

               while(true) {
                  try {
                     if (var8 >= var4.size()) {
                        break;
                     }

                     var6 = (Long)var4.get(var8);
                     var52.requery();
                     var52.bindLong(1, var6);
                     var52.bindLong(2, var6);
                     var52.bindLong(3, var6);
                     var52.step();
                  } catch (Exception var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label319;
                  }

                  ++var8;
               }

               try {
                  var52.dispose();
                  this.database.commitTransaction();
               } catch (Exception var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label319;
               }
            } else {
               try {
                  var4.add((long)(-var3));
               } catch (Exception var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label319;
               }
            }

            if (var2 != null) {
               var8 = 0;

               while(true) {
                  try {
                     if (var8 >= var2.size()) {
                        break;
                     }

                     Long var55 = (Long)var2.get(var8);
                     if (!var4.contains(var55)) {
                        var4.add(var55);
                     }
                  } catch (Exception var40) {
                     var10000 = var40;
                     var10001 = false;
                     break label319;
                  }

                  ++var8;
               }
            }

            TLRPC.TL_messages_dialogs var9;
            ArrayList var10;
            ArrayList var11;
            ArrayList var12;
            SQLiteCursor var13;
            try {
               var50 = TextUtils.join(",", var4);
               var9 = new TLRPC.TL_messages_dialogs();
               var10 = new ArrayList();
               var4 = new ArrayList();
               var11 = new ArrayList();
               var12 = new ArrayList();
               var13 = this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state, m.date, d.pts, d.inbox_max, d.outbox_max, d.pinned, d.unread_count_i, d.flags, d.folder_id, d.data FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid WHERE d.did IN(%s)", var50));
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
               break label319;
            }

            while(true) {
               Object var56;
               label325: {
                  TLRPC.TL_dialogFolder var53;
                  label264: {
                     try {
                        if (!var13.next()) {
                           break;
                        }

                        var6 = var13.longValue(0);
                        if (DialogObject.isFolderDialogId(var6)) {
                           var53 = new TLRPC.TL_dialogFolder();
                           break label264;
                        }
                     } catch (Exception var39) {
                        var10000 = var39;
                        var10001 = false;
                        break label319;
                     }

                     try {
                        var56 = new TLRPC.TL_dialog();
                        break label325;
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label319;
                     }
                  }

                  var56 = var53;

                  NativeByteBuffer var14;
                  try {
                     if (var13.isNull(16)) {
                        break label325;
                     }

                     var14 = var13.byteBufferValue(16);
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label319;
                  }

                  if (var14 != null) {
                     try {
                        var53.folder = TLRPC.TL_folder.TLdeserialize(var14, var14.readInt32(false), false);
                     } catch (Exception var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label319;
                     }
                  } else {
                     try {
                        TLRPC.TL_folder var57 = new TLRPC.TL_folder();
                        var53.folder = var57;
                        var53.folder.id = var13.intValue(15);
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label319;
                     }
                  }

                  try {
                     var14.reuse();
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label319;
                  }

                  var56 = var53;
               }

               try {
                  ((TLRPC.Dialog)var56).id = var6;
                  ((TLRPC.Dialog)var56).top_message = var13.intValue(1);
                  ((TLRPC.Dialog)var56).read_inbox_max_id = var13.intValue(10);
                  ((TLRPC.Dialog)var56).read_outbox_max_id = var13.intValue(11);
                  ((TLRPC.Dialog)var56).unread_count = var13.intValue(2);
                  ((TLRPC.Dialog)var56).unread_mentions_count = var13.intValue(13);
                  ((TLRPC.Dialog)var56).last_message_date = var13.intValue(3);
                  ((TLRPC.Dialog)var56).pts = var13.intValue(9);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label319;
               }

               byte var59;
               if (var3 == 0) {
                  var59 = 0;
               } else {
                  var59 = 1;
               }

               label248: {
                  label247: {
                     try {
                        ((TLRPC.Dialog)var56).flags = var59;
                        ((TLRPC.Dialog)var56).pinnedNum = var13.intValue(12);
                        if (((TLRPC.Dialog)var56).pinnedNum != 0) {
                           break label247;
                        }
                     } catch (Exception var37) {
                        var10000 = var37;
                        var10001 = false;
                        break label319;
                     }

                     var5 = false;
                     break label248;
                  }

                  var5 = true;
               }

               label241: {
                  label240: {
                     try {
                        ((TLRPC.Dialog)var56).pinned = var5;
                        if ((var13.intValue(14) & 1) != 0) {
                           break label240;
                        }
                     } catch (Exception var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label319;
                     }

                     var5 = false;
                     break label241;
                  }

                  var5 = true;
               }

               NativeByteBuffer var54;
               try {
                  ((TLRPC.Dialog)var56).unread_mark = var5;
                  ((TLRPC.Dialog)var56).folder_id = var13.intValue(15);
                  var9.dialogs.add(var56);
                  var54 = var13.byteBufferValue(4);
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label319;
               }

               if (var54 != null) {
                  TLRPC.Message var60;
                  try {
                     var60 = TLRPC.Message.TLdeserialize(var54, var54.readInt32(false), false);
                     var60.readAttachPath(var54, UserConfig.getInstance(this.currentAccount).clientUserId);
                     var54.reuse();
                     MessageObject.setUnreadFlags(var60, var13.intValue(5));
                     var60.id = var13.intValue(6);
                     var60.send_state = var13.intValue(7);
                     var8 = var13.intValue(8);
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label319;
                  }

                  if (var8 != 0) {
                     try {
                        ((TLRPC.Dialog)var56).last_message_date = var8;
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label319;
                     }
                  }

                  try {
                     var60.dialog_id = ((TLRPC.Dialog)var56).id;
                     var9.messages.add(var60);
                     addUsersAndChatsFromMessage(var60, var4, var11);
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label319;
                  }
               }

               int var15;
               try {
                  var15 = (int)((TLRPC.Dialog)var56).id;
                  var8 = (int)(((TLRPC.Dialog)var56).id >> 32);
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label319;
               }

               if (var15 != 0) {
                  if (var8 == 1) {
                     try {
                        if (!var11.contains(var15)) {
                           var11.add(var15);
                        }
                     } catch (Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label319;
                     }
                  } else if (var15 > 0) {
                     try {
                        if (!var4.contains(var15)) {
                           var4.add(var15);
                        }
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break label319;
                     }
                  } else {
                     var8 = -var15;

                     try {
                        if (!var11.contains(var8)) {
                           var11.add(var8);
                        }
                     } catch (Exception var35) {
                        var10000 = var35;
                        var10001 = false;
                        break label319;
                     }
                  }
               } else {
                  try {
                     if (!var12.contains(var8)) {
                        var12.add(var8);
                     }
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label319;
                  }
               }
            }

            try {
               var13.dispose();
               if (!var12.isEmpty()) {
                  this.getEncryptedChatsInternal(TextUtils.join(",", var12), var10, var4);
               }
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label319;
            }

            try {
               if (!var11.isEmpty()) {
                  this.getChatsInternal(TextUtils.join(",", var11), var9.chats);
               }
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label319;
            }

            try {
               if (!var4.isEmpty()) {
                  this.getUsersInternal(TextUtils.join(",", var4), var9.users);
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label319;
            }

            try {
               if (var9.dialogs.isEmpty() && var10.isEmpty()) {
                  return;
               }
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label319;
            }

            try {
               MessagesController.getInstance(this.currentAccount).processDialogsUpdate(var9, var10);
               return;
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
            }
         }

         Exception var58 = var10000;
         FileLog.e((Throwable)var58);
      } else {
         throw new RuntimeException("wrong db thread");
      }
   }

   private void updateDialogsWithReadMessagesInternal(ArrayList var1, SparseLongArray var2, SparseLongArray var3, ArrayList var4) {
      Exception var10000;
      label310: {
         LongSparseArray var5;
         LongSparseArray var6;
         ArrayList var7;
         boolean var8;
         boolean var10001;
         try {
            var5 = new LongSparseArray();
            var6 = new LongSparseArray();
            var7 = new ArrayList();
            var8 = isEmpty((List)var1);
         } catch (Exception var47) {
            var10000 = var47;
            var10001 = false;
            break label310;
         }

         long var9;
         int var11;
         SQLiteCursor var49;
         SQLitePreparedStatement var53;
         if (!var8) {
            try {
               String var48 = TextUtils.join(",", var1);
               var49 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, read_state, out FROM messages WHERE mid IN(%s)", var48));
            } catch (Exception var36) {
               var10000 = var36;
               var10001 = false;
               break label310;
            }

            label241:
            while(true) {
               while(true) {
                  try {
                     if (!var49.next()) {
                        break label241;
                     }

                     if (var49.intValue(2) != 0) {
                        continue;
                     }
                  } catch (Exception var37) {
                     var10000 = var37;
                     var10001 = false;
                     break label310;
                  }

                  label235:
                  try {
                     if (var49.intValue(1) == 0) {
                        break label235;
                     }
                     continue;
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label310;
                  }

                  Integer var51;
                  try {
                     var9 = var49.longValue(0);
                     var51 = (Integer)var5.get(var9);
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label310;
                  }

                  if (var51 == null) {
                     try {
                        var5.put(var9, 1);
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break label310;
                     }
                  } else {
                     try {
                        var5.put(var9, var51 + 1);
                     } catch (Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label310;
                     }
                  }
               }
            }

            try {
               var49.dispose();
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
               break label310;
            }
         } else {
            label313: {
               int var12;
               long var13;
               label314: {
                  try {
                     if (isEmpty(var2)) {
                        break label314;
                     }
                  } catch (Exception var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label310;
                  }

                  var11 = 0;

                  while(true) {
                     SparseLongArray var50 = var2;

                     try {
                        if (var11 >= var2.size()) {
                           break;
                        }

                        var12 = var50.keyAt(var11);
                        var9 = var50.get(var12);
                        var49 = this.database.queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM messages WHERE uid = %d AND mid > %d AND read_state IN(0,2) AND out = 0", var12, var9));
                        if (var49.next()) {
                           var5.put((long)var12, var49.intValue(0));
                        }
                     } catch (Exception var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label310;
                     }

                     try {
                        var49.dispose();
                        var53 = this.database.executeFast("UPDATE dialogs SET inbox_max = max((SELECT inbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?");
                        var53.requery();
                     } catch (Exception var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label310;
                     }

                     var13 = (long)var12;

                     try {
                        var53.bindLong(1, var13);
                        var53.bindInteger(2, (int)var9);
                        var53.bindLong(3, var13);
                        var53.step();
                        var53.dispose();
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label310;
                     }

                     ++var11;
                  }
               }

               label317: {
                  SQLiteCursor var54;
                  try {
                     if (isEmpty((List)var4)) {
                        break label317;
                     }

                     var1 = new ArrayList(var4);
                     String var52 = TextUtils.join(",", var4);
                     var54 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, read_state, out, mention, mid FROM messages WHERE mid IN(%s)", var52));
                  } catch (Exception var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label310;
                  }

                  while(true) {
                     Integer var56;
                     try {
                        if (!var54.next()) {
                           break;
                        }

                        var9 = var54.longValue(0);
                        var1.remove(var54.longValue(4));
                        if (var54.intValue(1) >= 2 || var54.intValue(2) != 0 || var54.intValue(3) != 1) {
                           continue;
                        }

                        var56 = (Integer)var6.get(var9);
                     } catch (Exception var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label310;
                     }

                     if (var56 != null) {
                        try {
                           var6.put(var9, Math.max(0, var56 - 1));
                        } catch (Exception var28) {
                           var10000 = var28;
                           var10001 = false;
                           break label310;
                        }
                     } else {
                        SQLiteCursor var58;
                        label283: {
                           try {
                              SQLiteDatabase var15 = this.database;
                              StringBuilder var57 = new StringBuilder();
                              var57.append("SELECT unread_count_i FROM dialogs WHERE did = ");
                              var57.append(var9);
                              var58 = var15.queryFinalized(var57.toString());
                              if (var58.next()) {
                                 var11 = var58.intValue(0);
                                 break label283;
                              }
                           } catch (Exception var44) {
                              var10000 = var44;
                              var10001 = false;
                              break label310;
                           }

                           var11 = 0;
                        }

                        try {
                           var58.dispose();
                           var6.put(var9, Math.max(0, var11 - 1));
                        } catch (Exception var29) {
                           var10000 = var29;
                           var10001 = false;
                           break label310;
                        }
                     }
                  }

                  try {
                     var54.dispose();
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label310;
                  }

                  var11 = 0;

                  while(true) {
                     try {
                        if (var11 >= var1.size()) {
                           break;
                        }

                        var12 = (int)((Long)var1.get(var11) >> 32);
                     } catch (Exception var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label310;
                     }

                     if (var12 > 0) {
                        try {
                           if (!var7.contains(var12)) {
                              var7.add(var12);
                           }
                        } catch (Exception var26) {
                           var10000 = var26;
                           var10001 = false;
                           break label310;
                        }
                     }

                     ++var11;
                  }
               }

               try {
                  if (isEmpty(var3)) {
                     break label313;
                  }
               } catch (Exception var40) {
                  var10000 = var40;
                  var10001 = false;
                  break label310;
               }

               var11 = 0;

               while(true) {
                  try {
                     if (var11 >= var3.size()) {
                        break;
                     }

                     var12 = var3.keyAt(var11);
                     var13 = var3.get(var12);
                     var53 = this.database.executeFast("UPDATE dialogs SET outbox_max = max((SELECT outbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?");
                     var53.requery();
                  } catch (Exception var39) {
                     var10000 = var39;
                     var10001 = false;
                     break label310;
                  }

                  var9 = (long)var12;

                  try {
                     var53.bindLong(1, var9);
                     var53.bindInteger(2, (int)var13);
                     var53.bindLong(3, var9);
                     var53.step();
                     var53.dispose();
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label310;
                  }

                  ++var11;
               }
            }
         }

         byte var59 = 0;

         label322: {
            try {
               if (var5.size() <= 0 && var6.size() <= 0) {
                  break label322;
               }
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label310;
            }

            label323: {
               try {
                  this.database.beginTransaction();
                  if (var5.size() <= 0) {
                     break label323;
                  }

                  var53 = this.database.executeFast("UPDATE dialogs SET unread_count = ? WHERE did = ?");
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label310;
               }

               var11 = 0;

               while(true) {
                  try {
                     if (var11 >= var5.size()) {
                        break;
                     }

                     var53.requery();
                     var53.bindInteger(1, (Integer)var5.valueAt(var11));
                     var53.bindLong(2, var5.keyAt(var11));
                     var53.step();
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label310;
                  }

                  ++var11;
               }

               try {
                  var53.dispose();
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label310;
               }
            }

            label324: {
               try {
                  if (var6.size() <= 0) {
                     break label324;
                  }

                  var53 = this.database.executeFast("UPDATE dialogs SET unread_count_i = ? WHERE did = ?");
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label310;
               }

               var11 = var59;

               while(true) {
                  try {
                     if (var11 >= var6.size()) {
                        break;
                     }

                     var53.requery();
                     var53.bindInteger(1, (Integer)var6.valueAt(var11));
                     var53.bindLong(2, var6.keyAt(var11));
                     var53.step();
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label310;
                  }

                  ++var11;
               }

               try {
                  var53.dispose();
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label310;
               }
            }

            try {
               this.database.commitTransaction();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label310;
            }
         }

         try {
            MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead(var5, var6);
            if (!var7.isEmpty()) {
               MessagesController.getInstance(this.currentAccount).reloadMentionsCountForChannels(var7);
            }

            return;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
         }
      }

      Exception var55 = var10000;
      FileLog.e((Throwable)var55);
   }

   private long[] updateMessageStateAndIdInternal(long param1, Integer param3, int param4, int param5, int param6) {
      // $FF: Couldn't be decompiled
   }

   private void updateUsersInternal(ArrayList var1, boolean var2, boolean var3) {
      if (Thread.currentThread().getId() != this.storageQueue.getId()) {
         throw new RuntimeException("wrong db thread");
      } else {
         byte var4 = 0;
         int var6;
         TLRPC.User var7;
         Exception var10000;
         boolean var10001;
         if (var2) {
            label199: {
               if (var3) {
                  try {
                     this.database.beginTransaction();
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label199;
                  }
               }

               SQLitePreparedStatement var5;
               int var31;
               try {
                  var5 = this.database.executeFast("UPDATE users SET status = ? WHERE uid = ?");
                  var31 = var1.size();
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label199;
               }

               var6 = 0;

               while(true) {
                  if (var6 >= var31) {
                     try {
                        var5.dispose();
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break;
                     }

                     if (!var3) {
                        return;
                     }

                     try {
                        this.database.commitTransaction();
                        return;
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break;
                     }
                  }

                  label209: {
                     try {
                        var7 = (TLRPC.User)var1.get(var6);
                        var5.requery();
                        if (var7.status != null) {
                           var5.bindInteger(1, var7.status.expires);
                           break label209;
                        }
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }

                     try {
                        var5.bindInteger(1, 0);
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var5.bindInteger(2, var7.id);
                     var5.step();
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }

                  ++var6;
               }
            }
         } else {
            label202: {
               int var8;
               SparseArray var32;
               StringBuilder var33;
               try {
                  var33 = new StringBuilder();
                  var32 = new SparseArray();
                  var8 = var1.size();
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label202;
               }

               var6 = 0;

               label189:
               while(true) {
                  TLRPC.User var9;
                  if (var6 >= var8) {
                     try {
                        var1 = new ArrayList();
                        this.getUsersInternal(var33.toString(), var1);
                        var8 = var1.size();
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break;
                     }

                     for(var6 = var4; var6 < var8; ++var6) {
                        try {
                           var7 = (TLRPC.User)var1.get(var6);
                           var9 = (TLRPC.User)var32.get(var7.id);
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label189;
                        }

                        if (var9 != null) {
                           label168: {
                              try {
                                 if (var9.first_name == null || var9.last_name == null) {
                                    break label168;
                                 }

                                 if (!UserObject.isContact(var7)) {
                                    var7.first_name = var9.first_name;
                                    var7.last_name = var9.last_name;
                                 }
                              } catch (Exception var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break label189;
                              }

                              try {
                                 var7.username = var9.username;
                                 continue;
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break label189;
                              }
                           }

                           try {
                              if (var9.photo != null) {
                                 var7.photo = var9.photo;
                                 continue;
                              }
                           } catch (Exception var24) {
                              var10000 = var24;
                              var10001 = false;
                              break label189;
                           }

                           try {
                              if (var9.phone != null) {
                                 var7.phone = var9.phone;
                              }
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label189;
                           }
                        }
                     }

                     try {
                        if (var1.isEmpty()) {
                           return;
                        }
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break;
                     }

                     if (var3) {
                        try {
                           this.database.beginTransaction();
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break;
                        }
                     }

                     try {
                        this.putUsersInternal(var1);
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break;
                     }

                     if (!var3) {
                        return;
                     }

                     try {
                        this.database.commitTransaction();
                        return;
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var9 = (TLRPC.User)var1.get(var6);
                     if (var33.length() != 0) {
                        var33.append(",");
                     }
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break;
                  }

                  try {
                     var33.append(var9.id);
                     var32.put(var9.id, var9);
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break;
                  }

                  ++var6;
               }
            }
         }

         Exception var30 = var10000;
         FileLog.e((Throwable)var30);
      }
   }

   public void addRecentLocalFile(String var1, String var2, TLRPC.Document var3) {
      if (var1 != null && var1.length() != 0 && (var2 != null && var2.length() != 0 || var3 != null)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU(this, var3, var1, var2));
      }

   }

   public void applyPhoneBookUpdates(String var1, String var2) {
      if (var1.length() != 0 || var2.length() != 0) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$OreTmYrmWTgoW8Zk3cD5yTn7vp0(this, var1, var2));
      }
   }

   public void checkIfFolderEmpty(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$icwTQAKHt7cMla2SxcMIgCt_hLo(this, var1));
   }

   public boolean checkMessageByRandomId(long var1) {
      boolean[] var3 = new boolean[1];
      CountDownLatch var4 = new CountDownLatch(1);
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d1ma4ral7Pvbl0aQnDGt6um4wvc(this, var1, var3, var4));

      try {
         var4.await();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      return var3[0];
   }

   public boolean checkMessageId(long var1, int var3) {
      boolean[] var4 = new boolean[1];
      CountDownLatch var5 = new CountDownLatch(1);
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0gsEtoIKwqYGtFeFeKyba_2TLjY(this, var1, var3, var4, var5));

      try {
         var5.await();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      return var4[0];
   }

   public void cleanup(boolean var1) {
      if (!var1) {
         this.storageQueue.cleanupQueue();
      }

      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ekOTDfJDLwKOj7vRXd_V4MXUJZc(this, var1));
   }

   public void clearDownloadQueue(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cshF_4Ao69vigcUys96seLmbz8c(this, var1));
   }

   public void clearSentMedia() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4rfOeATzWNWfd2pWwZkHIx6T8OU(this));
   }

   public void clearUserPhoto(int var1, long var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Cj9Zle7GEzC4ItOsJHnwvLaRg2g(this, var1, var2));
   }

   public void clearUserPhotos(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$1fRs_OhnqRmo_7LJUTfMP7QEmPg(this, var1));
   }

   public void clearWebRecent(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IAAcgw0MEJsvXeLksE_HqIMUybc(this, var1));
   }

   public void closeHolesInMedia(long var1, int var3, int var4, int var5) {
      Exception var10000;
      label146: {
         SQLiteCursor var6;
         boolean var10001;
         if (var5 < 0) {
            try {
               var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type >= 0 AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))", var1, var3, var4, var3, var4, var3, var4, var3, var4));
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label146;
            }
         } else {
            try {
               var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))", var1, var5, var3, var4, var3, var4, var3, var4, var3, var4));
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label146;
            }
         }

         ArrayList var7 = null;

         int var9;
         while(true) {
            try {
               if (!var6.next()) {
                  break;
               }
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label146;
            }

            ArrayList var8 = var7;
            if (var7 == null) {
               try {
                  var8 = new ArrayList();
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label146;
               }
            }

            int var10;
            try {
               var5 = var6.intValue(0);
               var9 = var6.intValue(1);
               var10 = var6.intValue(2);
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label146;
            }

            if (var9 != var10 || var9 != 1) {
               try {
                  MessagesStorage.Hole var28 = new MessagesStorage.Hole(var5, var9, var10);
                  var8.add(var28);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label146;
               }
            }

            var7 = var8;
         }

         try {
            var6.dispose();
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label146;
         }

         if (var7 == null) {
            return;
         }

         var5 = 0;

         while(true) {
            MessagesStorage.Hole var31;
            try {
               if (var5 >= var7.size()) {
                  return;
               }

               var31 = (MessagesStorage.Hole)var7.get(var5);
               var9 = var31.end;
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }

            label149: {
               if (var4 >= var9 - 1) {
                  try {
                     if (var3 <= var31.start + 1) {
                        this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d", var1, var31.type, var31.start, var31.end)).stepThis().dispose();
                        break label149;
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break;
                  }
               }

               Exception var27;
               label106: {
                  try {
                     if (var4 < var31.end - 1) {
                        break label106;
                     }

                     var9 = var31.end;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break;
                  }

                  if (var9 != var3) {
                     try {
                        this.database.executeFast(String.format(Locale.US, "UPDATE media_holes_v2 SET end = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d", var3, var1, var31.type, var31.start, var31.end)).stepThis().dispose();
                     } catch (Exception var15) {
                        var27 = var15;

                        try {
                           FileLog.e((Throwable)var27);
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break;
                        }
                     }
                  }
                  break label149;
               }

               label99: {
                  try {
                     if (var3 > var31.start + 1) {
                        break label99;
                     }

                     var9 = var31.start;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break;
                  }

                  if (var9 != var4) {
                     try {
                        this.database.executeFast(String.format(Locale.US, "UPDATE media_holes_v2 SET start = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d", var4, var1, var31.type, var31.start, var31.end)).stepThis().dispose();
                     } catch (Exception var13) {
                        var27 = var13;

                        try {
                           FileLog.e((Throwable)var27);
                        } catch (Exception var12) {
                           var10000 = var12;
                           var10001 = false;
                           break;
                        }
                     }
                  }
                  break label149;
               }

               try {
                  this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d", var1, var31.type, var31.start, var31.end)).stepThis().dispose();
                  SQLitePreparedStatement var29 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                  var29.requery();
                  var29.bindLong(1, var1);
                  var29.bindInteger(2, var31.type);
                  var29.bindInteger(3, var31.start);
                  var29.bindInteger(4, var3);
                  var29.step();
                  var29.requery();
                  var29.bindLong(1, var1);
                  var29.bindInteger(2, var31.type);
                  var29.bindInteger(3, var4);
                  var29.bindInteger(4, var31.end);
                  var29.step();
                  var29.dispose();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }
            }

            ++var5;
         }
      }

      Exception var30 = var10000;
      FileLog.e((Throwable)var30);
   }

   public long createPendingTask(NativeByteBuffer var1) {
      if (var1 == null) {
         return 0L;
      } else {
         long var2 = this.lastTaskId.getAndAdd(1L);
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3__p_EFkrwY3ws(this, var2, var1));
         return var2;
      }
   }

   public void createTaskForMid(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KFVjweRV_KgXgJdzwgxmQRw4vps(this, var3, var4, var5, var1, var2, var6));
   }

   public void createTaskForSecretChat(int var1, int var2, int var3, int var4, ArrayList var5) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M(this, var5, var1, var4, var2, var3));
   }

   public void deleteBlockedUser(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0TaEdYhzl8U_O7oEChUGdj3lNyw(this, var1));
   }

   public void deleteContacts(ArrayList var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$GMUC_3wWZaXGMALhkVNJZ4ROQdc(this, var1));
      }

   }

   public void deleteDialog(long var1, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$gHn5_xuiEvlIe5KWph9_QlfcuBk(this, var3, var1));
   }

   protected void deletePushMessages(long var1, ArrayList var3) {
      try {
         this.database.executeFast(String.format(Locale.US, "DELETE FROM unread_push_messages WHERE uid = %d AND mid IN(%s)", var1, TextUtils.join(",", var3))).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public void deleteUserChannelHistory(int var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$_PQj73otok1WW_acAxeuk_40Zls(this, var1, var2));
   }

   public void doneHolesInMedia(long var1, int var3, int var4) throws Exception {
      byte var5 = 0;
      SQLitePreparedStatement var6;
      if (var4 == -1) {
         if (var3 == 0) {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d", var1)).stepThis().dispose();
         } else {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND start = 0", var1)).stepThis().dispose();
         }

         var6 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");

         for(var3 = var5; var3 < 5; ++var3) {
            var6.requery();
            var6.bindLong(1, var1);
            var6.bindInteger(2, var3);
            var6.bindInteger(3, 1);
            var6.bindInteger(4, 1);
            var6.step();
         }

         var6.dispose();
      } else {
         if (var3 == 0) {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d", var1, var4)).stepThis().dispose();
         } else {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = 0", var1, var4)).stepThis().dispose();
         }

         var6 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
         var6.requery();
         var6.bindLong(1, var1);
         var6.bindInteger(2, var4);
         var6.bindInteger(3, 1);
         var6.bindInteger(4, 1);
         var6.step();
         var6.dispose();
      }

   }

   public void emptyMessagesMedia(ArrayList var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bzcgnKbuFLtWEX1lODvqWEyFuDo(this, var1));
   }

   public void getBlockedUsers() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4q7FLepIWnDxVXQ6fo32wEXrjW8(this));
   }

   public void getBotCache(String var1, RequestDelegate var2) {
      if (var1 != null && var2 != null) {
         int var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cYU0WnBJxesdCVkll0wOXZ3U0RE(this, var3, var1, var2));
      }

   }

   public void getCachedPhoneBook(boolean var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IEfB3sC9k_J4K6n4lzcQAX4QVAM(this, var1));
   }

   public int getChannelPtsSync(int var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      Integer[] var3 = new Integer[]{0};
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5zCr421jp6BG9VEI5v5DJ7UN8Ng(this, var1, var3, var2));

      try {
         var2.await();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      return var3[0];
   }

   public TLRPC.Chat getChat(int var1) {
      Object var2 = null;

      Exception var10000;
      TLRPC.Chat var7;
      label29: {
         boolean var10001;
         ArrayList var3;
         try {
            var3 = new ArrayList();
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var1);
            this.getChatsInternal(var4.toString(), var3);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label29;
         }

         var7 = (TLRPC.Chat)var2;

         try {
            if (!var3.isEmpty()) {
               var7 = (TLRPC.Chat)var3.get(0);
            }

            return var7;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      FileLog.e((Throwable)var8);
      var7 = (TLRPC.Chat)var2;
      return var7;
   }

   public TLRPC.Chat getChatSync(int var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      TLRPC.Chat[] var3 = new TLRPC.Chat[1];
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$coeKkTQyQbVSBUMnXP7SoD3ukxc(this, var3, var1, var2));

      try {
         var2.await();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      return var3[0];
   }

   public void getChatsInternal(String var1, ArrayList var2) throws Exception {
      if (var1 != null && var1.length() != 0 && var2 != null) {
         SQLiteCursor var8 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", var1));

         while(var8.next()) {
            Exception var10000;
            label53: {
               boolean var10001;
               NativeByteBuffer var3;
               try {
                  var3 = var8.byteBufferValue(0);
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label53;
               }

               if (var3 == null) {
                  continue;
               }

               TLRPC.Chat var4;
               try {
                  var4 = TLRPC.Chat.TLdeserialize(var3, var3.readInt32(false), false);
                  var3.reuse();
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label53;
               }

               if (var4 == null) {
                  continue;
               }

               try {
                  var2.add(var4);
                  continue;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var9 = var10000;
            FileLog.e((Throwable)var9);
         }

         var8.dispose();
      }

   }

   public void getContacts() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$c0C_JwfaP_juJcmhUEmKrLugWTs(this));
   }

   public SQLiteDatabase getDatabase() {
      return this.database;
   }

   public long getDatabaseSize() {
      File var1 = this.cacheFile;
      long var2 = 0L;
      if (var1 != null) {
         var2 = 0L + var1.length();
      }

      var1 = this.shmCacheFile;
      long var4 = var2;
      if (var1 != null) {
         var4 = var2 + var1.length();
      }

      return var4;
   }

   public void getDialogFolderId(long var1, MessagesStorage.IntCallback var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$SkuV6Hpjk5GQ4ZDcr1sIT7ixm50(this, var1, var3));
   }

   public void getDialogPhotos(int var1, int var2, long var3, int var5) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cZWPpHEko_cU5qbg57W0VB3uiqc(this, var3, var1, var2, var5));
   }

   public int getDialogReadMax(boolean var1, long var2) {
      CountDownLatch var4 = new CountDownLatch(1);
      Integer[] var5 = new Integer[]{0};
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$NotrdRfLQ2qAM8nf7f6dNfZC9LQ(this, var1, var2, var5, var4));

      try {
         var4.await();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      return var5[0];
   }

   public void getDialogs(int var1, int var2, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$xgSn2rhYdKpJx8E6odU_ONY7l64(this, var1, var2, var3));
   }

   public void getDownloadQueue(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$65MKjebMe2zja_JD_jfTGLf_uoQ(this, var1));
   }

   public TLRPC.EncryptedChat getEncryptedChat(int var1) {
      Object var2 = null;

      Exception var10000;
      TLRPC.EncryptedChat var7;
      label29: {
         boolean var10001;
         ArrayList var3;
         try {
            var3 = new ArrayList();
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var1);
            this.getEncryptedChatsInternal(var4.toString(), var3, (ArrayList)null);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label29;
         }

         var7 = (TLRPC.EncryptedChat)var2;

         try {
            if (!var3.isEmpty()) {
               var7 = (TLRPC.EncryptedChat)var3.get(0);
            }

            return var7;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      FileLog.e((Throwable)var8);
      var7 = (TLRPC.EncryptedChat)var2;
      return var7;
   }

   public void getEncryptedChat(int var1, CountDownLatch var2, ArrayList var3) {
      if (var2 != null && var3 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$T3CnFGNE_u1NlDHvs1A5BarAi2Q(this, var1, var3, var2));
      }

   }

   public void getEncryptedChatsInternal(String var1, ArrayList var2, ArrayList var3) throws Exception {
      if (var1 != null && var1.length() != 0 && var2 != null) {
         SQLiteCursor var14 = this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl, layer, seq_in, seq_out, use_count, exchange_id, key_date, fprint, fauthkey, khash, in_seq_no, admin_id, mtproto_seq FROM enc_chats WHERE uid IN(%s)", var1));

         while(var14.next()) {
            Exception var10000;
            label83: {
               NativeByteBuffer var4;
               boolean var10001;
               try {
                  var4 = var14.byteBufferValue(0);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label83;
               }

               if (var4 == null) {
                  continue;
               }

               TLRPC.EncryptedChat var5;
               try {
                  var5 = TLRPC.EncryptedChat.TLdeserialize(var4, var4.readInt32(false), false);
                  var4.reuse();
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label83;
               }

               if (var5 == null) {
                  continue;
               }

               try {
                  var5.user_id = var14.intValue(1);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label83;
               }

               if (var3 != null) {
                  try {
                     if (!var3.contains(var5.user_id)) {
                        var3.add(var5.user_id);
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label83;
                  }
               }

               int var6;
               try {
                  var5.a_or_b = var14.byteArrayValue(2);
                  var5.auth_key = var14.byteArrayValue(3);
                  var5.ttl = var14.intValue(4);
                  var5.layer = var14.intValue(5);
                  var5.seq_in = var14.intValue(6);
                  var5.seq_out = var14.intValue(7);
                  var6 = var14.intValue(8);
                  var5.key_use_count_in = (short)((short)(var6 >> 16));
                  var5.key_use_count_out = (short)((short)var6);
                  var5.exchange_id = var14.longValue(9);
                  var5.key_create_date = var14.intValue(10);
                  var5.future_key_fingerprint = var14.longValue(11);
                  var5.future_auth_key = var14.byteArrayValue(12);
                  var5.key_hash = var14.byteArrayValue(13);
                  var5.in_seq_no = var14.intValue(14);
                  var6 = var14.intValue(15);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label83;
               }

               if (var6 != 0) {
                  try {
                     var5.admin_id = var6;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label83;
                  }
               }

               try {
                  var5.mtproto_seq = var14.intValue(16);
                  var2.add(var5);
                  continue;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            Exception var15 = var10000;
            FileLog.e((Throwable)var15);
         }

         var14.dispose();
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

   public void getMessages(long var1, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, int var10) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$07SM_wPgrIcj52iujRCXFFg5cuY(this, var3, var4, var9, var1, var8, var6, var5, var7, var10));
   }

   public void getMessagesCount(long var1, MessagesStorage.IntCallback var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw(this, var1, var3));
   }

   public void getNewTask(ArrayList var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$SkcFNBx8pL2j8q0UCSVux88XNjo(this, var1));
   }

   public int getSecretG() {
      this.ensureOpened();
      return this.secretG;
   }

   public byte[] getSecretPBytes() {
      this.ensureOpened();
      return this.secretPBytes;
   }

   public Object[] getSentFile(String var1, int var2) {
      Object var3 = null;
      Object[] var4 = (Object[])var3;
      if (var1 != null) {
         if (var1.toLowerCase().endsWith("attheme")) {
            var4 = (Object[])var3;
         } else {
            CountDownLatch var7 = new CountDownLatch(1);
            Object[] var5 = new Object[2];
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8(this, var1, var2, var5, var7));

            try {
               var7.await();
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }

            var4 = (Object[])var3;
            if (var5[0] != null) {
               var4 = var5;
            }
         }
      }

      return var4;
   }

   public DispatchQueue getStorageQueue() {
      return this.storageQueue;
   }

   public void getUnreadMention(long var1, MessagesStorage.IntCallback var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$l8MwXwE6wtta48AYjWKC3wBXwIo(this, var1, var3));
   }

   public void getUnsentMessages(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$51EbzaU3YUe50I1L4RRCcEF_Asw(this, var1));
   }

   public TLRPC.User getUser(int var1) {
      Object var2 = null;

      Exception var10000;
      TLRPC.User var7;
      label29: {
         boolean var10001;
         ArrayList var3;
         try {
            var3 = new ArrayList();
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var1);
            this.getUsersInternal(var4.toString(), var3);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label29;
         }

         var7 = (TLRPC.User)var2;

         try {
            if (!var3.isEmpty()) {
               var7 = (TLRPC.User)var3.get(0);
            }

            return var7;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      FileLog.e((Throwable)var8);
      var7 = (TLRPC.User)var2;
      return var7;
   }

   public TLRPC.User getUserSync(int var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      TLRPC.User[] var3 = new TLRPC.User[1];
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$OGtMr0HOJdFgtGCQ7p3NBahgzn8(this, var3, var1, var2));

      try {
         var2.await();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      return var3[0];
   }

   public ArrayList getUsers(ArrayList var1) {
      ArrayList var2 = new ArrayList();

      try {
         this.getUsersInternal(TextUtils.join(",", var1), var2);
      } catch (Exception var3) {
         var2.clear();
         FileLog.e((Throwable)var3);
      }

      return var2;
   }

   public void getUsersInternal(String var1, ArrayList var2) throws Exception {
      if (var1 != null && var1.length() != 0 && var2 != null) {
         SQLiteCursor var9 = this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", var1));

         while(var9.next()) {
            Exception var10000;
            label63: {
               NativeByteBuffer var3;
               boolean var10001;
               try {
                  var3 = var9.byteBufferValue(0);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label63;
               }

               if (var3 == null) {
                  continue;
               }

               TLRPC.User var4;
               try {
                  var4 = TLRPC.User.TLdeserialize(var3, var3.readInt32(false), false);
                  var3.reuse();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label63;
               }

               if (var4 == null) {
                  continue;
               }

               try {
                  if (var4.status != null) {
                     var4.status.expires = var9.intValue(1);
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label63;
               }

               try {
                  var2.add(var4);
                  continue;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var10 = var10000;
            FileLog.e((Throwable)var10);
         }

         var9.dispose();
      }

   }

   public void getWallpapers() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ANII5DCxsANA4ox20RDROdwMfjc(this));
   }

   public boolean hasAuthMessage(int var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      boolean[] var3 = new boolean[1];
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$DRnJSL_cQzMMXDYp016D_2ih2II(this, var1, var3, var2));

      try {
         var2.await();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      return var3[0];
   }

   public boolean isDialogHasMessages(long var1) {
      CountDownLatch var3 = new CountDownLatch(1);
      boolean[] var4 = new boolean[1];
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$oKBMd5Lq6aDT_odvjSMscCF162k(this, var1, var4, var3));

      try {
         var3.await();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      return var4[0];
   }

   public boolean isMigratedChat(int var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      boolean[] var3 = new boolean[1];
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$vt3IkrkFUG3bfsSimMlmGH84kQo(this, var1, var3, var2));

      try {
         var2.await();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      return var3[0];
   }

   // $FF: synthetic method
   public void lambda$addRecentLocalFile$35$MessagesStorage(TLRPC.Document var1, String var2, String var3) {
      Exception var10000;
      boolean var10001;
      if (var1 != null) {
         try {
            SQLitePreparedStatement var4 = this.database.executeFast("UPDATE web_recent_v3 SET document = ? WHERE image_url = ?");
            var4.requery();
            NativeByteBuffer var9 = new NativeByteBuffer(var1.getObjectSize());
            var1.serializeToStream(var9);
            var4.bindByteBuffer(1, (NativeByteBuffer)var9);
            var4.bindString(2, var2);
            var4.step();
            var4.dispose();
            var9.reuse();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      } else {
         try {
            SQLitePreparedStatement var8 = this.database.executeFast("UPDATE web_recent_v3 SET local_url = ? WHERE image_url = ?");
            var8.requery();
            var8.bindString(1, var3);
            var8.bindString(2, var2);
            var8.step();
            var8.dispose();
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var7 = var10000;
      FileLog.e((Throwable)var7);
   }

   // $FF: synthetic method
   public void lambda$applyPhoneBookUpdates$88$MessagesStorage(String var1, String var2) {
      try {
         if (var1.length() != 0) {
            this.database.executeFast(String.format(Locale.US, "UPDATE user_phones_v7 SET deleted = 0 WHERE sphone IN(%s)", var1)).stepThis().dispose();
         }

         if (var2.length() != 0) {
            this.database.executeFast(String.format(Locale.US, "UPDATE user_phones_v7 SET deleted = 1 WHERE sphone IN(%s)", var2)).stepThis().dispose();
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   // $FF: synthetic method
   public void lambda$checkIfFolderEmpty$144$MessagesStorage(int var1) {
      this.checkIfFolderEmptyInternal(var1);
   }

   // $FF: synthetic method
   public void lambda$checkIfFolderEmptyInternal$143$MessagesStorage(int var1) {
      MessagesController.getInstance(this.currentAccount).onFolderEmpty(var1);
   }

   // $FF: synthetic method
   public void lambda$checkMessageByRandomId$93$MessagesStorage(long param1, boolean[] param3, CountDownLatch param4) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$checkMessageId$94$MessagesStorage(long param1, int param3, boolean[] param4, CountDownLatch param5) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$cleanup$3$MessagesStorage(boolean var1) {
      this.cleanupInternal(true);
      this.openDatabase(1);
      if (var1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesStorage$UEupzbErlOb7ycnNPm1lBZ9MmX8(this));
      }

   }

   // $FF: synthetic method
   public void lambda$clearDownloadQueue$115$MessagesStorage(int var1) {
      Exception var10000;
      boolean var10001;
      if (var1 == 0) {
         try {
            this.database.executeFast("DELETE FROM download_queue WHERE 1").stepThis().dispose();
            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      } else {
         try {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM download_queue WHERE type = %d", var1)).stepThis().dispose();
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var2 = var10000;
      FileLog.e((Throwable)var2);
   }

   // $FF: synthetic method
   public void lambda$clearSentMedia$101$MessagesStorage() {
      try {
         this.database.executeFast("DELETE FROM sent_files_v2 WHERE 1").stepThis().dispose();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$clearUserPhoto$50$MessagesStorage(int var1, long var2) {
      try {
         SQLiteDatabase var4 = this.database;
         StringBuilder var5 = new StringBuilder();
         var5.append("DELETE FROM user_photos WHERE uid = ");
         var5.append(var1);
         var5.append(" AND id = ");
         var5.append(var2);
         var4.executeFast(var5.toString()).stepThis().dispose();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   // $FF: synthetic method
   public void lambda$clearUserPhotos$49$MessagesStorage(int var1) {
      try {
         SQLiteDatabase var2 = this.database;
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM user_photos WHERE uid = ");
         var3.append(var1);
         var2.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$clearWebRecent$36$MessagesStorage(int var1) {
      try {
         SQLiteDatabase var2 = this.database;
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM web_recent_v3 WHERE type = ");
         var3.append(var1);
         var2.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$createPendingTask$6$MessagesStorage(long var1, NativeByteBuffer var3) {
      try {
         SQLitePreparedStatement var4 = this.database.executeFast("REPLACE INTO pending_tasks VALUES(?, ?)");
         var4.bindLong(1, var1);
         var4.bindByteBuffer(2, (NativeByteBuffer)var3);
         var4.step();
         var4.dispose();
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      } finally {
         var3.reuse();
      }

   }

   // $FF: synthetic method
   public void lambda$createTaskForMid$62$MessagesStorage(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (var1 <= var2) {
         var1 = var2;
      }

      var3 += var1;

      Exception var10000;
      label73: {
         SparseArray var7;
         ArrayList var8;
         boolean var10001;
         try {
            var7 = new SparseArray();
            var8 = new ArrayList();
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label73;
         }

         long var9 = (long)var4;
         long var11 = var9;
         if (var5 != 0) {
            var11 = var9 | (long)var5 << 32;
         }

         SQLitePreparedStatement var20;
         try {
            var8.add(var11);
            var7.put(var3, var8);
            _$$Lambda$MessagesStorage$LSwW05o0cOEMiYABF_G0tFfSKZ0 var13 = new _$$Lambda$MessagesStorage$LSwW05o0cOEMiYABF_G0tFfSKZ0(this, var6, var8);
            AndroidUtilities.runOnUIThread(var13);
            var20 = this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label73;
         }

         var1 = 0;

         while(true) {
            ArrayList var21;
            try {
               if (var1 >= var7.size()) {
                  break;
               }

               var4 = var7.keyAt(var1);
               var21 = (ArrayList)var7.get(var4);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label73;
            }

            var2 = 0;

            while(true) {
               try {
                  if (var2 >= var21.size()) {
                     break;
                  }

                  var20.requery();
                  var20.bindLong(1, (Long)var21.get(var2));
                  var20.bindInteger(2, var4);
                  var20.step();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label73;
               }

               ++var2;
            }

            ++var1;
         }

         try {
            var20.dispose();
            this.database.executeFast(String.format(Locale.US, "UPDATE messages SET ttl = 0 WHERE mid = %d", var11)).stepThis().dispose();
            MessagesController.getInstance(this.currentAccount).didAddedNewTask(var3, var7);
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var19 = var10000;
      FileLog.e((Throwable)var19);
   }

   // $FF: synthetic method
   public void lambda$createTaskForSecretChat$64$MessagesStorage(ArrayList var1, int var2, int var3, int var4, int var5) {
      int var6 = Integer.MAX_VALUE;

      Exception var10000;
      label151: {
         SparseArray var7;
         ArrayList var8;
         StringBuilder var9;
         boolean var10001;
         try {
            var7 = new SparseArray();
            var8 = new ArrayList();
            var9 = new StringBuilder();
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label151;
         }

         SQLiteCursor var10;
         if (var1 == null) {
            try {
               var10 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid, ttl FROM messages WHERE uid = %d AND out = %d AND read_state != 0 AND ttl > 0 AND date <= %d AND send_state = 0 AND media != 1", (long)var2 << 32, var3, var4));
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label151;
            }

            var2 = var6;
         } else {
            try {
               String var32 = TextUtils.join(",", var1);
               var10 = this.database.queryFinalized(String.format(Locale.US, "SELECT m.mid, m.ttl FROM messages as m INNER JOIN randoms as r ON m.mid = r.mid WHERE r.random_id IN (%s)", var32));
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label151;
            }

            var2 = var6;
         }

         while(true) {
            long var11;
            try {
               if (!var10.next()) {
                  break;
               }

               var6 = var10.intValue(1);
               var11 = (long)var10.intValue(0);
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label151;
            }

            if (var1 != null) {
               try {
                  var8.add(var11);
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label151;
               }
            }

            if (var6 > 0) {
               if (var4 > var5) {
                  var3 = var4;
               } else {
                  var3 = var5;
               }

               var3 += var6;

               ArrayList var13;
               try {
                  var2 = Math.min(var2, var3);
                  var13 = (ArrayList)var7.get(var3);
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label151;
               }

               if (var13 == null) {
                  try {
                     var13 = new ArrayList();
                     var7.put(var3, var13);
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label151;
                  }
               }

               try {
                  if (var9.length() != 0) {
                     var9.append(",");
                  }
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label151;
               }

               try {
                  var9.append(var11);
                  var13.add(var11);
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label151;
               }
            }
         }

         try {
            var10.dispose();
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label151;
         }

         if (var1 != null) {
            try {
               _$$Lambda$MessagesStorage$G_UdIl2qzLo_3dKfcoBBLIQUVS8 var29 = new _$$Lambda$MessagesStorage$G_UdIl2qzLo_3dKfcoBBLIQUVS8(this, var8);
               AndroidUtilities.runOnUIThread(var29);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label151;
            }
         }

         SQLitePreparedStatement var30;
         try {
            if (var7.size() == 0) {
               return;
            }

            this.database.beginTransaction();
            var30 = this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label151;
         }

         var3 = 0;

         while(true) {
            ArrayList var33;
            try {
               if (var3 >= var7.size()) {
                  break;
               }

               var5 = var7.keyAt(var3);
               var33 = (ArrayList)var7.get(var5);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label151;
            }

            var4 = 0;

            while(true) {
               try {
                  if (var4 >= var33.size()) {
                     break;
                  }

                  var30.requery();
                  var30.bindLong(1, (Long)var33.get(var4));
                  var30.bindInteger(2, var5);
                  var30.step();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label151;
               }

               ++var4;
            }

            ++var3;
         }

         try {
            var30.dispose();
            this.database.commitTransaction();
            this.database.executeFast(String.format(Locale.US, "UPDATE messages SET ttl = 0 WHERE mid IN(%s)", var9.toString())).stepThis().dispose();
            MessagesController.getInstance(this.currentAccount).didAddedNewTask(var2, var7);
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var31 = var10000;
      FileLog.e((Throwable)var31);
   }

   // $FF: synthetic method
   public void lambda$deleteBlockedUser$39$MessagesStorage(int var1) {
      try {
         SQLiteDatabase var2 = this.database;
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM blocked_users WHERE uid = ");
         var3.append(var1);
         var2.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteContacts$87$MessagesStorage(ArrayList var1) {
      try {
         String var2 = TextUtils.join(",", var1);
         SQLiteDatabase var5 = this.database;
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM contacts WHERE uid IN(");
         var3.append(var2);
         var3.append(")");
         var5.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteDialog$45$MessagesStorage(int var1, long var2) {
      Exception var10000;
      Exception var55;
      label311: {
         SQLiteDatabase var4;
         StringBuilder var5;
         int var6;
         boolean var10001;
         SQLiteCursor var43;
         if (var1 == 3) {
            label283: {
               try {
                  var4 = this.database;
                  var5 = new StringBuilder();
                  var5.append("SELECT last_mid FROM dialogs WHERE did = ");
                  var5.append(var2);
                  var43 = var4.queryFinalized(var5.toString());
                  if (var43.next()) {
                     var6 = var43.intValue(0);
                     break label283;
                  }
               } catch (Exception var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label311;
               }

               var6 = -1;
            }

            try {
               var43.dispose();
            } catch (Exception var41) {
               var10000 = var41;
               var10001 = false;
               break label311;
            }

            if (var6 != 0) {
               return;
            }
         }

         int var7 = (int)var2;
         StringBuilder var44;
         SQLiteDatabase var45;
         if (var7 == 0 || var1 == 2) {
            ArrayList var46;
            try {
               var45 = this.database;
               var44 = new StringBuilder();
               var44.append("SELECT data FROM messages WHERE uid = ");
               var44.append(var2);
               var43 = var45.queryFinalized(var44.toString());
               var46 = new ArrayList();
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label311;
            }

            label293: {
               label262:
               while(true) {
                  NativeByteBuffer var8;
                  try {
                     if (!var43.next()) {
                        break label293;
                     }

                     var8 = var43.byteBufferValue(0);
                  } catch (Exception var40) {
                     var10000 = var40;
                     var10001 = false;
                     break;
                  }

                  if (var8 != null) {
                     TLRPC.Message var9;
                     try {
                        var9 = TLRPC.Message.TLdeserialize(var8, var8.readInt32(false), false);
                        var9.readAttachPath(var8, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var8.reuse();
                     } catch (Exception var39) {
                        var10000 = var39;
                        var10001 = false;
                        break;
                     }

                     if (var9 != null) {
                        int var10;
                        File var47;
                        label295: {
                           try {
                              if (var9.media == null) {
                                 continue;
                              }

                              if (var9.media instanceof TLRPC.TL_messageMediaPhoto) {
                                 var10 = var9.media.photo.sizes.size();
                                 break label295;
                              }
                           } catch (Exception var38) {
                              var10000 = var38;
                              var10001 = false;
                              break;
                           }

                           try {
                              if (!(var9.media instanceof TLRPC.TL_messageMediaDocument)) {
                                 continue;
                              }

                              var47 = FileLoader.getPathToAttach(var9.media.document);
                           } catch (Exception var37) {
                              var10000 = var37;
                              var10001 = false;
                              break;
                           }

                           if (var47 != null) {
                              try {
                                 if (var47.toString().length() > 0) {
                                    var46.add(var47);
                                 }
                              } catch (Exception var36) {
                                 var10000 = var36;
                                 var10001 = false;
                                 break;
                              }
                           }

                           try {
                              var10 = var9.media.document.thumbs.size();
                           } catch (Exception var35) {
                              var10000 = var35;
                              var10001 = false;
                              break;
                           }

                           var6 = 0;

                           while(true) {
                              if (var6 >= var10) {
                                 continue label262;
                              }

                              try {
                                 var47 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var9.media.document.thumbs.get(var6));
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label262;
                              }

                              if (var47 != null) {
                                 try {
                                    if (var47.toString().length() > 0) {
                                       var46.add(var47);
                                    }
                                 } catch (Exception var33) {
                                    var10000 = var33;
                                    var10001 = false;
                                    break label262;
                                 }
                              }

                              ++var6;
                           }
                        }

                        for(var6 = 0; var6 < var10; ++var6) {
                           try {
                              var47 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var9.media.photo.sizes.get(var6));
                           } catch (Exception var32) {
                              var10000 = var32;
                              var10001 = false;
                              break label262;
                           }

                           if (var47 != null) {
                              try {
                                 if (var47.toString().length() > 0) {
                                    var46.add(var47);
                                 }
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break label262;
                              }
                           }
                        }
                     }
                  }
               }

               Exception var48 = var10000;

               try {
                  FileLog.e((Throwable)var48);
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label311;
               }
            }

            try {
               var43.dispose();
               FileLoader.getInstance(this.currentAccount).deleteFiles(var46, var1);
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label311;
            }
         }

         if (var1 != 0 && var1 != 3) {
            if (var1 == 2) {
               SQLiteCursor var54;
               label300: {
                  long var11;
                  long var13;
                  StringBuilder var50;
                  SQLiteCursor var52;
                  try {
                     var4 = this.database;
                     var5 = new StringBuilder();
                     var5.append("SELECT last_mid_i, last_mid FROM dialogs WHERE did = ");
                     var5.append(var2);
                     var54 = var4.queryFinalized(var5.toString());
                     if (!var54.next()) {
                        break label300;
                     }

                     var11 = var54.longValue(0);
                     var13 = var54.longValue(1);
                     var4 = this.database;
                     var50 = new StringBuilder();
                     var50.append("SELECT data FROM messages WHERE uid = ");
                     var50.append(var2);
                     var50.append(" AND mid IN (");
                     var50.append(var11);
                     var50.append(",");
                     var50.append(var13);
                     var50.append(")");
                     var52 = var4.queryFinalized(var50.toString());
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label311;
                  }

                  var1 = -1;

                  label176:
                  while(true) {
                     label174: {
                        while(true) {
                           NativeByteBuffer var56;
                           try {
                              if (!var52.next()) {
                                 break label176;
                              }

                              var56 = var52.byteBufferValue(0);
                           } catch (Exception var22) {
                              var55 = var22;
                              break;
                           }

                           if (var56 != null) {
                              label302: {
                                 TLRPC.Message var49;
                                 try {
                                    var49 = TLRPC.Message.TLdeserialize(var56, var56.readInt32(false), false);
                                    var49.readAttachPath(var56, UserConfig.getInstance(this.currentAccount).clientUserId);
                                    var56.reuse();
                                 } catch (Exception var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                    break label302;
                                 }

                                 if (var49 == null) {
                                    continue;
                                 }

                                 try {
                                    var6 = var49.id;
                                    break label174;
                                 } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                 }
                              }

                              var55 = var10000;
                              break;
                           }
                        }

                        try {
                           FileLog.e((Throwable)var55);
                           break;
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label311;
                        }
                     }

                     var1 = var6;
                  }

                  SQLitePreparedStatement var57;
                  SQLitePreparedStatement var58;
                  try {
                     var52.dispose();
                     SQLiteDatabase var53 = this.database;
                     var44 = new StringBuilder();
                     var44.append("DELETE FROM messages WHERE uid = ");
                     var44.append(var2);
                     var44.append(" AND mid != ");
                     var44.append(var11);
                     var44.append(" AND mid != ");
                     var44.append(var13);
                     var53.executeFast(var44.toString()).stepThis().dispose();
                     var4 = this.database;
                     var50 = new StringBuilder();
                     var50.append("DELETE FROM messages_holes WHERE uid = ");
                     var50.append(var2);
                     var4.executeFast(var50.toString()).stepThis().dispose();
                     var53 = this.database;
                     var44 = new StringBuilder();
                     var44.append("DELETE FROM bot_keyboard WHERE uid = ");
                     var44.append(var2);
                     var53.executeFast(var44.toString()).stepThis().dispose();
                     var4 = this.database;
                     var50 = new StringBuilder();
                     var50.append("DELETE FROM media_counts_v2 WHERE uid = ");
                     var50.append(var2);
                     var4.executeFast(var50.toString()).stepThis().dispose();
                     var53 = this.database;
                     var44 = new StringBuilder();
                     var44.append("DELETE FROM media_v2 WHERE uid = ");
                     var44.append(var2);
                     var53.executeFast(var44.toString()).stepThis().dispose();
                     var4 = this.database;
                     var50 = new StringBuilder();
                     var50.append("DELETE FROM media_holes_v2 WHERE uid = ");
                     var50.append(var2);
                     var4.executeFast(var50.toString()).stepThis().dispose();
                     DataQuery.getInstance(this.currentAccount).clearBotKeyboard(var2, (ArrayList)null);
                     var58 = this.database.executeFast("REPLACE INTO messages_holes VALUES(?, ?, ?)");
                     var57 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label311;
                  }

                  if (var1 != -1) {
                     try {
                        createFirstHoles(var2, var58, var57, var1);
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label311;
                     }
                  }

                  try {
                     var58.dispose();
                     var57.dispose();
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label311;
                  }
               }

               try {
                  var54.dispose();
                  return;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label311;
               }
            }
         } else {
            try {
               var4 = this.database;
               var5 = new StringBuilder();
               var5.append("DELETE FROM dialogs WHERE did = ");
               var5.append(var2);
               var4.executeFast(var5.toString()).stepThis().dispose();
               var4 = this.database;
               var5 = new StringBuilder();
               var5.append("DELETE FROM chat_settings_v2 WHERE uid = ");
               var5.append(var2);
               var4.executeFast(var5.toString()).stepThis().dispose();
               var4 = this.database;
               var5 = new StringBuilder();
               var5.append("DELETE FROM chat_pinned WHERE uid = ");
               var5.append(var2);
               var4.executeFast(var5.toString()).stepThis().dispose();
               var4 = this.database;
               var5 = new StringBuilder();
               var5.append("DELETE FROM channel_users_v2 WHERE did = ");
               var5.append(var2);
               var4.executeFast(var5.toString()).stepThis().dispose();
               var4 = this.database;
               var5 = new StringBuilder();
               var5.append("DELETE FROM search_recent WHERE did = ");
               var5.append(var2);
               var4.executeFast(var5.toString()).stepThis().dispose();
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label311;
            }

            var1 = (int)(var2 >> 32);
            if (var7 != 0) {
               if (var1 == 1) {
                  try {
                     var45 = this.database;
                     var44 = new StringBuilder();
                     var44.append("DELETE FROM chats WHERE uid = ");
                     var44.append(var7);
                     var45.executeFast(var44.toString()).stepThis().dispose();
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label311;
                  }
               }
            } else {
               try {
                  var45 = this.database;
                  var44 = new StringBuilder();
                  var44.append("DELETE FROM enc_chats WHERE uid = ");
                  var44.append(var1);
                  var45.executeFast(var44.toString()).stepThis().dispose();
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label311;
               }
            }
         }

         try {
            var45 = this.database;
            var44 = new StringBuilder();
            var44.append("UPDATE dialogs SET unread_count = 0, unread_count_i = 0 WHERE did = ");
            var44.append(var2);
            var45.executeFast(var44.toString()).stepThis().dispose();
            var45 = this.database;
            var44 = new StringBuilder();
            var44.append("DELETE FROM messages WHERE uid = ");
            var44.append(var2);
            var45.executeFast(var44.toString()).stepThis().dispose();
            var4 = this.database;
            var5 = new StringBuilder();
            var5.append("DELETE FROM bot_keyboard WHERE uid = ");
            var5.append(var2);
            var4.executeFast(var5.toString()).stepThis().dispose();
            var45 = this.database;
            var44 = new StringBuilder();
            var44.append("DELETE FROM media_counts_v2 WHERE uid = ");
            var44.append(var2);
            var45.executeFast(var44.toString()).stepThis().dispose();
            var45 = this.database;
            var44 = new StringBuilder();
            var44.append("DELETE FROM media_v2 WHERE uid = ");
            var44.append(var2);
            var45.executeFast(var44.toString()).stepThis().dispose();
            var4 = this.database;
            var5 = new StringBuilder();
            var5.append("DELETE FROM messages_holes WHERE uid = ");
            var5.append(var2);
            var4.executeFast(var5.toString()).stepThis().dispose();
            var4 = this.database;
            var5 = new StringBuilder();
            var5.append("DELETE FROM media_holes_v2 WHERE uid = ");
            var5.append(var2);
            var4.executeFast(var5.toString()).stepThis().dispose();
            DataQuery.getInstance(this.currentAccount).clearBotKeyboard(var2, (ArrayList)null);
            _$$Lambda$MessagesStorage$eGNSKWDegqojiFgkCHhM4t6KPc8 var51 = new _$$Lambda$MessagesStorage$eGNSKWDegqojiFgkCHhM4t6KPc8(this);
            AndroidUtilities.runOnUIThread(var51);
            return;
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
         }
      }

      var55 = var10000;
      FileLog.e((Throwable)var55);
   }

   // $FF: synthetic method
   public void lambda$deleteUserChannelHistory$43$MessagesStorage(int var1, int var2) {
      long var3 = (long)(-var1);

      Exception var10000;
      label146: {
         ArrayList var5;
         boolean var10001;
         SQLiteCursor var26;
         ArrayList var28;
         try {
            var5 = new ArrayList();
            SQLiteDatabase var6 = this.database;
            StringBuilder var7 = new StringBuilder();
            var7.append("SELECT data FROM messages WHERE uid = ");
            var7.append(var3);
            var26 = var6.queryFinalized(var7.toString());
            var28 = new ArrayList();
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label146;
         }

         label147: {
            label138:
            while(true) {
               NativeByteBuffer var8;
               try {
                  if (!var26.next()) {
                     break label147;
                  }

                  var8 = var26.byteBufferValue(0);
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }

               if (var8 != null) {
                  TLRPC.Message var9;
                  try {
                     var9 = TLRPC.Message.TLdeserialize(var8, var8.readInt32(false), false);
                     var9.readAttachPath(var8, UserConfig.getInstance(this.currentAccount).clientUserId);
                     var8.reuse();
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break;
                  }

                  if (var9 != null) {
                     int var10;
                     int var11;
                     File var30;
                     label149: {
                        try {
                           if (var9.from_id != var2 || var9.id == 1) {
                              continue;
                           }

                           var5.add(var9.id);
                           if (var9.media instanceof TLRPC.TL_messageMediaPhoto) {
                              var10 = var9.media.photo.sizes.size();
                              break label149;
                           }
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break;
                        }

                        try {
                           if (!(var9.media instanceof TLRPC.TL_messageMediaDocument)) {
                              continue;
                           }

                           var30 = FileLoader.getPathToAttach(var9.media.document);
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break;
                        }

                        if (var30 != null) {
                           try {
                              if (var30.toString().length() > 0) {
                                 var28.add(var30);
                              }
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break;
                           }
                        }

                        try {
                           var10 = var9.media.document.thumbs.size();
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break;
                        }

                        var11 = 0;

                        while(true) {
                           if (var11 >= var10) {
                              continue label138;
                           }

                           try {
                              var30 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var9.media.document.thumbs.get(var11));
                           } catch (Exception var18) {
                              var10000 = var18;
                              var10001 = false;
                              break label138;
                           }

                           if (var30 != null) {
                              try {
                                 if (var30.toString().length() > 0) {
                                    var28.add(var30);
                                 }
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label138;
                              }
                           }

                           ++var11;
                        }
                     }

                     for(var11 = 0; var11 < var10; ++var11) {
                        try {
                           var30 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var9.media.photo.sizes.get(var11));
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label138;
                        }

                        if (var30 != null) {
                           try {
                              if (var30.toString().length() > 0) {
                                 var28.add(var30);
                              }
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label138;
                           }
                        }
                     }
                  }
               }
            }

            Exception var31 = var10000;

            try {
               FileLog.e((Throwable)var31);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label146;
            }
         }

         try {
            var26.dispose();
            _$$Lambda$MessagesStorage$xrBSRE4KqhYgXVff8wvLv1PWOjc var27 = new _$$Lambda$MessagesStorage$xrBSRE4KqhYgXVff8wvLv1PWOjc(this, var5, var1);
            AndroidUtilities.runOnUIThread(var27);
            this.markMessagesAsDeletedInternal(var5, var1);
            this.updateDialogsWithDeletedMessagesInternal(var5, (ArrayList)null, var1);
            FileLoader.getInstance(this.currentAccount).deleteFiles(var28, 0);
            if (!var5.isEmpty()) {
               _$$Lambda$MessagesStorage$rtGvHc_Nh4AQF_VvLc0WZKhzEHE var29 = new _$$Lambda$MessagesStorage$rtGvHc_Nh4AQF_VvLc0WZKhzEHE(this, var5, var1);
               AndroidUtilities.runOnUIThread(var29);
            }

            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var25 = var10000;
      FileLog.e((Throwable)var25);
   }

   // $FF: synthetic method
   public void lambda$emptyMessagesMedia$55$MessagesStorage(ArrayList var1) {
      Exception var10000;
      label210: {
         ArrayList var2;
         ArrayList var3;
         boolean var10001;
         SQLiteCursor var30;
         try {
            var2 = new ArrayList();
            var3 = new ArrayList();
            var30 = this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM messages WHERE mid IN (%s)", TextUtils.join(",", var1)));
         } catch (Exception var26) {
            var10000 = var26;
            var10001 = false;
            break label210;
         }

         TLRPC.Message var5;
         int var7;
         while(true) {
            NativeByteBuffer var4;
            try {
               if (!var30.next()) {
                  break;
               }

               var4 = var30.byteBufferValue(0);
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label210;
            }

            if (var4 != null) {
               label212: {
                  int var6;
                  File var34;
                  label213: {
                     try {
                        var5 = TLRPC.Message.TLdeserialize(var4, var4.readInt32(false), false);
                        var5.readAttachPath(var4, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var4.reuse();
                        if (var5.media == null) {
                           continue;
                        }

                        if (var5.media.document != null) {
                           var34 = FileLoader.getPathToAttach(var5.media.document, true);
                           break label213;
                        }
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label210;
                     }

                     try {
                        if (var5.media.photo == null) {
                           continue;
                        }

                        var6 = var5.media.photo.sizes.size();
                     } catch (Exception var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label210;
                     }

                     for(var7 = 0; var7 < var6; ++var7) {
                        try {
                           var34 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var5.media.photo.sizes.get(var7));
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label210;
                        }

                        if (var34 != null) {
                           try {
                              if (var34.toString().length() > 0) {
                                 var2.add(var34);
                              }
                           } catch (Exception var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label210;
                           }
                        }
                     }

                     try {
                        TLRPC.MessageMedia var8 = var5.media;
                        TLRPC.TL_photoEmpty var35 = new TLRPC.TL_photoEmpty();
                        var8.photo = var35;
                        break label212;
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label210;
                     }
                  }

                  if (var34 != null) {
                     try {
                        if (var34.toString().length() > 0) {
                           var2.add(var34);
                        }
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label210;
                     }
                  }

                  try {
                     var6 = var5.media.document.thumbs.size();
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label210;
                  }

                  for(var7 = 0; var7 < var6; ++var7) {
                     try {
                        var34 = FileLoader.getPathToAttach((TLRPC.PhotoSize)var5.media.document.thumbs.get(var7));
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label210;
                     }

                     if (var34 != null) {
                        try {
                           if (var34.toString().length() > 0) {
                              var2.add(var34);
                           }
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label210;
                        }
                     }
                  }

                  try {
                     TLRPC.MessageMedia var36 = var5.media;
                     TLRPC.TL_documentEmpty var39 = new TLRPC.TL_documentEmpty();
                     var36.document = var39;
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label210;
                  }
               }

               try {
                  var5.media.flags &= -2;
                  var5.id = var30.intValue(1);
                  var5.date = var30.intValue(2);
                  var5.dialog_id = var30.longValue(3);
                  var3.add(var5);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label210;
               }
            }
         }

         label217: {
            SQLitePreparedStatement var37;
            try {
               var30.dispose();
               if (var3.isEmpty()) {
                  break label217;
               }

               var37 = this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label210;
            }

            var7 = 0;

            while(true) {
               NativeByteBuffer var31;
               byte var38;
               label119: {
                  label118: {
                     try {
                        if (var7 >= var3.size()) {
                           break;
                        }

                        var5 = (TLRPC.Message)var3.get(var7);
                        var31 = new NativeByteBuffer(var5.getObjectSize());
                        var5.serializeToStream(var31);
                        var37.requery();
                        var37.bindLong(1, (long)var5.id);
                        var37.bindLong(2, var5.dialog_id);
                        var37.bindInteger(3, MessageObject.getUnreadFlags(var5));
                        var37.bindInteger(4, var5.send_state);
                        var37.bindInteger(5, var5.date);
                        var37.bindByteBuffer(6, (NativeByteBuffer)var31);
                        if (MessageObject.isOut(var5)) {
                           break label118;
                        }
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label210;
                     }

                     var38 = 0;
                     break label119;
                  }

                  var38 = 1;
               }

               label127: {
                  try {
                     var37.bindInteger(7, var38);
                     var37.bindInteger(8, var5.ttl);
                     if ((var5.flags & 1024) != 0) {
                        var37.bindInteger(9, var5.views);
                        break label127;
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label210;
                  }

                  try {
                     var37.bindInteger(9, this.getMessageMediaType(var5));
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label210;
                  }
               }

               label136: {
                  label135: {
                     try {
                        var37.bindInteger(10, 0);
                        if (var5.mentioned) {
                           break label135;
                        }
                     } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label210;
                     }

                     var38 = 0;
                     break label136;
                  }

                  var38 = 1;
               }

               try {
                  var37.bindInteger(11, var38);
                  var37.step();
                  var31.reuse();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label210;
               }

               ++var7;
            }

            try {
               var37.dispose();
               _$$Lambda$MessagesStorage$nCnRqYOQtwzaZ2ZNInxhVBzA2As var32 = new _$$Lambda$MessagesStorage$nCnRqYOQtwzaZ2ZNInxhVBzA2As(this, var3);
               AndroidUtilities.runOnUIThread(var32);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label210;
            }
         }

         try {
            FileLoader.getInstance(this.currentAccount).deleteFiles(var2, 0);
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var33 = var10000;
      FileLog.e((Throwable)var33);
   }

   // $FF: synthetic method
   public void lambda$fixNotificationSettings$5$MessagesStorage() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getBlockedUsers$38$MessagesStorage() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getBotCache$72$MessagesStorage(int param1, String param2, RequestDelegate param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getCachedPhoneBook$90$MessagesStorage(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getChannelPtsSync$150$MessagesStorage(int param1, Integer[] param2, CountDownLatch param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getChatSync$152$MessagesStorage(TLRPC.Chat[] var1, int var2, CountDownLatch var3) {
      var1[0] = this.getChat(var2);
      var3.countDown();
   }

   // $FF: synthetic method
   public void lambda$getContacts$91$MessagesStorage() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();

      label57: {
         Exception var10000;
         label61: {
            SQLiteCursor var3;
            StringBuilder var4;
            boolean var10001;
            try {
               var3 = this.database.queryFinalized("SELECT * FROM contacts WHERE 1");
               var4 = new StringBuilder();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label61;
            }

            while(true) {
               TLRPC.TL_contact var6;
               boolean var7;
               label52: {
                  label51: {
                     try {
                        if (!var3.next()) {
                           break;
                        }

                        int var5 = var3.intValue(0);
                        var6 = new TLRPC.TL_contact();
                        var6.user_id = var5;
                        if (var3.intValue(1) == 1) {
                           break label51;
                        }
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label61;
                     }

                     var7 = false;
                     break label52;
                  }

                  var7 = true;
               }

               try {
                  var6.mutual = var7;
                  if (var4.length() != 0) {
                     var4.append(",");
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label61;
               }

               try {
                  var1.add(var6);
                  var4.append(var6.user_id);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label61;
               }
            }

            try {
               var3.dispose();
               if (var4.length() != 0) {
                  this.getUsersInternal(var4.toString(), var2);
               }
               break label57;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         Exception var13 = var10000;
         var1.clear();
         var2.clear();
         FileLog.e((Throwable)var13);
      }

      ContactsController.getInstance(this.currentAccount).processLoadedContacts(var1, var2, 1);
   }

   // $FF: synthetic method
   public void lambda$getDialogFolderId$141$MessagesStorage(long param1, MessagesStorage.IntCallback param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getDialogPhotos$48$MessagesStorage(long var1, int var3, int var4, int var5) {
      Exception var10000;
      label55: {
         SQLiteCursor var6;
         boolean var10001;
         if (var1 != 0L) {
            try {
               var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d AND id < %d ORDER BY rowid ASC LIMIT %d", var3, var1, var4));
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label55;
            }
         } else {
            try {
               var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d ORDER BY rowid ASC LIMIT %d", var3, var4));
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label55;
            }
         }

         TLRPC.TL_photos_photos var7;
         try {
            var7 = new TLRPC.TL_photos_photos();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label55;
         }

         while(true) {
            NativeByteBuffer var8;
            try {
               if (!var6.next()) {
                  break;
               }

               var8 = var6.byteBufferValue(0);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label55;
            }

            if (var8 != null) {
               try {
                  TLRPC.Photo var9 = TLRPC.Photo.TLdeserialize(var8, var8.readInt32(false), false);
                  var8.reuse();
                  var7.photos.add(var9);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label55;
               }
            }
         }

         try {
            var6.dispose();
            DispatchQueue var18 = Utilities.stageQueue;
            _$$Lambda$MessagesStorage$zh0hyL16UA2rjMCYsqbazYBjD4g var17 = new _$$Lambda$MessagesStorage$zh0hyL16UA2rjMCYsqbazYBjD4g(this, var7, var3, var4, var1, var5);
            var18.postRunnable(var17);
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
   }

   // $FF: synthetic method
   public void lambda$getDialogReadMax$149$MessagesStorage(boolean param1, long param2, Integer[] param4, CountDownLatch param5) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getDialogs$139$MessagesStorage(int var1, int var2, int var3) {
      TLRPC.TL_messages_dialogs var4 = new TLRPC.TL_messages_dialogs();
      Object var5 = new ArrayList();
      TLRPC.TL_messages_dialogs var6 = var4;
      Object var7 = var5;

      Exception var125;
      MessagesStorage var131;
      label883: {
         Exception var10000;
         label888: {
            ArrayList var8;
            ArrayList var9;
            boolean var10001;
            label889: {
               Object var133;
               String var138;
               label880: {
                  label879: {
                     label890: {
                        ArrayList var10;
                        ArrayList var11;
                        LongSparseArray var12;
                        boolean var22;
                        label877: {
                           label876: {
                              label875: {
                                 label891: {
                                    try {
                                       var8 = new ArrayList;
                                    } catch (Exception var124) {
                                       var10000 = var124;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var8.<init>();
                                    } catch (Exception var123) {
                                       var10000 = var123;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var8.add(UserConfig.getInstance(this.currentAccount).getClientUserId());
                                    } catch (Exception var122) {
                                       var10000 = var122;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var9 = new ArrayList;
                                    } catch (Exception var121) {
                                       var10000 = var121;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var9.<init>();
                                    } catch (Exception var120) {
                                       var10000 = var120;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var10 = new ArrayList;
                                    } catch (Exception var119) {
                                       var10000 = var119;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var10.<init>();
                                    } catch (Exception var118) {
                                       var10000 = var118;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var11 = new ArrayList;
                                    } catch (Exception var117) {
                                       var10000 = var117;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var11.<init>();
                                    } catch (Exception var116) {
                                       var10000 = var116;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var12 = new LongSparseArray;
                                    } catch (Exception var115) {
                                       var10000 = var115;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var12.<init>();
                                    } catch (Exception var114) {
                                       var10000 = var114;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    ArrayList var13;
                                    try {
                                       var13 = new ArrayList;
                                    } catch (Exception var113) {
                                       var10000 = var113;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var13.<init>(2);
                                    } catch (Exception var112) {
                                       var10000 = var112;
                                       var10001 = false;
                                       break label891;
                                    }

                                    var6 = var4;
                                    var7 = var5;

                                    try {
                                       var13.add(var1);
                                    } catch (Exception var111) {
                                       var10000 = var111;
                                       var10001 = false;
                                       break label891;
                                    }

                                    int var14 = 0;

                                    label818:
                                    while(true) {
                                       var6 = var4;
                                       var7 = var5;

                                       int var15;
                                       try {
                                          var15 = var13.size();
                                       } catch (Exception var46) {
                                          var10000 = var46;
                                          var10001 = false;
                                          break;
                                       }

                                       Exception var130;
                                       label918: {
                                          if (var14 >= var15) {
                                             var133 = var5;
                                             var6 = var4;
                                             var7 = var5;

                                             try {
                                                var22 = var11.isEmpty();
                                                break label877;
                                             } catch (Exception var48) {
                                                var10000 = var48;
                                                var10001 = false;
                                             }
                                          } else {
                                             var6 = var4;
                                             var7 = var5;

                                             int var16;
                                             try {
                                                var16 = (Integer)var13.get(var14);
                                             } catch (Exception var42) {
                                                var10000 = var42;
                                                var10001 = false;
                                                break label876;
                                             }

                                             int var17;
                                             if (var14 == 0) {
                                                var17 = var2;
                                                var15 = var3;
                                             } else {
                                                var17 = 0;
                                                var15 = 50;
                                             }

                                             var6 = var4;
                                             var7 = var5;

                                             SQLiteCursor var18;
                                             try {
                                                var18 = this.database.queryFinalized(String.format(Locale.US, "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state, s.flags, m.date, d.pts, d.inbox_max, d.outbox_max, m.replydata, d.pinned, d.unread_count_i, d.flags, d.folder_id, d.data FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid LEFT JOIN dialog_settings as s ON d.did = s.did WHERE d.folder_id = %d ORDER BY d.pinned DESC, d.date DESC LIMIT %d,%d", var16, var17, var15));
                                             } catch (Exception var41) {
                                                var10000 = var41;
                                                var10001 = false;
                                                break label876;
                                             }

                                             while(true) {
                                                MessagesStorage var19 = this;
                                                var6 = var4;
                                                var7 = var5;

                                                label895: {
                                                   try {
                                                      if (var18.next()) {
                                                         break label895;
                                                      }
                                                   } catch (Exception var110) {
                                                      var10000 = var110;
                                                      var10001 = false;
                                                      break label876;
                                                   }

                                                   var6 = var4;
                                                   var7 = var5;

                                                   try {
                                                      var18.dispose();
                                                   } catch (Exception var47) {
                                                      var10000 = var47;
                                                      var10001 = false;
                                                      break;
                                                   }

                                                   ++var14;
                                                   continue label818;
                                                }

                                                var6 = var4;
                                                var7 = var5;

                                                long var20;
                                                try {
                                                   var20 = var18.longValue(0);
                                                } catch (Exception var40) {
                                                   var10000 = var40;
                                                   var10001 = false;
                                                   break label876;
                                                }

                                                var6 = var4;
                                                var7 = var5;

                                                try {
                                                   var22 = DialogObject.isFolderDialogId(var20);
                                                } catch (Exception var39) {
                                                   var10000 = var39;
                                                   var10001 = false;
                                                   break label876;
                                                }

                                                Object var25;
                                                label803: {
                                                   TLRPC.TL_messages_dialogs var127;
                                                   label919: {
                                                      if (var22) {
                                                         var6 = var4;
                                                         var7 = var5;

                                                         TLRPC.TL_dialogFolder var23;
                                                         try {
                                                            var23 = new TLRPC.TL_dialogFolder;
                                                         } catch (Exception var45) {
                                                            var10000 = var45;
                                                            var10001 = false;
                                                            break label818;
                                                         }

                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            var23.<init>();
                                                         } catch (Exception var44) {
                                                            var10000 = var44;
                                                            var10001 = false;
                                                            break label818;
                                                         }

                                                         var6 = var4;
                                                         var7 = var5;

                                                         label898: {
                                                            try {
                                                               if (var18.isNull(18)) {
                                                                  break label898;
                                                               }
                                                            } catch (Exception var56) {
                                                               var10000 = var56;
                                                               var10001 = false;
                                                               break label818;
                                                            }

                                                            var6 = var4;
                                                            var7 = var5;

                                                            NativeByteBuffer var24;
                                                            try {
                                                               var24 = var18.byteBufferValue(18);
                                                            } catch (Exception var43) {
                                                               var10000 = var43;
                                                               var10001 = false;
                                                               break label818;
                                                            }

                                                            if (var24 != null) {
                                                               try {
                                                                  var23.folder = TLRPC.TL_folder.TLdeserialize(var24, var24.readInt32(false), false);
                                                               } catch (Exception var55) {
                                                                  var10000 = var55;
                                                                  var10001 = false;
                                                                  break label918;
                                                               }
                                                            } else {
                                                               try {
                                                                  TLRPC.TL_folder var126 = new TLRPC.TL_folder();
                                                                  var23.folder = var126;
                                                                  var23.folder.id = (int)var20;
                                                               } catch (Exception var54) {
                                                                  var10000 = var54;
                                                                  var10001 = false;
                                                                  break label918;
                                                               }
                                                            }

                                                            try {
                                                               var24.reuse();
                                                            } catch (Exception var53) {
                                                               var10000 = var53;
                                                               var10001 = false;
                                                               break label918;
                                                            }
                                                         }

                                                         var25 = var23;
                                                         if (var14 == 0) {
                                                            try {
                                                               var13.add(var23.folder.id);
                                                            } catch (Exception var52) {
                                                               var10000 = var52;
                                                               var10001 = false;
                                                               break label918;
                                                            }

                                                            var25 = var23;
                                                         }
                                                      } else {
                                                         var127 = var4;

                                                         try {
                                                            var25 = new TLRPC.TL_dialog();
                                                         } catch (Exception var109) {
                                                            var10000 = var109;
                                                            var10001 = false;
                                                            break label919;
                                                         }
                                                      }

                                                      var133 = var5;
                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).id = var20;
                                                      } catch (Exception var108) {
                                                         var10000 = var108;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).top_message = var18.intValue(1);
                                                      } catch (Exception var107) {
                                                         var10000 = var107;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).unread_count = var18.intValue(2);
                                                      } catch (Exception var106) {
                                                         var10000 = var106;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).last_message_date = var18.intValue(3);
                                                      } catch (Exception var105) {
                                                         var10000 = var105;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).pts = var18.intValue(10);
                                                      } catch (Exception var104) {
                                                         var10000 = var104;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         var15 = ((TLRPC.Dialog)var25).pts;
                                                      } catch (Exception var103) {
                                                         var10000 = var103;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      byte var132;
                                                      label608: {
                                                         if (var15 != 0) {
                                                            try {
                                                               var20 = ((TLRPC.Dialog)var25).id;
                                                            } catch (Exception var51) {
                                                               var10000 = var51;
                                                               var10001 = false;
                                                               break label918;
                                                            }

                                                            if ((int)var20 <= 0) {
                                                               var132 = 1;
                                                               break label608;
                                                            }
                                                         }

                                                         var132 = 0;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).flags = var132;
                                                      } catch (Exception var102) {
                                                         var10000 = var102;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).read_inbox_max_id = var18.intValue(11);
                                                      } catch (Exception var101) {
                                                         var10000 = var101;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).read_outbox_max_id = var18.intValue(12);
                                                      } catch (Exception var100) {
                                                         var10000 = var100;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).pinnedNum = var18.intValue(14);
                                                      } catch (Exception var99) {
                                                         var10000 = var99;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      label755: {
                                                         label754: {
                                                            try {
                                                               if (((TLRPC.Dialog)var25).pinnedNum == 0) {
                                                                  break label754;
                                                               }
                                                            } catch (Exception var98) {
                                                               var10000 = var98;
                                                               var10001 = false;
                                                               break label919;
                                                            }

                                                            var22 = true;
                                                            break label755;
                                                         }

                                                         var22 = false;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).pinned = var22;
                                                      } catch (Exception var97) {
                                                         var10000 = var97;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).unread_mentions_count = var18.intValue(15);
                                                      } catch (Exception var96) {
                                                         var10000 = var96;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      label738: {
                                                         label737: {
                                                            try {
                                                               if ((var18.intValue(16) & 1) != 0) {
                                                                  break label737;
                                                               }
                                                            } catch (Exception var95) {
                                                               var10000 = var95;
                                                               var10001 = false;
                                                               break label919;
                                                            }

                                                            var22 = false;
                                                            break label738;
                                                         }

                                                         var22 = true;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).unread_mark = var22;
                                                      } catch (Exception var94) {
                                                         var10000 = var94;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         var20 = var18.longValue(8);
                                                      } catch (Exception var93) {
                                                         var10000 = var93;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var15 = (int)var20;
                                                      var127 = var4;

                                                      TLRPC.TL_peerNotifySettings var128;
                                                      try {
                                                         var128 = new TLRPC.TL_peerNotifySettings;
                                                      } catch (Exception var92) {
                                                         var10000 = var92;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         var128.<init>();
                                                      } catch (Exception var91) {
                                                         var10000 = var91;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).notify_settings = var128;
                                                      } catch (Exception var90) {
                                                         var10000 = var90;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      if ((var15 & 1) != 0) {
                                                         try {
                                                            ((TLRPC.Dialog)var25).notify_settings.mute_until = (int)(var20 >> 32);
                                                            if (((TLRPC.Dialog)var25).notify_settings.mute_until == 0) {
                                                               ((TLRPC.Dialog)var25).notify_settings.mute_until = Integer.MAX_VALUE;
                                                            }
                                                         } catch (Exception var50) {
                                                            var10000 = var50;
                                                            var10001 = false;
                                                            break label918;
                                                         }
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         ((TLRPC.Dialog)var25).folder_id = var18.intValue(17);
                                                      } catch (Exception var89) {
                                                         var10000 = var89;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      try {
                                                         var4.dialogs.add(var25);
                                                      } catch (Exception var88) {
                                                         var10000 = var88;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;

                                                      NativeByteBuffer var129;
                                                      try {
                                                         var129 = var18.byteBufferValue(4);
                                                      } catch (Exception var87) {
                                                         var10000 = var87;
                                                         var10001 = false;
                                                         break label919;
                                                      }

                                                      var127 = var4;
                                                      if (var129 != null) {
                                                         var127 = var4;

                                                         TLRPC.Message var26;
                                                         try {
                                                            var26 = TLRPC.Message.TLdeserialize(var129, var129.readInt32(false), false);
                                                         } catch (Exception var80) {
                                                            var10000 = var80;
                                                            var10001 = false;
                                                            break label919;
                                                         }

                                                         var127 = var4;

                                                         try {
                                                            var26.readAttachPath(var129, UserConfig.getInstance(var19.currentAccount).clientUserId);
                                                         } catch (Exception var79) {
                                                            var10000 = var79;
                                                            var10001 = false;
                                                            break label919;
                                                         }

                                                         var127 = var4;

                                                         try {
                                                            var129.reuse();
                                                         } catch (Exception var78) {
                                                            var10000 = var78;
                                                            var10001 = false;
                                                            break label919;
                                                         }

                                                         var127 = var4;
                                                         if (var26 != null) {
                                                            label915: {
                                                               var127 = var4;

                                                               try {
                                                                  MessageObject.setUnreadFlags(var26, var18.intValue(5));
                                                               } catch (Exception var77) {
                                                                  var10000 = var77;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               var127 = var4;

                                                               try {
                                                                  var26.id = var18.intValue(6);
                                                               } catch (Exception var76) {
                                                                  var10000 = var76;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               var127 = var4;

                                                               try {
                                                                  var15 = var18.intValue(9);
                                                               } catch (Exception var75) {
                                                                  var10000 = var75;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               if (var15 != 0) {
                                                                  try {
                                                                     ((TLRPC.Dialog)var25).last_message_date = var15;
                                                                  } catch (Exception var49) {
                                                                     var10000 = var49;
                                                                     var10001 = false;
                                                                     break label918;
                                                                  }
                                                               }

                                                               var127 = var4;

                                                               try {
                                                                  var26.send_state = var18.intValue(7);
                                                               } catch (Exception var74) {
                                                                  var10000 = var74;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               var6 = var4;
                                                               var7 = var5;

                                                               try {
                                                                  var26.dialog_id = ((TLRPC.Dialog)var25).id;
                                                               } catch (Exception var64) {
                                                                  var10000 = var64;
                                                                  var10001 = false;
                                                                  break;
                                                               }

                                                               var6 = var4;
                                                               var127 = var4;

                                                               try {
                                                                  var6.messages.add(var26);
                                                               } catch (Exception var73) {
                                                                  var10000 = var73;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               var127 = var4;

                                                               try {
                                                                  addUsersAndChatsFromMessage(var26, var8, var9);
                                                               } catch (Exception var72) {
                                                                  var10000 = var72;
                                                                  var10001 = false;
                                                                  break label919;
                                                               }

                                                               var127 = var4;

                                                               label900: {
                                                                  label692: {
                                                                     label901: {
                                                                        label902: {
                                                                           try {
                                                                              if (var26.reply_to_msg_id == 0) {
                                                                                 break label915;
                                                                              }

                                                                              if (var26.action instanceof TLRPC.TL_messageActionPinMessage || var26.action instanceof TLRPC.TL_messageActionPaymentSent) {
                                                                                 break label902;
                                                                              }
                                                                           } catch (Exception var86) {
                                                                              var10000 = var86;
                                                                              var10001 = false;
                                                                              break label901;
                                                                           }

                                                                           var127 = var4;

                                                                           try {
                                                                              if (!(var26.action instanceof TLRPC.TL_messageActionGameScore)) {
                                                                                 break label915;
                                                                              }
                                                                           } catch (Exception var85) {
                                                                              var10000 = var85;
                                                                              var10001 = false;
                                                                              break label901;
                                                                           }
                                                                        }

                                                                        label679: {
                                                                           try {
                                                                              if (var18.isNull(13)) {
                                                                                 break label679;
                                                                              }

                                                                              var5 = var18.byteBufferValue(13);
                                                                           } catch (Exception var84) {
                                                                              var10000 = var84;
                                                                              var10001 = false;
                                                                              break label901;
                                                                           }

                                                                           if (var5 != null) {
                                                                              label673: {
                                                                                 try {
                                                                                    var26.replyMessage = TLRPC.Message.TLdeserialize((AbstractSerializedData)var5, ((NativeByteBuffer)var5).readInt32(false), false);
                                                                                    var26.replyMessage.readAttachPath((AbstractSerializedData)var5, UserConfig.getInstance(var19.currentAccount).clientUserId);
                                                                                    ((NativeByteBuffer)var5).reuse();
                                                                                    if (var26.replyMessage == null) {
                                                                                       break label673;
                                                                                    }

                                                                                    if (MessageObject.isMegagroup(var26)) {
                                                                                       var5 = var26.replyMessage;
                                                                                       ((TLRPC.Message)var5).flags |= Integer.MIN_VALUE;
                                                                                    }
                                                                                 } catch (Exception var83) {
                                                                                    var10000 = var83;
                                                                                    var10001 = false;
                                                                                    break label901;
                                                                                 }

                                                                                 try {
                                                                                    addUsersAndChatsFromMessage(var26.replyMessage, var8, var9);
                                                                                 } catch (Exception var82) {
                                                                                    var10000 = var82;
                                                                                    var10001 = false;
                                                                                    break label901;
                                                                                 }
                                                                              }
                                                                           }
                                                                        }

                                                                        var127 = var4;

                                                                        try {
                                                                           if (var26.replyMessage != null) {
                                                                              break label915;
                                                                           }

                                                                           var15 = var26.reply_to_msg_id;
                                                                           break label692;
                                                                        } catch (Exception var81) {
                                                                           var10000 = var81;
                                                                           var10001 = false;
                                                                        }
                                                                     }

                                                                     var5 = var10000;
                                                                     break label900;
                                                                  }

                                                                  long var27 = (long)var15;
                                                                  var20 = var27;

                                                                  label905: {
                                                                     try {
                                                                        if (var26.to_id.channel_id != 0) {
                                                                           var20 = var27 | (long)var26.to_id.channel_id << 32;
                                                                        }
                                                                     } catch (Exception var71) {
                                                                        var10000 = var71;
                                                                        var10001 = false;
                                                                        break label905;
                                                                     }

                                                                     try {
                                                                        if (!var11.contains(var20)) {
                                                                           var11.add(var20);
                                                                        }
                                                                     } catch (Exception var70) {
                                                                        var10000 = var70;
                                                                        var10001 = false;
                                                                        break label905;
                                                                     }

                                                                     try {
                                                                        var12.put(((TLRPC.Dialog)var25).id, var26);
                                                                     } catch (Exception var69) {
                                                                        var10000 = var69;
                                                                        var10001 = false;
                                                                        break label905;
                                                                     }

                                                                     var4 = var4;
                                                                     break label803;
                                                                  }

                                                                  var5 = var10000;
                                                               }

                                                               var6 = var4;
                                                               var7 = var133;

                                                               try {
                                                                  FileLog.e((Throwable)var5);
                                                                  break label803;
                                                               } catch (Exception var63) {
                                                                  var10000 = var63;
                                                                  var10001 = false;
                                                                  break;
                                                               }
                                                            }
                                                         }
                                                      }

                                                      var4 = var127;
                                                      break label803;
                                                   }

                                                   var130 = var10000;
                                                   var4 = var127;
                                                   var7 = var5;
                                                   var125 = var130;
                                                   break label879;
                                                }

                                                var6 = var4;
                                                var7 = var5;

                                                try {
                                                   var17 = (int)((TLRPC.Dialog)var25).id;
                                                } catch (Exception var62) {
                                                   var10000 = var62;
                                                   var10001 = false;
                                                   break;
                                                }

                                                var6 = var4;
                                                var7 = var5;

                                                try {
                                                   var15 = (int)(((TLRPC.Dialog)var25).id >> 32);
                                                } catch (Exception var61) {
                                                   var10000 = var61;
                                                   var10001 = false;
                                                   break;
                                                }

                                                if (var17 != 0) {
                                                   if (var15 == 1) {
                                                      label906: {
                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            if (var9.contains(var17)) {
                                                               break label906;
                                                            }
                                                         } catch (Exception var68) {
                                                            var10000 = var68;
                                                            var10001 = false;
                                                            break;
                                                         }

                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            var9.add(var17);
                                                         } catch (Exception var60) {
                                                            var10000 = var60;
                                                            var10001 = false;
                                                            break;
                                                         }
                                                      }
                                                   } else if (var17 > 0) {
                                                      label907: {
                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            if (var8.contains(var17)) {
                                                               break label907;
                                                            }
                                                         } catch (Exception var67) {
                                                            var10000 = var67;
                                                            var10001 = false;
                                                            break;
                                                         }

                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            var8.add(var17);
                                                         } catch (Exception var59) {
                                                            var10000 = var59;
                                                            var10001 = false;
                                                            break;
                                                         }
                                                      }
                                                   } else {
                                                      label908: {
                                                         var15 = -var17;
                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            if (var9.contains(var15)) {
                                                               break label908;
                                                            }
                                                         } catch (Exception var66) {
                                                            var10000 = var66;
                                                            var10001 = false;
                                                            break;
                                                         }

                                                         var6 = var4;
                                                         var7 = var5;

                                                         try {
                                                            var9.add(var15);
                                                         } catch (Exception var58) {
                                                            var10000 = var58;
                                                            var10001 = false;
                                                            break;
                                                         }
                                                      }
                                                   }
                                                } else {
                                                   label909: {
                                                      var6 = var4;
                                                      var7 = var5;

                                                      try {
                                                         if (var10.contains(var15)) {
                                                            break label909;
                                                         }
                                                      } catch (Exception var65) {
                                                         var10000 = var65;
                                                         var10001 = false;
                                                         break;
                                                      }

                                                      var6 = var4;
                                                      var7 = var5;

                                                      try {
                                                         var10.add(var15);
                                                      } catch (Exception var57) {
                                                         var10000 = var57;
                                                         var10001 = false;
                                                         break;
                                                      }
                                                   }
                                                }

                                                var5 = var5;
                                             }
                                          }

                                          var125 = var10000;
                                          var4 = var6;
                                          break label890;
                                       }

                                       var130 = var10000;
                                       var7 = var5;
                                       var125 = var130;
                                       break label875;
                                    }
                                 }

                                 var125 = var10000;
                                 var4 = var6;
                              }

                              var131 = this;
                              break label883;
                           }

                           var125 = var10000;
                           var4 = var6;
                           break label879;
                        }

                        label920: {
                           if (!var22) {
                              var131 = this;

                              SQLiteCursor var137;
                              try {
                                 var137 = var131.database.queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM messages WHERE mid IN(%s)", TextUtils.join(",", var11)));
                              } catch (Exception var36) {
                                 var10000 = var36;
                                 var10001 = false;
                                 break label920;
                              }

                              while(true) {
                                 NativeByteBuffer var135;
                                 try {
                                    if (!var137.next()) {
                                       break;
                                    }

                                    var135 = var137.byteBufferValue(0);
                                 } catch (Exception var38) {
                                    var10000 = var38;
                                    var10001 = false;
                                    break label920;
                                 }

                                 if (var135 != null) {
                                    TLRPC.Message var134;
                                    TLRPC.Message var136;
                                    try {
                                       var134 = TLRPC.Message.TLdeserialize(var135, var135.readInt32(false), false);
                                       var134.readAttachPath(var135, UserConfig.getInstance(var131.currentAccount).clientUserId);
                                       var135.reuse();
                                       var134.id = var137.intValue(1);
                                       var134.date = var137.intValue(2);
                                       var134.dialog_id = var137.longValue(3);
                                       addUsersAndChatsFromMessage(var134, var8, var9);
                                       var136 = (TLRPC.Message)var12.get(var134.dialog_id);
                                    } catch (Exception var35) {
                                       var10000 = var35;
                                       var10001 = false;
                                       break label920;
                                    }

                                    if (var136 != null) {
                                       try {
                                          var136.replyMessage = var134;
                                          var134.dialog_id = var136.dialog_id;
                                          if (MessageObject.isMegagroup(var136)) {
                                             var134 = var136.replyMessage;
                                             var134.flags |= Integer.MIN_VALUE;
                                          }
                                       } catch (Exception var37) {
                                          var10000 = var37;
                                          var10001 = false;
                                          break label920;
                                       }
                                    }
                                 }
                              }

                              try {
                                 var137.dispose();
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label920;
                              }
                           }

                           var131 = this;

                           try {
                              if (var10.isEmpty()) {
                                 break label889;
                              }

                              var138 = TextUtils.join(",", var10);
                              break label880;
                           } catch (Exception var33) {
                              var10000 = var33;
                              var10001 = false;
                           }
                        }

                        var125 = var10000;
                        var7 = var5;
                     }

                     var131 = this;
                     break label883;
                  }

                  var131 = this;
                  break label883;
               }

               try {
                  var131.getEncryptedChatsInternal(var138, (ArrayList)var133, var8);
               } catch (Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label888;
               }
            }

            try {
               if (!var9.isEmpty()) {
                  var131.getChatsInternal(TextUtils.join(",", var9), var4.chats);
               }
            } catch (Exception var31) {
               var10000 = var31;
               var10001 = false;
               break label888;
            }

            try {
               if (!var8.isEmpty()) {
                  var131.getUsersInternal(TextUtils.join(",", var8), var4.users);
               }
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label888;
            }

            try {
               MessagesController.getInstance(var131.currentAccount).processLoadedDialogs(var4, (ArrayList)var5, var1, var2, var3, 1, false, false, true);
               return;
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
            }
         }

         var7 = var5;
         var125 = var10000;
      }

      var4.dialogs.clear();
      var4.users.clear();
      var4.chats.clear();
      ((ArrayList)var7).clear();
      FileLog.e((Throwable)var125);
      MessagesController.getInstance(var131.currentAccount).processLoadedDialogs(var4, (ArrayList)var7, var1, 0, 100, 1, true, false, true);
   }

   // $FF: synthetic method
   public void lambda$getDownloadQueue$117$MessagesStorage(int var1) {
      Exception var10000;
      label94: {
         ArrayList var2;
         SQLiteCursor var3;
         boolean var10001;
         try {
            var2 = new ArrayList();
            var3 = this.database.queryFinalized(String.format(Locale.US, "SELECT uid, type, data, parent FROM download_queue WHERE type = %d ORDER BY date DESC LIMIT 3", var1));
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label94;
         }

         while(true) {
            DownloadObject var4;
            NativeByteBuffer var5;
            try {
               if (!var3.next()) {
                  break;
               }

               var4 = new DownloadObject();
               var4.type = var3.intValue(1);
               var4.id = var3.longValue(0);
               var4.parent = var3.stringValue(3);
               var5 = var3.byteBufferValue(2);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label94;
            }

            if (var5 != null) {
               TLRPC.MessageMedia var6;
               label97: {
                  try {
                     var6 = TLRPC.MessageMedia.TLdeserialize(var5, var5.readInt32(false), false);
                     var5.reuse();
                     if (var6.document != null) {
                        var4.object = var6.document;
                        break label97;
                     }
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label94;
                  }

                  try {
                     if (var6.photo != null) {
                        var4.object = var6.photo;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label94;
                  }
               }

               boolean var7;
               label72: {
                  label71: {
                     try {
                        if (var6.ttl_seconds > 0 && var6.ttl_seconds <= 60) {
                           break label71;
                        }
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label94;
                     }

                     var7 = false;
                     break label72;
                  }

                  var7 = true;
               }

               label62: {
                  label61: {
                     try {
                        var4.secret = var7;
                        if ((var6.flags & Integer.MIN_VALUE) == 0) {
                           break label61;
                        }
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label94;
                     }

                     var7 = true;
                     break label62;
                  }

                  var7 = false;
               }

               try {
                  var4.forceCache = var7;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label94;
               }
            }

            try {
               var2.add(var4);
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label94;
            }
         }

         try {
            var3.dispose();
            _$$Lambda$MessagesStorage$Xx8Nz7OOyNon0F2YF8EKYC_WRxQ var18 = new _$$Lambda$MessagesStorage$Xx8Nz7OOyNon0F2YF8EKYC_WRxQ(this, var1, var2);
            AndroidUtilities.runOnUIThread(var18);
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   public void lambda$getEncryptedChat$110$MessagesStorage(int var1, ArrayList var2, CountDownLatch var3) {
      try {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         StringBuilder var6 = new StringBuilder();
         var6.append("");
         var6.append(var1);
         this.getEncryptedChatsInternal(var6.toString(), var5, var4);
         if (!var5.isEmpty() && !var4.isEmpty()) {
            ArrayList var11 = new ArrayList();
            this.getUsersInternal(TextUtils.join(",", var4), var11);
            if (!var11.isEmpty()) {
               var2.add(var5.get(0));
               var2.add(var11.get(0));
            }
         }
      } catch (Exception var9) {
         FileLog.e((Throwable)var9);
      } finally {
         var3.countDown();
      }

   }

   // $FF: synthetic method
   public void lambda$getMessages$100$MessagesStorage(int param1, int param2, boolean param3, long param4, int param6, int param7, int param8, int param9, int param10) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getMessagesCount$98$MessagesStorage(long param1, MessagesStorage.IntCallback param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getNewTask$57$MessagesStorage(ArrayList var1) {
      Exception var10000;
      label68: {
         boolean var10001;
         if (var1 != null) {
            try {
               String var16 = TextUtils.join(",", var1);
               this.database.executeFast(String.format(Locale.US, "DELETE FROM enc_tasks_v2 WHERE mid IN(%s)", var16)).stepThis().dispose();
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label68;
            }
         }

         SQLiteCursor var2;
         try {
            var2 = this.database.queryFinalized("SELECT mid, date FROM enc_tasks_v2 WHERE date = (SELECT min(date) FROM enc_tasks_v2)");
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label68;
         }

         ArrayList var3 = null;
         int var4 = -1;
         int var5 = 0;

         while(true) {
            long var6;
            try {
               if (!var2.next()) {
                  break;
               }

               var6 = var2.longValue(0);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label68;
            }

            int var8 = var4;
            if (var4 == -1) {
               var4 = (int)(var6 >> 32);
               var8 = var4;
               if (var4 < 0) {
                  var8 = 0;
               }
            }

            try {
               var5 = var2.intValue(1);
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label68;
            }

            var1 = var3;
            if (var3 == null) {
               try {
                  var1 = new ArrayList();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label68;
               }
            }

            try {
               var1.add((int)var6);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label68;
            }

            var3 = var1;
            var4 = var8;
         }

         try {
            var2.dispose();
            MessagesController.getInstance(this.currentAccount).processLoadedDeleteTask(var5, var3, var4);
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   public void lambda$getSentFile$102$MessagesStorage(String param1, int param2, Object[] param3, CountDownLatch param4) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getUnreadMention$96$MessagesStorage(long param1, MessagesStorage.IntCallback param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$getUnsentMessages$92$MessagesStorage(int var1) {
      Exception var10000;
      label234: {
         SparseArray var2;
         ArrayList var3;
         ArrayList var4;
         ArrayList var5;
         ArrayList var6;
         ArrayList var7;
         ArrayList var8;
         ArrayList var9;
         ArrayList var10;
         boolean var10001;
         SQLiteCursor var44;
         try {
            var2 = new SparseArray();
            var3 = new ArrayList();
            var4 = new ArrayList();
            var5 = new ArrayList();
            var6 = new ArrayList();
            var7 = new ArrayList();
            var8 = new ArrayList();
            var9 = new ArrayList();
            var10 = new ArrayList();
            SQLiteDatabase var11 = this.database;
            StringBuilder var12 = new StringBuilder();
            var12.append("SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.uid, s.seq_in, s.seq_out, m.ttl FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid LEFT JOIN messages_seq as s ON m.mid = s.mid WHERE (m.mid < 0 AND m.send_state = 1) OR (m.mid > 0 AND m.send_state = 3) ORDER BY m.mid DESC LIMIT ");
            var12.append(var1);
            var44 = var11.queryFinalized(var12.toString());
         } catch (Exception var31) {
            var10000 = var31;
            var10001 = false;
            break label234;
         }

         int var14;
         while(true) {
            NativeByteBuffer var13;
            try {
               if (!var44.next()) {
                  break;
               }

               var13 = var44.byteBufferValue(1);
            } catch (Exception var39) {
               var10000 = var39;
               var10001 = false;
               break label234;
            }

            if (var13 != null) {
               TLRPC.Message var45;
               try {
                  var45 = TLRPC.Message.TLdeserialize(var13, var13.readInt32(false), false);
                  var45.send_state = var44.intValue(2);
                  var45.readAttachPath(var13, UserConfig.getInstance(this.currentAccount).clientUserId);
                  var13.reuse();
                  if (var2.indexOfKey(var45.id) >= 0) {
                     continue;
                  }

                  MessageObject.setUnreadFlags(var45, var44.intValue(0));
                  var45.id = var44.intValue(3);
                  var45.date = var44.intValue(4);
                  if (!var44.isNull(5)) {
                     var45.random_id = var44.longValue(5);
                  }
               } catch (Exception var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label234;
               }

               try {
                  var45.dialog_id = var44.longValue(6);
                  var45.seq_in = var44.intValue(7);
                  var45.seq_out = var44.intValue(8);
                  var45.ttl = var44.intValue(9);
                  var3.add(var45);
                  var2.put(var45.id, var45);
                  var1 = (int)var45.dialog_id;
                  var14 = (int)(var45.dialog_id >> 32);
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label234;
               }

               if (var1 != 0) {
                  if (var14 == 1) {
                     try {
                        if (!var9.contains(var1)) {
                           var9.add(var1);
                        }
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break label234;
                     }
                  } else if (var1 < 0) {
                     var14 = -var1;

                     try {
                        if (!var8.contains(var14)) {
                           var8.add(var14);
                        }
                     } catch (Exception var37) {
                        var10000 = var37;
                        var10001 = false;
                        break label234;
                     }
                  } else {
                     try {
                        if (!var7.contains(var1)) {
                           var7.add(var1);
                        }
                     } catch (Exception var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label234;
                     }
                  }
               } else {
                  try {
                     if (!var10.contains(var14)) {
                        var10.add(var14);
                     }
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label234;
                  }
               }

               label201: {
                  label200: {
                     label199: {
                        try {
                           addUsersAndChatsFromMessage(var45, var7, var8);
                           if (var45.send_state == 3) {
                              break label201;
                           }

                           if (var45.to_id.channel_id != 0 || MessageObject.isUnread(var45)) {
                              break label199;
                           }
                        } catch (Exception var35) {
                           var10000 = var35;
                           var10001 = false;
                           break label234;
                        }

                        if (var1 != 0) {
                           break label200;
                        }
                     }

                     try {
                        if (var45.id <= 0) {
                           break label201;
                        }
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break label234;
                     }
                  }

                  try {
                     var45.send_state = 0;
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label234;
                  }
               }

               if (var1 == 0) {
                  try {
                     if (!var44.isNull(5)) {
                        var45.random_id = var44.longValue(5);
                     }
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label234;
                  }
               }
            }
         }

         byte var15 = 0;

         boolean var16;
         try {
            var44.dispose();
            var16 = var10.isEmpty();
         } catch (Exception var27) {
            var10000 = var27;
            var10001 = false;
            break label234;
         }

         if (!var16) {
            try {
               this.getEncryptedChatsInternal(TextUtils.join(",", var10), var6, var7);
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label234;
            }
         }

         try {
            if (!var7.isEmpty()) {
               this.getUsersInternal(TextUtils.join(",", var7), var4);
            }
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break label234;
         }

         label239: {
            try {
               if (var8.isEmpty() && var9.isEmpty()) {
                  break label239;
               }
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label234;
            }

            StringBuilder var42;
            try {
               var42 = new StringBuilder();
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label234;
            }

            var14 = 0;

            while(true) {
               var1 = var15;

               Integer var40;
               try {
                  if (var14 >= var8.size()) {
                     break;
                  }

                  var40 = (Integer)var8.get(var14);
                  if (var42.length() != 0) {
                     var42.append(",");
                  }
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label234;
               }

               try {
                  var42.append(var40);
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label234;
               }

               ++var14;
            }

            while(true) {
               Integer var43;
               try {
                  if (var1 >= var9.size()) {
                     break;
                  }

                  var43 = (Integer)var9.get(var1);
                  if (var42.length() != 0) {
                     var42.append(",");
                  }
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label234;
               }

               try {
                  var42.append(-var43);
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label234;
               }

               ++var1;
            }

            try {
               this.getChatsInternal(var42.toString(), var5);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label234;
            }
         }

         try {
            SendMessagesHelper.getInstance(this.currentAccount).processUnsentMessages(var3, var4, var5, var6);
            return;
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
         }
      }

      Exception var41 = var10000;
      FileLog.e((Throwable)var41);
   }

   // $FF: synthetic method
   public void lambda$getUserSync$151$MessagesStorage(TLRPC.User[] var1, int var2, CountDownLatch var3) {
      var1[0] = this.getUser(var2);
      var3.countDown();
   }

   // $FF: synthetic method
   public void lambda$getWallpapers$32$MessagesStorage() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$hasAuthMessage$109$MessagesStorage(int var1, boolean[] var2, CountDownLatch var3) {
      try {
         SQLiteCursor var4 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM messages WHERE uid = 777000 AND date = %d AND mid < 0 LIMIT 1", var1));
         var2[0] = var4.next();
         var4.dispose();
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      } finally {
         var3.countDown();
      }

   }

   // $FF: synthetic method
   public void lambda$isDialogHasMessages$108$MessagesStorage(long var1, boolean[] var3, CountDownLatch var4) {
      try {
         SQLiteCursor var5 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM messages WHERE uid = %d LIMIT 1", var1));
         var3[0] = var5.next();
         var5.dispose();
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
      } finally {
         var4.countDown();
      }

   }

   // $FF: synthetic method
   public void lambda$isMigratedChat$83$MessagesStorage(int param1, boolean[] param2, CountDownLatch param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadChannelAdmins$68$MessagesStorage(int param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadChatInfo$84$MessagesStorage(int param1, CountDownLatch param2, boolean param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadPendingTasks$21$MessagesStorage() {
      Exception var10000;
      label259: {
         SQLiteCursor var1;
         boolean var10001;
         try {
            var1 = this.database.queryFinalized("SELECT id, data FROM pending_tasks WHERE 1");
         } catch (Exception var51) {
            var10000 = var51;
            var10001 = false;
            break label259;
         }

         while(true) {
            long var2;
            NativeByteBuffer var4;
            try {
               if (!var1.next()) {
                  break;
               }

               var2 = var1.longValue(0);
               var4 = var1.byteBufferValue(1);
            } catch (Exception var52) {
               var10000 = var52;
               var10001 = false;
               break label259;
            }

            if (var4 != null) {
               int var5;
               try {
                  var5 = var4.readInt32(false);
               } catch (Exception var50) {
                  var10000 = var50;
                  var10001 = false;
                  break label259;
               }

               int var6;
               int var7;
               long var11;
               boolean var13;
               boolean var14;
               TLRPC.InputPeer var55;
               TLRPC.InputPeer var62;
               NativeByteBuffer var63;
               DispatchQueue var66;
               switch(var5) {
               case 0:
                  var63 = var4;

                  TLRPC.Chat var72;
                  try {
                     var72 = TLRPC.Chat.TLdeserialize(var63, var63.readInt32(false), false);
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label259;
                  }

                  if (var72 != null) {
                     try {
                        DispatchQueue var69 = Utilities.stageQueue;
                        _$$Lambda$MessagesStorage$X8wmcVmkWlOOmT7hyCwq34C_3HA var78 = new _$$Lambda$MessagesStorage$X8wmcVmkWlOOmT7hyCwq34C_3HA(this, var72, var2);
                        var69.postRunnable(var78);
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label259;
                     }
                  }
                  break;
               case 1:
                  var63 = var4;

                  try {
                     var7 = var63.readInt32(false);
                     var5 = var63.readInt32(false);
                     var66 = Utilities.stageQueue;
                     _$$Lambda$MessagesStorage$0pEDcAF09KSwzKWD9sU7FMqhzD0 var77 = new _$$Lambda$MessagesStorage$0pEDcAF09KSwzKWD9sU7FMqhzD0(this, var7, var5, var2);
                     var66.postRunnable(var77);
                     break;
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label259;
                  }
               case 2:
               case 5:
               case 8:
               case 10:
               case 14:
                  var63 = var4;

                  TLRPC.TL_dialog var71;
                  try {
                     var71 = new TLRPC.TL_dialog();
                     var71.id = var63.readInt64(false);
                     var71.top_message = var63.readInt32(false);
                     var71.read_inbox_max_id = var63.readInt32(false);
                     var71.read_outbox_max_id = var63.readInt32(false);
                     var71.unread_count = var63.readInt32(false);
                     var71.last_message_date = var63.readInt32(false);
                     var71.pts = var63.readInt32(false);
                     var71.flags = var63.readInt32(false);
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label259;
                  }

                  if (var5 >= 5) {
                     try {
                        var71.pinned = var63.readBool(false);
                        var71.pinnedNum = var63.readInt32(false);
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label259;
                     }
                  }

                  if (var5 >= 8) {
                     try {
                        var71.unread_mentions_count = var63.readInt32(false);
                     } catch (Exception var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label259;
                     }
                  }

                  if (var5 >= 10) {
                     try {
                        var71.unread_mark = var63.readBool(false);
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label259;
                     }
                  }

                  if (var5 >= 14) {
                     try {
                        var71.folder_id = var63.readInt32(false);
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label259;
                     }
                  }

                  try {
                     var62 = TLRPC.InputPeer.TLdeserialize(var63, var63.readInt32(false), false);
                     _$$Lambda$MessagesStorage$uwK8wtI_v95kHeLcf4gXEkrDfLc var68 = new _$$Lambda$MessagesStorage$uwK8wtI_v95kHeLcf4gXEkrDfLc(this, var71, var62, var2);
                     AndroidUtilities.runOnUIThread(var68);
                     break;
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label259;
                  }
               case 3:
                  var63 = var4;

                  try {
                     var11 = var63.readInt64(false);
                     var55 = TLRPC.InputPeer.TLdeserialize(var63, var63.readInt32(false), false);
                     TLRPC.TL_inputMediaGame var76 = (TLRPC.TL_inputMediaGame)TLRPC.InputMedia.TLdeserialize(var63, var63.readInt32(false), false);
                     SendMessagesHelper.getInstance(this.currentAccount).sendGame(var55, var76, var11, var2);
                     break;
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label259;
                  }
               case 4:
                  var63 = var4;

                  try {
                     var11 = var63.readInt64(false);
                     var14 = var63.readBool(false);
                     var55 = TLRPC.InputPeer.TLdeserialize(var63, var63.readInt32(false), false);
                     _$$Lambda$MessagesStorage$AuH1gRs2iXmoSCC0c3xvFDAEwcM var75 = new _$$Lambda$MessagesStorage$AuH1gRs2iXmoSCC0c3xvFDAEwcM(this, var11, var14, var55, var2);
                     AndroidUtilities.runOnUIThread(var75);
                     break;
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label259;
                  }
               case 6:
                  var63 = var4;

                  try {
                     var5 = var63.readInt32(false);
                     var7 = var63.readInt32(false);
                     TLRPC.InputChannel var64 = TLRPC.InputChannel.TLdeserialize(var63, var63.readInt32(false), false);
                     var66 = Utilities.stageQueue;
                     _$$Lambda$MessagesStorage$veltQ_QzWYSSmgAGGDUTY_jvHoM var74 = new _$$Lambda$MessagesStorage$veltQ_QzWYSSmgAGGDUTY_jvHoM(this, var5, var7, var2, var64);
                     var66.postRunnable(var74);
                     break;
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label259;
                  }
               case 7:
                  NativeByteBuffer var61 = var4;

                  Object var73;
                  try {
                     var7 = var61.readInt32(false);
                     var5 = var61.readInt32(false);
                     var73 = TLRPC.TL_messages_deleteMessages.TLdeserialize(var61, var5, false);
                  } catch (Exception var36) {
                     var10000 = var36;
                     var10001 = false;
                     break label259;
                  }

                  if (var73 == null) {
                     try {
                        var73 = TLRPC.TL_channels_deleteMessages.TLdeserialize(var61, var5, false);
                     } catch (Exception var35) {
                        var10000 = var35;
                        var10001 = false;
                        break label259;
                     }
                  }

                  if (var73 == null) {
                     try {
                        this.removePendingTask(var2);
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break label259;
                     }
                  } else {
                     try {
                        _$$Lambda$MessagesStorage$EuuPlYsIg_jeReoMzIbm6stO0ag var65 = new _$$Lambda$MessagesStorage$EuuPlYsIg_jeReoMzIbm6stO0ag(this, var7, var2, (TLObject)var73);
                        AndroidUtilities.runOnUIThread(var65);
                     } catch (Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label259;
                     }
                  }
                  break;
               case 9:
                  var63 = var4;

                  try {
                     var11 = var63.readInt64(false);
                     var62 = TLRPC.InputPeer.TLdeserialize(var63, var63.readInt32(false), false);
                     _$$Lambda$MessagesStorage$OTCbnyNXoirwhvbsu8TFVflzodM var60 = new _$$Lambda$MessagesStorage$OTCbnyNXoirwhvbsu8TFVflzodM(this, var11, var62, var2);
                     AndroidUtilities.runOnUIThread(var60);
                     break;
                  } catch (Exception var37) {
                     var10000 = var37;
                     var10001 = false;
                     break label259;
                  }
               case 11:
                  var63 = var4;

                  try {
                     var5 = var63.readInt32(false);
                     var7 = var63.readInt32(false);
                     var6 = var63.readInt32(false);
                  } catch (Exception var40) {
                     var10000 = var40;
                     var10001 = false;
                     break label259;
                  }

                  TLRPC.InputChannel var70;
                  if (var7 != 0) {
                     try {
                        var70 = TLRPC.InputChannel.TLdeserialize(var63, var63.readInt32(false), false);
                     } catch (Exception var39) {
                        var10000 = var39;
                        var10001 = false;
                        break label259;
                     }
                  } else {
                     var70 = null;
                  }

                  try {
                     _$$Lambda$MessagesStorage$TqvcAvYPfh_Cio_d66SYceSFjMQ var58 = new _$$Lambda$MessagesStorage$TqvcAvYPfh_Cio_d66SYceSFjMQ(this, var5, var7, var70, var6, var2);
                     AndroidUtilities.runOnUIThread(var58);
                     break;
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label259;
                  }
               case 12:
                  var63 = var4;

                  try {
                     var11 = var63.readInt64(false);
                     long var15 = var63.readInt64(false);
                     boolean var17 = var63.readBool(false);
                     var14 = var63.readBool(false);
                     var5 = var63.readInt32(false);
                     float var18 = (float)var63.readDouble(false);
                     var13 = var63.readBool(false);
                     _$$Lambda$MessagesStorage$86hFXP2JtkqWJ8sHKPYBQGuj0fs var67 = new _$$Lambda$MessagesStorage$86hFXP2JtkqWJ8sHKPYBQGuj0fs(this, var11, var15, var17, var14, var5, var18, var13, var2);
                     AndroidUtilities.runOnUIThread(var67);
                     break;
                  } catch (Exception var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label259;
                  }
               case 13:
                  try {
                     var11 = var4.readInt64(false);
                     var13 = var4.readBool(false);
                     var7 = var4.readInt32(false);
                     var5 = var4.readInt32(false);
                     var14 = var4.readBool(false);
                     var62 = TLRPC.InputPeer.TLdeserialize(var4, var4.readInt32(false), false);
                     _$$Lambda$MessagesStorage$gZU7uqq34_u60ckwe65AH41ydVA var56 = new _$$Lambda$MessagesStorage$gZU7uqq34_u60ckwe65AH41ydVA(this, var11, var13, var7, var5, var14, var62, var2);
                     AndroidUtilities.runOnUIThread(var56);
                     break;
                  } catch (Exception var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label259;
                  }
               case 15:
                  try {
                     var55 = TLRPC.InputPeer.TLdeserialize(var4, var4.readInt32(false), false);
                     DispatchQueue var59 = Utilities.stageQueue;
                     _$$Lambda$MessagesStorage$tZ31OdkMW8PKLVCYnMiDI_DfRTs var10 = new _$$Lambda$MessagesStorage$tZ31OdkMW8PKLVCYnMiDI_DfRTs(this, var55, var2);
                     var59.postRunnable(var10);
                     break;
                  } catch (Exception var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label259;
                  }
               case 16:
                  ArrayList var57;
                  try {
                     var6 = var4.readInt32(false);
                     var7 = var4.readInt32(false);
                     var57 = new ArrayList();
                  } catch (Exception var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label259;
                  }

                  for(var5 = 0; var5 < var7; ++var5) {
                     try {
                        var57.add(TLRPC.InputDialogPeer.TLdeserialize(var4, var4.readInt32(false), false));
                     } catch (Exception var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label259;
                     }
                  }

                  try {
                     _$$Lambda$MessagesStorage$KOMsQ0FDjAutn_YKgmf9ukUIQZQ var54 = new _$$Lambda$MessagesStorage$KOMsQ0FDjAutn_YKgmf9ukUIQZQ(this, var6, var57, var2);
                     AndroidUtilities.runOnUIThread(var54);
                     break;
                  } catch (Exception var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label259;
                  }
               case 17:
                  ArrayList var8;
                  try {
                     var6 = var4.readInt32(false);
                     var7 = var4.readInt32(false);
                     var8 = new ArrayList();
                  } catch (Exception var49) {
                     var10000 = var49;
                     var10001 = false;
                     break label259;
                  }

                  for(var5 = 0; var5 < var7; ++var5) {
                     try {
                        var8.add(TLRPC.TL_inputFolderPeer.TLdeserialize(var4, var4.readInt32(false), false));
                     } catch (Exception var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label259;
                     }
                  }

                  try {
                     _$$Lambda$MessagesStorage$LtM1spYCOokckYn9GalfKw21eVw var9 = new _$$Lambda$MessagesStorage$LtM1spYCOokckYn9GalfKw21eVw(this, var6, var8, var2);
                     AndroidUtilities.runOnUIThread(var9);
                  } catch (Exception var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label259;
                  }
               }

               try {
                  var4.reuse();
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label259;
               }
            }
         }

         try {
            var1.dispose();
            return;
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
         }
      }

      Exception var53 = var10000;
      FileLog.e((Throwable)var53);
   }

   // $FF: synthetic method
   public void lambda$loadUnreadMessages$29$MessagesStorage() {
      Exception var10000;
      label546: {
         ArrayList var1;
         ArrayList var2;
         ArrayList var3;
         LongSparseArray var4;
         SQLiteCursor var5;
         StringBuilder var6;
         int var7;
         boolean var10001;
         try {
            var1 = new ArrayList();
            var2 = new ArrayList();
            var3 = new ArrayList();
            var4 = new LongSparseArray();
            var5 = this.database.queryFinalized("SELECT d.did, d.unread_count, s.flags FROM dialogs as d LEFT JOIN dialog_settings as s ON d.did = s.did WHERE d.unread_count != 0");
            var6 = new StringBuilder();
            var7 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         } catch (Exception var72) {
            var10000 = var72;
            var10001 = false;
            break label546;
         }

         label545:
         while(true) {
            boolean var8;
            try {
               var8 = var5.next();
            } catch (Exception var69) {
               var10000 = var69;
               var10001 = false;
               break;
            }

            String var9 = ",";
            long var10;
            int var13;
            int var90;
            if (!var8) {
               ArrayList var17;
               ArrayList var18;
               ArrayList var19;
               ArrayList var79;
               ArrayList var85;
               label494: {
                  ArrayList var16;
                  label567: {
                     ArrayList var14;
                     SparseArray var15;
                     SQLiteDatabase var20;
                     StringBuilder var21;
                     try {
                        var5.dispose();
                        var14 = new ArrayList();
                        var15 = new SparseArray();
                        var16 = new ArrayList();
                        var17 = new ArrayList();
                        var18 = new ArrayList();
                        var19 = new ArrayList();
                        var79 = new ArrayList();
                        if (var6.length() <= 0) {
                           break label567;
                        }

                        var20 = this.database;
                        var21 = new StringBuilder();
                     } catch (Exception var68) {
                        var10000 = var68;
                        var10001 = false;
                        break;
                     }

                     SQLiteCursor var94;
                     try {
                        var21.append("SELECT read_state, data, send_state, mid, date, uid, replydata FROM messages WHERE uid IN (");
                        var21.append(var6.toString());
                        var21.append(") AND out = 0 AND read_state IN(0,2) ORDER BY date DESC LIMIT 50");
                        var94 = var20.queryFinalized(var21.toString());
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break;
                     }

                     var90 = 0;

                     TLRPC.Message var22;
                     NativeByteBuffer var81;
                     label485:
                     while(true) {
                        do {
                           try {
                              if (!var94.next()) {
                                 break label485;
                              }

                              var81 = var94.byteBufferValue(1);
                           } catch (Exception var56) {
                              var10000 = var56;
                              var10001 = false;
                              break label545;
                           }
                        } while(var81 == null);

                        label427: {
                           label426: {
                              label425: {
                                 try {
                                    var22 = TLRPC.Message.TLdeserialize(var81, var81.readInt32(false), false);
                                    var22.readAttachPath(var81, UserConfig.getInstance(this.currentAccount).clientUserId);
                                    var81.reuse();
                                    MessageObject.setUnreadFlags(var22, var94.intValue(0));
                                    var22.id = var94.intValue(3);
                                    var22.date = var94.intValue(4);
                                    var22.dialog_id = var94.longValue(5);
                                    var16.add(var22);
                                    var90 = Math.max(var90, var22.date);
                                    var7 = (int)var22.dialog_id;
                                    addUsersAndChatsFromMessage(var22, var1, var2);
                                    var22.send_state = var94.intValue(2);
                                    if (var22.to_id.channel_id != 0 || MessageObject.isUnread(var22)) {
                                       break label425;
                                    }
                                 } catch (Exception var58) {
                                    var10000 = var58;
                                    var10001 = false;
                                    break label545;
                                 }

                                 if (var7 != 0) {
                                    break label426;
                                 }
                              }

                              try {
                                 if (var22.id <= 0) {
                                    break label427;
                                 }
                              } catch (Exception var57) {
                                 var10000 = var57;
                                 var10001 = false;
                                 break label545;
                              }
                           }

                           try {
                              var22.send_state = 0;
                           } catch (Exception var41) {
                              var10000 = var41;
                              var10001 = false;
                              break label545;
                           }
                        }

                        if (var7 == 0) {
                           try {
                              if (!var94.isNull(5)) {
                                 var22.random_id = var94.longValue(5);
                              }
                           } catch (Exception var40) {
                              var10000 = var40;
                              var10001 = false;
                              break label545;
                           }
                        }

                        Exception var84;
                        label555: {
                           label481: {
                              label480: {
                                 label556: {
                                    try {
                                       if (var22.reply_to_msg_id == 0 || !(var22.action instanceof TLRPC.TL_messageActionPinMessage) && !(var22.action instanceof TLRPC.TL_messageActionPaymentSent) && !(var22.action instanceof TLRPC.TL_messageActionGameScore)) {
                                          continue;
                                       }
                                    } catch (Exception var67) {
                                       var10000 = var67;
                                       var10001 = false;
                                       break label556;
                                    }

                                    label475: {
                                       try {
                                          if (var94.isNull(6)) {
                                             break label475;
                                          }

                                          var81 = var94.byteBufferValue(6);
                                       } catch (Exception var66) {
                                          var10000 = var66;
                                          var10001 = false;
                                          break label556;
                                       }

                                       if (var81 != null) {
                                          label469: {
                                             try {
                                                var22.replyMessage = TLRPC.Message.TLdeserialize(var81, var81.readInt32(false), false);
                                                var22.replyMessage.readAttachPath(var81, UserConfig.getInstance(this.currentAccount).clientUserId);
                                                var81.reuse();
                                                if (var22.replyMessage == null) {
                                                   break label469;
                                                }

                                                if (MessageObject.isMegagroup(var22)) {
                                                   TLRPC.Message var83 = var22.replyMessage;
                                                   var83.flags |= Integer.MIN_VALUE;
                                                }
                                             } catch (Exception var65) {
                                                var10000 = var65;
                                                var10001 = false;
                                                break label556;
                                             }

                                             try {
                                                addUsersAndChatsFromMessage(var22.replyMessage, var1, var2);
                                             } catch (Exception var64) {
                                                var10000 = var64;
                                                var10001 = false;
                                                break label556;
                                             }
                                          }
                                       }
                                    }

                                    try {
                                       if (var22.replyMessage != null) {
                                          continue;
                                       }

                                       var10 = (long)var22.reply_to_msg_id;
                                       if (var22.to_id.channel_id == 0) {
                                          break label481;
                                       }

                                       var7 = var22.to_id.channel_id;
                                       break label480;
                                    } catch (Exception var63) {
                                       var10000 = var63;
                                       var10001 = false;
                                    }
                                 }

                                 var84 = var10000;
                                 break label555;
                              }

                              var10 |= (long)var7 << 32;
                           }

                           label559: {
                              try {
                                 if (!var14.contains(var10)) {
                                    var14.add(var10);
                                 }
                              } catch (Exception var62) {
                                 var10000 = var62;
                                 var10001 = false;
                                 break label559;
                              }

                              ArrayList var96;
                              try {
                                 var96 = (ArrayList)var15.get(var22.reply_to_msg_id);
                              } catch (Exception var61) {
                                 var10000 = var61;
                                 var10001 = false;
                                 break label559;
                              }

                              var85 = var96;
                              if (var96 == null) {
                                 try {
                                    var85 = new ArrayList();
                                    var15.put(var22.reply_to_msg_id, var85);
                                 } catch (Exception var60) {
                                    var10000 = var60;
                                    var10001 = false;
                                    break label559;
                                 }
                              }

                              try {
                                 var85.add(var22);
                                 continue;
                              } catch (Exception var59) {
                                 var10000 = var59;
                                 var10001 = false;
                              }
                           }

                           var84 = var10000;
                        }

                        try {
                           FileLog.e((Throwable)var84);
                        } catch (Exception var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label545;
                        }
                     }

                     SQLiteCursor var23;
                     try {
                        var94.dispose();
                        SQLiteDatabase var86 = this.database;
                        var21 = new StringBuilder();
                        var21.append("DELETE FROM unread_push_messages WHERE date <= ");
                        var21.append(var90);
                        var86.executeFast(var21.toString()).stepThis().dispose();
                        var23 = this.database.queryFinalized("SELECT data, mid, date, uid, random, fm, name, uname, flags FROM unread_push_messages WHERE 1 ORDER BY date DESC LIMIT 50");
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break;
                     }

                     label404:
                     while(true) {
                        do {
                           try {
                              if (!var23.next()) {
                                 break label404;
                              }

                              var81 = var23.byteBufferValue(0);
                           } catch (Exception var53) {
                              var10000 = var53;
                              var10001 = false;
                              break label545;
                           }
                        } while(var81 == null);

                        try {
                           var22 = TLRPC.Message.TLdeserialize(var81, var81.readInt32(false), false);
                           var81.reuse();
                           var22.id = var23.intValue(1);
                           var22.date = var23.intValue(2);
                           var22.dialog_id = var23.longValue(3);
                           var22.random_id = var23.longValue(4);
                           var8 = var23.isNull(5);
                        } catch (Exception var37) {
                           var10000 = var37;
                           var10001 = false;
                           break label545;
                        }

                        String var95 = null;
                        String var88;
                        if (var8) {
                           var88 = null;
                        } else {
                           try {
                              var88 = var23.stringValue(5);
                           } catch (Exception var36) {
                              var10000 = var36;
                              var10001 = false;
                              break label545;
                           }
                        }

                        String var99;
                        label393: {
                           label392: {
                              try {
                                 if (var23.isNull(6)) {
                                    break label392;
                                 }
                              } catch (Exception var54) {
                                 var10000 = var54;
                                 var10001 = false;
                                 break label545;
                              }

                              try {
                                 var99 = var23.stringValue(6);
                                 break label393;
                              } catch (Exception var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label545;
                              }
                           }

                           var99 = null;
                        }

                        label575: {
                           label399:
                           try {
                              if (!var23.isNull(7)) {
                                 break label399;
                              }
                              break label575;
                           } catch (Exception var55) {
                              var10000 = var55;
                              var10001 = false;
                              break label545;
                           }

                           try {
                              var95 = var23.stringValue(7);
                           } catch (Exception var34) {
                              var10000 = var34;
                              var10001 = false;
                              break label545;
                           }
                        }

                        MessageObject var24;
                        try {
                           var90 = var23.intValue(8);
                           var24 = new MessageObject;
                           var7 = this.currentAccount;
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label545;
                        }

                        if ((var90 & 1) != 0) {
                           var8 = true;
                        } else {
                           var8 = false;
                        }

                        boolean var25;
                        if ((var90 & 2) != 0) {
                           var25 = true;
                        } else {
                           var25 = false;
                        }

                        try {
                           var24.<init>(var7, var22, var88, var99, var95, var8, var25, false);
                           var17.add(var24);
                           addUsersAndChatsFromMessage(var22, var1, var2);
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label545;
                        }
                     }

                     label561: {
                        SQLiteCursor var89;
                        try {
                           var23.dispose();
                           if (var14.isEmpty()) {
                              break label561;
                           }

                           var89 = this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM messages WHERE mid IN(%s)", TextUtils.join(var9, var14)));
                        } catch (Exception var50) {
                           var10000 = var50;
                           var10001 = false;
                           break;
                        }

                        while(true) {
                           NativeByteBuffer var97;
                           try {
                              if (!var89.next()) {
                                 break;
                              }

                              var97 = var89.byteBufferValue(0);
                           } catch (Exception var52) {
                              var10000 = var52;
                              var10001 = false;
                              break label545;
                           }

                           if (var97 != null) {
                              ArrayList var98;
                              TLRPC.Message var101;
                              try {
                                 var101 = TLRPC.Message.TLdeserialize(var97, var97.readInt32(false), false);
                                 var101.readAttachPath(var97, UserConfig.getInstance(this.currentAccount).clientUserId);
                                 var97.reuse();
                                 var101.id = var89.intValue(1);
                                 var101.date = var89.intValue(2);
                                 var101.dialog_id = var89.longValue(3);
                                 addUsersAndChatsFromMessage(var101, var1, var2);
                                 var98 = (ArrayList)var15.get(var101.id);
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break label545;
                              }

                              if (var98 != null) {
                                 var90 = 0;

                                 while(true) {
                                    try {
                                       if (var90 >= var98.size()) {
                                          break;
                                       }

                                       TLRPC.Message var91 = (TLRPC.Message)var98.get(var90);
                                       var91.replyMessage = var101;
                                       if (MessageObject.isMegagroup(var91)) {
                                          var91 = var91.replyMessage;
                                          var91.flags |= Integer.MIN_VALUE;
                                       }
                                    } catch (Exception var51) {
                                       var10000 = var51;
                                       var10001 = false;
                                       break label545;
                                    }

                                    ++var90;
                                 }
                              }
                           }
                        }

                        try {
                           var89.dispose();
                        } catch (Exception var30) {
                           var10000 = var30;
                           var10001 = false;
                           break;
                        }
                     }

                     try {
                        if (!var3.isEmpty()) {
                           this.getEncryptedChatsInternal(TextUtils.join(var9, var3), var79, var1);
                        }
                     } catch (Exception var49) {
                        var10000 = var49;
                        var10001 = false;
                        break;
                     }

                     var85 = var79;

                     try {
                        if (!var1.isEmpty()) {
                           this.getUsersInternal(TextUtils.join(var9, var1), var18);
                        }
                     } catch (Exception var48) {
                        var10000 = var48;
                        var10001 = false;
                        break;
                     }

                     label564: {
                        String var80;
                        try {
                           if (var2.isEmpty()) {
                              break label564;
                           }

                           var80 = TextUtils.join(var9, var2);
                        } catch (Exception var47) {
                           var10000 = var47;
                           var10001 = false;
                           break;
                        }

                        ArrayList var87 = var19;

                        try {
                           this.getChatsInternal(var80, var87);
                        } catch (Exception var29) {
                           var10000 = var29;
                           var10001 = false;
                           break;
                        }

                        var90 = 0;
                        var79 = var16;

                        while(true) {
                           TLRPC.Chat var92;
                           try {
                              if (var90 >= var87.size()) {
                                 break label494;
                              }

                              var92 = (TLRPC.Chat)var87.get(var90);
                           } catch (Exception var43) {
                              var10000 = var43;
                              var10001 = false;
                              break label545;
                           }

                           if (var92 != null) {
                              label571: {
                                 try {
                                    if (!var92.left && var92.migrated_to == null) {
                                       break label571;
                                    }
                                 } catch (Exception var46) {
                                    var10000 = var46;
                                    var10001 = false;
                                    break label545;
                                 }

                                 try {
                                    var10 = (long)(-var92.id);
                                    SQLiteDatabase var102 = this.database;
                                    StringBuilder var100 = new StringBuilder();
                                    var100.append("UPDATE dialogs SET unread_count = 0 WHERE did = ");
                                    var100.append(var10);
                                    var102.executeFast(var100.toString()).stepThis().dispose();
                                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = 3 WHERE uid = %d AND mid > 0 AND read_state IN(0,2) AND out = 0", var10)).stepThis().dispose();
                                    var87.remove(var90);
                                 } catch (Exception var28) {
                                    var10000 = var28;
                                    var10001 = false;
                                    break label545;
                                 }

                                 var13 = var90 - 1;

                                 try {
                                    var4.remove((long)(-var92.id));
                                 } catch (Exception var27) {
                                    var10000 = var27;
                                    var10001 = false;
                                    break label545;
                                 }

                                 var90 = 0;

                                 while(true) {
                                    try {
                                       if (var90 >= var79.size()) {
                                          break;
                                       }
                                    } catch (Exception var44) {
                                       var10000 = var44;
                                       var10001 = false;
                                       break label545;
                                    }

                                    var7 = var90;

                                    label319: {
                                       try {
                                          if (((TLRPC.Message)var79.get(var90)).dialog_id != (long)(-var92.id)) {
                                             break label319;
                                          }

                                          var79.remove(var90);
                                       } catch (Exception var45) {
                                          var10000 = var45;
                                          var10001 = false;
                                          break label545;
                                       }

                                       var7 = var90 - 1;
                                    }

                                    var90 = var7 + 1;
                                 }

                                 var90 = var13;
                              }
                           }

                           ++var90;
                        }
                     }

                     var79 = var16;
                     break label494;
                  }

                  var85 = var79;
                  var79 = var16;
               }

               try {
                  Collections.reverse(var79);
                  _$$Lambda$MessagesStorage$S3bj_aGKTgw9UejHP75FZtyG2ms var93 = new _$$Lambda$MessagesStorage$S3bj_aGKTgw9UejHP75FZtyG2ms(this, var4, var79, var17, var18, var19, var85);
                  AndroidUtilities.runOnUIThread(var93);
                  return;
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break;
               }
            }

            try {
               var10 = var5.longValue(2);
            } catch (Exception var71) {
               var10000 = var71;
               var10001 = false;
               break;
            }

            boolean var12;
            if ((var10 & 1L) != 0L) {
               var12 = true;
            } else {
               var12 = false;
            }

            var13 = (int)(var10 >> 32);

            label539: {
               try {
                  if (var5.isNull(2)) {
                     break label539;
                  }
               } catch (Exception var78) {
                  var10000 = var78;
                  var10001 = false;
                  break;
               }

               if (var12 && (var13 == 0 || var13 >= var7)) {
                  continue;
               }
            }

            label533:
            try {
               var10 = var5.longValue(0);
               if (!DialogObject.isFolderDialogId(var10)) {
                  break label533;
               }
               continue;
            } catch (Exception var77) {
               var10000 = var77;
               var10001 = false;
               break;
            }

            try {
               var4.put(var10, var5.intValue(1));
               if (var6.length() != 0) {
                  var6.append(",");
               }
            } catch (Exception var76) {
               var10000 = var76;
               var10001 = false;
               break;
            }

            try {
               var6.append(var10);
            } catch (Exception var70) {
               var10000 = var70;
               var10001 = false;
               break;
            }

            var13 = (int)var10;
            var90 = (int)(var10 >> 32);
            if (var13 != 0) {
               if (var13 < 0) {
                  var90 = -var13;

                  try {
                     if (!var2.contains(var90)) {
                        var2.add(var90);
                     }
                  } catch (Exception var73) {
                     var10000 = var73;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     if (!var1.contains(var13)) {
                        var1.add(var13);
                     }
                  } catch (Exception var74) {
                     var10000 = var74;
                     var10001 = false;
                     break;
                  }
               }
            } else {
               try {
                  if (!var3.contains(var90)) {
                     var3.add(var90);
                  }
               } catch (Exception var75) {
                  var10000 = var75;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      Exception var82 = var10000;
      FileLog.e((Throwable)var82);
   }

   // $FF: synthetic method
   public void lambda$loadUserInfo$73$MessagesStorage(TLRPC.User param1, boolean param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadWebRecent$34$MessagesStorage(int var1) {
      try {
         ArrayList var2 = new ArrayList();
         _$$Lambda$MessagesStorage$lXEFOLx65JNFN3MBWdpO9k0o0mA var3 = new _$$Lambda$MessagesStorage$lXEFOLx65JNFN3MBWdpO9k0o0mA(this, var1, var2);
         AndroidUtilities.runOnUIThread(var3);
      } catch (Throwable var4) {
         FileLog.e(var4);
      }

   }

   // $FF: synthetic method
   public void lambda$markMentionMessageAsRead$58$MessagesStorage(int var1, int var2, long var3) {
      long var5 = (long)var1;
      long var7 = var5;
      if (var2 != 0) {
         var7 = var5 | (long)var2 << 32;
      }

      Exception var10000;
      label37: {
         SQLiteCursor var13;
         boolean var10001;
         label30: {
            try {
               this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE mid = %d", var7)).stepThis().dispose();
               SQLiteDatabase var9 = this.database;
               StringBuilder var10 = new StringBuilder();
               var10.append("SELECT unread_count_i FROM dialogs WHERE did = ");
               var10.append(var3);
               var13 = var9.queryFinalized(var10.toString());
               if (var13.next()) {
                  var1 = Math.max(0, var13.intValue(0) - 1);
                  break label30;
               }
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label37;
            }

            var1 = 0;
         }

         try {
            var13.dispose();
            this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", var1, var3)).stepThis().dispose();
            LongSparseArray var15 = new LongSparseArray(1);
            var15.put(var3, var1);
            MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead((LongSparseArray)null, var15);
            return;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public void lambda$markMessageAsMention$59$MessagesStorage(long var1) {
      try {
         this.database.executeFast(String.format(Locale.US, "UPDATE messages SET mention = 1, read_state = read_state & ~2 WHERE mid = %d", var1)).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$markMessageAsSendError$125$MessagesStorage(TLRPC.Message param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$markMessagesAsDeleted$134$MessagesStorage(ArrayList var1, int var2) {
      this.markMessagesAsDeletedInternal(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$markMessagesAsDeleted$135$MessagesStorage(int var1, int var2) {
      this.markMessagesAsDeletedInternal(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$markMessagesAsDeletedByRandoms$132$MessagesStorage(ArrayList param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$markMessagesAsRead$130$MessagesStorage(SparseLongArray var1, SparseLongArray var2, SparseIntArray var3) {
      this.markMessagesAsReadInternal(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$markMessagesContentAsRead$129$MessagesStorage(ArrayList var1, int var2) {
      Exception var10000;
      label64: {
         String var12;
         boolean var10001;
         try {
            var12 = TextUtils.join(",", var1);
            this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE mid IN (%s)", var12)).stepThis().dispose();
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label64;
         }

         if (var2 == 0) {
            return;
         }

         SQLiteCursor var3;
         try {
            var3 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid, ttl FROM messages WHERE mid IN (%s) AND ttl > 0", var12));
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label64;
         }

         ArrayList var4 = null;

         while(true) {
            try {
               if (!var3.next()) {
                  break;
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label64;
            }

            var1 = var4;
            if (var4 == null) {
               try {
                  var1 = new ArrayList();
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label64;
               }
            }

            try {
               var1.add(var3.intValue(0));
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label64;
            }

            var4 = var1;
         }

         if (var4 != null) {
            try {
               this.emptyMessagesMedia(var4);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label64;
            }
         }

         try {
            var3.dispose();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   // $FF: synthetic method
   public void lambda$new$0$MessagesStorage() {
      this.openDatabase(1);
   }

   // $FF: synthetic method
   public void lambda$null$10$MessagesStorage(TLRPC.Dialog var1, TLRPC.InputPeer var2, long var3) {
      MessagesController.getInstance(this.currentAccount).checkLastDialogMessage(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$11$MessagesStorage(long var1, boolean var3, TLRPC.InputPeer var4, long var5) {
      MessagesController.getInstance(this.currentAccount).pinDialog(var1, var3, var4, var5);
   }

   // $FF: synthetic method
   public void lambda$null$116$MessagesStorage(int var1, ArrayList var2) {
      DownloadController.getInstance(this.currentAccount).processDownloadObjects(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$118$MessagesStorage(ArrayList var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didReceivedWebpages, var1);
   }

   // $FF: synthetic method
   public void lambda$null$12$MessagesStorage(int var1, int var2, long var3, TLRPC.InputChannel var5) {
      MessagesController.getInstance(this.currentAccount).getChannelDifference(var1, var2, var3, var5);
   }

   // $FF: synthetic method
   public void lambda$null$120$MessagesStorage(long var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$13$MessagesStorage(int var1, long var2, TLObject var4) {
      MessagesController.getInstance(this.currentAccount).deleteMessages((ArrayList)null, (ArrayList)null, (TLRPC.EncryptedChat)null, var1, true, var2, var4);
   }

   // $FF: synthetic method
   public void lambda$null$131$MessagesStorage(ArrayList var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, var1, 0);
   }

   // $FF: synthetic method
   public void lambda$null$14$MessagesStorage(long var1, TLRPC.InputPeer var3, long var4) {
      MessagesController.getInstance(this.currentAccount).markDialogAsUnread(var1, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$null$15$MessagesStorage(int var1, int var2, TLRPC.InputChannel var3, int var4, long var5) {
      MessagesController.getInstance(this.currentAccount).markMessageAsRead(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void lambda$null$16$MessagesStorage(long var1, long var3, boolean var5, boolean var6, int var7, float var8, boolean var9, long var10) {
      MessagesController.getInstance(this.currentAccount).saveWallpaperToServer((File)null, var1, var3, var5, var6, var7, var8, var9, var10);
   }

   // $FF: synthetic method
   public void lambda$null$17$MessagesStorage(long var1, boolean var3, int var4, int var5, boolean var6, TLRPC.InputPeer var7, long var8) {
      MessagesController.getInstance(this.currentAccount).deleteDialog(var1, var3, var4, var5, var6, var7, var8);
   }

   // $FF: synthetic method
   public void lambda$null$18$MessagesStorage(TLRPC.InputPeer var1, long var2) {
      MessagesController.getInstance(this.currentAccount).loadUnknownDialog(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$19$MessagesStorage(int var1, ArrayList var2, long var3) {
      MessagesController.getInstance(this.currentAccount).reorderPinnedDialogs(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$2$MessagesStorage() {
      MessagesController.getInstance(this.currentAccount).getDifference();
   }

   // $FF: synthetic method
   public void lambda$null$20$MessagesStorage(int var1, ArrayList var2, long var3) {
      MessagesController.getInstance(this.currentAccount).addDialogToFolder((ArrayList)null, var1, -1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$26$MessagesStorage(ArrayList var1, ArrayList var2, ArrayList var3, LongSparseArray var4) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(this.currentAccount).putChats(var2, true);
      MessagesController.getInstance(this.currentAccount).putEncryptedChats(var3, true);

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         long var6 = var4.keyAt(var5);
         MessagesStorage.ReadDialog var10 = (MessagesStorage.ReadDialog)var4.valueAt(var5);
         MessagesController var9 = MessagesController.getInstance(this.currentAccount);
         int var8 = var10.lastMid;
         var9.markDialogAsRead(var6, var8, var8, var10.date, false, var10.unreadCount, true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$28$MessagesStorage(LongSparseArray var1, ArrayList var2, ArrayList var3, ArrayList var4, ArrayList var5, ArrayList var6) {
      NotificationsController.getInstance(this.currentAccount).processLoadedUnreadMessages(var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   public void lambda$null$33$MessagesStorage(int var1, ArrayList var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentImagesDidLoad, var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$41$MessagesStorage(ArrayList var1, int var2) {
      MessagesController.getInstance(this.currentAccount).markChannelDialogMessageAsDeleted(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$42$MessagesStorage(ArrayList var1, int var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$44$MessagesStorage() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch);
   }

   // $FF: synthetic method
   public void lambda$null$47$MessagesStorage(TLRPC.photos_Photos var1, int var2, int var3, long var4, int var6) {
      MessagesController.getInstance(this.currentAccount).processLoadedUserPhotos(var1, var2, var3, var4, true, var6);
   }

   // $FF: synthetic method
   public void lambda$null$54$MessagesStorage(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, var1.get(var2));
      }

   }

   // $FF: synthetic method
   public void lambda$null$61$MessagesStorage(boolean var1, ArrayList var2) {
      if (!var1) {
         this.markMessagesContentAsRead(var2, 0);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, var2);
   }

   // $FF: synthetic method
   public void lambda$null$63$MessagesStorage(ArrayList var1) {
      this.markMessagesContentAsRead(var1, 0);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, var1);
   }

   // $FF: synthetic method
   public void lambda$null$66$MessagesStorage(TLRPC.ChatFull var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var1, 0, false, null);
   }

   // $FF: synthetic method
   public void lambda$null$76$MessagesStorage(int var1, TLRPC.UserFull var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoad, var1, var2, null);
   }

   // $FF: synthetic method
   public void lambda$null$79$MessagesStorage(TLRPC.ChatFull var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var1, 0, false, null);
   }

   // $FF: synthetic method
   public void lambda$null$8$MessagesStorage(TLRPC.Chat var1, long var2) {
      MessagesController.getInstance(this.currentAccount).loadUnknownChannel(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$81$MessagesStorage(TLRPC.ChatFull var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var1, 0, false, null);
   }

   // $FF: synthetic method
   public void lambda$null$9$MessagesStorage(int var1, int var2, long var3) {
      MessagesController.getInstance(this.currentAccount).getChannelDifference(var1, var2, var3, (TLRPC.InputChannel)null);
   }

   // $FF: synthetic method
   public void lambda$onDeleteQueryComplete$46$MessagesStorage(long var1) {
      try {
         SQLiteDatabase var3 = this.database;
         StringBuilder var4 = new StringBuilder();
         var4.append("DELETE FROM media_counts_v2 WHERE uid = ");
         var4.append(var1);
         var3.executeFast(var4.toString()).stepThis().dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   // $FF: synthetic method
   public void lambda$overwriteChannel$121$MessagesStorage(int var1, int var2, TLRPC.TL_updates_channelDifferenceTooLong var3) {
      long var4 = (long)(-var1);

      Exception var10000;
      label71: {
         SQLiteDatabase var6;
         StringBuilder var7;
         int var8;
         boolean var9;
         boolean var10001;
         SQLiteCursor var22;
         label65: {
            label64: {
               label72: {
                  try {
                     var6 = this.database;
                     var7 = new StringBuilder();
                     var7.append("SELECT pinned FROM dialogs WHERE did = ");
                     var7.append(var4);
                     var22 = var6.queryFinalized(var7.toString());
                     if (var22.next()) {
                        break label72;
                     }
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label71;
                  }

                  if (var2 != 0) {
                     var8 = 0;
                     var9 = true;
                     break label65;
                  }

                  var8 = 0;
                  break label64;
               }

               try {
                  var8 = var22.intValue(0);
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label71;
               }
            }

            var9 = false;
         }

         TLRPC.Dialog var17;
         TLRPC.TL_messages_dialogs var24;
         try {
            var22.dispose();
            var6 = this.database;
            var7 = new StringBuilder();
            var7.append("DELETE FROM messages WHERE uid = ");
            var7.append(var4);
            var6.executeFast(var7.toString()).stepThis().dispose();
            var6 = this.database;
            var7 = new StringBuilder();
            var7.append("DELETE FROM bot_keyboard WHERE uid = ");
            var7.append(var4);
            var6.executeFast(var7.toString()).stepThis().dispose();
            SQLiteDatabase var23 = this.database;
            StringBuilder var21 = new StringBuilder();
            var21.append("UPDATE media_counts_v2 SET old = 1 WHERE uid = ");
            var21.append(var4);
            var23.executeFast(var21.toString()).stepThis().dispose();
            var6 = this.database;
            var7 = new StringBuilder();
            var7.append("DELETE FROM media_v2 WHERE uid = ");
            var7.append(var4);
            var6.executeFast(var7.toString()).stepThis().dispose();
            var23 = this.database;
            var21 = new StringBuilder();
            var21.append("DELETE FROM messages_holes WHERE uid = ");
            var21.append(var4);
            var23.executeFast(var21.toString()).stepThis().dispose();
            var6 = this.database;
            var7 = new StringBuilder();
            var7.append("DELETE FROM media_holes_v2 WHERE uid = ");
            var7.append(var4);
            var6.executeFast(var7.toString()).stepThis().dispose();
            DataQuery.getInstance(this.currentAccount).clearBotKeyboard(var4, (ArrayList)null);
            var24 = new TLRPC.TL_messages_dialogs();
            var24.chats.addAll(var3.chats);
            var24.users.addAll(var3.users);
            var24.messages.addAll(var3.messages);
            var17 = var3.dialog;
            var17.id = var4;
            var17.flags = 1;
            var17.notify_settings = null;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label71;
         }

         boolean var10;
         if (var8 != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         try {
            var17.pinned = var10;
            var17.pinnedNum = var8;
            var24.dialogs.add(var17);
            this.putDialogsInternal(var24, 0);
            ArrayList var18 = new ArrayList();
            this.updateDialogsWithDeletedMessages(var18, (ArrayList)null, false, var1);
            _$$Lambda$MessagesStorage$TPsnCynXq6cUdHl_MSOW_ayG0w0 var19 = new _$$Lambda$MessagesStorage$TPsnCynXq6cUdHl_MSOW_ayG0w0(this, var4);
            AndroidUtilities.runOnUIThread(var19);
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label71;
         }

         if (!var9) {
            return;
         }

         if (var2 == 1) {
            try {
               MessagesController.getInstance(this.currentAccount).checkChannelInviter(var1);
               return;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         } else {
            try {
               MessagesController.getInstance(this.currentAccount).generateJoinMessage(var1, false);
               return;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
            }
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
   }

   // $FF: synthetic method
   public void lambda$processPendingRead$85$MessagesStorage(long var1, long var3, boolean var5, long var6) {
      Exception var10000;
      label134: {
         SQLiteDatabase var8;
         boolean var10001;
         String var35;
         try {
            var8 = this.database;
            StringBuilder var9 = new StringBuilder();
            var9.append("SELECT unread_count, inbox_max, last_mid FROM dialogs WHERE did = ");
            var9.append(var1);
            var35 = var9.toString();
         } catch (Exception var32) {
            var10000 = var32;
            var10001 = false;
            break label134;
         }

         byte var10 = 0;
         byte var11 = 0;

         boolean var12;
         SQLiteCursor var33;
         try {
            var33 = var8.queryFinalized(var35);
            var12 = var33.next();
         } catch (Exception var31) {
            var10000 = var31;
            var10001 = false;
            break label134;
         }

         long var13 = 0L;
         int var15;
         long var16;
         if (var12) {
            try {
               var15 = var33.intValue(0);
               var13 = (long)var33.intValue(1);
               var16 = var33.longValue(2);
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label134;
            }
         } else {
            var16 = 0L;
            var15 = 0;
         }

         try {
            var33.dispose();
            this.database.beginTransaction();
         } catch (Exception var29) {
            var10000 = var29;
            var10001 = false;
            break label134;
         }

         int var18 = (int)var1;
         SQLitePreparedStatement var34;
         int var37;
         if (var18 != 0) {
            var3 = (long)((int)var3);

            try {
               var6 = Math.max(var13, var3);
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label134;
            }

            var3 = var6;
            if (var5) {
               var3 = var6 | (long)(-var18) << 32;
            }

            try {
               var34 = this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND mid <= ? AND read_state IN(0,2) AND out = 0");
               var34.requery();
               var34.bindLong(1, var1);
               var34.bindLong(2, var3);
               var34.step();
               var34.dispose();
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label134;
            }

            if (var3 >= var16) {
               var15 = var11;
            } else {
               label92: {
                  try {
                     var33 = this.database.queryFinalized("SELECT changes()");
                     if (var33.next()) {
                        var37 = var33.intValue(0);
                        break label92;
                     }
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label134;
                  }

                  var37 = 0;
               }

               try {
                  var33.dispose();
                  var15 = Math.max(0, var15 - var37);
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label134;
               }
            }

            try {
               var34 = this.database.executeFast("DELETE FROM unread_push_messages WHERE uid = ? AND mid <= ?");
               var34.requery();
               var34.bindLong(1, var1);
               var34.bindLong(2, var3);
               var34.step();
               var34.dispose();
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label134;
            }
         } else {
            var3 = (long)((int)var6);

            try {
               var34 = this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND mid >= ? AND read_state IN(0,2) AND out = 0");
               var34.requery();
               var34.bindLong(1, var1);
               var34.bindLong(2, var3);
               var34.step();
               var34.dispose();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label134;
            }

            if (var3 <= var16) {
               var15 = var10;
            } else {
               label109: {
                  try {
                     var33 = this.database.queryFinalized("SELECT changes()");
                     if (var33.next()) {
                        var37 = var33.intValue(0);
                        break label109;
                     }
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label134;
                  }

                  var37 = 0;
               }

               try {
                  var33.dispose();
                  var15 = Math.max(0, var15 - var37);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label134;
               }
            }

            try {
               var34 = this.database.executeFast("DELETE FROM unread_push_messages WHERE uid = ? AND mid >= ?");
               var34.requery();
               var34.bindLong(1, var1);
               var34.bindLong(2, var3);
               var34.step();
               var34.dispose();
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label134;
            }
         }

         try {
            var34 = this.database.executeFast("UPDATE dialogs SET unread_count = ?, inbox_max = ? WHERE did = ?");
            var34.requery();
            var34.bindInteger(1, var15);
            var34.bindInteger(2, (int)var3);
            var34.bindLong(3, var1);
            var34.step();
            var34.dispose();
            this.database.commitTransaction();
            return;
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
         }
      }

      Exception var36 = var10000;
      FileLog.e((Throwable)var36);
   }

   // $FF: synthetic method
   public void lambda$putBlockedUsers$40$MessagesStorage(boolean var1, SparseIntArray var2) {
      Exception var10000;
      label50: {
         boolean var10001;
         if (var1) {
            try {
               this.database.executeFast("DELETE FROM blocked_users WHERE 1").stepThis().dispose();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label50;
            }
         }

         SQLitePreparedStatement var3;
         try {
            this.database.beginTransaction();
            var3 = this.database.executeFast("REPLACE INTO blocked_users VALUES(?)");
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label50;
         }

         int var4 = 0;

         int var5;
         try {
            var5 = var2.size();
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label50;
         }

         for(; var4 < var5; ++var4) {
            try {
               var3.requery();
               var3.bindInteger(1, var2.keyAt(var4));
               var3.step();
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label50;
            }
         }

         try {
            var3.dispose();
            this.database.commitTransaction();
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      FileLog.e((Throwable)var11);
   }

   // $FF: synthetic method
   public void lambda$putCachedPhoneBook$89$MessagesStorage(HashMap var1, boolean var2) {
      Exception var10000;
      label78: {
         boolean var10001;
         try {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var3 = new StringBuilder();
               var3.append(this.currentAccount);
               var3.append(" save contacts to db ");
               var3.append(var1.size());
               FileLog.d(var3.toString());
            }
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label78;
         }

         SQLitePreparedStatement var4;
         Iterator var5;
         SQLitePreparedStatement var18;
         try {
            this.database.executeFast("DELETE FROM user_contacts_v7 WHERE 1").stepThis().dispose();
            this.database.executeFast("DELETE FROM user_phones_v7 WHERE 1").stepThis().dispose();
            this.database.beginTransaction();
            var4 = this.database.executeFast("REPLACE INTO user_contacts_v7 VALUES(?, ?, ?, ?, ?)");
            var18 = this.database.executeFast("REPLACE INTO user_phones_v7 VALUES(?, ?, ?, ?)");
            var5 = var1.entrySet().iterator();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label78;
         }

         label71:
         while(true) {
            while(true) {
               boolean var6;
               try {
                  var6 = var5.hasNext();
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label71;
               }

               int var7 = 0;
               if (!var6) {
                  try {
                     var4.dispose();
                     var18.dispose();
                     this.database.commitTransaction();
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label71;
                  }

                  if (!var2) {
                     return;
                  }

                  try {
                     this.database.executeFast("DROP TABLE IF EXISTS user_contacts_v6;").stepThis().dispose();
                     this.database.executeFast("DROP TABLE IF EXISTS user_phones_v6;").stepThis().dispose();
                     this.getCachedPhoneBook(false);
                     return;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label71;
                  }
               }

               ContactsController.Contact var16;
               try {
                  var16 = (ContactsController.Contact)((Entry)var5.next()).getValue();
                  if (var16.phones.isEmpty() || var16.shortPhones.isEmpty()) {
                     continue;
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label71;
               }

               try {
                  var4.requery();
                  var4.bindString(1, var16.key);
                  var4.bindInteger(2, var16.contact_id);
                  var4.bindString(3, var16.first_name);
                  var4.bindString(4, var16.last_name);
                  var4.bindInteger(5, var16.imported);
                  var4.step();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label71;
               }

               while(true) {
                  try {
                     if (var7 >= var16.phones.size()) {
                        break;
                     }

                     var18.requery();
                     var18.bindString(1, var16.key);
                     var18.bindString(2, (String)var16.phones.get(var7));
                     var18.bindString(3, (String)var16.shortPhones.get(var7));
                     var18.bindInteger(4, (Integer)var16.phoneDeleted.get(var7));
                     var18.step();
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label71;
                  }

                  ++var7;
               }
            }
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   public void lambda$putChannelAdmins$69$MessagesStorage(int param1, ArrayList param2) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$putChannelViews$122$MessagesStorage(SparseArray var1, boolean var2) {
      Exception var10000;
      label60: {
         SQLitePreparedStatement var3;
         boolean var10001;
         try {
            this.database.beginTransaction();
            var3 = this.database.executeFast("UPDATE messages SET media = max((SELECT media FROM messages WHERE mid = ?), ?) WHERE mid = ?");
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label60;
         }

         int var4 = 0;

         while(true) {
            int var5;
            SparseIntArray var6;
            try {
               if (var4 >= var1.size()) {
                  break;
               }

               var5 = var1.keyAt(var4);
               var6 = (SparseIntArray)var1.get(var5);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label60;
            }

            int var7 = 0;

            while(true) {
               int var8;
               long var9;
               try {
                  if (var7 >= var6.size()) {
                     break;
                  }

                  var8 = var6.get(var6.keyAt(var7));
                  var9 = (long)var6.keyAt(var7);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label60;
               }

               long var11 = var9;
               if (var2) {
                  var11 = var9 | (long)(-var5) << 32;
               }

               try {
                  var3.requery();
                  var3.bindLong(1, var11);
                  var3.bindInteger(2, var8);
                  var3.bindLong(3, var11);
                  var3.step();
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label60;
               }

               ++var7;
            }

            ++var4;
         }

         try {
            var3.dispose();
            this.database.commitTransaction();
            return;
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
         }
      }

      Exception var18 = var10000;
      FileLog.e((Throwable)var18);
   }

   // $FF: synthetic method
   public void lambda$putContacts$86$MessagesStorage(boolean var1, ArrayList var2) {
      Exception var10000;
      label64: {
         boolean var10001;
         if (var1) {
            try {
               this.database.executeFast("DELETE FROM contacts WHERE 1").stepThis().dispose();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label64;
            }
         }

         SQLitePreparedStatement var3;
         try {
            this.database.beginTransaction();
            var3 = this.database.executeFast("REPLACE INTO contacts VALUES(?, ?)");
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label64;
         }

         int var4 = 0;

         while(true) {
            TLRPC.TL_contact var5;
            int var6;
            try {
               if (var4 >= var2.size()) {
                  break;
               }

               var5 = (TLRPC.TL_contact)var2.get(var4);
               var3.requery();
               var6 = var5.user_id;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label64;
            }

            byte var7 = 1;

            label48: {
               try {
                  var3.bindInteger(1, var6);
                  if (var5.mutual) {
                     break label48;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label64;
               }

               var7 = 0;
            }

            try {
               var3.bindInteger(2, var7);
               var3.step();
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label64;
            }

            ++var4;
         }

         try {
            var3.dispose();
            this.database.commitTransaction();
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public void lambda$putDialogPhotos$53$MessagesStorage(int var1, TLRPC.photos_Photos var2) {
      Exception var10000;
      label48: {
         SQLitePreparedStatement var5;
         boolean var10001;
         try {
            SQLiteDatabase var3 = this.database;
            StringBuilder var4 = new StringBuilder();
            var4.append("DELETE FROM user_photos WHERE uid = ");
            var4.append(var1);
            var3.executeFast(var4.toString()).stepThis().dispose();
            var5 = this.database.executeFast("REPLACE INTO user_photos VALUES(?, ?, ?)");
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label48;
         }

         int var6 = 0;

         int var7;
         try {
            var7 = var2.photos.size();
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label48;
         }

         while(true) {
            if (var6 >= var7) {
               try {
                  var5.dispose();
                  return;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }

            label37: {
               TLRPC.Photo var15;
               try {
                  var15 = (TLRPC.Photo)var2.photos.get(var6);
                  if (var15 instanceof TLRPC.TL_photoEmpty) {
                     break label37;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               try {
                  var5.requery();
                  NativeByteBuffer var14 = new NativeByteBuffer(var15.getObjectSize());
                  var15.serializeToStream(var14);
                  var5.bindInteger(1, var1);
                  var5.bindLong(2, var15.id);
                  var5.bindByteBuffer(3, (NativeByteBuffer)var14);
                  var5.step();
                  var14.reuse();
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }

            ++var6;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   // $FF: synthetic method
   public void lambda$putDialogs$148$MessagesStorage(TLRPC.messages_Dialogs var1, int var2) {
      this.putDialogsInternal(var1, var2);

      try {
         this.loadUnreadMessages();
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   // $FF: synthetic method
   public void lambda$putEncryptedChat$111$MessagesStorage(TLRPC.EncryptedChat var1, TLRPC.User var2, TLRPC.Dialog var3) {
      Exception var10000;
      label144: {
         boolean var10001;
         label139: {
            try {
               if (var1.key_hash != null && var1.key_hash.length >= 16) {
                  break label139;
               }
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label144;
            }

            try {
               if (var1.auth_key != null) {
                  var1.key_hash = AndroidUtilities.calcAuthKeyHash(var1.auth_key);
               }
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label144;
            }
         }

         SQLitePreparedStatement var4;
         NativeByteBuffer var5;
         NativeByteBuffer var6;
         int var7;
         label128: {
            try {
               var4 = this.database.executeFast("REPLACE INTO enc_chats VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
               var5 = new NativeByteBuffer(var1.getObjectSize());
               var6 = new NativeByteBuffer;
               if (var1.a_or_b != null) {
                  var7 = var1.a_or_b.length;
                  break label128;
               }
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label144;
            }

            var7 = 1;
         }

         NativeByteBuffer var8;
         label118: {
            try {
               var6.<init>(var7);
               var8 = new NativeByteBuffer;
               if (var1.auth_key != null) {
                  var7 = var1.auth_key.length;
                  break label118;
               }
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label144;
            }

            var7 = 1;
         }

         NativeByteBuffer var9;
         label145: {
            try {
               var8.<init>(var7);
               var9 = new NativeByteBuffer;
               if (var1.future_auth_key != null) {
                  var7 = var1.future_auth_key.length;
                  break label145;
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label144;
            }

            var7 = 1;
         }

         NativeByteBuffer var10;
         label99: {
            try {
               var9.<init>(var7);
               var10 = new NativeByteBuffer;
               if (var1.key_hash != null) {
                  var7 = var1.key_hash.length;
                  break label99;
               }
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label144;
            }

            var7 = 1;
         }

         try {
            var10.<init>(var7);
            var1.serializeToStream(var5);
            var4.bindInteger(1, var1.id);
            var4.bindInteger(2, var2.id);
            var4.bindString(3, this.formatUserSearchName(var2));
            var4.bindByteBuffer(4, (NativeByteBuffer)var5);
            if (var1.a_or_b != null) {
               var6.writeBytes(var1.a_or_b);
            }
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label144;
         }

         try {
            if (var1.auth_key != null) {
               var8.writeBytes(var1.auth_key);
            }
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label144;
         }

         try {
            if (var1.future_auth_key != null) {
               var9.writeBytes(var1.future_auth_key);
            }
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label144;
         }

         try {
            if (var1.key_hash != null) {
               var10.writeBytes(var1.key_hash);
            }
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label144;
         }

         try {
            var4.bindByteBuffer(5, (NativeByteBuffer)var6);
            var4.bindByteBuffer(6, (NativeByteBuffer)var8);
            var4.bindInteger(7, var1.ttl);
            var4.bindInteger(8, var1.layer);
            var4.bindInteger(9, var1.seq_in);
            var4.bindInteger(10, var1.seq_out);
            short var25 = var1.key_use_count_in;
            var4.bindInteger(11, var1.key_use_count_out | var25 << 16);
            var4.bindLong(12, var1.exchange_id);
            var4.bindInteger(13, var1.key_create_date);
            var4.bindLong(14, var1.future_key_fingerprint);
            var4.bindByteBuffer(15, (NativeByteBuffer)var9);
            var4.bindByteBuffer(16, (NativeByteBuffer)var10);
            var4.bindInteger(17, var1.in_seq_no);
            var4.bindInteger(18, var1.admin_id);
            var4.bindInteger(19, var1.mtproto_seq);
            var4.step();
            var4.dispose();
            var5.reuse();
            var6.reuse();
            var8.reuse();
            var9.reuse();
            var10.reuse();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label144;
         }

         if (var3 == null) {
            return;
         }

         try {
            SQLitePreparedStatement var24 = this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            var24.bindLong(1, var3.id);
            var24.bindInteger(2, var3.last_message_date);
            var24.bindInteger(3, var3.unread_count);
            var24.bindInteger(4, var3.top_message);
            var24.bindInteger(5, var3.read_inbox_max_id);
            var24.bindInteger(6, var3.read_outbox_max_id);
            var24.bindInteger(7, 0);
            var24.bindInteger(8, var3.unread_mentions_count);
            var24.bindInteger(9, var3.pts);
            var24.bindInteger(10, 0);
            var24.bindInteger(11, var3.pinnedNum);
            var24.bindInteger(12, var3.flags);
            var24.bindInteger(13, var3.folder_id);
            var24.bindNull(14);
            var24.step();
            var24.dispose();
            return;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
         }
      }

      Exception var23 = var10000;
      FileLog.e((Throwable)var23);
   }

   // $FF: synthetic method
   public void lambda$putMessages$124$MessagesStorage(ArrayList var1, boolean var2, boolean var3, int var4, boolean var5) {
      this.putMessagesInternal(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void lambda$putMessages$138$MessagesStorage(TLRPC.messages_Messages var1, int var2, long var3, int var5, boolean var6) {
      long var8 = var3;

      Exception var10000;
      label479: {
         boolean var10001;
         label480: {
            try {
               if (!var1.messages.isEmpty()) {
                  break label480;
               }
            } catch (Exception var81) {
               var10000 = var81;
               var10001 = false;
               break label479;
            }

            if (var2 == 0) {
               try {
                  this.doneHolesInTable("messages_holes", var8, var5);
                  this.doneHolesInMedia(var8, var5, -1);
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label479;
               }
            }

            return;
         }

         try {
            this.database.beginTransaction();
         } catch (Exception var80) {
            var10000 = var80;
            var10001 = false;
            break label479;
         }

         int var7;
         if (var2 == 0) {
            try {
               var7 = ((TLRPC.Message)var1.messages.get(var1.messages.size() - 1)).id;
               this.closeHolesInTable("messages_holes", var3, var7, var5);
               this.closeHolesInMedia(var3, var7, var5, -1);
            } catch (Exception var79) {
               var10000 = var79;
               var10001 = false;
               break label479;
            }
         } else if (var2 == 1) {
            try {
               var7 = ((TLRPC.Message)var1.messages.get(0)).id;
               this.closeHolesInTable("messages_holes", var3, var5, var7);
               this.closeHolesInMedia(var3, var5, var7, -1);
            } catch (Exception var78) {
               var10000 = var78;
               var10001 = false;
               break label479;
            }
         } else if (var2 == 3 || var2 == 2 || var2 == 4) {
            if (var5 == 0 && var2 != 4) {
               var5 = Integer.MAX_VALUE;
            } else {
               try {
                  var5 = ((TLRPC.Message)var1.messages.get(0)).id;
               } catch (Exception var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label479;
               }
            }

            try {
               var7 = ((TLRPC.Message)var1.messages.get(var1.messages.size() - 1)).id;
               this.closeHolesInTable("messages_holes", var3, var7, var5);
               this.closeHolesInMedia(var3, var7, var5, -1);
            } catch (Exception var76) {
               var10000 = var76;
               var10001 = false;
               break label479;
            }
         }

         int var10;
         SQLitePreparedStatement var11;
         SQLitePreparedStatement var12;
         try {
            var10 = var1.messages.size();
            var11 = this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
            var12 = this.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
         } catch (Exception var75) {
            var10000 = var75;
            var10001 = false;
            break label479;
         }

         int var13 = 0;
         int var14 = 0;
         SQLitePreparedStatement var15 = null;
         SQLitePreparedStatement var16 = null;
         TLRPC.Message var17 = null;
         var5 = Integer.MAX_VALUE;

         while(true) {
            long var18 = var3;
            if (var14 >= var10) {
               try {
                  var11.dispose();
                  var12.dispose();
               } catch (Exception var37) {
                  var10000 = var37;
                  var10001 = false;
                  break;
               }

               if (var15 != null) {
                  try {
                     var15.dispose();
                  } catch (Exception var36) {
                     var10000 = var36;
                     var10001 = false;
                     break;
                  }
               }

               if (var16 != null) {
                  try {
                     var16.dispose();
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break;
                  }
               }

               if (var17 != null) {
                  try {
                     DataQuery.getInstance(this.currentAccount).putBotKeyboard(var3, var17);
                  } catch (Exception var34) {
                     var10000 = var34;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  this.putUsersInternal(var1.users);
                  this.putChatsInternal(var1.chats);
               } catch (Exception var33) {
                  var10000 = var33;
                  var10001 = false;
                  break;
               }

               if (var5 != Integer.MAX_VALUE) {
                  try {
                     this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", var5, var3)).stepThis().dispose();
                     LongSparseArray var82 = new LongSparseArray(1);
                     var82.put(var3, var5);
                     MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead((LongSparseArray)null, var82);
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  this.database.commitTransaction();
               } catch (Exception var31) {
                  var10000 = var31;
                  var10001 = false;
                  break;
               }

               if (!var6) {
                  return;
               }

               try {
                  ArrayList var84 = new ArrayList();
                  this.updateDialogsWithDeletedMessages(var84, (ArrayList)null, false, var13);
                  return;
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break;
               }
            }

            TLRPC.Message var20;
            try {
               var20 = (TLRPC.Message)var1.messages.get(var14);
               var8 = (long)var20.id;
            } catch (Exception var61) {
               var10000 = var61;
               var10001 = false;
               break;
            }

            var7 = var13;
            if (var13 == 0) {
               try {
                  var7 = var20.to_id.channel_id;
               } catch (Exception var60) {
                  var10000 = var60;
                  var10001 = false;
                  break;
               }
            }

            label437: {
               try {
                  if (var20.to_id.channel_id == 0) {
                     break label437;
                  }
               } catch (Exception var74) {
                  var10000 = var74;
                  var10001 = false;
                  break;
               }

               var8 |= (long)var7 << 32;
            }

            SQLitePreparedStatement var88;
            label492: {
               var13 = var10;
               boolean var26;
               int var27;
               if (var2 == -2) {
                  SQLiteDatabase var21;
                  Locale var22;
                  try {
                     var21 = this.database;
                     var22 = Locale.US;
                  } catch (Exception var59) {
                     var10000 = var59;
                     var10001 = false;
                     break;
                  }

                  boolean var24;
                  SQLiteCursor var89;
                  try {
                     var89 = var21.queryFinalized(String.format(var22, "SELECT mid, data, ttl, mention, read_state, send_state FROM messages WHERE mid = %d", var8));
                     var24 = var89.next();
                  } catch (Exception var58) {
                     var10000 = var58;
                     var10001 = false;
                     break;
                  }

                  if (var24) {
                     label493: {
                        NativeByteBuffer var25;
                        try {
                           var25 = var89.byteBufferValue(1);
                        } catch (Exception var57) {
                           var10000 = var57;
                           var10001 = false;
                           break;
                        }

                        if (var25 != null) {
                           TLRPC.Message var92;
                           try {
                              var92 = TLRPC.Message.TLdeserialize(var25, var25.readInt32(false), false);
                              var92.readAttachPath(var25, UserConfig.getInstance(this.currentAccount).clientUserId);
                              var25.reuse();
                              var10 = var89.intValue(5);
                           } catch (Exception var56) {
                              var10000 = var56;
                              var10001 = false;
                              break;
                           }

                           if (var92 != null && var10 != 3) {
                              try {
                                 var20.attachPath = var92.attachPath;
                                 var20.ttl = var89.intValue(2);
                              } catch (Exception var55) {
                                 var10000 = var55;
                                 var10001 = false;
                                 break;
                              }
                           }
                        }

                        label426: {
                           label425: {
                              try {
                                 if (var89.intValue(3) != 0) {
                                    break label425;
                                 }
                              } catch (Exception var73) {
                                 var10000 = var73;
                                 var10001 = false;
                                 break;
                              }

                              var26 = false;
                              break label426;
                           }

                           var26 = true;
                        }

                        try {
                           var27 = var89.intValue(4);
                           if (var26 == var20.mentioned) {
                              break label493;
                           }
                        } catch (Exception var71) {
                           var10000 = var71;
                           var10001 = false;
                           break;
                        }

                        if (var5 == Integer.MAX_VALUE) {
                           SQLiteDatabase var98;
                           try {
                              var98 = this.database;
                           } catch (Exception var54) {
                              var10000 = var54;
                              var10001 = false;
                              break;
                           }

                           SQLiteCursor var94;
                           try {
                              StringBuilder var93 = new StringBuilder();
                              var93.append("SELECT unread_count_i FROM dialogs WHERE did = ");
                              var93.append(var18);
                              var94 = var98.queryFinalized(var93.toString());
                           } catch (Exception var53) {
                              var10000 = var53;
                              var10001 = false;
                              break;
                           }

                           var10 = var5;

                           try {
                              if (var94.next()) {
                                 var10 = var94.intValue(0);
                              }
                           } catch (Exception var72) {
                              var10000 = var72;
                              var10001 = false;
                              break;
                           }

                           try {
                              var94.dispose();
                           } catch (Exception var52) {
                              var10000 = var52;
                              var10001 = false;
                              break;
                           }
                        } else {
                           var10 = var5;
                        }

                        if (var26) {
                           var5 = var10;
                           if (var27 <= 1) {
                              var5 = var10 - 1;
                           }
                        } else {
                           label494: {
                              var5 = var10;

                              try {
                                 if (!var20.media_unread) {
                                    break label494;
                                 }
                              } catch (Exception var70) {
                                 var10000 = var70;
                                 var10001 = false;
                                 break;
                              }

                              var5 = var10 + 1;
                           }
                        }
                     }
                  }

                  try {
                     var89.dispose();
                  } catch (Exception var51) {
                     var10000 = var51;
                     var10001 = false;
                     break;
                  }

                  var10 = var5;
                  if (!var24) {
                     var20 = var17;
                     var88 = var15;
                     break label492;
                  }
               } else {
                  var10 = var5;
               }

               SQLitePreparedStatement var97;
               if (var14 == 0 && var6) {
                  SQLiteCursor var95;
                  try {
                     SQLiteDatabase var23 = this.database;
                     StringBuilder var90 = new StringBuilder();
                     var90.append("SELECT pinned, unread_count_i, flags FROM dialogs WHERE did = ");
                     var90.append(var18);
                     var95 = var23.queryFinalized(var90.toString());
                     var26 = var95.next();
                  } catch (Exception var50) {
                     var10000 = var50;
                     var10001 = false;
                     break;
                  }

                  int var28;
                  if (var26) {
                     try {
                        var5 = var95.intValue(0);
                        var28 = var95.intValue(1);
                        var27 = var95.intValue(2);
                     } catch (Exception var49) {
                        var10000 = var49;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var28 = 0;
                     var5 = 0;
                     var27 = 0;
                  }

                  try {
                     var95.dispose();
                  } catch (Exception var48) {
                     var10000 = var48;
                     var10001 = false;
                     break;
                  }

                  if (var26) {
                     try {
                        var97 = this.database.executeFast("UPDATE dialogs SET date = ?, last_mid = ?, inbox_max = ?, last_mid_i = ?, pts = ?, date_i = ? WHERE did = ?");
                        var97.bindInteger(1, var20.date);
                        var97.bindLong(2, var8);
                        var97.bindInteger(3, var20.id);
                        var97.bindLong(4, var8);
                        var97.bindInteger(5, var1.pts);
                        var97.bindInteger(6, var20.date);
                        var97.bindLong(7, var18);
                     } catch (Exception var47) {
                        var10000 = var47;
                        var10001 = false;
                        break;
                     }
                  } else {
                     try {
                        var97 = this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        var97.bindLong(1, var18);
                        var97.bindInteger(2, var20.date);
                        var97.bindInteger(3, 0);
                        var97.bindLong(4, var8);
                        var97.bindInteger(5, var20.id);
                        var97.bindInteger(6, 0);
                        var97.bindLong(7, var8);
                        var97.bindInteger(8, var28);
                        var97.bindInteger(9, var1.pts);
                        var97.bindInteger(10, var20.date);
                        var97.bindInteger(11, var5);
                        var97.bindInteger(12, var27);
                        var97.bindInteger(13, 0);
                        var97.bindNull(14);
                     } catch (Exception var46) {
                        var10000 = var46;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var97.step();
                     var97.dispose();
                  } catch (Exception var45) {
                     var10000 = var45;
                     var10001 = false;
                     break;
                  }
               }

               NativeByteBuffer var96;
               try {
                  this.fixUnsupportedMedia(var20);
                  var11.requery();
                  var96 = new NativeByteBuffer(var20.getObjectSize());
                  var20.serializeToStream(var96);
               } catch (Exception var44) {
                  var10000 = var44;
                  var10001 = false;
                  break;
               }

               var97 = var11;

               byte var85;
               label393: {
                  label392: {
                     try {
                        var97.bindLong(1, var8);
                        var97.bindLong(2, var18);
                        var97.bindInteger(3, MessageObject.getUnreadFlags(var20));
                        var97.bindInteger(4, var20.send_state);
                        var97.bindInteger(5, var20.date);
                        var97.bindByteBuffer(6, (NativeByteBuffer)var96);
                        if (MessageObject.isOut(var20)) {
                           break label392;
                        }
                     } catch (Exception var69) {
                        var10000 = var69;
                        var10001 = false;
                        break;
                     }

                     var85 = 0;
                     break label393;
                  }

                  var85 = 1;
               }

               label385: {
                  try {
                     var97.bindInteger(7, var85);
                     var97.bindInteger(8, var20.ttl);
                     if ((var20.flags & 1024) != 0) {
                        var97.bindInteger(9, var20.views);
                        break label385;
                     }
                  } catch (Exception var68) {
                     var10000 = var68;
                     var10001 = false;
                     break;
                  }

                  try {
                     var97.bindInteger(9, this.getMessageMediaType(var20));
                  } catch (Exception var43) {
                     var10000 = var43;
                     var10001 = false;
                     break;
                  }
               }

               label377: {
                  label376: {
                     try {
                        var97.bindInteger(10, 0);
                        if (var20.mentioned) {
                           break label376;
                        }
                     } catch (Exception var67) {
                        var10000 = var67;
                        var10001 = false;
                        break;
                     }

                     var85 = 0;
                     break label377;
                  }

                  var85 = 1;
               }

               label488: {
                  try {
                     var97.bindInteger(11, var85);
                     var97.step();
                     if (!DataQuery.canAddMessageToMedia(var20)) {
                        break label488;
                     }

                     var12.requery();
                  } catch (Exception var66) {
                     var10000 = var66;
                     var10001 = false;
                     break;
                  }

                  var97 = var12;

                  try {
                     var97.bindLong(1, var8);
                     var97.bindLong(2, var18);
                     var97.bindInteger(3, var20.date);
                     var97.bindInteger(4, DataQuery.getMediaType(var20));
                     var97.bindByteBuffer(5, (NativeByteBuffer)var96);
                     var97.step();
                  } catch (Exception var42) {
                     var10000 = var42;
                     var10001 = false;
                     break;
                  }
               }

               SQLitePreparedStatement var91 = var12;

               label489: {
                  label490: {
                     try {
                        var96.reuse();
                        if (var20.media instanceof TLRPC.TL_messageMediaPoll) {
                           break label490;
                        }
                     } catch (Exception var65) {
                        var10000 = var65;
                        var10001 = false;
                        break;
                     }

                     var12 = var15;
                     var97 = var16;

                     try {
                        if (!(var20.media instanceof TLRPC.TL_messageMediaWebPage)) {
                           break label489;
                        }
                     } catch (Exception var64) {
                        var10000 = var64;
                        var10001 = false;
                        break;
                     }

                     if (var15 == null) {
                        try {
                           var15 = this.database.executeFast("REPLACE INTO webpage_pending VALUES(?, ?)");
                        } catch (Exception var39) {
                           var10000 = var39;
                           var10001 = false;
                           break;
                        }
                     }

                     try {
                        var15.requery();
                        var15.bindLong(1, var20.media.webpage.id);
                        var15.bindLong(2, var8);
                        var15.step();
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break;
                     }

                     var97 = var16;
                     var12 = var15;
                     break label489;
                  }

                  if (var16 == null) {
                     try {
                        var12 = this.database.executeFast("REPLACE INTO polls VALUES(?, ?)");
                     } catch (Exception var41) {
                        var10000 = var41;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var12 = var16;
                  }

                  try {
                     TLRPC.TL_messageMediaPoll var87 = (TLRPC.TL_messageMediaPoll)var20.media;
                     var12.requery();
                     var12.bindLong(1, var8);
                     var12.bindLong(2, var87.poll.id);
                     var12.step();
                  } catch (Exception var40) {
                     var10000 = var40;
                     var10001 = false;
                     break;
                  }

                  var97 = var12;
                  var12 = var15;
               }

               TLRPC.Message var86;
               label352: {
                  label497: {
                     if (var2 == 0) {
                        label495: {
                           try {
                              if (!this.isValidKeyboardToSave(var20)) {
                                 break label495;
                              }
                           } catch (Exception var63) {
                              var10000 = var63;
                              var10001 = false;
                              break;
                           }

                           var86 = var17;
                           if (var17 == null) {
                              break label497;
                           }

                           try {
                              if (var86.id < var20.id) {
                                 break label497;
                              }
                           } catch (Exception var62) {
                              var10000 = var62;
                              var10001 = false;
                              break;
                           }
                        }
                     }

                     var86 = var17;
                     break label352;
                  }

                  var86 = var20;
               }

               var16 = var97;
               var88 = var12;
               var5 = var10;
               var20 = var86;
               var12 = var91;
            }

            ++var14;
            var10 = var13;
            var13 = var7;
            var15 = var88;
            var17 = var20;
         }
      }

      Exception var83 = var10000;
      FileLog.e((Throwable)var83);
   }

   // $FF: synthetic method
   public void lambda$putMessagesInternal$123$MessagesStorage(int var1) {
      DownloadController.getInstance(this.currentAccount).newDownloadObjectsAvailable(var1);
   }

   // $FF: synthetic method
   public void lambda$putPushMessage$25$MessagesStorage(MessageObject var1) {
      Exception var10000;
      label115: {
         NativeByteBuffer var2;
         long var3;
         boolean var10001;
         try {
            var2 = new NativeByteBuffer(var1.messageOwner.getObjectSize());
            var1.messageOwner.serializeToStream(var2);
            var3 = (long)var1.getId();
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label115;
         }

         long var5 = var3;

         try {
            if (var1.messageOwner.to_id.channel_id != 0) {
               var5 = var3 | (long)var1.messageOwner.to_id.channel_id << 32;
            }
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label115;
         }

         byte var7 = 0;

         label102: {
            try {
               if (var1.localType != 2) {
                  break label102;
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label115;
            }

            var7 = 1;
         }

         int var8 = var7;

         label95: {
            try {
               if (!var1.localChannel) {
                  break label95;
               }
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label115;
            }

            var8 = var7 | 2;
         }

         SQLitePreparedStatement var9;
         label116: {
            try {
               var9 = this.database.executeFast("REPLACE INTO unread_push_messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
               var9.requery();
               var9.bindLong(1, var1.getDialogId());
               var9.bindLong(2, var5);
               var9.bindLong(3, var1.messageOwner.random_id);
               var9.bindInteger(4, var1.messageOwner.date);
               var9.bindByteBuffer(5, (NativeByteBuffer)var2);
               if (var1.messageText == null) {
                  var9.bindNull(6);
                  break label116;
               }
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label115;
            }

            try {
               var9.bindString(6, var1.messageText.toString());
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label115;
            }
         }

         label78: {
            try {
               if (var1.localName == null) {
                  var9.bindNull(7);
                  break label78;
               }
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label115;
            }

            try {
               var9.bindString(7, var1.localName);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label115;
            }
         }

         label67: {
            try {
               if (var1.localUserName == null) {
                  var9.bindNull(8);
                  break label67;
               }
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label115;
            }

            try {
               var9.bindString(8, var1.localUserName);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label115;
            }
         }

         try {
            var9.bindInteger(9, var8);
            var9.step();
            var2.reuse();
            var9.dispose();
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var21 = var10000;
      FileLog.e((Throwable)var21);
   }

   // $FF: synthetic method
   public void lambda$putSentFile$103$MessagesStorage(String param1, TLObject param2, int param3, String param4) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$putUsersAndChats$113$MessagesStorage(ArrayList var1, ArrayList var2, boolean var3) {
      this.putUsersAndChatsInternal(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$putWallpapers$30$MessagesStorage(int var1, ArrayList var2) {
      Exception var10000;
      label91: {
         boolean var10001;
         if (var1 == 1) {
            try {
               this.database.executeFast("DELETE FROM wallpapers2 WHERE 1").stepThis().dispose();
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label91;
            }
         }

         try {
            this.database.beginTransaction();
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label91;
         }

         SQLitePreparedStatement var3;
         if (var1 != 0) {
            try {
               var3 = this.database.executeFast("REPLACE INTO wallpapers2 VALUES(?, ?, ?)");
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label91;
            }
         } else {
            try {
               var3 = this.database.executeFast("UPDATE wallpapers2 SET data = ? WHERE uid = ?");
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label91;
            }
         }

         int var4 = 0;

         int var5;
         try {
            var5 = var2.size();
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label91;
         }

         for(; var4 < var5; ++var4) {
            TLRPC.TL_wallPaper var6;
            NativeByteBuffer var7;
            try {
               var6 = (TLRPC.TL_wallPaper)var2.get(var4);
               var3.requery();
               var7 = new NativeByteBuffer(var6.getObjectSize());
               var6.serializeToStream(var7);
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label91;
            }

            if (var1 != 0) {
               try {
                  var3.bindLong(1, var6.id);
                  var3.bindByteBuffer(2, (NativeByteBuffer)var7);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label91;
               }

               int var8;
               if (var1 == 2) {
                  var8 = -1;
               } else {
                  var8 = var4;
               }

               try {
                  var3.bindInteger(3, var8);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label91;
               }
            } else {
               try {
                  var3.bindByteBuffer(1, (NativeByteBuffer)var7);
                  var3.bindLong(2, var6.id);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label91;
               }
            }

            try {
               var3.step();
               var7.reuse();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label91;
            }
         }

         try {
            var3.dispose();
            this.database.commitTransaction();
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
   }

   // $FF: synthetic method
   public void lambda$putWebPages$119$MessagesStorage(LongSparseArray var1) {
      Exception var10000;
      label140: {
         ArrayList var2;
         boolean var10001;
         try {
            var2 = new ArrayList();
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label140;
         }

         byte var3 = 0;
         int var4 = 0;

         while(true) {
            ArrayList var30;
            SQLiteCursor var33;
            try {
               if (var4 >= var1.size()) {
                  break;
               }

               SQLiteDatabase var5 = this.database;
               StringBuilder var6 = new StringBuilder();
               var6.append("SELECT mid FROM webpage_pending WHERE id = ");
               var6.append(var1.keyAt(var4));
               var33 = var5.queryFinalized(var6.toString());
               var30 = new ArrayList();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label140;
            }

            while(true) {
               try {
                  if (!var33.next()) {
                     break;
                  }

                  var30.add(var33.longValue(0));
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label140;
               }
            }

            label142: {
               try {
                  var33.dispose();
                  if (var30.isEmpty()) {
                     break label142;
                  }
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label140;
               }

               SQLiteCursor var31;
               try {
                  var31 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid, data FROM messages WHERE mid IN (%s)", TextUtils.join(",", var30)));
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label140;
               }

               label119:
               while(true) {
                  while(true) {
                     int var7;
                     NativeByteBuffer var34;
                     do {
                        try {
                           if (!var31.next()) {
                              break label119;
                           }

                           var7 = var31.intValue(0);
                           var34 = var31.byteBufferValue(1);
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label140;
                        }
                     } while(var34 == null);

                     try {
                        TLRPC.Message var8 = TLRPC.Message.TLdeserialize(var34, var34.readInt32(false), false);
                        var8.readAttachPath(var34, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var34.reuse();
                        if (var8.media instanceof TLRPC.TL_messageMediaWebPage) {
                           var8.id = var7;
                           var8.media.webpage = (TLRPC.WebPage)var1.valueAt(var4);
                           var2.add(var8);
                        }
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label140;
                     }
                  }
               }

               try {
                  var31.dispose();
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label140;
               }
            }

            ++var4;
         }

         try {
            if (var2.isEmpty()) {
               return;
            }
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break label140;
         }

         SQLitePreparedStatement var32;
         SQLitePreparedStatement var36;
         try {
            this.database.beginTransaction();
            var32 = this.database.executeFast("UPDATE messages SET data = ? WHERE mid = ?");
            var36 = this.database.executeFast("UPDATE media_v2 SET data = ? WHERE mid = ?");
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label140;
         }

         var4 = var3;

         while(true) {
            long var9;
            NativeByteBuffer var27;
            TLRPC.Message var35;
            try {
               if (var4 >= var2.size()) {
                  break;
               }

               var35 = (TLRPC.Message)var2.get(var4);
               var27 = new NativeByteBuffer(var35.getObjectSize());
               var35.serializeToStream(var27);
               var9 = (long)var35.id;
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label140;
            }

            long var11 = var9;

            try {
               if (var35.to_id.channel_id != 0) {
                  var11 = var9 | (long)var35.to_id.channel_id << 32;
               }
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label140;
            }

            try {
               var32.requery();
               var32.bindByteBuffer(1, (NativeByteBuffer)var27);
               var32.bindLong(2, var11);
               var32.step();
               var36.requery();
               var36.bindByteBuffer(1, (NativeByteBuffer)var27);
               var36.bindLong(2, var11);
               var36.step();
               var27.reuse();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label140;
            }

            ++var4;
         }

         try {
            var32.dispose();
            var36.dispose();
            this.database.commitTransaction();
            _$$Lambda$MessagesStorage$b6__z_rbTOVbfauOuDqwtjBSV3I var29 = new _$$Lambda$MessagesStorage$b6__z_rbTOVbfauOuDqwtjBSV3I(this, var2);
            AndroidUtilities.runOnUIThread(var29);
            return;
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
         }
      }

      Exception var28 = var10000;
      FileLog.e((Throwable)var28);
   }

   // $FF: synthetic method
   public void lambda$putWebRecent$37$MessagesStorage(ArrayList var1) {
      Exception var10000;
      label160: {
         SQLitePreparedStatement var2;
         boolean var10001;
         try {
            this.database.beginTransaction();
            var2 = this.database.executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
            break label160;
         }

         int var3 = 0;

         label152:
         while(true) {
            int var4;
            try {
               var4 = var1.size();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }

            short var5 = 200;
            if (var3 >= var4 || var3 == 200) {
               try {
                  var2.dispose();
                  this.database.commitTransaction();
                  if (var1.size() < 200) {
                     return;
                  }

                  this.database.beginTransaction();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               var3 = var5;

               while(true) {
                  try {
                     if (var3 >= var1.size()) {
                        break;
                     }

                     SQLiteDatabase var28 = this.database;
                     StringBuilder var26 = new StringBuilder();
                     var26.append("DELETE FROM web_recent_v3 WHERE id = '");
                     var26.append(((MediaController.SearchImage)var1.get(var3)).id);
                     var26.append("'");
                     var28.executeFast(var26.toString()).stepThis().dispose();
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label152;
                  }

                  ++var3;
               }

               try {
                  this.database.commitTransaction();
                  return;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }

            MediaController.SearchImage var6;
            String var7;
            try {
               var6 = (MediaController.SearchImage)var1.get(var3);
               var2.requery();
               var2.bindString(1, var6.id);
               var2.bindInteger(2, var6.type);
               var7 = var6.imageUrl;
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break;
            }

            String var8 = "";
            if (var7 != null) {
               try {
                  var7 = var6.imageUrl;
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break;
               }
            } else {
               var7 = "";
            }

            label148: {
               try {
                  var2.bindString(3, var7);
                  if (var6.thumbUrl != null) {
                     var7 = var6.thumbUrl;
                     break label148;
                  }
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               var7 = "";
            }

            try {
               var2.bindString(4, var7);
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break;
            }

            var7 = var8;

            try {
               if (var6.localUrl != null) {
                  var7 = var6.localUrl;
               }
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               var2.bindString(5, var7);
               var2.bindInteger(6, var6.width);
               var2.bindInteger(7, var6.height);
               var2.bindInteger(8, var6.size);
               var2.bindInteger(9, var6.date);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }

            NativeByteBuffer var27 = null;

            label164: {
               try {
                  if (var6.photo != null) {
                     var27 = new NativeByteBuffer(var6.photo.getObjectSize());
                     var6.photo.serializeToStream(var27);
                     var2.bindByteBuffer(10, (NativeByteBuffer)var27);
                     break label164;
                  }
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }

               try {
                  if (var6.document != null) {
                     var27 = new NativeByteBuffer(var6.document.getObjectSize());
                     var6.document.serializeToStream(var27);
                     var2.bindByteBuffer(10, (NativeByteBuffer)var27);
                     break label164;
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break;
               }

               try {
                  var2.bindNull(10);
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break;
               }
            }

            try {
               var2.step();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            if (var27 != null) {
               try {
                  var27.reuse();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }
            }

            ++var3;
         }
      }

      Exception var25 = var10000;
      FileLog.e((Throwable)var25);
   }

   // $FF: synthetic method
   public void lambda$readAllDialogs$27$MessagesStorage() {
      Exception var10000;
      label109: {
         ArrayList var1;
         ArrayList var2;
         ArrayList var3;
         LongSparseArray var4;
         SQLiteCursor var5;
         boolean var10001;
         try {
            var1 = new ArrayList();
            var2 = new ArrayList();
            var3 = new ArrayList();
            var4 = new LongSparseArray();
            var5 = this.database.queryFinalized("SELECT did, last_mid, unread_count, date FROM dialogs WHERE unread_count != 0");
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label109;
         }

         label108:
         while(true) {
            long var6;
            while(true) {
               try {
                  if (var5.next()) {
                     var6 = var5.longValue(0);
                     if (!DialogObject.isFolderDialogId(var6)) {
                        break;
                     }
                     continue;
                  }
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label108;
               }

               ArrayList var8;
               ArrayList var11;
               boolean var12;
               ArrayList var26;
               try {
                  var5.dispose();
                  var11 = new ArrayList();
                  var26 = new ArrayList();
                  var8 = new ArrayList();
                  var12 = var3.isEmpty();
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label108;
               }

               if (!var12) {
                  try {
                     this.getEncryptedChatsInternal(TextUtils.join(",", var3), var8, var1);
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label108;
                  }
               }

               try {
                  if (!var1.isEmpty()) {
                     this.getUsersInternal(TextUtils.join(",", var1), var11);
                  }
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label108;
               }

               try {
                  if (!var2.isEmpty()) {
                     this.getChatsInternal(TextUtils.join(",", var2), var26);
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label108;
               }

               try {
                  _$$Lambda$MessagesStorage$8eKeJbrxLTdGP5YjS5xvuniLwx0 var24 = new _$$Lambda$MessagesStorage$8eKeJbrxLTdGP5YjS5xvuniLwx0(this, var11, var26, var8, var4);
                  AndroidUtilities.runOnUIThread(var24);
                  return;
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label108;
               }
            }

            try {
               MessagesStorage.ReadDialog var27 = new MessagesStorage.ReadDialog();
               var27.lastMid = var5.intValue(1);
               var27.unreadCount = var5.intValue(2);
               var27.date = var5.intValue(3);
               var4.put(var6, var27);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break;
            }

            int var9 = (int)var6;
            int var10 = (int)(var6 >> 32);
            if (var9 != 0) {
               if (var9 < 0) {
                  var10 = -var9;

                  try {
                     if (!var2.contains(var10)) {
                        var2.add(var10);
                     }
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     if (!var1.contains(var9)) {
                        var1.add(var9);
                     }
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break;
                  }
               }
            } else {
               try {
                  if (!var3.contains(var10)) {
                     var3.add(var10);
                  }
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      Exception var25 = var10000;
      FileLog.e((Throwable)var25);
   }

   // $FF: synthetic method
   public void lambda$removeFromDownloadQueue$114$MessagesStorage(boolean var1, int var2, long var3) {
      Exception var10000;
      boolean var10001;
      if (var1) {
         label50: {
            SQLiteCursor var5;
            int var6;
            label41: {
               try {
                  var5 = this.database.queryFinalized(String.format(Locale.US, "SELECT min(date) FROM download_queue WHERE type = %d", var2));
                  if (var5.next()) {
                     var6 = var5.intValue(0);
                     break label41;
                  }
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label50;
               }

               var6 = -1;
            }

            try {
               var5.dispose();
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label50;
            }

            if (var6 == -1) {
               return;
            }

            try {
               this.database.executeFast(String.format(Locale.US, "UPDATE download_queue SET date = %d WHERE uid = %d AND type = %d", var6 - 1, var3, var2)).stepThis().dispose();
               return;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }
      } else {
         try {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM download_queue WHERE uid = %d AND type = %d", var3, var2)).stepThis().dispose();
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      FileLog.e((Throwable)var11);
   }

   // $FF: synthetic method
   public void lambda$removePendingTask$7$MessagesStorage(long var1) {
      try {
         SQLiteDatabase var3 = this.database;
         StringBuilder var4 = new StringBuilder();
         var4.append("DELETE FROM pending_tasks WHERE id = ");
         var4.append(var1);
         var3.executeFast(var4.toString()).stepThis().dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   // $FF: synthetic method
   public void lambda$replaceMessageIfExists$137$MessagesStorage(TLRPC.Message param1, boolean param2, ArrayList param3, ArrayList param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$resetDialogs$52$MessagesStorage(TLRPC.messages_Dialogs var1, int var2, int var3, int var4, int var5, int var6, TLRPC.Message var7, int var8, LongSparseArray var9, LongSparseArray var10) {
      Exception var10000;
      label290: {
         ArrayList var11;
         int var12;
         LongSparseArray var13;
         ArrayList var14;
         ArrayList var15;
         boolean var10001;
         try {
            var11 = new ArrayList();
            var12 = var1.dialogs.size() - var2;
            var13 = new LongSparseArray();
            var14 = new ArrayList();
            var15 = new ArrayList();
         } catch (Exception var49) {
            var10000 = var49;
            var10001 = false;
            break label290;
         }

         int var16 = var2;

         while(true) {
            try {
               if (var16 >= var1.dialogs.size()) {
                  break;
               }

               var15.add(((TLRPC.Dialog)var1.dialogs.get(var16)).id);
            } catch (Exception var48) {
               var10000 = var48;
               var10001 = false;
               break label290;
            }

            ++var16;
         }

         SQLiteCursor var17;
         try {
            var17 = this.database.queryFinalized("SELECT did, pinned FROM dialogs WHERE 1");
         } catch (Exception var47) {
            var10000 = var47;
            var10001 = false;
            break label290;
         }

         var16 = 0;

         long var18;
         int var20;
         int var21;
         while(true) {
            try {
               if (!var17.next()) {
                  break;
               }

               var18 = var17.longValue(0);
               var20 = var17.intValue(1);
            } catch (Exception var46) {
               var10000 = var46;
               var10001 = false;
               break label290;
            }

            var21 = (int)var18;
            if (var21 != 0) {
               try {
                  var11.add(var21);
               } catch (Exception var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label290;
               }

               if (var20 > 0) {
                  try {
                     var16 = Math.max(var20, var16);
                     var13.put(var18, var20);
                     var14.add(var18);
                  } catch (Exception var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label290;
                  }
               }
            }
         }

         try {
            _$$Lambda$MessagesStorage$k30A8i0EMAqCRpnk5v4RsWWhCoc var22 = new _$$Lambda$MessagesStorage$k30A8i0EMAqCRpnk5v4RsWWhCoc(var13);
            Collections.sort(var14, var22);
         } catch (Exception var42) {
            var10000 = var42;
            var10001 = false;
            break label290;
         }

         while(true) {
            try {
               if (var14.size() >= var12) {
                  break;
               }

               var14.add(0, 0L);
            } catch (Exception var43) {
               var10000 = var43;
               var10001 = false;
               break label290;
            }
         }

         try {
            var17.dispose();
            StringBuilder var58 = new StringBuilder();
            var58.append("(");
            var58.append(TextUtils.join(",", var11));
            var58.append(")");
            String var51 = var58.toString();
            this.database.beginTransaction();
            SQLiteDatabase var59 = this.database;
            StringBuilder var56 = new StringBuilder();
            var56.append("DELETE FROM dialogs WHERE did IN ");
            var56.append(var51);
            var59.executeFast(var56.toString()).stepThis().dispose();
            var59 = this.database;
            var56 = new StringBuilder();
            var56.append("DELETE FROM messages WHERE uid IN ");
            var56.append(var51);
            var59.executeFast(var56.toString()).stepThis().dispose();
            this.database.executeFast("DELETE FROM polls WHERE 1").stepThis().dispose();
            SQLiteDatabase var57 = this.database;
            var58 = new StringBuilder();
            var58.append("DELETE FROM bot_keyboard WHERE uid IN ");
            var58.append(var51);
            var57.executeFast(var58.toString()).stepThis().dispose();
            var59 = this.database;
            var56 = new StringBuilder();
            var56.append("DELETE FROM media_v2 WHERE uid IN ");
            var56.append(var51);
            var59.executeFast(var56.toString()).stepThis().dispose();
            var57 = this.database;
            var58 = new StringBuilder();
            var58.append("DELETE FROM messages_holes WHERE uid IN ");
            var58.append(var51);
            var57.executeFast(var58.toString()).stepThis().dispose();
            var59 = this.database;
            var56 = new StringBuilder();
            var56.append("DELETE FROM media_holes_v2 WHERE uid IN ");
            var56.append(var51);
            var59.executeFast(var56.toString()).stepThis().dispose();
            this.database.commitTransaction();
         } catch (Exception var41) {
            var10000 = var41;
            var10001 = false;
            break label290;
         }

         var20 = 0;

         label243:
         while(true) {
            int var23;
            if (var20 >= var12) {
               int var24;
               int var25;
               label293: {
                  TLRPC.Chat var54;
                  label294: {
                     try {
                        this.putDialogsInternal(var1, 0);
                        this.saveDiffParamsInternal(var3, var4, var5, var6);
                        var24 = UserConfig.getInstance(this.currentAccount).getTotalDialogsCount(0);
                        UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(0);
                        var25 = var1.dialogs.size();
                        var23 = var7.id;
                        var21 = var7.date;
                        if (var7.to_id.channel_id == 0) {
                           break label294;
                        }

                        var16 = var7.to_id.channel_id;
                     } catch (Exception var33) {
                        var10000 = var33;
                        var10001 = false;
                        break;
                     }

                     var2 = 0;

                     while(true) {
                        label203: {
                           try {
                              if (var2 < var1.chats.size()) {
                                 var54 = (TLRPC.Chat)var1.chats.get(var2);
                                 if (var54.id != var16) {
                                    break label203;
                                 }

                                 var18 = var54.access_hash;
                                 break;
                              }
                           } catch (Exception var32) {
                              var10000 = var32;
                              var10001 = false;
                              break label243;
                           }

                           var18 = 0L;
                           break;
                        }

                        ++var2;
                     }

                     byte var53 = 0;
                     var20 = 0;
                     var2 = var16;
                     var16 = var53;
                     break label293;
                  }

                  label191: {
                     label190: {
                        label296: {
                           label297: {
                              try {
                                 if (var7.to_id.chat_id != 0) {
                                    var16 = var7.to_id.chat_id;
                                    break label297;
                                 }
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (var7.to_id.user_id == 0) {
                                    break label190;
                                 }

                                 var2 = var7.to_id.user_id;
                              } catch (Exception var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break;
                              }

                              var16 = 0;

                              while(true) {
                                 label172: {
                                    try {
                                       if (var16 < var1.users.size()) {
                                          TLRPC.User var55 = (TLRPC.User)var1.users.get(var16);
                                          if (var55.id != var2) {
                                             break label172;
                                          }

                                          var18 = var55.access_hash;
                                          break;
                                       }
                                    } catch (Exception var29) {
                                       var10000 = var29;
                                       var10001 = false;
                                       break label243;
                                    }

                                    var20 = var2;
                                    break label191;
                                 }

                                 ++var16;
                              }

                              var16 = 0;
                              var20 = var2;
                              break label296;
                           }

                           var2 = 0;

                           while(true) {
                              label156: {
                                 try {
                                    if (var2 < var1.chats.size()) {
                                       var54 = (TLRPC.Chat)var1.chats.get(var2);
                                       if (var54.id != var16) {
                                          break label156;
                                       }

                                       var18 = var54.access_hash;
                                       break;
                                    }
                                 } catch (Exception var28) {
                                    var10000 = var28;
                                    var10001 = false;
                                    break label243;
                                 }

                                 var18 = 0L;
                                 break;
                              }

                              ++var2;
                           }

                           var20 = 0;
                        }

                        var2 = 0;
                        break label293;
                     }

                     var20 = 0;
                  }

                  var16 = 0;
                  var2 = 0;
                  var18 = 0L;
               }

               for(var12 = 0; var12 < 2; ++var12) {
                  try {
                     UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var12, var23, var21, var20, var16, var2, var18);
                     UserConfig.getInstance(this.currentAccount).setTotalDialogsCount(var12, var24 + var25);
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label243;
                  }
               }

               try {
                  UserConfig.getInstance(this.currentAccount).saveConfig(false);
                  MessagesController.getInstance(this.currentAccount).completeDialogsReset(var1, var8, var3, var4, var5, var6, var9, var10, var7);
                  return;
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break;
               }
            }

            label301: {
               TLRPC.Dialog var52;
               try {
                  var52 = (TLRPC.Dialog)var1.dialogs.get(var2 + var20);
                  if (var52 instanceof TLRPC.TL_dialog && !var52.pinned) {
                     break label301;
                  }
               } catch (Exception var40) {
                  var10000 = var40;
                  var10001 = false;
                  break;
               }

               try {
                  var23 = var14.indexOf(var52.id);
                  var21 = var15.indexOf(var52.id);
               } catch (Exception var39) {
                  var10000 = var39;
                  var10001 = false;
                  break;
               }

               if (var23 != -1 && var21 != -1) {
                  Integer var60;
                  if (var23 == var21) {
                     try {
                        var60 = (Integer)var13.get(var52.id);
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break;
                     }

                     if (var60 != null) {
                        try {
                           var52.pinnedNum = var60;
                        } catch (Exception var37) {
                           var10000 = var37;
                           var10001 = false;
                           break;
                        }
                     }
                  } else {
                     try {
                        var60 = (Integer)var13.get((Long)var14.get(var21));
                     } catch (Exception var36) {
                        var10000 = var36;
                        var10001 = false;
                        break;
                     }

                     if (var60 != null) {
                        try {
                           var52.pinnedNum = var60;
                        } catch (Exception var35) {
                           var10000 = var35;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }

               try {
                  if (var52.pinnedNum == 0) {
                     var52.pinnedNum = var12 - var20 + var16;
                  }
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break;
               }
            }

            ++var20;
         }
      }

      Exception var50 = var10000;
      FileLog.e((Throwable)var50);
   }

   // $FF: synthetic method
   public void lambda$resetMentionsCount$60$MessagesStorage(int var1, long var2) {
      Exception var10000;
      label21: {
         boolean var10001;
         if (var1 == 0) {
            try {
               this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE uid = %d AND mention = 1 AND read_state IN(0, 1)", var2)).stepThis().dispose();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label21;
            }
         }

         try {
            this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", var1, var2)).stepThis().dispose();
            LongSparseArray var7 = new LongSparseArray(1);
            var7.put(var2, var1);
            MessagesController.getInstance(this.currentAccount).processDialogsUpdateRead((LongSparseArray)null, var7);
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var4 = var10000;
      FileLog.e((Throwable)var4);
   }

   // $FF: synthetic method
   public void lambda$saveBotCache$71$MessagesStorage(TLObject var1, String var2) {
      Exception var10000;
      label38: {
         int var4;
         boolean var10001;
         label37: {
            int var3;
            label41: {
               try {
                  var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                  if (var1 instanceof TLRPC.TL_messages_botCallbackAnswer) {
                     var4 = ((TLRPC.TL_messages_botCallbackAnswer)var1).cache_time;
                     break label41;
                  }
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label38;
               }

               var4 = var3;

               try {
                  if (!(var1 instanceof TLRPC.TL_messages_botResults)) {
                     break label37;
                  }

                  var4 = ((TLRPC.TL_messages_botResults)var1).cache_time;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label38;
               }
            }

            var4 += var3;
         }

         try {
            SQLitePreparedStatement var5 = this.database.executeFast("REPLACE INTO botcache VALUES(?, ?, ?)");
            NativeByteBuffer var6 = new NativeByteBuffer(var1.getObjectSize());
            var1.serializeToStream(var6);
            var5.bindString(1, var2);
            var5.bindInteger(2, var4);
            var5.bindByteBuffer(3, (NativeByteBuffer)var6);
            var5.step();
            var5.dispose();
            var6.reuse();
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var10 = var10000;
      FileLog.e((Throwable)var10);
   }

   // $FF: synthetic method
   public void lambda$saveChannelPts$22$MessagesStorage(int var1, int var2) {
      try {
         SQLitePreparedStatement var3 = this.database.executeFast("UPDATE dialogs SET pts = ? WHERE did = ?");
         var3.bindInteger(1, var1);
         var3.bindInteger(2, -var2);
         var3.step();
         var3.dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$saveDiffParams$23$MessagesStorage(int var1, int var2, int var3, int var4) {
      this.saveDiffParamsInternal(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$saveSecretParams$4$MessagesStorage(int var1, int var2, byte[] var3) {
      Exception var10000;
      label52: {
         SQLitePreparedStatement var4;
         boolean var10001;
         try {
            var4 = this.database.executeFast("UPDATE params SET lsv = ?, sg = ?, pbytes = ? WHERE id = 1");
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label52;
         }

         byte var5 = 1;

         NativeByteBuffer var6;
         try {
            var4.bindInteger(1, var1);
            var4.bindInteger(2, var2);
            var6 = new NativeByteBuffer;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label52;
         }

         var1 = var5;
         if (var3 != null) {
            try {
               var1 = var3.length;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label52;
            }
         }

         try {
            var6.<init>(var1);
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label52;
         }

         if (var3 != null) {
            try {
               var6.writeBytes(var3);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label52;
            }
         }

         try {
            var4.bindByteBuffer(3, (NativeByteBuffer)var6);
            var4.step();
            var4.dispose();
            var6.reuse();
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   // $FF: synthetic method
   public void lambda$setDialogFlags$24$MessagesStorage(long var1, long var3) {
      try {
         this.database.executeFast(String.format(Locale.US, "REPLACE INTO dialog_settings VALUES(%d, %d)", var1, var3)).stepThis().dispose();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   // $FF: synthetic method
   public void lambda$setDialogPinned$147$MessagesStorage(int var1, long var2) {
      try {
         SQLitePreparedStatement var4 = this.database.executeFast("UPDATE dialogs SET pinned = ? WHERE did = ?");
         var4.bindInteger(1, var1);
         var4.bindLong(2, var2);
         var4.step();
         var4.dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   // $FF: synthetic method
   public void lambda$setDialogUnread$146$MessagesStorage(long param1, boolean param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$setDialogsFolderId$142$MessagesStorage(ArrayList var1, ArrayList var2, int var3, long var4) {
      Exception var10000;
      label68: {
         SQLitePreparedStatement var6;
         boolean var10001;
         try {
            this.database.beginTransaction();
            var6 = this.database.executeFast("UPDATE dialogs SET folder_id = ? WHERE did = ?");
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label68;
         }

         byte var7 = 0;
         byte var8 = 0;
         if (var1 != null) {
            int var19;
            try {
               var19 = var1.size();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label68;
            }

            for(var3 = var8; var3 < var19; ++var3) {
               try {
                  TLRPC.TL_folderPeer var18 = (TLRPC.TL_folderPeer)var1.get(var3);
                  var4 = DialogObject.getPeerDialogId(var18.peer);
                  var6.requery();
                  var6.bindInteger(1, var18.folder_id);
                  var6.bindLong(2, var4);
                  var6.step();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label68;
               }
            }
         } else if (var2 != null) {
            int var20;
            try {
               var20 = var2.size();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label68;
            }

            for(var3 = var7; var3 < var20; ++var3) {
               try {
                  TLRPC.TL_inputFolderPeer var16 = (TLRPC.TL_inputFolderPeer)var2.get(var3);
                  var4 = DialogObject.getPeerDialogId(var16.peer);
                  var6.requery();
                  var6.bindInteger(1, var16.folder_id);
                  var6.bindLong(2, var4);
                  var6.step();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label68;
               }
            }
         } else {
            try {
               var6.requery();
               var6.bindInteger(1, var3);
               var6.bindLong(2, var4);
               var6.step();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label68;
            }
         }

         try {
            var6.dispose();
            this.database.commitTransaction();
            this.checkIfFolderEmptyInternal(1);
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   public void lambda$setMessageSeq$126$MessagesStorage(int var1, int var2, int var3) {
      try {
         SQLitePreparedStatement var4 = this.database.executeFast("REPLACE INTO messages_seq VALUES(?, ?, ?)");
         var4.requery();
         var4.bindInteger(1, var1);
         var4.bindInteger(2, var2);
         var4.bindInteger(3, var3);
         var4.step();
         var4.dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   // $FF: synthetic method
   public void lambda$unpinAllDialogsExceptNew$145$MessagesStorage(ArrayList var1, int var2) {
      Exception var10000;
      label71: {
         ArrayList var3;
         SQLiteCursor var12;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var12 = this.database.queryFinalized(String.format(Locale.US, "SELECT did, folder_id FROM dialogs WHERE pinned != 0 AND did NOT IN (%s)", TextUtils.join(",", var1)));
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label71;
         }

         long var4;
         while(true) {
            try {
               if (!var12.next()) {
                  break;
               }

               var4 = var12.longValue(0);
               if (var12.intValue(1) != var2) {
                  continue;
               }
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label71;
            }

            if ((int)var4 != 0) {
               try {
                  if (!DialogObject.isFolderDialogId(var4)) {
                     var3.add(var12.longValue(0));
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label71;
               }
            }
         }

         SQLitePreparedStatement var13;
         try {
            var12.dispose();
            if (var3.isEmpty()) {
               return;
            }

            var13 = this.database.executeFast("UPDATE dialogs SET pinned = ? WHERE did = ?");
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label71;
         }

         var2 = 0;

         while(true) {
            try {
               if (var2 >= var3.size()) {
                  break;
               }

               var4 = (Long)var3.get(var2);
               var13.requery();
               var13.bindInteger(1, 0);
               var13.bindLong(2, var4);
               var13.step();
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label71;
            }

            ++var2;
         }

         try {
            var13.dispose();
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public void lambda$updateChannelUsers$70$MessagesStorage(int var1, ArrayList var2) {
      long var3 = (long)(-var1);

      Exception var10000;
      label40: {
         int var7;
         SQLitePreparedStatement var14;
         boolean var10001;
         try {
            SQLiteDatabase var5 = this.database;
            StringBuilder var6 = new StringBuilder();
            var6.append("DELETE FROM channel_users_v2 WHERE did = ");
            var6.append(var3);
            var5.executeFast(var6.toString()).stepThis().dispose();
            this.database.beginTransaction();
            var14 = this.database.executeFast("REPLACE INTO channel_users_v2 VALUES(?, ?, ?, ?)");
            var7 = (int)(System.currentTimeMillis() / 1000L);
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label40;
         }

         int var8 = 0;

         while(true) {
            try {
               if (var8 >= var2.size()) {
                  break;
               }

               TLRPC.ChannelParticipant var15 = (TLRPC.ChannelParticipant)var2.get(var8);
               var14.requery();
               var14.bindLong(1, var3);
               var14.bindInteger(2, var15.user_id);
               var14.bindInteger(3, var7);
               NativeByteBuffer var9 = new NativeByteBuffer(var15.getObjectSize());
               var15.serializeToStream(var9);
               var14.bindByteBuffer(4, (NativeByteBuffer)var9);
               var9.reuse();
               var14.step();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label40;
            }

            --var7;
            ++var8;
         }

         try {
            var14.dispose();
            this.database.commitTransaction();
            this.loadChatInfo(var1, (CountDownLatch)null, false, true);
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   // $FF: synthetic method
   public void lambda$updateChatDefaultBannedRights$112$MessagesStorage(int var1, int var2, TLRPC.TL_chatBannedRights var3) {
      NativeByteBuffer var4 = null;

      Exception var10000;
      label69: {
         SQLiteCursor var5;
         boolean var10001;
         try {
            var5 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid = %d", var1));
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label69;
         }

         TLRPC.Chat var6 = var4;

         label58: {
            NativeByteBuffer var7;
            try {
               if (!var5.next()) {
                  break label58;
               }

               var7 = var5.byteBufferValue(0);
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label69;
            }

            var6 = var4;
            if (var7 != null) {
               try {
                  var6 = TLRPC.Chat.TLdeserialize(var7, var7.readInt32(false), false);
                  var7.reuse();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label69;
               }
            }
         }

         try {
            var5.dispose();
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label69;
         }

         if (var6 == null) {
            return;
         }

         try {
            if (var6.default_banned_rights != null && var2 < var6.version) {
               return;
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label69;
         }

         try {
            var6.default_banned_rights = var3;
            var6.flags |= 262144;
            var6.version = var2;
            SQLitePreparedStatement var15 = this.database.executeFast("UPDATE chats SET data = ? WHERE uid = ?");
            var4 = new NativeByteBuffer(var6.getObjectSize());
            var6.serializeToStream(var4);
            var15.bindByteBuffer(1, (NativeByteBuffer)var4);
            var15.bindInteger(2, var6.id);
            var15.step();
            var4.reuse();
            var15.dispose();
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public void lambda$updateChatInfo$75$MessagesStorage(TLRPC.ChatFull var1, boolean var2) {
      Exception var10000;
      label71: {
         SQLiteDatabase var3;
         StringBuilder var4;
         int var5;
         SQLiteCursor var12;
         boolean var10001;
         label64: {
            try {
               var3 = this.database;
               var4 = new StringBuilder();
               var4.append("SELECT online FROM chat_settings_v2 WHERE uid = ");
               var4.append(var1.id);
               var12 = var3.queryFinalized(var4.toString());
               if (var12.next()) {
                  var5 = var12.intValue(0);
                  break label64;
               }
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label71;
            }

            var5 = -1;
         }

         try {
            var12.dispose();
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label71;
         }

         if (var2 && var5 == -1) {
            return;
         }

         if (var5 >= 0) {
            try {
               if ((var1.flags & 8192) == 0) {
                  var1.online_count = var5;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label71;
            }
         }

         SQLiteCursor var16;
         try {
            SQLitePreparedStatement var14 = this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?, ?)");
            NativeByteBuffer var13 = new NativeByteBuffer(var1.getObjectSize());
            var1.serializeToStream(var13);
            var14.bindInteger(1, var1.id);
            var14.bindByteBuffer(2, (NativeByteBuffer)var13);
            var14.bindInteger(3, var1.pinned_msg_id);
            var14.bindInteger(4, var1.online_count);
            var14.step();
            var14.dispose();
            var13.reuse();
            if (!(var1 instanceof TLRPC.TL_channelFull)) {
               return;
            }

            var3 = this.database;
            var4 = new StringBuilder();
            var4.append("SELECT inbox_max, outbox_max FROM dialogs WHERE did = ");
            var4.append(-var1.id);
            var16 = var3.queryFinalized(var4.toString());
            if (var16.next() && var16.intValue(0) < var1.read_inbox_max_id) {
               var5 = var16.intValue(1);
               SQLitePreparedStatement var15 = this.database.executeFast("UPDATE dialogs SET unread_count = ?, inbox_max = ?, outbox_max = ? WHERE did = ?");
               var15.bindInteger(1, var1.unread_count);
               var15.bindInteger(2, var1.read_inbox_max_id);
               var15.bindInteger(3, Math.max(var5, var1.read_outbox_max_id));
               var15.bindLong(4, (long)(-var1.id));
               var15.step();
               var15.dispose();
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label71;
         }

         try {
            var16.dispose();
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      FileLog.e((Throwable)var11);
   }

   // $FF: synthetic method
   public void lambda$updateChatInfo$82$MessagesStorage(int var1, int var2, int var3, int var4, int var5) {
      Exception var10000;
      label150: {
         SQLiteDatabase var6;
         StringBuilder var7;
         boolean var10001;
         String var28;
         try {
            var6 = this.database;
            var7 = new StringBuilder();
            var7.append("SELECT info, pinned, online FROM chat_settings_v2 WHERE uid = ");
            var7.append(var1);
            var28 = var7.toString();
         } catch (Exception var26) {
            var10000 = var26;
            var10001 = false;
            break label150;
         }

         byte var8 = 0;
         byte var9 = 0;

         SQLiteCursor var10;
         try {
            var10 = var6.queryFinalized(var28);
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break label150;
         }

         var7 = null;

         try {
            new ArrayList();
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
            break label150;
         }

         TLRPC.ChatFull var27 = var7;

         label134: {
            NativeByteBuffer var11;
            try {
               if (!var10.next()) {
                  break label134;
               }

               var11 = var10.byteBufferValue(0);
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label150;
            }

            var27 = var7;
            if (var11 != null) {
               try {
                  var27 = TLRPC.ChatFull.TLdeserialize(var11, var11.readInt32(false), false);
                  var11.reuse();
                  var27.pinned_msg_id = var10.intValue(1);
                  var27.online_count = var10.intValue(2);
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label150;
               }
            }
         }

         try {
            var10.dispose();
            if (!(var27 instanceof TLRPC.TL_chatFull)) {
               return;
            }
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break label150;
         }

         if (var2 == 1) {
            var2 = var9;

            while(true) {
               try {
                  if (var2 >= var27.participants.participants.size()) {
                     break;
                  }

                  if (((TLRPC.ChatParticipant)var27.participants.participants.get(var2)).user_id == var3) {
                     var27.participants.participants.remove(var2);
                     break;
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label150;
               }

               ++var2;
            }
         } else if (var2 == 0) {
            Iterator var30;
            try {
               var30 = var27.participants.participants.iterator();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label150;
            }

            try {
               while(var30.hasNext()) {
                  if (((TLRPC.ChatParticipant)var30.next()).user_id == var3) {
                     return;
                  }
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label150;
            }

            try {
               TLRPC.TL_chatParticipant var31 = new TLRPC.TL_chatParticipant();
               var31.user_id = var3;
               var31.inviter_id = var4;
               var31.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
               var27.participants.participants.add(var31);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label150;
            }
         } else if (var2 == 2) {
            label154: {
               var2 = var8;

               TLRPC.ChatParticipant var35;
               while(true) {
                  try {
                     if (var2 >= var27.participants.participants.size()) {
                        break label154;
                     }

                     var35 = (TLRPC.ChatParticipant)var27.participants.participants.get(var2);
                     if (var35.user_id == var3) {
                        break;
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label150;
                  }

                  ++var2;
               }

               Object var32;
               if (var4 == 1) {
                  try {
                     var32 = new TLRPC.TL_chatParticipantAdmin();
                     ((TLRPC.ChatParticipant)var32).user_id = var35.user_id;
                     ((TLRPC.ChatParticipant)var32).date = var35.date;
                     ((TLRPC.ChatParticipant)var32).inviter_id = var35.inviter_id;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label150;
                  }
               } else {
                  try {
                     var32 = new TLRPC.TL_chatParticipant();
                     ((TLRPC.ChatParticipant)var32).user_id = var35.user_id;
                     ((TLRPC.ChatParticipant)var32).date = var35.date;
                     ((TLRPC.ChatParticipant)var32).inviter_id = var35.inviter_id;
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label150;
                  }
               }

               try {
                  var27.participants.participants.set(var2, var32);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label150;
               }
            }
         }

         try {
            var27.participants.version = var5;
            _$$Lambda$MessagesStorage$OhB9rklUh_vaW5jSFjxvzZmL4rA var33 = new _$$Lambda$MessagesStorage$OhB9rklUh_vaW5jSFjxvzZmL4rA(this, var27);
            AndroidUtilities.runOnUIThread(var33);
            SQLitePreparedStatement var34 = this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?, ?)");
            NativeByteBuffer var36 = new NativeByteBuffer(var27.getObjectSize());
            var27.serializeToStream(var36);
            var34.bindInteger(1, var1);
            var34.bindByteBuffer(2, (NativeByteBuffer)var36);
            var34.bindInteger(3, var27.pinned_msg_id);
            var34.bindInteger(4, var27.online_count);
            var34.step();
            var34.dispose();
            var36.reuse();
            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var29 = var10000;
      FileLog.e((Throwable)var29);
   }

   // $FF: synthetic method
   public void lambda$updateChatOnlineCount$78$MessagesStorage(int var1, int var2) {
      try {
         SQLitePreparedStatement var3 = this.database.executeFast("UPDATE chat_settings_v2 SET online = ? WHERE uid = ?");
         var3.requery();
         var3.bindInteger(1, var1);
         var3.bindInteger(2, var2);
         var3.step();
         var3.dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$updateChatParticipants$67$MessagesStorage(TLRPC.ChatParticipants var1) {
      Exception var10000;
      label51: {
         StringBuilder var3;
         SQLiteCursor var4;
         boolean var10001;
         try {
            SQLiteDatabase var2 = this.database;
            var3 = new StringBuilder();
            var3.append("SELECT info, pinned, online FROM chat_settings_v2 WHERE uid = ");
            var3.append(var1.chat_id);
            var4 = var2.queryFinalized(var3.toString());
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label51;
         }

         var3 = null;

         try {
            new ArrayList();
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label51;
         }

         TLRPC.ChatFull var14 = var3;

         label39: {
            NativeByteBuffer var5;
            try {
               if (!var4.next()) {
                  break label39;
               }

               var5 = var4.byteBufferValue(0);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label51;
            }

            var14 = var3;
            if (var5 != null) {
               try {
                  var14 = TLRPC.ChatFull.TLdeserialize(var5, var5.readInt32(false), false);
                  var5.reuse();
                  var14.pinned_msg_id = var4.intValue(1);
                  var14.online_count = var4.intValue(2);
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label51;
               }
            }
         }

         try {
            var4.dispose();
            if (var14 instanceof TLRPC.TL_chatFull) {
               var14.participants = var1;
               _$$Lambda$MessagesStorage$nxqnJkYq3vw5KhxgpTpnuTR0WR8 var12 = new _$$Lambda$MessagesStorage$nxqnJkYq3vw5KhxgpTpnuTR0WR8(this, var14);
               AndroidUtilities.runOnUIThread(var12);
               SQLitePreparedStatement var13 = this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?, ?)");
               NativeByteBuffer var15 = new NativeByteBuffer(var14.getObjectSize());
               var14.serializeToStream(var15);
               var13.bindInteger(1, var14.id);
               var13.bindByteBuffer(2, (NativeByteBuffer)var15);
               var13.bindInteger(3, var14.pinned_msg_id);
               var13.bindInteger(4, var14.online_count);
               var13.step();
               var13.dispose();
               var15.reuse();
            }

            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var11 = var10000;
      FileLog.e((Throwable)var11);
   }

   // $FF: synthetic method
   public void lambda$updateChatPinnedMessage$80$MessagesStorage(int var1, int var2) {
      Exception var10000;
      label70: {
         SQLiteDatabase var3;
         SQLiteCursor var5;
         boolean var10001;
         try {
            var3 = this.database;
            StringBuilder var4 = new StringBuilder();
            var4.append("SELECT info, pinned, online FROM chat_settings_v2 WHERE uid = ");
            var4.append(var1);
            var5 = var3.queryFinalized(var4.toString());
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label70;
         }

         var3 = null;
         TLRPC.ChatFull var16 = var3;

         label62: {
            NativeByteBuffer var6;
            try {
               if (!var5.next()) {
                  break label62;
               }

               var6 = var5.byteBufferValue(0);
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label70;
            }

            var16 = var3;
            if (var6 != null) {
               try {
                  var16 = TLRPC.ChatFull.TLdeserialize(var6, var6.readInt32(false), false);
                  var6.reuse();
                  var16.pinned_msg_id = var5.intValue(1);
                  var16.online_count = var5.intValue(2);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label70;
               }
            }
         }

         try {
            var5.dispose();
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label70;
         }

         if (var16 == null) {
            return;
         }

         label71: {
            try {
               if (var16 instanceof TLRPC.TL_channelFull) {
                  var16.pinned_msg_id = var2;
                  var16.flags |= 32;
                  break label71;
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label70;
            }

            try {
               if (var16 instanceof TLRPC.TL_chatFull) {
                  var16.pinned_msg_id = var2;
                  var16.flags |= 64;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label70;
            }
         }

         try {
            _$$Lambda$MessagesStorage$NxZU0dQWFf1s1Ei7lgvtohw5Sqw var14 = new _$$Lambda$MessagesStorage$NxZU0dQWFf1s1Ei7lgvtohw5Sqw(this, var16);
            AndroidUtilities.runOnUIThread(var14);
            SQLitePreparedStatement var17 = this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?, ?)");
            NativeByteBuffer var15 = new NativeByteBuffer(var16.getObjectSize());
            var16.serializeToStream(var15);
            var17.bindInteger(1, var1);
            var17.bindByteBuffer(2, (NativeByteBuffer)var15);
            var17.bindInteger(3, var16.pinned_msg_id);
            var17.bindInteger(4, var16.online_count);
            var17.step();
            var17.dispose();
            var15.reuse();
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var18 = var10000;
      FileLog.e((Throwable)var18);
   }

   // $FF: synthetic method
   public void lambda$updateDbToLastVersion$1$MessagesStorage(int var1) {
      Exception var10000;
      label593: {
         int var2 = var1;
         boolean var10001;
         if (var1 < 4) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS read_state_out_idx_messages;").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS ttl_idx_messages;").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS date_idx_messages;").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_contacts_v6(uid INTEGER PRIMARY KEY, fname TEXT, sname TEXT)").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_phones_v6(uid INTEGER, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (uid, phone))").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v6(sphone, deleted);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_randoms ON randoms(mid);").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS sent_files_v2(uid TEXT, type INTEGER, data BLOB, PRIMARY KEY (uid, type))").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS blocked_users(uid INTEGER PRIMARY KEY)").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS download_queue(uid INTEGER, type INTEGER, date INTEGER, data BLOB, PRIMARY KEY (uid, type));").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS type_date_idx_download_queue ON download_queue(type, date);").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS dialog_settings(did INTEGER PRIMARY KEY, flags INTEGER);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS send_state_idx_messages ON messages(mid, send_state, date) WHERE mid < 0 AND send_state = 1;").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_idx_dialogs ON dialogs(unread_count);").stepThis().dispose();
               this.database.executeFast("UPDATE messages SET send_state = 2 WHERE mid < 0 AND send_state = 1").stepThis().dispose();
               this.fixNotificationSettings();
               this.database.executeFast("PRAGMA user_version = 4").stepThis().dispose();
            } catch (Exception var65) {
               var10000 = var65;
               var10001 = false;
               break label593;
            }

            var2 = 4;
         }

         var1 = var2;
         SQLitePreparedStatement var3;
         int var6;
         if (var2 == 4) {
            SQLiteCursor var4;
            label595: {
               NativeByteBuffer var5;
               try {
                  this.database.executeFast("CREATE TABLE IF NOT EXISTS enc_tasks_v2(mid INTEGER PRIMARY KEY, date INTEGER)").stepThis().dispose();
                  this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_enc_tasks_v2 ON enc_tasks_v2(date);").stepThis().dispose();
                  this.database.beginTransaction();
                  var4 = this.database.queryFinalized("SELECT date, data FROM enc_tasks WHERE 1");
                  var3 = this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
                  if (!var4.next()) {
                     break label595;
                  }

                  var2 = var4.intValue(0);
                  var5 = var4.byteBufferValue(1);
               } catch (Exception var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label593;
               }

               if (var5 != null) {
                  try {
                     var6 = var5.limit();
                  } catch (Exception var62) {
                     var10000 = var62;
                     var10001 = false;
                     break label593;
                  }

                  var1 = 0;

                  while(true) {
                     try {
                        if (var1 >= var6 / 4) {
                           break;
                        }

                        var3.requery();
                        var3.bindInteger(1, var5.readInt32(false));
                        var3.bindInteger(2, var2);
                        var3.step();
                     } catch (Exception var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label593;
                     }

                     ++var1;
                  }

                  try {
                     var5.reuse();
                  } catch (Exception var61) {
                     var10000 = var61;
                     var10001 = false;
                     break label593;
                  }
               }
            }

            try {
               var3.dispose();
               var4.dispose();
               this.database.commitTransaction();
               this.database.executeFast("DROP INDEX IF EXISTS date_idx_enc_tasks;").stepThis().dispose();
               this.database.executeFast("DROP TABLE IF EXISTS enc_tasks;").stepThis().dispose();
               this.database.executeFast("ALTER TABLE messages ADD COLUMN media INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 6").stepThis().dispose();
            } catch (Exception var60) {
               var10000 = var60;
               var10001 = false;
               break label593;
            }

            var1 = 6;
         }

         var2 = var1;
         if (var1 == 6) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS messages_seq(mid INTEGER PRIMARY KEY, seq_in INTEGER, seq_out INTEGER);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS seq_idx_messages_seq ON messages_seq(seq_in, seq_out);").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN layer INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN seq_in INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN seq_out INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 7").stepThis().dispose();
            } catch (Exception var59) {
               var10000 = var59;
               var10001 = false;
               break label593;
            }

            var2 = 7;
         }

         label596: {
            if (var2 != 7 && var2 != 8) {
               var1 = var2;
               if (var2 != 9) {
                  break label596;
               }
            }

            try {
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN use_count INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN exchange_id INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN key_date INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN fprint INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN fauthkey BLOB default NULL").stepThis().dispose();
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN khash BLOB default NULL").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 10").stepThis().dispose();
            } catch (Exception var58) {
               var10000 = var58;
               var10001 = false;
               break label593;
            }

            var1 = 10;
         }

         var2 = var1;
         if (var1 == 10) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS web_recent_v3(id TEXT, type INTEGER, image_url TEXT, thumb_url TEXT, local_url TEXT, width INTEGER, height INTEGER, size INTEGER, date INTEGER, PRIMARY KEY (id, type));").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 11").stepThis().dispose();
            } catch (Exception var57) {
               var10000 = var57;
               var10001 = false;
               break label593;
            }

            var2 = 11;
         }

         label597: {
            if (var2 != 11) {
               var1 = var2;
               if (var2 != 12) {
                  break label597;
               }
            }

            try {
               this.database.executeFast("DROP INDEX IF EXISTS uid_mid_idx_media;").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS mid_idx_media;").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS uid_date_mid_idx_media;").stepThis().dispose();
               this.database.executeFast("DROP TABLE IF EXISTS media;").stepThis().dispose();
               this.database.executeFast("DROP TABLE IF EXISTS media_counts;").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS media_v2(mid INTEGER PRIMARY KEY, uid INTEGER, date INTEGER, type INTEGER, data BLOB)").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS media_counts_v2(uid INTEGER, type INTEGER, count INTEGER, PRIMARY KEY(uid, type))").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_type_date_idx_media ON media_v2(uid, mid, type, date);").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS keyvalue(id TEXT PRIMARY KEY, value TEXT)").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 13").stepThis().dispose();
            } catch (Exception var56) {
               var10000 = var56;
               var10001 = false;
               break label593;
            }

            var1 = 13;
         }

         var6 = var1;
         if (var1 == 13) {
            try {
               this.database.executeFast("ALTER TABLE messages ADD COLUMN replydata BLOB default NULL").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 14").stepThis().dispose();
            } catch (Exception var55) {
               var10000 = var55;
               var10001 = false;
               break label593;
            }

            var6 = 14;
         }

         var2 = var6;
         if (var6 == 14) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS hashtag_recent_v2(id TEXT PRIMARY KEY, date INTEGER);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 15").stepThis().dispose();
            } catch (Exception var54) {
               var10000 = var54;
               var10001 = false;
               break label593;
            }

            var2 = 15;
         }

         var1 = var2;
         if (var2 == 15) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS webpage_pending(id INTEGER, mid INTEGER, PRIMARY KEY (id, mid));").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 16").stepThis().dispose();
            } catch (Exception var53) {
               var10000 = var53;
               var10001 = false;
               break label593;
            }

            var1 = 16;
         }

         var2 = var1;
         if (var1 == 16) {
            try {
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN inbox_max INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN outbox_max INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 17").stepThis().dispose();
            } catch (Exception var52) {
               var10000 = var52;
               var10001 = false;
               break label593;
            }

            var2 = 17;
         }

         var1 = var2;
         if (var2 == 17) {
            try {
               this.database.executeFast("CREATE TABLE bot_info(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 18").stepThis().dispose();
            } catch (Exception var51) {
               var10000 = var51;
               var10001 = false;
               break label593;
            }

            var1 = 18;
         }

         var2 = var1;
         if (var1 == 18) {
            try {
               this.database.executeFast("DROP TABLE IF EXISTS stickers;").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS stickers_v2(id INTEGER PRIMARY KEY, data BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 19").stepThis().dispose();
            } catch (Exception var50) {
               var10000 = var50;
               var10001 = false;
               break label593;
            }

            var2 = 19;
         }

         var1 = var2;
         if (var2 == 19) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS bot_keyboard(uid INTEGER PRIMARY KEY, mid INTEGER, info BLOB)").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS bot_keyboard_idx_mid ON bot_keyboard(mid);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 20").stepThis().dispose();
            } catch (Exception var49) {
               var10000 = var49;
               var10001 = false;
               break label593;
            }

            var1 = 20;
         }

         var2 = var1;
         if (var1 == 20) {
            try {
               this.database.executeFast("CREATE TABLE search_recent(did INTEGER PRIMARY KEY, date INTEGER);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 21").stepThis().dispose();
            } catch (Exception var48) {
               var10000 = var48;
               var10001 = false;
               break label593;
            }

            var2 = 21;
         }

         var1 = var2;
         if (var2 == 21) {
            SQLiteCursor var69;
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_settings_v2(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
               var69 = this.database.queryFinalized("SELECT uid, participants FROM chat_settings WHERE uid < 0");
               var3 = this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?)");
            } catch (Exception var46) {
               var10000 = var46;
               var10001 = false;
               break label593;
            }

            while(true) {
               NativeByteBuffer var7;
               try {
                  if (!var69.next()) {
                     break;
                  }

                  var1 = var69.intValue(0);
                  var7 = var69.byteBufferValue(1);
               } catch (Exception var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label593;
               }

               if (var7 != null) {
                  TLRPC.ChatParticipants var67;
                  try {
                     var67 = TLRPC.ChatParticipants.TLdeserialize(var7, var7.readInt32(false), false);
                     var7.reuse();
                  } catch (Exception var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label593;
                  }

                  if (var67 != null) {
                     try {
                        TLRPC.TL_chatFull var70 = new TLRPC.TL_chatFull();
                        var70.id = var1;
                        TLRPC.TL_photoEmpty var8 = new TLRPC.TL_photoEmpty();
                        var70.chat_photo = var8;
                        TLRPC.TL_peerNotifySettingsEmpty_layer77 var71 = new TLRPC.TL_peerNotifySettingsEmpty_layer77();
                        var70.notify_settings = var71;
                        TLRPC.TL_chatInviteEmpty var72 = new TLRPC.TL_chatInviteEmpty();
                        var70.exported_invite = var72;
                        var70.participants = var67;
                        NativeByteBuffer var68 = new NativeByteBuffer(var70.getObjectSize());
                        var70.serializeToStream(var68);
                        var3.requery();
                        var3.bindInteger(1, var1);
                        var3.bindByteBuffer(2, (NativeByteBuffer)var68);
                        var3.step();
                        var68.reuse();
                     } catch (Exception var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label593;
                     }
                  }
               }
            }

            try {
               var3.dispose();
               var69.dispose();
               this.database.executeFast("DROP TABLE IF EXISTS chat_settings;").stepThis().dispose();
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN last_mid_i INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN unread_count_i INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN pts INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN date_i INTEGER default 0").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_i_idx_dialogs ON dialogs(last_mid_i);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_i_idx_dialogs ON dialogs(unread_count_i);").stepThis().dispose();
               this.database.executeFast("ALTER TABLE messages ADD COLUMN imp INTEGER default 0").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS messages_holes(uid INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, start));").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_messages_holes ON messages_holes(uid, end);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 22").stepThis().dispose();
            } catch (Exception var43) {
               var10000 = var43;
               var10001 = false;
               break label593;
            }

            var1 = 22;
         }

         var6 = var1;
         if (var1 == 22) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS media_holes_v2(uid INTEGER, type INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, type, start));").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_media_holes_v2 ON media_holes_v2(uid, type, end);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 23").stepThis().dispose();
            } catch (Exception var42) {
               var10000 = var42;
               var10001 = false;
               break label593;
            }

            var6 = 23;
         }

         label599: {
            if (var6 != 23) {
               var2 = var6;
               if (var6 != 24) {
                  break label599;
               }
            }

            try {
               this.database.executeFast("DELETE FROM media_holes_v2 WHERE uid != 0 AND type >= 0 AND start IN (0, 1)").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 25").stepThis().dispose();
            } catch (Exception var41) {
               var10000 = var41;
               var10001 = false;
               break label593;
            }

            var2 = 25;
         }

         label600: {
            if (var2 != 25) {
               var1 = var2;
               if (var2 != 26) {
                  break label600;
               }
            }

            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS channel_users_v2(did INTEGER, uid INTEGER, date INTEGER, data BLOB, PRIMARY KEY(did, uid))").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 27").stepThis().dispose();
            } catch (Exception var40) {
               var10000 = var40;
               var10001 = false;
               break label593;
            }

            var1 = 27;
         }

         var2 = var1;
         if (var1 == 27) {
            try {
               this.database.executeFast("ALTER TABLE web_recent_v3 ADD COLUMN document BLOB default NULL").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 28").stepThis().dispose();
            } catch (Exception var39) {
               var10000 = var39;
               var10001 = false;
               break label593;
            }

            var2 = 28;
         }

         label601: {
            if (var2 != 28) {
               var6 = var2;
               if (var2 != 29) {
                  break label601;
               }
            }

            try {
               this.database.executeFast("DELETE FROM sent_files_v2 WHERE 1").stepThis().dispose();
               this.database.executeFast("DELETE FROM download_queue WHERE 1").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 30").stepThis().dispose();
            } catch (Exception var38) {
               var10000 = var38;
               var10001 = false;
               break label593;
            }

            var6 = 30;
         }

         var1 = var6;
         if (var6 == 30) {
            try {
               this.database.executeFast("ALTER TABLE chat_settings_v2 ADD COLUMN pinned INTEGER default 0").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_settings_pinned_idx ON chat_settings_v2(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_pinned(uid INTEGER PRIMARY KEY, pinned INTEGER, data BLOB)").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_pinned_mid_idx ON chat_pinned(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS users_data(uid INTEGER PRIMARY KEY, about TEXT)").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 31").stepThis().dispose();
            } catch (Exception var37) {
               var10000 = var37;
               var10001 = false;
               break label593;
            }

            var1 = 31;
         }

         var6 = var1;
         if (var1 == 31) {
            try {
               this.database.executeFast("DROP TABLE IF EXISTS bot_recent;").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_hints(did INTEGER, type INTEGER, rating REAL, date INTEGER, PRIMARY KEY(did, type))").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_hints_rating_idx ON chat_hints(rating);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 32").stepThis().dispose();
            } catch (Exception var36) {
               var10000 = var36;
               var10001 = false;
               break label593;
            }

            var6 = 32;
         }

         var2 = var6;
         if (var6 == 32) {
            try {
               this.database.executeFast("DROP INDEX IF EXISTS uid_mid_idx_imp_messages;").stepThis().dispose();
               this.database.executeFast("DROP INDEX IF EXISTS uid_date_mid_imp_idx_messages;").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 33").stepThis().dispose();
            } catch (Exception var35) {
               var10000 = var35;
               var10001 = false;
               break label593;
            }

            var2 = 33;
         }

         var1 = var2;
         if (var2 == 33) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS pending_tasks(id INTEGER PRIMARY KEY, data BLOB);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 34").stepThis().dispose();
            } catch (Exception var34) {
               var10000 = var34;
               var10001 = false;
               break label593;
            }

            var1 = 34;
         }

         var2 = var1;
         if (var1 == 34) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS stickers_featured(id INTEGER PRIMARY KEY, data BLOB, unread BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 35").stepThis().dispose();
            } catch (Exception var33) {
               var10000 = var33;
               var10001 = false;
               break label593;
            }

            var2 = 35;
         }

         var1 = var2;
         if (var2 == 35) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS requested_holes(uid INTEGER, seq_out_start INTEGER, seq_out_end INTEGER, PRIMARY KEY (uid, seq_out_start, seq_out_end));").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 36").stepThis().dispose();
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
               break label593;
            }

            var1 = 36;
         }

         var2 = var1;
         if (var1 == 36) {
            try {
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN in_seq_no INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 37").stepThis().dispose();
            } catch (Exception var31) {
               var10000 = var31;
               var10001 = false;
               break label593;
            }

            var2 = 37;
         }

         var1 = var2;
         if (var2 == 37) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS botcache(id TEXT PRIMARY KEY, date INTEGER, data BLOB)").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS botcache_date_idx ON botcache(date);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 38").stepThis().dispose();
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label593;
            }

            var1 = 38;
         }

         var2 = var1;
         if (var1 == 38) {
            try {
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN pinned INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 39").stepThis().dispose();
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label593;
            }

            var2 = 39;
         }

         var1 = var2;
         if (var2 == 39) {
            try {
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN admin_id INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 40").stepThis().dispose();
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label593;
            }

            var1 = 40;
         }

         var2 = var1;
         if (var1 == 40) {
            try {
               this.fixNotificationSettings();
               this.database.executeFast("PRAGMA user_version = 41").stepThis().dispose();
            } catch (Exception var27) {
               var10000 = var27;
               var10001 = false;
               break label593;
            }

            var2 = 41;
         }

         var1 = var2;
         if (var2 == 41) {
            try {
               this.database.executeFast("ALTER TABLE messages ADD COLUMN mention INTEGER default 0").stepThis().dispose();
               this.database.executeFast("ALTER TABLE user_contacts_v6 ADD COLUMN imported INTEGER default 0").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mention_idx_messages ON messages(uid, mention, read_state);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 42").stepThis().dispose();
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label593;
            }

            var1 = 42;
         }

         var2 = var1;
         if (var1 == 42) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS sharing_locations(uid INTEGER PRIMARY KEY, mid INTEGER, date INTEGER, period INTEGER, message BLOB);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 43").stepThis().dispose();
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label593;
            }

            var2 = 43;
         }

         var6 = var2;
         if (var2 == 43) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS channel_admins(did INTEGER, uid INTEGER, PRIMARY KEY(did, uid))").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 44").stepThis().dispose();
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label593;
            }

            var6 = 44;
         }

         var1 = var6;
         if (var6 == 44) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_contacts_v7(key TEXT PRIMARY KEY, uid INTEGER, fname TEXT, sname TEXT, imported INTEGER)").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_phones_v7(key TEXT, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (key, phone))").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v7(sphone, deleted);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 45").stepThis().dispose();
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label593;
            }

            var1 = 45;
         }

         var2 = var1;
         if (var1 == 45) {
            try {
               this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN mtproto_seq INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 46").stepThis().dispose();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label593;
            }

            var2 = 46;
         }

         var1 = var2;
         if (var2 == 46) {
            try {
               this.database.executeFast("DELETE FROM botcache WHERE 1").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 47").stepThis().dispose();
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label593;
            }

            var1 = 47;
         }

         var2 = var1;
         if (var1 == 47) {
            try {
               this.database.executeFast("ALTER TABLE dialogs ADD COLUMN flags INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 48").stepThis().dispose();
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label593;
            }

            var2 = 48;
         }

         var1 = var2;
         if (var2 == 48) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS unread_push_messages(uid INTEGER, mid INTEGER, random INTEGER, date INTEGER, data BLOB, fm TEXT, name TEXT, uname TEXT, flags INTEGER, PRIMARY KEY(uid, mid))").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_push_messages_idx_date ON unread_push_messages(date);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_push_messages_idx_random ON unread_push_messages(random);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 49").stepThis().dispose();
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label593;
            }

            var1 = 49;
         }

         var2 = var1;
         if (var1 == 49) {
            try {
               this.database.executeFast("DELETE FROM chat_pinned WHERE uid = 1").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS user_settings(uid INTEGER PRIMARY KEY, info BLOB, pinned INTEGER)").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS user_settings_pinned_idx ON user_settings(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 50").stepThis().dispose();
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label593;
            }

            var2 = 50;
         }

         var6 = var2;
         if (var2 == 50) {
            try {
               this.database.executeFast("DELETE FROM sent_files_v2 WHERE 1").stepThis().dispose();
               this.database.executeFast("ALTER TABLE sent_files_v2 ADD COLUMN parent TEXT").stepThis().dispose();
               this.database.executeFast("DELETE FROM download_queue WHERE 1").stepThis().dispose();
               this.database.executeFast("ALTER TABLE download_queue ADD COLUMN parent TEXT").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 51").stepThis().dispose();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label593;
            }

            var6 = 51;
         }

         var1 = var6;
         if (var6 == 51) {
            try {
               this.database.executeFast("ALTER TABLE media_counts_v2 ADD COLUMN old INTEGER").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 52").stepThis().dispose();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label593;
            }

            var1 = 52;
         }

         var6 = var1;
         if (var1 == 52) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS polls(mid INTEGER PRIMARY KEY, id INTEGER);").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS polls_id ON polls(id);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 53").stepThis().dispose();
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label593;
            }

            var6 = 53;
         }

         var2 = var6;
         if (var6 == 53) {
            try {
               this.database.executeFast("ALTER TABLE chat_settings_v2 ADD COLUMN online INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 54").stepThis().dispose();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label593;
            }

            var2 = 54;
         }

         var1 = var2;
         if (var2 == 54) {
            try {
               this.database.executeFast("DROP TABLE IF EXISTS wallpapers;").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 55").stepThis().dispose();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label593;
            }

            var1 = 55;
         }

         var2 = var1;
         if (var1 == 55) {
            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS wallpapers2(uid INTEGER PRIMARY KEY, data BLOB, num INTEGER)").stepThis().dispose();
               this.database.executeFast("CREATE INDEX IF NOT EXISTS wallpapers_num ON wallpapers2(num);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 56").stepThis().dispose();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label593;
            }

            var2 = 56;
         }

         label602: {
            if (var2 != 56) {
               var1 = var2;
               if (var2 != 57) {
                  break label602;
               }
            }

            try {
               this.database.executeFast("CREATE TABLE IF NOT EXISTS emoji_keywords_v2(lang TEXT, keyword TEXT, emoji TEXT, PRIMARY KEY(lang, keyword, emoji));").stepThis().dispose();
               this.database.executeFast("CREATE TABLE IF NOT EXISTS emoji_keywords_info_v2(lang TEXT PRIMARY KEY, alias TEXT, version INTEGER);").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 58").stepThis().dispose();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label593;
            }

            var1 = 58;
         }

         var2 = var1;
         if (var1 == 58) {
            try {
               this.database.executeFast("CREATE INDEX IF NOT EXISTS emoji_keywords_v2_keyword ON emoji_keywords_v2(keyword);").stepThis().dispose();
               this.database.executeFast("ALTER TABLE emoji_keywords_info_v2 ADD COLUMN date INTEGER default 0").stepThis().dispose();
               this.database.executeFast("PRAGMA user_version = 59").stepThis().dispose();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label593;
            }

            var2 = 59;
         }

         if (var2 != 59) {
            return;
         }

         try {
            this.database.executeFast("ALTER TABLE dialogs ADD COLUMN folder_id INTEGER default 0").stepThis().dispose();
            this.database.executeFast("ALTER TABLE dialogs ADD COLUMN data BLOB default NULL").stepThis().dispose();
            this.database.executeFast("CREATE INDEX IF NOT EXISTS folder_id_idx_dialogs ON dialogs(folder_id);").stepThis().dispose();
            this.database.executeFast("PRAGMA user_version = 60").stepThis().dispose();
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var66 = var10000;
      FileLog.e((Throwable)var66);
   }

   // $FF: synthetic method
   public void lambda$updateDialogsWithDeletedMessages$133$MessagesStorage(ArrayList var1, ArrayList var2, int var3) {
      this.updateDialogsWithDeletedMessagesInternal(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$updateDialogsWithReadMessages$65$MessagesStorage(SparseLongArray var1, SparseLongArray var2, ArrayList var3) {
      this.updateDialogsWithReadMessagesInternal((ArrayList)null, var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$updateEncryptedChat$107$MessagesStorage(TLRPC.EncryptedChat param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$updateEncryptedChatLayer$106$MessagesStorage(TLRPC.EncryptedChat param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$updateEncryptedChatSeq$104$MessagesStorage(TLRPC.EncryptedChat param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$updateEncryptedChatTTL$105$MessagesStorage(TLRPC.EncryptedChat param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$updateMessagePollResults$56$MessagesStorage(long var1, TLRPC.TL_poll var3, TLRPC.TL_pollResults var4) {
      ArrayList var5 = null;

      Exception var10000;
      label125: {
         SQLiteCursor var6;
         boolean var10001;
         try {
            var6 = this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM polls WHERE id = %d", var1));
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break label125;
         }

         while(true) {
            try {
               if (!var6.next()) {
                  break;
               }
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label125;
            }

            ArrayList var7 = var5;
            if (var5 == null) {
               try {
                  var7 = new ArrayList();
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label125;
               }
            }

            try {
               var7.add(var6.longValue(0));
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label125;
            }

            var5 = var7;
         }

         try {
            var6.dispose();
         } catch (Exception var22) {
            var10000 = var22;
            var10001 = false;
            break label125;
         }

         if (var5 == null) {
            return;
         }

         int var8;
         try {
            this.database.beginTransaction();
            var8 = var5.size();
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break label125;
         }

         int var9 = 0;

         while(true) {
            if (var9 >= var8) {
               try {
                  this.database.commitTransaction();
                  return;
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }
            }

            SQLiteCursor var29;
            label128: {
               Long var10;
               NativeByteBuffer var11;
               label93: {
                  try {
                     var10 = (Long)var5.get(var9);
                     var29 = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM messages WHERE mid = %d", var10));
                     if (var29.next()) {
                        var11 = var29.byteBufferValue(0);
                        break label93;
                     }
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break;
                  }

                  try {
                     this.database.executeFast(String.format(Locale.US, "DELETE FROM polls WHERE mid = %d", var10)).stepThis().dispose();
                     break label128;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break;
                  }
               }

               if (var11 != null) {
                  label129: {
                     TLRPC.Message var28;
                     TLRPC.TL_messageMediaPoll var30;
                     try {
                        var28 = TLRPC.Message.TLdeserialize(var11, var11.readInt32(false), false);
                        var28.readAttachPath(var11, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var11.reuse();
                        if (!(var28.media instanceof TLRPC.TL_messageMediaPoll)) {
                           break label129;
                        }

                        var30 = (TLRPC.TL_messageMediaPoll)var28.media;
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break;
                     }

                     if (var3 != null) {
                        try {
                           var30.poll = var3;
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break;
                        }
                     }

                     if (var4 != null) {
                        try {
                           MessageObject.updatePollResults(var30, var4);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break;
                        }
                     }

                     try {
                        SQLitePreparedStatement var31 = this.database.executeFast("UPDATE messages SET data = ? WHERE mid = ?");
                        NativeByteBuffer var12 = new NativeByteBuffer(var28.getObjectSize());
                        var28.serializeToStream(var12);
                        var31.requery();
                        var31.bindByteBuffer(1, (NativeByteBuffer)var12);
                        var31.bindLong(2, var10);
                        var31.step();
                        var12.reuse();
                        var31.dispose();
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break;
                     }
                  }
               }
            }

            try {
               var29.dispose();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }

            ++var9;
         }
      }

      Exception var27 = var10000;
      FileLog.e((Throwable)var27);
   }

   // $FF: synthetic method
   public void lambda$updateMessageStateAndId$127$MessagesStorage(long var1, Integer var3, int var4, int var5, int var6) {
      this.updateMessageStateAndIdInternal(var1, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   public void lambda$updateUserInfo$74$MessagesStorage(boolean var1, TLRPC.UserFull var2) {
      Exception var10000;
      label25: {
         boolean var10001;
         if (var1) {
            try {
               SQLiteDatabase var3 = this.database;
               StringBuilder var4 = new StringBuilder();
               var4.append("SELECT uid FROM user_settings WHERE uid = ");
               var4.append(var2.user.id);
               SQLiteCursor var8 = var3.queryFinalized(var4.toString());
               var1 = var8.next();
               var8.dispose();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label25;
            }

            if (!var1) {
               return;
            }
         }

         try {
            SQLitePreparedStatement var9 = this.database.executeFast("REPLACE INTO user_settings VALUES(?, ?, ?)");
            NativeByteBuffer var10 = new NativeByteBuffer(var2.getObjectSize());
            var2.serializeToStream(var10);
            var9.bindInteger(1, var2.user.id);
            var9.bindByteBuffer(2, (NativeByteBuffer)var10);
            var9.bindInteger(3, var2.pinned_msg_id);
            var9.step();
            var9.dispose();
            var10.reuse();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var7 = var10000;
      FileLog.e((Throwable)var7);
   }

   // $FF: synthetic method
   public void lambda$updateUserPinnedMessage$77$MessagesStorage(int var1, int var2) {
      Exception var10000;
      label43: {
         SQLiteDatabase var3;
         SQLiteCursor var5;
         boolean var10001;
         try {
            var3 = this.database;
            StringBuilder var4 = new StringBuilder();
            var4.append("SELECT info, pinned FROM user_settings WHERE uid = ");
            var4.append(var1);
            var5 = var3.queryFinalized(var4.toString());
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label43;
         }

         var3 = null;
         TLRPC.UserFull var13 = var3;

         label35: {
            NativeByteBuffer var6;
            try {
               if (!var5.next()) {
                  break label35;
               }

               var6 = var5.byteBufferValue(0);
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label43;
            }

            var13 = var3;
            if (var6 != null) {
               try {
                  var13 = TLRPC.UserFull.TLdeserialize(var6, var6.readInt32(false), false);
                  var6.reuse();
                  var13.pinned_msg_id = var5.intValue(1);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label43;
               }
            }
         }

         try {
            var5.dispose();
            if (var13 instanceof TLRPC.UserFull) {
               var13.pinned_msg_id = var2;
               var13.flags |= 64;
               _$$Lambda$MessagesStorage$6oEVGdM2xXW3N6wtIglBSuDFLWE var11 = new _$$Lambda$MessagesStorage$6oEVGdM2xXW3N6wtIglBSuDFLWE(this, var1, var13);
               AndroidUtilities.runOnUIThread(var11);
               SQLitePreparedStatement var15 = this.database.executeFast("REPLACE INTO user_settings VALUES(?, ?, ?)");
               NativeByteBuffer var12 = new NativeByteBuffer(var13.getObjectSize());
               var13.serializeToStream(var12);
               var15.bindInteger(1, var1);
               var15.bindByteBuffer(2, (NativeByteBuffer)var12);
               var15.bindInteger(3, var13.pinned_msg_id);
               var15.step();
               var15.dispose();
               var12.reuse();
            }

            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public void lambda$updateUsers$128$MessagesStorage(ArrayList var1, boolean var2, boolean var3) {
      this.updateUsersInternal(var1, var2, var3);
   }

   public void loadChannelAdmins(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Vnf_RLnltCEPaLdbkpbktxt0AFA(this, var1));
   }

   public void loadChatInfo(int var1, CountDownLatch var2, boolean var3, boolean var4) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VOppcN16yRL668WP3aQYIlU1CXM(this, var1, var2, var3, var4));
   }

   public void loadUnreadMessages() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$joRU6ZuCNEib3vg1K0JvduLpCMk(this));
   }

   public void loadUserInfo(TLRPC.User var1, boolean var2, int var3) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg(this, var1, var2, var3));
      }
   }

   public void loadWebRecent(int var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IRnMGvkA4nj7Im0gunHCNPyjjd0(this, var1));
   }

   public void markMentionMessageAsRead(int var1, int var2, long var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY(this, var1, var2, var3));
   }

   public void markMessageAsMention(long var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$G2AR1YES2RX33l0g8yjj7MWOmwU(this, var1));
   }

   public void markMessageAsSendError(TLRPC.Message var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE_KUXI(this, var1));
   }

   public ArrayList markMessagesAsDeleted(int var1, int var2, boolean var3) {
      if (var3) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$G8wlamTxGBm2te_IIW2_2dxQkB8(this, var1, var2));
         return null;
      } else {
         return this.markMessagesAsDeletedInternal(var1, var2);
      }
   }

   public ArrayList markMessagesAsDeleted(ArrayList var1, boolean var2, int var3) {
      if (var1.isEmpty()) {
         return null;
      } else if (var2) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$CwczV_sujyFOSEA_5BEgE4G3Gcw(this, var1, var3));
         return null;
      } else {
         return this.markMessagesAsDeletedInternal(var1, var3);
      }
   }

   public void markMessagesAsDeletedByRandoms(ArrayList var1) {
      if (!var1.isEmpty()) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5tLDgdMiD0B9gd7akTqO_XaBigI(this, var1));
      }
   }

   public void markMessagesAsRead(SparseLongArray var1, SparseLongArray var2, SparseIntArray var3, boolean var4) {
      if (var4) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7M1fsQyOLqfQ09O_qH6xF5Wf9zk(this, var1, var2, var3));
      } else {
         this.markMessagesAsReadInternal(var1, var2, var3);
      }

   }

   public void markMessagesContentAsRead(ArrayList var1, int var2) {
      if (!isEmpty((List)var1)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Kd_7KIl84Ykan0Xx9_cq5K_0a4k(this, var1, var2));
      }
   }

   public void onDeleteQueryComplete(long var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9qnT2TxRdf9DxlCuMWJHZarRSno(this, var1));
   }

   public void openDatabase(int var1) {
      File var2 = ApplicationLoader.getFilesDirFixed();
      File var3 = var2;
      StringBuilder var20;
      if (this.currentAccount != 0) {
         var20 = new StringBuilder();
         var20.append("account");
         var20.append(this.currentAccount);
         var20.append("/");
         var3 = new File(var2, var20.toString());
         var3.mkdirs();
      }

      this.cacheFile = new File(var3, "cache4.db");
      this.walCacheFile = new File(var3, "cache4.db-wal");
      this.shmCacheFile = new File(var3, "cache4.db-shm");
      boolean var4 = this.cacheFile.exists();
      byte var5 = 3;

      label144: {
         int var6;
         Exception var10000;
         Exception var23;
         label143: {
            boolean var10001;
            try {
               SQLiteDatabase var21 = new SQLiteDatabase(this.cacheFile.getPath());
               this.database = var21;
               this.database.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
               this.database.executeFast("PRAGMA temp_store = 1").stepThis().dispose();
               this.database.executeFast("PRAGMA journal_mode = WAL").stepThis().dispose();
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label143;
            }

            if (var4 ^ true) {
               label107: {
                  try {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("create new database");
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label107;
                  }

                  try {
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
                     break label144;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                  }
               }
            } else {
               label139: {
                  try {
                     var6 = this.database.executeInt("PRAGMA user_version");
                     if (BuildVars.LOGS_ENABLED) {
                        var20 = new StringBuilder();
                        var20.append("current db version = ");
                        var20.append(var6);
                        FileLog.d(var20.toString());
                     }
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label139;
                  }

                  if (var6 != 0) {
                     label150: {
                        label151: {
                           label155: {
                              SQLiteCursor var22;
                              label131: {
                                 try {
                                    var22 = this.database.queryFinalized("SELECT seq, pts, date, qts, lsv, sg, pbytes FROM params WHERE id = 1");
                                    if (!var22.next()) {
                                       break label131;
                                    }

                                    this.lastSeqValue = var22.intValue(0);
                                    this.lastPtsValue = var22.intValue(1);
                                    this.lastDateValue = var22.intValue(2);
                                    this.lastQtsValue = var22.intValue(3);
                                    this.lastSecretVersion = var22.intValue(4);
                                    this.secretG = var22.intValue(5);
                                    if (var22.isNull(6)) {
                                       this.secretPBytes = null;
                                       break label131;
                                    }
                                 } catch (Exception var17) {
                                    var10000 = var17;
                                    var10001 = false;
                                    break label155;
                                 }

                                 try {
                                    this.secretPBytes = var22.byteArrayValue(6);
                                    if (this.secretPBytes != null && this.secretPBytes.length == 1) {
                                       this.secretPBytes = null;
                                    }
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break label155;
                                 }
                              }

                              try {
                                 var22.dispose();
                                 break label151;
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                              }
                           }

                           var23 = var10000;

                           try {
                              FileLog.e((Throwable)var23);
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label150;
                           }

                           try {
                              this.database.executeFast("CREATE TABLE IF NOT EXISTS params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                              this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                           } catch (Exception var13) {
                              var23 = var13;

                              try {
                                 FileLog.e((Throwable)var23);
                              } catch (Exception var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break label150;
                              }
                           }
                        }

                        if (var6 >= 60) {
                           break label144;
                        }

                        try {
                           this.updateDbToLastVersion(var6);
                           break label144;
                        } catch (Exception var11) {
                           var10000 = var11;
                           var10001 = false;
                        }
                     }
                  } else {
                     try {
                        var23 = new Exception("malformed");
                        throw var23;
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }
               }
            }
         }

         var23 = var10000;
         FileLog.e((Throwable)var23);
         if (var1 < 3 && var23.getMessage().contains("malformed")) {
            if (var1 != 2) {
               this.cleanupInternal(false);
            } else {
               this.cleanupInternal(true);

               for(var6 = 0; var6 < 2; ++var6) {
                  UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var6, 0, 0, 0, 0, 0, 0L);
                  UserConfig.getInstance(this.currentAccount).setTotalDialogsCount(var6, 0);
               }

               UserConfig.getInstance(this.currentAccount).saveConfig(false);
            }

            byte var24 = var5;
            if (var1 == 1) {
               var24 = 2;
            }

            this.openDatabase(var24);
         }
      }

      this.loadUnreadMessages();
      this.loadPendingTasks();

      try {
         this.openSync.countDown();
      } catch (Throwable var7) {
      }

   }

   public void overwriteChannel(int var1, TLRPC.TL_updates_channelDifferenceTooLong var2, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s_yY(this, var1, var3, var2));
   }

   public void processPendingRead(long var1, long var3, long var5, int var7, boolean var8) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$BFuJtbbUa5cf4B4J2ETafzpLwTU(this, var1, var3, var8, var5));
   }

   public void putBlockedUsers(SparseIntArray var1, boolean var2) {
      if (var1 != null && var1.size() != 0) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$a_e0LwnKknlA9E0BfJ3sY5J_dD0(this, var2, var1));
      }

   }

   public void putCachedPhoneBook(HashMap var1, boolean var2, boolean var3) {
      if (var1 != null && (!var1.isEmpty() || var2 || var3)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh_A2PiSqHA(this, var1, var2));
      }

   }

   public void putChannelAdmins(int var1, ArrayList var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$mAd0DJIW27tlUDIOzW0wIDeqyuE(this, var1, var2));
   }

   public void putChannelViews(SparseArray var1, boolean var2) {
      if (!isEmpty(var1)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc(this, var1, var2));
      }
   }

   public void putContacts(ArrayList var1, boolean var2) {
      if (!var1.isEmpty() || var2) {
         var1 = new ArrayList(var1);
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810(this, var2, var1));
      }
   }

   public void putDialogPhotos(int var1, TLRPC.photos_Photos var2) {
      if (var2 != null && !var2.photos.isEmpty()) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$L4Om6tbm2x7Gby_R4CSBHE04Yg4(this, var1, var2));
      }

   }

   public void putDialogs(TLRPC.messages_Dialogs var1, int var2) {
      if (!var1.dialogs.isEmpty()) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$hry9IvsaQDnemeIROZgTEzWio5I(this, var1, var2));
      }
   }

   public void putEncryptedChat(TLRPC.EncryptedChat var1, TLRPC.User var2, TLRPC.Dialog var3) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$wWMmlnBYX7ztuC_F9UPquAEbR_s(this, var1, var2, var3));
      }
   }

   public void putMessages(ArrayList var1, boolean var2, boolean var3, boolean var4, int var5) {
      this.putMessages(var1, var2, var3, var4, var5, false);
   }

   public void putMessages(ArrayList var1, boolean var2, boolean var3, boolean var4, int var5, boolean var6) {
      if (var1.size() != 0) {
         if (var3) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Zbl8iyH6cJ1Eo71mteogwoPbxxQ(this, var1, var2, var4, var5, var6));
         } else {
            this.putMessagesInternal(var1, var2, var4, var5, var6);
         }

      }
   }

   public void putMessages(TLRPC.messages_Messages var1, long var2, int var4, int var5, boolean var6) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90(this, var1, var4, var2, var5, var6));
   }

   public void putPushMessage(MessageObject var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$lVMMHudh0dN8CKFv0xYu7ddEmi8(this, var1));
   }

   public void putSentFile(String var1, TLObject var2, int var3, String var4) {
      if (var1 != null && var2 != null && var4 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$0ihTGohq3QJed2BJHkvrASib2fM(this, var1, var2, var3, var4));
      }

   }

   public void putUsersAndChats(ArrayList var1, ArrayList var2, boolean var3, boolean var4) {
      if (var1 == null || !var1.isEmpty() || var2 == null || !var2.isEmpty()) {
         if (var4) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$MmeBiywJvVY_laiR4XDpwGlLSH4(this, var1, var2, var3));
         } else {
            this.putUsersAndChatsInternal(var1, var2, var3);
         }

      }
   }

   public void putWallpapers(ArrayList var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9BkX0ldup_qkhjHCNOhyXfgGwXY(this, var2, var1));
   }

   public void putWebPages(LongSparseArray var1) {
      if (!isEmpty(var1)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$fzFvY5cznRt5WEudcaJ9_2Km0iI(this, var1));
      }
   }

   public void putWebRecent(ArrayList var1) {
      if (!var1.isEmpty() && var1.isEmpty()) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$2zgETsycS9dz5I652QRWAYabQH4(this, var1));
      }

   }

   public void readAllDialogs() {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Uv7xGGpYr4xQqc_hYra_gjZD6hY(this));
   }

   public void removeFromDownloadQueue(long var1, int var3, boolean var4) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY(this, var4, var3, var1));
   }

   public void removePendingTask(long var1) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bLsQ9Zez_Ef0tQJtoMR54iM_bHA(this, var1));
   }

   public void replaceMessageIfExists(TLRPC.Message var1, int var2, ArrayList var3, ArrayList var4, boolean var5) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$pND7SeCnHWGSkCmeT8Uq6NOWqtE(this, var1, var5, var3, var4, var2));
      }
   }

   public void resetDialogs(TLRPC.messages_Dialogs var1, int var2, int var3, int var4, int var5, int var6, LongSparseArray var7, LongSparseArray var8, TLRPC.Message var9, int var10) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Rsj_LL8Uz4Bh77DLk8Q7Jiw37h4(this, var1, var10, var3, var4, var5, var6, var9, var2, var7, var8));
   }

   public void resetMentionsCount(long var1, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$QZA58f_xBct34Cb7F3baqareFec(this, var3, var1));
   }

   public void saveBotCache(String var1, TLObject var2) {
      if (var2 != null && !TextUtils.isEmpty(var1)) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$7VZ_XJgDXdzbLiASsmURuo_C9GM(this, var2, var1));
      }

   }

   public void saveChannelPts(int var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$bE1TJhfMynV6_iw8er6RBG5GCDM(this, var2, var1));
   }

   public void saveDiffParams(int var1, int var2, int var3, int var4) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$_bpF9_lI_H0oz_1CAiE5SbDJe_c(this, var1, var2, var3, var4));
   }

   public void saveSecretParams(int var1, int var2, byte[] var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$NtpBYQeXRJpUfal8CNuiE9yNR3Y(this, var1, var2, var3));
   }

   public void setDialogFlags(long var1, long var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$uZgJttXUWPBitEWEPJJQPgaQByA(this, var1, var3));
   }

   public void setDialogPinned(long var1, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$pydpGfyEJY86wZVM_5gk1G5enLU(this, var3, var1));
   }

   public void setDialogUnread(long var1, boolean var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$YR6__nj0nsdVFY6J_MW9CHpRtYs(this, var1, var3));
   }

   public void setDialogsFolderId(ArrayList var1, ArrayList var2, long var3, int var5) {
      if (var1 != null || var2 != null || var3 != 0L) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$vU3H4O_uMIIPLYpg8QUqZEOJanU(this, var1, var2, var5, var3));
      }
   }

   public void setLastDateValue(int var1) {
      this.ensureOpened();
      this.lastDateValue = var1;
   }

   public void setLastPtsValue(int var1) {
      this.ensureOpened();
      this.lastPtsValue = var1;
   }

   public void setLastQtsValue(int var1) {
      this.ensureOpened();
      this.lastQtsValue = var1;
   }

   public void setLastSecretVersion(int var1) {
      this.ensureOpened();
      this.lastSecretVersion = var1;
   }

   public void setLastSeqValue(int var1) {
      this.ensureOpened();
      this.lastSeqValue = var1;
   }

   public void setMessageSeq(int var1, int var2, int var3) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$I_Nkh3lDe_kxZ__U4jy8HVjamUk(this, var1, var2, var3));
   }

   public void setSecretG(int var1) {
      this.ensureOpened();
      this.secretG = var1;
   }

   public void setSecretPBytes(byte[] var1) {
      this.ensureOpened();
      this.secretPBytes = var1;
   }

   public void unpinAllDialogsExceptNew(ArrayList var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$UNG3O_q0_n6IbfxTyxeEDWN4wjc(this, var1, var2));
   }

   public void updateChannelUsers(int var1, ArrayList var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo(this, var1, var2));
   }

   public void updateChatDefaultBannedRights(int var1, TLRPC.TL_chatBannedRights var2, int var3) {
      if (var2 != null && var1 != 0) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9hiJJNQirqjcrHEWZID3qqycfH0(this, var1, var3, var2));
      }

   }

   public void updateChatInfo(int var1, int var2, int var3, int var4, int var5) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KNbNa8I6iljUtQSIcsWsYI2Eqos(this, var1, var3, var2, var4, var5));
   }

   public void updateChatInfo(TLRPC.ChatFull var1, boolean var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc(this, var1, var2));
   }

   public void updateChatOnlineCount(int var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$JoSxg9ZlDc30vVq_9Ro7__Ct4Kk(this, var2, var1));
   }

   public void updateChatParticipants(TLRPC.ChatParticipants var1) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ul28E39n2CqjyGu1NrRVaRUiBf8(this, var1));
      }
   }

   public void updateChatPinnedMessage(int var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$ZKAInF8vAaa7ZvlZcI_h3lCnx34(this, var1, var2));
   }

   public void updateDialogsWithDeletedMessages(ArrayList var1, ArrayList var2, boolean var3, int var4) {
      if (!var1.isEmpty() || var4 != 0) {
         if (var3) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE(this, var1, var2, var4));
         } else {
            this.updateDialogsWithDeletedMessagesInternal(var1, var2, var4);
         }

      }
   }

   public void updateDialogsWithReadMessages(SparseLongArray var1, SparseLongArray var2, ArrayList var3, boolean var4) {
      if (!isEmpty(var1) || !isEmpty((List)var3)) {
         if (var4) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$IgPbgy6_ebqi0So_m2IdijRNQZs(this, var1, var2, var3));
         } else {
            this.updateDialogsWithReadMessagesInternal((ArrayList)null, var1, var2, var3);
         }

      }
   }

   public void updateEncryptedChat(TLRPC.EncryptedChat var1) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM(this, var1));
      }
   }

   public void updateEncryptedChatLayer(TLRPC.EncryptedChat var1) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$8Zp97g2M9CG9f_Df_BLJuu2zQDo(this, var1));
      }
   }

   public void updateEncryptedChatSeq(TLRPC.EncryptedChat var1, boolean var2) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM(this, var1, var2));
      }
   }

   public void updateEncryptedChatTTL(TLRPC.EncryptedChat var1) {
      if (var1 != null) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY(this, var1));
      }
   }

   public void updateMessagePollResults(long var1, TLRPC.TL_poll var3, TLRPC.TL_pollResults var4) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$zk6IBPLI4pOBatA30WuVm_5FFz4(this, var1, var3, var4));
   }

   public long[] updateMessageStateAndId(long var1, Integer var3, int var4, int var5, boolean var6, int var7) {
      if (var6) {
         this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$cHj3_heIm7v3yVK5WTcTNH3ovCc(this, var1, var3, var4, var5, var7));
         return null;
      } else {
         return this.updateMessageStateAndIdInternal(var1, var3, var4, var5, var7);
      }
   }

   public void updateUserInfo(TLRPC.UserFull var1, boolean var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$VXYkBnXA4FUiRp4wxPHJnwfBAPc(this, var2, var1));
   }

   public void updateUserPinnedMessage(int var1, int var2) {
      this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$g1iuRG0K_i5oYkMy9HCpNOf8FqU(this, var1, var2));
   }

   public void updateUsers(ArrayList var1, boolean var2, boolean var3, boolean var4) {
      if (var1 != null && !var1.isEmpty()) {
         if (var4) {
            this.storageQueue.postRunnable(new _$$Lambda$MessagesStorage$KhcIchuE4KJbeKdTaRAB8CWDcM0(this, var1, var2, var3));
         } else {
            this.updateUsersInternal(var1, var2, var3);
         }
      }

   }

   public interface BooleanCallback {
      void run(boolean var1);
   }

   private class Hole {
      public int end;
      public int start;
      public int type;

      public Hole(int var2, int var3) {
         this.start = var2;
         this.end = var3;
      }

      public Hole(int var2, int var3, int var4) {
         this.type = var2;
         this.start = var3;
         this.end = var4;
      }
   }

   public interface IntCallback {
      void run(int var1);
   }

   private class ReadDialog {
      public int date;
      public int lastMid;
      public int unreadCount;

      private ReadDialog() {
      }

      // $FF: synthetic method
      ReadDialog(Object var2) {
         this();
      }
   }
}
