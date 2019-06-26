package org.telegram.ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class SearchAdapterHelper {
   private boolean allResultsAreGlobal;
   private int channelLastReqId;
   private int channelReqId;
   private int currentAccount;
   private SearchAdapterHelper.SearchAdapterHelperDelegate delegate;
   private ArrayList globalSearch = new ArrayList();
   private SparseArray globalSearchMap = new SparseArray();
   private ArrayList groupSearch = new ArrayList();
   private SparseArray groupSearchMap = new SparseArray();
   private ArrayList hashtags;
   private HashMap hashtagsByText;
   private boolean hashtagsLoadedFromDb;
   private String lastFoundChannel;
   private String lastFoundUsername = null;
   private int lastReqId;
   private ArrayList localSearchResults;
   private ArrayList localServerSearch = new ArrayList();
   private int reqId = 0;

   public SearchAdapterHelper(boolean var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.channelReqId = 0;
      this.hashtagsLoadedFromDb = false;
      this.allResultsAreGlobal = var1;
   }

   // $FF: synthetic method
   static int lambda$null$4(SearchAdapterHelper.HashtagObject var0, SearchAdapterHelper.HashtagObject var1) {
      int var2 = var0.date;
      int var3 = var1.date;
      if (var2 < var3) {
         return 1;
      } else {
         return var2 > var3 ? -1 : 0;
      }
   }

   private void putRecentHashtags(ArrayList var1) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$GIT1_XH69tqWW5ny1WrNuu7bX8Q(this, var1));
   }

   public void addHashtagsFromMessage(CharSequence var1) {
      if (var1 != null) {
         Matcher var2 = Pattern.compile("(^|\\s)#[\\w@.]+").matcher(var1);

         boolean var3;
         for(var3 = false; var2.find(); var3 = true) {
            int var4 = var2.start();
            int var5 = var2.end();
            int var8 = var4;
            if (var1.charAt(var4) != '@') {
               var8 = var4;
               if (var1.charAt(var4) != '#') {
                  var8 = var4 + 1;
               }
            }

            String var6 = var1.subSequence(var8, var5).toString();
            if (this.hashtagsByText == null) {
               this.hashtagsByText = new HashMap();
               this.hashtags = new ArrayList();
            }

            SearchAdapterHelper.HashtagObject var7 = (SearchAdapterHelper.HashtagObject)this.hashtagsByText.get(var6);
            if (var7 == null) {
               var7 = new SearchAdapterHelper.HashtagObject();
               var7.hashtag = var6;
               this.hashtagsByText.put(var7.hashtag, var7);
            } else {
               this.hashtags.remove(var7);
            }

            var7.date = (int)(System.currentTimeMillis() / 1000L);
            this.hashtags.add(0, var7);
         }

         if (var3) {
            this.putRecentHashtags(this.hashtags);
         }

      }
   }

   public void clearRecentHashtags() {
      this.hashtags = new ArrayList();
      this.hashtagsByText = new HashMap();
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$rJWBOKrijR0k23tFEyFN_1lhPTE(this));
   }

   public ArrayList getGlobalSearch() {
      return this.globalSearch;
   }

   public ArrayList getGroupSearch() {
      return this.groupSearch;
   }

   public ArrayList getHashtags() {
      return this.hashtags;
   }

   public String getLastFoundChannel() {
      return this.lastFoundChannel;
   }

   public String getLastFoundUsername() {
      return this.lastFoundUsername;
   }

   public ArrayList getLocalServerSearch() {
      return this.localServerSearch;
   }

   public boolean isSearchInProgress() {
      boolean var1;
      if (this.reqId == 0 && this.channelReqId == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$clearRecentHashtags$8$SearchAdapterHelper() {
      try {
         MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM hashtag_recent_v2 WHERE 1").stepThis().dispose();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$loadRecentHashtags$6$SearchAdapterHelper() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$null$0$SearchAdapterHelper(int var1, TLRPC.TL_error var2, TLObject var3, String var4, boolean var5) {
      if (var1 == this.channelLastReqId && var2 == null) {
         TLRPC.TL_channels_channelParticipants var8 = (TLRPC.TL_channels_channelParticipants)var3;
         this.lastFoundChannel = var4.toLowerCase();
         MessagesController.getInstance(this.currentAccount).putUsers(var8.users, false);
         this.groupSearch.clear();
         this.groupSearchMap.clear();
         this.groupSearch.addAll(var8.participants);
         int var6 = UserConfig.getInstance(this.currentAccount).getClientUserId();
         int var7 = var8.participants.size();

         for(var1 = 0; var1 < var7; ++var1) {
            TLRPC.ChannelParticipant var10 = (TLRPC.ChannelParticipant)var8.participants.get(var1);
            if (!var5 && var10.user_id == var6) {
               this.groupSearch.remove(var10);
            } else {
               this.groupSearchMap.put(var10.user_id, var10);
            }
         }

         ArrayList var9 = this.localSearchResults;
         if (var9 != null) {
            this.mergeResults(var9);
         }

         this.delegate.onDataSetChanged();
      }

      this.channelReqId = 0;
   }

   // $FF: synthetic method
   public void lambda$null$2$SearchAdapterHelper(int var1, TLRPC.TL_error var2, TLObject var3, boolean var4, boolean var5, boolean var6, String var7) {
      if (var1 == this.lastReqId && var2 == null) {
         TLRPC.TL_contacts_found var8 = (TLRPC.TL_contacts_found)var3;
         this.globalSearch.clear();
         this.globalSearchMap.clear();
         this.localServerSearch.clear();
         MessagesController.getInstance(this.currentAccount).putChats(var8.chats, false);
         MessagesController.getInstance(this.currentAccount).putUsers(var8.users, false);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var8.users, var8.chats, true, true);
         SparseArray var9 = new SparseArray();
         SparseArray var10 = new SparseArray();

         TLRPC.Chat var14;
         for(var1 = 0; var1 < var8.chats.size(); ++var1) {
            var14 = (TLRPC.Chat)var8.chats.get(var1);
            var9.put(var14.id, var14);
         }

         for(var1 = 0; var1 < var8.users.size(); ++var1) {
            TLRPC.User var15 = (TLRPC.User)var8.users.get(var1);
            var10.put(var15.id, var15);
         }

         int var12;
         TLRPC.Peer var16;
         Object var17;
         for(var1 = 0; var1 < 2; ++var1) {
            ArrayList var11;
            if (var1 == 0) {
               if (!this.allResultsAreGlobal) {
                  continue;
               }

               var11 = var8.my_results;
            } else {
               var11 = var8.results;
            }

            for(var12 = 0; var12 < var11.size(); ++var12) {
               var16 = (TLRPC.Peer)var11.get(var12);
               int var13 = var16.user_id;
               if (var13 != 0) {
                  var17 = (TLRPC.User)var10.get(var13);
                  var14 = null;
               } else {
                  label143: {
                     var13 = var16.chat_id;
                     if (var13 != 0) {
                        var14 = (TLRPC.Chat)var9.get(var13);
                     } else {
                        var13 = var16.channel_id;
                        if (var13 == 0) {
                           var14 = null;
                           var17 = var14;
                           break label143;
                        }

                        var14 = (TLRPC.Chat)var9.get(var13);
                     }

                     var17 = null;
                  }
               }

               if (var14 != null) {
                  if (var4) {
                     this.globalSearch.add(var14);
                     this.globalSearchMap.put(-var14.id, var14);
                  }
               } else if (var17 != null && (var5 || !((TLRPC.User)var17).bot) && (var6 || !((TLRPC.User)var17).self)) {
                  this.globalSearch.add(var17);
                  this.globalSearchMap.put(((TLRPC.User)var17).id, var17);
               }
            }
         }

         if (!this.allResultsAreGlobal) {
            for(var1 = 0; var1 < var8.my_results.size(); ++var1) {
               var16 = (TLRPC.Peer)var8.my_results.get(var1);
               var12 = var16.user_id;
               if (var12 != 0) {
                  var17 = (TLRPC.User)var10.get(var12);
                  var14 = null;
               } else {
                  label147: {
                     var12 = var16.chat_id;
                     if (var12 != 0) {
                        var14 = (TLRPC.Chat)var9.get(var12);
                     } else {
                        var12 = var16.channel_id;
                        if (var12 == 0) {
                           var14 = null;
                           var17 = var14;
                           break label147;
                        }

                        var14 = (TLRPC.Chat)var9.get(var12);
                     }

                     var17 = null;
                  }
               }

               if (var14 != null) {
                  this.localServerSearch.add(var14);
                  this.globalSearchMap.put(-var14.id, var14);
               } else if (var17 != null) {
                  this.localServerSearch.add(var17);
                  this.globalSearchMap.put(((TLRPC.User)var17).id, var17);
               }
            }
         }

         this.lastFoundUsername = var7.toLowerCase();
         ArrayList var18 = this.localSearchResults;
         if (var18 != null) {
            this.mergeResults(var18);
         }

         this.mergeExcludeResults();
         this.delegate.onDataSetChanged();
      }

      this.reqId = 0;
   }

   // $FF: synthetic method
   public void lambda$null$5$SearchAdapterHelper(ArrayList var1, HashMap var2) {
      this.setHashtags(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$putRecentHashtags$7$SearchAdapterHelper(ArrayList var1) {
      Exception var10000;
      label70: {
         SQLitePreparedStatement var2;
         boolean var10001;
         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
            var2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO hashtag_recent_v2 VALUES(?, ?)");
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label70;
         }

         int var3 = 0;

         label62:
         while(true) {
            int var4;
            try {
               var4 = var1.size();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break;
            }

            byte var5 = 100;
            if (var3 >= var4 || var3 == 100) {
               try {
                  var2.dispose();
                  MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
                  if (var1.size() < 100) {
                     return;
                  }

                  MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }

               var3 = var5;

               while(true) {
                  try {
                     if (var3 >= var1.size()) {
                        break;
                     }

                     SQLiteDatabase var15 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
                     StringBuilder var14 = new StringBuilder();
                     var14.append("DELETE FROM hashtag_recent_v2 WHERE id = '");
                     var14.append(((SearchAdapterHelper.HashtagObject)var1.get(var3)).hashtag);
                     var14.append("'");
                     var15.executeFast(var14.toString()).stepThis().dispose();
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label62;
                  }

                  ++var3;
               }

               try {
                  MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
                  return;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }

            try {
               SearchAdapterHelper.HashtagObject var6 = (SearchAdapterHelper.HashtagObject)var1.get(var3);
               var2.requery();
               var2.bindString(1, var6.hashtag);
               var2.bindInteger(2, var6.date);
               var2.step();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            ++var3;
         }
      }

      Exception var13 = var10000;
      FileLog.e((Throwable)var13);
   }

   // $FF: synthetic method
   public void lambda$queryServerSearch$1$SearchAdapterHelper(int var1, String var2, boolean var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapterHelper$v5cwP_i_1geBZNEja0OzJuPoFoM(this, var1, var5, var4, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$queryServerSearch$3$SearchAdapterHelper(int var1, boolean var2, boolean var3, boolean var4, String var5, TLObject var6, TLRPC.TL_error var7) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapterHelper$BW0zXscUb5FIQ5wJIg2kAIcfk_Q(this, var1, var7, var6, var2, var3, var4, var5));
   }

   public boolean loadRecentHashtags() {
      if (this.hashtagsLoadedFromDb) {
         return true;
      } else {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$DPsjy4ay_Xo08TMx7YPJqUY2IzQ(this));
         return false;
      }
   }

   public void mergeExcludeResults() {
      SearchAdapterHelper.SearchAdapterHelperDelegate var1 = this.delegate;
      if (var1 != null) {
         SparseArray var5 = var1.getExcludeUsers();
         if (var5 != null) {
            int var2 = 0;

            for(int var3 = var5.size(); var2 < var3; ++var2) {
               TLRPC.User var4 = (TLRPC.User)this.globalSearchMap.get(var5.keyAt(var2));
               if (var4 != null) {
                  this.globalSearch.remove(var4);
                  this.localServerSearch.remove(var4);
                  this.globalSearchMap.remove(var4.id);
               }
            }

         }
      }
   }

   public void mergeResults(ArrayList var1) {
      this.localSearchResults = var1;
      if (this.globalSearchMap.size() != 0 && var1 != null) {
         int var2 = var1.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            TLObject var4 = (TLObject)var1.get(var3);
            if (var4 instanceof TLRPC.User) {
               TLRPC.User var6 = (TLRPC.User)var4;
               TLRPC.User var5 = (TLRPC.User)this.globalSearchMap.get(var6.id);
               if (var5 != null) {
                  this.globalSearch.remove(var5);
                  this.localServerSearch.remove(var5);
                  this.globalSearchMap.remove(var5.id);
               }

               TLObject var8 = (TLObject)this.groupSearchMap.get(var6.id);
               if (var8 != null) {
                  this.groupSearch.remove(var8);
                  this.groupSearchMap.remove(var6.id);
               }
            } else if (var4 instanceof TLRPC.Chat) {
               TLRPC.Chat var7 = (TLRPC.Chat)var4;
               var7 = (TLRPC.Chat)this.globalSearchMap.get(-var7.id);
               if (var7 != null) {
                  this.globalSearch.remove(var7);
                  this.localServerSearch.remove(var7);
                  this.globalSearchMap.remove(-var7.id);
               }
            }
         }
      }

   }

   public void queryServerSearch(String var1, boolean var2, boolean var3, boolean var4, boolean var5, int var6, int var7) {
      if (this.reqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
         this.reqId = 0;
      }

      if (this.channelReqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
         this.channelReqId = 0;
      }

      if (var1 == null) {
         this.groupSearch.clear();
         this.groupSearchMap.clear();
         this.globalSearch.clear();
         this.globalSearchMap.clear();
         this.localServerSearch.clear();
         this.lastReqId = 0;
         this.channelLastReqId = 0;
         this.delegate.onDataSetChanged();
      } else {
         if (var1.length() > 0) {
            if (var6 != 0) {
               TLRPC.TL_channels_getParticipants var8 = new TLRPC.TL_channels_getParticipants();
               if (var7 == 1) {
                  var8.filter = new TLRPC.TL_channelParticipantsAdmins();
               } else if (var7 == 3) {
                  var8.filter = new TLRPC.TL_channelParticipantsBanned();
               } else if (var7 == 0) {
                  var8.filter = new TLRPC.TL_channelParticipantsKicked();
               } else {
                  var8.filter = new TLRPC.TL_channelParticipantsSearch();
               }

               var8.filter.q = var1;
               var8.limit = 50;
               var8.offset = 0;
               var8.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(var6);
               var6 = this.channelLastReqId + 1;
               this.channelLastReqId = var6;
               this.channelReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg(this, var6, var1, var5), 2);
            } else {
               this.lastFoundChannel = var1.toLowerCase();
            }
         } else {
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            this.channelLastReqId = 0;
            this.delegate.onDataSetChanged();
         }

         if (var2) {
            if (var1.length() > 0) {
               TLRPC.TL_contacts_search var9 = new TLRPC.TL_contacts_search();
               var9.q = var1;
               var9.limit = 50;
               var6 = this.lastReqId + 1;
               this.lastReqId = var6;
               this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, new _$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA(this, var6, var3, var4, var5, var1), 2);
            } else {
               this.globalSearch.clear();
               this.globalSearchMap.clear();
               this.localServerSearch.clear();
               this.lastReqId = 0;
               this.delegate.onDataSetChanged();
            }
         }

      }
   }

   public void setDelegate(SearchAdapterHelper.SearchAdapterHelperDelegate var1) {
      this.delegate = var1;
   }

   public void setHashtags(ArrayList var1, HashMap var2) {
      this.hashtags = var1;
      this.hashtagsByText = var2;
      this.hashtagsLoadedFromDb = true;
      this.delegate.onSetHashtags(var1, var2);
   }

   public void unloadRecentHashtags() {
      this.hashtagsLoadedFromDb = false;
   }

   protected static final class DialogSearchResult {
      public int date;
      public CharSequence name;
      public TLObject object;
   }

   public static class HashtagObject {
      int date;
      String hashtag;
   }

   public interface SearchAdapterHelperDelegate {
      SparseArray getExcludeUsers();

      void onDataSetChanged();

      void onSetHashtags(ArrayList var1, HashMap var2);
   }
}
