package org.telegram.messenger;

import org.telegram.tgnet.ConnectionsManager;

public class AccountInstance {
   private static volatile AccountInstance[] Instance = new AccountInstance[3];
   private int currentAccount;

   public AccountInstance(int var1) {
      this.currentAccount = var1;
   }

   public static AccountInstance getInstance(int var0) {
      AccountInstance var1 = Instance[var0];
      AccountInstance var2 = var1;
      if (var1 == null) {
         synchronized(AccountInstance.class){}

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
               AccountInstance[] var23;
               try {
                  var23 = Instance;
                  var2 = new AccountInstance(var0);
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

   public ConnectionsManager getConnectionsManager() {
      return ConnectionsManager.getInstance(this.currentAccount);
   }

   public ContactsController getContactsController() {
      return ContactsController.getInstance(this.currentAccount);
   }

   public DataQuery getDataQuery() {
      return DataQuery.getInstance(this.currentAccount);
   }

   public MessagesController getMessagesController() {
      return MessagesController.getInstance(this.currentAccount);
   }

   public MessagesStorage getMessagesStorage() {
      return MessagesStorage.getInstance(this.currentAccount);
   }

   public NotificationCenter getNotificationCenter() {
      return NotificationCenter.getInstance(this.currentAccount);
   }

   public NotificationsController getNotificationsController() {
      return NotificationsController.getInstance(this.currentAccount);
   }

   public UserConfig getUserConfig() {
      return UserConfig.getInstance(this.currentAccount);
   }
}
