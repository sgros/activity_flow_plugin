package org.telegram.messenger;

import android.text.TextUtils;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.tgnet.TLRPC;

public class UserObject {
   public static String getFirstName(TLRPC.User var0) {
      return getFirstName(var0, true);
   }

   public static String getFirstName(TLRPC.User var0, boolean var1) {
      if (var0 != null && !isDeleted(var0)) {
         String var2 = var0.first_name;
         String var3;
         if (TextUtils.isEmpty(var2)) {
            var3 = var0.last_name;
         } else {
            var3 = var2;
            if (!var1) {
               var3 = var2;
               if (var2.length() <= 2) {
                  return ContactsController.formatName(var0.first_name, var0.last_name);
               }
            }
         }

         if (TextUtils.isEmpty(var3)) {
            var3 = LocaleController.getString("HiddenName", 2131559636);
         }

         return var3;
      } else {
         return "DELETED";
      }
   }

   public static String getUserName(TLRPC.User var0) {
      if (var0 != null && !isDeleted(var0)) {
         String var1 = ContactsController.formatName(var0.first_name, var0.last_name);
         String var2 = var1;
         if (var1.length() == 0) {
            String var3 = var0.phone;
            var2 = var1;
            if (var3 != null) {
               if (var3.length() == 0) {
                  var2 = var1;
               } else {
                  PhoneFormat var5 = PhoneFormat.getInstance();
                  StringBuilder var4 = new StringBuilder();
                  var4.append("+");
                  var4.append(var0.phone);
                  var2 = var5.format(var4.toString());
               }
            }
         }

         return var2;
      } else {
         return LocaleController.getString("HiddenName", 2131559636);
      }
   }

   public static boolean isContact(TLRPC.User var0) {
      boolean var1;
      if (var0 == null || !(var0 instanceof TLRPC.TL_userContact_old2) && !var0.contact && !var0.mutual_contact) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isDeleted(TLRPC.User var0) {
      boolean var1;
      if (var0 != null && !(var0 instanceof TLRPC.TL_userDeleted_old2) && !(var0 instanceof TLRPC.TL_userEmpty) && !var0.deleted) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isUserSelf(TLRPC.User var0) {
      boolean var1;
      if (var0 == null || !(var0 instanceof TLRPC.TL_userSelf_old3) && !var0.self) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
