package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ShortcutManager;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.StickersArchiveAlert;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;

public class DataQuery {
   private static volatile DataQuery[] Instance = new DataQuery[3];
   public static final int MEDIA_AUDIO = 2;
   public static final int MEDIA_FILE = 1;
   public static final int MEDIA_MUSIC = 4;
   public static final int MEDIA_PHOTOVIDEO = 0;
   public static final int MEDIA_TYPES_COUNT = 5;
   public static final int MEDIA_URL = 3;
   public static final int TYPE_FAVE = 2;
   public static final int TYPE_FEATURED = 3;
   public static final int TYPE_IMAGE = 0;
   public static final int TYPE_MASK = 1;
   private static RectF bitmapRect;
   private static Comparator entityComparator;
   private static Paint erasePaint;
   private static Paint roundPaint;
   private static Path roundPath;
   private HashMap allStickers = new HashMap();
   private HashMap allStickersFeatured = new HashMap();
   private int[] archivedStickersCount = new int[2];
   private SparseArray botInfos = new SparseArray();
   private LongSparseArray botKeyboards = new LongSparseArray();
   private SparseLongArray botKeyboardsByMids = new SparseLongArray();
   private int currentAccount;
   private HashMap currentFetchingEmoji = new HashMap();
   private LongSparseArray draftMessages = new LongSparseArray();
   private LongSparseArray drafts = new LongSparseArray();
   private ArrayList featuredStickerSets = new ArrayList();
   private LongSparseArray featuredStickerSetsById = new LongSparseArray();
   private boolean featuredStickersLoaded;
   private LongSparseArray groupStickerSets = new LongSparseArray();
   public ArrayList hints = new ArrayList();
   private boolean inTransaction;
   public ArrayList inlineBots = new ArrayList();
   private LongSparseArray installedStickerSetsById = new LongSparseArray();
   private long lastMergeDialogId;
   private int lastReqId;
   private int lastReturnedNum;
   private String lastSearchQuery;
   private int[] loadDate = new int[4];
   private int loadFeaturedDate;
   private int loadFeaturedHash;
   private int[] loadHash = new int[4];
   boolean loaded;
   boolean loading;
   private boolean loadingDrafts;
   private boolean loadingFeaturedStickers;
   private boolean loadingRecentGifs;
   private boolean[] loadingRecentStickers = new boolean[3];
   private boolean[] loadingStickers = new boolean[4];
   private int mergeReqId;
   private int[] messagesSearchCount = new int[]{0, 0};
   private boolean[] messagesSearchEndReached = new boolean[]{(boolean)0, (boolean)0};
   private SharedPreferences preferences;
   private ArrayList readingStickerSets = new ArrayList();
   private ArrayList recentGifs = new ArrayList();
   private boolean recentGifsLoaded;
   private ArrayList[] recentStickers = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList()};
   private boolean[] recentStickersLoaded = new boolean[3];
   private int reqId;
   private ArrayList searchResultMessages = new ArrayList();
   private SparseArray[] searchResultMessagesMap = new SparseArray[]{new SparseArray(), new SparseArray()};
   private ArrayList[] stickerSets = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList(0), new ArrayList()};
   private LongSparseArray stickerSetsById = new LongSparseArray();
   private HashMap stickerSetsByName = new HashMap();
   private LongSparseArray stickersByEmoji = new LongSparseArray();
   private boolean[] stickersLoaded = new boolean[4];
   private ArrayList unreadStickerSets = new ArrayList();

   static {
      entityComparator = _$$Lambda$DataQuery$_GtBS_Mb74mqs3D5Wip5N2Gb424.INSTANCE;
   }

   public DataQuery(int var1) {
      this.currentAccount = var1;
      if (this.currentAccount == 0) {
         this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("drafts", 0);
      } else {
         Context var2 = ApplicationLoader.applicationContext;
         StringBuilder var3 = new StringBuilder();
         var3.append("drafts");
         var3.append(this.currentAccount);
         this.preferences = var2.getSharedPreferences(var3.toString(), 0);
      }

      Iterator var15 = this.preferences.getAll().entrySet().iterator();

      while(var15.hasNext()) {
         Entry var13 = (Entry)var15.next();

         SerializedData var14;
         boolean var10001;
         label56: {
            long var5;
            TLRPC.Message var17;
            label65: {
               try {
                  String var4 = (String)var13.getKey();
                  var5 = Utilities.parseLong(var4);
                  byte[] var7 = Utilities.hexToBytes((String)var13.getValue());
                  var14 = new SerializedData(var7);
                  if (var4.startsWith("r_")) {
                     var17 = TLRPC.Message.TLdeserialize(var14, var14.readInt32(true), true);
                     var17.readAttachPath(var14, UserConfig.getInstance(this.currentAccount).clientUserId);
                     break label65;
                  }
               } catch (Exception var12) {
                  var10001 = false;
                  continue;
               }

               TLRPC.DraftMessage var16;
               try {
                  var16 = TLRPC.DraftMessage.TLdeserialize(var14, var14.readInt32(true), true);
               } catch (Exception var10) {
                  var10001 = false;
                  continue;
               }

               if (var16 != null) {
                  try {
                     this.drafts.put(var5, var16);
                  } catch (Exception var9) {
                     var10001 = false;
                     continue;
                  }
               }
               break label56;
            }

            if (var17 != null) {
               try {
                  this.draftMessages.put(var5, var17);
               } catch (Exception var11) {
                  var10001 = false;
                  continue;
               }
            }
         }

         try {
            var14.cleanup();
         } catch (Exception var8) {
            var10001 = false;
         }
      }

   }

   private MessageObject broadcastPinnedMessage(TLRPC.Message var1, ArrayList var2, ArrayList var3, boolean var4, boolean var5) {
      SparseArray var6 = new SparseArray();
      byte var7 = 0;

      int var8;
      for(var8 = 0; var8 < var2.size(); ++var8) {
         TLRPC.User var9 = (TLRPC.User)var2.get(var8);
         var6.put(var9.id, var9);
      }

      SparseArray var10 = new SparseArray();

      for(var8 = var7; var8 < var3.size(); ++var8) {
         TLRPC.Chat var11 = (TLRPC.Chat)var3.get(var8);
         var10.put(var11.id, var11);
      }

      if (var5) {
         return new MessageObject(this.currentAccount, var1, var6, var10, false);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$Yx2wTX87h9OMWDNW8TJbWVjbdlk(this, var2, var4, var3, var1, var6, var10));
         return null;
      }
   }

   private void broadcastReplyMessages(ArrayList var1, SparseArray var2, ArrayList var3, ArrayList var4, long var5, boolean var7) {
      SparseArray var8 = new SparseArray();
      byte var9 = 0;

      int var10;
      for(var10 = 0; var10 < var3.size(); ++var10) {
         TLRPC.User var11 = (TLRPC.User)var3.get(var10);
         var8.put(var11.id, var11);
      }

      SparseArray var13 = new SparseArray();

      for(var10 = var9; var10 < var4.size(); ++var10) {
         TLRPC.Chat var12 = (TLRPC.Chat)var4.get(var10);
         var13.put(var12.id, var12);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU(this, var3, var7, var4, var1, var2, var8, var13, var5));
   }

   private static int calcDocumentsHash(ArrayList var0) {
      int var1 = 0;
      if (var0 == null) {
         return 0;
      } else {
         long var2;
         for(var2 = 0L; var1 < Math.min(200, var0.size()); ++var1) {
            TLRPC.Document var4 = (TLRPC.Document)var0.get(var1);
            if (var4 != null) {
               long var5 = var4.id;
               int var7 = (int)(var5 >> 32);
               int var8 = (int)var5;
               var2 = ((var2 * 20261L + 2147483648L + (long)var7) % 2147483648L * 20261L + 2147483648L + (long)var8) % 2147483648L;
            }
         }

         return (int)var2;
      }
   }

   private int calcFeaturedStickersHash(ArrayList var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         TLRPC.StickerSet var5 = ((TLRPC.StickerSetCovered)var1.get(var4)).set;
         if (!var5.archived) {
            long var6 = var5.id;
            int var8 = (int)(var6 >> 32);
            int var9 = (int)var6;
            long var10 = ((var2 * 20261L + 2147483648L + (long)var8) % 2147483648L * 20261L + 2147483648L + (long)var9) % 2147483648L;
            var2 = var10;
            if (this.unreadStickerSets.contains(var6)) {
               var2 = (var10 * 20261L + 2147483648L + 1L) % 2147483648L;
            }
         }
      }

      return (int)var2;
   }

   private static int calcStickersHash(ArrayList var0) {
      long var1 = 0L;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         TLRPC.StickerSet var4 = ((TLRPC.TL_messages_stickerSet)var0.get(var3)).set;
         if (!var4.archived) {
            var1 = (var1 * 20261L + 2147483648L + (long)var4.hash) % 2147483648L;
         }
      }

      return (int)var1;
   }

   public static boolean canAddMessageToMedia(TLRPC.Message var0) {
      boolean var1 = var0 instanceof TLRPC.TL_message_secret;
      int var2;
      if (var1 && (var0.media instanceof TLRPC.TL_messageMediaPhoto || MessageObject.isVideoMessage(var0) || MessageObject.isGifMessage(var0))) {
         var2 = var0.media.ttl_seconds;
         if (var2 != 0 && var2 <= 60) {
            return false;
         }
      }

      TLRPC.MessageMedia var3;
      if (!var1 && var0 instanceof TLRPC.TL_message) {
         var3 = var0.media;
         if ((var3 instanceof TLRPC.TL_messageMediaPhoto || var3 instanceof TLRPC.TL_messageMediaDocument) && var0.media.ttl_seconds != 0) {
            return false;
         }
      }

      var3 = var0.media;
      if (var3 instanceof TLRPC.TL_messageMediaPhoto || var3 instanceof TLRPC.TL_messageMediaDocument && !MessageObject.isGifDocument(var3.document)) {
         return true;
      } else {
         if (!var0.entities.isEmpty()) {
            for(var2 = 0; var2 < var0.entities.size(); ++var2) {
               TLRPC.MessageEntity var4 = (TLRPC.MessageEntity)var0.entities.get(var2);
               if (var4 instanceof TLRPC.TL_messageEntityUrl || var4 instanceof TLRPC.TL_messageEntityTextUrl || var4 instanceof TLRPC.TL_messageEntityEmail) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private static boolean checkInclusion(int var0, ArrayList var1) {
      if (var1 != null && !var1.isEmpty()) {
         int var2 = var1.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            TLRPC.MessageEntity var4 = (TLRPC.MessageEntity)var1.get(var3);
            int var5 = var4.offset;
            if (var5 <= var0 && var5 + var4.length > var0) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean checkIntersection(int var0, int var1, ArrayList var2) {
      if (var2 != null && !var2.isEmpty()) {
         int var3 = var2.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            TLRPC.MessageEntity var5 = (TLRPC.MessageEntity)var2.get(var4);
            int var6 = var5.offset;
            if (var6 > var0 && var6 + var5.length <= var1) {
               return true;
            }
         }
      }

      return false;
   }

   private Intent createIntrnalShortcutIntent(long var1) {
      Intent var3 = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
      int var4 = (int)var1;
      int var5 = (int)(var1 >> 32);
      if (var4 == 0) {
         var3.putExtra("encId", var5);
         if (MessagesController.getInstance(this.currentAccount).getEncryptedChat(var5) == null) {
            return null;
         }
      } else if (var4 > 0) {
         var3.putExtra("userId", var4);
      } else {
         if (var4 >= 0) {
            return null;
         }

         var3.putExtra("chatId", -var4);
      }

      var3.putExtra("currentAccount", this.currentAccount);
      StringBuilder var6 = new StringBuilder();
      var6.append("com.tmessages.openchat");
      var6.append(var1);
      var3.setAction(var6.toString());
      var3.addFlags(67108864);
      return var3;
   }

   private void deletePeer(int var1, int var2) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$zfORkxHX1nBnvnpPSEIl0qTrWEM(this, var1, var2));
   }

   public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Document var0) {
      for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
         TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
         if (var2 instanceof TLRPC.TL_documentAttributeSticker) {
            TLRPC.InputStickerSet var3 = var2.stickerset;
            if (var3 instanceof TLRPC.TL_inputStickerSetEmpty) {
               return null;
            }

            return var3;
         }
      }

      return null;
   }

   public static DataQuery getInstance(int var0) {
      DataQuery var1 = Instance[var0];
      DataQuery var2 = var1;
      if (var1 == null) {
         synchronized(DataQuery.class){}

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
               DataQuery[] var23;
               try {
                  var23 = Instance;
                  var2 = new DataQuery(var0);
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

   private int getMask() {
      int var1 = this.lastReturnedNum;
      int var2 = this.searchResultMessages.size();
      byte var3 = 1;
      byte var4 = var3;
      if (var1 >= var2 - 1) {
         boolean[] var5 = this.messagesSearchEndReached;
         var4 = var3;
         if (var5[0]) {
            if (!var5[1]) {
               var4 = var3;
            } else {
               var4 = 0;
            }
         }
      }

      int var6 = var4;
      if (this.lastReturnedNum > 0) {
         var6 = var4 | 2;
      }

      return var6;
   }

   private void getMediaCountDatabase(long var1, int var3, int var4) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$r7IDcaPS4__Lq9u_Bcjhps7n1wc(this, var1, var3, var4));
   }

   public static int getMediaType(TLRPC.Message var0) {
      if (var0 == null) {
         return -1;
      } else {
         TLRPC.MessageMedia var1 = var0.media;
         boolean var2 = var1 instanceof TLRPC.TL_messageMediaPhoto;
         int var3 = 0;
         if (var2) {
            return 0;
         } else if (var1 instanceof TLRPC.TL_messageMediaDocument) {
            if (!MessageObject.isVoiceMessage(var0) && !MessageObject.isRoundVideoMessage(var0)) {
               if (MessageObject.isVideoMessage(var0)) {
                  return 0;
               } else if (MessageObject.isStickerMessage(var0)) {
                  return -1;
               } else {
                  return MessageObject.isMusicMessage(var0) ? 4 : 1;
               }
            } else {
               return 2;
            }
         } else {
            if (!var0.entities.isEmpty()) {
               while(var3 < var0.entities.size()) {
                  TLRPC.MessageEntity var4 = (TLRPC.MessageEntity)var0.entities.get(var3);
                  if (var4 instanceof TLRPC.TL_messageEntityUrl || var4 instanceof TLRPC.TL_messageEntityTextUrl || var4 instanceof TLRPC.TL_messageEntityEmail) {
                     return 3;
                  }

                  ++var3;
               }
            }

            return -1;
         }
      }
   }

   public static long getStickerSetId(TLRPC.Document var0) {
      for(int var1 = 0; var1 < var0.attributes.size(); ++var1) {
         TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)var0.attributes.get(var1);
         if (var2 instanceof TLRPC.TL_documentAttributeSticker) {
            TLRPC.InputStickerSet var3 = var2.stickerset;
            if (var3 instanceof TLRPC.TL_inputStickerSetID) {
               return var3.id;
            }
            break;
         }
      }

      return -1L;
   }

   // $FF: synthetic method
   static int lambda$increaseInlineRaiting$76(TLRPC.TL_topPeer var0, TLRPC.TL_topPeer var1) {
      double var2 = var0.rating;
      double var4 = var1.rating;
      if (var2 > var4) {
         return -1;
      } else {
         return var2 < var4 ? 1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$markFaturedStickersAsRead$26(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$markFaturedStickersByIdAsRead$27(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$null$120(ArrayList var0, DataQuery.KeywordResult var1, DataQuery.KeywordResult var2) {
      int var3 = var0.indexOf(var1.emoji);
      int var4 = var3;
      if (var3 < 0) {
         var4 = Integer.MAX_VALUE;
      }

      int var5 = var0.indexOf(var2.emoji);
      var3 = var5;
      if (var5 < 0) {
         var3 = Integer.MAX_VALUE;
      }

      if (var4 < var3) {
         return -1;
      } else if (var4 > var3) {
         return 1;
      } else {
         var3 = var1.keyword.length();
         var4 = var2.keyword.length();
         if (var3 < var4) {
            return -1;
         } else {
            return var3 > var4 ? 1 : 0;
         }
      }
   }

   // $FF: synthetic method
   static void lambda$null$121(DataQuery.KeywordResultCallback var0, ArrayList var1, String var2) {
      var0.run(var1, var2);
   }

   // $FF: synthetic method
   static int lambda$null$79(TLRPC.TL_topPeer var0, TLRPC.TL_topPeer var1) {
      double var2 = var0.rating;
      double var4 = var1.rating;
      if (var2 > var4) {
         return -1;
      } else {
         return var2 < var4 ? 1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$removeInline$77(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$removePeer$78(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$reorderStickers$16(ArrayList var0, TLRPC.TL_messages_stickerSet var1, TLRPC.TL_messages_stickerSet var2) {
      int var3 = var0.indexOf(var1.set.id);
      int var4 = var0.indexOf(var2.set.id);
      if (var3 > var4) {
         return 1;
      } else {
         return var3 < var4 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$saveDraft$99(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$static$84(TLRPC.MessageEntity var0, TLRPC.MessageEntity var1) {
      int var2 = var0.offset;
      int var3 = var1.offset;
      if (var2 > var3) {
         return 1;
      } else {
         return var2 < var3 ? -1 : 0;
      }
   }

   private void loadGroupStickerSet(TLRPC.StickerSet var1, boolean var2) {
      if (var2) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_TcC0mAoIoAzht6PKxrSMA0GNwQ(this, var1));
      } else {
         TLRPC.TL_messages_getStickerSet var3 = new TLRPC.TL_messages_getStickerSet();
         var3.stickerset = new TLRPC.TL_inputStickerSetID();
         TLRPC.InputStickerSet var4 = var3.stickerset;
         var4.id = var1.id;
         var4.access_hash = var1.access_hash;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$DataQuery$sSt__gYzisWLp4pAgk5FJ0UZgFM(this));
      }

   }

   private void loadMediaDatabase(long var1, int var3, int var4, int var5, int var6, boolean var7, int var8) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$OqcREWH3JA8hAPi_XM3PyDyEsWM(this, var3, var1, var4, var7, var5, var8, var6));
   }

   private MessageObject loadPinnedMessageInternal(long var1, int var3, int var4, boolean var5) {
      long var6;
      if (var3 != 0) {
         var6 = (long)var4 | (long)var3 << 32;
      } else {
         var6 = (long)var4;
      }

      Exception var10000;
      label154: {
         ArrayList var8;
         ArrayList var9;
         ArrayList var10;
         ArrayList var11;
         SQLiteCursor var12;
         TLRPC.Message var14;
         boolean var10001;
         label155: {
            label147: {
               label146: {
                  NativeByteBuffer var13;
                  try {
                     var8 = new ArrayList();
                     var9 = new ArrayList();
                     var10 = new ArrayList();
                     var11 = new ArrayList();
                     var12 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date FROM messages WHERE mid = %d", var6));
                     if (!var12.next()) {
                        break label146;
                     }

                     var13 = var12.byteBufferValue(0);
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label154;
                  }

                  if (var13 != null) {
                     try {
                        var14 = TLRPC.Message.TLdeserialize(var13, var13.readInt32(false), false);
                        var14.readAttachPath(var13, UserConfig.getInstance(this.currentAccount).clientUserId);
                        var13.reuse();
                        if (!(var14.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                           break label147;
                        }
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label154;
                     }
                  }
               }

               var14 = null;
               break label155;
            }

            try {
               var14.id = var12.intValue(1);
               var14.date = var12.intValue(2);
               var14.dialog_id = var1;
               MessagesStorage.addUsersAndChatsFromMessage(var14, var10, var11);
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label154;
            }
         }

         try {
            var12.dispose();
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label154;
         }

         TLRPC.Message var33 = var14;
         if (var14 == null) {
            SQLiteCursor var34;
            try {
               var34 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM chat_pinned WHERE uid = %d", var1));
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label154;
            }

            var33 = var14;

            label157: {
               NativeByteBuffer var15;
               try {
                  if (!var34.next()) {
                     break label157;
                  }

                  var15 = var34.byteBufferValue(0);
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label154;
               }

               var33 = var14;
               if (var15 != null) {
                  label160: {
                     label158: {
                        try {
                           var33 = TLRPC.Message.TLdeserialize(var15, var15.readInt32(false), false);
                           var33.readAttachPath(var15, UserConfig.getInstance(this.currentAccount).clientUserId);
                           var15.reuse();
                           if (var33.id != var4 || var33.action instanceof TLRPC.TL_messageActionHistoryClear) {
                              break label158;
                           }
                        } catch (Exception var27) {
                           var10000 = var27;
                           var10001 = false;
                           break label154;
                        }

                        try {
                           var33.dialog_id = var1;
                           MessagesStorage.addUsersAndChatsFromMessage(var33, var10, var11);
                           break label160;
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label154;
                        }
                     }

                     var33 = null;
                  }
               }
            }

            try {
               var34.dispose();
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label154;
            }
         }

         if (var33 == null) {
            ConnectionsManager var32;
            if (var3 != 0) {
               try {
                  TLRPC.TL_channels_getMessages var37 = new TLRPC.TL_channels_getMessages();
                  var37.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var3);
                  var37.id.add(var4);
                  var32 = ConnectionsManager.getInstance(this.currentAccount);
                  _$$Lambda$DataQuery$sut4gZqSHSa63dx_2bNsxpC35Sk var35 = new _$$Lambda$DataQuery$sut4gZqSHSa63dx_2bNsxpC35Sk(this, var3);
                  var32.sendRequest(var37, var35);
                  return null;
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
               }
            } else {
               try {
                  TLRPC.TL_messages_getMessages var36 = new TLRPC.TL_messages_getMessages();
                  var36.id.add(var4);
                  var32 = ConnectionsManager.getInstance(this.currentAccount);
                  _$$Lambda$DataQuery$bn_59i2M3GKJW8EWB0ORPpDQN_w var38 = new _$$Lambda$DataQuery$bn_59i2M3GKJW8EWB0ORPpDQN_w(this, var3);
                  var32.sendRequest(var36, var38);
                  return null;
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
               }
            }
         } else if (var5) {
            try {
               return this.broadcastPinnedMessage(var33, var8, var9, true, var5);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
            }
         } else {
            label159: {
               try {
                  var5 = var10.isEmpty();
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label159;
               }

               if (!var5) {
                  try {
                     MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", var10), var8);
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label159;
                  }
               }

               try {
                  if (!var11.isEmpty()) {
                     MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", var11), var9);
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label159;
               }

               try {
                  this.broadcastPinnedMessage(var33, var8, var9, true, false);
                  return null;
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
               }
            }
         }
      }

      Exception var39 = var10000;
      FileLog.e((Throwable)var39);
      return null;
   }

   private void processLoadStickersResponse(int var1, TLRPC.TL_messages_allStickers var2) {
      ArrayList var3 = new ArrayList();
      if (var2.sets.isEmpty()) {
         this.processLoadedStickers(var1, var3, false, (int)(System.currentTimeMillis() / 1000L), var2.hash);
      } else {
         LongSparseArray var4 = new LongSparseArray();

         for(int var5 = 0; var5 < var2.sets.size(); ++var5) {
            TLRPC.StickerSet var6 = (TLRPC.StickerSet)var2.sets.get(var5);
            TLRPC.TL_messages_stickerSet var7 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(var6.id);
            if (var7 != null) {
               TLRPC.StickerSet var8 = var7.set;
               if (var8.hash == var6.hash) {
                  var8.archived = var6.archived;
                  var8.installed = var6.installed;
                  var8.official = var6.official;
                  var4.put(var8.id, var7);
                  var3.add(var7);
                  if (var4.size() == var2.sets.size()) {
                     this.processLoadedStickers(var1, var3, false, (int)(System.currentTimeMillis() / 1000L), var2.hash);
                  }
                  continue;
               }
            }

            var3.add((Object)null);
            TLRPC.TL_messages_getStickerSet var9 = new TLRPC.TL_messages_getStickerSet();
            var9.stickerset = new TLRPC.TL_inputStickerSetID();
            TLRPC.InputStickerSet var10 = var9.stickerset;
            var10.id = var6.id;
            var10.access_hash = var6.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, new _$$Lambda$DataQuery$w1XixRjF16wrwBdg1yg9ZISWPKs(this, var3, var5, var4, var6, var2, var1));
         }
      }

   }

   private void processLoadedFeaturedStickers(ArrayList var1, ArrayList var2, boolean var3, int var4, int var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$Br07Yue1FWlAHhubZJ8mcHmpI1c(this));
      Utilities.stageQueue.postRunnable(new _$$Lambda$DataQuery$cOH_T13u95HL6anewGDnniRkdeE(this, var3, var1, var4, var5, var2));
   }

   private void processLoadedMedia(TLRPC.messages_Messages var1, long var2, int var4, int var5, int var6, int var7, int var8, boolean var9, boolean var10) {
      int var11 = (int)var2;
      if (var7 != 0 && var1.messages.isEmpty() && var11 != 0) {
         if (var7 == 2) {
            return;
         }

         this.loadMedia(var2, var4, var5, var6, 0, var8);
      } else {
         if (var7 == 0) {
            ImageLoader.saveMessagesThumbs(var1.messages);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var1.users, var1.chats, true, true);
            this.putMediaDatabase(var2, var6, var1.messages, var5, var10);
         }

         SparseArray var12 = new SparseArray();
         byte var15 = 0;

         for(var4 = 0; var4 < var1.users.size(); ++var4) {
            TLRPC.User var13 = (TLRPC.User)var1.users.get(var4);
            var12.put(var13.id, var13);
         }

         ArrayList var14 = new ArrayList();

         for(var4 = var15; var4 < var1.messages.size(); ++var4) {
            TLRPC.Message var16 = (TLRPC.Message)var1.messages.get(var4);
            var14.add(new MessageObject(this.currentAccount, var16, var12, true));
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$0dlxm8i_c0v_chVqHF7I_je7M4g(this, var1, var7, var2, var14, var8, var6, var10));
      }

   }

   private void processLoadedMediaCount(int var1, long var2, int var4, int var5, boolean var6, int var7) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$GYaSd5P0GrMZfcQPvBwtuZFrwqw(this, var2, var6, var1, var4, var7, var5));
   }

   private void processLoadedStickers(int var1, ArrayList var2, boolean var3, int var4, int var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$9vMJ1w_Dj5oCt4zZwXAybfYG_nU(this, var1));
      Utilities.stageQueue.postRunnable(new _$$Lambda$DataQuery$inBvXSiWQIx1Y9BuZIwlL6K4fAA(this, var3, var2, var4, var5, var1));
   }

   private void putEmojiKeywords(String var1, TLRPC.TL_emojiKeywordsDifference var2) {
      if (var2 != null) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc(this, var2, var1));
      }
   }

   private void putFeaturedStickersToCache(ArrayList var1, ArrayList var2, int var3, int var4) {
      if (var1 != null) {
         var1 = new ArrayList(var1);
      } else {
         var1 = null;
      }

      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$EvRKZ0icyHpXu5syph8WWuRUigE(this, var1, var2, var3, var4));
   }

   private void putMediaCountDatabase(long var1, int var3, int var4) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$VPyo4uiijc6Mw5vRqsga_xtHpO4(this, var1, var3, var4));
   }

   private void putMediaDatabase(long var1, int var3, ArrayList var4, int var5, boolean var6) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$VgKCIvejIuaPm3YY59TrIO7FIyE(this, var4, var6, var1, var5, var3));
   }

   private void putSetToCache(TLRPC.TL_messages_stickerSet var1) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$Gs_0kL3OC_eV_RiFuvATRDxcFsE(this, var1));
   }

   private void putStickersToCache(int var1, ArrayList var2, int var3, int var4) {
      if (var2 != null) {
         var2 = new ArrayList(var2);
      } else {
         var2 = null;
      }

      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HuXXMuf2NeuYCbcUMY_TfU_FwGA(this, var2, var1, var3, var4));
   }

   private static void removeEmptyMessages(ArrayList var0) {
      int var3;
      for(int var1 = 0; var1 < var0.size(); var1 = var3 + 1) {
         TLRPC.Message var2 = (TLRPC.Message)var0.get(var1);
         if (var2 != null && !(var2 instanceof TLRPC.TL_messageEmpty)) {
            var3 = var1;
            if (!(var2.action instanceof TLRPC.TL_messageActionHistoryClear)) {
               continue;
            }
         }

         var0.remove(var1);
         var3 = var1 - 1;
      }

   }

   private static void removeOffsetAfter(int var0, int var1, ArrayList var2) {
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.MessageEntity var5 = (TLRPC.MessageEntity)var2.get(var4);
         int var6 = var5.offset;
         if (var6 > var0) {
            var5.offset = var6 - var1;
         }
      }

   }

   private void saveDraftReplyMessage(long var1, TLRPC.Message var3) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$d7oypRr3g0xB92hPNQy49uD_p8A(this, var1, var3));
      }
   }

   private void savePeer(int var1, int var2, double var3) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$v4m2TDWRtPX_m91A0Er26YD_Gv8(this, var1, var2, var3));
   }

   private void savePinnedMessage(TLRPC.Message var1) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk(this, var1));
   }

   private void saveReplyMessages(SparseArray var1, ArrayList var2) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$H2PEh31YFWmyOEdst_RwAHB__gE(this, var2, var1));
   }

   private void searchMessagesInChat(String var1, long var2, long var4, int var6, int var7, boolean var8, TLRPC.User var9) {
      boolean var10 = var8 ^ true;
      if (this.reqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
         this.reqId = 0;
      }

      if (this.mergeReqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.mergeReqId, true);
         this.mergeReqId = 0;
      }

      int var11;
      long var14;
      boolean[] var26;
      if (var1 == null) {
         if (this.searchResultMessages.isEmpty()) {
            return;
         }

         int var12;
         NotificationCenter var21;
         MessageObject var22;
         int[] var23;
         int var24;
         if (var7 != 1) {
            if (var7 == 2) {
               --this.lastReturnedNum;
               var7 = this.lastReturnedNum;
               if (var7 < 0) {
                  this.lastReturnedNum = 0;
                  return;
               }

               if (var7 >= this.searchResultMessages.size()) {
                  this.lastReturnedNum = this.searchResultMessages.size() - 1;
               }

               var22 = (MessageObject)this.searchResultMessages.get(this.lastReturnedNum);
               var21 = NotificationCenter.getInstance(this.currentAccount);
               var11 = NotificationCenter.chatSearchResultsAvailable;
               var7 = var22.getId();
               var24 = this.getMask();
               var2 = var22.getDialogId();
               var12 = this.lastReturnedNum;
               var23 = this.messagesSearchCount;
               var21.postNotificationName(var11, var6, var7, var24, var2, var12, var23[0] + var23[1]);
            }

            return;
         }

         ++this.lastReturnedNum;
         if (this.lastReturnedNum < this.searchResultMessages.size()) {
            var22 = (MessageObject)this.searchResultMessages.get(this.lastReturnedNum);
            var21 = NotificationCenter.getInstance(this.currentAccount);
            var7 = NotificationCenter.chatSearchResultsAvailable;
            var11 = var22.getId();
            var24 = this.getMask();
            var2 = var22.getDialogId();
            var12 = this.lastReturnedNum;
            var23 = this.messagesSearchCount;
            var21.postNotificationName(var7, var6, var11, var24, var2, var12, var23[0] + var23[1]);
            return;
         }

         boolean[] var20 = this.messagesSearchEndReached;
         if (var20[0] && var4 == 0L && var20[1]) {
            --this.lastReturnedNum;
            return;
         }

         var1 = this.lastSearchQuery;
         ArrayList var13 = this.searchResultMessages;
         MessageObject var25 = (MessageObject)var13.get(var13.size() - 1);
         if (var25.getDialogId() == var2 && !this.messagesSearchEndReached[0]) {
            var24 = var25.getId();
            var14 = var2;
         } else {
            if (var25.getDialogId() == var4) {
               var24 = var25.getId();
            } else {
               var24 = 0;
            }

            this.messagesSearchEndReached[1] = false;
            var14 = var4;
         }

         var11 = var24;
         var10 = false;
      } else {
         if (var10) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatSearchResultsLoading, var6);
            var26 = this.messagesSearchEndReached;
            var26[1] = false;
            var26[0] = false;
            int[] var27 = this.messagesSearchCount;
            var27[1] = 0;
            var27[0] = 0;
            this.searchResultMessages.clear();
            this.searchResultMessagesMap[0].clear();
            this.searchResultMessagesMap[1].clear();
         }

         var14 = var2;
         var11 = 0;
      }

      var26 = this.messagesSearchEndReached;
      long var16 = var14;
      if (var26[0]) {
         var16 = var14;
         if (!var26[1]) {
            var16 = var14;
            if (var4 != 0L) {
               var16 = var4;
            }
         }
      }

      String var28 = "";
      TLRPC.TL_messages_search var19;
      if (var16 == var2 && var10) {
         if (var4 != 0L) {
            TLRPC.InputPeer var18 = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var4);
            if (var18 == null) {
               return;
            }

            var19 = new TLRPC.TL_messages_search();
            var19.peer = var18;
            this.lastMergeDialogId = var4;
            var19.limit = 1;
            if (var1 != null) {
               var28 = var1;
            }

            var19.q = var28;
            if (var9 != null) {
               var19.from_id = MessagesController.getInstance(this.currentAccount).getInputUser(var9);
               var19.flags |= 1;
            }

            var19.filter = new TLRPC.TL_inputMessagesFilterEmpty();
            this.mergeReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var19, new _$$Lambda$DataQuery$X_xjH4zrLbF98x7_YlP3_d_hOC0(this, var4, var19, var2, var6, var7, var9), 2);
            return;
         }

         this.lastMergeDialogId = 0L;
         this.messagesSearchEndReached[1] = true;
         this.messagesSearchCount[1] = 0;
      }

      var19 = new TLRPC.TL_messages_search();
      var19.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var16);
      if (var19.peer != null) {
         var19.limit = 21;
         if (var1 != null) {
            var28 = var1;
         }

         var19.q = var28;
         var19.offset_id = var11;
         if (var9 != null) {
            var19.from_id = MessagesController.getInstance(this.currentAccount).getInputUser(var9);
            var19.flags |= 1;
         }

         var19.filter = new TLRPC.TL_inputMessagesFilterEmpty();
         var7 = this.lastReqId + 1;
         this.lastReqId = var7;
         this.lastSearchQuery = var1;
         this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var19, new _$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog(this, var7, var19, var16, var2, var6, var4, var9), 2);
      }
   }

   public static void sortEntities(ArrayList var0) {
      Collections.sort(var0, entityComparator);
   }

   public void addNewStickerSet(TLRPC.TL_messages_stickerSet var1) {
      if (this.stickerSetsById.indexOfKey(var1.set.id) < 0 && !this.stickerSetsByName.containsKey(var1.set.short_name)) {
         byte var2 = var1.set.masks;
         this.stickerSets[var2].add(0, var1);
         this.stickerSetsById.put(var1.set.id, var1);
         this.installedStickerSetsById.put(var1.set.id, var1);
         this.stickerSetsByName.put(var1.set.short_name, var1);
         LongSparseArray var3 = new LongSparseArray();

         int var4;
         for(var4 = 0; var4 < var1.documents.size(); ++var4) {
            TLRPC.Document var5 = (TLRPC.Document)var1.documents.get(var4);
            var3.put(var5.id, var5);
         }

         for(var4 = 0; var4 < var1.packs.size(); ++var4) {
            TLRPC.TL_stickerPack var6 = (TLRPC.TL_stickerPack)var1.packs.get(var4);
            var6.emoticon = var6.emoticon.replace("️", "");
            ArrayList var7 = (ArrayList)this.allStickers.get(var6.emoticon);
            ArrayList var9 = var7;
            if (var7 == null) {
               var9 = new ArrayList();
               this.allStickers.put(var6.emoticon, var9);
            }

            for(int var8 = 0; var8 < var6.documents.size(); ++var8) {
               Long var10 = (Long)var6.documents.get(var8);
               if (this.stickersByEmoji.indexOfKey(var10) < 0) {
                  this.stickersByEmoji.put(var10, var6.emoticon);
               }

               TLRPC.Document var11 = (TLRPC.Document)var3.get(var10);
               if (var11 != null) {
                  var9.add(var11);
               }
            }
         }

         this.loadHash[var2] = calcStickersHash(this.stickerSets[var2]);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(var2));
         this.loadStickers(var2, false, true);
      }

   }

   public void addRecentGif(TLRPC.Document var1, int var2) {
      int var3 = 0;

      TLRPC.Document var4;
      boolean var5;
      while(true) {
         if (var3 >= this.recentGifs.size()) {
            var5 = false;
            break;
         }

         var4 = (TLRPC.Document)this.recentGifs.get(var3);
         if (var4.id == var1.id) {
            this.recentGifs.remove(var3);
            this.recentGifs.add(0, var4);
            var5 = true;
            break;
         }

         ++var3;
      }

      if (!var5) {
         this.recentGifs.add(0, var1);
      }

      ArrayList var6;
      if (this.recentGifs.size() > MessagesController.getInstance(this.currentAccount).maxRecentGifsCount) {
         var6 = this.recentGifs;
         var4 = (TLRPC.Document)var6.remove(var6.size() - 1);
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$xvF6o_1_RcVDv47fzE1DMviRP_s(this, var4));
      }

      var6 = new ArrayList();
      var6.add(var1);
      this.processLoadedRecentDocuments(0, var6, true, var2, false);
   }

   public void addRecentSticker(int var1, Object var2, TLRPC.Document var3, int var4, boolean var5) {
      int var6 = 0;

      boolean var12;
      while(true) {
         if (var6 >= this.recentStickers[var1].size()) {
            var12 = false;
            break;
         }

         TLRPC.Document var7 = (TLRPC.Document)this.recentStickers[var1].get(var6);
         if (var7.id == var3.id) {
            this.recentStickers[var1].remove(var6);
            if (!var5) {
               this.recentStickers[var1].add(0, var7);
            }

            var12 = true;
            break;
         }

         ++var6;
      }

      if (!var12 && !var5) {
         this.recentStickers[var1].add(0, var3);
      }

      if (var1 == 2) {
         if (var5) {
            Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("RemovedFromFavorites", 2131560555), 0).show();
         } else {
            Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("AddedToFavorites", 2131558597), 0).show();
         }

         TLRPC.TL_messages_faveSticker var8 = new TLRPC.TL_messages_faveSticker();
         var8.id = new TLRPC.TL_inputDocument();
         TLRPC.InputDocument var13 = var8.id;
         var13.id = var3.id;
         var13.access_hash = var3.access_hash;
         var13.file_reference = var3.file_reference;
         if (var13.file_reference == null) {
            var13.file_reference = new byte[0];
         }

         var8.unfave = var5;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$DataQuery$s_0gO8L3a_nFeJVvPBKP6JduK9o(this, var2, var8));
         var6 = MessagesController.getInstance(this.currentAccount).maxFaveStickersCount;
      } else {
         var6 = MessagesController.getInstance(this.currentAccount).maxRecentStickersCount;
      }

      if (this.recentStickers[var1].size() > var6 || var5) {
         TLRPC.Document var9;
         if (var5) {
            var9 = var3;
         } else {
            ArrayList[] var10 = this.recentStickers;
            var9 = (TLRPC.Document)var10[var1].remove(var10[var1].size() - 1);
         }

         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HfncZGNQeEbuO4TOGgzIo_2VPzk(this, var1, var9));
      }

      if (!var5) {
         ArrayList var11 = new ArrayList();
         var11.add(var3);
         this.processLoadedRecentDocuments(var1, var11, false, var4, false);
      }

      if (var1 == 2) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentDocumentsDidLoad, false, var1);
      }

   }

   public boolean areAllTrendingStickerSetsUnread() {
      int var1 = this.featuredStickerSets.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         TLRPC.StickerSetCovered var3 = (TLRPC.StickerSetCovered)this.featuredStickerSets.get(var2);
         if (!getInstance(this.currentAccount).isStickerPackInstalled(var3.set.id) && (!var3.covers.isEmpty() || var3.cover != null) && !this.unreadStickerSets.contains(var3.set.id)) {
            return false;
         }
      }

      return true;
   }

   public void beginTransaction() {
      this.inTransaction = true;
   }

   public void buildShortcuts() {
      if (VERSION.SDK_INT >= 25) {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < this.hints.size(); ++var2) {
            var1.add(this.hints.get(var2));
            if (var1.size() == 3) {
               break;
            }
         }

         Utilities.globalQueue.postRunnable(new _$$Lambda$DataQuery$usUs3tLksjqGquVtjg9TZkSDUaI(this, var1));
      }
   }

   public void calcNewHash(int var1) {
      this.loadHash[var1] = calcStickersHash(this.stickerSets[var1]);
   }

   public boolean canAddStickerToFavorites() {
      boolean[] var1 = this.stickersLoaded;
      boolean var2 = false;
      if (!var1[0] || this.stickerSets[0].size() >= 5 || !this.recentStickers[2].isEmpty()) {
         var2 = true;
      }

      return var2;
   }

   public void checkFeaturedStickers() {
      if (!this.loadingFeaturedStickers && (!this.featuredStickersLoaded || Math.abs(System.currentTimeMillis() / 1000L - (long)this.loadFeaturedDate) >= 3600L)) {
         this.loadFeaturedStickers(true, false);
      }

   }

   public void checkStickers(int var1) {
      if (!this.loadingStickers[var1] && (!this.stickersLoaded[var1] || Math.abs(System.currentTimeMillis() / 1000L - (long)this.loadDate[var1]) >= 3600L)) {
         this.loadStickers(var1, true, false);
      }

   }

   public void cleanDraft(long var1, boolean var3) {
      TLRPC.DraftMessage var4 = (TLRPC.DraftMessage)this.drafts.get(var1);
      if (var4 != null) {
         if (!var3) {
            this.drafts.remove(var1);
            this.draftMessages.remove(var1);
            Editor var5 = this.preferences.edit();
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(var1);
            var5 = var5.remove(var6.toString());
            var6 = new StringBuilder();
            var6.append("r_");
            var6.append(var1);
            var5.remove(var6.toString()).commit();
            MessagesController.getInstance(this.currentAccount).sortDialogs((SparseArray)null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         } else if (var4.reply_to_msg_id != 0) {
            var4.reply_to_msg_id = 0;
            var4.flags &= -2;
            this.saveDraft(var1, var4.message, var4.entities, (TLRPC.Message)null, var4.no_webpage, true);
         }

      }
   }

   public void cleanup() {
      int var1;
      for(var1 = 0; var1 < 3; ++var1) {
         this.recentStickers[var1].clear();
         this.loadingRecentStickers[var1] = false;
         this.recentStickersLoaded[var1] = false;
      }

      for(var1 = 0; var1 < 4; ++var1) {
         this.loadHash[var1] = 0;
         this.loadDate[var1] = 0;
         this.stickerSets[var1].clear();
         this.loadingStickers[var1] = false;
         this.stickersLoaded[var1] = false;
      }

      this.featuredStickerSets.clear();
      this.loadFeaturedDate = 0;
      this.loadFeaturedHash = 0;
      this.allStickers.clear();
      this.allStickersFeatured.clear();
      this.stickersByEmoji.clear();
      this.featuredStickerSetsById.clear();
      this.featuredStickerSets.clear();
      this.unreadStickerSets.clear();
      this.recentGifs.clear();
      this.stickerSetsById.clear();
      this.installedStickerSetsById.clear();
      this.stickerSetsByName.clear();
      this.loadingFeaturedStickers = false;
      this.featuredStickersLoaded = false;
      this.loadingRecentGifs = false;
      this.recentGifsLoaded = false;
      this.currentFetchingEmoji.clear();
      this.loading = false;
      this.loaded = false;
      this.hints.clear();
      this.inlineBots.clear();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
      this.drafts.clear();
      this.draftMessages.clear();
      this.preferences.edit().clear().commit();
      this.botInfos.clear();
      this.botKeyboards.clear();
      this.botKeyboardsByMids.clear();
   }

   public void clearAllDrafts() {
      this.drafts.clear();
      this.draftMessages.clear();
      this.preferences.edit().clear().commit();
      MessagesController.getInstance(this.currentAccount).sortDialogs((SparseArray)null);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   public void clearBotKeyboard(long var1, ArrayList var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$4JOcfs_Jho1Vm_zEM2xMi0yW30I(this, var3, var1));
   }

   public void clearTopPeers() {
      this.hints.clear();
      this.inlineBots.clear();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$HUiUbvM6V_1I_4yUAM7UkocMBMA(this));
      this.buildShortcuts();
   }

   public void endTransaction() {
      this.inTransaction = false;
   }

   public void fetchNewEmojiKeywords(String[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2];
            if (TextUtils.isEmpty(var3)) {
               return;
            }

            if (this.currentFetchingEmoji.get(var3) != null) {
               return;
            }

            this.currentFetchingEmoji.put(var3, true);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$PLdKh5WsK6t3m___OQnCRDAEWbk(this, var3));
         }

      }
   }

   public HashMap getAllStickers() {
      return this.allStickers;
   }

   public HashMap getAllStickersFeatured() {
      return this.allStickersFeatured;
   }

   public int getArchivedStickersCount(int var1) {
      return this.archivedStickersCount[var1];
   }

   public TLRPC.DraftMessage getDraft(long var1) {
      return (TLRPC.DraftMessage)this.drafts.get(var1);
   }

   public TLRPC.Message getDraftMessage(long var1) {
      return (TLRPC.Message)this.draftMessages.get(var1);
   }

   public String getEmojiForSticker(long var1) {
      String var3 = (String)this.stickersByEmoji.get(var1);
      if (var3 == null) {
         var3 = "";
      }

      return var3;
   }

   public void getEmojiSuggestions(String[] var1, String var2, boolean var3, DataQuery.KeywordResultCallback var4) {
      this.getEmojiSuggestions(var1, var2, var3, var4, (CountDownLatch)null);
   }

   public void getEmojiSuggestions(String[] var1, String var2, boolean var3, DataQuery.KeywordResultCallback var4, CountDownLatch var5) {
      if (var4 != null) {
         if (!TextUtils.isEmpty(var2) && var1 != null) {
            ArrayList var6 = new ArrayList(Emoji.recentEmoji);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA(this, var1, var4, var2, var3, var6, var5));
            if (var5 != null) {
               try {
                  var5.await();
               } catch (Throwable var7) {
               }
            }

         } else {
            var4.run(new ArrayList(), (String)null);
         }
      }
   }

   public ArrayList getEntities(CharSequence[] var1) {
      if (var1 != null && var1[0] != null) {
         ArrayList var2 = null;
         int var3 = 0;

         while(true) {
            boolean var4 = false;
            int var5 = -1;
            int var6 = var3;
            boolean var16 = var4;

            while(true) {
               CharSequence var7 = var1[0];
               String var8;
               if (!var16) {
                  var8 = "`";
               } else {
                  var8 = "```";
               }

               var6 = TextUtils.indexOf(var7, var8, var6);
               int var9;
               int var11;
               TLRPC.TL_messageEntityCode var14;
               byte var18;
               ArrayList var24;
               if (var6 == -1) {
                  var24 = var2;
                  if (var5 != -1) {
                     var24 = var2;
                     if (var16) {
                        var1[0] = AndroidUtilities.concat(this.substring(var1[0], 0, var5), this.substring(var1[0], var5 + 2, var1[0].length()));
                        var24 = var2;
                        if (var2 == null) {
                           var24 = new ArrayList();
                        }

                        var14 = new TLRPC.TL_messageEntityCode();
                        var14.offset = var5;
                        var14.length = 1;
                        var24.add(var14);
                     }
                  }

                  var2 = var24;
                  if (var1[0] instanceof Spanned) {
                     Spanned var34 = (Spanned)var1[0];
                     TypefaceSpan[] var23 = (TypefaceSpan[])var34.getSpans(0, var1[0].length(), TypefaceSpan.class);
                     var2 = var24;
                     if (var23 != null) {
                        var2 = var24;
                        if (var23.length > 0) {
                           var3 = 0;

                           while(true) {
                              var2 = var24;
                              if (var3 >= var23.length) {
                                 break;
                              }

                              TypefaceSpan var27 = var23[var3];
                              var5 = var34.getSpanStart(var27);
                              var6 = var34.getSpanEnd(var27);
                              var2 = var24;
                              if (!checkInclusion(var5, var24)) {
                                 var2 = var24;
                                 if (!checkInclusion(var6, var24)) {
                                    if (checkIntersection(var5, var6, var24)) {
                                       var2 = var24;
                                    } else {
                                       var2 = var24;
                                       if (var24 == null) {
                                          var2 = new ArrayList();
                                       }

                                       Object var35;
                                       if (var27.isMono()) {
                                          var35 = new TLRPC.TL_messageEntityCode();
                                       } else if (var27.isBold()) {
                                          var35 = new TLRPC.TL_messageEntityBold();
                                       } else {
                                          var35 = new TLRPC.TL_messageEntityItalic();
                                       }

                                       ((TLRPC.MessageEntity)var35).offset = var5;
                                       ((TLRPC.MessageEntity)var35).length = var6 - var5;
                                       var2.add(var35);
                                    }
                                 }
                              }

                              ++var3;
                              var24 = var2;
                           }
                        }
                     }

                     URLSpanUserMention[] var30 = (URLSpanUserMention[])var34.getSpans(0, var1[0].length(), URLSpanUserMention.class);
                     var24 = var2;
                     ArrayList var25;
                     if (var30 != null) {
                        var24 = var2;
                        if (var30.length > 0) {
                           var25 = var2;
                           if (var2 == null) {
                              var25 = new ArrayList();
                           }

                           var3 = 0;

                           while(true) {
                              var24 = var25;
                              if (var3 >= var30.length) {
                                 break;
                              }

                              TLRPC.TL_inputMessageEntityMentionName var36 = new TLRPC.TL_inputMessageEntityMentionName();
                              var36.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(Utilities.parseInt(var30[var3].getURL()));
                              if (var36.user_id != null) {
                                 var36.offset = var34.getSpanStart(var30[var3]);
                                 var6 = Math.min(var34.getSpanEnd(var30[var3]), var1[0].length());
                                 var5 = var36.offset;
                                 var36.length = var6 - var5;
                                 if (var1[0].charAt(var5 + var36.length - 1) == ' ') {
                                    --var36.length;
                                 }

                                 var25.add(var36);
                              }

                              ++var3;
                           }
                        }
                     }

                     URLSpanReplacement[] var31 = (URLSpanReplacement[])var34.getSpans(0, var1[0].length(), URLSpanReplacement.class);
                     var2 = var24;
                     if (var31 != null) {
                        var2 = var24;
                        if (var31.length > 0) {
                           var25 = var24;
                           if (var24 == null) {
                              var25 = new ArrayList();
                           }

                           var3 = 0;

                           while(true) {
                              var2 = var25;
                              if (var3 >= var31.length) {
                                 break;
                              }

                              TLRPC.TL_messageEntityTextUrl var37 = new TLRPC.TL_messageEntityTextUrl();
                              var37.offset = var34.getSpanStart(var31[var3]);
                              var37.length = Math.min(var34.getSpanEnd(var31[var3]), var1[0].length()) - var37.offset;
                              var37.url = var31[var3].getURL();
                              var25.add(var37);
                              ++var3;
                           }
                        }
                     }
                  }

                  label230:
                  for(var6 = 0; var6 < 2; ++var6) {
                     String var32;
                     if (var6 == 0) {
                        var32 = "**";
                     } else {
                        var32 = "__";
                     }

                     if (var6 == 0) {
                        var18 = 42;
                     } else {
                        var18 = 95;
                     }

                     var5 = 0;
                     var3 = -1;

                     while(true) {
                        while(true) {
                           var5 = TextUtils.indexOf(var1[0], var32, var5);
                           if (var5 == -1) {
                              continue label230;
                           }

                           if (var3 == -1) {
                              char var33;
                              if (var5 == 0) {
                                 var33 = ' ';
                              } else {
                                 var33 = var1[0].charAt(var5 - 1);
                              }

                              var9 = var3;
                              if (!checkInclusion(var5, var2)) {
                                 label329: {
                                    if (var33 != ' ') {
                                       var9 = var3;
                                       if (var33 != '\n') {
                                          break label329;
                                       }
                                    }

                                    var9 = var5;
                                 }
                              }

                              var5 += 2;
                              var3 = var9;
                           } else {
                              for(var9 = var5 + 2; var9 < var1[0].length() && var1[0].charAt(var9) == var18; ++var9) {
                                 ++var5;
                              }

                              var9 = var5 + 2;
                              if (!checkInclusion(var5, var2) && !checkIntersection(var3, var5, var2)) {
                                 var11 = var3 + 2;
                                 if (var11 != var5) {
                                    var24 = var2;
                                    if (var2 == null) {
                                       var24 = new ArrayList();
                                    }

                                    try {
                                       var1[0] = AndroidUtilities.concat(this.substring(var1[0], 0, var3), this.substring(var1[0], var11, var5), this.substring(var1[0], var9, var1[0].length()));
                                    } catch (Exception var13) {
                                       StringBuilder var26 = new StringBuilder();
                                       var26.append(this.substring(var1[0], 0, var3).toString());
                                       var26.append(this.substring(var1[0], var11, var5).toString());
                                       var26.append(this.substring(var1[0], var9, var1[0].length()).toString());
                                       var1[0] = var26.toString();
                                    }

                                    Object var29;
                                    if (var6 == 0) {
                                       var29 = new TLRPC.TL_messageEntityBold();
                                    } else {
                                       var29 = new TLRPC.TL_messageEntityItalic();
                                    }

                                    ((TLRPC.MessageEntity)var29).offset = var3;
                                    ((TLRPC.MessageEntity)var29).length = var5 - var3 - 2;
                                    removeOffsetAfter(((TLRPC.MessageEntity)var29).offset + ((TLRPC.MessageEntity)var29).length, 4, var24);
                                    var24.add(var29);
                                    var3 = var9 - 4;
                                 } else {
                                    var24 = var2;
                                    var3 = var9;
                                 }
                              } else {
                                 var3 = var9;
                                 var24 = var2;
                              }

                              byte var28 = -1;
                              var5 = var3;
                              var2 = var24;
                              var3 = var28;
                           }
                        }
                     }
                  }

                  return var2;
               }

               if (var5 != -1) {
                  var24 = var2;
                  if (var2 == null) {
                     var24 = new ArrayList();
                  }

                  if (var16) {
                     var18 = 3;
                  } else {
                     var18 = 1;
                  }

                  int var20;
                  for(var20 = var18 + var6; var20 < var1[0].length() && var1[0].charAt(var20) == '`'; ++var20) {
                     ++var6;
                  }

                  if (var16) {
                     var18 = 3;
                  } else {
                     var18 = 1;
                  }

                  var9 = var18 + var6;
                  if (var16) {
                     char var19;
                     if (var5 > 0) {
                        var19 = var1[0].charAt(var5 - 1);
                     } else {
                        var19 = 0;
                     }

                     byte var21;
                     if (var19 != ' ' && var19 != '\n') {
                        var21 = 0;
                     } else {
                        var21 = 1;
                     }

                     CharSequence var15 = this.substring(var1[0], 0, var5 - var21);
                     CharSequence var10 = this.substring(var1[0], var5 + 3, var6);
                     var11 = var6 + 3;
                     char var22;
                     if (var11 < var1[0].length()) {
                        var22 = var1[0].charAt(var11);
                     } else {
                        var22 = 0;
                     }

                     var7 = var1[0];
                     if (var22 != ' ' && var22 != '\n') {
                        var18 = 0;
                     } else {
                        var18 = 1;
                     }

                     CharSequence var12 = this.substring(var7, var11 + var18, var1[0].length());
                     if (var15.length() != 0) {
                        var15 = AndroidUtilities.concat(var15, "\n");
                        var18 = var21;
                     } else {
                        var18 = 1;
                     }

                     var7 = var12;
                     if (var12.length() != 0) {
                        var7 = AndroidUtilities.concat("\n", var12);
                     }

                     var3 = var9;
                     if (!TextUtils.isEmpty(var10)) {
                        var1[0] = AndroidUtilities.concat(var15, var10, var7);
                        TLRPC.TL_messageEntityPre var17 = new TLRPC.TL_messageEntityPre();
                        var17.offset = (var18 ^ 1) + var5;
                        var17.length = var6 - var5 - 3 + (var18 ^ 1);
                        var17.language = "";
                        var24.add(var17);
                        var3 = var9 - 6;
                     }
                  } else {
                     var20 = var5 + 1;
                     var3 = var9;
                     if (var20 != var6) {
                        var1[0] = AndroidUtilities.concat(this.substring(var1[0], 0, var5), this.substring(var1[0], var20, var6), this.substring(var1[0], var6 + 1, var1[0].length()));
                        var14 = new TLRPC.TL_messageEntityCode();
                        var14.offset = var5;
                        var14.length = var6 - var5 - 1;
                        var24.add(var14);
                        var3 = var9 - 2;
                     }
                  }

                  var2 = var24;
                  break;
               }

               if (var1[0].length() - var6 > 2 && var1[0].charAt(var6 + 1) == '`' && var1[0].charAt(var6 + 2) == '`') {
                  var16 = true;
               } else {
                  var16 = false;
               }

               if (var16) {
                  var18 = 3;
               } else {
                  var18 = 1;
               }

               var5 = var6;
               var6 += var18;
            }
         }
      } else {
         return null;
      }
   }

   public ArrayList getFeaturedStickerSets() {
      return this.featuredStickerSets;
   }

   public int getFeaturesStickersHashWithoutUnread() {
      long var1 = 0L;

      for(int var3 = 0; var3 < this.featuredStickerSets.size(); ++var3) {
         TLRPC.StickerSet var4 = ((TLRPC.StickerSetCovered)this.featuredStickerSets.get(var3)).set;
         if (!var4.archived) {
            long var5 = var4.id;
            int var7 = (int)(var5 >> 32);
            int var8 = (int)var5;
            var1 = ((var1 * 20261L + 2147483648L + (long)var7) % 2147483648L * 20261L + 2147483648L + (long)var8) % 2147483648L;
         }
      }

      return (int)var1;
   }

   public TLRPC.TL_messages_stickerSet getGroupStickerSetById(TLRPC.StickerSet var1) {
      TLRPC.TL_messages_stickerSet var2 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(var1.id);
      TLRPC.TL_messages_stickerSet var3 = var2;
      if (var2 == null) {
         var2 = (TLRPC.TL_messages_stickerSet)this.groupStickerSets.get(var1.id);
         if (var2 != null) {
            TLRPC.StickerSet var4 = var2.set;
            if (var4 != null) {
               var3 = var2;
               if (var4.hash != var1.hash) {
                  this.loadGroupStickerSet(var1, false);
                  var3 = var2;
               }

               return var3;
            }
         }

         this.loadGroupStickerSet(var1, true);
         var3 = var2;
      }

      return var3;
   }

   public String getLastSearchQuery() {
      return this.lastSearchQuery;
   }

   public void getMediaCount(long var1, int var3, int var4, boolean var5) {
      int var6 = (int)var1;
      if (!var5 && var6 != 0) {
         TLRPC.TL_messages_search var7 = new TLRPC.TL_messages_search();
         var7.limit = 1;
         var7.offset_id = 0;
         if (var3 == 0) {
            var7.filter = new TLRPC.TL_inputMessagesFilterPhotoVideo();
         } else if (var3 == 1) {
            var7.filter = new TLRPC.TL_inputMessagesFilterDocument();
         } else if (var3 == 2) {
            var7.filter = new TLRPC.TL_inputMessagesFilterRoundVoice();
         } else if (var3 == 3) {
            var7.filter = new TLRPC.TL_inputMessagesFilterUrl();
         } else if (var3 == 4) {
            var7.filter = new TLRPC.TL_inputMessagesFilterMusic();
         }

         var7.q = "";
         var7.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var6);
         if (var7.peer == null) {
            return;
         }

         var3 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$DataQuery$b79Pt8rqwc9fu4IUQhVikz5ynL4(this, var1, var3, var4));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var3, var4);
      } else {
         this.getMediaCountDatabase(var1, var3, var4);
      }

   }

   public void getMediaCounts(long var1, int var3) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$NQ5MkErWobCUOgqYnlpeC3jx_Y8(this, var1, var3));
   }

   public ArrayList getRecentGifs() {
      return new ArrayList(this.recentGifs);
   }

   public ArrayList getRecentStickers(int var1) {
      ArrayList var2 = this.recentStickers[var1];
      return new ArrayList(var2.subList(0, Math.min(var2.size(), 20)));
   }

   public ArrayList getRecentStickersNoCopy(int var1) {
      return this.recentStickers[var1];
   }

   public TLRPC.TL_messages_stickerSet getStickerSetById(long var1) {
      return (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(var1);
   }

   public TLRPC.TL_messages_stickerSet getStickerSetByName(String var1) {
      return (TLRPC.TL_messages_stickerSet)this.stickerSetsByName.get(var1);
   }

   public String getStickerSetName(long var1) {
      TLRPC.TL_messages_stickerSet var3 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(var1);
      if (var3 != null) {
         return var3.set.short_name;
      } else {
         TLRPC.StickerSetCovered var4 = (TLRPC.StickerSetCovered)this.featuredStickerSetsById.get(var1);
         return var4 != null ? var4.set.short_name : null;
      }
   }

   public ArrayList getStickerSets(int var1) {
      return var1 == 3 ? this.stickerSets[2] : this.stickerSets[var1];
   }

   public ArrayList getUnreadStickerSets() {
      return this.unreadStickerSets;
   }

   public boolean hasRecentGif(TLRPC.Document var1) {
      for(int var2 = 0; var2 < this.recentGifs.size(); ++var2) {
         TLRPC.Document var3 = (TLRPC.Document)this.recentGifs.get(var2);
         if (var3.id == var1.id) {
            this.recentGifs.remove(var2);
            this.recentGifs.add(0, var3);
            return true;
         }
      }

      return false;
   }

   public void increaseInlineRaiting(int var1) {
      if (UserConfig.getInstance(this.currentAccount).suggestContacts) {
         int var2;
         if (UserConfig.getInstance(this.currentAccount).botRatingLoadTime != 0) {
            var2 = Math.max(1, (int)(System.currentTimeMillis() / 1000L) - UserConfig.getInstance(this.currentAccount).botRatingLoadTime);
         } else {
            var2 = 60;
         }

         TLRPC.TL_topPeer var3 = null;
         int var4 = 0;

         TLRPC.TL_topPeer var5;
         while(true) {
            var5 = var3;
            if (var4 >= this.inlineBots.size()) {
               break;
            }

            var5 = (TLRPC.TL_topPeer)this.inlineBots.get(var4);
            if (var5.peer.user_id == var1) {
               break;
            }

            ++var4;
         }

         var3 = var5;
         if (var5 == null) {
            var3 = new TLRPC.TL_topPeer();
            var3.peer = new TLRPC.TL_peerUser();
            var3.peer.user_id = var1;
            this.inlineBots.add(var3);
         }

         var3.rating += Math.exp((double)(var2 / MessagesController.getInstance(this.currentAccount).ratingDecay));
         Collections.sort(this.inlineBots, _$$Lambda$DataQuery$PxKOMpM0CQNQDkCfpxm9xlSwLx0.INSTANCE);
         if (this.inlineBots.size() > 20) {
            ArrayList var6 = this.inlineBots;
            var6.remove(var6.size() - 1);
         }

         this.savePeer(var1, 1, var3.rating);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
      }
   }

   public void increasePeerRaiting(long var1) {
      if (UserConfig.getInstance(this.currentAccount).suggestContacts) {
         int var3 = (int)var1;
         if (var3 > 0) {
            TLRPC.User var4;
            if (var3 > 0) {
               var4 = MessagesController.getInstance(this.currentAccount).getUser(var3);
            } else {
               var4 = null;
            }

            if (var4 != null && !var4.bot && !var4.self) {
               MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$5no8NqhTBSPD_1vCOByuz2PkDVw(this, var1, var3));
            }

         }
      }
   }

   public void installShortcut(long param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isLoadingStickers(int var1) {
      return this.loadingStickers[var1];
   }

   public boolean isMessageFound(int var1, boolean var2) {
      boolean var3;
      if (this.searchResultMessagesMap[var2].indexOfKey(var1) >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isStickerInFavorites(TLRPC.Document var1) {
      for(int var2 = 0; var2 < this.recentStickers[2].size(); ++var2) {
         TLRPC.Document var3 = (TLRPC.Document)this.recentStickers[2].get(var2);
         if (var3.id == var1.id && var3.dc_id == var1.dc_id) {
            return true;
         }
      }

      return false;
   }

   public boolean isStickerPackInstalled(long var1) {
      boolean var3;
      if (this.installedStickerSetsById.indexOfKey(var1) >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isStickerPackInstalled(String var1) {
      return this.stickerSetsByName.containsKey(var1);
   }

   public boolean isStickerPackUnread(long var1) {
      return this.unreadStickerSets.contains(var1);
   }

   // $FF: synthetic method
   public void lambda$addRecentGif$4$DataQuery(TLRPC.Document var1) {
      try {
         SQLiteDatabase var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM web_recent_v3 WHERE id = '");
         var3.append(var1.id);
         var3.append("' AND type = 2");
         var2.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$addRecentSticker$0$DataQuery(Object var1, TLRPC.TL_messages_faveSticker var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null && FileRefController.isFileRefError(var4.text) && var1 != null) {
         FileRefController.getInstance(this.currentAccount).requestReference(var1, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$addRecentSticker$1$DataQuery(int var1, TLRPC.Document var2) {
      byte var6;
      if (var1 == 0) {
         var6 = 3;
      } else if (var1 == 1) {
         var6 = 4;
      } else {
         var6 = 5;
      }

      try {
         SQLiteDatabase var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
         StringBuilder var4 = new StringBuilder();
         var4.append("DELETE FROM web_recent_v3 WHERE id = '");
         var4.append(var2.id);
         var4.append("' AND type = ");
         var4.append(var6);
         var3.executeFast(var4.toString()).stepThis().dispose();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

   }

   // $FF: synthetic method
   public void lambda$broadcastPinnedMessage$89$DataQuery(ArrayList var1, boolean var2, ArrayList var3, TLRPC.Message var4, SparseArray var5, SparseArray var6) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, var2);
      MessagesController.getInstance(this.currentAccount).putChats(var3, var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.pinnedMessageDidLoad, new MessageObject(this.currentAccount, var4, var5, var6, false));
   }

   // $FF: synthetic method
   public void lambda$broadcastReplyMessages$96$DataQuery(ArrayList var1, boolean var2, ArrayList var3, ArrayList var4, SparseArray var5, SparseArray var6, SparseArray var7, long var8) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, var2);
      MessagesController.getInstance(this.currentAccount).putChats(var3, var2);
      int var10 = 0;

      boolean var11;
      for(var11 = false; var10 < var4.size(); ++var10) {
         TLRPC.Message var14 = (TLRPC.Message)var4.get(var10);
         var1 = (ArrayList)var5.get(var14.id);
         if (var1 != null) {
            MessageObject var15 = new MessageObject(this.currentAccount, var14, var6, var7, false);

            for(int var16 = 0; var16 < var1.size(); ++var16) {
               MessageObject var12 = (MessageObject)var1.get(var16);
               var12.replyMessageObject = var15;
               TLRPC.MessageAction var13 = var12.messageOwner.action;
               if (var13 instanceof TLRPC.TL_messageActionPinMessage) {
                  var12.generatePinMessageText((TLRPC.User)null, (TLRPC.Chat)null);
               } else if (var13 instanceof TLRPC.TL_messageActionGameScore) {
                  var12.generateGameMessageText((TLRPC.User)null);
               } else if (var13 instanceof TLRPC.TL_messageActionPaymentSent) {
                  var12.generatePaymentSentMessageText((TLRPC.User)null);
               }

               if (var12.isMegagroup()) {
                  TLRPC.Message var17 = var12.replyMessageObject.messageOwner;
                  var17.flags |= Integer.MIN_VALUE;
               }
            }

            var11 = true;
         }
      }

      if (var11) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replyMessagesDidLoad, var8);
      }

   }

   // $FF: synthetic method
   public void lambda$buildShortcuts$67$DataQuery(ArrayList param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$clearBotKeyboard$104$DataQuery(ArrayList var1, long var2) {
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.size(); ++var4) {
            var2 = this.botKeyboardsByMids.get((Integer)var1.get(var4));
            if (var2 != 0L) {
               this.botKeyboards.remove(var2);
               this.botKeyboardsByMids.delete((Integer)var1.get(var4));
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, null, var2);
            }
         }
      } else {
         this.botKeyboards.remove(var2);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, null, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$clearTopPeers$75$DataQuery() {
      try {
         MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
      } catch (Exception var2) {
      }

   }

   // $FF: synthetic method
   public void lambda$deletePeer$83$DataQuery(int var1, int var2) {
      try {
         MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast(String.format(Locale.US, "DELETE FROM chat_hints WHERE did = %d AND type = %d", var1, var2)).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$fetchNewEmojiKeywords$116$DataQuery(String var1) {
      String var2 = null;
      Exception var3 = null;
      long var4 = 0L;
      String var6 = var2;

      int var8;
      label77: {
         int var11;
         label76: {
            long var9;
            Exception var10000;
            String var19;
            label81: {
               SQLiteCursor var7;
               boolean var10001;
               label74: {
                  label73: {
                     label82: {
                        try {
                           var7 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT alias, version, date FROM emoji_keywords_info_v2 WHERE lang = ?", var1);
                        } catch (Exception var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label82;
                        }

                        var6 = var2;

                        try {
                           if (!var7.next()) {
                              break label73;
                           }
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label82;
                        }

                        var6 = var2;

                        try {
                           var2 = var7.stringValue(0);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label82;
                        }

                        var6 = var2;

                        try {
                           var8 = var7.intValue(1);
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label82;
                        }

                        var19 = var2;
                        var9 = var4;
                        var11 = var8;

                        try {
                           var4 = var7.longValue(2);
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label81;
                        }

                        var6 = var2;
                        break label74;
                     }

                     var3 = var10000;
                     var11 = -1;
                     break label76;
                  }

                  var8 = -1;
                  var6 = var3;
               }

               var19 = var6;
               var9 = var4;
               var11 = var8;

               try {
                  var7.dispose();
                  break label77;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            }

            Exception var18 = var10000;
            var6 = var19;
            var4 = var9;
            var3 = var18;
         }

         FileLog.e((Throwable)var3);
         var8 = var11;
      }

      if (!BuildVars.DEBUG_VERSION && Math.abs(System.currentTimeMillis() - var4) < 3600000L) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$j5bKk2bgx79DQU8ExYLHBW0OgBI(this, var1));
      } else {
         Object var20;
         if (var8 == -1) {
            var20 = new TLRPC.TL_messages_getEmojiKeywords();
            ((TLRPC.TL_messages_getEmojiKeywords)var20).lang_code = var1;
         } else {
            var20 = new TLRPC.TL_messages_getEmojiKeywordsDifference();
            ((TLRPC.TL_messages_getEmojiKeywordsDifference)var20).lang_code = var1;
            ((TLRPC.TL_messages_getEmojiKeywordsDifference)var20).from_version = var8;
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var20, new _$$Lambda$DataQuery$3jRza4bNh4kyolsI4k84JKOEvVQ(this, var8, var6, var1));
      }
   }

   // $FF: synthetic method
   public void lambda$getEmojiSuggestions$122$DataQuery(String[] var1, DataQuery.KeywordResultCallback var2, String var3, boolean var4, ArrayList var5, CountDownLatch var6) {
      ArrayList var7 = new ArrayList();
      HashMap var8 = new HashMap();
      String var9 = null;
      int var10 = 0;
      boolean var11 = false;

      while(true) {
         String var12 = var9;

         label319: {
            Exception var10000;
            label325: {
               String var14;
               boolean var10001;
               label317: {
                  try {
                     if (var10 < var1.length) {
                        break label317;
                     }
                  } catch (Exception var50) {
                     var10000 = var50;
                     var10001 = false;
                     break label325;
                  }

                  if (!var11) {
                     var12 = var9;

                     _$$Lambda$DataQuery$F5S3CTjE_FLA0OXEFvyeWWAj6H0 var53;
                     try {
                        var53 = new _$$Lambda$DataQuery$F5S3CTjE_FLA0OXEFvyeWWAj6H0;
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label325;
                     }

                     var12 = var9;

                     try {
                        var53.<init>(this, var1, var2, var7);
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label325;
                     }

                     var12 = var9;

                     try {
                        AndroidUtilities.runOnUIThread(var53);
                        return;
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label325;
                     }
                  } else {
                     var12 = var9;

                     try {
                        var3 = var3.toLowerCase();
                     } catch (Exception var49) {
                        var10000 = var49;
                        var10001 = false;
                        break label325;
                     }

                     var10 = 0;

                     while(true) {
                        var12 = var9;
                        if (var10 >= 2) {
                           break label319;
                        }

                        label329: {
                           String var51;
                           if (var10 == 1) {
                              var12 = var9;

                              try {
                                 var14 = LocaleController.getInstance().getTranslitString(var3, false, false);
                              } catch (Exception var45) {
                                 var10000 = var45;
                                 var10001 = false;
                                 break label325;
                              }

                              var51 = var14;
                              var12 = var9;

                              try {
                                 if (var14.equals(var3)) {
                                    break label329;
                                 }
                              } catch (Exception var48) {
                                 var10000 = var48;
                                 var10001 = false;
                                 break label325;
                              }
                           } else {
                              var51 = var3;
                           }

                           var12 = var9;

                           StringBuilder var54;
                           try {
                              var54 = new StringBuilder;
                           } catch (Exception var44) {
                              var10000 = var44;
                              var10001 = false;
                              break label325;
                           }

                           var12 = var9;

                           try {
                              var54.<init>(var51);
                           } catch (Exception var43) {
                              var10000 = var43;
                              var10001 = false;
                              break label325;
                           }

                           var12 = var9;

                           int var56;
                           try {
                              var56 = var54.length();
                           } catch (Exception var42) {
                              var10000 = var42;
                              var10001 = false;
                              break label325;
                           }

                           label295: {
                              while(var56 > 0) {
                                 --var56;
                                 var12 = var9;

                                 char var15;
                                 try {
                                    var15 = (char)(var54.charAt(var56) + 1);
                                 } catch (Exception var41) {
                                    var10000 = var41;
                                    var10001 = false;
                                    break label325;
                                 }

                                 var12 = var9;

                                 try {
                                    var54.setCharAt(var56, var15);
                                 } catch (Exception var40) {
                                    var10000 = var40;
                                    var10001 = false;
                                    break label325;
                                 }

                                 if (var15 != 0) {
                                    var12 = var9;

                                    try {
                                       var3 = var54.toString();
                                       break label295;
                                    } catch (Exception var39) {
                                       var10000 = var39;
                                       var10001 = false;
                                       break label325;
                                    }
                                 }
                              }

                              var3 = null;
                           }

                           SQLiteCursor var55;
                           if (var4) {
                              var12 = var9;

                              try {
                                 var55 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword = ?", var51);
                              } catch (Exception var38) {
                                 var10000 = var38;
                                 var10001 = false;
                                 break label325;
                              }
                           } else if (var3 != null) {
                              var12 = var9;

                              try {
                                 var55 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword >= ? AND keyword < ?", var51, var3);
                              } catch (Exception var37) {
                                 var10000 = var37;
                                 var10001 = false;
                                 break label325;
                              }
                           } else {
                              var12 = var9;

                              try {
                                 var54 = new StringBuilder;
                              } catch (Exception var36) {
                                 var10000 = var36;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var54.<init>();
                              } catch (Exception var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var54.append(var51);
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var54.append("%");
                              } catch (Exception var33) {
                                 var10000 = var33;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var51 = var54.toString();
                              } catch (Exception var32) {
                                 var10000 = var32;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var55 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword LIKE ?", var51);
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break label325;
                              }
                           }

                           label285:
                           while(true) {
                              while(true) {
                                 var12 = var9;

                                 try {
                                    if (!var55.next()) {
                                       break label285;
                                    }
                                 } catch (Exception var46) {
                                    var10000 = var46;
                                    var10001 = false;
                                    break label325;
                                 }

                                 var12 = var9;

                                 try {
                                    var14 = var55.stringValue(0).replace("️", "");
                                 } catch (Exception var30) {
                                    var10000 = var30;
                                    var10001 = false;
                                    break label325;
                                 }

                                 var12 = var9;

                                 try {
                                    if (var8.get(var14) != null) {
                                       continue;
                                    }
                                    break;
                                 } catch (Exception var47) {
                                    var10000 = var47;
                                    var10001 = false;
                                    break label325;
                                 }
                              }

                              var12 = var9;

                              try {
                                 var8.put(var14, true);
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              DataQuery.KeywordResult var13;
                              try {
                                 var13 = new DataQuery.KeywordResult;
                              } catch (Exception var28) {
                                 var10000 = var28;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var13.<init>();
                              } catch (Exception var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var13.emoji = var14;
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var13.keyword = var55.stringValue(1);
                              } catch (Exception var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break label325;
                              }

                              var12 = var9;

                              try {
                                 var7.add(var13);
                              } catch (Exception var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label325;
                              }
                           }

                           var12 = var9;

                           try {
                              var55.dispose();
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break label325;
                           }

                           var3 = var51;
                        }

                        ++var10;
                     }
                  }
               }

               var12 = var9;

               SQLiteCursor var57;
               try {
                  var57 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT alias FROM emoji_keywords_info_v2 WHERE lang = ?", var1[var10]);
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label325;
               }

               var14 = var9;
               var12 = var9;

               label335: {
                  try {
                     if (!var57.next()) {
                        break label335;
                     }
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label325;
                  }

                  var12 = var9;

                  try {
                     var14 = var57.stringValue(0);
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label325;
                  }
               }

               var12 = var14;

               try {
                  var57.dispose();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label325;
               }

               if (var14 != null) {
                  var11 = true;
               }

               ++var10;
               var9 = var14;
               continue;
            }

            Exception var52 = var10000;
            FileLog.e((Throwable)var52);
         }

         Collections.sort(var7, new _$$Lambda$DataQuery$EciNoKVivTg7lU2H2PBLqQwZEOk(var5));
         if (var6 != null) {
            var2.run(var7, var12);
            var6.countDown();
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$WcOlD4X1lYU5wXzTVWls1TBaRSM(var2, var7, var12));
         }

         return;
      }
   }

   // $FF: synthetic method
   public void lambda$getMediaCount$58$DataQuery(long var1, int var3, int var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         TLRPC.messages_Messages var8 = (TLRPC.messages_Messages)var5;
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var8.users, var8.chats, true, true);
         int var7;
         if (var8 instanceof TLRPC.TL_messages_messages) {
            var7 = var8.messages.size();
         } else {
            var7 = var8.count;
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$GKdiXJjXrtMcEJNPcPmeNeb7UEQ(this, var8));
         this.processLoadedMediaCount(var7, var1, var3, var4, false, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$getMediaCountDatabase$62$DataQuery(long var1, int var3, int var4) {
      Exception var10000;
      label76: {
         SQLiteCursor var5;
         int var6;
         int var7;
         boolean var10001;
         label69: {
            try {
               var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", var1, var3));
               if (var5.next()) {
                  var6 = var5.intValue(0);
                  var7 = var5.intValue(1);
                  break label69;
               }
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label76;
            }

            var6 = -1;
            var7 = 0;
         }

         try {
            var5.dispose();
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label76;
         }

         label57: {
            int var8 = (int)var1;
            int var9 = var6;
            if (var6 == -1) {
               var9 = var6;
               if (var8 == 0) {
                  try {
                     var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM media_v2 WHERE uid = %d AND type = %d LIMIT 1", var1, var3));
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label76;
                  }

                  try {
                     if (var5.next()) {
                        var6 = var5.intValue(0);
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label76;
                  }

                  try {
                     var5.dispose();
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label76;
                  }

                  var9 = var6;
                  if (var6 != -1) {
                     try {
                        this.putMediaCountDatabase(var1, var3, var6);
                        break label57;
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label76;
                     }
                  }
               }
            }

            var6 = var9;
         }

         try {
            this.processLoadedMediaCount(var6, var1, var3, var4, true, var7);
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   public void lambda$getMediaCounts$56$DataQuery(long var1, int var3) {
      Exception var10000;
      label221: {
         int[] var4;
         boolean var10001;
         try {
            var4 = new int[5];
         } catch (Exception var37) {
            var10000 = var37;
            var10001 = false;
            break label221;
         }

         var4[0] = -1;
         var4[1] = -1;
         var4[2] = -1;
         var4[3] = -1;
         var4[4] = -1;

         int[] var5;
         try {
            var5 = new int[5];
         } catch (Exception var36) {
            var10000 = var36;
            var10001 = false;
            break label221;
         }

         var5[0] = -1;
         var5[1] = -1;
         var5[2] = -1;
         var5[3] = -1;
         var5[4] = -1;

         int[] var6;
         try {
            var6 = new int[5];
         } catch (Exception var35) {
            var10000 = var35;
            var10001 = false;
            break label221;
         }

         var6[0] = 0;
         var6[1] = 0;
         var6[2] = 0;
         var6[3] = 0;
         var6[4] = 0;

         SQLiteCursor var7;
         try {
            var7 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT type, count, old FROM media_counts_v2 WHERE uid = %d", var1));
         } catch (Exception var33) {
            var10000 = var33;
            var10001 = false;
            break label221;
         }

         int var8;
         while(true) {
            try {
               if (!var7.next()) {
                  break;
               }

               var8 = var7.intValue(0);
            } catch (Exception var34) {
               var10000 = var34;
               var10001 = false;
               break label221;
            }

            if (var8 >= 0 && var8 < 5) {
               int var9;
               try {
                  var9 = var7.intValue(1);
               } catch (Exception var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label221;
               }

               var4[var8] = var9;
               var5[var8] = var9;

               try {
                  var6[var8] = var7.intValue(2);
               } catch (Exception var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label221;
               }
            }
         }

         try {
            var7.dispose();
         } catch (Exception var30) {
            var10000 = var30;
            var10001 = false;
            break label221;
         }

         int var10 = (int)var1;
         if (var10 == 0) {
            label223: {
               var3 = 0;

               while(true) {
                  try {
                     if (var3 >= var4.length) {
                        break;
                     }
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label223;
                  }

                  if (var4[var3] == -1) {
                     SQLiteCursor var38;
                     label132: {
                        try {
                           var38 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM media_v2 WHERE uid = %d AND type = %d LIMIT 1", var1, var3));
                           if (var38.next()) {
                              var4[var3] = var38.intValue(0);
                              break label132;
                           }
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label223;
                        }

                        var4[var3] = 0;
                     }

                     try {
                        var38.dispose();
                        this.putMediaCountDatabase(var1, var3, var4[var3]);
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label223;
                     }
                  }

                  ++var3;
               }

               try {
                  _$$Lambda$DataQuery$uGKbWFS5irrcqKAN9F4Ebx5IyKQ var39 = new _$$Lambda$DataQuery$uGKbWFS5irrcqKAN9F4Ebx5IyKQ(this, var1, var4);
                  AndroidUtilities.runOnUIThread(var39);
                  return;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
               }
            }
         } else {
            label225: {
               var8 = 0;
               boolean var43 = false;

               while(true) {
                  try {
                     if (var8 >= var4.length) {
                        break;
                     }
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label225;
                  }

                  boolean var44;
                  label232: {
                     if (var4[var8] == -1 || var6[var8] == 1) {
                        TLRPC.TL_messages_search var42;
                        try {
                           var42 = new TLRPC.TL_messages_search();
                           var42.limit = 1;
                           var42.offset_id = 0;
                        } catch (Exception var27) {
                           var10000 = var27;
                           var10001 = false;
                           break label225;
                        }

                        if (var8 == 0) {
                           try {
                              TLRPC.TL_inputMessagesFilterPhotoVideo var12 = new TLRPC.TL_inputMessagesFilterPhotoVideo();
                              var42.filter = var12;
                           } catch (Exception var26) {
                              var10000 = var26;
                              var10001 = false;
                              break label225;
                           }
                        } else if (var8 == 1) {
                           try {
                              TLRPC.TL_inputMessagesFilterDocument var45 = new TLRPC.TL_inputMessagesFilterDocument();
                              var42.filter = var45;
                           } catch (Exception var25) {
                              var10000 = var25;
                              var10001 = false;
                              break label225;
                           }
                        } else if (var8 == 2) {
                           try {
                              TLRPC.TL_inputMessagesFilterRoundVoice var46 = new TLRPC.TL_inputMessagesFilterRoundVoice();
                              var42.filter = var46;
                           } catch (Exception var24) {
                              var10000 = var24;
                              var10001 = false;
                              break label225;
                           }
                        } else if (var8 != 3) {
                           if (var8 == 4) {
                              try {
                                 TLRPC.TL_inputMessagesFilterMusic var48 = new TLRPC.TL_inputMessagesFilterMusic();
                                 var42.filter = var48;
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break label225;
                              }
                           }
                        } else {
                           try {
                              TLRPC.TL_inputMessagesFilterUrl var47 = new TLRPC.TL_inputMessagesFilterUrl();
                              var42.filter = var47;
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break label225;
                           }
                        }

                        label228: {
                           try {
                              var42.q = "";
                              var42.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var10);
                              if (var42.peer == null) {
                                 break label228;
                              }
                           } catch (Exception var29) {
                              var10000 = var29;
                              var10001 = false;
                              break label225;
                           }

                           ConnectionsManager var13;
                           _$$Lambda$DataQuery$i4rT5QEZ9uG6p5oQhmr_S03Vnuo var49;
                           try {
                              var13 = ConnectionsManager.getInstance(this.currentAccount);
                              var49 = new _$$Lambda$DataQuery$i4rT5QEZ9uG6p5oQhmr_S03Vnuo;
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label225;
                           }

                           try {
                              var49.<init>(this, var4, var8, var1);
                              int var11 = var13.sendRequest(var42, var49);
                              ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var11, var3);
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label225;
                           }

                           if (var4[var8] == -1) {
                              var44 = true;
                           } else {
                              var44 = var43;
                              if (var6[var8] == 1) {
                                 var4[var8] = -1;
                                 var44 = var43;
                              }
                           }
                           break label232;
                        }

                        var4[var8] = 0;
                     }

                     var44 = var43;
                  }

                  ++var8;
                  var43 = var44;
               }

               if (var43) {
                  return;
               }

               try {
                  _$$Lambda$DataQuery$wSmeN_vKHIRrAW1dEanMWvFyMCo var41 = new _$$Lambda$DataQuery$wSmeN_vKHIRrAW1dEanMWvFyMCo(this, var1, var5);
                  AndroidUtilities.runOnUIThread(var41);
                  return;
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
               }
            }
         }
      }

      Exception var40 = var10000;
      FileLog.e((Throwable)var40);
   }

   // $FF: synthetic method
   public void lambda$increasePeerRaiting$81$DataQuery(long var1, int var3) {
      double var4 = 0.0D;

      double var10;
      label55: {
         int var8;
         int var9;
         label54: {
            Exception var10000;
            label60: {
               SQLiteDatabase var6;
               Locale var7;
               boolean var10001;
               try {
                  var6 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
                  var7 = Locale.US;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label60;
               }

               var8 = 0;

               SQLiteCursor var16;
               label47: {
                  try {
                     var16 = var6.queryFinalized(String.format(var7, "SELECT MAX(mid), MAX(date) FROM messages WHERE uid = %d AND out = 1", var1));
                     if (var16.next()) {
                        var8 = var16.intValue(0);
                        var9 = var16.intValue(1);
                        break label47;
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label60;
                  }

                  var9 = 0;
               }

               try {
                  var16.dispose();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label60;
               }

               var10 = var4;
               if (var8 <= 0) {
                  break label55;
               }

               var10 = var4;

               try {
                  if (UserConfig.getInstance(this.currentAccount).ratingLoadTime == 0) {
                     break label55;
                  }

                  var8 = UserConfig.getInstance(this.currentAccount).ratingLoadTime;
                  break label54;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            }

            Exception var17 = var10000;
            FileLog.e((Throwable)var17);
            var10 = var4;
            break label55;
         }

         var10 = (double)(var9 - var8);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$5DGSvGUh65NYBwUAJYUrimvkN1o(this, var3, var10, var1));
   }

   // $FF: synthetic method
   public void lambda$loadArchivedStickersCount$30$DataQuery(int var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$ii2s4yGT_QcUcBrCrSZAhn6DFB4(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadBotInfo$108$DataQuery(int var1, int var2) {
      _$$Lambda$DataQuery$8CCh4_uK8so3qsC_8J2GOPVArpo var3 = null;

      Exception var10000;
      label62: {
         SQLiteCursor var4;
         boolean var10001;
         try {
            var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_info WHERE uid = %d", var1));
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label62;
         }

         TLRPC.BotInfo var5 = var3;

         label63: {
            try {
               if (!var4.next()) {
                  break label63;
               }
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label62;
            }

            var5 = var3;

            NativeByteBuffer var6;
            try {
               if (var4.isNull(0)) {
                  break label63;
               }

               var6 = var4.byteBufferValue(0);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label62;
            }

            var5 = var3;
            if (var6 != null) {
               try {
                  var5 = TLRPC.BotInfo.TLdeserialize(var6, var6.readInt32(false), false);
                  var6.reuse();
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label62;
               }
            }
         }

         try {
            var4.dispose();
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label62;
         }

         if (var5 == null) {
            return;
         }

         try {
            var3 = new _$$Lambda$DataQuery$8CCh4_uK8so3qsC_8J2GOPVArpo(this, var5, var2);
            AndroidUtilities.runOnUIThread(var3);
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
   public void lambda$loadBotKeyboard$106$DataQuery(long var1) {
      _$$Lambda$DataQuery$pG7ILjNgxGBHl9Rr7Ps_HNlhdG8 var3 = null;

      Exception var10000;
      label62: {
         SQLiteCursor var4;
         boolean var10001;
         try {
            var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_keyboard WHERE uid = %d", var1));
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label62;
         }

         TLRPC.Message var5 = var3;

         label63: {
            try {
               if (!var4.next()) {
                  break label63;
               }
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label62;
            }

            var5 = var3;

            NativeByteBuffer var6;
            try {
               if (var4.isNull(0)) {
                  break label63;
               }

               var6 = var4.byteBufferValue(0);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label62;
            }

            var5 = var3;
            if (var6 != null) {
               try {
                  var5 = TLRPC.Message.TLdeserialize(var6, var6.readInt32(false), false);
                  var6.reuse();
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label62;
               }
            }
         }

         try {
            var4.dispose();
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label62;
         }

         if (var5 == null) {
            return;
         }

         try {
            var3 = new _$$Lambda$DataQuery$pG7ILjNgxGBHl9Rr7Ps_HNlhdG8(this, var5, var1);
            AndroidUtilities.runOnUIThread(var3);
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
   public void lambda$loadDrafts$98$DataQuery(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var1, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$VlsaxyuL0jbFdSJtmR9unZb00xo(this));
      }
   }

   // $FF: synthetic method
   public void lambda$loadFeaturedStickers$17$DataQuery() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadFeaturedStickers$19$DataQuery(TLRPC.TL_messages_getFeaturedStickers var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$LGTT4xzHAhL56fxY9zS2yMdk6qA(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadGroupStickerSet$6$DataQuery(TLRPC.StickerSet var1) {
      Throwable var10000;
      label71: {
         SQLiteDatabase var2;
         SQLiteCursor var4;
         boolean var5;
         boolean var10001;
         try {
            var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            StringBuilder var3 = new StringBuilder();
            var3.append("SELECT document FROM web_recent_v3 WHERE id = 's_");
            var3.append(var1.id);
            var3.append("'");
            var4 = var2.queryFinalized(var3.toString());
            var5 = var4.next();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label71;
         }

         var2 = null;
         TLRPC.TL_messages_stickerSet var16 = var2;
         if (var5) {
            label72: {
               var16 = var2;

               NativeByteBuffer var6;
               try {
                  if (var4.isNull(0)) {
                     break label72;
                  }

                  var6 = var4.byteBufferValue(0);
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label71;
               }

               var16 = var2;
               if (var6 != null) {
                  try {
                     var16 = TLRPC.TL_messages_stickerSet.TLdeserialize(var6, var6.readInt32(false), false);
                     var6.reuse();
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label71;
                  }
               }
            }
         }

         try {
            var4.dispose();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label71;
         }

         label50: {
            if (var16 != null) {
               try {
                  if (var16.set != null && var16.set.hash == var1.hash) {
                     break label50;
                  }
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label71;
               }
            }

            try {
               this.loadGroupStickerSet(var1, false);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label71;
            }
         }

         if (var16 == null) {
            return;
         }

         try {
            if (var16.set != null) {
               _$$Lambda$DataQuery$mnsVNSCVdaMktJngurpfoZuqcbc var15 = new _$$Lambda$DataQuery$mnsVNSCVdaMktJngurpfoZuqcbc(this, var16);
               AndroidUtilities.runOnUIThread(var15);
            }

            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Throwable var14 = var10000;
      FileLog.e(var14);
   }

   // $FF: synthetic method
   public void lambda$loadGroupStickerSet$8$DataQuery(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$Wjx9ZjzCtddl_eyuO29wwGUiJOs(this, (TLRPC.TL_messages_stickerSet)var1));
      }

   }

   // $FF: synthetic method
   public void lambda$loadHints$69$DataQuery() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      int var5 = UserConfig.getInstance(this.currentAccount).getClientUserId();

      Exception var10000;
      label104: {
         ArrayList var6;
         ArrayList var7;
         SQLiteCursor var8;
         boolean var10001;
         try {
            var6 = new ArrayList();
            var7 = new ArrayList();
            var8 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT did, type, rating FROM chat_hints WHERE 1 ORDER BY rating DESC");
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
            break label104;
         }

         while(true) {
            int var9;
            try {
               if (!var8.next()) {
                  break;
               }

               var9 = var8.intValue(0);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label104;
            }

            if (var9 != var5) {
               int var10;
               TLRPC.TL_topPeer var11;
               try {
                  var10 = var8.intValue(1);
                  var11 = new TLRPC.TL_topPeer();
                  var11.rating = var8.doubleValue(2);
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label104;
               }

               if (var9 > 0) {
                  try {
                     TLRPC.TL_peerUser var12 = new TLRPC.TL_peerUser();
                     var11.peer = var12;
                     var11.peer.user_id = var9;
                     var6.add(var9);
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label104;
                  }
               } else {
                  TLRPC.Peer var29;
                  try {
                     TLRPC.TL_peerChat var28 = new TLRPC.TL_peerChat();
                     var11.peer = var28;
                     var29 = var11.peer;
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label104;
                  }

                  var9 = -var9;

                  try {
                     var29.chat_id = var9;
                     var7.add(var9);
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label104;
                  }
               }

               if (var10 == 0) {
                  try {
                     var1.add(var11);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label104;
                  }
               } else if (var10 == 1) {
                  try {
                     var2.add(var11);
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label104;
                  }
               }
            }
         }

         boolean var13;
         try {
            var8.dispose();
            var13 = var6.isEmpty();
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label104;
         }

         if (!var13) {
            try {
               MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", var6), var3);
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label104;
            }
         }

         try {
            if (!var7.isEmpty()) {
               MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", var7), var4);
            }
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label104;
         }

         try {
            _$$Lambda$DataQuery$rnIvVF8aLiMm_ZCPm9Vn6vsrLWI var27 = new _$$Lambda$DataQuery$rnIvVF8aLiMm_ZCPm9Vn6vsrLWI(this, var3, var4, var1, var2);
            AndroidUtilities.runOnUIThread(var27);
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var26 = var10000;
      FileLog.e((Throwable)var26);
   }

   // $FF: synthetic method
   public void lambda$loadHints$74$DataQuery(TLObject var1, TLRPC.TL_error var2) {
      if (var1 instanceof TLRPC.TL_contacts_topPeers) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$oRCKjdqN7faicjM2qImTY_wiotg(this, var1));
      } else if (var1 instanceof TLRPC.TL_contacts_topPeersDisabled) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$g9_k4zEX2f9EpLAUA_w8HsokieE(this));
      }

   }

   // $FF: synthetic method
   public void lambda$loadMedia$51$DataQuery(long var1, int var3, int var4, int var5, int var6, boolean var7, TLObject var8, TLRPC.TL_error var9) {
      if (var9 == null) {
         TLRPC.messages_Messages var11 = (TLRPC.messages_Messages)var8;
         MessagesController.getInstance(this.currentAccount).removeDeletedMessagesFromArray(var1, var11.messages);
         boolean var10;
         if (var11.messages.size() == 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         this.processLoadedMedia(var11, var1, var3, var4, var5, 0, var6, var7, var10);
      }

   }

   // $FF: synthetic method
   public void lambda$loadMediaDatabase$63$DataQuery(int param1, long param2, int param4, boolean param5, int param6, int param7, int param8) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadMusic$66$DataQuery(long var1, long var3) {
      ArrayList var5;
      label53: {
         Exception var10000;
         label52: {
            var5 = new ArrayList();
            SQLiteCursor var6;
            boolean var10001;
            if ((int)var1 != 0) {
               try {
                  var6 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v2 WHERE uid = %d AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", var1, var3, 4));
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label52;
               }
            } else {
               try {
                  var6 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v2 WHERE uid = %d AND mid > %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", var1, var3, 4));
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label52;
               }
            }

            while(true) {
               NativeByteBuffer var7;
               label49: {
                  try {
                     if (var6.next()) {
                        var7 = var6.byteBufferValue(0);
                        break label49;
                     }
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break;
                  }

                  try {
                     var6.dispose();
                     break label53;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }
               }

               if (var7 != null) {
                  try {
                     TLRPC.Message var8 = TLRPC.Message.TLdeserialize(var7, var7.readInt32(false), false);
                     var8.readAttachPath(var7, UserConfig.getInstance(this.currentAccount).clientUserId);
                     var7.reuse();
                     if (MessageObject.isMusicMessage(var8)) {
                        var8.id = var6.intValue(1);
                        var8.dialog_id = var1;
                        MessageObject var15 = new MessageObject(this.currentAccount, var8, false);
                        var5.add(0, var15);
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }
               }
            }
         }

         Exception var14 = var10000;
         FileLog.e((Throwable)var14);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$etglwa1VnT5BvHlWsrv51HhlBY4(this, var1, var5));
   }

   // $FF: synthetic method
   public void lambda$loadPinnedMessage$85$DataQuery(long var1, int var3, int var4) {
      this.loadPinnedMessageInternal(var1, var3, var4, false);
   }

   // $FF: synthetic method
   public void lambda$loadPinnedMessageInternal$86$DataQuery(int var1, TLObject var2, TLRPC.TL_error var3) {
      boolean var4;
      label16: {
         var4 = true;
         if (var3 == null) {
            TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var2;
            removeEmptyMessages(var5.messages);
            if (!var5.messages.isEmpty()) {
               ImageLoader.saveMessagesThumbs(var5.messages);
               this.broadcastPinnedMessage((TLRPC.Message)var5.messages.get(0), var5.users, var5.chats, false, false);
               MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var5.users, var5.chats, true, true);
               this.savePinnedMessage((TLRPC.Message)var5.messages.get(0));
               break label16;
            }
         }

         var4 = false;
      }

      if (!var4) {
         MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(var1, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$loadPinnedMessageInternal$87$DataQuery(int var1, TLObject var2, TLRPC.TL_error var3) {
      boolean var4;
      label16: {
         var4 = true;
         if (var3 == null) {
            TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var2;
            removeEmptyMessages(var5.messages);
            if (!var5.messages.isEmpty()) {
               ImageLoader.saveMessagesThumbs(var5.messages);
               this.broadcastPinnedMessage((TLRPC.Message)var5.messages.get(0), var5.users, var5.chats, false, false);
               MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var5.users, var5.chats, true, true);
               this.savePinnedMessage((TLRPC.Message)var5.messages.get(0));
               break label16;
            }
         }

         var4 = false;
      }

      if (!var4) {
         MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(var1, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$loadRecents$11$DataQuery(boolean var1, int var2) {
      byte var3;
      if (var1) {
         var3 = 2;
      } else if (var2 == 0) {
         var3 = 3;
      } else if (var2 == 1) {
         var3 = 4;
      } else {
         var3 = 5;
      }

      Throwable var10000;
      label66: {
         SQLiteCursor var6;
         ArrayList var14;
         boolean var10001;
         try {
            SQLiteDatabase var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            StringBuilder var5 = new StringBuilder();
            var5.append("SELECT document FROM web_recent_v3 WHERE type = ");
            var5.append(var3);
            var5.append(" ORDER BY date DESC");
            var6 = var4.queryFinalized(var5.toString());
            var14 = new ArrayList();
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label66;
         }

         while(true) {
            NativeByteBuffer var7;
            label63: {
               try {
                  while(var6.next()) {
                     if (!var6.isNull(0)) {
                        var7 = var6.byteBufferValue(0);
                        break label63;
                     }
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }

               try {
                  var6.dispose();
                  _$$Lambda$DataQuery$4Ct3KKcQG3d2vX_KGUT_tJNgszQ var17 = new _$$Lambda$DataQuery$4Ct3KKcQG3d2vX_KGUT_tJNgszQ(this, var1, var14, var2);
                  AndroidUtilities.runOnUIThread(var17);
                  return;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }

            if (var7 != null) {
               TLRPC.Document var16;
               try {
                  var16 = TLRPC.Document.TLdeserialize(var7, var7.readInt32(false), false);
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               if (var16 != null) {
                  try {
                     var14.add(var16);
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var7.reuse();
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      Throwable var15 = var10000;
      FileLog.e(var15);
   }

   // $FF: synthetic method
   public void lambda$loadRecents$12$DataQuery(int var1, boolean var2, TLObject var3, TLRPC.TL_error var4) {
      ArrayList var5;
      if (var3 instanceof TLRPC.TL_messages_savedGifs) {
         var5 = ((TLRPC.TL_messages_savedGifs)var3).gifs;
      } else {
         var5 = null;
      }

      this.processLoadedRecentDocuments(var1, var5, var2, 0, true);
   }

   // $FF: synthetic method
   public void lambda$loadRecents$13$DataQuery(int var1, boolean var2, TLObject var3, TLRPC.TL_error var4) {
      ArrayList var5;
      label15: {
         if (var1 == 2) {
            if (var3 instanceof TLRPC.TL_messages_favedStickers) {
               var5 = ((TLRPC.TL_messages_favedStickers)var3).stickers;
               break label15;
            }
         } else if (var3 instanceof TLRPC.TL_messages_recentStickers) {
            var5 = ((TLRPC.TL_messages_recentStickers)var3).stickers;
            break label15;
         }

         var5 = null;
      }

      this.processLoadedRecentDocuments(var1, var5, var2, 0, true);
   }

   // $FF: synthetic method
   public void lambda$loadReplyMessagesForMessages$91$DataQuery(ArrayList var1, long var2, LongSparseArray var4) {
      Exception var10000;
      label102: {
         boolean var10001;
         SQLiteCursor var21;
         try {
            var21 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, m.date, r.random_id FROM randoms as r INNER JOIN messages as m ON r.mid = m.mid WHERE r.random_id IN(%s)", TextUtils.join(",", var1)));
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label102;
         }

         int var10;
         while(true) {
            NativeByteBuffer var5;
            try {
               if (!var21.next()) {
                  break;
               }

               var5 = var21.byteBufferValue(0);
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label102;
            }

            if (var5 != null) {
               TLRPC.Message var6;
               ArrayList var9;
               try {
                  var6 = TLRPC.Message.TLdeserialize(var5, var5.readInt32(false), false);
                  var6.readAttachPath(var5, UserConfig.getInstance(this.currentAccount).clientUserId);
                  var5.reuse();
                  var6.id = var21.intValue(1);
                  var6.date = var21.intValue(2);
                  var6.dialog_id = var2;
                  long var7 = var21.longValue(3);
                  var9 = (ArrayList)var4.get(var7);
                  var4.remove(var7);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label102;
               }

               if (var9 != null) {
                  MessageObject var24;
                  try {
                     var24 = new MessageObject(this.currentAccount, var6, false);
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label102;
                  }

                  var10 = 0;

                  while(true) {
                     try {
                        if (var10 >= var9.size()) {
                           break;
                        }

                        MessageObject var25 = (MessageObject)var9.get(var10);
                        var25.replyMessageObject = var24;
                        var25.messageOwner.reply_to_msg_id = var24.getId();
                        if (var25.isMegagroup()) {
                           var6 = var25.replyMessageObject.messageOwner;
                           var6.flags |= Integer.MIN_VALUE;
                        }
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label102;
                     }

                     ++var10;
                  }
               }
            }
         }

         label105: {
            try {
               var21.dispose();
               if (var4.size() == 0) {
                  break label105;
               }
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label102;
            }

            var10 = 0;

            while(true) {
               try {
                  if (var10 >= var4.size()) {
                     break;
                  }

                  var1 = (ArrayList)var4.valueAt(var10);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label102;
               }

               int var11 = 0;

               while(true) {
                  try {
                     if (var11 >= var1.size()) {
                        break;
                     }

                     ((MessageObject)var1.get(var11)).messageOwner.reply_to_random_id = 0L;
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label102;
                  }

                  ++var11;
               }

               ++var10;
            }
         }

         try {
            _$$Lambda$DataQuery$Xhb2v_0o_Dm3WG47KU6zYke67_0 var23 = new _$$Lambda$DataQuery$Xhb2v_0o_Dm3WG47KU6zYke67_0(this, var2);
            AndroidUtilities.runOnUIThread(var23);
            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var22 = var10000;
      FileLog.e((Throwable)var22);
   }

   // $FF: synthetic method
   public void lambda$loadReplyMessagesForMessages$94$DataQuery(StringBuilder var1, long var2, ArrayList var4, SparseArray var5, int var6) {
      Exception var10000;
      label78: {
         ArrayList var7;
         ArrayList var8;
         ArrayList var9;
         ArrayList var10;
         ArrayList var11;
         SQLiteCursor var12;
         boolean var10001;
         try {
            var7 = new ArrayList();
            var8 = new ArrayList();
            var9 = new ArrayList();
            var10 = new ArrayList();
            var11 = new ArrayList();
            var12 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date FROM messages WHERE mid IN(%s)", var1.toString()));
         } catch (Exception var22) {
            var10000 = var22;
            var10001 = false;
            break label78;
         }

         while(true) {
            NativeByteBuffer var13;
            label81: {
               try {
                  if (var12.next()) {
                     var13 = var12.byteBufferValue(0);
                     break label81;
                  }
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               boolean var14;
               try {
                  var12.dispose();
                  var14 = var10.isEmpty();
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break;
               }

               if (!var14) {
                  try {
                     MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", var10), var8);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  if (!var11.isEmpty()) {
                     MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", var11), var9);
                  }
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break;
               }

               try {
                  this.broadcastReplyMessages(var7, var5, var8, var9, var2, true);
                  if (var4.isEmpty()) {
                     return;
                  }
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break;
               }

               ConnectionsManager var30;
               if (var6 != 0) {
                  try {
                     TLRPC.TL_channels_getMessages var24 = new TLRPC.TL_channels_getMessages();
                     var24.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var6);
                     var24.id = var4;
                     var30 = ConnectionsManager.getInstance(this.currentAccount);
                     _$$Lambda$DataQuery$JW_1UL1JA9UbvXsdwarITz2ppQM var28 = new _$$Lambda$DataQuery$JW_1UL1JA9UbvXsdwarITz2ppQM(this, var5, var2);
                     var30.sendRequest(var24, var28);
                     return;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     TLRPC.TL_messages_getMessages var25 = new TLRPC.TL_messages_getMessages();
                     var25.id = var4;
                     var30 = ConnectionsManager.getInstance(this.currentAccount);
                     _$$Lambda$DataQuery$DP0ftrGTBszIUd99KwPuGmnwqBM var29 = new _$$Lambda$DataQuery$DP0ftrGTBszIUd99KwPuGmnwqBM(this, var5, var2);
                     var30.sendRequest(var25, var29);
                     return;
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break;
                  }
               }
            }

            if (var13 != null) {
               try {
                  TLRPC.Message var27 = TLRPC.Message.TLdeserialize(var13, var13.readInt32(false), false);
                  var27.readAttachPath(var13, UserConfig.getInstance(this.currentAccount).clientUserId);
                  var13.reuse();
                  var27.id = var12.intValue(1);
                  var27.date = var12.intValue(2);
                  var27.dialog_id = var2;
                  MessagesStorage.addUsersAndChatsFromMessage(var27, var10, var11);
                  var7.add(var27);
                  var4.remove(var27.id);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      Exception var26 = var10000;
      FileLog.e((Throwable)var26);
   }

   // $FF: synthetic method
   public void lambda$loadStickers$33$DataQuery(int param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$loadStickers$35$DataQuery(int var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$iGCHItKH69AqIe_44thbbHy_0Sg(this, var3, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$markFaturedStickersByIdAsRead$28$DataQuery(long var1) {
      this.unreadStickerSets.remove(var1);
      this.readingStickerSets.remove(var1);
      this.loadFeaturedHash = this.calcFeaturedStickersHash(this.featuredStickerSets);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.featuredStickersDidLoad);
      this.putFeaturedStickersToCache(this.featuredStickerSets, this.unreadStickerSets, this.loadFeaturedDate, this.loadFeaturedHash);
   }

   // $FF: synthetic method
   public void lambda$null$10$DataQuery(boolean var1, ArrayList var2, int var3) {
      if (var1) {
         this.recentGifs = var2;
         this.loadingRecentGifs = false;
         this.recentGifsLoaded = true;
      } else {
         this.recentStickers[var3] = var2;
         this.loadingRecentStickers[var3] = false;
         this.recentStickersLoaded[var3] = true;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentDocumentsDidLoad, var1, var3);
      this.loadRecents(var3, var1, false, false);
   }

   // $FF: synthetic method
   public void lambda$null$100$DataQuery(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var3;
         if (!var5.messages.isEmpty()) {
            this.saveDraftReplyMessage(var1, (TLRPC.Message)var5.messages.get(0));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$101$DataQuery(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var3;
         if (!var5.messages.isEmpty()) {
            this.saveDraftReplyMessage(var1, (TLRPC.Message)var5.messages.get(0));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$105$DataQuery(TLRPC.Message var1, long var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$107$DataQuery(TLRPC.BotInfo var1, int var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botInfoDidLoad, var1, var2);
   }

   // $FF: synthetic method
   public void lambda$null$111$DataQuery(String var1) {
      Boolean var2 = (Boolean)this.currentFetchingEmoji.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$null$112$DataQuery(String var1) {
      this.currentFetchingEmoji.remove(var1);
      this.fetchNewEmojiKeywords(new String[]{var1});
   }

   // $FF: synthetic method
   public void lambda$null$113$DataQuery(String var1) {
      try {
         SQLitePreparedStatement var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM emoji_keywords_info_v2 WHERE lang = ?");
         var2.bindString(1, var1);
         var2.step();
         var2.dispose();
         _$$Lambda$DataQuery$DNPRuAZm79Q9W4IPII5JfioAwcA var4 = new _$$Lambda$DataQuery$DNPRuAZm79Q9W4IPII5JfioAwcA(this, var1);
         AndroidUtilities.runOnUIThread(var4);
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$114$DataQuery(String var1) {
      Boolean var2 = (Boolean)this.currentFetchingEmoji.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$null$115$DataQuery(int var1, String var2, String var3, TLObject var4, TLRPC.TL_error var5) {
      if (var4 != null) {
         TLRPC.TL_emojiKeywordsDifference var6 = (TLRPC.TL_emojiKeywordsDifference)var4;
         if (var1 != -1 && !var6.lang_code.equals(var2)) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$tmdFPTFKuPJBcR8hsRDSJe8XFo4(this, var3));
         } else {
            this.putEmojiKeywords(var3, var6);
         }
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$9eXVS7UU9pt2ANgZ__XiV0PmN8Y(this, var3));
      }

   }

   // $FF: synthetic method
   public void lambda$null$117$DataQuery(String var1) {
      this.currentFetchingEmoji.remove(var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.newEmojiSuggestionsAvailable, var1);
   }

   // $FF: synthetic method
   public void lambda$null$119$DataQuery(String[] var1, DataQuery.KeywordResultCallback var2, ArrayList var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (this.currentFetchingEmoji.get(var1[var4]) != null) {
            return;
         }
      }

      var2.run(var3, (String)null);
   }

   // $FF: synthetic method
   public void lambda$null$18$DataQuery(TLObject var1, TLRPC.TL_messages_getFeaturedStickers var2) {
      if (var1 instanceof TLRPC.TL_messages_featuredStickers) {
         TLRPC.TL_messages_featuredStickers var3 = (TLRPC.TL_messages_featuredStickers)var1;
         this.processLoadedFeaturedStickers(var3.sets, var3.unread, false, (int)(System.currentTimeMillis() / 1000L), var3.hash);
      } else {
         this.processLoadedFeaturedStickers((ArrayList)null, (ArrayList)null, false, (int)(System.currentTimeMillis() / 1000L), var2.hash);
      }

   }

   // $FF: synthetic method
   public void lambda$null$21$DataQuery(ArrayList var1, int var2) {
      if (var1 != null && var2 != 0) {
         this.loadFeaturedHash = var2;
      }

      this.loadFeaturedStickers(false, false);
   }

   // $FF: synthetic method
   public void lambda$null$22$DataQuery(ArrayList var1, LongSparseArray var2, ArrayList var3, int var4, int var5) {
      this.unreadStickerSets = var1;
      this.featuredStickerSetsById = var2;
      this.featuredStickerSets = var3;
      this.loadFeaturedHash = var4;
      this.loadFeaturedDate = var5;
      this.loadStickers(3, true, false);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.featuredStickersDidLoad);
   }

   // $FF: synthetic method
   public void lambda$null$23$DataQuery(int var1) {
      this.loadFeaturedDate = var1;
   }

   // $FF: synthetic method
   public void lambda$null$29$DataQuery(TLRPC.TL_error var1, TLObject var2, int var3) {
      if (var1 == null) {
         TLRPC.TL_messages_archivedStickers var4 = (TLRPC.TL_messages_archivedStickers)var2;
         this.archivedStickersCount[var3] = var4.count;
         Editor var5 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
         StringBuilder var6 = new StringBuilder();
         var6.append("archivedStickersCount");
         var6.append(var3);
         var5.putInt(var6.toString(), var4.count).commit();
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.archivedStickersCountDidLoad, var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$31$DataQuery(TLObject var1, ArrayList var2, int var3, LongSparseArray var4, TLRPC.StickerSet var5, TLRPC.TL_messages_allStickers var6, int var7) {
      TLRPC.TL_messages_stickerSet var9 = (TLRPC.TL_messages_stickerSet)var1;
      var2.set(var3, var9);
      var4.put(var5.id, var9);
      if (var4.size() == var6.sets.size()) {
         int var8;
         for(var3 = 0; var3 < var2.size(); var3 = var8 + 1) {
            var8 = var3;
            if (var2.get(var3) == null) {
               var2.remove(var3);
               var8 = var3 - 1;
            }
         }

         this.processLoadedStickers(var7, var2, false, (int)(System.currentTimeMillis() / 1000L), var6.hash);
      }

   }

   // $FF: synthetic method
   public void lambda$null$34$DataQuery(TLObject var1, int var2, int var3) {
      if (var1 instanceof TLRPC.TL_messages_allStickers) {
         this.processLoadStickersResponse(var2, (TLRPC.TL_messages_allStickers)var1);
      } else {
         this.processLoadedStickers(var2, (ArrayList)null, false, (int)(System.currentTimeMillis() / 1000L), var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$38$DataQuery(ArrayList var1, int var2, int var3) {
      if (var1 != null && var2 != 0) {
         this.loadHash[var3] = var2;
      }

      this.loadStickers(var3, false, false);
   }

   // $FF: synthetic method
   public void lambda$null$39$DataQuery(int var1, LongSparseArray var2, HashMap var3, ArrayList var4, int var5, int var6, HashMap var7, LongSparseArray var8) {
      int var9;
      for(var9 = 0; var9 < this.stickerSets[var1].size(); ++var9) {
         TLRPC.StickerSet var10 = ((TLRPC.TL_messages_stickerSet)this.stickerSets[var1].get(var9)).set;
         this.stickerSetsById.remove(var10.id);
         this.installedStickerSetsById.remove(var10.id);
         this.stickerSetsByName.remove(var10.short_name);
      }

      for(var9 = 0; var9 < var2.size(); ++var9) {
         this.stickerSetsById.put(var2.keyAt(var9), var2.valueAt(var9));
         if (var1 != 3) {
            this.installedStickerSetsById.put(var2.keyAt(var9), var2.valueAt(var9));
         }
      }

      this.stickerSetsByName.putAll(var3);
      this.stickerSets[var1] = var4;
      this.loadHash[var1] = var5;
      this.loadDate[var1] = var6;
      if (var1 == 0) {
         this.allStickers = var7;
         this.stickersByEmoji = var8;
      } else if (var1 == 3) {
         this.allStickersFeatured = var7;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, var1);
   }

   // $FF: synthetic method
   public void lambda$null$40$DataQuery(int var1, int var2) {
      this.loadDate[var1] = var2;
   }

   // $FF: synthetic method
   public void lambda$null$42$DataQuery(TLObject var1, int var2, int var3, BaseFragment var4, boolean var5) {
      if (var1 instanceof TLRPC.TL_messages_stickerSetInstallResultArchive) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadArchivedStickers, var2);
         if (var3 != 1 && var4 != null && var4.getParentActivity() != null) {
            Activity var6 = var4.getParentActivity();
            BaseFragment var7;
            if (var5) {
               var7 = var4;
            } else {
               var7 = null;
            }

            var4.showDialog((new StickersArchiveAlert(var6, var7, ((TLRPC.TL_messages_stickerSetInstallResultArchive)var1).sets)).create());
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$43$DataQuery(int var1) {
      this.loadStickers(var1, false, false);
   }

   // $FF: synthetic method
   public void lambda$null$45$DataQuery(TLRPC.TL_error var1, TLRPC.StickerSet var2, Context var3, int var4) {
      label39: {
         Exception var10000;
         boolean var10001;
         if (var1 != null) {
            try {
               Toast.makeText(var3, LocaleController.getString("ErrorOccurred", 2131559375), 0).show();
               break label39;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         } else {
            label38: {
               try {
                  if (var2.masks) {
                     Toast.makeText(var3, LocaleController.getString("MasksRemoved", 2131559817), 0).show();
                     break label39;
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label38;
               }

               try {
                  Toast.makeText(var3, LocaleController.getString("StickersRemoved", 2131560812), 0).show();
                  break label39;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      }

      this.loadStickers(var4, false, true);
   }

   // $FF: synthetic method
   public void lambda$null$47$DataQuery(long var1, TLObject var3, TLRPC.TL_messages_search var4, long var5, int var7, int var8, TLRPC.User var9) {
      if (this.lastMergeDialogId == var1) {
         this.mergeReqId = 0;
         if (var3 != null) {
            TLRPC.messages_Messages var10 = (TLRPC.messages_Messages)var3;
            this.messagesSearchEndReached[1] = var10.messages.isEmpty();
            int[] var12 = this.messagesSearchCount;
            int var11;
            if (var10 instanceof TLRPC.TL_messages_messagesSlice) {
               var11 = var10.count;
            } else {
               var11 = var10.messages.size();
            }

            var12[1] = var11;
            this.searchMessagesInChat(var4.q, var5, var1, var7, var8, true, var9);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$49$DataQuery(int var1, TLObject var2, TLRPC.TL_messages_search var3, long var4, long var6, int var8, long var9, TLRPC.User var11) {
      if (var1 == this.lastReqId) {
         this.reqId = 0;
         if (var2 != null) {
            TLRPC.messages_Messages var20 = (TLRPC.messages_Messages)var2;

            int var13;
            for(var1 = 0; var1 < var20.messages.size(); var1 = var13 + 1) {
               TLRPC.Message var12 = (TLRPC.Message)var20.messages.get(var1);
               if (!(var12 instanceof TLRPC.TL_messageEmpty)) {
                  var13 = var1;
                  if (!(var12.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                     continue;
                  }
               }

               var20.messages.remove(var1);
               var13 = var1 - 1;
            }

            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var20.users, var20.chats, true, true);
            MessagesController.getInstance(this.currentAccount).putUsers(var20.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(var20.chats, false);
            if (var3.offset_id == 0 && var4 == var6) {
               this.lastReturnedNum = 0;
               this.searchResultMessages.clear();
               this.searchResultMessagesMap[0].clear();
               this.searchResultMessagesMap[1].clear();
               this.messagesSearchCount[0] = 0;
            }

            var13 = 0;

            boolean var19;
            for(var19 = false; var13 < Math.min(var20.messages.size(), 20); var19 = true) {
               TLRPC.Message var24 = (TLRPC.Message)var20.messages.get(var13);
               MessageObject var29 = new MessageObject(this.currentAccount, var24, false);
               this.searchResultMessages.add(var29);
               SparseArray[] var25 = this.searchResultMessagesMap;
               byte var21;
               if (var4 == var6) {
                  var21 = 0;
               } else {
                  var21 = 1;
               }

               var25[var21].put(var29.getId(), var29);
               ++var13;
            }

            boolean[] var26 = this.messagesSearchEndReached;
            byte var30;
            if (var4 == var6) {
               var30 = 0;
            } else {
               var30 = 1;
            }

            boolean var14;
            if (var20.messages.size() != 21) {
               var14 = true;
            } else {
               var14 = false;
            }

            var26[var30] = var14;
            int[] var27 = this.messagesSearchCount;
            if (var4 == var6) {
               var30 = 0;
            } else {
               var30 = 1;
            }

            int var15;
            if (!(var20 instanceof TLRPC.TL_messages_messagesSlice) && !(var20 instanceof TLRPC.TL_messages_channelMessages)) {
               var15 = var20.messages.size();
            } else {
               var15 = var20.count;
            }

            var27[var30] = var15;
            if (this.searchResultMessages.isEmpty()) {
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatSearchResultsAvailable, var8, 0, this.getMask(), 0L, 0, 0);
            } else if (var19) {
               if (this.lastReturnedNum >= this.searchResultMessages.size()) {
                  this.lastReturnedNum = this.searchResultMessages.size() - 1;
               }

               MessageObject var28 = (MessageObject)this.searchResultMessages.get(this.lastReturnedNum);
               NotificationCenter var22 = NotificationCenter.getInstance(this.currentAccount);
               var1 = NotificationCenter.chatSearchResultsAvailable;
               int var16 = var28.getId();
               var15 = this.getMask();
               long var17 = var28.getDialogId();
               var13 = this.lastReturnedNum;
               var27 = this.messagesSearchCount;
               var22.postNotificationName(var1, var8, var16, var15, var17, var13, var27[0] + var27[1]);
            }

            if (var4 == var6) {
               boolean[] var23 = this.messagesSearchEndReached;
               if (var23[0] && var9 != 0L && !var23[1]) {
                  this.searchMessagesInChat(this.lastSearchQuery, var6, var9, var8, 0, true, var11);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$DataQuery(TLRPC.TL_messages_stickerSet var1) {
      this.groupStickerSets.put(var1.set.id, var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.groupStickersDidLoad, var1.set.id);
   }

   // $FF: synthetic method
   public void lambda$null$52$DataQuery(long var1, int[] var3) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mediaCountsDidLoad, var1, var3);
   }

   // $FF: synthetic method
   public void lambda$null$53$DataQuery(long var1, int[] var3) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mediaCountsDidLoad, var1, var3);
   }

   // $FF: synthetic method
   public void lambda$null$54$DataQuery(int[] var1, int var2, long var3, TLObject var5, TLRPC.TL_error var6) {
      boolean var7 = false;
      if (var6 == null) {
         TLRPC.messages_Messages var9 = (TLRPC.messages_Messages)var5;
         if (var9 instanceof TLRPC.TL_messages_messages) {
            var1[var2] = var9.messages.size();
         } else {
            var1[var2] = var9.count;
         }

         this.putMediaCountDatabase(var3, var2, var1[var2]);
      } else {
         var1[var2] = 0;
      }

      var2 = 0;

      boolean var8;
      while(true) {
         if (var2 >= var1.length) {
            var8 = true;
            break;
         }

         if (var1[var2] == -1) {
            var8 = var7;
            break;
         }

         ++var2;
      }

      if (var8) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$D0edyBe7uzUZgJEes_37nxNPUP8(this, var3, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$null$55$DataQuery(long var1, int[] var3) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mediaCountsDidLoad, var1, var3);
   }

   // $FF: synthetic method
   public void lambda$null$57$DataQuery(TLRPC.messages_Messages var1) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1.users, false);
      MessagesController.getInstance(this.currentAccount).putChats(var1.chats, false);
   }

   // $FF: synthetic method
   public void lambda$null$65$DataQuery(long var1, ArrayList var3) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.musicDidLoad, var1, var3);
   }

   // $FF: synthetic method
   public void lambda$null$68$DataQuery(ArrayList var1, ArrayList var2, ArrayList var3, ArrayList var4) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(this.currentAccount).putChats(var2, true);
      this.loading = false;
      this.loaded = true;
      this.hints = var3;
      this.inlineBots = var4;
      this.buildShortcuts();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
      if (Math.abs(UserConfig.getInstance(this.currentAccount).lastHintsSyncTime - (int)(System.currentTimeMillis() / 1000L)) >= 86400) {
         this.loadHints(false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$DataQuery(TLRPC.TL_messages_stickerSet var1) {
      this.groupStickerSets.put(var1.set.id, var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.groupStickersDidLoad, var1.set.id);
   }

   // $FF: synthetic method
   public void lambda$null$70$DataQuery() {
      UserConfig.getInstance(this.currentAccount).suggestContacts = true;
      UserConfig.getInstance(this.currentAccount).lastHintsSyncTime = (int)(System.currentTimeMillis() / 1000L);
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
   }

   // $FF: synthetic method
   public void lambda$null$71$DataQuery(TLRPC.TL_contacts_topPeers var1) {
      Exception var10000;
      label88: {
         SQLitePreparedStatement var2;
         boolean var10001;
         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
            MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var1.users, var1.chats, false, false);
            var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO chat_hints VALUES(?, ?, ?, ?)");
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label88;
         }

         int var3 = 0;

         while(true) {
            TLRPC.TL_topPeerCategoryPeers var4;
            byte var5;
            label78: {
               label77: {
                  try {
                     if (var3 >= var1.categories.size()) {
                        break;
                     }

                     var4 = (TLRPC.TL_topPeerCategoryPeers)var1.categories.get(var3);
                     if (var4.category instanceof TLRPC.TL_topPeerCategoryBotsInline) {
                        break label77;
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label88;
                  }

                  var5 = 0;
                  break label78;
               }

               var5 = 1;
            }

            int var6 = 0;

            while(true) {
               TLRPC.TL_topPeer var7;
               int var8;
               label93: {
                  try {
                     if (var6 >= var4.peers.size()) {
                        break;
                     }

                     var7 = (TLRPC.TL_topPeer)var4.peers.get(var6);
                     if (var7.peer instanceof TLRPC.TL_peerUser) {
                        var8 = var7.peer.user_id;
                        break label93;
                     }
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label88;
                  }

                  label58: {
                     try {
                        if (var7.peer instanceof TLRPC.TL_peerChat) {
                           var8 = var7.peer.chat_id;
                           break label58;
                        }
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label88;
                     }

                     try {
                        var8 = var7.peer.channel_id;
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label88;
                     }
                  }

                  var8 = -var8;
               }

               try {
                  var2.requery();
                  var2.bindInteger(1, var8);
                  var2.bindInteger(2, var5);
                  var2.bindDouble(3, var7.rating);
                  var2.bindInteger(4, 0);
                  var2.step();
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label88;
               }

               ++var6;
            }

            ++var3;
         }

         try {
            var2.dispose();
            MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
            _$$Lambda$DataQuery$SlSBEOrYr_Gu3TZ2prgLfFC7QFU var17 = new _$$Lambda$DataQuery$SlSBEOrYr_Gu3TZ2prgLfFC7QFU(this);
            AndroidUtilities.runOnUIThread(var17);
            return;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
   }

   // $FF: synthetic method
   public void lambda$null$72$DataQuery(TLObject var1) {
      TLRPC.TL_contacts_topPeers var2 = (TLRPC.TL_contacts_topPeers)var1;
      MessagesController.getInstance(this.currentAccount).putUsers(var2.users, false);
      MessagesController.getInstance(this.currentAccount).putChats(var2.chats, false);

      for(int var3 = 0; var3 < var2.categories.size(); ++var3) {
         TLRPC.TL_topPeerCategoryPeers var6 = (TLRPC.TL_topPeerCategoryPeers)var2.categories.get(var3);
         if (var6.category instanceof TLRPC.TL_topPeerCategoryBotsInline) {
            this.inlineBots = var6.peers;
            UserConfig.getInstance(this.currentAccount).botRatingLoadTime = (int)(System.currentTimeMillis() / 1000L);
         } else {
            this.hints = var6.peers;
            int var4 = UserConfig.getInstance(this.currentAccount).getClientUserId();

            for(int var5 = 0; var5 < this.hints.size(); ++var5) {
               if (((TLRPC.TL_topPeer)this.hints.get(var5)).peer.user_id == var4) {
                  this.hints.remove(var5);
                  break;
               }
            }

            UserConfig.getInstance(this.currentAccount).ratingLoadTime = (int)(System.currentTimeMillis() / 1000L);
         }
      }

      UserConfig.getInstance(this.currentAccount).saveConfig(false);
      this.buildShortcuts();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU(this, var2));
   }

   // $FF: synthetic method
   public void lambda$null$73$DataQuery() {
      UserConfig.getInstance(this.currentAccount).suggestContacts = false;
      UserConfig.getInstance(this.currentAccount).lastHintsSyncTime = (int)(System.currentTimeMillis() / 1000L);
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
      this.clearTopPeers();
   }

   // $FF: synthetic method
   public void lambda$null$80$DataQuery(int var1, double var2, long var4) {
      int var6 = 0;

      TLRPC.TL_topPeer var7;
      TLRPC.TL_topPeer var11;
      while(true) {
         if (var6 >= this.hints.size()) {
            var11 = null;
            break;
         }

         var7 = (TLRPC.TL_topPeer)this.hints.get(var6);
         if (var1 < 0) {
            TLRPC.Peer var8 = var7.peer;
            int var9 = var8.chat_id;
            int var10 = -var1;
            var11 = var7;
            if (var9 == var10) {
               break;
            }

            var11 = var7;
            if (var8.channel_id == var10) {
               break;
            }
         }

         if (var1 > 0 && var7.peer.user_id == var1) {
            var11 = var7;
            break;
         }

         ++var6;
      }

      var7 = var11;
      if (var11 == null) {
         var7 = new TLRPC.TL_topPeer();
         if (var1 > 0) {
            var7.peer = new TLRPC.TL_peerUser();
            var7.peer.user_id = var1;
         } else {
            var7.peer = new TLRPC.TL_peerChat();
            var7.peer.chat_id = -var1;
         }

         this.hints.add(var7);
      }

      double var12 = var7.rating;
      double var14 = (double)MessagesController.getInstance(this.currentAccount).ratingDecay;
      Double.isNaN(var14);
      var7.rating = var12 + Math.exp(var2 / var14);
      Collections.sort(this.hints, _$$Lambda$DataQuery$12fipD1xacUTdjkG1gIFzgfExOI.INSTANCE);
      this.savePeer((int)var4, 0, var7.rating);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
   }

   // $FF: synthetic method
   public void lambda$null$90$DataQuery(long var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replyMessagesDidLoad, var1);
   }

   // $FF: synthetic method
   public void lambda$null$92$DataQuery(SparseArray var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         TLRPC.messages_Messages var6 = (TLRPC.messages_Messages)var4;
         removeEmptyMessages(var6.messages);
         ImageLoader.saveMessagesThumbs(var6.messages);
         this.broadcastReplyMessages(var6.messages, var1, var6.users, var6.chats, var2, false);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6.users, var6.chats, true, true);
         this.saveReplyMessages(var1, var6.messages);
      }

   }

   // $FF: synthetic method
   public void lambda$null$93$DataQuery(SparseArray var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         TLRPC.messages_Messages var6 = (TLRPC.messages_Messages)var4;
         removeEmptyMessages(var6.messages);
         ImageLoader.saveMessagesThumbs(var6.messages);
         this.broadcastReplyMessages(var6.messages, var1, var6.users, var6.chats, var2, false);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6.users, var6.chats, true, true);
         this.saveReplyMessages(var1, var6.messages);
      }

   }

   // $FF: synthetic method
   public void lambda$null$97$DataQuery() {
      UserConfig.getInstance(this.currentAccount).draftsLoaded = true;
      this.loadingDrafts = false;
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
   }

   // $FF: synthetic method
   public void lambda$processLoadStickersResponse$32$DataQuery(ArrayList var1, int var2, LongSparseArray var3, TLRPC.StickerSet var4, TLRPC.TL_messages_allStickers var5, int var6, TLObject var7, TLRPC.TL_error var8) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$m9Jrl7_U__SUx_Gpw56kknTexVQ(this, var7, var1, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$processLoadedFeaturedStickers$20$DataQuery() {
      this.loadingFeaturedStickers = false;
      this.featuredStickersLoaded = true;
   }

   // $FF: synthetic method
   public void lambda$processLoadedFeaturedStickers$24$DataQuery(boolean var1, ArrayList var2, int var3, int var4, ArrayList var5) {
      long var6 = 1000L;
      if (var1 && (var2 == null || Math.abs(System.currentTimeMillis() / 1000L - (long)var3) >= 3600L) || !var1 && var2 == null && var4 == 0) {
         _$$Lambda$DataQuery$kRg5HaPmcNQUyKbUY_yQPqEg7dE var8 = new _$$Lambda$DataQuery$kRg5HaPmcNQUyKbUY_yQPqEg7dE(this, var2, var4);
         if (var2 != null || var1) {
            var6 = 0L;
         }

         AndroidUtilities.runOnUIThread(var8, var6);
         if (var2 == null) {
            return;
         }
      }

      int var9 = 0;
      if (var2 != null) {
         Throwable var10000;
         label58: {
            ArrayList var10;
            boolean var10001;
            LongSparseArray var18;
            try {
               var10 = new ArrayList();
               var18 = new LongSparseArray();
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label58;
            }

            while(true) {
               label84: {
                  try {
                     if (var9 < var2.size()) {
                        TLRPC.StickerSetCovered var11 = (TLRPC.StickerSetCovered)var2.get(var9);
                        var10.add(var11);
                        var18.put(var11.set.id, var11);
                        break label84;
                     }
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break;
                  }

                  if (!var1) {
                     try {
                        this.putFeaturedStickersToCache(var10, var5, var3, var4);
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     _$$Lambda$DataQuery$dAuSsiUmfJ4Spuw_RVTqCszQl9Y var17 = new _$$Lambda$DataQuery$dAuSsiUmfJ4Spuw_RVTqCszQl9Y(this, var5, var18, var10, var4, var3);
                     AndroidUtilities.runOnUIThread(var17);
                     return;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }
               }

               ++var9;
            }
         }

         Throwable var16 = var10000;
         FileLog.e(var16);
      } else if (!var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$W2Cvj9Df_M9tvBcZ_35ZTktPTCg(this, var3));
         this.putFeaturedStickersToCache((ArrayList)null, (ArrayList)null, var3, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedMedia$59$DataQuery(TLRPC.messages_Messages var1, int var2, long var3, ArrayList var5, int var6, int var7, boolean var8) {
      int var9 = var1.count;
      MessagesController var10 = MessagesController.getInstance(this.currentAccount);
      ArrayList var11 = var1.users;
      boolean var12;
      if (var2 != 0) {
         var12 = true;
      } else {
         var12 = false;
      }

      var10.putUsers(var11, var12);
      MessagesController var13 = MessagesController.getInstance(this.currentAccount);
      ArrayList var14 = var1.chats;
      if (var2 != 0) {
         var12 = true;
      } else {
         var12 = false;
      }

      var13.putChats(var14, var12);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mediaDidLoad, var3, var9, var5, var6, var7, var8);
   }

   // $FF: synthetic method
   public void lambda$processLoadedMediaCount$60$DataQuery(long var1, boolean var3, int var4, int var5, int var6, int var7) {
      int var8 = (int)var1;
      boolean var9;
      if (var3 && (var4 == -1 || var4 == 0 && var5 == 2) && var8 != 0) {
         var9 = true;
      } else {
         var9 = false;
      }

      if (var9 || var6 == 1 && var8 != 0) {
         this.getMediaCount(var1, var5, var7, false);
      }

      if (!var9) {
         if (!var3) {
            this.putMediaCountDatabase(var1, var5, var4);
         }

         NotificationCenter var10 = NotificationCenter.getInstance(this.currentAccount);
         var7 = NotificationCenter.mediaCountDidLoad;
         var6 = var4;
         if (var3) {
            var6 = var4;
            if (var4 == -1) {
               var6 = 0;
            }
         }

         var10.postNotificationName(var7, var1, var6, var3, var5);
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedRecentDocuments$14$DataQuery(boolean var1, int var2, ArrayList var3, boolean var4, int var5) {
      Exception var10000;
      label119: {
         SQLiteDatabase var6;
         boolean var10001;
         try {
            var6 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
            break label119;
         }

         int var7;
         if (var1) {
            try {
               var7 = MessagesController.getInstance(this.currentAccount).maxRecentGifsCount;
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label119;
            }
         } else if (var2 == 2) {
            try {
               var7 = MessagesController.getInstance(this.currentAccount).maxFaveStickersCount;
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label119;
            }
         } else {
            try {
               var7 = MessagesController.getInstance(this.currentAccount).maxRecentStickersCount;
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label119;
            }
         }

         SQLitePreparedStatement var8;
         int var9;
         try {
            var6.beginTransaction();
            var8 = var6.executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            var9 = var3.size();
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label119;
         }

         byte var25;
         if (var1) {
            var25 = 2;
         } else if (var2 == 0) {
            var25 = 3;
         } else if (var2 == 1) {
            var25 = 4;
         } else {
            var25 = 5;
         }

         if (var4) {
            try {
               StringBuilder var10 = new StringBuilder();
               var10.append("DELETE FROM web_recent_v3 WHERE type = ");
               var10.append(var25);
               var6.executeFast(var10.toString()).stepThis().dispose();
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label119;
            }
         }

         for(int var11 = 0; var11 < var9 && var11 != var7; ++var11) {
            TLRPC.Document var28;
            try {
               var28 = (TLRPC.Document)var3.get(var11);
               var8.requery();
               StringBuilder var12 = new StringBuilder();
               var12.append("");
               var12.append(var28.id);
               var8.bindString(1, var12.toString());
               var8.bindInteger(2, var25);
               var8.bindString(3, "");
               var8.bindString(4, "");
               var8.bindString(5, "");
               var8.bindInteger(6, 0);
               var8.bindInteger(7, 0);
               var8.bindInteger(8, 0);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label119;
            }

            int var13;
            if (var5 != 0) {
               var13 = var5;
            } else {
               var13 = var9 - var11;
            }

            try {
               var8.bindInteger(9, var13);
               NativeByteBuffer var29 = new NativeByteBuffer(var28.getObjectSize());
               var28.serializeToStream(var29);
               var8.bindByteBuffer(10, (NativeByteBuffer)var29);
               var8.step();
               var29.reuse();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label119;
            }
         }

         try {
            var8.dispose();
            var6.commitTransaction();
            if (var3.size() < var7) {
               return;
            }

            var6.beginTransaction();
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label119;
         }

         while(true) {
            try {
               if (var7 >= var3.size()) {
                  break;
               }

               StringBuilder var27 = new StringBuilder();
               var27.append("DELETE FROM web_recent_v3 WHERE id = '");
               var27.append(((TLRPC.Document)var3.get(var7)).id);
               var27.append("' AND type = ");
               var27.append(var25);
               var6.executeFast(var27.toString()).stepThis().dispose();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label119;
            }

            ++var7;
         }

         try {
            var6.commitTransaction();
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var26 = var10000;
      FileLog.e((Throwable)var26);
   }

   // $FF: synthetic method
   public void lambda$processLoadedRecentDocuments$15$DataQuery(boolean var1, int var2, ArrayList var3) {
      Editor var4 = MessagesController.getEmojiSettings(this.currentAccount).edit();
      if (var1) {
         this.loadingRecentGifs = false;
         this.recentGifsLoaded = true;
         var4.putLong("lastGifLoadTime", System.currentTimeMillis()).commit();
      } else {
         this.loadingRecentStickers[var2] = false;
         this.recentStickersLoaded[var2] = true;
         if (var2 == 0) {
            var4.putLong("lastStickersLoadTime", System.currentTimeMillis()).commit();
         } else if (var2 == 1) {
            var4.putLong("lastStickersLoadTimeMask", System.currentTimeMillis()).commit();
         } else {
            var4.putLong("lastStickersLoadTimeFavs", System.currentTimeMillis()).commit();
         }
      }

      if (var3 != null) {
         if (var1) {
            this.recentGifs = var3;
         } else {
            this.recentStickers[var2] = var3;
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentDocumentsDidLoad, var1, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedStickers$37$DataQuery(int var1) {
      this.loadingStickers[var1] = false;
      this.stickersLoaded[var1] = true;
   }

   // $FF: synthetic method
   public void lambda$processLoadedStickers$41$DataQuery(boolean var1, ArrayList var2, int var3, int var4, int var5) {
      long var7 = 1000L;
      if (var1 && (var2 == null || Math.abs(System.currentTimeMillis() / 1000L - (long)var3) >= 3600L) || !var1 && var2 == null && var4 == 0) {
         _$$Lambda$DataQuery$p__TrjJXc0wEGRq96cujq9LPyXc var9 = new _$$Lambda$DataQuery$p__TrjJXc0wEGRq96cujq9LPyXc(this, var2, var4, var5);
         if (var2 != null || var1) {
            var7 = 0L;
         }

         AndroidUtilities.runOnUIThread(var9, var7);
         if (var2 == null) {
            return;
         }
      }

      if (var2 != null) {
         Throwable var10000;
         label210: {
            LongSparseArray var6;
            ArrayList var10;
            LongSparseArray var11;
            LongSparseArray var12;
            HashMap var13;
            boolean var10001;
            HashMap var40;
            try {
               var10 = new ArrayList();
               var6 = new LongSparseArray();
               var40 = new HashMap();
               var11 = new LongSparseArray();
               var12 = new LongSparseArray();
               var13 = new HashMap();
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label210;
            }

            int var14 = 0;

            while(true) {
               TLRPC.TL_messages_stickerSet var15;
               try {
                  if (var14 >= var2.size()) {
                     break;
                  }

                  var15 = (TLRPC.TL_messages_stickerSet)var2.get(var14);
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label210;
               }

               HashMap var16;
               HashMap var17;
               LongSparseArray var18;
               if (var15 == null) {
                  var16 = var13;
                  var17 = var40;
                  var18 = var6;
               } else {
                  label212: {
                     try {
                        var10.add(var15);
                        var6.put(var15.set.id, var15);
                        var40.put(var15.set.short_name, var15);
                     } catch (Throwable var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label210;
                     }

                     int var19 = 0;

                     while(true) {
                        TLRPC.Document var42;
                        try {
                           if (var19 >= var15.documents.size()) {
                              break;
                           }

                           var42 = (TLRPC.Document)var15.documents.get(var19);
                        } catch (Throwable var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label210;
                        }

                        if (var42 != null) {
                           label220: {
                              try {
                                 if (var42 instanceof TLRPC.TL_documentEmpty) {
                                    break label220;
                                 }
                              } catch (Throwable var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label210;
                              }

                              try {
                                 var12.put(var42.id, var42);
                              } catch (Throwable var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label210;
                              }
                           }
                        }

                        ++var19;
                     }

                     var18 = var6;
                     var17 = var40;
                     var16 = var13;

                     try {
                        if (var15.set.archived) {
                           break label212;
                        }
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label210;
                     }

                     var19 = 0;

                     while(true) {
                        var18 = var6;
                        var17 = var40;
                        var16 = var13;

                        TLRPC.TL_stickerPack var48;
                        try {
                           if (var19 >= var15.packs.size()) {
                              break;
                           }

                           var48 = (TLRPC.TL_stickerPack)var15.packs.get(var19);
                        } catch (Throwable var30) {
                           var10000 = var30;
                           var10001 = false;
                           break label210;
                        }

                        HashMap var39;
                        LongSparseArray var41;
                        label223: {
                           if (var48 != null) {
                              label221: {
                                 try {
                                    if (var48.emoticon == null) {
                                       break label221;
                                    }
                                 } catch (Throwable var32) {
                                    var10000 = var32;
                                    var10001 = false;
                                    break label210;
                                 }

                                 ArrayList var44;
                                 try {
                                    var48.emoticon = var48.emoticon.replace("️", "");
                                    var44 = (ArrayList)var13.get(var48.emoticon);
                                 } catch (Throwable var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break label210;
                                 }

                                 ArrayList var43 = var44;
                                 if (var44 == null) {
                                    try {
                                       var43 = new ArrayList();
                                       var13.put(var48.emoticon, var43);
                                    } catch (Throwable var25) {
                                       var10000 = var25;
                                       var10001 = false;
                                       break label210;
                                    }
                                 }

                                 int var20 = 0;

                                 while(true) {
                                    Long var45;
                                    try {
                                       if (var20 >= var48.documents.size()) {
                                          break;
                                       }

                                       var45 = (Long)var48.documents.get(var20);
                                       if (var11.indexOfKey(var45) < 0) {
                                          var11.put(var45, var48.emoticon);
                                       }
                                    } catch (Throwable var31) {
                                       var10000 = var31;
                                       var10001 = false;
                                       break label210;
                                    }

                                    TLRPC.Document var47;
                                    try {
                                       var47 = (TLRPC.Document)var12.get(var45);
                                    } catch (Throwable var24) {
                                       var10000 = var24;
                                       var10001 = false;
                                       break label210;
                                    }

                                    if (var47 != null) {
                                       try {
                                          var43.add(var47);
                                       } catch (Throwable var23) {
                                          var10000 = var23;
                                          var10001 = false;
                                          break label210;
                                       }
                                    }

                                    ++var20;
                                 }

                                 var41 = var6;
                                 var39 = var13;
                                 break label223;
                              }
                           }

                           var39 = var13;
                           var41 = var6;
                        }

                        ++var19;
                        var6 = var41;
                        var13 = var39;
                     }
                  }
               }

               ++var14;
               var6 = var18;
               var40 = var17;
               var13 = var16;
            }

            if (!var1) {
               try {
                  this.putStickersToCache(var5, var10, var3, var4);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label210;
               }
            }

            try {
               _$$Lambda$DataQuery$tJcdmi2t6adWqXKlQnAh02D50rs var38 = new _$$Lambda$DataQuery$tJcdmi2t6adWqXKlQnAh02D50rs(this, var5, var6, var40, var10, var4, var3, var13, var11);
               AndroidUtilities.runOnUIThread(var38);
               return;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
            }
         }

         Throwable var37 = var10000;
         FileLog.e(var37);
      } else if (!var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$8k_rAECt_51WNomiJ13t8JNjAGU(this, var5, var3));
         this.putStickersToCache(var5, (ArrayList)null, var3, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$putBotInfo$110$DataQuery(TLRPC.BotInfo var1) {
      try {
         SQLitePreparedStatement var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO bot_info(uid, info) VALUES(?, ?)");
         var2.requery();
         NativeByteBuffer var3 = new NativeByteBuffer(var1.getObjectSize());
         var1.serializeToStream(var3);
         var2.bindInteger(1, var1.user_id);
         var2.bindByteBuffer(2, (NativeByteBuffer)var3);
         var2.step();
         var3.reuse();
         var2.dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$putBotKeyboard$109$DataQuery(long var1, TLRPC.Message var3) {
      TLRPC.Message var4 = (TLRPC.Message)this.botKeyboards.get(var1);
      this.botKeyboards.put(var1, var3);
      if (var4 != null) {
         this.botKeyboardsByMids.delete(var4.id);
      }

      this.botKeyboardsByMids.put(var3.id, var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, var3, var1);
   }

   // $FF: synthetic method
   public void lambda$putEmojiKeywords$118$DataQuery(TLRPC.TL_emojiKeywordsDifference var1, String var2) {
      Exception var10000;
      label83: {
         SQLitePreparedStatement var3;
         boolean var10001;
         label86: {
            SQLitePreparedStatement var4;
            int var5;
            try {
               if (var1.keywords.isEmpty()) {
                  break label86;
               }

               var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO emoji_keywords_v2 VALUES(?, ?, ?)");
               var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM emoji_keywords_v2 WHERE lang = ? AND keyword = ? AND emoji = ?");
               MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
               var5 = var1.keywords.size();
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label83;
            }

            int var6 = 0;

            while(true) {
               if (var6 >= var5) {
                  try {
                     MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
                     var3.dispose();
                     var4.dispose();
                     break;
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label83;
                  }
               }

               label88: {
                  TLRPC.EmojiKeyword var7;
                  int var9;
                  int var10;
                  String var20;
                  label89: {
                     TLRPC.TL_emojiKeyword var8;
                     try {
                        var7 = (TLRPC.EmojiKeyword)var1.keywords.get(var6);
                        if (!(var7 instanceof TLRPC.TL_emojiKeyword)) {
                           break label89;
                        }

                        var8 = (TLRPC.TL_emojiKeyword)var7;
                        var20 = var8.keyword.toLowerCase();
                        var9 = var8.emoticons.size();
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label83;
                     }

                     var10 = 0;

                     while(true) {
                        if (var10 >= var9) {
                           break label88;
                        }

                        try {
                           var3.requery();
                           var3.bindString(1, var1.lang_code);
                           var3.bindString(2, var20);
                           var3.bindString(3, (String)var8.emoticons.get(var10));
                           var3.step();
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label83;
                        }

                        ++var10;
                     }
                  }

                  TLRPC.TL_emojiKeywordDeleted var21;
                  try {
                     if (!(var7 instanceof TLRPC.TL_emojiKeywordDeleted)) {
                        break label88;
                     }

                     var21 = (TLRPC.TL_emojiKeywordDeleted)var7;
                     var20 = var21.keyword.toLowerCase();
                     var9 = var21.emoticons.size();
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label83;
                  }

                  for(var10 = 0; var10 < var9; ++var10) {
                     try {
                        var4.requery();
                        var4.bindString(1, var1.lang_code);
                        var4.bindString(2, var20);
                        var4.bindString(3, (String)var21.emoticons.get(var10));
                        var4.step();
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label83;
                     }
                  }
               }

               ++var6;
            }
         }

         try {
            var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO emoji_keywords_info_v2 VALUES(?, ?, ?, ?)");
            var3.bindString(1, var2);
            var3.bindString(2, var1.lang_code);
            var3.bindInteger(3, var1.version);
            var3.bindLong(4, System.currentTimeMillis());
            var3.step();
            var3.dispose();
            _$$Lambda$DataQuery$TyujBNGo6dMrF4_NTghE5U4jKgQ var19 = new _$$Lambda$DataQuery$TyujBNGo6dMrF4_NTghE5U4jKgQ(this, var2);
            AndroidUtilities.runOnUIThread(var19);
            return;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
         }
      }

      Exception var18 = var10000;
      FileLog.e((Throwable)var18);
   }

   // $FF: synthetic method
   public void lambda$putFeaturedStickersToCache$25$DataQuery(ArrayList var1, ArrayList var2, int var3, int var4) {
      Exception var10000;
      boolean var10001;
      if (var1 != null) {
         label87: {
            SQLitePreparedStatement var5;
            try {
               var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO stickers_featured VALUES(?, ?, ?, ?, ?)");
               var5.requery();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label87;
            }

            byte var6 = 0;
            int var7 = 0;
            int var8 = 4;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  var8 += ((TLRPC.StickerSetCovered)var1.get(var7)).getObjectSize();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label87;
               }

               ++var7;
            }

            NativeByteBuffer var9;
            NativeByteBuffer var10;
            try {
               var9 = new NativeByteBuffer(var8);
               var10 = new NativeByteBuffer(var2.size() * 8 + 4);
               var9.writeInt32(var1.size());
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label87;
            }

            var7 = 0;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  ((TLRPC.StickerSetCovered)var1.get(var7)).serializeToStream(var9);
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label87;
               }

               ++var7;
            }

            try {
               var10.writeInt32(var2.size());
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label87;
            }

            var7 = var6;

            while(true) {
               try {
                  if (var7 >= var2.size()) {
                     break;
                  }

                  var10.writeInt64((Long)var2.get(var7));
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label87;
               }

               ++var7;
            }

            try {
               var5.bindInteger(1, 1);
               var5.bindByteBuffer(2, (NativeByteBuffer)var9);
               var5.bindByteBuffer(3, (NativeByteBuffer)var10);
               var5.bindInteger(4, var3);
               var5.bindInteger(5, var4);
               var5.step();
               var9.reuse();
               var10.reuse();
               var5.dispose();
               return;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         }
      } else {
         try {
            SQLitePreparedStatement var20 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("UPDATE stickers_featured SET date = ?");
            var20.requery();
            var20.bindInteger(1, var3);
            var20.step();
            var20.dispose();
            return;
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
         }
      }

      Exception var19 = var10000;
      FileLog.e((Throwable)var19);
   }

   // $FF: synthetic method
   public void lambda$putMediaCountDatabase$61$DataQuery(long var1, int var3, int var4) {
      try {
         SQLitePreparedStatement var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
         var5.requery();
         var5.bindLong(1, var1);
         var5.bindInteger(2, var3);
         var5.bindInteger(3, var4);
         var5.bindInteger(4, 0);
         var5.step();
         var5.dispose();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   // $FF: synthetic method
   public void lambda$putMediaDatabase$64$DataQuery(ArrayList var1, boolean var2, long var3, int var5, int var6) {
      Exception var10000;
      label111: {
         boolean var10001;
         label102: {
            label101: {
               try {
                  if (var1.isEmpty()) {
                     break label101;
                  }
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label111;
               }

               if (!var2) {
                  break label102;
               }
            }

            try {
               MessagesStorage.getInstance(this.currentAccount).doneHolesInMedia(var3, var5, var6);
               if (var1.isEmpty()) {
                  return;
               }
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label111;
            }
         }

         SQLitePreparedStatement var7;
         Iterator var8;
         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
            var7 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
            var8 = var1.iterator();
         } catch (Exception var22) {
            var10000 = var22;
            var10001 = false;
            break label111;
         }

         while(true) {
            TLRPC.Message var9;
            long var10;
            try {
               if (!var8.hasNext()) {
                  break;
               }

               var9 = (TLRPC.Message)var8.next();
               if (!canAddMessageToMedia(var9)) {
                  continue;
               }

               var10 = (long)var9.id;
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label111;
            }

            long var12 = var10;

            try {
               if (var9.to_id.channel_id != 0) {
                  var12 = var10 | (long)var9.to_id.channel_id << 32;
               }
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label111;
            }

            try {
               var7.requery();
               NativeByteBuffer var14 = new NativeByteBuffer(var9.getObjectSize());
               var9.serializeToStream(var14);
               var7.bindLong(1, var12);
               var7.bindLong(2, var3);
               var7.bindInteger(3, var9.date);
               var7.bindInteger(4, var6);
               var7.bindByteBuffer(5, (NativeByteBuffer)var14);
               var7.step();
               var14.reuse();
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label111;
            }
         }

         try {
            var7.dispose();
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label111;
         }

         if (!var2 || var5 != 0) {
            int var15;
            if (var2) {
               var15 = 1;
            } else {
               try {
                  var15 = ((TLRPC.Message)var1.get(var1.size() - 1)).id;
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label111;
               }
            }

            if (var5 != 0) {
               try {
                  MessagesStorage.getInstance(this.currentAccount).closeHolesInMedia(var3, var15, var5, var6);
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label111;
               }
            } else {
               try {
                  MessagesStorage.getInstance(this.currentAccount).closeHolesInMedia(var3, var15, Integer.MAX_VALUE, var6);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label111;
               }
            }
         }

         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
            return;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
         }
      }

      Exception var27 = var10000;
      FileLog.e((Throwable)var27);
   }

   // $FF: synthetic method
   public void lambda$putSetToCache$9$DataQuery(TLRPC.TL_messages_stickerSet var1) {
      try {
         SQLitePreparedStatement var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var2.requery();
         StringBuilder var3 = new StringBuilder();
         var3.append("s_");
         var3.append(var1.set.id);
         var2.bindString(1, var3.toString());
         var2.bindInteger(2, 6);
         var2.bindString(3, "");
         var2.bindString(4, "");
         var2.bindString(5, "");
         var2.bindInteger(6, 0);
         var2.bindInteger(7, 0);
         var2.bindInteger(8, 0);
         var2.bindInteger(9, 0);
         NativeByteBuffer var5 = new NativeByteBuffer(var1.getObjectSize());
         var1.serializeToStream(var5);
         var2.bindByteBuffer(10, (NativeByteBuffer)var5);
         var2.step();
         var5.reuse();
         var2.dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$putStickersToCache$36$DataQuery(ArrayList var1, int var2, int var3, int var4) {
      Exception var10000;
      boolean var10001;
      if (var1 != null) {
         label66: {
            SQLitePreparedStatement var5;
            try {
               var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO stickers_v2 VALUES(?, ?, ?, ?)");
               var5.requery();
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label66;
            }

            byte var6 = 0;
            int var7 = 0;
            int var8 = 4;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  var8 += ((TLRPC.TL_messages_stickerSet)var1.get(var7)).getObjectSize();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label66;
               }

               ++var7;
            }

            NativeByteBuffer var9;
            try {
               var9 = new NativeByteBuffer(var8);
               var9.writeInt32(var1.size());
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label66;
            }

            var7 = var6;

            while(true) {
               try {
                  if (var7 >= var1.size()) {
                     break;
                  }

                  ((TLRPC.TL_messages_stickerSet)var1.get(var7)).serializeToStream(var9);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label66;
               }

               ++var7;
            }

            try {
               var5.bindInteger(1, var2 + 1);
               var5.bindByteBuffer(2, (NativeByteBuffer)var9);
               var5.bindInteger(3, var3);
               var5.bindInteger(4, var4);
               var5.step();
               var9.reuse();
               var5.dispose();
               return;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
            }
         }
      } else {
         try {
            SQLitePreparedStatement var17 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("UPDATE stickers_v2 SET date = ?");
            var17.requery();
            var17.bindInteger(1, var3);
            var17.step();
            var17.dispose();
            return;
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
   }

   // $FF: synthetic method
   public void lambda$removeRecentGif$2$DataQuery(TLRPC.TL_messages_saveGif var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 != null && FileRefController.isFileRefError(var3.text)) {
         FileRefController.getInstance(this.currentAccount).requestReference("gif", var1);
      }

   }

   // $FF: synthetic method
   public void lambda$removeRecentGif$3$DataQuery(TLRPC.Document var1) {
      try {
         SQLiteDatabase var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
         StringBuilder var3 = new StringBuilder();
         var3.append("DELETE FROM web_recent_v3 WHERE id = '");
         var3.append(var1.id);
         var3.append("' AND type = 2");
         var2.executeFast(var3.toString()).stepThis().dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$removeStickersSet$44$DataQuery(int var1, int var2, BaseFragment var3, boolean var4, TLObject var5, TLRPC.TL_error var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$O3U_V0n7MR7QFur96I7yoxXpLvQ(this, var5, var1, var2, var3, var4));
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$XkVtXDXzkHZcNcyTDn3dq8s1agk(this, var1), 1000L);
   }

   // $FF: synthetic method
   public void lambda$removeStickersSet$46$DataQuery(TLRPC.StickerSet var1, Context var2, int var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$x29XwwId3_CQasQvc9QUrJecg8c(this, var5, var1, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$saveDraft$102$DataQuery(long var1, int var3, long var4) {
      TLRPC.TL_channels_getMessages var6 = null;

      Exception var10000;
      label66: {
         SQLiteCursor var7;
         boolean var10001;
         try {
            var7 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM messages WHERE mid = %d", var1));
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label66;
         }

         TLRPC.Message var8 = var6;

         label56: {
            NativeByteBuffer var9;
            try {
               if (!var7.next()) {
                  break label56;
               }

               var9 = var7.byteBufferValue(0);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label66;
            }

            var8 = var6;
            if (var9 != null) {
               try {
                  var8 = TLRPC.Message.TLdeserialize(var9, var9.readInt32(false), false);
                  var8.readAttachPath(var9, UserConfig.getInstance(this.currentAccount).clientUserId);
                  var9.reuse();
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label66;
               }
            }
         }

         try {
            var7.dispose();
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label66;
         }

         if (var8 == null) {
            ConnectionsManager var20;
            if (var3 != 0) {
               try {
                  var6 = new TLRPC.TL_channels_getMessages();
                  var6.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var3);
                  var6.id.add((int)var1);
                  var20 = ConnectionsManager.getInstance(this.currentAccount);
                  _$$Lambda$DataQuery$Fmy_RKu5TkGeEmPjdx9_EaHjKV0 var18 = new _$$Lambda$DataQuery$Fmy_RKu5TkGeEmPjdx9_EaHjKV0(this, var4);
                  var20.sendRequest(var6, var18);
                  return;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
               }
            } else {
               try {
                  TLRPC.TL_messages_getMessages var19 = new TLRPC.TL_messages_getMessages();
                  var19.id.add((int)var1);
                  var20 = ConnectionsManager.getInstance(this.currentAccount);
                  _$$Lambda$DataQuery$a_Ly20evk0XxP_zooJOMw6rmEJc var17 = new _$$Lambda$DataQuery$a_Ly20evk0XxP_zooJOMw6rmEJc(this, var4);
                  var20.sendRequest(var19, var17);
                  return;
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
               }
            }
         } else {
            try {
               this.saveDraftReplyMessage(var4, var8);
               return;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
            }
         }
      }

      Exception var21 = var10000;
      FileLog.e((Throwable)var21);
   }

   // $FF: synthetic method
   public void lambda$saveDraftReplyMessage$103$DataQuery(long var1, TLRPC.Message var3) {
      TLRPC.DraftMessage var4 = (TLRPC.DraftMessage)this.drafts.get(var1);
      if (var4 != null && var4.reply_to_msg_id == var3.id) {
         this.draftMessages.put(var1, var3);
         SerializedData var7 = new SerializedData(var3.getObjectSize());
         var3.serializeToStream(var7);
         Editor var6 = this.preferences.edit();
         StringBuilder var5 = new StringBuilder();
         var5.append("r_");
         var5.append(var1);
         var6.putString(var5.toString(), Utilities.bytesToHex(var7.toByteArray())).commit();
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.newDraftReceived, var1);
         var7.cleanup();
      }

   }

   // $FF: synthetic method
   public void lambda$savePeer$82$DataQuery(int var1, int var2, double var3) {
      try {
         SQLitePreparedStatement var5 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO chat_hints VALUES(?, ?, ?, ?)");
         var5.requery();
         var5.bindInteger(1, var1);
         var5.bindInteger(2, var2);
         var5.bindDouble(3, var3);
         var5.bindInteger(4, (int)System.currentTimeMillis() / 1000);
         var5.step();
         var5.dispose();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

   }

   // $FF: synthetic method
   public void lambda$savePinnedMessage$88$DataQuery(TLRPC.Message param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$saveReplyMessages$95$DataQuery(ArrayList var1, SparseArray var2) {
      Exception var10000;
      label79: {
         SQLitePreparedStatement var3;
         boolean var10001;
         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
            var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("UPDATE messages SET replydata = ? WHERE mid = ?");
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label79;
         }

         int var4 = 0;

         while(true) {
            TLRPC.Message var5;
            ArrayList var6;
            try {
               if (var4 >= var1.size()) {
                  break;
               }

               var5 = (TLRPC.Message)var1.get(var4);
               var6 = (ArrayList)var2.get(var5.id);
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label79;
            }

            if (var6 != null) {
               NativeByteBuffer var7;
               try {
                  var7 = new NativeByteBuffer(var5.getObjectSize());
                  var5.serializeToStream(var7);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label79;
               }

               int var8 = 0;

               while(true) {
                  long var9;
                  MessageObject var22;
                  try {
                     if (var8 >= var6.size()) {
                        break;
                     }

                     var22 = (MessageObject)var6.get(var8);
                     var3.requery();
                     var9 = (long)var22.getId();
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label79;
                  }

                  long var11 = var9;

                  try {
                     if (var22.messageOwner.to_id.channel_id != 0) {
                        var11 = var9 | (long)var22.messageOwner.to_id.channel_id << 32;
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label79;
                  }

                  try {
                     var3.bindByteBuffer(1, (NativeByteBuffer)var7);
                     var3.bindLong(2, var11);
                     var3.step();
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label79;
                  }

                  ++var8;
               }

               try {
                  var7.reuse();
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label79;
               }
            }

            ++var4;
         }

         try {
            var3.dispose();
            MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
            return;
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
         }
      }

      Exception var21 = var10000;
      FileLog.e((Throwable)var21);
   }

   // $FF: synthetic method
   public void lambda$searchMessagesInChat$48$DataQuery(long var1, TLRPC.TL_messages_search var3, long var4, int var6, int var7, TLRPC.User var8, TLObject var9, TLRPC.TL_error var10) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw(this, var1, var9, var3, var4, var6, var7, var8));
   }

   // $FF: synthetic method
   public void lambda$searchMessagesInChat$50$DataQuery(int var1, TLRPC.TL_messages_search var2, long var3, long var5, int var7, long var8, TLRPC.User var10, TLObject var11, TLRPC.TL_error var12) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$2id2umTzhmpQDk5s_dtlRZWpox0(this, var1, var11, var2, var3, var5, var7, var8, var10));
   }

   public void loadArchivedStickersCount(int var1, boolean var2) {
      boolean var3 = true;
      if (var2) {
         SharedPreferences var4 = MessagesController.getNotificationsSettings(this.currentAccount);
         StringBuilder var5 = new StringBuilder();
         var5.append("archivedStickersCount");
         var5.append(var1);
         int var6 = var4.getInt(var5.toString(), -1);
         if (var6 == -1) {
            this.loadArchivedStickersCount(var1, false);
         } else {
            this.archivedStickersCount[var1] = var6;
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.archivedStickersCountDidLoad, var1);
         }
      } else {
         TLRPC.TL_messages_getArchivedStickers var7 = new TLRPC.TL_messages_getArchivedStickers();
         var7.limit = 0;
         if (var1 == 1) {
            var2 = var3;
         } else {
            var2 = false;
         }

         var7.masks = var2;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$DataQuery$vXxSlr6eK9Z6xNmLs0Dj7UWvraQ(this, var1));
      }

   }

   public void loadBotInfo(int var1, boolean var2, int var3) {
      if (var2) {
         TLRPC.BotInfo var4 = (TLRPC.BotInfo)this.botInfos.get(var1);
         if (var4 != null) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botInfoDidLoad, var4, var3);
            return;
         }
      }

      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$RU0vB5vrHjb7JApo8zy3krcj73Y(this, var1, var3));
   }

   public void loadBotKeyboard(long var1) {
      TLRPC.Message var3 = (TLRPC.Message)this.botKeyboards.get(var1);
      if (var3 != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botKeyboardDidLoad, var3, var1);
      } else {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$C70bX86RVrRMk7D3n7tAtGTuntU(this, var1));
      }
   }

   public void loadDrafts() {
      if (!UserConfig.getInstance(this.currentAccount).draftsLoaded && !this.loadingDrafts) {
         this.loadingDrafts = true;
         TLRPC.TL_messages_getAllDrafts var1 = new TLRPC.TL_messages_getAllDrafts();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$DataQuery$A03PPqgH8dlVye1igOOaKjX18Mo(this));
      }

   }

   public void loadFeaturedStickers(boolean var1, boolean var2) {
      if (!this.loadingFeaturedStickers) {
         this.loadingFeaturedStickers = true;
         if (var1) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_js4uGbpT5ecS0xV_WWy27wFeXg(this));
         } else {
            TLRPC.TL_messages_getFeaturedStickers var3 = new TLRPC.TL_messages_getFeaturedStickers();
            int var4;
            if (var2) {
               var4 = 0;
            } else {
               var4 = this.loadFeaturedHash;
            }

            var3.hash = var4;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$DataQuery$KjoE15J_7Y5rDsfACZqjBs6rNXk(this, var3));
         }

      }
   }

   public void loadHints(boolean var1) {
      if (!this.loading && UserConfig.getInstance(this.currentAccount).suggestContacts) {
         if (var1) {
            if (this.loaded) {
               return;
            }

            this.loading = true;
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$lAwKQ3k2p_MRBgXcDMGAD4Or380(this));
            this.loaded = true;
         } else {
            this.loading = true;
            TLRPC.TL_contacts_getTopPeers var2 = new TLRPC.TL_contacts_getTopPeers();
            var2.hash = 0;
            var2.bots_pm = false;
            var2.correspondents = true;
            var2.groups = false;
            var2.channels = false;
            var2.bots_inline = true;
            var2.offset = 0;
            var2.limit = 20;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$DataQuery$pT1Y9otWt0WNp61eT_y0o9CzuLY(this));
         }
      }

   }

   public void loadMedia(long var1, int var3, int var4, int var5, int var6, int var7) {
      int var8 = (int)var1;
      boolean var9;
      if (var8 < 0 && ChatObject.isChannel(-var8, this.currentAccount)) {
         var9 = true;
      } else {
         var9 = false;
      }

      if (var6 == 0 && var8 != 0) {
         TLRPC.TL_messages_search var10 = new TLRPC.TL_messages_search();
         var10.limit = var3;
         var10.offset_id = var4;
         if (var5 == 0) {
            var10.filter = new TLRPC.TL_inputMessagesFilterPhotoVideo();
         } else if (var5 == 1) {
            var10.filter = new TLRPC.TL_inputMessagesFilterDocument();
         } else if (var5 == 2) {
            var10.filter = new TLRPC.TL_inputMessagesFilterRoundVoice();
         } else if (var5 == 3) {
            var10.filter = new TLRPC.TL_inputMessagesFilterUrl();
         } else if (var5 == 4) {
            var10.filter = new TLRPC.TL_inputMessagesFilterMusic();
         }

         var10.q = "";
         var10.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var8);
         if (var10.peer == null) {
            return;
         }

         var3 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU(this, var1, var3, var4, var5, var7, var9));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var3, var7);
      } else {
         this.loadMediaDatabase(var1, var3, var4, var5, var7, var9, var6);
      }

   }

   public void loadMusic(long var1, long var3) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$cA_X50sc6geZKVWNtiHKllnXTb8(this, var1, var3));
   }

   public MessageObject loadPinnedMessage(long var1, int var3, int var4, boolean var5) {
      if (var5) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$hzk0ktoumSLB8vFJVERgSnj2uGI(this, var1, var3, var4));
         return null;
      } else {
         return this.loadPinnedMessageInternal(var1, var3, var4, true);
      }
   }

   public void loadRecents(int var1, boolean var2, boolean var3, boolean var4) {
      boolean var5;
      label61: {
         var5 = false;
         if (var2) {
            if (this.loadingRecentGifs) {
               return;
            }

            this.loadingRecentGifs = true;
            if (!this.recentGifsLoaded) {
               break label61;
            }
         } else {
            boolean[] var6 = this.loadingRecentStickers;
            if (var6[var1]) {
               return;
            }

            var6[var1] = true;
            if (!this.recentStickersLoaded[var1]) {
               break label61;
            }
         }

         var3 = false;
      }

      if (var3) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$vDbI4SDDFGBUSoYXXeeYvbjHYJY(this, var2, var1));
      } else {
         SharedPreferences var9 = MessagesController.getEmojiSettings(this.currentAccount);
         if (!var4) {
            long var7;
            if (var2) {
               var7 = var9.getLong("lastGifLoadTime", 0L);
            } else if (var1 == 0) {
               var7 = var9.getLong("lastStickersLoadTime", 0L);
            } else if (var1 == 1) {
               var7 = var9.getLong("lastStickersLoadTimeMask", 0L);
            } else {
               var7 = var9.getLong("lastStickersLoadTimeFavs", 0L);
            }

            if (Math.abs(System.currentTimeMillis() - var7) < 3600000L) {
               if (var2) {
                  this.loadingRecentGifs = false;
               } else {
                  this.loadingRecentStickers[var1] = false;
               }

               return;
            }
         }

         if (var2) {
            TLRPC.TL_messages_getSavedGifs var10 = new TLRPC.TL_messages_getSavedGifs();
            var10.hash = calcDocumentsHash(this.recentGifs);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$DataQuery$5cEA9KV_G5ibi7bkh4w7n6aFdJ8(this, var1, var2));
         } else {
            Object var11;
            if (var1 == 2) {
               var11 = new TLRPC.TL_messages_getFavedStickers();
               ((TLRPC.TL_messages_getFavedStickers)var11).hash = calcDocumentsHash(this.recentStickers[var1]);
            } else {
               var11 = new TLRPC.TL_messages_getRecentStickers();
               ((TLRPC.TL_messages_getRecentStickers)var11).hash = calcDocumentsHash(this.recentStickers[var1]);
               var3 = var5;
               if (var1 == 1) {
                  var3 = true;
               }

               ((TLRPC.TL_messages_getRecentStickers)var11).attached = var3;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var11, new _$$Lambda$DataQuery$5qGaDi2FRyj1QCnrOleMSVLz1k4(this, var1, var2));
         }
      }

   }

   public void loadReplyMessagesForMessages(ArrayList var1, long var2) {
      int var4 = (int)var2;
      int var5 = 0;
      int var6 = 0;
      MessageObject var9;
      long var10;
      ArrayList var12;
      ArrayList var13;
      if (var4 == 0) {
         ArrayList var7 = new ArrayList();

         LongSparseArray var8;
         for(var8 = new LongSparseArray(); var6 < var1.size(); ++var6) {
            var9 = (MessageObject)var1.get(var6);
            if (var9.isReply() && var9.replyMessageObject == null) {
               var10 = var9.messageOwner.reply_to_random_id;
               var12 = (ArrayList)var8.get(var10);
               var13 = var12;
               if (var12 == null) {
                  var13 = new ArrayList();
                  var8.put(var10, var13);
               }

               var13.add(var9);
               if (!var7.contains(var10)) {
                  var7.add(var10);
               }
            }
         }

         if (var7.isEmpty()) {
            return;
         }

         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$WzRDIT94H3f0FgoSArkqNqup9dc(this, var7, var2, var8));
      } else {
         ArrayList var14 = new ArrayList();
         SparseArray var18 = new SparseArray();
         StringBuilder var19 = new StringBuilder();

         for(var6 = 0; var5 < var1.size(); var6 = var4) {
            var9 = (MessageObject)var1.get(var5);
            var4 = var6;
            if (var9.getId() > 0) {
               var4 = var6;
               if (var9.isReply()) {
                  var4 = var6;
                  if (var9.replyMessageObject == null) {
                     TLRPC.Message var20 = var9.messageOwner;
                     int var15 = var20.reply_to_msg_id;
                     long var16 = (long)var15;
                     var4 = var20.to_id.channel_id;
                     var10 = var16;
                     if (var4 != 0) {
                        var10 = var16 | (long)var4 << 32;
                        var6 = var4;
                     }

                     if (var19.length() > 0) {
                        var19.append(',');
                     }

                     var19.append(var10);
                     var12 = (ArrayList)var18.get(var15);
                     var13 = var12;
                     if (var12 == null) {
                        var13 = new ArrayList();
                        var18.put(var15, var13);
                     }

                     var13.add(var9);
                     var4 = var6;
                     if (!var14.contains(var15)) {
                        var14.add(var15);
                        var4 = var6;
                     }
                  }
               }
            }

            ++var5;
         }

         if (var14.isEmpty()) {
            return;
         }

         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$BoxhS9ivFCv8Vy62eC0FaKjPf8o(this, var19, var2, var14, var18, var6));
      }

   }

   public void loadStickers(int var1, boolean var2, boolean var3) {
      if (!this.loadingStickers[var1]) {
         if (var1 == 3) {
            if (this.featuredStickerSets.isEmpty() || !MessagesController.getInstance(this.currentAccount).preloadFeaturedStickers) {
               return;
            }
         } else {
            this.loadArchivedStickersCount(var1, var2);
         }

         this.loadingStickers[var1] = true;
         if (var2) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$xa4Sj3vXQ9Lm0vsRxYjFZWyzhng(this, var1));
         } else {
            byte var4 = 0;
            byte var5 = 0;
            int var6 = 0;
            if (var1 == 3) {
               TLRPC.TL_messages_allStickers var9 = new TLRPC.TL_messages_allStickers();
               var9.hash = this.loadFeaturedHash;

               for(int var8 = this.featuredStickerSets.size(); var6 < var8; ++var6) {
                  var9.sets.add(((TLRPC.StickerSetCovered)this.featuredStickerSets.get(var6)).set);
               }

               this.processLoadStickersResponse(var1, var9);
               return;
            }

            Object var7;
            if (var1 == 0) {
               var7 = new TLRPC.TL_messages_getAllStickers();
               if (var3) {
                  var6 = var4;
               } else {
                  var6 = this.loadHash[var1];
               }

               ((TLRPC.TL_messages_getAllStickers)var7).hash = var6;
            } else {
               var7 = new TLRPC.TL_messages_getMaskStickers();
               if (var3) {
                  var6 = var5;
               } else {
                  var6 = this.loadHash[var1];
               }

               ((TLRPC.TL_messages_getMaskStickers)var7).hash = var6;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var7, new _$$Lambda$DataQuery$rOQjG3YS0y6BqT7zHxJCSwr131o(this, var1, var6));
         }

      }
   }

   public void markFaturedStickersAsRead(boolean var1) {
      if (!this.unreadStickerSets.isEmpty()) {
         this.unreadStickerSets.clear();
         this.loadFeaturedHash = this.calcFeaturedStickersHash(this.featuredStickerSets);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.featuredStickersDidLoad);
         this.putFeaturedStickersToCache(this.featuredStickerSets, this.unreadStickerSets, this.loadFeaturedDate, this.loadFeaturedHash);
         if (var1) {
            TLRPC.TL_messages_readFeaturedStickers var2 = new TLRPC.TL_messages_readFeaturedStickers();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, _$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M.INSTANCE);
         }

      }
   }

   public void markFaturedStickersByIdAsRead(long var1) {
      if (this.unreadStickerSets.contains(var1) && !this.readingStickerSets.contains(var1)) {
         this.readingStickerSets.add(var1);
         TLRPC.TL_messages_readFeaturedStickers var3 = new TLRPC.TL_messages_readFeaturedStickers();
         var3.id.add(var1);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, _$$Lambda$DataQuery$CoOuaI5Ui4g_M8Hv23Yz8tRYQcY.INSTANCE);
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$g0o9Z_244wbMtOYxpACC2qzITTc(this, var1), 1000L);
      }

   }

   protected void processLoadedRecentDocuments(int var1, ArrayList var2, boolean var3, int var4, boolean var5) {
      if (var2 != null) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$uuHSgHUH35SvbaUdMusqTnQ_iPk(this, var3, var1, var2, var5, var4));
      }

      if (var4 == 0) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$DataQuery$7M05Rnf_3mDk_ioxbIIY86tv1DE(this, var3, var1, var2));
      }

   }

   public void putBotInfo(TLRPC.BotInfo var1) {
      if (var1 != null) {
         this.botInfos.put(var1.user_id, var1);
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc(this, var1));
      }
   }

   public void putBotKeyboard(long var1, TLRPC.Message var3) {
      if (var3 != null) {
         Exception var10000;
         label52: {
            SQLiteDatabase var4;
            Locale var5;
            boolean var10001;
            try {
               var4 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
               var5 = Locale.US;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label52;
            }

            int var6 = 0;

            SQLiteCursor var13;
            try {
               var13 = var4.queryFinalized(String.format(var5, "SELECT mid FROM bot_keyboard WHERE uid = %d", var1));
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label52;
            }

            try {
               if (var13.next()) {
                  var6 = var13.intValue(0);
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label52;
            }

            try {
               var13.dispose();
               if (var6 >= var3.id) {
                  return;
               }
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label52;
            }

            try {
               SQLitePreparedStatement var16 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO bot_keyboard VALUES(?, ?, ?)");
               var16.requery();
               NativeByteBuffer var14 = new NativeByteBuffer(var3.getObjectSize());
               var3.serializeToStream(var14);
               var16.bindLong(1, var1);
               var16.bindInteger(2, var3.id);
               var16.bindByteBuffer(3, (NativeByteBuffer)var14);
               var16.step();
               var14.reuse();
               var16.dispose();
               _$$Lambda$DataQuery$g5fV5AzEjpYa9k_FQikVmx9CWxY var15 = new _$$Lambda$DataQuery$g5fV5AzEjpYa9k_FQikVmx9CWxY(this, var1, var3);
               AndroidUtilities.runOnUIThread(var15);
               return;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var12 = var10000;
         FileLog.e((Throwable)var12);
      }
   }

   public void putGroupStickerSet(TLRPC.TL_messages_stickerSet var1) {
      this.groupStickerSets.put(var1.set.id, var1);
   }

   public void removeInline(int var1) {
      for(int var2 = 0; var2 < this.inlineBots.size(); ++var2) {
         if (((TLRPC.TL_topPeer)this.inlineBots.get(var2)).peer.user_id == var1) {
            this.inlineBots.remove(var2);
            TLRPC.TL_contacts_resetTopPeerRating var3 = new TLRPC.TL_contacts_resetTopPeerRating();
            var3.category = new TLRPC.TL_topPeerCategoryBotsInline();
            var3.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var1);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, _$$Lambda$DataQuery$nEW3BBG5NIOAbnpoGoJ7S4IYot0.INSTANCE);
            this.deletePeer(var1, 1);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadInlineHints);
            return;
         }
      }

   }

   public void removePeer(int var1) {
      for(int var2 = 0; var2 < this.hints.size(); ++var2) {
         if (((TLRPC.TL_topPeer)this.hints.get(var2)).peer.user_id == var1) {
            this.hints.remove(var2);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.reloadHints);
            TLRPC.TL_contacts_resetTopPeerRating var3 = new TLRPC.TL_contacts_resetTopPeerRating();
            var3.category = new TLRPC.TL_topPeerCategoryCorrespondents();
            var3.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var1);
            this.deletePeer(var1, 0);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, _$$Lambda$DataQuery$eHV0FXEJJkBEZzT5FGLWr_l3mT8.INSTANCE);
            return;
         }
      }

   }

   public void removeRecentGif(TLRPC.Document var1) {
      this.recentGifs.remove(var1);
      TLRPC.TL_messages_saveGif var2 = new TLRPC.TL_messages_saveGif();
      var2.id = new TLRPC.TL_inputDocument();
      TLRPC.InputDocument var3 = var2.id;
      var3.id = var1.id;
      var3.access_hash = var1.access_hash;
      var3.file_reference = var1.file_reference;
      if (var3.file_reference == null) {
         var3.file_reference = new byte[0];
      }

      var2.unsave = true;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$DataQuery$2rJc9xeBmgwGexV2v1U_RebLTns(this, var2));
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$_3R_0wEE3expP9wo0aColXdZCOk(this, var1));
   }

   public void removeStickersSet(Context var1, TLRPC.StickerSet var2, int var3, BaseFragment var4, boolean var5) {
      byte var6 = var2.masks;
      TLRPC.TL_inputStickerSetID var7 = new TLRPC.TL_inputStickerSetID();
      var7.access_hash = var2.access_hash;
      var7.id = var2.id;
      if (var3 != 0) {
         boolean var8 = false;
         boolean var9;
         if (var3 == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var2.archived = var9;

         for(int var10 = 0; var10 < this.stickerSets[var6].size(); ++var10) {
            TLRPC.TL_messages_stickerSet var11 = (TLRPC.TL_messages_stickerSet)this.stickerSets[var6].get(var10);
            if (var11.set.id == var2.id) {
               this.stickerSets[var6].remove(var10);
               if (var3 == 2) {
                  this.stickerSets[var6].add(0, var11);
               } else {
                  this.stickerSetsById.remove(var11.set.id);
                  this.installedStickerSetsById.remove(var11.set.id);
                  this.stickerSetsByName.remove(var11.set.short_name);
               }
               break;
            }
         }

         this.loadHash[var6] = calcStickersHash(this.stickerSets[var6]);
         this.putStickersToCache(var6, this.stickerSets[var6], this.loadDate[var6], this.loadHash[var6]);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(var6));
         TLRPC.TL_messages_installStickerSet var12 = new TLRPC.TL_messages_installStickerSet();
         var12.stickerset = var7;
         var9 = var8;
         if (var3 == 1) {
            var9 = true;
         }

         var12.archived = var9;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$DataQuery$qjcWF_Rlu499xbvvlZfzwoJxsQI(this, var6, var3, var4, var5));
      } else {
         TLRPC.TL_messages_uninstallStickerSet var13 = new TLRPC.TL_messages_uninstallStickerSet();
         var13.stickerset = var7;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var13, new _$$Lambda$DataQuery$DpM7Y4tX6aFYwIY3CiDaFq5CjNE(this, var2, var1, var6));
      }

   }

   public void reorderStickers(int var1, ArrayList var2) {
      Collections.sort(this.stickerSets[var1], new _$$Lambda$DataQuery$dtOJW5lUpjPVOn6aZw8KSWcnFQs(var2));
      this.loadHash[var1] = calcStickersHash(this.stickerSets[var1]);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, var1);
      this.loadStickers(var1, false, true);
   }

   public void replaceStickerSet(TLRPC.TL_messages_stickerSet var1) {
      TLRPC.TL_messages_stickerSet var2 = (TLRPC.TL_messages_stickerSet)this.stickerSetsById.get(var1.set.id);
      TLRPC.TL_messages_stickerSet var3 = var2;
      if (var2 == null) {
         var3 = (TLRPC.TL_messages_stickerSet)this.stickerSetsByName.get(var1.set.short_name);
      }

      byte var4;
      boolean var5;
      label46: {
         var4 = 0;
         var2 = var3;
         if (var3 == null) {
            var3 = (TLRPC.TL_messages_stickerSet)this.groupStickerSets.get(var1.set.id);
            var2 = var3;
            if (var3 != null) {
               var5 = true;
               var2 = var3;
               break label46;
            }
         }

         var5 = false;
      }

      if (var2 != null) {
         LongSparseArray var10 = new LongSparseArray();
         int var6 = var1.documents.size();

         int var7;
         TLRPC.Document var8;
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = (TLRPC.Document)var1.documents.get(var7);
            var10.put(var8.id, var8);
         }

         int var9 = var2.documents.size();
         boolean var12 = false;

         for(var7 = var4; var7 < var9; ++var7) {
            var8 = (TLRPC.Document)var10.get(((TLRPC.Document)var1.documents.get(var7)).id);
            if (var8 != null) {
               var2.documents.set(var7, var8);
               var12 = true;
            }
         }

         if (var12) {
            if (var5) {
               this.putSetToCache(var2);
            } else {
               byte var11 = var1.set.masks;
               this.putStickersToCache(var11, this.stickerSets[var11], this.loadDate[var11], this.loadHash[var11]);
            }
         }

      }
   }

   public void saveDraft(long var1, CharSequence var3, ArrayList var4, TLRPC.Message var5, boolean var6) {
      this.saveDraft(var1, var3, var4, var5, var6, false);
   }

   public void saveDraft(long var1, CharSequence var3, ArrayList var4, TLRPC.Message var5, boolean var6, boolean var7) {
      Object var8;
      if (TextUtils.isEmpty(var3) && var5 == null) {
         var8 = new TLRPC.TL_draftMessageEmpty();
      } else {
         var8 = new TLRPC.TL_draftMessage();
      }

      ((TLRPC.DraftMessage)var8).date = (int)(System.currentTimeMillis() / 1000L);
      String var10;
      if (var3 == null) {
         var10 = "";
      } else {
         var10 = var3.toString();
      }

      ((TLRPC.DraftMessage)var8).message = var10;
      ((TLRPC.DraftMessage)var8).no_webpage = var6;
      if (var5 != null) {
         ((TLRPC.DraftMessage)var8).reply_to_msg_id = var5.id;
         ((TLRPC.DraftMessage)var8).flags |= 1;
      }

      if (var4 != null && !var4.isEmpty()) {
         ((TLRPC.DraftMessage)var8).entities = var4;
         ((TLRPC.DraftMessage)var8).flags |= 8;
      }

      TLRPC.DraftMessage var11 = (TLRPC.DraftMessage)this.drafts.get(var1);
      if (var7 || (var11 == null || !var11.message.equals(((TLRPC.DraftMessage)var8).message) || var11.reply_to_msg_id != ((TLRPC.DraftMessage)var8).reply_to_msg_id || var11.no_webpage != ((TLRPC.DraftMessage)var8).no_webpage) && (var11 != null || !TextUtils.isEmpty(((TLRPC.DraftMessage)var8).message) || ((TLRPC.DraftMessage)var8).reply_to_msg_id != 0)) {
         this.saveDraft(var1, (TLRPC.DraftMessage)var8, var5, false);
         int var9 = (int)var1;
         if (var9 != 0) {
            TLRPC.TL_messages_saveDraft var12 = new TLRPC.TL_messages_saveDraft();
            var12.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var9);
            if (var12.peer == null) {
               return;
            }

            var12.message = ((TLRPC.DraftMessage)var8).message;
            var12.no_webpage = ((TLRPC.DraftMessage)var8).no_webpage;
            var12.reply_to_msg_id = ((TLRPC.DraftMessage)var8).reply_to_msg_id;
            var12.entities = ((TLRPC.DraftMessage)var8).entities;
            var12.flags = ((TLRPC.DraftMessage)var8).flags;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, _$$Lambda$DataQuery$jWB8qVUldoWOaeSwZryuPoQq0I0.INSTANCE);
         }

         MessagesController.getInstance(this.currentAccount).sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }
   }

   public void saveDraft(long var1, TLRPC.DraftMessage var3, TLRPC.Message var4, boolean var5) {
      Editor var6 = this.preferences.edit();
      StringBuilder var7;
      SerializedData var15;
      StringBuilder var16;
      if (var3 != null && !(var3 instanceof TLRPC.TL_draftMessageEmpty)) {
         this.drafts.put(var1, var3);

         try {
            var15 = new SerializedData(var3.getObjectSize());
            var3.serializeToStream(var15);
            var16 = new StringBuilder();
            var16.append("");
            var16.append(var1);
            var6.putString(var16.toString(), Utilities.bytesToHex(var15.toByteArray()));
            var15.cleanup();
         } catch (Exception var12) {
            FileLog.e((Throwable)var12);
         }
      } else {
         this.drafts.remove(var1);
         this.draftMessages.remove(var1);
         Editor var8 = this.preferences.edit();
         var7 = new StringBuilder();
         var7.append("");
         var7.append(var1);
         var8 = var8.remove(var7.toString());
         var7 = new StringBuilder();
         var7.append("r_");
         var7.append(var1);
         var8.remove(var7.toString()).commit();
      }

      if (var4 == null) {
         this.draftMessages.remove(var1);
         var7 = new StringBuilder();
         var7.append("r_");
         var7.append(var1);
         var6.remove(var7.toString());
      } else {
         this.draftMessages.put(var1, var4);
         var15 = new SerializedData(var4.getObjectSize());
         var4.serializeToStream(var15);
         var16 = new StringBuilder();
         var16.append("r_");
         var16.append(var1);
         var6.putString(var16.toString(), Utilities.bytesToHex(var15.toByteArray()));
         var15.cleanup();
      }

      var6.commit();
      if (var5) {
         if (var3.reply_to_msg_id != 0 && var4 == null) {
            int var9 = (int)var1;
            TLRPC.Chat var13 = null;
            TLRPC.User var14;
            if (var9 > 0) {
               var14 = MessagesController.getInstance(this.currentAccount).getUser(var9);
            } else {
               var13 = MessagesController.getInstance(this.currentAccount).getChat(-var9);
               var14 = null;
            }

            if (var14 != null || var13 != null) {
               long var10 = (long)var3.reply_to_msg_id;
               if (ChatObject.isChannel(var13)) {
                  var9 = var13.id;
                  var10 |= (long)var9 << 32;
               } else {
                  var9 = 0;
               }

               MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DataQuery$JgCeTQWWzbO2EPbV9fjIFzenxxY(this, var10, var9, var1));
            }
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.newDraftReceived, var1);
      }

   }

   public void searchMessagesInChat(String var1, long var2, long var4, int var6, int var7, TLRPC.User var8) {
      this.searchMessagesInChat(var1, var2, var4, var6, var7, false, var8);
   }

   public CharSequence substring(CharSequence var1, int var2, int var3) {
      if (var1 instanceof SpannableStringBuilder) {
         return var1.subSequence(var2, var3);
      } else {
         return (CharSequence)(var1 instanceof SpannedString ? var1.subSequence(var2, var3) : TextUtils.substring(var1, var2, var3));
      }
   }

   public void uninstallShortcut(long var1) {
      Exception var10000;
      label84: {
         boolean var10001;
         try {
            if (VERSION.SDK_INT >= 26) {
               ShortcutManager var18 = (ShortcutManager)ApplicationLoader.applicationContext.getSystemService(ShortcutManager.class);
               ArrayList var21 = new ArrayList();
               StringBuilder var5 = new StringBuilder();
               var5.append("sdid_");
               var5.append(var1);
               var21.add(var5.toString());
               var18.removeDynamicShortcuts(var21);
               return;
            }
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label84;
         }

         int var6 = (int)var1;
         int var7 = (int)(var1 >> 32);
         TLRPC.Chat var3 = null;
         TLRPC.User var16;
         if (var6 == 0) {
            TLRPC.EncryptedChat var4;
            try {
               var4 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var7);
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label84;
            }

            if (var4 == null) {
               return;
            }

            try {
               var16 = MessagesController.getInstance(this.currentAccount).getUser(var4.user_id);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label84;
            }
         } else if (var6 > 0) {
            try {
               var16 = MessagesController.getInstance(this.currentAccount).getUser(var6);
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label84;
            }
         } else {
            if (var6 >= 0) {
               return;
            }

            try {
               var3 = MessagesController.getInstance(this.currentAccount).getChat(-var6);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label84;
            }

            var16 = null;
         }

         if (var16 == null && var3 == null) {
            return;
         }

         String var19;
         if (var16 != null) {
            try {
               var19 = ContactsController.formatName(var16.first_name, var16.last_name);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label84;
            }
         } else {
            try {
               var19 = var3.title;
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            }
         }

         try {
            Intent var17 = new Intent();
            var17.putExtra("android.intent.extra.shortcut.INTENT", this.createIntrnalShortcutIntent(var1));
            var17.putExtra("android.intent.extra.shortcut.NAME", var19);
            var17.putExtra("duplicate", false);
            var17.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            ApplicationLoader.applicationContext.sendBroadcast(var17);
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
   }

   public static class KeywordResult {
      public String emoji;
      public String keyword;
   }

   public interface KeywordResultCallback {
      void run(ArrayList var1, String var2);
   }
}
