package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookAdapter extends RecyclerListView.SectionsAdapter {
   private int currentAccount;
   private Context mContext;

   public PhonebookAdapter(Context var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.mContext = var1;
   }

   public int getCountForSection(int var1) {
      HashMap var2 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
      ArrayList var3 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
      if (var1 < var3.size()) {
         int var4 = ((ArrayList)var2.get(var3.get(var1))).size();
         int var5 = var4;
         if (var1 != var3.size() - 1) {
            var5 = var4 + 1;
         }

         return var5;
      } else {
         return 0;
      }
   }

   public Object getItem(int var1, int var2) {
      HashMap var3 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
      ArrayList var4 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
      if (var1 < var4.size()) {
         var4 = (ArrayList)var3.get(var4.get(var1));
         if (var2 < var4.size()) {
            return var4.get(var2);
         }
      }

      return null;
   }

   public int getItemViewType(int var1, int var2) {
      byte var3;
      if (var2 < ((ArrayList)ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(var1))).size()) {
         var3 = 0;
      } else {
         var3 = 1;
      }

      return var3;
   }

   public String getLetter(int var1) {
      ArrayList var2 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
      int var3 = this.getSectionForPosition(var1);
      var1 = var3;
      if (var3 == -1) {
         var1 = var2.size() - 1;
      }

      return var1 >= 0 && var1 < var2.size() ? (String)var2.get(var1) : null;
   }

   public int getPositionForScrollProgress(float var1) {
      return (int)((float)this.getItemCount() * var1);
   }

   public int getSectionCount() {
      return ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.size();
   }

   public View getSectionHeaderView(int var1, View var2) {
      HashMap var3 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
      ArrayList var4 = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
      Object var6 = var2;
      if (var2 == null) {
         var6 = new LetterSectionCell(this.mContext);
      }

      LetterSectionCell var5 = (LetterSectionCell)var6;
      if (var1 < var4.size()) {
         var5.setLetter((String)var4.get(var1));
      } else {
         var5.setLetter("");
      }

      return (View)var6;
   }

   public boolean isEnabled(int var1, int var2) {
      boolean var3;
      if (var2 < ((ArrayList)ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(var1))).size()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
      if (var3.getItemViewType() == 0) {
         UserCell var4 = (UserCell)var3.itemView;
         Object var7 = this.getItem(var1, var2);
         TLRPC.User var8;
         if (var7 instanceof ContactsController.Contact) {
            ContactsController.Contact var5 = (ContactsController.Contact)var7;
            var8 = var5.user;
            if (var8 == null) {
               var4.setCurrentId(var5.contact_id);
               String var6 = ContactsController.formatName(var5.first_name, var5.last_name);
               String var9;
               if (var5.phones.isEmpty()) {
                  var9 = "";
               } else {
                  var9 = PhoneFormat.getInstance().format((String)var5.phones.get(0));
               }

               var4.setData((TLObject)null, var6, var9, 0);
               var8 = null;
            }
         } else {
            var8 = (TLRPC.User)var7;
         }

         if (var8 != null) {
            PhoneFormat var10 = PhoneFormat.getInstance();
            StringBuilder var11 = new StringBuilder();
            var11.append("+");
            var11.append(var8.phone);
            var4.setData(var8, (CharSequence)null, var10.format(var11.toString()), 0);
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var7;
      if (var2 != 0) {
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
         var7 = new UserCell(this.mContext, 58, 1, false);
         ((UserCell)var7).setNameTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      }

      return new RecyclerListView.Holder((View)var7);
   }
}
