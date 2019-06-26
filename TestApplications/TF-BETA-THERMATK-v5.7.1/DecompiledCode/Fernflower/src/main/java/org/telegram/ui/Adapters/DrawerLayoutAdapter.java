package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Components.RecyclerListView;

public class DrawerLayoutAdapter extends RecyclerListView.SelectionAdapter {
   private ArrayList accountNumbers = new ArrayList();
   private boolean accountsShowed;
   private ArrayList items = new ArrayList(11);
   private Context mContext;
   private DrawerProfileCell profileCell;

   public DrawerLayoutAdapter(Context var1) {
      this.mContext = var1;
      int var2 = UserConfig.getActivatedAccountsCount();
      boolean var3 = true;
      if (var2 <= 1 || !MessagesController.getGlobalMainSettings().getBoolean("accountsShowed", true)) {
         var3 = false;
      }

      this.accountsShowed = var3;
      Theme.createDialogsResources(var1);
      this.resetItems();
   }

   private int getAccountRowsCount() {
      int var1 = this.accountNumbers.size() + 1;
      int var2 = var1;
      if (this.accountNumbers.size() < 3) {
         var2 = var1 + 1;
      }

      return var2;
   }

   // $FF: synthetic method
   static int lambda$resetItems$1(Integer var0, Integer var1) {
      long var2 = (long)UserConfig.getInstance(var0).loginTime;
      long var4 = (long)UserConfig.getInstance(var1).loginTime;
      if (var2 > var4) {
         return 1;
      } else {
         return var2 < var4 ? -1 : 0;
      }
   }

   private void resetItems() {
      this.accountNumbers.clear();

      for(int var1 = 0; var1 < 3; ++var1) {
         if (UserConfig.getInstance(var1).isClientActivated()) {
            this.accountNumbers.add(var1);
         }
      }

      Collections.sort(this.accountNumbers, _$$Lambda$DrawerLayoutAdapter$4y_nKr_8KM_K0ZE7aMWcSgkl64k.INSTANCE);
      this.items.clear();
      if (UserConfig.getInstance(UserConfig.selectedAccount).isClientActivated()) {
         if (Theme.getEventType() == 0) {
            this.items.add(new DrawerLayoutAdapter.Item(2, LocaleController.getString("NewGroup", 2131559900), 2131165581));
            this.items.add(new DrawerLayoutAdapter.Item(3, LocaleController.getString("NewSecretChat", 2131559909), 2131165595));
            this.items.add(new DrawerLayoutAdapter.Item(4, LocaleController.getString("NewChannel", 2131559898), 2131165573));
            this.items.add((Object)null);
            this.items.add(new DrawerLayoutAdapter.Item(6, LocaleController.getString("Contacts", 2131559149), 2131165577));
            this.items.add(new DrawerLayoutAdapter.Item(11, LocaleController.getString("SavedMessages", 2131560633), 2131165567));
            this.items.add(new DrawerLayoutAdapter.Item(10, LocaleController.getString("Calls", 2131558888), 2131165570));
            this.items.add(new DrawerLayoutAdapter.Item(7, LocaleController.getString("InviteFriends", 2131559677), 2131165585));
            this.items.add(new DrawerLayoutAdapter.Item(8, LocaleController.getString("Settings", 2131560738), 2131165598));
            this.items.add(new DrawerLayoutAdapter.Item(9, LocaleController.getString("TelegramFAQ", 2131560869), 2131165583));
         } else {
            this.items.add(new DrawerLayoutAdapter.Item(2, LocaleController.getString("NewGroup", 2131559900), 2131165580));
            this.items.add(new DrawerLayoutAdapter.Item(3, LocaleController.getString("NewSecretChat", 2131559909), 2131165594));
            this.items.add(new DrawerLayoutAdapter.Item(4, LocaleController.getString("NewChannel", 2131559898), 2131165568));
            this.items.add((Object)null);
            this.items.add(new DrawerLayoutAdapter.Item(6, LocaleController.getString("Contacts", 2131559149), 2131165576));
            this.items.add(new DrawerLayoutAdapter.Item(11, LocaleController.getString("SavedMessages", 2131560633), 2131165592));
            this.items.add(new DrawerLayoutAdapter.Item(10, LocaleController.getString("Calls", 2131558888), 2131165569));
            this.items.add(new DrawerLayoutAdapter.Item(7, LocaleController.getString("InviteFriends", 2131559677), 2131165584));
            this.items.add(new DrawerLayoutAdapter.Item(8, LocaleController.getString("Settings", 2131560738), 2131165596));
            this.items.add(new DrawerLayoutAdapter.Item(9, LocaleController.getString("TelegramFAQ", 2131560869), 2131165582));
         }

      }
   }

   public int getId(int var1) {
      int var2 = var1 - 2;
      var1 = var2;
      if (this.accountsShowed) {
         var1 = var2 - this.getAccountRowsCount();
      }

      byte var3 = -1;
      var2 = var3;
      if (var1 >= 0) {
         if (var1 >= this.items.size()) {
            var2 = var3;
         } else {
            DrawerLayoutAdapter.Item var4 = (DrawerLayoutAdapter.Item)this.items.get(var1);
            var2 = var3;
            if (var4 != null) {
               var2 = var4.id;
            }
         }
      }

      return var2;
   }

   public int getItemCount() {
      int var1 = this.items.size() + 2;
      int var2 = var1;
      if (this.accountsShowed) {
         var2 = var1 + this.getAccountRowsCount();
      }

      return var2;
   }

   public int getItemViewType(int var1) {
      if (var1 == 0) {
         return 0;
      } else if (var1 == 1) {
         return 1;
      } else {
         int var2 = var1 - 2;
         var1 = var2;
         if (this.accountsShowed) {
            if (var2 < this.accountNumbers.size()) {
               return 4;
            }

            if (this.accountNumbers.size() < 3) {
               if (var2 == this.accountNumbers.size()) {
                  return 5;
               }

               if (var2 == this.accountNumbers.size() + 1) {
                  return 2;
               }
            } else if (var2 == this.accountNumbers.size()) {
               return 2;
            }

            var1 = var2 - this.getAccountRowsCount();
         }

         return var1 == 3 ? 2 : 3;
      }
   }

   public boolean isAccountsShowed() {
      return this.accountsShowed;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      int var2 = var1.getItemViewType();
      boolean var3;
      if (var2 != 3 && var2 != 4 && var2 != 5) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   // $FF: synthetic method
   public void lambda$onCreateViewHolder$0$DrawerLayoutAdapter(View var1) {
      this.setAccountsShowed(((DrawerProfileCell)var1).isAccountsShowed(), true);
   }

   public void notifyDataSetChanged() {
      this.resetItems();
      super.notifyDataSetChanged();
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      if (var3 != 0) {
         if (var3 != 3) {
            if (var3 == 4) {
               ((DrawerUserCell)var1.itemView).setAccount((Integer)this.accountNumbers.get(var2 - 2));
            }
         } else {
            var3 = var2 - 2;
            var2 = var3;
            if (this.accountsShowed) {
               var2 = var3 - this.getAccountRowsCount();
            }

            DrawerActionCell var4 = (DrawerActionCell)var1.itemView;
            ((DrawerLayoutAdapter.Item)this.items.get(var2)).bind(var4);
            var4.setPadding(0, 0, 0, 0);
         }
      } else {
         ((DrawerProfileCell)var1.itemView).setUser(MessagesController.getInstance(UserConfig.selectedAccount).getUser(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()), this.accountsShowed);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var3;
      if (var2 != 0) {
         if (var2 != 2) {
            if (var2 != 3) {
               if (var2 != 4) {
                  if (var2 != 5) {
                     var3 = new EmptyCell(this.mContext, AndroidUtilities.dp(8.0F));
                  } else {
                     var3 = new DrawerAddCell(this.mContext);
                  }
               } else {
                  var3 = new DrawerUserCell(this.mContext);
               }
            } else {
               var3 = new DrawerActionCell(this.mContext);
            }
         } else {
            var3 = new DividerCell(this.mContext);
         }
      } else {
         this.profileCell = new DrawerProfileCell(this.mContext);
         this.profileCell.setOnArrowClickListener(new _$$Lambda$DrawerLayoutAdapter$u_CA_vrvN_8Y4__5_7WI_JZfY_U(this));
         var3 = this.profileCell;
      }

      ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
      return new RecyclerListView.Holder((View)var3);
   }

   public void setAccountsShowed(boolean var1, boolean var2) {
      if (this.accountsShowed != var1) {
         this.accountsShowed = var1;
         DrawerProfileCell var3 = this.profileCell;
         if (var3 != null) {
            var3.setAccountsShowed(this.accountsShowed);
         }

         MessagesController.getGlobalMainSettings().edit().putBoolean("accountsShowed", this.accountsShowed).commit();
         if (var2) {
            if (this.accountsShowed) {
               this.notifyItemRangeInserted(2, this.getAccountRowsCount());
            } else {
               this.notifyItemRangeRemoved(2, this.getAccountRowsCount());
            }
         } else {
            this.notifyDataSetChanged();
         }

      }
   }

   private class Item {
      public int icon;
      public int id;
      public String text;

      public Item(int var2, String var3, int var4) {
         this.icon = var4;
         this.id = var2;
         this.text = var3;
      }

      public void bind(DrawerActionCell var1) {
         var1.setTextAndIcon(this.text, this.icon);
      }
   }
}
