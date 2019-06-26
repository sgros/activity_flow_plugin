package org.telegram.ui.Adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.RecyclerListView;

public class SearchAdapter extends RecyclerListView.SelectionAdapter {
   private boolean allowBots;
   private boolean allowChats;
   private boolean allowUsernameSearch;
   private int channelId;
   private SparseArray checkedMap;
   private SparseArray ignoreUsers;
   private Context mContext;
   private boolean onlyMutual;
   private SearchAdapterHelper searchAdapterHelper;
   private ArrayList searchResult = new ArrayList();
   private ArrayList searchResultNames = new ArrayList();
   private Timer searchTimer;
   private boolean useUserCell;

   public SearchAdapter(Context var1, SparseArray var2, boolean var3, boolean var4, boolean var5, boolean var6, int var7) {
      this.mContext = var1;
      this.ignoreUsers = var2;
      this.onlyMutual = var4;
      this.allowUsernameSearch = var3;
      this.allowChats = var5;
      this.allowBots = var6;
      this.channelId = var7;
      this.searchAdapterHelper = new SearchAdapterHelper(true);
      this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() {
         public SparseArray getExcludeUsers() {
            return SearchAdapter.this.ignoreUsers;
         }

         public void onDataSetChanged() {
            SearchAdapter.this.notifyDataSetChanged();
         }

         public void onSetHashtags(ArrayList var1, HashMap var2) {
         }
      });
   }

   private void processSearch(String var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapter$orWB0TdKMjyMiCeh3w5NvcjUVZU(this, var1));
   }

   private void updateSearchResults(ArrayList var1, ArrayList var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapter$6uwOh4k7ujlooXYRN4wF38fqlek(this, var1, var2));
   }

   public TLObject getItem(int var1) {
      int var2 = this.searchResult.size();
      int var3 = this.searchAdapterHelper.getGlobalSearch().size();
      if (var1 >= 0 && var1 < var2) {
         return (TLObject)this.searchResult.get(var1);
      } else {
         return var1 > var2 && var1 <= var3 + var2 ? (TLObject)this.searchAdapterHelper.getGlobalSearch().get(var1 - var2 - 1) : null;
      }
   }

   public int getItemCount() {
      int var1 = this.searchResult.size();
      int var2 = this.searchAdapterHelper.getGlobalSearch().size();
      int var3 = var1;
      if (var2 != 0) {
         var3 = var1 + var2 + 1;
      }

      return var3;
   }

   public int getItemViewType(int var1) {
      return var1 == this.searchResult.size() ? 1 : 0;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      boolean var2;
      if (var1.getItemViewType() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isGlobalSearch(int var1) {
      int var2 = this.searchResult.size();
      int var3 = this.searchAdapterHelper.getGlobalSearch().size();
      if (var1 >= 0 && var1 < var2) {
         return false;
      } else {
         return var1 > var2 && var1 <= var3 + var2;
      }
   }

   // $FF: synthetic method
   public void lambda$null$0$SearchAdapter(String var1, ArrayList var2, int var3) {
      String var4 = var1.trim().toLowerCase();
      if (var4.length() == 0) {
         this.updateSearchResults(new ArrayList(), new ArrayList());
      } else {
         String var5;
         label92: {
            var5 = LocaleController.getInstance().getTranslitString(var4);
            if (!var4.equals(var5)) {
               var1 = var5;
               if (var5.length() != 0) {
                  break label92;
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

         String[] var7 = new String[var6 + 1];
         var7[0] = var4;
         if (var1 != null) {
            var7[1] = var1;
         }

         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();

         for(int var10 = 0; var10 < var2.size(); ++var10) {
            TLRPC.TL_contact var16 = (TLRPC.TL_contact)var2.get(var10);
            TLRPC.User var18 = MessagesController.getInstance(var3).getUser(var16.user_id);
            if (var18.id != UserConfig.getInstance(var3).getClientUserId() && (!this.onlyMutual || var18.mutual_contact)) {
               SparseArray var19 = this.ignoreUsers;
               if (var19 == null || var19.indexOfKey(var16.user_id) < 0) {
                  String var11 = ContactsController.formatName(var18.first_name, var18.last_name).toLowerCase();
                  var5 = LocaleController.getInstance().getTranslitString(var11);
                  var1 = var5;
                  if (var11.equals(var5)) {
                     var1 = null;
                  }

                  int var12 = var7.length;
                  int var13 = 0;

                  boolean var20;
                  for(boolean var14 = false; var13 < var12; var14 = var20) {
                     label104: {
                        var5 = var7[var13];
                        if (!var11.startsWith(var5)) {
                           StringBuilder var15 = new StringBuilder();
                           var15.append(" ");
                           var15.append(var5);
                           if (!var11.contains(var15.toString())) {
                              label102: {
                                 if (var1 != null) {
                                    if (var1.startsWith(var5)) {
                                       break label102;
                                    }

                                    var15 = new StringBuilder();
                                    var15.append(" ");
                                    var15.append(var5);
                                    if (var1.contains(var15.toString())) {
                                       break label102;
                                    }
                                 }

                                 String var22 = var18.username;
                                 var20 = var14;
                                 if (var22 != null) {
                                    var20 = var14;
                                    if (var22.startsWith(var5)) {
                                       var20 = true;
                                    }
                                 }
                                 break label104;
                              }
                           }
                        }

                        var20 = true;
                     }

                     if (var20) {
                        if (var20) {
                           var9.add(AndroidUtilities.generateSearchName(var18.first_name, var18.last_name, var5));
                        } else {
                           StringBuilder var17 = new StringBuilder();
                           var17.append("@");
                           var17.append(var18.username);
                           var1 = var17.toString();
                           StringBuilder var21 = new StringBuilder();
                           var21.append("@");
                           var21.append(var5);
                           var9.add(AndroidUtilities.generateSearchName(var1, (String)null, var21.toString()));
                        }

                        var8.add(var18);
                        break;
                     }

                     ++var13;
                  }
               }
            }
         }

         this.updateSearchResults(var8, var9);
      }
   }

   // $FF: synthetic method
   public void lambda$processSearch$1$SearchAdapter(String var1) {
      if (this.allowUsernameSearch) {
         this.searchAdapterHelper.queryServerSearch(var1, true, this.allowChats, this.allowBots, true, this.channelId, -1);
      }

      int var2 = UserConfig.selectedAccount;
      ArrayList var3 = new ArrayList(ContactsController.getInstance(var2).contacts);
      Utilities.searchQueue.postRunnable(new _$$Lambda$SearchAdapter$MJ9cur0I3ZiqQGm3sZTS0MY0LdM(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$updateSearchResults$2$SearchAdapter(ArrayList var1, ArrayList var2) {
      this.searchResult = var1;
      this.searchResultNames = var2;
      this.searchAdapterHelper.mergeResults(var1);
      this.notifyDataSetChanged();
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      if (var1.getItemViewType() == 0) {
         TLObject var3 = this.getItem(var2);
         if (var3 != null) {
            boolean var4 = var3 instanceof TLRPC.User;
            boolean var5 = false;
            String var7;
            int var8;
            if (var4) {
               TLRPC.User var6 = (TLRPC.User)var3;
               var7 = var6.username;
               var8 = var6.id;
            } else if (var3 instanceof TLRPC.Chat) {
               TLRPC.Chat var18 = (TLRPC.Chat)var3;
               var7 = var18.username;
               var8 = var18.id;
            } else {
               var7 = null;
               var8 = 0;
            }

            int var9 = this.searchResult.size();
            var4 = true;
            String var10;
            Object var19;
            Object var22;
            if (var2 < var9) {
               label65: {
                  var19 = (CharSequence)this.searchResultNames.get(var2);
                  if (var19 != null && var7 != null && var7.length() > 0) {
                     var10 = ((CharSequence)var19).toString();
                     StringBuilder var11 = new StringBuilder();
                     var11.append("@");
                     var11.append(var7);
                     if (var10.startsWith(var11.toString())) {
                        var22 = null;
                        break label65;
                     }
                  }

                  var10 = null;
                  var22 = var19;
                  var19 = var10;
               }
            } else if (var2 > this.searchResult.size() && var7 != null) {
               label94: {
                  var10 = this.searchAdapterHelper.getLastFoundUsername();
                  String var20 = var10;
                  if (var10.startsWith("@")) {
                     var20 = var10.substring(1);
                  }

                  SpannableStringBuilder var24;
                  label79: {
                     Exception var10000;
                     label95: {
                        int var12;
                        boolean var10001;
                        try {
                           var24 = new SpannableStringBuilder();
                           var24.append("@");
                           var24.append(var7);
                           var12 = var7.toLowerCase().indexOf(var20);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label95;
                        }

                        if (var12 == -1) {
                           break label79;
                        }

                        try {
                           var9 = var20.length();
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label95;
                        }

                        if (var12 == 0) {
                           ++var9;
                        } else {
                           ++var12;
                        }

                        try {
                           ForegroundColorSpan var23 = new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4"));
                           var24.setSpan(var23, var12, var9 + var12, 33);
                           break label79;
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                        }
                     }

                     Exception var21 = var10000;
                     FileLog.e((Throwable)var21);
                     var10 = null;
                     var19 = var7;
                     var22 = var10;
                     break label94;
                  }

                  var22 = null;
                  var19 = var24;
               }
            } else {
               var22 = null;
               var19 = var22;
            }

            if (this.useUserCell) {
               UserCell var16 = (UserCell)var1.itemView;
               var16.setData(var3, (CharSequence)var22, (CharSequence)var19, 0);
               SparseArray var25 = this.checkedMap;
               if (var25 != null) {
                  if (var25.indexOfKey(var8) < 0) {
                     var4 = false;
                  }

                  var16.setChecked(var4, false);
               }
            } else {
               ProfileSearchCell var17 = (ProfileSearchCell)var1.itemView;
               var17.setData(var3, (TLRPC.EncryptedChat)null, (CharSequence)var22, (CharSequence)var19, false, false);
               var4 = var5;
               if (var2 != this.getItemCount() - 1) {
                  var4 = var5;
                  if (var2 != this.searchResult.size() - 1) {
                     var4 = true;
                  }
               }

               var17.useSeparator = var4;
            }
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var4;
      if (var2 != 0) {
         var4 = new GraySectionCell(this.mContext);
         ((GraySectionCell)var4).setText(LocaleController.getString("GlobalSearch", 2131559594));
      } else if (this.useUserCell) {
         UserCell var3 = new UserCell(this.mContext, 1, 1, false);
         var4 = var3;
         if (this.checkedMap != null) {
            var3.setChecked(false, false);
            var4 = var3;
         }
      } else {
         var4 = new ProfileSearchCell(this.mContext);
      }

      return new RecyclerListView.Holder((View)var4);
   }

   public void searchDialogs(final String var1) {
      try {
         if (this.searchTimer != null) {
            this.searchTimer.cancel();
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      if (var1 == null) {
         this.searchResult.clear();
         this.searchResultNames.clear();
         if (this.allowUsernameSearch) {
            this.searchAdapterHelper.queryServerSearch((String)null, true, this.allowChats, this.allowBots, true, this.channelId, 0);
         }

         this.notifyDataSetChanged();
      } else {
         this.searchTimer = new Timer();
         this.searchTimer.schedule(new TimerTask() {
            public void run() {
               try {
                  SearchAdapter.this.searchTimer.cancel();
                  SearchAdapter.this.searchTimer = null;
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

               SearchAdapter.this.processSearch(var1);
            }
         }, 200L, 300L);
      }

   }

   public void setCheckedMap(SparseArray var1) {
      this.checkedMap = var1;
   }

   public void setUseUserCell(boolean var1) {
      this.useUserCell = var1;
   }
}
