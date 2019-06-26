package org.telegram.ui.Adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Components.RecyclerListView;

public class DialogsSearchAdapter extends RecyclerListView.SelectionAdapter {
   private int currentAccount;
   private DialogsSearchAdapter.DialogsSearchAdapterDelegate delegate;
   private int dialogsType;
   private RecyclerListView innerListView;
   private String lastMessagesSearchString;
   private int lastReqId;
   private int lastSearchId = 0;
   private String lastSearchText;
   private Context mContext;
   private boolean messagesSearchEndReached;
   private int needMessagesSearch;
   private int nextSearchRate;
   private ArrayList recentSearchObjects;
   private LongSparseArray recentSearchObjectsById;
   private int reqId = 0;
   private SearchAdapterHelper searchAdapterHelper;
   private ArrayList searchResult = new ArrayList();
   private ArrayList searchResultHashtags = new ArrayList();
   private ArrayList searchResultMessages = new ArrayList();
   private ArrayList searchResultNames = new ArrayList();
   private Runnable searchRunnable;
   private Runnable searchRunnable2;
   private boolean searchWas;
   private int selfUserId;

   public DialogsSearchAdapter(Context var1, int var2, int var3) {
      this.currentAccount = UserConfig.selectedAccount;
      this.recentSearchObjects = new ArrayList();
      this.recentSearchObjectsById = new LongSparseArray();
      this.searchAdapterHelper = new SearchAdapterHelper(false);
      this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() {
         // $FF: synthetic method
         public SparseArray getExcludeUsers() {
            return SearchAdapterHelper$SearchAdapterHelperDelegate$_CC.$default$getExcludeUsers(this);
         }

         public void onDataSetChanged() {
            DialogsSearchAdapter.this.searchWas = true;
            if (!DialogsSearchAdapter.this.searchAdapterHelper.isSearchInProgress() && DialogsSearchAdapter.this.delegate != null) {
               DialogsSearchAdapter.this.delegate.searchStateChanged(false);
            }

            DialogsSearchAdapter.this.notifyDataSetChanged();
         }

         public void onSetHashtags(ArrayList var1, HashMap var2) {
            for(int var3 = 0; var3 < var1.size(); ++var3) {
               DialogsSearchAdapter.this.searchResultHashtags.add(((SearchAdapterHelper.HashtagObject)var1.get(var3)).hashtag);
            }

            if (DialogsSearchAdapter.this.delegate != null) {
               DialogsSearchAdapter.this.delegate.searchStateChanged(false);
            }

            DialogsSearchAdapter.this.notifyDataSetChanged();
         }
      });
      this.mContext = var1;
      this.needMessagesSearch = var2;
      this.dialogsType = var3;
      this.selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
      this.loadRecentSearch();
      DataQuery.getInstance(this.currentAccount).loadHints(true);
   }

   // $FF: synthetic method
   static int lambda$null$2(DialogsSearchAdapter.RecentSearchObject var0, DialogsSearchAdapter.RecentSearchObject var1) {
      int var2 = var0.date;
      int var3 = var1.date;
      if (var2 < var3) {
         return 1;
      } else {
         return var2 > var3 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static int lambda$null$7(DialogsSearchAdapter.DialogSearchResult var0, DialogsSearchAdapter.DialogSearchResult var1) {
      int var2 = var0.date;
      int var3 = var1.date;
      if (var2 < var3) {
         return 1;
      } else {
         return var2 > var3 ? -1 : 0;
      }
   }

   private void searchDialogsInternal(String var1, int var2) {
      if (this.needMessagesSearch != 2) {
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$jlz_Jg93hVH6t3KHP9A_P2d1MzU(this, var1, var2));
      }
   }

   private void searchMessagesInternal(String var1) {
      if (this.needMessagesSearch != 0 && (!TextUtils.isEmpty(this.lastMessagesSearchString) || !TextUtils.isEmpty(var1))) {
         if (this.reqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
         }

         if (TextUtils.isEmpty(var1)) {
            this.searchResultMessages.clear();
            this.lastReqId = 0;
            this.lastMessagesSearchString = null;
            this.searchWas = false;
            this.notifyDataSetChanged();
            DialogsSearchAdapter.DialogsSearchAdapterDelegate var5 = this.delegate;
            if (var5 != null) {
               var5.searchStateChanged(false);
            }

            return;
         }

         TLRPC.TL_messages_searchGlobal var2 = new TLRPC.TL_messages_searchGlobal();
         var2.limit = 20;
         var2.q = var1;
         int var4;
         if (var1.equals(this.lastMessagesSearchString) && !this.searchResultMessages.isEmpty()) {
            label32: {
               ArrayList var3 = this.searchResultMessages;
               MessageObject var6 = (MessageObject)var3.get(var3.size() - 1);
               var2.offset_id = var6.getId();
               var2.offset_rate = this.nextSearchRate;
               TLRPC.Peer var7 = var6.messageOwner.to_id;
               var4 = var7.channel_id;
               if (var4 == 0) {
                  var4 = var7.chat_id;
                  if (var4 == 0) {
                     var4 = var7.user_id;
                     break label32;
                  }
               }

               var4 = -var4;
            }

            var2.offset_peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var4);
         } else {
            var2.offset_rate = 0;
            var2.offset_id = 0;
            var2.offset_peer = new TLRPC.TL_inputPeerEmpty();
         }

         this.lastMessagesSearchString = var1;
         var4 = this.lastReqId + 1;
         this.lastReqId = var4;
         this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$DialogsSearchAdapter$jvBeESgA8AL2rHOVVbFgj80mlPg(this, var4, var2), 2);
      }

   }

   private void setRecentSearch(ArrayList var1, LongSparseArray var2) {
      this.recentSearchObjects = var1;
      this.recentSearchObjectsById = var2;

      for(int var3 = 0; var3 < this.recentSearchObjects.size(); ++var3) {
         DialogsSearchAdapter.RecentSearchObject var4 = (DialogsSearchAdapter.RecentSearchObject)this.recentSearchObjects.get(var3);
         TLObject var5 = var4.object;
         if (var5 instanceof TLRPC.User) {
            MessagesController.getInstance(this.currentAccount).putUser((TLRPC.User)var4.object, true);
         } else if (var5 instanceof TLRPC.Chat) {
            MessagesController.getInstance(this.currentAccount).putChat((TLRPC.Chat)var4.object, true);
         } else if (var5 instanceof TLRPC.EncryptedChat) {
            MessagesController.getInstance(this.currentAccount).putEncryptedChat((TLRPC.EncryptedChat)var4.object, true);
         }
      }

      this.notifyDataSetChanged();
   }

   private void updateSearchResults(ArrayList var1, ArrayList var2, ArrayList var3, int var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsSearchAdapter$_rG_J91Hu2opSK8sjEhFEpnZiuA(this, var4, var1, var3, var2));
   }

   public void addHashtagsFromMessage(CharSequence var1) {
      this.searchAdapterHelper.addHashtagsFromMessage(var1);
   }

   public void clearRecentHashtags() {
      this.searchAdapterHelper.clearRecentHashtags();
      this.searchResultHashtags.clear();
      this.notifyDataSetChanged();
   }

   public void clearRecentSearch() {
      this.recentSearchObjectsById = new LongSparseArray();
      this.recentSearchObjects = new ArrayList();
      this.notifyDataSetChanged();
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$It_IWmufaVpNeuW4BM4G8iqCxcU(this));
   }

   public RecyclerListView getInnerListView() {
      return this.innerListView;
   }

   public Object getItem(int var1) {
      boolean var2 = this.isRecentSearchDisplayed();
      int var3 = 0;
      byte var4 = 0;
      if (var2) {
         if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
            var4 = 2;
         }

         if (var1 > var4) {
            var1 = var1 - 1 - var4;
            if (var1 < this.recentSearchObjects.size()) {
               TLObject var12 = ((DialogsSearchAdapter.RecentSearchObject)this.recentSearchObjects.get(var1)).object;
               Object var14;
               if (var12 instanceof TLRPC.User) {
                  TLRPC.User var6 = MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.User)var12).id);
                  var14 = var12;
                  if (var6 != null) {
                     var14 = var6;
                  }
               } else {
                  var14 = var12;
                  if (var12 instanceof TLRPC.Chat) {
                     TLRPC.Chat var13 = MessagesController.getInstance(this.currentAccount).getChat(((TLRPC.Chat)var12).id);
                     var14 = var12;
                     if (var13 != null) {
                        var14 = var13;
                     }
                  }
               }

               return var14;
            }
         }

         return null;
      } else if (!this.searchResultHashtags.isEmpty()) {
         return var1 > 0 ? this.searchResultHashtags.get(var1 - 1) : null;
      } else {
         ArrayList var7 = this.searchAdapterHelper.getGlobalSearch();
         ArrayList var5 = this.searchAdapterHelper.getLocalServerSearch();
         int var8 = this.searchResult.size();
         int var9 = var5.size();
         int var11;
         if (var7.isEmpty()) {
            var11 = 0;
         } else {
            var11 = var7.size() + 1;
         }

         if (!this.searchResultMessages.isEmpty()) {
            var3 = this.searchResultMessages.size() + 1;
         }

         if (var1 >= 0 && var1 < var8) {
            return this.searchResult.get(var1);
         } else if (var1 >= var8 && var1 < var9 + var8) {
            return var5.get(var1 - var8);
         } else if (var1 > var8 + var9 && var1 < var11 + var8 + var9) {
            return var7.get(var1 - var8 - var9 - 1);
         } else {
            int var10 = var11 + var8;
            return var1 > var10 + var9 && var1 < var10 + var3 + var9 ? this.searchResultMessages.get(var1 - var8 - var11 - var9 - 1) : null;
         }
      }
   }

   public int getItemCount() {
      int var3;
      if (this.isRecentSearchDisplayed()) {
         boolean var1 = this.recentSearchObjects.isEmpty();
         byte var6 = 0;
         if (!var1) {
            var3 = this.recentSearchObjects.size() + 1;
         } else {
            var3 = 0;
         }

         if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
            var6 = 2;
         }

         return var3 + var6;
      } else if (!this.searchResultHashtags.isEmpty()) {
         return this.searchResultHashtags.size() + 1;
      } else {
         var3 = this.searchResult.size();
         int var2 = this.searchAdapterHelper.getLocalServerSearch().size();
         int var4 = this.searchAdapterHelper.getGlobalSearch().size();
         int var5 = this.searchResultMessages.size();
         var2 += var3;
         var3 = var2;
         if (var4 != 0) {
            var3 = var2 + var4 + 1;
         }

         var2 = var3;
         if (var5 != 0) {
            var2 = var3 + var5 + 1 + (this.messagesSearchEndReached ^ 1);
         }

         return var2;
      }
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public int getItemViewType(int var1) {
      boolean var2 = this.isRecentSearchDisplayed();
      byte var3 = 1;
      if (var2) {
         if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
            var3 = 2;
         } else {
            var3 = 0;
         }

         if (var1 <= var3) {
            return var1 != var3 && var1 % 2 != 0 ? 5 : 1;
         } else {
            return 0;
         }
      } else if (!this.searchResultHashtags.isEmpty()) {
         byte var8;
         if (var1 == 0) {
            var8 = var3;
         } else {
            var8 = 4;
         }

         return var8;
      } else {
         ArrayList var4 = this.searchAdapterHelper.getGlobalSearch();
         int var5 = this.searchResult.size();
         int var6 = this.searchAdapterHelper.getLocalServerSearch().size();
         int var9;
         if (var4.isEmpty()) {
            var9 = 0;
         } else {
            var9 = var4.size() + 1;
         }

         int var7;
         if (this.searchResultMessages.isEmpty()) {
            var7 = 0;
         } else {
            var7 = this.searchResultMessages.size() + 1;
         }

         if (var1 >= 0 && var1 < var5 + var6 || var1 > var5 + var6 && var1 < var9 + var5 + var6) {
            return 0;
         } else {
            var9 += var5;
            if (var1 > var9 + var6 && var1 < var9 + var7 + var6) {
               return 2;
            } else {
               return var7 != 0 && var1 == var9 + var7 + var6 ? 3 : 1;
            }
         }
      }
   }

   public String getLastSearchString() {
      return this.lastMessagesSearchString;
   }

   public boolean hasRecentRearch() {
      boolean var1;
      if (this.recentSearchObjects.isEmpty() && DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      int var2 = var1.getItemViewType();
      boolean var3 = true;
      if (var2 == 1 || var2 == 3) {
         var3 = false;
      }

      return var3;
   }

   public boolean isGlobalSearch(int var1) {
      if (this.isRecentSearchDisplayed()) {
         return false;
      } else if (!this.searchResultHashtags.isEmpty()) {
         return false;
      } else {
         ArrayList var2 = this.searchAdapterHelper.getGlobalSearch();
         ArrayList var3 = this.searchAdapterHelper.getLocalServerSearch();
         int var4 = this.searchResult.size();
         int var5 = var3.size();
         int var6;
         if (var2.isEmpty()) {
            var6 = 0;
         } else {
            var6 = var2.size() + 1;
         }

         int var7;
         if (this.searchResultMessages.isEmpty()) {
            var7 = 0;
         } else {
            var7 = this.searchResultMessages.size() + 1;
         }

         if (var1 >= 0 && var1 < var4) {
            return false;
         } else if (var1 >= var4 && var1 < var5 + var4) {
            return false;
         } else if (var1 > var4 + var5 && var1 < var6 + var4 + var5) {
            return true;
         } else {
            var6 += var4;
            if (var1 > var6 + var5 && var1 < var6 + var7 + var5) {
            }

            return false;
         }
      }
   }

   public boolean isMessagesSearchEndReached() {
      return this.messagesSearchEndReached;
   }

   public boolean isRecentSearchDisplayed() {
      boolean var1;
      if (this.needMessagesSearch == 2 || this.searchWas || this.recentSearchObjects.isEmpty() && DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$clearRecentSearch$6$DialogsSearchAdapter() {
      try {
         MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM search_recent WHERE 1").stepThis().dispose();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$loadRecentSearch$4$DialogsSearchAdapter() {
      Exception var10000;
      label226: {
         SQLiteDatabase var1;
         boolean var10001;
         try {
            var1 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
         } catch (Exception var35) {
            var10000 = var35;
            var10001 = false;
            break label226;
         }

         byte var2 = 0;

         SQLiteCursor var3;
         ArrayList var4;
         ArrayList var5;
         ArrayList var6;
         ArrayList var7;
         LongSparseArray var36;
         try {
            var3 = var1.queryFinalized("SELECT did, date FROM search_recent WHERE 1");
            var4 = new ArrayList();
            var5 = new ArrayList();
            var6 = new ArrayList();
            new ArrayList();
            var7 = new ArrayList();
            var36 = new LongSparseArray();
         } catch (Exception var28) {
            var10000 = var28;
            var10001 = false;
            break label226;
         }

         long var8;
         int var10;
         while(true) {
            try {
               if (!var3.next()) {
                  break;
               }

               var8 = var3.longValue(0);
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label226;
            }

            boolean var43;
            label214: {
               label236: {
                  var10 = (int)var8;
                  int var11 = (int)(var8 >> 32);
                  if (var10 != 0) {
                     if (var11 == 1) {
                        try {
                           if (this.dialogsType == 0 && !var5.contains(var10)) {
                              var5.add(var10);
                              break label236;
                           }
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label226;
                        }
                     } else if (var10 > 0) {
                        try {
                           if (this.dialogsType != 2 && !var4.contains(var10)) {
                              var4.add(var10);
                              break label236;
                           }
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label226;
                        }
                     } else {
                        var10 = -var10;

                        try {
                           if (!var5.contains(var10)) {
                              var5.add(var10);
                              break label236;
                           }
                        } catch (Exception var31) {
                           var10000 = var31;
                           var10001 = false;
                           break label226;
                        }
                     }
                  } else {
                     label235: {
                        try {
                           if (this.dialogsType != 0 && this.dialogsType != 3) {
                              break label235;
                           }
                        } catch (Exception var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label226;
                        }

                        try {
                           if (!var6.contains(var11)) {
                              var6.add(var11);
                              break label236;
                           }
                        } catch (Exception var30) {
                           var10000 = var30;
                           var10001 = false;
                           break label226;
                        }
                     }
                  }

                  var43 = false;
                  break label214;
               }

               var43 = true;
            }

            if (var43) {
               try {
                  DialogsSearchAdapter.RecentSearchObject var12 = new DialogsSearchAdapter.RecentSearchObject();
                  var12.did = var8;
                  var12.date = var3.intValue(1);
                  var7.add(var12);
                  var36.put(var12.did, var12);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label226;
               }
            }
         }

         boolean var13;
         ArrayList var44;
         try {
            var3.dispose();
            var44 = new ArrayList();
            var13 = var6.isEmpty();
         } catch (Exception var26) {
            var10000 = var26;
            var10001 = false;
            break label226;
         }

         if (!var13) {
            ArrayList var38;
            try {
               var38 = new ArrayList();
               MessagesStorage.getInstance(this.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", var6), var38, var4);
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label226;
            }

            var10 = 0;

            while(true) {
               try {
                  if (var10 >= var38.size()) {
                     break;
                  }

                  ((DialogsSearchAdapter.RecentSearchObject)var36.get((long)((TLRPC.EncryptedChat)var38.get(var10)).id << 32)).object = (TLObject)var38.get(var10);
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label226;
               }

               ++var10;
            }
         }

         DialogsSearchAdapter.RecentSearchObject var42;
         label230: {
            try {
               if (var5.isEmpty()) {
                  break label230;
               }

               var6 = new ArrayList();
               MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", var5), var6);
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label226;
            }

            var10 = 0;

            while(true) {
               TLRPC.Chat var41;
               label237: {
                  try {
                     if (var10 >= var6.size()) {
                        break;
                     }

                     var41 = (TLRPC.Chat)var6.get(var10);
                     if (var41.id > 0) {
                        var8 = (long)(-var41.id);
                        break label237;
                     }
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label226;
                  }

                  try {
                     var8 = AndroidUtilities.makeBroadcastId(var41.id);
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label226;
                  }
               }

               label154: {
                  label153: {
                     try {
                        if (var41.migrated_to == null) {
                           break label153;
                        }

                        var42 = (DialogsSearchAdapter.RecentSearchObject)var36.get(var8);
                        var36.remove(var8);
                     } catch (Exception var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label226;
                     }

                     if (var42 != null) {
                        try {
                           var7.remove(var42);
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label226;
                        }
                     }
                     break label154;
                  }

                  try {
                     ((DialogsSearchAdapter.RecentSearchObject)var36.get(var8)).object = var41;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label226;
                  }
               }

               ++var10;
            }
         }

         label233: {
            try {
               if (var4.isEmpty()) {
                  break label233;
               }

               MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", var4), var44);
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label226;
            }

            var10 = var2;

            while(true) {
               TLRPC.User var39;
               try {
                  if (var10 >= var44.size()) {
                     break;
                  }

                  var39 = (TLRPC.User)var44.get(var10);
                  var42 = (DialogsSearchAdapter.RecentSearchObject)var36.get((long)var39.id);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label226;
               }

               if (var42 != null) {
                  try {
                     var42.object = var39;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label226;
                  }
               }

               ++var10;
            }
         }

         try {
            Collections.sort(var7, _$$Lambda$DialogsSearchAdapter$TgrSEhniISqCg6ct5i9NTHhT7C8.INSTANCE);
            _$$Lambda$DialogsSearchAdapter$YAAaRoGgRkDmshNt90P0fNwfz_U var40 = new _$$Lambda$DialogsSearchAdapter$YAAaRoGgRkDmshNt90P0fNwfz_U(this, var7, var36);
            AndroidUtilities.runOnUIThread(var40);
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var37 = var10000;
      FileLog.e((Throwable)var37);
   }

   // $FF: synthetic method
   public void lambda$null$0$DialogsSearchAdapter(int var1, TLRPC.TL_error var2, TLObject var3, TLRPC.TL_messages_searchGlobal var4) {
      if (var1 == this.lastReqId && var2 == null) {
         TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var3;
         MessagesStorage var6 = MessagesStorage.getInstance(this.currentAccount);
         ArrayList var13 = var5.users;
         ArrayList var11 = var5.chats;
         boolean var7 = true;
         var6.putUsersAndChats(var13, var11, true, true);
         MessagesController.getInstance(this.currentAccount).putUsers(var5.users, false);
         MessagesController.getInstance(this.currentAccount).putChats(var5.chats, false);
         if (var4.offset_id == 0) {
            this.searchResultMessages.clear();
         }

         this.nextSearchRate = var5.next_rate;

         boolean var10;
         for(var1 = 0; var1 < var5.messages.size(); ++var1) {
            TLRPC.Message var17 = (TLRPC.Message)var5.messages.get(var1);
            this.searchResultMessages.add(new MessageObject(this.currentAccount, var17, false));
            long var8 = MessageObject.getDialogId(var17);
            ConcurrentHashMap var12;
            if (var17.out) {
               var12 = MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max;
            } else {
               var12 = MessagesController.getInstance(this.currentAccount).dialogs_read_inbox_max;
            }

            Integer var16 = (Integer)var12.get(var8);
            Integer var15 = var16;
            if (var16 == null) {
               var15 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var17.out, var8);
               var12.put(var8, var15);
            }

            if (var15 < var17.id) {
               var10 = true;
            } else {
               var10 = false;
            }

            var17.unread = var10;
         }

         this.searchWas = true;
         if (var5.messages.size() != 20) {
            var10 = var7;
         } else {
            var10 = false;
         }

         this.messagesSearchEndReached = var10;
         this.notifyDataSetChanged();
      }

      DialogsSearchAdapter.DialogsSearchAdapterDelegate var14 = this.delegate;
      if (var14 != null) {
         var14.searchStateChanged(false);
      }

      this.reqId = 0;
   }

   // $FF: synthetic method
   public void lambda$null$10$DialogsSearchAdapter(int var1, String var2) {
      this.searchRunnable2 = null;
      if (var1 == this.lastSearchId) {
         if (this.needMessagesSearch != 2) {
            this.searchAdapterHelper.queryServerSearch(var2, true, true, true, true, 0, 0);
         }

         this.searchMessagesInternal(var2);
      }
   }

   // $FF: synthetic method
   public void lambda$null$3$DialogsSearchAdapter(ArrayList var1, LongSparseArray var2) {
      this.setRecentSearch(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$onBindViewHolder$14$DialogsSearchAdapter(View var1) {
      DialogsSearchAdapter.DialogsSearchAdapterDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.needClearList();
      }

   }

   // $FF: synthetic method
   public void lambda$onBindViewHolder$15$DialogsSearchAdapter(View var1) {
      DialogsSearchAdapter.DialogsSearchAdapterDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.needClearList();
      }

   }

   // $FF: synthetic method
   public void lambda$onCreateViewHolder$12$DialogsSearchAdapter(View var1, int var2) {
      DialogsSearchAdapter.DialogsSearchAdapterDelegate var3 = this.delegate;
      if (var3 != null) {
         var3.didPressedOnSubDialog((long)(Integer)var1.getTag());
      }

   }

   // $FF: synthetic method
   public boolean lambda$onCreateViewHolder$13$DialogsSearchAdapter(View var1, int var2) {
      DialogsSearchAdapter.DialogsSearchAdapterDelegate var3 = this.delegate;
      if (var3 != null) {
         var3.needRemoveHint((Integer)var1.getTag());
      }

      return true;
   }

   // $FF: synthetic method
   public void lambda$putRecentSearch$5$DialogsSearchAdapter(long var1) {
      try {
         SQLitePreparedStatement var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO search_recent VALUES(?, ?)");
         var3.requery();
         var3.bindLong(1, var1);
         var3.bindInteger(2, (int)(System.currentTimeMillis() / 1000L));
         var3.step();
         var3.dispose();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$searchDialogs$11$DialogsSearchAdapter(String var1, int var2) {
      this.searchRunnable = null;
      this.searchDialogsInternal(var1, var2);
      _$$Lambda$DialogsSearchAdapter$VsX50TGE7QZO8RUCru1or1M1bIg var3 = new _$$Lambda$DialogsSearchAdapter$VsX50TGE7QZO8RUCru1or1M1bIg(this, var2, var1);
      this.searchRunnable2 = var3;
      AndroidUtilities.runOnUIThread(var3);
   }

   // $FF: synthetic method
   public void lambda$searchDialogsInternal$8$DialogsSearchAdapter(String var1, int var2) {
      Exception var10000;
      label889: {
         String var3;
         String var4;
         boolean var10001;
         ArrayList var105;
         ArrayList var110;
         try {
            var3 = LocaleController.getString("SavedMessages", 2131560633).toLowerCase();
            var4 = var1.trim().toLowerCase();
            if (var4.length() == 0) {
               this.lastSearchId = -1;
               var105 = new ArrayList();
               var110 = new ArrayList();
               ArrayList var123 = new ArrayList();
               this.updateSearchResults(var105, var110, var123, this.lastSearchId);
               return;
            }
         } catch (Exception var104) {
            var10000 = var104;
            var10001 = false;
            break label889;
         }

         String var5;
         label842: {
            label850: {
               try {
                  var5 = LocaleController.getInstance().getTranslitString(var4);
                  if (var4.equals(var5)) {
                     break label850;
                  }
               } catch (Exception var103) {
                  var10000 = var103;
                  var10001 = false;
                  break label889;
               }

               var1 = var5;

               try {
                  if (var5.length() != 0) {
                     break label842;
                  }
               } catch (Exception var102) {
                  var10000 = var102;
                  var10001 = false;
                  break label889;
               }
            }

            var1 = null;
         }

         byte var6;
         if (var1 != null) {
            var6 = 1;
         } else {
            var6 = 0;
         }

         String[] var7;
         try {
            var7 = new String[var6 + 1];
         } catch (Exception var101) {
            var10000 = var101;
            var10001 = false;
            break label889;
         }

         var7[0] = var4;
         if (var1 != null) {
            var7[1] = var1;
         }

         ArrayList var8;
         ArrayList var9;
         LongSparseArray var10;
         SQLiteCursor var11;
         try {
            var110 = new ArrayList();
            var8 = new ArrayList();
            var9 = new ArrayList();
            var105 = new ArrayList();
            var10 = new LongSparseArray();
            var11 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 600");
         } catch (Exception var95) {
            var10000 = var95;
            var10001 = false;
            break label889;
         }

         long var12;
         int var15;
         int var115;
         while(true) {
            try {
               if (!var11.next()) {
                  break;
               }

               var12 = var11.longValue(0);
               DialogsSearchAdapter.DialogSearchResult var14 = new DialogsSearchAdapter.DialogSearchResult();
               var14.date = var11.intValue(1);
               var10.put(var12, var14);
            } catch (Exception var99) {
               var10000 = var99;
               var10001 = false;
               break label889;
            }

            var115 = (int)var12;
            var15 = (int)(var12 >> 32);
            if (var115 != 0) {
               if (var15 == 1) {
                  try {
                     if (this.dialogsType == 0 && !var8.contains(var115)) {
                        var8.add(var115);
                     }
                  } catch (Exception var96) {
                     var10000 = var96;
                     var10001 = false;
                     break label889;
                  }
               } else if (var115 > 0) {
                  try {
                     if (this.dialogsType != 2 && !var110.contains(var115)) {
                        var110.add(var115);
                     }
                  } catch (Exception var97) {
                     var10000 = var97;
                     var10001 = false;
                     break label889;
                  }
               } else {
                  var115 = -var115;

                  try {
                     if (!var8.contains(var115)) {
                        var8.add(var115);
                     }
                  } catch (Exception var98) {
                     var10000 = var98;
                     var10001 = false;
                     break label889;
                  }
               }
            } else {
               try {
                  if (this.dialogsType != 0 && this.dialogsType != 3) {
                     continue;
                  }
               } catch (Exception var100) {
                  var10000 = var100;
                  var10001 = false;
                  break label889;
               }

               try {
                  if (!var9.contains(var15)) {
                     var9.add(var15);
                  }
               } catch (Exception var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label889;
               }
            }
         }

         DialogsSearchAdapter.DialogSearchResult var124;
         label786: {
            label785: {
               try {
                  var11.dispose();
                  if (var3.startsWith(var4)) {
                     TLRPC.User var108 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                     var124 = new DialogsSearchAdapter.DialogSearchResult();
                     var124.date = Integer.MAX_VALUE;
                     var124.name = var3;
                     var124.object = var108;
                     var10.put((long)var108.id, var124);
                     break label785;
                  }
               } catch (Exception var93) {
                  var10000 = var93;
                  var10001 = false;
                  break label889;
               }

               var6 = 0;
               break label786;
            }

            var6 = 1;
         }

         boolean var16;
         try {
            var16 = var110.isEmpty();
         } catch (Exception var92) {
            var10000 = var92;
            var10001 = false;
            break label889;
         }

         var4 = ";;;";
         String var17;
         int var18;
         StringBuilder var20;
         NativeByteBuffer var109;
         DialogsSearchAdapter.DialogSearchResult var111;
         String var128;
         if (!var16) {
            Locale var107;
            SQLiteDatabase var125;
            try {
               var125 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
               var107 = Locale.US;
            } catch (Exception var85) {
               var10000 = var85;
               var10001 = false;
               break label889;
            }

            try {
               var11 = var125.queryFinalized(String.format(var107, "SELECT data, status, name FROM users WHERE uid IN(%s)", TextUtils.join(",", var110)));
            } catch (Exception var84) {
               var10000 = var84;
               var10001 = false;
               break label889;
            }

            var15 = var6;

            label770:
            while(true) {
               label762:
               do {
                  while(true) {
                     try {
                        if (!var11.next()) {
                           break label770;
                        }

                        var17 = var11.stringValue(2);
                        var3 = LocaleController.getInstance().getTranslitString(var17);
                     } catch (Exception var86) {
                        var10000 = var86;
                        var10001 = false;
                        break label889;
                     }

                     var5 = var3;

                     label724: {
                        try {
                           if (!var17.equals(var3)) {
                              break label724;
                           }
                        } catch (Exception var87) {
                           var10000 = var87;
                           var10001 = false;
                           break label889;
                        }

                        var5 = null;
                     }

                     try {
                        var115 = var17.lastIndexOf(";;;");
                     } catch (Exception var83) {
                        var10000 = var83;
                        var10001 = false;
                        break label889;
                     }

                     if (var115 != -1) {
                        try {
                           var3 = var17.substring(var115 + 3);
                        } catch (Exception var82) {
                           var10000 = var82;
                           var10001 = false;
                           break label889;
                        }
                     } else {
                        var3 = null;
                     }

                     try {
                        var18 = var7.length;
                     } catch (Exception var81) {
                        var10000 = var81;
                        var10001 = false;
                        break label889;
                     }

                     int var19 = 0;

                     for(var6 = 0; var19 < var18; ++var19) {
                        var128 = var7[var19];

                        label860: {
                           label751: {
                              label861: {
                                 try {
                                    if (var17.startsWith(var128)) {
                                       break label861;
                                    }

                                    var20 = new StringBuilder();
                                    var20.append(" ");
                                    var20.append(var128);
                                    if (var17.contains(var20.toString())) {
                                       break label861;
                                    }
                                 } catch (Exception var90) {
                                    var10000 = var90;
                                    var10001 = false;
                                    break label889;
                                 }

                                 if (var5 == null) {
                                    break label751;
                                 }

                                 try {
                                    if (!var5.startsWith(var128)) {
                                       var20 = new StringBuilder();
                                       var20.append(" ");
                                       var20.append(var128);
                                       if (!var5.contains(var20.toString())) {
                                          break label751;
                                       }
                                    }
                                 } catch (Exception var89) {
                                    var10000 = var89;
                                    var10001 = false;
                                    break label889;
                                 }
                              }

                              var6 = 1;
                              break label860;
                           }

                           if (var3 != null) {
                              label887: {
                                 try {
                                    if (!var3.startsWith(var128)) {
                                       break label887;
                                    }
                                 } catch (Exception var88) {
                                    var10000 = var88;
                                    var10001 = false;
                                    break label889;
                                 }

                                 var6 = 2;
                              }
                           }
                        }

                        if (var6 != 0) {
                           try {
                              var109 = var11.byteBufferValue(0);
                              continue label762;
                           } catch (Exception var80) {
                              var10000 = var80;
                              var10001 = false;
                              break label889;
                           }
                        }
                     }
                  }
               } while(var109 == null);

               TLRPC.User var113;
               label767: {
                  TLRPC.UserStatus var131;
                  try {
                     var113 = TLRPC.User.TLdeserialize(var109, var109.readInt32(false), false);
                     var109.reuse();
                     var111 = (DialogsSearchAdapter.DialogSearchResult)var10.get((long)var113.id);
                     if (var113.status == null) {
                        break label767;
                     }

                     var131 = var113.status;
                     var18 = var11.intValue(1);
                  } catch (Exception var91) {
                     var10000 = var91;
                     var10001 = false;
                     break label889;
                  }

                  try {
                     var131.expires = var18;
                  } catch (Exception var79) {
                     var10000 = var79;
                     var10001 = false;
                     break label889;
                  }
               }

               if (var6 == 1) {
                  try {
                     var111.name = AndroidUtilities.generateSearchName(var113.first_name, var113.last_name, var128);
                  } catch (Exception var78) {
                     var10000 = var78;
                     var10001 = false;
                     break label889;
                  }
               } else {
                  try {
                     StringBuilder var133 = new StringBuilder();
                     var133.append("@");
                     var133.append(var113.username);
                     String var135 = var133.toString();
                     var133 = new StringBuilder();
                     var133.append("@");
                     var133.append(var128);
                     var111.name = AndroidUtilities.generateSearchName(var135, (String)null, var133.toString());
                  } catch (Exception var77) {
                     var10000 = var77;
                     var10001 = false;
                     break label889;
                  }
               }

               try {
                  var111.object = var113;
               } catch (Exception var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label889;
               }

               ++var15;
            }

            try {
               var11.dispose();
            } catch (Exception var75) {
               var10000 = var75;
               var10001 = false;
               break label889;
            }
         } else {
            var15 = var6;
         }

         var115 = var15;

         String var126;
         NativeByteBuffer var127;
         StringBuilder var129;
         label862: {
            SQLiteCursor var118;
            try {
               if (var8.isEmpty()) {
                  break label862;
               }

               var118 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, name FROM chats WHERE uid IN(%s)", TextUtils.join(",", var8)));
            } catch (Exception var74) {
               var10000 = var74;
               var10001 = false;
               break label889;
            }

            var115 = var15;

            label683:
            while(true) {
               TLRPC.Chat var116;
               while(true) {
                  do {
                     do {
                        label652:
                        while(true) {
                           try {
                              if (!var118.next()) {
                                 break label683;
                              }

                              var126 = var118.stringValue(1);
                              var3 = LocaleController.getInstance().getTranslitString(var126);
                           } catch (Exception var68) {
                              var10000 = var68;
                              var10001 = false;
                              break label889;
                           }

                           var5 = var3;

                           label630: {
                              try {
                                 if (!var126.equals(var3)) {
                                    break label630;
                                 }
                              } catch (Exception var69) {
                                 var10000 = var69;
                                 var10001 = false;
                                 break label889;
                              }

                              var5 = null;
                           }

                           try {
                              var18 = var7.length;
                           } catch (Exception var67) {
                              var10000 = var67;
                              var10001 = false;
                              break label889;
                           }

                           for(var15 = 0; var15 < var18; ++var15) {
                              var3 = var7[var15];

                              try {
                                 if (var126.startsWith(var3)) {
                                    break label652;
                                 }

                                 var129 = new StringBuilder();
                                 var129.append(" ");
                                 var129.append(var3);
                                 if (var126.contains(var129.toString())) {
                                    break label652;
                                 }
                              } catch (Exception var70) {
                                 var10000 = var70;
                                 var10001 = false;
                                 break label889;
                              }

                              if (var5 != null) {
                                 try {
                                    if (var5.startsWith(var3)) {
                                       break label652;
                                    }

                                    var129 = new StringBuilder();
                                    var129.append(" ");
                                    var129.append(var3);
                                    if (var5.contains(var129.toString())) {
                                       break label652;
                                    }
                                 } catch (Exception var71) {
                                    var10000 = var71;
                                    var10001 = false;
                                    break label889;
                                 }
                              }
                           }
                        }

                        try {
                           var127 = var118.byteBufferValue(0);
                        } catch (Exception var66) {
                           var10000 = var66;
                           var10001 = false;
                           break label889;
                        }
                     } while(var127 == null);

                     try {
                        var116 = TLRPC.Chat.TLdeserialize(var127, var127.readInt32(false), false);
                        var127.reuse();
                     } catch (Exception var65) {
                        var10000 = var65;
                        var10001 = false;
                        break label889;
                     }
                  } while(var116 == null);

                  try {
                     if (!var116.deactivated && (!ChatObject.isChannel(var116) || !ChatObject.isNotInChat(var116))) {
                        break;
                     }
                  } catch (Exception var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label889;
                  }
               }

               label679: {
                  try {
                     if (var116.id > 0) {
                        var12 = (long)(-var116.id);
                        break label679;
                     }
                  } catch (Exception var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label889;
                  }

                  try {
                     var12 = AndroidUtilities.makeBroadcastId(var116.id);
                  } catch (Exception var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label889;
                  }
               }

               try {
                  var124 = (DialogsSearchAdapter.DialogSearchResult)var10.get(var12);
                  var124.name = AndroidUtilities.generateSearchName(var116.title, (String)null, var3);
                  var124.object = var116;
               } catch (Exception var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label889;
               }

               ++var115;
            }

            try {
               var118.dispose();
            } catch (Exception var62) {
               var10000 = var62;
               var10001 = false;
               break label889;
            }
         }

         TLRPC.User var117;
         label611: {
            label610: {
               SQLiteCursor var121;
               try {
                  if (var9.isEmpty()) {
                     break label610;
                  }

                  var121 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT q.data, u.name, q.user, q.g, q.authkey, q.ttl, u.data, u.status, q.layer, q.seq_in, q.seq_out, q.use_count, q.exchange_id, q.key_date, q.fprint, q.fauthkey, q.khash, q.in_seq_no, q.admin_id, q.mtproto_seq FROM enc_chats as q INNER JOIN users as u ON q.user = u.uid WHERE q.uid IN(%s)", TextUtils.join(",", var9)));
               } catch (Exception var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label889;
               }

               while(true) {
                  label870: {
                     try {
                        if (var121.next()) {
                           var126 = var121.stringValue(1);
                           var3 = LocaleController.getInstance().getTranslitString(var126);
                           break label870;
                        }
                     } catch (Exception var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label889;
                     }

                     try {
                        var121.dispose();
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label889;
                     }

                     var110 = var105;
                     break label611;
                  }

                  var5 = var3;

                  label595: {
                     try {
                        if (!var126.equals(var3)) {
                           break label595;
                        }
                     } catch (Exception var59) {
                        var10000 = var59;
                        var10001 = false;
                        break label889;
                     }

                     var5 = null;
                  }

                  try {
                     var15 = var126.lastIndexOf(";;;");
                  } catch (Exception var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label889;
                  }

                  if (var15 != -1) {
                     try {
                        var3 = var126.substring(var15 + 2);
                     } catch (Exception var53) {
                        var10000 = var53;
                        var10001 = false;
                        break label889;
                     }
                  } else {
                     var3 = null;
                  }

                  var18 = 0;
                  byte var134 = 0;

                  while(true) {
                     try {
                        if (var18 >= var7.length) {
                           break;
                        }
                     } catch (Exception var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label889;
                     }

                     String var119 = var7[var18];

                     byte var132;
                     label587: {
                        label872: {
                           try {
                              if (var126.startsWith(var119)) {
                                 break label872;
                              }

                              var129 = new StringBuilder();
                              var129.append(" ");
                              var129.append(var119);
                              if (var126.contains(var129.toString())) {
                                 break label872;
                              }
                           } catch (Exception var58) {
                              var10000 = var58;
                              var10001 = false;
                              break label889;
                           }

                           if (var5 != null) {
                              try {
                                 if (var5.startsWith(var119)) {
                                    break label872;
                                 }

                                 var129 = new StringBuilder();
                                 var129.append(" ");
                                 var129.append(var119);
                                 if (var5.contains(var129.toString())) {
                                    break label872;
                                 }
                              } catch (Exception var57) {
                                 var10000 = var57;
                                 var10001 = false;
                                 break label889;
                              }
                           }

                           var132 = var134;
                           if (var3 == null) {
                              break label587;
                           }

                           var132 = var134;

                           try {
                              if (!var3.startsWith(var119)) {
                                 break label587;
                              }
                           } catch (Exception var56) {
                              var10000 = var56;
                              var10001 = false;
                              break label889;
                           }

                           var132 = 2;
                           break label587;
                        }

                        var132 = 1;
                     }

                     if (var132 != 0) {
                        try {
                           var109 = var121.byteBufferValue(0);
                        } catch (Exception var52) {
                           var10000 = var52;
                           var10001 = false;
                           break label889;
                        }

                        TLRPC.EncryptedChat var122;
                        if (var109 != null) {
                           try {
                              var122 = TLRPC.EncryptedChat.TLdeserialize(var109, var109.readInt32(false), false);
                              var109.reuse();
                           } catch (Exception var51) {
                              var10000 = var51;
                              var10001 = false;
                              break label889;
                           }
                        } else {
                           var122 = null;
                        }

                        try {
                           var127 = var121.byteBufferValue(6);
                        } catch (Exception var50) {
                           var10000 = var50;
                           var10001 = false;
                           break label889;
                        }

                        if (var127 != null) {
                           try {
                              var117 = TLRPC.User.TLdeserialize(var127, var127.readInt32(false), false);
                              var127.reuse();
                           } catch (Exception var49) {
                              var10000 = var49;
                              var10001 = false;
                              break label889;
                           }
                        } else {
                           var117 = null;
                        }

                        if (var122 != null && var117 != null) {
                           try {
                              var124 = (DialogsSearchAdapter.DialogSearchResult)var10.get((long)var122.id << 32);
                              var122.user_id = var121.intValue(2);
                              var122.a_or_b = var121.byteArrayValue(3);
                              var122.auth_key = var121.byteArrayValue(4);
                              var122.ttl = var121.intValue(5);
                              var122.layer = var121.intValue(8);
                              var122.seq_in = var121.intValue(9);
                              var122.seq_out = var121.intValue(10);
                              var18 = var121.intValue(11);
                              var122.key_use_count_in = (short)((short)(var18 >> 16));
                              var122.key_use_count_out = (short)((short)var18);
                              var122.exchange_id = var121.longValue(12);
                              var122.key_create_date = var121.intValue(13);
                              var122.future_key_fingerprint = var121.longValue(14);
                              var122.future_auth_key = var121.byteArrayValue(15);
                              var122.key_hash = var121.byteArrayValue(16);
                              var122.in_seq_no = var121.intValue(17);
                              var18 = var121.intValue(18);
                           } catch (Exception var48) {
                              var10000 = var48;
                              var10001 = false;
                              break label889;
                           }

                           if (var18 != 0) {
                              try {
                                 var122.admin_id = var18;
                              } catch (Exception var47) {
                                 var10000 = var47;
                                 var10001 = false;
                                 break label889;
                              }
                           }

                           try {
                              var122.mtproto_seq = var121.intValue(19);
                              if (var117.status != null) {
                                 var117.status.expires = var121.intValue(7);
                              }
                           } catch (Exception var46) {
                              var10000 = var46;
                              var10001 = false;
                              break label889;
                           }

                           if (var132 == 1) {
                              try {
                                 SpannableStringBuilder var120 = new SpannableStringBuilder(ContactsController.formatName(var117.first_name, var117.last_name));
                                 var124.name = var120;
                                 var120 = (SpannableStringBuilder)var124.name;
                                 ForegroundColorSpan var130 = new ForegroundColorSpan(Theme.getColor("chats_secretName"));
                                 var120.setSpan(var130, 0, var124.name.length(), 33);
                              } catch (Exception var45) {
                                 var10000 = var45;
                                 var10001 = false;
                                 break label889;
                              }
                           } else {
                              try {
                                 var129 = new StringBuilder();
                                 var129.append("@");
                                 var129.append(var117.username);
                                 var17 = var129.toString();
                                 var129 = new StringBuilder();
                                 var129.append("@");
                                 var129.append(var119);
                                 var124.name = AndroidUtilities.generateSearchName(var17, (String)null, var129.toString());
                              } catch (Exception var44) {
                                 var10000 = var44;
                                 var10001 = false;
                                 break label889;
                              }
                           }

                           try {
                              var124.object = var122;
                              var105.add(var117);
                           } catch (Exception var43) {
                              var10000 = var43;
                              var10001 = false;
                              break label889;
                           }

                           ++var115;
                        }
                        break;
                     }

                     ++var18;
                     var134 = var132;
                  }
               }
            }

            var110 = var105;
         }

         try {
            var105 = new ArrayList(var115);
         } catch (Exception var41) {
            var10000 = var41;
            var10001 = false;
            break label889;
         }

         var115 = 0;

         while(true) {
            try {
               if (var115 >= var10.size()) {
                  break;
               }

               var111 = (DialogsSearchAdapter.DialogSearchResult)var10.valueAt(var115);
               if (var111.object != null && var111.name != null) {
                  var105.add(var111);
               }
            } catch (Exception var40) {
               var10000 = var40;
               var10001 = false;
               break label889;
            }

            ++var115;
         }

         try {
            Collections.sort(var105, _$$Lambda$DialogsSearchAdapter$FVguPxj8QpbjyrNjyvgW9r4iI6c.INSTANCE);
            var9 = new ArrayList();
            var8 = new ArrayList();
         } catch (Exception var39) {
            var10000 = var39;
            var10001 = false;
            break label889;
         }

         var115 = 0;

         while(true) {
            try {
               if (var115 >= var105.size()) {
                  break;
               }

               var111 = (DialogsSearchAdapter.DialogSearchResult)var105.get(var115);
               var9.add(var111.object);
               var8.add(var111.name);
            } catch (Exception var38) {
               var10000 = var38;
               var10001 = false;
               break label889;
            }

            ++var115;
         }

         label876: {
            try {
               if (this.dialogsType == 2) {
                  break label876;
               }

               var11 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid");
            } catch (Exception var37) {
               var10000 = var37;
               var10001 = false;
               break label889;
            }

            var1 = var4;

            label489:
            while(true) {
               while(true) {
                  try {
                     if (!var11.next()) {
                        break label489;
                     }

                     if (var10.indexOfKey((long)var11.intValue(3)) >= 0) {
                        continue;
                     }
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label889;
                  }

                  try {
                     var128 = var11.stringValue(2);
                     var4 = LocaleController.getInstance().getTranslitString(var128);
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label889;
                  }

                  var3 = var4;

                  label484: {
                     try {
                        if (!var128.equals(var4)) {
                           break label484;
                        }
                     } catch (Exception var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label889;
                     }

                     var3 = null;
                  }

                  try {
                     var115 = var128.lastIndexOf(var1);
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label889;
                  }

                  if (var115 != -1) {
                     try {
                        var4 = var128.substring(var115 + 3);
                     } catch (Exception var29) {
                        var10000 = var29;
                        var10001 = false;
                        break label889;
                     }
                  } else {
                     var4 = null;
                  }

                  try {
                     var18 = var7.length;
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label889;
                  }

                  var15 = 0;

                  for(var6 = 0; var15 < var18; ++var15) {
                     var17 = var7[var15];

                     label476: {
                        label879: {
                           try {
                              if (var128.startsWith(var17)) {
                                 break label879;
                              }

                              var20 = new StringBuilder();
                              var20.append(" ");
                              var20.append(var17);
                              if (var128.contains(var20.toString())) {
                                 break label879;
                              }
                           } catch (Exception var35) {
                              var10000 = var35;
                              var10001 = false;
                              break label889;
                           }

                           if (var3 != null) {
                              try {
                                 if (var3.startsWith(var17)) {
                                    break label879;
                                 }

                                 var20 = new StringBuilder();
                                 var20.append(" ");
                                 var20.append(var17);
                                 if (var3.contains(var20.toString())) {
                                    break label879;
                                 }
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label889;
                              }
                           }

                           if (var4 == null) {
                              break label476;
                           }

                           try {
                              if (!var4.startsWith(var17)) {
                                 break label476;
                              }
                           } catch (Exception var33) {
                              var10000 = var33;
                              var10001 = false;
                              break label889;
                           }

                           var6 = 2;
                           break label476;
                        }

                        var6 = 1;
                     }

                     if (var6 != 0) {
                        NativeByteBuffer var112;
                        try {
                           var112 = var11.byteBufferValue(0);
                        } catch (Exception var27) {
                           var10000 = var27;
                           var10001 = false;
                           break label889;
                        }

                        if (var112 != null) {
                           try {
                              var117 = TLRPC.User.TLdeserialize(var112, var112.readInt32(false), false);
                              var112.reuse();
                              if (var117.status != null) {
                                 var117.status.expires = var11.intValue(1);
                              }
                           } catch (Exception var26) {
                              var10000 = var26;
                              var10001 = false;
                              break label889;
                           }

                           if (var6 == 1) {
                              try {
                                 var8.add(AndroidUtilities.generateSearchName(var117.first_name, var117.last_name, var17));
                              } catch (Exception var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break label889;
                              }
                           } else {
                              try {
                                 StringBuilder var114 = new StringBuilder();
                                 var114.append("@");
                                 var114.append(var117.username);
                                 var4 = var114.toString();
                                 var129 = new StringBuilder();
                                 var129.append("@");
                                 var129.append(var17);
                                 var8.add(AndroidUtilities.generateSearchName(var4, (String)null, var129.toString()));
                              } catch (Exception var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label889;
                              }
                           }

                           try {
                              var9.add(var117);
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break label889;
                           }
                        }
                        break;
                     }
                  }
               }
            }

            try {
               var11.dispose();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label889;
            }
         }

         try {
            this.updateSearchResults(var9, var8, var110, var2);
            return;
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
         }
      }

      Exception var106 = var10000;
      FileLog.e((Throwable)var106);
   }

   // $FF: synthetic method
   public void lambda$searchMessagesInternal$1$DialogsSearchAdapter(int var1, TLRPC.TL_messages_searchGlobal var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsSearchAdapter$tzGJ1M1cHM4tSDBBFCCxa18ZhzA(this, var1, var4, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$updateSearchResults$9$DialogsSearchAdapter(int var1, ArrayList var2, ArrayList var3, ArrayList var4) {
      if (var1 == this.lastSearchId) {
         this.searchWas = true;

         for(var1 = 0; var1 < var2.size(); ++var1) {
            TLObject var5 = (TLObject)var2.get(var1);
            if (var5 instanceof TLRPC.User) {
               TLRPC.User var6 = (TLRPC.User)var5;
               MessagesController.getInstance(this.currentAccount).putUser(var6, true);
            } else if (var5 instanceof TLRPC.Chat) {
               TLRPC.Chat var7 = (TLRPC.Chat)var5;
               MessagesController.getInstance(this.currentAccount).putChat(var7, true);
            } else if (var5 instanceof TLRPC.EncryptedChat) {
               TLRPC.EncryptedChat var8 = (TLRPC.EncryptedChat)var5;
               MessagesController.getInstance(this.currentAccount).putEncryptedChat(var8, true);
            }
         }

         MessagesController.getInstance(this.currentAccount).putUsers(var3, true);
         this.searchResult = var2;
         this.searchResultNames = var4;
         this.searchAdapterHelper.mergeResults(this.searchResult);
         this.notifyDataSetChanged();
         if (this.delegate != null) {
            if (this.getItemCount() != 0 || this.searchRunnable2 == null && !this.searchAdapterHelper.isSearchInProgress()) {
               this.delegate.searchStateChanged(false);
            } else {
               this.delegate.searchStateChanged(true);
            }
         }

      }
   }

   public void loadMoreSearchMessages() {
      this.searchMessagesInternal(this.lastMessagesSearchString);
   }

   public void loadRecentSearch() {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$EdS8aWM1r9L4_WkQYwpXcCyFRfM(this));
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      byte var4 = 2;
      boolean var5 = false;
      boolean var6 = false;
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 != 4) {
                     if (var3 == 5) {
                        ((DialogsSearchAdapter.CategoryAdapterRecycler)((RecyclerListView)var1.itemView).getAdapter()).setIndex(var2 / 2);
                     }
                  } else {
                     HashtagSearchCell var18 = (HashtagSearchCell)var1.itemView;
                     var18.setText((CharSequence)this.searchResultHashtags.get(var2 - 1));
                     if (var2 != this.searchResultHashtags.size()) {
                        var6 = true;
                     }

                     var18.setNeedDivider(var6);
                  }
               }
            } else {
               DialogCell var7 = (DialogCell)var1.itemView;
               var6 = var5;
               if (var2 != this.getItemCount() - 1) {
                  var6 = true;
               }

               var7.useSeparator = var6;
               MessageObject var19 = (MessageObject)this.getItem(var2);
               var7.setDialog(var19.getDialogId(), var19, var19.messageOwner.date);
            }
         } else {
            GraySectionCell var20 = (GraySectionCell)var1.itemView;
            if (this.isRecentSearchDisplayed()) {
               if (DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                  var4 = 0;
               }

               if (var2 < var4) {
                  var20.setText(LocaleController.getString("ChatHints", 2131559029));
               } else {
                  var20.setText(LocaleController.getString("Recent", 2131560537), LocaleController.getString("ClearButton", 2131559102), new _$$Lambda$DialogsSearchAdapter$941fnPDSgReuKOmz7WsSoxVuOTY(this));
               }
            } else if (!this.searchResultHashtags.isEmpty()) {
               var20.setText(LocaleController.getString("Hashtags", 2131559634), LocaleController.getString("ClearButton", 2131559102), new _$$Lambda$DialogsSearchAdapter$honrBco_zV9w0fwaI91SKdwfMI0(this));
            } else if (!this.searchAdapterHelper.getGlobalSearch().isEmpty() && var2 == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size()) {
               var20.setText(LocaleController.getString("GlobalSearch", 2131559594));
            } else {
               var20.setText(LocaleController.getString("SearchMessages", 2131560655));
            }
         }
      } else {
         ProfileSearchCell var8 = (ProfileSearchCell)var1.itemView;
         Object var21 = this.getItem(var2);
         Object var9;
         TLRPC.EncryptedChat var10;
         TLRPC.Chat var25;
         if (var21 instanceof TLRPC.User) {
            var21 = (TLRPC.User)var21;
            var9 = ((TLRPC.User)var21).username;
            var25 = null;
            var10 = null;
         } else {
            label231: {
               if (var21 instanceof TLRPC.Chat) {
                  MessagesController var24 = MessagesController.getInstance(this.currentAccount);
                  TLRPC.Chat var22 = (TLRPC.Chat)var21;
                  var25 = var24.getChat(var22.id);
                  if (var25 != null) {
                     var22 = var25;
                  }

                  var9 = var22.username;
                  var25 = var22;
               } else {
                  if (var21 instanceof TLRPC.EncryptedChat) {
                     var10 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(((TLRPC.EncryptedChat)var21).id);
                     var21 = MessagesController.getInstance(this.currentAccount).getUser(var10.user_id);
                     var25 = null;
                     var9 = null;
                     break label231;
                  }

                  var25 = null;
                  var9 = null;
               }

               var21 = null;
               var10 = null;
            }
         }

         String var13;
         Object var31;
         if (this.isRecentSearchDisplayed()) {
            if (var2 != this.getItemCount() - 1) {
               var6 = true;
            } else {
               var6 = false;
            }

            var8.useSeparator = var6;
            var31 = null;
            var9 = null;
            var6 = true;
         } else {
            ArrayList var11 = this.searchAdapterHelper.getGlobalSearch();
            int var12 = this.searchResult.size();
            var3 = this.searchAdapterHelper.getLocalServerSearch().size();
            int var23;
            if (var11.isEmpty()) {
               var23 = 0;
            } else {
               var23 = var11.size() + 1;
            }

            if (var2 != this.getItemCount() - 1 && var2 != var12 + var3 - 1 && var2 != var12 + var23 + var3 - 1) {
               var6 = true;
            } else {
               var6 = false;
            }

            Object var34;
            label194: {
               label193: {
                  var8.useSeparator = var6;
                  String var14;
                  if (var2 < this.searchResult.size()) {
                     CharSequence var26 = (CharSequence)this.searchResultNames.get(var2);
                     var9 = var26;
                     if (var26 == null) {
                        break label193;
                     }

                     var9 = var26;
                     if (var21 == null) {
                        break label193;
                     }

                     var13 = ((TLRPC.User)var21).username;
                     var9 = var26;
                     if (var13 == null) {
                        break label193;
                     }

                     var9 = var26;
                     if (var13.length() <= 0) {
                        break label193;
                     }

                     var14 = var26.toString();
                     StringBuilder var28 = new StringBuilder();
                     var28.append("@");
                     var28.append(((TLRPC.User)var21).username);
                     var9 = var26;
                     if (!var14.startsWith(var28.toString())) {
                        break label193;
                     }

                     var9 = var26;
                  } else {
                     label238: {
                        var14 = this.searchAdapterHelper.getLastFoundUsername();
                        if (!TextUtils.isEmpty(var14)) {
                           String var27;
                           if (var21 != null) {
                              var27 = ContactsController.formatName(((TLRPC.User)var21).first_name, ((TLRPC.User)var21).last_name);
                              var13 = var27.toLowerCase();
                           } else if (var25 != null) {
                              var27 = var25.title;
                              var13 = var27.toLowerCase();
                           } else {
                              var27 = null;
                              var13 = null;
                           }

                           if (var27 != null) {
                              var2 = var13.indexOf(var14);
                              if (var2 != -1) {
                                 var9 = new SpannableStringBuilder(var27);
                                 ((SpannableStringBuilder)var9).setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), var2, var14.length() + var2, 33);
                                 break label193;
                              }
                           }

                           if (var9 != null) {
                              var27 = var14;
                              if (var14.startsWith("@")) {
                                 var27 = var14.substring(1);
                              }

                              SpannableStringBuilder var33;
                              label168: {
                                 Exception var10000;
                                 label229: {
                                    boolean var10001;
                                    try {
                                       var33 = new SpannableStringBuilder();
                                       var33.append("@");
                                       var33.append((CharSequence)var9);
                                       var2 = ((String)var9).toLowerCase().indexOf(var27);
                                    } catch (Exception var17) {
                                       var10000 = var17;
                                       var10001 = false;
                                       break label229;
                                    }

                                    if (var2 == -1) {
                                       break label168;
                                    }

                                    try {
                                       var23 = var27.length();
                                    } catch (Exception var16) {
                                       var10000 = var16;
                                       var10001 = false;
                                       break label229;
                                    }

                                    if (var2 == 0) {
                                       ++var23;
                                    } else {
                                       ++var2;
                                    }

                                    try {
                                       ForegroundColorSpan var30 = new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4"));
                                       var33.setSpan(var30, var2, var23 + var2, 33);
                                       break label168;
                                    } catch (Exception var15) {
                                       var10000 = var15;
                                       var10001 = false;
                                    }
                                 }

                                 Exception var29 = var10000;
                                 FileLog.e((Throwable)var29);
                                 break label238;
                              }

                              var9 = var33;
                              break label238;
                           }
                        }

                        var9 = null;
                        break label193;
                     }
                  }

                  var31 = null;
                  var34 = var9;
                  break label194;
               }

               var34 = null;
               var31 = var9;
            }

            var6 = false;
            var9 = var34;
         }

         Object var32;
         if (var21 != null && ((TLRPC.User)var21).id == this.selfUserId) {
            var32 = LocaleController.getString("SavedMessages", 2131560633);
            var9 = null;
            var5 = true;
         } else {
            var5 = false;
            var32 = var31;
         }

         label146: {
            if (var25 != null && var25.participants_count != 0) {
               if (ChatObject.isChannel(var25) && !var25.megagroup) {
                  var13 = LocaleController.formatPluralString("Subscribers", var25.participants_count);
               } else {
                  var13 = LocaleController.formatPluralString("Members", var25.participants_count);
               }

               if (!(var9 instanceof SpannableStringBuilder)) {
                  var31 = var13;
                  if (!TextUtils.isEmpty((CharSequence)var9)) {
                     var31 = TextUtils.concat(new CharSequence[]{(CharSequence)var9, ", ", var13});
                  }
                  break label146;
               }

               ((SpannableStringBuilder)var9).append(", ").append(var13);
            }

            var31 = var9;
         }

         if (var21 == null) {
            var21 = var25;
         }

         var8.setData((TLObject)var21, var10, (CharSequence)var32, (CharSequence)var31, var6, var5);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var4 = null;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               if (var2 != 3) {
                  if (var2 != 4) {
                     if (var2 == 5) {
                        var4 = new RecyclerListView(this.mContext) {
                           public boolean onInterceptTouchEvent(MotionEvent var1) {
                              if (this.getParent() != null && this.getParent().getParent() != null) {
                                 this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                              }

                              return super.onInterceptTouchEvent(var1);
                           }
                        };
                        ((ViewGroup)var4).setTag(9);
                        ((RecyclerView)var4).setItemAnimator((RecyclerView.ItemAnimator)null);
                        ((ViewGroup)var4).setLayoutAnimation((LayoutAnimationController)null);
                        LinearLayoutManager var3 = new LinearLayoutManager(this.mContext) {
                           public boolean supportsPredictiveItemAnimations() {
                              return false;
                           }
                        };
                        var3.setOrientation(0);
                        ((RecyclerView)var4).setLayoutManager(var3);
                        ((RecyclerListView)var4).setAdapter(new DialogsSearchAdapter.CategoryAdapterRecycler());
                        ((RecyclerListView)var4).setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$DialogsSearchAdapter$DZkEHCwRy7JqjbUQmUNPYIVHu_I(this)));
                        ((RecyclerListView)var4).setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$DialogsSearchAdapter$VmJg1wMYhOLJS8dwKIzHrQMjS0A(this)));
                        this.innerListView = (RecyclerListView)var4;
                     }
                  } else {
                     var4 = new HashtagSearchCell(this.mContext);
                  }
               } else {
                  var4 = new LoadingCell(this.mContext);
               }
            } else {
               var4 = new DialogCell(this.mContext, false, true);
            }
         } else {
            var4 = new GraySectionCell(this.mContext);
         }
      } else {
         var4 = new ProfileSearchCell(this.mContext);
      }

      if (var2 == 5) {
         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(86.0F)));
      } else {
         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
      }

      return new RecyclerListView.Holder((View)var4);
   }

   public void putRecentSearch(long var1, TLObject var3) {
      DialogsSearchAdapter.RecentSearchObject var4 = (DialogsSearchAdapter.RecentSearchObject)this.recentSearchObjectsById.get(var1);
      if (var4 == null) {
         var4 = new DialogsSearchAdapter.RecentSearchObject();
         this.recentSearchObjectsById.put(var1, var4);
      } else {
         this.recentSearchObjects.remove(var4);
      }

      this.recentSearchObjects.add(0, var4);
      var4.did = var1;
      var4.object = var3;
      var4.date = (int)(System.currentTimeMillis() / 1000L);
      this.notifyDataSetChanged();
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$EHNVdrp_nz_CUR77EsM44jqsFBg(this, var1));
   }

   public void searchDialogs(String var1) {
      if (var1 == null || !var1.equals(this.lastSearchText)) {
         this.lastSearchText = var1;
         if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
         }

         Runnable var2 = this.searchRunnable2;
         if (var2 != null) {
            AndroidUtilities.cancelRunOnUIThread(var2);
            this.searchRunnable2 = null;
         }

         if (var1 != null) {
            var1 = var1.trim();
         } else {
            var1 = null;
         }

         if (TextUtils.isEmpty(var1)) {
            this.searchAdapterHelper.unloadRecentHashtags();
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchResultHashtags.clear();
            this.searchAdapterHelper.mergeResults((ArrayList)null);
            if (this.needMessagesSearch != 2) {
               this.searchAdapterHelper.queryServerSearch((String)null, true, true, true, true, 0, 0);
            }

            this.searchWas = false;
            this.lastSearchId = -1;
            this.searchMessagesInternal((String)null);
            this.notifyDataSetChanged();
         } else {
            int var3;
            if (this.needMessagesSearch != 2 && var1.startsWith("#") && var1.length() == 1) {
               this.messagesSearchEndReached = true;
               if (this.searchAdapterHelper.loadRecentHashtags()) {
                  this.searchResultMessages.clear();
                  this.searchResultHashtags.clear();
                  ArrayList var5 = this.searchAdapterHelper.getHashtags();

                  for(var3 = 0; var3 < var5.size(); ++var3) {
                     this.searchResultHashtags.add(((SearchAdapterHelper.HashtagObject)var5.get(var3)).hashtag);
                  }

                  DialogsSearchAdapter.DialogsSearchAdapterDelegate var6 = this.delegate;
                  if (var6 != null) {
                     var6.searchStateChanged(false);
                  }
               }

               this.notifyDataSetChanged();
            } else {
               this.searchResultHashtags.clear();
               this.notifyDataSetChanged();
            }

            var3 = this.lastSearchId + 1;
            this.lastSearchId = var3;
            DispatchQueue var7 = Utilities.searchQueue;
            _$$Lambda$DialogsSearchAdapter$2OrdD5nR_nF_l2wgPqqTnpM3YF8 var4 = new _$$Lambda$DialogsSearchAdapter$2OrdD5nR_nF_l2wgPqqTnpM3YF8(this, var1, var3);
            this.searchRunnable = var4;
            var7.postRunnable(var4, 300L);
         }

      }
   }

   public void setDelegate(DialogsSearchAdapter.DialogsSearchAdapterDelegate var1) {
      this.delegate = var1;
   }

   private class CategoryAdapterRecycler extends RecyclerListView.SelectionAdapter {
      private CategoryAdapterRecycler() {
      }

      // $FF: synthetic method
      CategoryAdapterRecycler(Object var2) {
         this();
      }

      public int getItemCount() {
         return DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.size();
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         HintDialogCell var3 = (HintDialogCell)var1.itemView;
         TLRPC.TL_topPeer var6 = (TLRPC.TL_topPeer)DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.get(var2);
         new TLRPC.TL_dialog();
         TLRPC.Peer var4 = var6.peer;
         var2 = var4.user_id;
         TLRPC.User var5 = null;
         TLRPC.Chat var7;
         if (var2 != 0) {
            var5 = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getUser(var6.peer.user_id);
            var7 = null;
         } else {
            var2 = var4.channel_id;
            if (var2 != 0) {
               var2 = -var2;
               var7 = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(var6.peer.channel_id);
            } else {
               var2 = var4.chat_id;
               if (var2 != 0) {
                  var2 = -var2;
                  var7 = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(var6.peer.chat_id);
               } else {
                  var2 = 0;
                  var7 = null;
               }
            }
         }

         var3.setTag(var2);
         String var8;
         if (var5 != null) {
            var8 = UserObject.getFirstName(var5);
         } else if (var7 != null) {
            var8 = var7.title;
         } else {
            var8 = "";
         }

         var3.setDialog(var2, true, var8);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         HintDialogCell var3 = new HintDialogCell(DialogsSearchAdapter.this.mContext);
         var3.setLayoutParams(new RecyclerView.LayoutParams(AndroidUtilities.dp(80.0F), AndroidUtilities.dp(86.0F)));
         return new RecyclerListView.Holder(var3);
      }

      public void setIndex(int var1) {
         this.notifyDataSetChanged();
      }
   }

   private class DialogSearchResult {
      public int date;
      public CharSequence name;
      public TLObject object;

      private DialogSearchResult() {
      }

      // $FF: synthetic method
      DialogSearchResult(Object var2) {
         this();
      }
   }

   public interface DialogsSearchAdapterDelegate {
      void didPressedOnSubDialog(long var1);

      void needClearList();

      void needRemoveHint(int var1);

      void searchStateChanged(boolean var1);
   }

   protected static class RecentSearchObject {
      int date;
      long did;
      TLObject object;
   }
}
