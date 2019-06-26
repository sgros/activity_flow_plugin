package org.telegram.ui.Adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.RecyclerListView;

public class ContactsAdapter extends RecyclerListView.SectionsAdapter {
   private SparseArray checkedMap;
   private int currentAccount;
   private SparseArray ignoreUsers;
   private boolean isAdmin;
   private boolean isChannel;
   private Context mContext;
   private boolean needPhonebook;
   private ArrayList onlineContacts;
   private int onlyUsers;
   private boolean scrolling;
   private int sortType;

   public ContactsAdapter(Context var1, int var2, boolean var3, SparseArray var4, int var5) {
      this.currentAccount = UserConfig.selectedAccount;
      this.mContext = var1;
      this.onlyUsers = var2;
      this.needPhonebook = var3;
      this.ignoreUsers = var4;
      boolean var6 = true;
      if (var5 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.isAdmin = var3;
      if (var5 == 2) {
         var3 = var6;
      } else {
         var3 = false;
      }

      this.isChannel = var3;
   }

   // $FF: synthetic method
   static int lambda$sortOnlineContacts$0(MessagesController var0, int var1, TLRPC.TL_contact var2, TLRPC.TL_contact var3) {
      int var4;
      TLRPC.User var5;
      label87: {
         TLRPC.User var8 = var0.getUser(var3.user_id);
         var5 = var0.getUser(var2.user_id);
         if (var8 != null) {
            if (var8.self) {
               var4 = var1 + '썐';
               break label87;
            }

            TLRPC.UserStatus var7 = var8.status;
            if (var7 != null) {
               var4 = var7.expires;
               break label87;
            }
         }

         var4 = 0;
      }

      label80: {
         if (var5 != null) {
            if (var5.self) {
               var1 += 50000;
               break label80;
            }

            TLRPC.UserStatus var6 = var5.status;
            if (var6 != null) {
               var1 = var6.expires;
               break label80;
            }
         }

         var1 = 0;
      }

      if (var4 > 0 && var1 > 0) {
         if (var4 > var1) {
            return 1;
         } else {
            return var4 < var1 ? -1 : 0;
         }
      } else if (var4 < 0 && var1 < 0) {
         if (var4 > var1) {
            return 1;
         } else {
            return var4 < var1 ? -1 : 0;
         }
      } else if (var4 < 0 && var1 > 0 || var4 == 0 && var1 != 0) {
         return -1;
      } else {
         return (var1 >= 0 || var4 <= 0) && (var1 != 0 || var4 == 0) ? 0 : 1;
      }
   }

   public int getCountForSection(int var1) {
      HashMap var2;
      if (this.onlyUsers == 2) {
         var2 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
      } else {
         var2 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
      }

      ArrayList var3;
      if (this.onlyUsers == 2) {
         var3 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
      } else {
         var3 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
      }

      int var4 = this.onlyUsers;
      byte var5 = 0;
      int var6;
      if (var4 != 0 && !this.isAdmin) {
         if (var1 < var3.size()) {
            var6 = ((ArrayList)var2.get(var3.get(var1))).size();
            if (var1 == var3.size() - 1) {
               var1 = var6;
               if (!this.needPhonebook) {
                  return var1;
               }
            }

            var1 = var6 + 1;
            return var1;
         }
      } else {
         if (var1 == 0) {
            if (!this.needPhonebook && !this.isAdmin) {
               return 4;
            }

            return 2;
         }

         if (this.sortType == 2) {
            if (var1 == 1) {
               if (this.onlineContacts.isEmpty()) {
                  var1 = var5;
               } else {
                  var1 = this.onlineContacts.size() + 1;
               }

               return var1;
            }
         } else {
            --var1;
            if (var1 < var3.size()) {
               var6 = ((ArrayList)var2.get(var3.get(var1))).size();
               if (var1 == var3.size() - 1) {
                  var1 = var6;
                  if (!this.needPhonebook) {
                     return var1;
                  }
               }

               var1 = var6 + 1;
               return var1;
            }
         }
      }

      return this.needPhonebook ? ContactsController.getInstance(this.currentAccount).phoneBookContacts.size() : 0;
   }

   public Object getItem(int var1, int var2) {
      HashMap var3;
      if (this.onlyUsers == 2) {
         var3 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
      } else {
         var3 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
      }

      ArrayList var4;
      if (this.onlyUsers == 2) {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
      } else {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
      }

      ArrayList var5;
      if (this.onlyUsers != 0 && !this.isAdmin) {
         if (var1 < var4.size()) {
            var5 = (ArrayList)var3.get(var4.get(var1));
            if (var2 < var5.size()) {
               return MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)var5.get(var2)).user_id);
            }
         }

         return null;
      } else if (var1 == 0) {
         return null;
      } else {
         if (this.sortType == 2) {
            if (var1 == 1) {
               if (var2 < this.onlineContacts.size()) {
                  return MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)this.onlineContacts.get(var2)).user_id);
               }

               return null;
            }
         } else {
            --var1;
            if (var1 < var4.size()) {
               var5 = (ArrayList)var3.get(var4.get(var1));
               if (var2 < var5.size()) {
                  return MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)var5.get(var2)).user_id);
               }

               return null;
            }
         }

         return this.needPhonebook ? ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(var2) : null;
      }
   }

   public int getItemViewType(int var1, int var2) {
      HashMap var3;
      if (this.onlyUsers == 2) {
         var3 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
      } else {
         var3 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
      }

      ArrayList var4;
      if (this.onlyUsers == 2) {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
      } else {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
      }

      int var5 = this.onlyUsers;
      byte var6 = 0;
      byte var7 = 0;
      byte var8 = 0;
      byte var9;
      if (var5 != 0 && !this.isAdmin) {
         if (var2 < ((ArrayList)var3.get(var4.get(var1))).size()) {
            var9 = var8;
         } else {
            var9 = 3;
         }

         return var9;
      } else {
         if (var1 != 0) {
            if (this.sortType == 2) {
               if (var1 == 1) {
                  if (var2 < this.onlineContacts.size()) {
                     var9 = var6;
                  } else {
                     var9 = 3;
                  }

                  return var9;
               }
            } else {
               --var1;
               if (var1 < var4.size()) {
                  if (var2 < ((ArrayList)var3.get(var4.get(var1))).size()) {
                     var9 = var7;
                  } else {
                     var9 = 3;
                  }

                  return var9;
               }
            }
         } else if ((this.needPhonebook || this.isAdmin) && var2 == 1 || var2 == 3) {
            return 2;
         }

         return 1;
      }
   }

   public String getLetter(int var1) {
      if (this.sortType == 2) {
         return null;
      } else {
         ArrayList var2;
         if (this.onlyUsers == 2) {
            var2 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
         } else {
            var2 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
         }

         int var3 = this.getSectionForPosition(var1);
         var1 = var3;
         if (var3 == -1) {
            var1 = var2.size() - 1;
         }

         return var1 > 0 && var1 <= var2.size() ? (String)var2.get(var1 - 1) : null;
      }
   }

   public int getPositionForScrollProgress(float var1) {
      return (int)((float)this.getItemCount() * var1);
   }

   public int getSectionCount() {
      int var1;
      if (this.sortType == 2) {
         var1 = 1;
      } else {
         ArrayList var2;
         if (this.onlyUsers == 2) {
            var2 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
         } else {
            var2 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
         }

         var1 = var2.size();
      }

      int var3 = var1;
      if (this.onlyUsers == 0) {
         var3 = var1 + 1;
      }

      var1 = var3;
      if (this.isAdmin) {
         var1 = var3 + 1;
      }

      boolean var4 = this.needPhonebook;
      return var1;
   }

   public View getSectionHeaderView(int var1, View var2) {
      HashMap var3;
      if (this.onlyUsers == 2) {
         var3 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
      } else {
         var3 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
      }

      ArrayList var6;
      if (this.onlyUsers == 2) {
         var6 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
      } else {
         var6 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
      }

      Object var4 = var2;
      if (var2 == null) {
         var4 = new LetterSectionCell(this.mContext);
      }

      LetterSectionCell var5 = (LetterSectionCell)var4;
      if (this.sortType == 2) {
         var5.setLetter("");
      } else if (this.onlyUsers != 0 && !this.isAdmin) {
         if (var1 < var6.size()) {
            var5.setLetter((String)var6.get(var1));
         } else {
            var5.setLetter("");
         }
      } else if (var1 == 0) {
         var5.setLetter("");
      } else {
         --var1;
         if (var1 < var6.size()) {
            var5.setLetter((String)var6.get(var1));
         } else {
            var5.setLetter("");
         }
      }

      return (View)var4;
   }

   public boolean isEnabled(int var1, int var2) {
      HashMap var3;
      if (this.onlyUsers == 2) {
         var3 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
      } else {
         var3 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
      }

      ArrayList var4;
      if (this.onlyUsers == 2) {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
      } else {
         var4 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
      }

      int var5 = this.onlyUsers;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      if (var5 != 0 && !this.isAdmin) {
         if (var2 < ((ArrayList)var3.get(var4.get(var1))).size()) {
            var10 = true;
         }

         return var10;
      } else if (var1 == 0) {
         if (!this.needPhonebook && !this.isAdmin) {
            var10 = var6;
            if (var2 != 3) {
               var10 = true;
            }

            return var10;
         } else {
            var10 = var7;
            if (var2 != 1) {
               var10 = true;
            }

            return var10;
         }
      } else {
         if (this.sortType == 2) {
            if (var1 == 1) {
               var10 = var8;
               if (var2 < this.onlineContacts.size()) {
                  var10 = true;
               }

               return var10;
            }
         } else {
            --var1;
            if (var1 < var4.size()) {
               var10 = var9;
               if (var2 < ((ArrayList)var3.get(var4.get(var1))).size()) {
                  var10 = true;
               }

               return var10;
            }
         }

         return true;
      }
   }

   public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
      int var4 = var3.getItemViewType();
      boolean var5 = false;
      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 == 2) {
               GraySectionCell var8 = (GraySectionCell)var3.itemView;
               var1 = this.sortType;
               if (var1 == 0) {
                  var8.setText(LocaleController.getString("Contacts", 2131559149));
               } else if (var1 == 1) {
                  var8.setText(LocaleController.getString("SortedByName", 2131560799));
               } else {
                  var8.setText(LocaleController.getString("SortedByLastSeen", 2131560798));
               }
            }
         } else {
            TextCell var6 = (TextCell)var3.itemView;
            if (var1 == 0) {
               if (this.needPhonebook) {
                  var6.setTextAndIcon(LocaleController.getString("InviteFriends", 2131559677), 2131165584, false);
               } else if (this.isAdmin) {
                  if (this.isChannel) {
                     var6.setTextAndIcon(LocaleController.getString("ChannelInviteViaLink", 2131558953), 2131165787, false);
                  } else {
                     var6.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", 2131559688), 2131165787, false);
                  }
               } else if (var2 == 0) {
                  var6.setTextAndIcon(LocaleController.getString("NewGroup", 2131559900), 2131165580, false);
               } else if (var2 == 1) {
                  var6.setTextAndIcon(LocaleController.getString("NewSecretChat", 2131559909), 2131165594, false);
               } else if (var2 == 2) {
                  var6.setTextAndIcon(LocaleController.getString("NewChannel", 2131559898), 2131165568, false);
               }
            } else {
               ContactsController.Contact var9 = (ContactsController.Contact)ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(var2);
               if (var9.first_name != null && var9.last_name != null) {
                  StringBuilder var16 = new StringBuilder();
                  var16.append(var9.first_name);
                  var16.append(" ");
                  var16.append(var9.last_name);
                  var6.setText(var16.toString(), false);
               } else {
                  String var7 = var9.first_name;
                  if (var7 != null && var9.last_name == null) {
                     var6.setText(var7, false);
                  } else {
                     var6.setText(var9.last_name, false);
                  }
               }
            }
         }
      } else {
         UserCell var17 = (UserCell)var3.itemView;
         byte var11;
         if (this.sortType == 2) {
            var11 = 6;
         } else {
            var11 = 58;
         }

         var17.setAvatarPadding(var11);
         ArrayList var12;
         if (this.sortType == 2) {
            var12 = this.onlineContacts;
         } else {
            HashMap var10;
            if (this.onlyUsers == 2) {
               var10 = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
            } else {
               var10 = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
            }

            ArrayList var14;
            if (this.onlyUsers == 2) {
               var14 = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
            } else {
               var14 = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
            }

            if (this.onlyUsers != 0 && !this.isAdmin) {
               var11 = 0;
            } else {
               var11 = 1;
            }

            var12 = (ArrayList)var10.get(var14.get(var1 - var11));
         }

         TLRPC.User var13 = MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)var12.get(var2)).user_id);
         var17.setData(var13, (CharSequence)null, (CharSequence)null, 0);
         SparseArray var15 = this.checkedMap;
         if (var15 != null) {
            if (var15.indexOfKey(var13.id) >= 0) {
               var5 = true;
            }

            var17.setChecked(var5, this.scrolling ^ true);
         }

         var15 = this.ignoreUsers;
         if (var15 != null) {
            if (var15.indexOfKey(var13.id) >= 0) {
               var17.setAlpha(0.5F);
            } else {
               var17.setAlpha(1.0F);
            }
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var7;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               var7 = new DividerCell(this.mContext);
               boolean var3 = LocaleController.isRTL;
               float var4 = 28.0F;
               float var5;
               if (var3) {
                  var5 = 28.0F;
               } else {
                  var5 = 72.0F;
               }

               int var6 = AndroidUtilities.dp(var5);
               var2 = AndroidUtilities.dp(8.0F);
               var5 = var4;
               if (LocaleController.isRTL) {
                  var5 = 72.0F;
               }

               ((View)var7).setPadding(var6, var2, AndroidUtilities.dp(var5), AndroidUtilities.dp(8.0F));
            } else {
               var7 = new GraySectionCell(this.mContext);
            }
         } else {
            var7 = new TextCell(this.mContext);
         }
      } else {
         var7 = new UserCell(this.mContext, 58, 1, false);
      }

      return new RecyclerListView.Holder((View)var7);
   }

   public void setCheckedMap(SparseArray var1) {
      this.checkedMap = var1;
   }

   public void setIsScrolling(boolean var1) {
      this.scrolling = var1;
   }

   public void setSortType(int var1) {
      this.sortType = var1;
      if (this.sortType == 2) {
         if (this.onlineContacts == null) {
            this.onlineContacts = new ArrayList();
            int var2 = UserConfig.getInstance(this.currentAccount).clientUserId;
            this.onlineContacts.addAll(ContactsController.getInstance(this.currentAccount).contacts);
            var1 = 0;

            for(int var3 = this.onlineContacts.size(); var1 < var3; ++var1) {
               if (((TLRPC.TL_contact)this.onlineContacts.get(var1)).user_id == var2) {
                  this.onlineContacts.remove(var1);
                  break;
               }
            }
         }

         this.sortOnlineContacts();
      } else {
         this.notifyDataSetChanged();
      }

   }

   public void sortOnlineContacts() {
      if (this.onlineContacts != null) {
         try {
            int var1 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            MessagesController var2 = MessagesController.getInstance(this.currentAccount);
            ArrayList var3 = this.onlineContacts;
            _$$Lambda$ContactsAdapter$AjIuF4bNE_A90essgyL0wfJ8HaU var4 = new _$$Lambda$ContactsAdapter$AjIuF4bNE_A90essgyL0wfJ8HaU(var2, var1);
            Collections.sort(var3, var4);
            this.notifyDataSetChanged();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

      }
   }
}
