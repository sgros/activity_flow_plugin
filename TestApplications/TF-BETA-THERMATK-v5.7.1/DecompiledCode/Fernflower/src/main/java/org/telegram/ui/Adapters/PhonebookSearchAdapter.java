package org.telegram.ui.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookSearchAdapter extends RecyclerListView.SelectionAdapter {
   private Context mContext;
   private ArrayList searchResult = new ArrayList();
   private ArrayList searchResultNames = new ArrayList();
   private Timer searchTimer;

   public PhonebookSearchAdapter(Context var1) {
      this.mContext = var1;
   }

   private void processSearch(String var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PhonebookSearchAdapter$KOELxq6F75LZaR2Hw7LDqS_qxfw(this, var1));
   }

   private void updateSearchResults(String var1, ArrayList var2, ArrayList var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PhonebookSearchAdapter$5E7mSLYtYVP_MgRNf26lnHW5RXo(this, var1, var2, var3));
   }

   public Object getItem(int var1) {
      return this.searchResult.get(var1);
   }

   public int getItemCount() {
      return this.searchResult.size();
   }

   public int getItemViewType(int var1) {
      return 0;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      return true;
   }

   // $FF: synthetic method
   public void lambda$null$0$PhonebookSearchAdapter(String var1, ArrayList var2, ArrayList var3, int var4) {
      String var5 = var1.trim().toLowerCase();
      if (var5.length() == 0) {
         this.updateSearchResults(var1, new ArrayList(), new ArrayList());
      } else {
         String var6;
         String var7;
         label169: {
            var6 = LocaleController.getInstance().getTranslitString(var5);
            if (!var5.equals(var6)) {
               var7 = var6;
               if (var6.length() != 0) {
                  break label169;
               }
            }

            var7 = null;
         }

         byte var8;
         if (var7 != null) {
            var8 = 1;
         } else {
            var8 = 0;
         }

         String[] var9 = new String[var8 + 1];
         var9[0] = var5;
         if (var7 != null) {
            var9[1] = var7;
         }

         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();
         SparseBooleanArray var12 = new SparseBooleanArray();

         int var13;
         String var16;
         int var17;
         for(var13 = 0; var13 < var2.size(); ++var13) {
            ContactsController.Contact var14 = (ContactsController.Contact)var2.get(var13);
            String var15 = ContactsController.formatName(var14.first_name, var14.last_name).toLowerCase();
            var16 = LocaleController.getInstance().getTranslitString(var15);
            TLRPC.User var27 = var14.user;
            if (var27 != null) {
               var6 = ContactsController.formatName(var27.first_name, var27.last_name).toLowerCase();
               var7 = LocaleController.getInstance().getTranslitString(var15);
            } else {
               var6 = null;
               var7 = null;
            }

            var5 = var16;
            if (var15.equals(var16)) {
               var5 = null;
            }

            var17 = var9.length;
            int var18 = 0;

            for(var8 = 0; var18 < var17; ++var18) {
               label178: {
                  StringBuilder var19;
                  label157: {
                     label179: {
                        var16 = var9[var18];
                        if (var6 != null) {
                           if (var6.startsWith(var16)) {
                              break label179;
                           }

                           var19 = new StringBuilder();
                           var19.append(" ");
                           var19.append(var16);
                           if (var6.contains(var19.toString())) {
                              break label179;
                           }
                        }

                        if (var7 == null) {
                           break label157;
                        }

                        if (!var7.startsWith(var16)) {
                           var19 = new StringBuilder();
                           var19.append(" ");
                           var19.append(var16);
                           if (!var7.contains(var19.toString())) {
                              break label157;
                           }
                        }
                     }

                     var8 = 1;
                     break label178;
                  }

                  TLRPC.User var32 = var14.user;
                  if (var32 != null) {
                     String var33 = var32.username;
                     if (var33 != null && var33.startsWith(var16)) {
                        var8 = 2;
                        break label178;
                     }
                  }

                  if (!var15.startsWith(var16)) {
                     var19 = new StringBuilder();
                     var19.append(" ");
                     var19.append(var16);
                     if (!var15.contains(var19.toString())) {
                        if (var5 == null) {
                           break label178;
                        }

                        if (!var5.startsWith(var16)) {
                           var19 = new StringBuilder();
                           var19.append(" ");
                           var19.append(var16);
                           if (!var5.contains(var19.toString())) {
                              break label178;
                           }
                        }
                     }
                  }

                  var8 = 3;
               }

               if (var8 != 0) {
                  if (var8 == 3) {
                     var11.add(AndroidUtilities.generateSearchName(var14.first_name, var14.last_name, var16));
                  } else if (var8 == 1) {
                     var27 = var14.user;
                     var11.add(AndroidUtilities.generateSearchName(var27.first_name, var27.last_name, var16));
                  } else {
                     StringBuilder var28 = new StringBuilder();
                     var28.append("@");
                     var28.append(var14.user.username);
                     var7 = var28.toString();
                     StringBuilder var25 = new StringBuilder();
                     var25.append("@");
                     var25.append(var16);
                     var11.add(AndroidUtilities.generateSearchName(var7, (String)null, var25.toString()));
                  }

                  var27 = var14.user;
                  if (var27 != null) {
                     var12.put(var27.id, true);
                  }

                  var10.add(var14);
                  break;
               }
            }
         }

         for(var13 = 0; var13 < var3.size(); ++var13) {
            TLRPC.TL_contact var21 = (TLRPC.TL_contact)var3.get(var13);
            if (var12.indexOfKey(var21.user_id) < 0) {
               TLRPC.User var26 = MessagesController.getInstance(var4).getUser(var21.user_id);
               var5 = ContactsController.formatName(var26.first_name, var26.last_name).toLowerCase();
               var7 = LocaleController.getInstance().getTranslitString(var5);
               String var22 = var7;
               if (var5.equals(var7)) {
                  var22 = null;
               }

               int var20 = var9.length;
               var17 = 0;

               boolean var29;
               for(boolean var31 = false; var17 < var20; var31 = var29) {
                  label186: {
                     var7 = var9[var17];
                     if (!var5.startsWith(var7)) {
                        StringBuilder var30 = new StringBuilder();
                        var30.append(" ");
                        var30.append(var7);
                        if (!var5.contains(var30.toString())) {
                           label184: {
                              if (var22 != null) {
                                 if (var22.startsWith(var7)) {
                                    break label184;
                                 }

                                 var30 = new StringBuilder();
                                 var30.append(" ");
                                 var30.append(var7);
                                 if (var22.contains(var30.toString())) {
                                    break label184;
                                 }
                              }

                              var16 = var26.username;
                              var29 = var31;
                              if (var16 != null) {
                                 var29 = var31;
                                 if (var16.startsWith(var7)) {
                                    var29 = true;
                                 }
                              }
                              break label186;
                           }
                        }
                     }

                     var29 = true;
                  }

                  if (var29) {
                     if (var29) {
                        var11.add(AndroidUtilities.generateSearchName(var26.first_name, var26.last_name, var7));
                     } else {
                        StringBuilder var23 = new StringBuilder();
                        var23.append("@");
                        var23.append(var26.username);
                        var22 = var23.toString();
                        StringBuilder var24 = new StringBuilder();
                        var24.append("@");
                        var24.append(var7);
                        var11.add(AndroidUtilities.generateSearchName(var22, (String)null, var24.toString()));
                     }

                     var10.add(var26);
                     break;
                  }

                  ++var17;
               }
            }
         }

         this.updateSearchResults(var1, var10, var11);
      }
   }

   // $FF: synthetic method
   public void lambda$processSearch$1$PhonebookSearchAdapter(String var1) {
      int var2 = UserConfig.selectedAccount;
      ArrayList var3 = new ArrayList(ContactsController.getInstance(var2).contactsBook.values());
      ArrayList var4 = new ArrayList(ContactsController.getInstance(var2).contacts);
      Utilities.searchQueue.postRunnable(new _$$Lambda$PhonebookSearchAdapter$tWtQ3i_wKL8GTUJsG8Cqe7b7WRE(this, var1, var3, var4, var2));
   }

   // $FF: synthetic method
   public void lambda$updateSearchResults$2$PhonebookSearchAdapter(String var1, ArrayList var2, ArrayList var3) {
      this.onUpdateSearchResults(var1);
      this.searchResult = var2;
      this.searchResultNames = var3;
      this.notifyDataSetChanged();
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      if (var1.getItemViewType() == 0) {
         UserCell var3 = (UserCell)var1.itemView;
         Object var7 = this.getItem(var2);
         TLRPC.User var8;
         if (var7 instanceof ContactsController.Contact) {
            ContactsController.Contact var4 = (ContactsController.Contact)var7;
            var8 = var4.user;
            if (var8 == null) {
               var3.setCurrentId(var4.contact_id);
               CharSequence var5 = (CharSequence)this.searchResultNames.get(var2);
               String var9;
               if (var4.phones.isEmpty()) {
                  var9 = "";
               } else {
                  var9 = PhoneFormat.getInstance().format((String)var4.phones.get(0));
               }

               var3.setData((TLObject)null, var5, var9, 0);
               var8 = null;
            }
         } else {
            var8 = (TLRPC.User)var7;
         }

         if (var8 != null) {
            CharSequence var10 = (CharSequence)this.searchResultNames.get(var2);
            PhoneFormat var11 = PhoneFormat.getInstance();
            StringBuilder var6 = new StringBuilder();
            var6.append("+");
            var6.append(var8.phone);
            var3.setData(var8, var10, var11.format(var6.toString()), 0);
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      UserCell var3 = new UserCell(this.mContext, 8, 0, false);
      var3.setNameTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      return new RecyclerListView.Holder(var3);
   }

   protected void onUpdateSearchResults(String var1) {
   }

   public void search(final String var1) {
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
         this.notifyDataSetChanged();
      } else {
         this.searchTimer = new Timer();
         this.searchTimer.schedule(new TimerTask() {
            public void run() {
               try {
                  PhonebookSearchAdapter.this.searchTimer.cancel();
                  PhonebookSearchAdapter.this.searchTimer = null;
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

               PhonebookSearchAdapter.this.processSearch(var1);
            }
         }, 200L, 300L);
      }

   }
}
