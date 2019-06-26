package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.ContentProviderOperation.Builder;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class ContactsController {
   private static volatile ContactsController[] Instance = new ContactsController[3];
   public static final int PRIVACY_RULES_TYPE_CALLS = 2;
   public static final int PRIVACY_RULES_TYPE_COUNT = 7;
   public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
   public static final int PRIVACY_RULES_TYPE_INVITE = 1;
   public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
   public static final int PRIVACY_RULES_TYPE_P2P = 3;
   public static final int PRIVACY_RULES_TYPE_PHONE = 6;
   public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
   private ArrayList callPrivacyRules;
   private int completedRequestsCount;
   public ArrayList contacts = new ArrayList();
   public HashMap contactsBook = new HashMap();
   private boolean contactsBookLoaded;
   public HashMap contactsBookSPhones = new HashMap();
   public HashMap contactsByPhone = new HashMap();
   public HashMap contactsByShortPhone = new HashMap();
   public ConcurrentHashMap contactsDict = new ConcurrentHashMap(20, 1.0F, 2);
   public boolean contactsLoaded;
   private boolean contactsSyncInProgress;
   private int currentAccount;
   private ArrayList delayedContactsUpdate = new ArrayList();
   private int deleteAccountTTL;
   private ArrayList forwardsPrivacyRules;
   private ArrayList groupPrivacyRules;
   private boolean ignoreChanges;
   private String inviteLink;
   private String lastContactsVersions = "";
   private ArrayList lastseenPrivacyRules;
   private final Object loadContactsSync = new Object();
   private boolean loadingContacts;
   private int loadingDeleteInfo;
   private int[] loadingPrivacyInfo = new int[7];
   private boolean migratingContacts;
   private final Object observerLock = new Object();
   private ArrayList p2pPrivacyRules;
   public ArrayList phoneBookContacts = new ArrayList();
   public ArrayList phoneBookSectionsArray = new ArrayList();
   public HashMap phoneBookSectionsDict = new HashMap();
   private ArrayList phonePrivacyRules;
   private ArrayList profilePhotoPrivacyRules;
   private String[] projectionNames = new String[]{"lookup", "data2", "data3", "data5"};
   private String[] projectionPhones = new String[]{"lookup", "data1", "data2", "data3", "display_name", "account_type"};
   private HashMap sectionsToReplace = new HashMap();
   public ArrayList sortedUsersMutualSectionsArray = new ArrayList();
   public ArrayList sortedUsersSectionsArray = new ArrayList();
   private Account systemAccount;
   private boolean updatingInviteLink;
   public HashMap usersMutualSectionsDict = new HashMap();
   public HashMap usersSectionsDict = new HashMap();

   public ContactsController(int var1) {
      this.currentAccount = var1;
      if (MessagesController.getMainSettings(this.currentAccount).getBoolean("needGetStatuses", false)) {
         this.reloadContactsStatuses();
      }

      this.sectionsToReplace.put("À", "A");
      this.sectionsToReplace.put("Á", "A");
      this.sectionsToReplace.put("Ä", "A");
      this.sectionsToReplace.put("Ù", "U");
      this.sectionsToReplace.put("Ú", "U");
      this.sectionsToReplace.put("Ü", "U");
      this.sectionsToReplace.put("Ì", "I");
      this.sectionsToReplace.put("Í", "I");
      this.sectionsToReplace.put("Ï", "I");
      this.sectionsToReplace.put("È", "E");
      this.sectionsToReplace.put("É", "E");
      this.sectionsToReplace.put("Ê", "E");
      this.sectionsToReplace.put("Ë", "E");
      this.sectionsToReplace.put("Ò", "O");
      this.sectionsToReplace.put("Ó", "O");
      this.sectionsToReplace.put("Ö", "O");
      this.sectionsToReplace.put("Ç", "C");
      this.sectionsToReplace.put("Ñ", "N");
      this.sectionsToReplace.put("Ÿ", "Y");
      this.sectionsToReplace.put("Ý", "Y");
      this.sectionsToReplace.put("Ţ", "Y");
      if (var1 == 0) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$IIQVqTpQCvnIY8_p91gfnj3he0s(this));
      }

   }

   private void applyContactsUpdates(ArrayList var1, ConcurrentHashMap var2, ArrayList var3, ArrayList var4) {
      byte var5;
      ArrayList var6;
      int var8;
      label117: {
         var5 = 0;
         if (var3 != null) {
            var6 = var3;
            var3 = var4;
            if (var4 != null) {
               break label117;
            }
         }

         var4 = new ArrayList();
         ArrayList var7 = new ArrayList();
         var8 = 0;

         while(true) {
            var6 = var4;
            var3 = var7;
            if (var8 >= var1.size()) {
               break;
            }

            Integer var18 = (Integer)var1.get(var8);
            if (var18 > 0) {
               TLRPC.TL_contact var16 = new TLRPC.TL_contact();
               var16.user_id = var18;
               var4.add(var16);
            } else if (var18 < 0) {
               var7.add(-var18);
            }

            ++var8;
         }
      }

      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var14 = new StringBuilder();
         var14.append("process update - contacts add = ");
         var14.append(var6.size());
         var14.append(" delete = ");
         var14.append(var3.size());
         FileLog.d(var14.toString());
      }

      StringBuilder var17 = new StringBuilder();
      StringBuilder var19 = new StringBuilder();
      int var9 = 0;
      boolean var20 = false;

      while(true) {
         int var10 = var6.size();
         TLRPC.User var15 = null;
         int var11 = var5;
         boolean var12 = var20;
         ContactsController.Contact var21;
         if (var9 >= var10) {
            while(var11 < var3.size()) {
               Integer var22 = (Integer)var3.get(var11);
               Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$HjkvfPeYk7Ow3aL58FFYm929xXw(this, var22));
               if (var2 != null) {
                  var15 = (TLRPC.User)var2.get(var22);
               } else {
                  var15 = null;
               }

               if (var15 == null) {
                  var15 = MessagesController.getInstance(this.currentAccount).getUser(var22);
               } else {
                  MessagesController.getInstance(this.currentAccount).putUser(var15, true);
               }

               if (var15 == null) {
                  var20 = true;
               } else {
                  var20 = var12;
                  if (!TextUtils.isEmpty(var15.phone)) {
                     var21 = (ContactsController.Contact)this.contactsBookSPhones.get(var15.phone);
                     if (var21 != null) {
                        var8 = var21.shortPhones.indexOf(var15.phone);
                        if (var8 != -1) {
                           var21.phoneDeleted.set(var8, 1);
                        }
                     }

                     if (var19.length() != 0) {
                        var19.append(",");
                     }

                     var19.append(var15.phone);
                     var20 = var12;
                  }
               }

               ++var11;
               var12 = var20;
            }

            if (var17.length() != 0 || var19.length() != 0) {
               MessagesStorage.getInstance(this.currentAccount).applyPhoneBookUpdates(var17.toString(), var19.toString());
            }

            if (var12) {
               Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$zT0R4MDQT_YLqu_ka0J_Safa99M(this));
            } else {
               AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE(this, var6, var3));
            }

            return;
         }

         TLRPC.TL_contact var13 = (TLRPC.TL_contact)var6.get(var9);
         if (var2 != null) {
            var15 = (TLRPC.User)var2.get(var13.user_id);
         }

         if (var15 == null) {
            var15 = MessagesController.getInstance(this.currentAccount).getUser(var13.user_id);
         } else {
            MessagesController.getInstance(this.currentAccount).putUser(var15, true);
         }

         if (var15 != null && !TextUtils.isEmpty(var15.phone)) {
            var21 = (ContactsController.Contact)this.contactsBookSPhones.get(var15.phone);
            if (var21 != null) {
               var11 = var21.shortPhones.indexOf(var15.phone);
               if (var11 != -1) {
                  var21.phoneDeleted.set(var11, 0);
               }
            }

            if (var17.length() != 0) {
               var17.append(",");
            }

            var17.append(var15.phone);
         } else {
            var20 = true;
         }

         ++var9;
      }
   }

   private void buildContactsSectionsArrays(boolean var1) {
      if (var1) {
         Collections.sort(this.contacts, new _$$Lambda$ContactsController$l3XEyiXk02DazId_mQdpRpST3Co(this));
      }

      HashMap var2 = new HashMap();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < this.contacts.size(); ++var4) {
         TLRPC.TL_contact var5 = (TLRPC.TL_contact)this.contacts.get(var4);
         TLRPC.User var6 = MessagesController.getInstance(this.currentAccount).getUser(var5.user_id);
         if (var6 != null) {
            String var7 = UserObject.getFirstName(var6);
            String var9 = var7;
            if (var7.length() > 1) {
               var9 = var7.substring(0, 1);
            }

            if (var9.length() == 0) {
               var9 = "#";
            } else {
               var9 = var9.toUpperCase();
            }

            var7 = (String)this.sectionsToReplace.get(var9);
            if (var7 != null) {
               var9 = var7;
            }

            ArrayList var8 = (ArrayList)var2.get(var9);
            ArrayList var10 = var8;
            if (var8 == null) {
               var10 = new ArrayList();
               var2.put(var9, var10);
               var3.add(var9);
            }

            var10.add(var5);
         }
      }

      Collections.sort(var3, _$$Lambda$ContactsController$4fSnP4tj8Rx2FPjE7t10auDnEhg.INSTANCE);
      this.usersSectionsDict = var2;
      this.sortedUsersSectionsArray = var3;
   }

   private boolean checkContactsInternal() {
      // $FF: Couldn't be decompiled
   }

   private void deleteContactFromPhoneBook(int param1) {
      // $FF: Couldn't be decompiled
   }

   public static String formatName(String var0, String var1) {
      String var2 = var0;
      if (var0 != null) {
         var2 = var0.trim();
      }

      var0 = var1;
      if (var1 != null) {
         var0 = var1.trim();
      }

      int var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.length();
      } else {
         var4 = 0;
      }

      if (var0 != null) {
         var3 = var0.length();
      }

      StringBuilder var5 = new StringBuilder(var4 + var3 + 1);
      if (LocaleController.nameDisplayOrder == 1) {
         if (var2 != null && var2.length() > 0) {
            var5.append(var2);
            if (var0 != null && var0.length() > 0) {
               var5.append(" ");
               var5.append(var0);
            }
         } else if (var0 != null && var0.length() > 0) {
            var5.append(var0);
         }
      } else if (var0 != null && var0.length() > 0) {
         var5.append(var0);
         if (var2 != null && var2.length() > 0) {
            var5.append(" ");
            var5.append(var2);
         }
      } else if (var2 != null && var2.length() > 0) {
         var5.append(var2);
      }

      return var5.toString();
   }

   private int getContactsHash(ArrayList var1) {
      var1 = new ArrayList(var1);
      Collections.sort(var1, _$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y.INSTANCE);
      int var2 = var1.size();
      long var3 = 0L;

      for(int var5 = -1; var5 < var2; ++var5) {
         if (var5 == -1) {
            var3 = (var3 * 20261L + 2147483648L + (long)UserConfig.getInstance(this.currentAccount).contactsSavedCount) % 2147483648L;
         } else {
            var3 = (var3 * 20261L + 2147483648L + (long)((TLRPC.TL_contact)var1.get(var5)).user_id) % 2147483648L;
         }
      }

      return (int)var3;
   }

   public static ContactsController getInstance(int var0) {
      ContactsController var1 = Instance[var0];
      ContactsController var2 = var1;
      if (var1 == null) {
         synchronized(ContactsController.class){}

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
               ContactsController[] var23;
               try {
                  var23 = Instance;
                  var2 = new ContactsController(var0);
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

   private boolean hasContactsPermission() {
      // $FF: Couldn't be decompiled
   }

   private boolean isNotValidNameString(String var1) {
      boolean var2 = TextUtils.isEmpty(var1);
      boolean var3 = true;
      if (var2) {
         return true;
      } else {
         int var4 = var1.length();
         int var5 = 0;

         int var6;
         int var8;
         for(var6 = 0; var5 < var4; var6 = var8) {
            char var7 = var1.charAt(var5);
            var8 = var6;
            if (var7 >= '0') {
               var8 = var6;
               if (var7 <= '9') {
                  var8 = var6 + 1;
               }
            }

            ++var5;
         }

         if (var6 <= 3) {
            var3 = false;
         }

         return var3;
      }
   }

   // $FF: synthetic method
   static int lambda$buildContactsSectionsArrays$42(String var0, String var1) {
      char var2 = var0.charAt(0);
      char var3 = var1.charAt(0);
      if (var2 == '#') {
         return 1;
      } else {
         return var3 == '#' ? -1 : var0.compareTo(var1);
      }
   }

   // $FF: synthetic method
   static int lambda$getContactsHash$25(TLRPC.TL_contact var0, TLRPC.TL_contact var1) {
      int var2 = var0.user_id;
      int var3 = var1.user_id;
      if (var2 > var3) {
         return 1;
      } else {
         return var2 < var3 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$markAsContacted$47(String var0) {
      Uri var1 = Uri.parse(var0);
      ContentValues var2 = new ContentValues();
      var2.put("last_time_contacted", System.currentTimeMillis());
      ApplicationLoader.applicationContext.getContentResolver().update(var1, var2, (String)null, (String[])null);
   }

   // $FF: synthetic method
   static int lambda$null$28(SparseArray var0, TLRPC.TL_contact var1, TLRPC.TL_contact var2) {
      TLRPC.User var4 = (TLRPC.User)var0.get(var1.user_id);
      TLRPC.User var3 = (TLRPC.User)var0.get(var2.user_id);
      return UserObject.getFirstName(var4).compareTo(UserObject.getFirstName(var3));
   }

   // $FF: synthetic method
   static int lambda$null$29(String var0, String var1) {
      char var2 = var0.charAt(0);
      char var3 = var1.charAt(0);
      if (var2 == '#') {
         return 1;
      } else {
         return var3 == '#' ? -1 : var0.compareTo(var1);
      }
   }

   // $FF: synthetic method
   static int lambda$null$30(String var0, String var1) {
      char var2 = var0.charAt(0);
      char var3 = var1.charAt(0);
      if (var2 == '#') {
         return 1;
      } else {
         return var3 == '#' ? -1 : var0.compareTo(var1);
      }
   }

   // $FF: synthetic method
   static int lambda$null$36(Object var0, Object var1) {
      boolean var2 = var0 instanceof TLRPC.User;
      String var3 = "";
      String var6;
      if (var2) {
         TLRPC.User var5 = (TLRPC.User)var0;
         var6 = formatName(var5.first_name, var5.last_name);
      } else if (var0 instanceof ContactsController.Contact) {
         ContactsController.Contact var7 = (ContactsController.Contact)var0;
         TLRPC.User var4 = var7.user;
         if (var4 != null) {
            var6 = formatName(var4.first_name, var4.last_name);
         } else {
            var6 = formatName(var7.first_name, var7.last_name);
         }
      } else {
         var6 = "";
      }

      if (var1 instanceof TLRPC.User) {
         TLRPC.User var8 = (TLRPC.User)var1;
         var3 = formatName(var8.first_name, var8.last_name);
      } else if (var1 instanceof ContactsController.Contact) {
         ContactsController.Contact var9 = (ContactsController.Contact)var1;
         TLRPC.User var11 = var9.user;
         String var10;
         if (var11 != null) {
            var10 = formatName(var11.first_name, var11.last_name);
         } else {
            var10 = formatName(var9.first_name, var9.last_name);
         }

         var3 = var10;
      }

      return var6.compareTo(var3);
   }

   // $FF: synthetic method
   static int lambda$null$37(String var0, String var1) {
      char var2 = var0.charAt(0);
      char var3 = var1.charAt(0);
      if (var2 == '#') {
         return 1;
      } else {
         return var3 == '#' ? -1 : var0.compareTo(var1);
      }
   }

   // $FF: synthetic method
   static void lambda$resetImportedContacts$9(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$updateUnregisteredContacts$40(ContactsController.Contact var0, ContactsController.Contact var1) {
      String var2 = var0.first_name;
      String var3 = var2;
      if (var2.length() == 0) {
         var3 = var0.last_name;
      }

      var2 = var1.first_name;
      String var4 = var2;
      if (var2.length() == 0) {
         var4 = var1.last_name;
      }

      return var3.compareTo(var4);
   }

   private void mergePhonebookAndTelegramContacts(HashMap var1, ArrayList var2, HashMap var3) {
      ArrayList var4 = new ArrayList(this.contacts);
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$gajOA1_r5XMonBIZ6Lf97P0bD9I(this, var4, var3, var1, var2));
   }

   private void performWriteContactsToPhoneBook() {
      ArrayList var1 = new ArrayList(this.contacts);
      Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$E0GDxDm4XOysG7SHcvpj5BVOSkQ(this, var1));
   }

   private void performWriteContactsToPhoneBookInternal(ArrayList param1) {
      // $FF: Couldn't be decompiled
   }

   private HashMap readContactsFromPhoneBook() {
      // $FF: Couldn't be decompiled
   }

   private void reloadContactsStatusesMaybe() {
      try {
         if (MessagesController.getMainSettings(this.currentAccount).getLong("lastReloadStatusTime", 0L) < System.currentTimeMillis() - 86400000L) {
            this.reloadContactsStatuses();
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   private void saveContactsLoadTime() {
      try {
         MessagesController.getMainSettings(this.currentAccount).edit().putLong("lastReloadStatusTime", System.currentTimeMillis()).commit();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   private void updateUnregisteredContacts() {
      HashMap var1 = new HashMap();
      int var2 = this.contacts.size();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         TLRPC.TL_contact var4 = (TLRPC.TL_contact)this.contacts.get(var3);
         TLRPC.User var5 = MessagesController.getInstance(this.currentAccount).getUser(var4.user_id);
         if (var5 != null && !TextUtils.isEmpty(var5.phone)) {
            var1.put(var5.phone, var4);
         }
      }

      ArrayList var10 = new ArrayList();
      Iterator var9 = this.contactsBook.entrySet().iterator();

      while(var9.hasNext()) {
         ContactsController.Contact var6 = (ContactsController.Contact)((Entry)var9.next()).getValue();
         var3 = 0;

         boolean var8;
         while(true) {
            var2 = var6.phones.size();
            boolean var7 = true;
            if (var3 >= var2) {
               var8 = false;
               break;
            }

            var8 = var7;
            if (var1.containsKey((String)var6.shortPhones.get(var3))) {
               break;
            }

            if ((Integer)var6.phoneDeleted.get(var3) == 1) {
               var8 = var7;
               break;
            }

            ++var3;
         }

         if (!var8) {
            var10.add(var6);
         }
      }

      Collections.sort(var10, _$$Lambda$ContactsController$mHBJOOEPuO6QrX7ZbJrnWuc_NBQ.INSTANCE);
      this.phoneBookContacts = var10;
   }

   public void addContact(TLRPC.User var1) {
      if (var1 != null && !TextUtils.isEmpty(var1.phone)) {
         TLRPC.TL_contacts_importContacts var2 = new TLRPC.TL_contacts_importContacts();
         ArrayList var3 = new ArrayList();
         TLRPC.TL_inputPhoneContact var4 = new TLRPC.TL_inputPhoneContact();
         var4.phone = var1.phone;
         if (!var4.phone.startsWith("+")) {
            StringBuilder var5 = new StringBuilder();
            var5.append("+");
            var5.append(var4.phone);
            var4.phone = var5.toString();
         }

         var4.first_name = var1.first_name;
         var4.last_name = var1.last_name;
         var4.client_id = 0L;
         var3.add(var4);
         var2.contacts = var3;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$ContactsController$_Vi_J8_93cHW3trChoxTwI17XeA(this), 6);
      }

   }

   public long addContactToPhoneBook(TLRPC.User param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void checkAppAccount() {
      // $FF: Couldn't be decompiled
   }

   public void checkContacts() {
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$gch7bRXld1l_k0g1GAfFCoMhjIs(this));
   }

   public void checkInviteText() {
      SharedPreferences var1 = MessagesController.getMainSettings(this.currentAccount);
      this.inviteLink = var1.getString("invitelink", (String)null);
      int var2 = var1.getInt("invitelinktime", 0);
      if (!this.updatingInviteLink && (this.inviteLink == null || Math.abs(System.currentTimeMillis() / 1000L - (long)var2) >= 86400L)) {
         this.updatingInviteLink = true;
         TLRPC.TL_help_getInviteText var3 = new TLRPC.TL_help_getInviteText();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$ContactsController$kcKNthDEzlD6nPSHQklW5Vw14V8(this), 2);
      }

   }

   public void cleanup() {
      this.contactsBook.clear();
      this.contactsBookSPhones.clear();
      this.phoneBookContacts.clear();
      this.contacts.clear();
      this.contactsDict.clear();
      this.usersSectionsDict.clear();
      this.usersMutualSectionsDict.clear();
      this.sortedUsersSectionsArray.clear();
      this.sortedUsersMutualSectionsArray.clear();
      this.delayedContactsUpdate.clear();
      this.contactsByPhone.clear();
      this.contactsByShortPhone.clear();
      this.phoneBookSectionsDict.clear();
      this.phoneBookSectionsArray.clear();
      this.loadingContacts = false;
      this.contactsSyncInProgress = false;
      this.contactsLoaded = false;
      this.contactsBookLoaded = false;
      this.lastContactsVersions = "";
      this.loadingDeleteInfo = 0;
      this.deleteAccountTTL = 0;
      int var1 = 0;

      while(true) {
         int[] var2 = this.loadingPrivacyInfo;
         if (var1 >= var2.length) {
            this.lastseenPrivacyRules = null;
            this.groupPrivacyRules = null;
            this.callPrivacyRules = null;
            this.p2pPrivacyRules = null;
            this.profilePhotoPrivacyRules = null;
            this.forwardsPrivacyRules = null;
            this.phonePrivacyRules = null;
            Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$6JQLrbPbpNJhoOI_5BGOSFa9lbo(this));
            return;
         }

         var2[var1] = 0;
         ++var1;
      }
   }

   public void createOrUpdateConnectionServiceContact(int var1, String var2, String var3) {
      if (this.hasContactsPermission()) {
         Exception var10000;
         label95: {
            ContentResolver var4;
            ArrayList var5;
            Uri var6;
            Uri var7;
            String var9;
            boolean var10001;
            Cursor var32;
            try {
               var4 = ApplicationLoader.applicationContext.getContentResolver();
               var5 = new ArrayList();
               var6 = Groups.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
               var7 = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
               String var8 = this.systemAccount.type;
               var9 = this.systemAccount.name;
               var32 = var4.query(var6, new String[]{"_id"}, "title=? AND account_type=? AND account_name=?", new String[]{"TelegramConnectionService", var8, var9}, (String)null);
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label95;
            }

            int var10;
            label85: {
               if (var32 != null) {
                  try {
                     if (var32.moveToFirst()) {
                        var10 = var32.getInt(0);
                        break label85;
                     }
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label95;
                  }
               }

               try {
                  ContentValues var30 = new ContentValues();
                  var30.put("account_type", this.systemAccount.type);
                  var30.put("account_name", this.systemAccount.name);
                  var30.put("group_visible", 0);
                  var30.put("group_is_read_only", 1);
                  var30.put("title", "TelegramConnectionService");
                  var10 = Integer.parseInt(var4.insert(var6, var30).getLastPathSegment());
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label95;
               }
            }

            if (var32 != null) {
               try {
                  var32.close();
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label95;
               }
            }

            int var11;
            try {
               var6 = Data.CONTENT_URI;
               StringBuilder var33 = new StringBuilder();
               var33.append(var10);
               var33.append("");
               var9 = var33.toString();
               var32 = var4.query(var6, new String[]{"raw_contact_id"}, "mimetype=? AND data1=?", new String[]{"vnd.android.cursor.item/group_membership", var9}, (String)null);
               var11 = var5.size();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label95;
            }

            label100: {
               ArrayList var26;
               if (var32 != null) {
                  label99: {
                     ContentProviderOperation var28;
                     try {
                        if (!var32.moveToFirst()) {
                           break label99;
                        }

                        var10 = var32.getInt(0);
                        Builder var25 = ContentProviderOperation.newUpdate(var7);
                        StringBuilder var27 = new StringBuilder();
                        var27.append(var10);
                        var27.append("");
                        var28 = var25.withSelection("_id=?", new String[]{var27.toString()}).withValue("deleted", 0).build();
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label95;
                     }

                     var26 = var5;

                     try {
                        var26.add(var28);
                        Builder var29 = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
                        StringBuilder var31 = new StringBuilder();
                        var31.append(var10);
                        var31.append("");
                        var29 = var29.withSelection("raw_contact_id=? AND mimetype=?", new String[]{var31.toString(), "vnd.android.cursor.item/phone_v2"});
                        var31 = new StringBuilder();
                        var31.append("+99084");
                        var31.append(var1);
                        var26.add(var29.withValue("data1", var31.toString()).build());
                        var29 = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
                        var31 = new StringBuilder();
                        var31.append(var10);
                        var31.append("");
                        var26.add(var29.withSelection("raw_contact_id=? AND mimetype=?", new String[]{var31.toString(), "vnd.android.cursor.item/name"}).withValue("data2", var2).withValue("data3", var3).build());
                        break label100;
                     } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label95;
                     }
                  }
               }

               var26 = var5;

               try {
                  var26.add(ContentProviderOperation.newInsert(var7).withValue("account_type", this.systemAccount.type).withValue("account_name", this.systemAccount.name).withValue("raw_contact_is_read_only", 1).withValue("aggregation_mode", 3).build());
                  var26.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", var11).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", var2).withValue("data3", var3).build());
                  Builder var22 = ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", var11).withValue("mimetype", "vnd.android.cursor.item/phone_v2");
                  StringBuilder var24 = new StringBuilder();
                  var24.append("+99084");
                  var24.append(var1);
                  var26.add(var22.withValue("data1", var24.toString()).build());
                  var26.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", var11).withValue("mimetype", "vnd.android.cursor.item/group_membership").withValue("data1", var10).build());
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label95;
               }
            }

            if (var32 != null) {
               try {
                  var32.close();
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label95;
               }
            }

            try {
               var4.applyBatch("com.android.contacts", var5);
               return;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
            }
         }

         Exception var23 = var10000;
         FileLog.e((Throwable)var23);
      }
   }

   public void deleteAllContacts(Runnable var1) {
      this.resetImportedContacts();
      TLRPC.TL_contacts_deleteContacts var2 = new TLRPC.TL_contacts_deleteContacts();
      int var3 = this.contacts.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.TL_contact var5 = (TLRPC.TL_contact)this.contacts.get(var4);
         var2.id.add(MessagesController.getInstance(this.currentAccount).getInputUser(var5.user_id));
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$ContactsController$Ok5Vywi0AIsSfLPeC7ZAX0Eg0KM(this, var1));
   }

   public void deleteConnectionServiceContact() {
      if (this.hasContactsPermission()) {
         Exception var10000;
         label69: {
            ContentResolver var1;
            Uri var2;
            String var3;
            Cursor var12;
            boolean var10001;
            try {
               var1 = ApplicationLoader.applicationContext.getContentResolver();
               var2 = Groups.CONTENT_URI;
               var3 = this.systemAccount.type;
               String var4 = this.systemAccount.name;
               var12 = var1.query(var2, new String[]{"_id"}, "title=? AND account_type=? AND account_name=?", new String[]{"TelegramConnectionService", var3, var4}, (String)null);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label69;
            }

            if (var12 != null) {
               label68: {
                  int var5;
                  StringBuilder var13;
                  try {
                     if (!var12.moveToFirst()) {
                        break label68;
                     }

                     var5 = var12.getInt(0);
                     var12.close();
                     var2 = Data.CONTENT_URI;
                     var13 = new StringBuilder();
                     var13.append(var5);
                     var13.append("");
                     var3 = var13.toString();
                     var12 = var1.query(var2, new String[]{"raw_contact_id"}, "mimetype=? AND data1=?", new String[]{"vnd.android.cursor.item/group_membership", var3}, (String)null);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label69;
                  }

                  if (var12 != null) {
                     try {
                        if (var12.moveToFirst()) {
                           var5 = var12.getInt(0);
                           var12.close();
                           var2 = RawContacts.CONTENT_URI;
                           var13 = new StringBuilder();
                           var13.append(var5);
                           var13.append("");
                           var1.delete(var2, "_id=?", new String[]{var13.toString()});
                           return;
                        }
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label69;
                     }
                  }

                  if (var12 != null) {
                     try {
                        var12.close();
                     } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label69;
                     }
                  }

                  return;
               }
            }

            if (var12 != null) {
               try {
                  var12.close();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label69;
               }
            }

            return;
         }

         Exception var11 = var10000;
         FileLog.e((Throwable)var11);
      }
   }

   public void deleteContact(ArrayList var1) {
      if (var1 != null && !var1.isEmpty()) {
         TLRPC.TL_contacts_deleteContacts var2 = new TLRPC.TL_contacts_deleteContacts();
         ArrayList var3 = new ArrayList();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            TLRPC.User var5 = (TLRPC.User)var4.next();
            TLRPC.InputUser var6 = MessagesController.getInstance(this.currentAccount).getInputUser(var5);
            if (var6 != null) {
               var3.add(var5.id);
               var2.id.add(var6);
            }
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$ContactsController$AcnaGQSIUUZcaRV7Rm4_aExKG2o(this, var3, var1));
      }

   }

   public void deleteUnknownAppAccounts() {
      Exception var10000;
      label60: {
         AccountManager var1;
         Account[] var2;
         boolean var10001;
         try {
            this.systemAccount = null;
            var1 = AccountManager.get(ApplicationLoader.applicationContext);
            var2 = var1.getAccountsByType("org.telegram.messenger");
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label60;
         }

         int var3 = 0;

         label53:
         while(true) {
            try {
               if (var3 >= var2.length) {
                  return;
               }
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            Account var4 = var2[var3];
            int var5 = 0;

            boolean var16;
            while(true) {
               if (var5 >= 3) {
                  var16 = false;
                  break;
               }

               TLRPC.User var6;
               try {
                  var6 = UserConfig.getInstance(var5).getCurrentUser();
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label53;
               }

               if (var6 != null) {
                  boolean var9;
                  try {
                     String var7 = var4.name;
                     StringBuilder var8 = new StringBuilder();
                     var8.append("");
                     var8.append(var6.id);
                     var9 = var7.equals(var8.toString());
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label53;
                  }

                  if (var9) {
                     var16 = true;
                     break;
                  }
               }

               ++var5;
            }

            if (!var16) {
               try {
                  var1.removeAccount(var2[var3], (AccountManagerCallback)null, (Handler)null);
               } catch (Exception var10) {
               }
            }

            ++var3;
         }
      }

      Exception var15 = var10000;
      var15.printStackTrace();
   }

   public void forceImportContacts() {
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$PHwuGsXHnbafxq3b4_SbXpvFUkE(this));
   }

   public HashMap getContactsCopy(HashMap var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         ContactsController.Contact var5 = new ContactsController.Contact();
         ContactsController.Contact var6 = (ContactsController.Contact)var4.getValue();
         var5.phoneDeleted.addAll(var6.phoneDeleted);
         var5.phones.addAll(var6.phones);
         var5.phoneTypes.addAll(var6.phoneTypes);
         var5.shortPhones.addAll(var6.shortPhones);
         var5.first_name = var6.first_name;
         var5.last_name = var6.last_name;
         var5.contact_id = var6.contact_id;
         var5.key = var6.key;
         var2.put(var5.key, var5);
      }

      return var2;
   }

   public int getDeleteAccountTTL() {
      return this.deleteAccountTTL;
   }

   public String getInviteText(int var1) {
      String var2 = this.inviteLink;
      String var3 = var2;
      if (var2 == null) {
         var3 = "https://f-droid.org/packages/org.telegram.messenger";
      }

      if (var1 <= 1) {
         return LocaleController.formatString("InviteText2", 2131559680, var3);
      } else {
         try {
            var2 = String.format(LocaleController.getPluralString("InviteTextNum", var1), var1, var3);
            return var2;
         } catch (Exception var4) {
            return LocaleController.formatString("InviteText2", 2131559680, var3);
         }
      }
   }

   public boolean getLoadingDeleteInfo() {
      boolean var1;
      if (this.loadingDeleteInfo != 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean getLoadingPrivicyInfo(int var1) {
      boolean var2;
      if (this.loadingPrivacyInfo[var1] != 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public ArrayList getPrivacyRules(int var1) {
      switch(var1) {
      case 0:
         return this.lastseenPrivacyRules;
      case 1:
         return this.groupPrivacyRules;
      case 2:
         return this.callPrivacyRules;
      case 3:
         return this.p2pPrivacyRules;
      case 4:
         return this.profilePhotoPrivacyRules;
      case 5:
         return this.forwardsPrivacyRules;
      case 6:
         return this.phonePrivacyRules;
      default:
         return null;
      }
   }

   public boolean isContact(int var1) {
      boolean var2;
      if (this.contactsDict.get(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isLoadingContacts() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$addContact$50$ContactsController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.TL_contacts_importedContacts var7 = (TLRPC.TL_contacts_importedContacts)var1;
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var7.users, (ArrayList)null, true, true);

         for(int var3 = 0; var3 < var7.users.size(); ++var3) {
            TLRPC.User var8 = (TLRPC.User)var7.users.get(var3);
            Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$tvZs_rDgKy4_ldDTgpQkKllXL0E(this, var8));
            TLRPC.TL_contact var4 = new TLRPC.TL_contact();
            var4.user_id = var8.id;
            ArrayList var5 = new ArrayList();
            var5.add(var4);
            MessagesStorage.getInstance(this.currentAccount).putContacts(var5, false);
            if (!TextUtils.isEmpty(var8.phone)) {
               formatName(var8.first_name, var8.last_name);
               MessagesStorage.getInstance(this.currentAccount).applyPhoneBookUpdates(var8.phone, "");
               ContactsController.Contact var9 = (ContactsController.Contact)this.contactsBookSPhones.get(var8.phone);
               if (var9 != null) {
                  int var6 = var9.shortPhones.indexOf(var8.phone);
                  if (var6 != -1) {
                     var9.phoneDeleted.set(var6, 0);
                  }
               }
            }
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$3Lppzk_aX70_Q6C3adV0V2TBG9k(this, var7));
      }
   }

   // $FF: synthetic method
   public void lambda$applyContactsUpdates$44$ContactsController(Integer var1) {
      this.deleteContactFromPhoneBook(var1);
   }

   // $FF: synthetic method
   public void lambda$applyContactsUpdates$45$ContactsController() {
      this.loadContacts(false, 0);
   }

   // $FF: synthetic method
   public void lambda$applyContactsUpdates$46$ContactsController(ArrayList var1, ArrayList var2) {
      int var3;
      TLRPC.TL_contact var4;
      for(var3 = 0; var3 < var1.size(); ++var3) {
         var4 = (TLRPC.TL_contact)var1.get(var3);
         if (this.contactsDict.get(var4.user_id) == null) {
            this.contacts.add(var4);
            this.contactsDict.put(var4.user_id, var4);
         }
      }

      for(var3 = 0; var3 < var2.size(); ++var3) {
         Integer var5 = (Integer)var2.get(var3);
         var4 = (TLRPC.TL_contact)this.contactsDict.get(var5);
         if (var4 != null) {
            this.contacts.remove(var4);
            this.contactsDict.remove(var5);
         }
      }

      if (!var1.isEmpty()) {
         this.updateUnregisteredContacts();
         this.performWriteContactsToPhoneBook();
      }

      this.performSyncPhoneBook(this.getContactsCopy(this.contactsBook), false, false, false, false, true, false);
      this.buildContactsSectionsArrays(var1.isEmpty() ^ true);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
   }

   // $FF: synthetic method
   public int lambda$buildContactsSectionsArrays$41$ContactsController(TLRPC.TL_contact var1, TLRPC.TL_contact var2) {
      TLRPC.User var3 = MessagesController.getInstance(this.currentAccount).getUser(var1.user_id);
      TLRPC.User var4 = MessagesController.getInstance(this.currentAccount).getUser(var2.user_id);
      return UserObject.getFirstName(var3).compareTo(UserObject.getFirstName(var4));
   }

   // $FF: synthetic method
   public void lambda$checkContacts$4$ContactsController() {
      if (this.checkContactsInternal()) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("detected contacts change");
         }

         this.performSyncPhoneBook(this.getContactsCopy(this.contactsBook), true, false, true, false, true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$checkInviteText$3$ContactsController(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         TLRPC.TL_help_inviteText var3 = (TLRPC.TL_help_inviteText)var1;
         if (var3.message.length() != 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$A4_cT__oXrEc9OYs6FogIzF6avE(this, var3));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$cleanup$1$ContactsController() {
      this.migratingContacts = false;
      this.completedRequestsCount = 0;
   }

   // $FF: synthetic method
   public void lambda$deleteAllContacts$8$ContactsController(Runnable var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 instanceof TLRPC.TL_boolTrue) {
         this.contactsBookSPhones.clear();
         this.contactsBook.clear();
         this.completedRequestsCount = 0;
         this.migratingContacts = false;
         this.contactsSyncInProgress = false;
         this.contactsLoaded = false;
         this.loadingContacts = false;
         this.contactsBookLoaded = false;
         this.lastContactsVersions = "";
         AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$Oyly5yQ4u_Oj_ZJTW79_45tDILE(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$deleteContact$53$ContactsController(ArrayList var1, ArrayList var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         MessagesStorage.getInstance(this.currentAccount).deleteContacts(var1);
         Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$4aPOKb9vaAxgXYIv3F96_1I3WZs(this, var2));

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            TLRPC.User var8 = (TLRPC.User)var2.get(var5);
            if (!TextUtils.isEmpty(var8.phone)) {
               UserObject.getUserName(var8);
               MessagesStorage.getInstance(this.currentAccount).applyPhoneBookUpdates(var8.phone, "");
               ContactsController.Contact var7 = (ContactsController.Contact)this.contactsBookSPhones.get(var8.phone);
               if (var7 != null) {
                  int var6 = var7.shortPhones.indexOf(var8.phone);
                  if (var6 != -1) {
                     var7.phoneDeleted.set(var6, 1);
                  }
               }
            }
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$c7nr2gq_mBuBJyCFVFUiq4lubio(this, var2));
      }
   }

   // $FF: synthetic method
   public void lambda$forceImportContacts$5$ContactsController() {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("force import contacts");
      }

      this.performSyncPhoneBook(new HashMap(), true, true, true, true, false, false);
   }

   // $FF: synthetic method
   public void lambda$loadContacts$27$ContactsController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         TLRPC.contacts_Contacts var4 = (TLRPC.contacts_Contacts)var2;
         if (var1 != 0 && var4 instanceof TLRPC.TL_contacts_contactsNotModified) {
            this.contactsLoaded = true;
            if (!this.delayedContactsUpdate.isEmpty() && this.contactsBookLoaded) {
               this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
               this.delayedContactsUpdate.clear();
            }

            UserConfig.getInstance(this.currentAccount).lastContactsSyncTime = (int)(System.currentTimeMillis() / 1000L);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$gXCq3FitJ9fiYzIr_FTcMY_P3T0(this));
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("load contacts don't change");
            }

            return;
         }

         UserConfig.getInstance(this.currentAccount).contactsSavedCount = var4.saved_count;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         this.processLoadedContacts(var4.contacts, var4.users, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$loadPrivacySettings$57$ContactsController(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$3k8Ydvk8Rxka6E1rBHMnjl6kFrM(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadPrivacySettings$59$ContactsController(int var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$EuAKug4zrNU4o4RK27r3kMVoC7I(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$mergePhonebookAndTelegramContacts$39$ContactsController(ArrayList var1, HashMap var2, HashMap var3, ArrayList var4) {
      int var5 = var1.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         TLRPC.TL_contact var7 = (TLRPC.TL_contact)var1.get(var6);
         TLRPC.User var8 = MessagesController.getInstance(this.currentAccount).getUser(var7.user_id);
         if (var8 != null && !TextUtils.isEmpty(var8.phone)) {
            String var12 = var8.phone;
            ContactsController.Contact var13 = (ContactsController.Contact)var2.get(var12.substring(Math.max(0, var12.length() - 7)));
            if (var13 != null) {
               if (var13.user == null) {
                  var13.user = var8;
               }
            } else {
               String var9 = ContactsController.Contact.getLetter(var8.first_name, var8.last_name);
               ArrayList var10 = (ArrayList)var3.get(var9);
               ArrayList var14 = var10;
               if (var10 == null) {
                  var14 = new ArrayList();
                  var3.put(var9, var14);
                  var4.add(var9);
               }

               var14.add(var8);
            }
         }
      }

      Iterator var11 = var3.values().iterator();

      while(var11.hasNext()) {
         Collections.sort((ArrayList)var11.next(), _$$Lambda$ContactsController$433Sx76__fODvK_PjR_GANTzejU.INSTANCE);
      }

      Collections.sort(var4, _$$Lambda$ContactsController$8txvpYjs0528CTiynLTYRW8aKog.INSTANCE);
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$EJa18fQNfNOetnYr7I8VZ_MtAgo(this, var4, var3));
   }

   // $FF: synthetic method
   public void lambda$migratePhoneBookToV7$11$ContactsController(SparseArray var1) {
      if (!this.migratingContacts) {
         this.migratingContacts = true;
         HashMap var2 = new HashMap();
         HashMap var3 = this.readContactsFromPhoneBook();
         HashMap var4 = new HashMap();
         Iterator var9 = var3.entrySet().iterator();

         while(true) {
            boolean var5 = var9.hasNext();
            int var6 = 0;
            ContactsController.Contact var7;
            if (!var5) {
               for(var6 = 0; var6 < var1.size(); ++var6) {
                  var7 = (ContactsController.Contact)var1.valueAt(var6);

                  for(int var8 = 0; var8 < var7.shortPhones.size(); ++var8) {
                     String var10 = (String)var4.get((String)var7.shortPhones.get(var8));
                     if (var10 != null) {
                        var7.key = var10;
                        var2.put(var10, var7);
                        break;
                     }
                  }
               }

               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var11 = new StringBuilder();
                  var11.append("migrated contacts ");
                  var11.append(var2.size());
                  var11.append(" of ");
                  var11.append(var1.size());
                  FileLog.d(var11.toString());
               }

               MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(var2, true, false);
               return;
            }

            for(var7 = (ContactsController.Contact)((Entry)var9.next()).getValue(); var6 < var7.shortPhones.size(); ++var6) {
               var4.put(var7.shortPhones.get(var6), var7.key);
            }
         }
      }
   }

   // $FF: synthetic method
   public void lambda$new$0$ContactsController() {
      try {
         if (this.hasContactsPermission()) {
            ContentResolver var1 = ApplicationLoader.applicationContext.getContentResolver();
            Uri var2 = Contacts.CONTENT_URI;
            ContactsController.MyContentObserver var3 = new ContactsController.MyContentObserver();
            var1.registerContentObserver(var2, true, var3);
         }
      } catch (Throwable var4) {
      }

   }

   // $FF: synthetic method
   public void lambda$null$13$ContactsController(int var1, HashMap var2, boolean var3, boolean var4) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.hasNewContactsToImport, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$null$14$ContactsController(HashMap var1, ArrayList var2, HashMap var3) {
      this.mergePhonebookAndTelegramContacts(var1, var2, var3);
      this.updateUnregisteredContacts();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsImported);
   }

   // $FF: synthetic method
   public void lambda$null$15$ContactsController(HashMap var1, HashMap var2, boolean var3, HashMap var4, ArrayList var5, HashMap var6) {
      this.contactsBookSPhones = var1;
      this.contactsBook = var2;
      this.contactsSyncInProgress = false;
      this.contactsBookLoaded = true;
      if (var3) {
         this.contactsLoaded = true;
      }

      if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
         this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
         this.delayedContactsUpdate.clear();
      }

      MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(var2, false, false);
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$HLpbAIiF9PYZwFaig6jCvfwgV6A(this, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$null$16$ContactsController(HashMap var1, ArrayList var2, HashMap var3) {
      this.mergePhonebookAndTelegramContacts(var1, var2, var3);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsImported);
   }

   // $FF: synthetic method
   public void lambda$null$17$ContactsController() {
      MessagesStorage.getInstance(this.currentAccount).getCachedPhoneBook(true);
   }

   // $FF: synthetic method
   public void lambda$null$18$ContactsController(HashMap var1, HashMap var2, boolean var3, HashMap var4, ArrayList var5, HashMap var6, boolean[] var7) {
      this.contactsBookSPhones = var1;
      this.contactsBook = var2;
      this.contactsSyncInProgress = false;
      this.contactsBookLoaded = true;
      if (var3) {
         this.contactsLoaded = true;
      }

      if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
         this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
         this.delayedContactsUpdate.clear();
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$QCec17nQJOhdVmTlR71H9gJSWro(this, var4, var5, var6));
      if (var7[0]) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$jDhNdq7M1sl26gLErTQhOHpc1l4(this), 300000L);
      }

   }

   // $FF: synthetic method
   public void lambda$null$19$ContactsController(HashMap var1, SparseArray var2, boolean[] var3, HashMap var4, TLRPC.TL_contacts_importContacts var5, int var6, HashMap var7, boolean var8, HashMap var9, ArrayList var10, HashMap var11, TLObject var12, TLRPC.TL_error var13) {
      ++this.completedRequestsCount;
      int var14;
      if (var13 == null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("contacts imported");
         }

         TLRPC.TL_contacts_importedContacts var17 = (TLRPC.TL_contacts_importedContacts)var12;
         if (!var17.retry_contacts.isEmpty()) {
            for(var14 = 0; var14 < var17.retry_contacts.size(); ++var14) {
               var1.remove(var2.get((int)(Long)var17.retry_contacts.get(var14)));
            }

            var3[0] = true;
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("result has retry contacts");
            }
         }

         for(var14 = 0; var14 < var17.popular_invites.size(); ++var14) {
            TLRPC.TL_popularContact var20 = (TLRPC.TL_popularContact)var17.popular_invites.get(var14);
            ContactsController.Contact var18 = (ContactsController.Contact)var4.get(var2.get((int)var20.client_id));
            if (var18 != null) {
               var18.imported = var20.importers;
            }
         }

         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var17.users, (ArrayList)null, true, true);
         ArrayList var15 = new ArrayList();

         for(var14 = 0; var14 < var17.imported.size(); ++var14) {
            TLRPC.TL_contact var19 = new TLRPC.TL_contact();
            var19.user_id = ((TLRPC.TL_importedContact)var17.imported.get(var14)).user_id;
            var15.add(var19);
         }

         this.processLoadedContacts(var15, var17.users, 2);
      } else {
         for(var14 = 0; var14 < var5.contacts.size(); ++var14) {
            var1.remove(var2.get((int)((TLRPC.TL_inputPhoneContact)var5.contacts.get(var14)).client_id));
         }

         var3[0] = true;
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var16 = new StringBuilder();
            var16.append("import contacts error ");
            var16.append(var13.text);
            FileLog.d(var16.toString());
         }
      }

      if (this.completedRequestsCount == var6) {
         if (!var1.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(var1, false, false);
         }

         Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$5_qynEMvCO_Vcwp4tn_AnSTIGhc(this, var7, var4, var8, var9, var10, var11, var3));
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$ContactsController(TLRPC.TL_help_inviteText var1) {
      this.updatingInviteLink = false;
      Editor var2 = MessagesController.getMainSettings(this.currentAccount).edit();
      String var3 = var1.message;
      this.inviteLink = var3;
      var2.putString("invitelink", var3);
      var2.putInt("invitelinktime", (int)(System.currentTimeMillis() / 1000L));
      var2.commit();
   }

   // $FF: synthetic method
   public void lambda$null$20$ContactsController(HashMap var1, ArrayList var2, HashMap var3) {
      this.mergePhonebookAndTelegramContacts(var1, var2, var3);
      this.updateUnregisteredContacts();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsImported);
   }

   // $FF: synthetic method
   public void lambda$null$21$ContactsController(HashMap var1, HashMap var2, boolean var3, HashMap var4, ArrayList var5, HashMap var6) {
      this.contactsBookSPhones = var1;
      this.contactsBook = var2;
      this.contactsSyncInProgress = false;
      this.contactsBookLoaded = true;
      if (var3) {
         this.contactsLoaded = true;
      }

      if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
         this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
         this.delayedContactsUpdate.clear();
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$P0Dtr3FaFqbEIj7kQUrR_yvUd8o(this, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$null$22$ContactsController(HashMap var1, ArrayList var2, HashMap var3) {
      this.mergePhonebookAndTelegramContacts(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$23$ContactsController(HashMap var1, HashMap var2, boolean var3, HashMap var4, ArrayList var5, HashMap var6) {
      this.contactsBookSPhones = var1;
      this.contactsBook = var2;
      this.contactsSyncInProgress = false;
      this.contactsBookLoaded = true;
      if (var3) {
         this.contactsLoaded = true;
      }

      if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded && this.contactsBookLoaded) {
         this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
         this.delayedContactsUpdate.clear();
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$3JHpI_WMQtsFtzkjJ3kfvTUf7ro(this, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$null$26$ContactsController() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$null$31$ContactsController(ArrayList param1, ConcurrentHashMap param2, HashMap param3, HashMap param4, ArrayList param5, ArrayList param6, int param7, boolean param8) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$null$32$ContactsController(HashMap var1, HashMap var2) {
      this.contactsByPhone = var1;
      this.contactsByShortPhone = var2;
   }

   // $FF: synthetic method
   public void lambda$null$33$ContactsController(HashMap var1, HashMap var2) {
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$EHQlX5PriiljkxmdW3HZOsijhuY(this, var1, var2));
      if (!this.contactsSyncInProgress) {
         this.contactsSyncInProgress = true;
         MessagesStorage.getInstance(this.currentAccount).getCachedPhoneBook(false);
      }
   }

   // $FF: synthetic method
   public void lambda$null$34$ContactsController(int var1, ArrayList var2, SparseArray var3, ArrayList var4, boolean var5) {
      ArrayList var6 = var2;
      SparseArray var7 = var3;
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("done loading contacts");
      }

      if (var1 == 1 && (var2.isEmpty() || Math.abs(System.currentTimeMillis() / 1000L - (long)UserConfig.getInstance(this.currentAccount).lastContactsSyncTime) >= 86400L)) {
         this.loadContacts(false, this.getContactsHash(var2));
         if (var2.isEmpty()) {
            return;
         }
      }

      if (var1 == 0) {
         UserConfig.getInstance(this.currentAccount).lastContactsSyncTime = (int)(System.currentTimeMillis() / 1000L);
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
      }

      int var8;
      for(var8 = 0; var8 < var2.size(); ++var8) {
         TLRPC.TL_contact var9 = (TLRPC.TL_contact)var6.get(var8);
         if (var7.get(var9.user_id) == null && var9.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.loadContacts(false, 0);
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("contacts are broken, load from server");
            }

            return;
         }
      }

      if (var1 != 1) {
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var4, (ArrayList)null, true, true);
         MessagesStorage var19 = MessagesStorage.getInstance(this.currentAccount);
         boolean var10;
         if (var1 != 2) {
            var10 = true;
         } else {
            var10 = false;
         }

         var19.putContacts(var6, var10);
      }

      Collections.sort(var6, new _$$Lambda$ContactsController$hjm8gMZUsX8pFMsciXhWnMP28rs(var7));
      ConcurrentHashMap var11 = new ConcurrentHashMap(20, 1.0F, 2);
      HashMap var12 = new HashMap();
      HashMap var13 = new HashMap();
      ArrayList var14 = new ArrayList();
      ArrayList var15 = new ArrayList();
      HashMap var21;
      HashMap var22;
      if (!this.contactsBookLoaded) {
         var22 = new HashMap();
         var21 = new HashMap();
      } else {
         var22 = null;
         var21 = var22;
      }

      for(var8 = 0; var8 < var2.size(); ++var8) {
         TLRPC.TL_contact var16 = (TLRPC.TL_contact)var2.get(var8);
         TLRPC.User var17 = (TLRPC.User)var3.get(var16.user_id);
         if (var17 != null) {
            var11.put(var16.user_id, var16);
            String var20;
            if (var22 != null && !TextUtils.isEmpty(var17.phone)) {
               var22.put(var17.phone, var16);
               var20 = var17.phone;
               var21.put(var20.substring(Math.max(0, var20.length() - 7)), var16);
            }

            String var23 = UserObject.getFirstName(var17);
            var20 = var23;
            if (var23.length() > 1) {
               var20 = var23.substring(0, 1);
            }

            if (var20.length() == 0) {
               var20 = "#";
            } else {
               var20 = var20.toUpperCase();
            }

            var23 = (String)this.sectionsToReplace.get(var20);
            if (var23 != null) {
               var20 = var23;
            }

            ArrayList var18 = (ArrayList)var12.get(var20);
            ArrayList var24 = var18;
            if (var18 == null) {
               var24 = new ArrayList();
               var12.put(var20, var24);
               var14.add(var20);
            }

            var24.add(var16);
            if (var17.mutual_contact) {
               var18 = (ArrayList)var13.get(var20);
               var24 = var18;
               if (var18 == null) {
                  var24 = new ArrayList();
                  var13.put(var20, var24);
                  var15.add(var20);
               }

               var24.add(var16);
            }
         }
      }

      Collections.sort(var14, _$$Lambda$ContactsController$Io20DZyV84DSau97gyyKYypoUA8.INSTANCE);
      Collections.sort(var15, _$$Lambda$ContactsController$DcIoZeDGqSMeB8gKr5g3ew0Ok2Y.INSTANCE);
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$1CJ23H1PXk1UitBXEZ5w9tuujVY(this, var2, var11, var12, var13, var14, var15, var1, var5));
      if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded && this.contactsBookLoaded) {
         this.applyContactsUpdates(this.delayedContactsUpdate, (ConcurrentHashMap)null, (ArrayList)null, (ArrayList)null);
         this.delayedContactsUpdate.clear();
      }

      if (var22 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$VBtQ6OXrlCuEUdHYI1TREe5kYV4(this, var22, var21));
      } else {
         this.contactsLoaded = true;
      }

   }

   // $FF: synthetic method
   public void lambda$null$38$ContactsController(ArrayList var1, HashMap var2) {
      this.phoneBookSectionsArray = var1;
      this.phoneBookSectionsDict = var2;
   }

   // $FF: synthetic method
   public void lambda$null$48$ContactsController(TLRPC.User var1) {
      this.addContactToPhoneBook(var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$49$ContactsController(TLRPC.TL_contacts_importedContacts var1) {
      Iterator var4 = var1.users.iterator();

      while(var4.hasNext()) {
         TLRPC.User var2 = (TLRPC.User)var4.next();
         MessagesController.getInstance(this.currentAccount).putUser(var2, false);
         if (this.contactsDict.get(var2.id) == null) {
            TLRPC.TL_contact var3 = new TLRPC.TL_contact();
            var3.user_id = var2.id;
            this.contacts.add(var3);
            this.contactsDict.put(var3.user_id, var3);
         }
      }

      this.buildContactsSectionsArrays(true);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
   }

   // $FF: synthetic method
   public void lambda$null$51$ContactsController(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.deleteContactFromPhoneBook(((TLRPC.User)var2.next()).id);
      }

   }

   // $FF: synthetic method
   public void lambda$null$52$ContactsController(ArrayList var1) {
      Iterator var2 = var1.iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         TLRPC.User var4 = (TLRPC.User)var2.next();
         TLRPC.TL_contact var5 = (TLRPC.TL_contact)this.contactsDict.get(var4.id);
         if (var5 != null) {
            this.contacts.remove(var5);
            this.contactsDict.remove(var4.id);
            var3 = true;
         }
      }

      if (var3) {
         this.buildContactsSectionsArrays(false);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
   }

   // $FF: synthetic method
   public void lambda$null$54$ContactsController(Editor var1, TLObject var2) {
      var1.remove("needGetStatuses").commit();
      TLRPC.Vector var7 = (TLRPC.Vector)var2;
      if (!var7.objects.isEmpty()) {
         ArrayList var6 = new ArrayList();
         Iterator var8 = var7.objects.iterator();

         while(var8.hasNext()) {
            Object var3 = var8.next();
            TLRPC.TL_user var4 = new TLRPC.TL_user();
            TLRPC.TL_contactStatus var9 = (TLRPC.TL_contactStatus)var3;
            if (var9 != null) {
               TLRPC.UserStatus var5 = var9.status;
               if (var5 instanceof TLRPC.TL_userStatusRecently) {
                  var5.expires = -100;
               } else if (var5 instanceof TLRPC.TL_userStatusLastWeek) {
                  var5.expires = -101;
               } else if (var5 instanceof TLRPC.TL_userStatusLastMonth) {
                  var5.expires = -102;
               }

               TLRPC.User var10 = MessagesController.getInstance(this.currentAccount).getUser(var9.user_id);
               if (var10 != null) {
                  var10.status = var9.status;
               }

               var4.status = var9.status;
               var6.add(var4);
            }
         }

         MessagesStorage.getInstance(this.currentAccount).updateUsers(var6, true, true, true);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 4);
   }

   // $FF: synthetic method
   public void lambda$null$56$ContactsController(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         this.deleteAccountTTL = ((TLRPC.TL_accountDaysTTL)var2).days;
         this.loadingDeleteInfo = 2;
      } else {
         this.loadingDeleteInfo = 0;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated);
   }

   // $FF: synthetic method
   public void lambda$null$58$ContactsController(TLRPC.TL_error var1, TLObject var2, int var3) {
      if (var1 == null) {
         TLRPC.TL_account_privacyRules var4 = (TLRPC.TL_account_privacyRules)var2;
         MessagesController.getInstance(this.currentAccount).putUsers(var4.users, false);
         MessagesController.getInstance(this.currentAccount).putChats(var4.chats, false);
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     if (var3 != 4) {
                        if (var3 != 5) {
                           this.phonePrivacyRules = var4.rules;
                        } else {
                           this.forwardsPrivacyRules = var4.rules;
                        }
                     } else {
                        this.profilePhotoPrivacyRules = var4.rules;
                     }
                  } else {
                     this.p2pPrivacyRules = var4.rules;
                  }
               } else {
                  this.callPrivacyRules = var4.rules;
               }
            } else {
               this.groupPrivacyRules = var4.rules;
            }
         } else {
            this.lastseenPrivacyRules = var4.rules;
         }

         this.loadingPrivacyInfo[var3] = 2;
      } else {
         this.loadingPrivacyInfo[var3] = 0;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated);
   }

   // $FF: synthetic method
   public void lambda$null$7$ContactsController(Runnable var1) {
      AccountManager var2 = AccountManager.get(ApplicationLoader.applicationContext);

      Account var5;
      label59: {
         boolean var10001;
         Account[] var3;
         try {
            var3 = var2.getAccountsByType("org.telegram.messenger");
            this.systemAccount = null;
         } catch (Throwable var14) {
            var10001 = false;
            break label59;
         }

         int var4 = 0;

         label50:
         while(true) {
            try {
               if (var4 >= var3.length) {
                  break;
               }
            } catch (Throwable var12) {
               var10001 = false;
               break;
            }

            var5 = var3[var4];

            for(int var6 = 0; var6 < 3; ++var6) {
               TLRPC.User var7;
               try {
                  var7 = UserConfig.getInstance(var6).getCurrentUser();
               } catch (Throwable var11) {
                  var10001 = false;
                  break label50;
               }

               if (var7 != null) {
                  try {
                     String var8 = var5.name;
                     StringBuilder var9 = new StringBuilder();
                     var9.append("");
                     var9.append(var7.id);
                     if (var8.equals(var9.toString())) {
                        var2.removeAccount(var5, (AccountManagerCallback)null, (Handler)null);
                        break;
                     }
                  } catch (Throwable var13) {
                     var10001 = false;
                     break label50;
                  }
               }
            }

            ++var4;
         }
      }

      try {
         StringBuilder var15 = new StringBuilder();
         var15.append("");
         var15.append(UserConfig.getInstance(this.currentAccount).getClientUserId());
         var5 = new Account(var15.toString(), "org.telegram.messenger");
         this.systemAccount = var5;
         var2.addAccountExplicitly(this.systemAccount, "", (Bundle)null);
      } catch (Exception var10) {
      }

      MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(new HashMap(), false, true);
      MessagesStorage.getInstance(this.currentAccount).putContacts(new ArrayList(), true);
      this.phoneBookContacts.clear();
      this.contacts.clear();
      this.contactsDict.clear();
      this.usersSectionsDict.clear();
      this.usersMutualSectionsDict.clear();
      this.sortedUsersSectionsArray.clear();
      this.phoneBookSectionsDict.clear();
      this.phoneBookSectionsArray.clear();
      this.delayedContactsUpdate.clear();
      this.sortedUsersMutualSectionsArray.clear();
      this.contactsByPhone.clear();
      this.contactsByShortPhone.clear();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoad);
      this.loadContacts(false, 0);
      var1.run();
   }

   // $FF: synthetic method
   public void lambda$performSyncPhoneBook$24$ContactsController(HashMap var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      ContactsController var8 = this;
      HashMap var9 = new HashMap();
      Iterator var10 = var1.entrySet().iterator();

      while(true) {
         boolean var11 = var10.hasNext();
         int var12 = 0;
         if (!var11) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("start read contacts from phone");
            }

            if (!var2) {
               this.checkContactsInternal();
            }

            HashMap var14 = this.readContactsFromPhoneBook();
            HashMap var15 = new HashMap();
            Object var42 = new HashMap();
            ArrayList var16 = new ArrayList();

            ContactsController.Contact var18;
            int var19;
            String var20;
            String var46;
            ArrayList var47;
            for(Iterator var17 = var14.entrySet().iterator(); var17.hasNext(); var47.add(var18)) {
               var18 = (ContactsController.Contact)((Entry)var17.next()).getValue();
               var19 = var18.shortPhones.size();

               for(var12 = 0; var12 < var19; ++var12) {
                  var46 = (String)var18.shortPhones.get(var12);
                  ((HashMap)var42).put(var46.substring(Math.max(0, var46.length() - 7)), var18);
               }

               var20 = var18.getLetter();
               ArrayList var21 = (ArrayList)var15.get(var20);
               var47 = var21;
               if (var21 == null) {
                  var47 = new ArrayList();
                  var15.put(var20, var47);
                  var16.add(var20);
               }
            }

            HashMap var22 = new HashMap();
            int var23 = var1.size();
            ArrayList var24 = new ArrayList();
            var11 = var1.isEmpty();
            String var53 = "";
            int var25;
            String var41;
            String var49;
            Object var50;
            TLRPC.TL_inputPhoneContact var52;
            Iterator var61;
            if (!var11) {
               var61 = var14.entrySet().iterator();
               var19 = 0;
               var25 = 0;
               Object var48 = var15;
               var49 = var53;
               var12 = var23;
               HashMap var36 = var9;

               while(true) {
                  ContactsController var26 = this;
                  var23 = 0;
                  if (!var61.hasNext()) {
                     if (!var4 && var1.isEmpty() && var24.isEmpty() && var12 == var14.size()) {
                        if (BuildVars.LOGS_ENABLED) {
                           FileLog.d("contacts not changed!");
                        }

                        return;
                     }

                     var23 = var12;
                     if (var3 && !var1.isEmpty() && !var14.isEmpty() && var24.isEmpty()) {
                        MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(var14, false, false);
                     }

                     var12 = var25;
                     var50 = var48;
                     var47 = var16;
                     break;
                  }

                  ContactsController.Contact var27;
                  String var51;
                  label398: {
                     Entry var39 = (Entry)var61.next();
                     var51 = (String)var39.getKey();
                     var27 = (ContactsController.Contact)var39.getValue();
                     ContactsController.Contact var40 = (ContactsController.Contact)var1.get(var51);
                     if (var40 == null) {
                        while(var23 < var27.shortPhones.size()) {
                           var18 = (ContactsController.Contact)var36.get(var27.shortPhones.get(var23));
                           if (var18 != null) {
                              var51 = var18.key;
                              break label398;
                           }

                           ++var23;
                        }
                     }

                     var18 = var40;
                  }

                  if (var18 != null) {
                     var27.imported = var18.imported;
                  }

                  boolean var65;
                  if (var18 == null || (TextUtils.isEmpty(var27.first_name) || var18.first_name.equals(var27.first_name)) && (TextUtils.isEmpty(var27.last_name) || var18.last_name.equals(var27.last_name))) {
                     var65 = false;
                  } else {
                     var65 = true;
                  }

                  int var28;
                  long var32;
                  int var68;
                  if (var18 != null && !var65) {
                     var28 = 0;
                     var46 = var49;

                     while(true) {
                        if (var28 >= var27.phones.size()) {
                           var49 = var46;
                           var47 = var16;
                           var50 = var48;
                           if (var18.phones.isEmpty()) {
                              var1.remove(var51);
                           }

                           var23 = var19;
                           var68 = var25;
                           break;
                        }

                        boolean var31;
                        label271: {
                           label270: {
                              var20 = (String)var27.shortPhones.get(var28);
                              var49 = var20.substring(Math.max(0, var20.length() - 7));
                              var22.put(var20, var27);
                              var68 = var18.shortPhones.indexOf(var20);
                              if (var3) {
                                 TLRPC.TL_contact var69 = (TLRPC.TL_contact)var26.contactsByPhone.get(var20);
                                 if (var69 != null) {
                                    TLRPC.User var70 = MessagesController.getInstance(var26.currentAccount).getUser(var69.user_id);
                                    var23 = var25;
                                    if (var70 != null) {
                                       ++var25;
                                       var23 = var25;
                                       if (TextUtils.isEmpty(var70.first_name)) {
                                          var23 = var25;
                                          if (TextUtils.isEmpty(var70.last_name)) {
                                             if (!TextUtils.isEmpty(var27.first_name)) {
                                                break label270;
                                             }

                                             var23 = var25;
                                             if (!TextUtils.isEmpty(var27.last_name)) {
                                                break label270;
                                             }
                                          }
                                       }
                                    }
                                 } else {
                                    var23 = var25;
                                    if (var26.contactsByShortPhone.containsKey(var49)) {
                                       var23 = var25 + 1;
                                    }
                                 }
                              } else {
                                 var23 = var25;
                              }

                              var31 = false;
                              var25 = var23;
                              var23 = var68;
                              break label271;
                           }

                           var31 = true;
                           var23 = -1;
                        }

                        if (var23 == -1) {
                           var68 = var19;
                           var23 = var25;
                           if (var3) {
                              label394: {
                                 var68 = var19;
                                 var23 = var25;
                                 if (!var31) {
                                    TLRPC.TL_contact var60 = (TLRPC.TL_contact)var26.contactsByPhone.get(var20);
                                    if (var60 != null) {
                                       TLRPC.User var63 = MessagesController.getInstance(var26.currentAccount).getUser(var60.user_id);
                                       if (var63 != null) {
                                          ++var25;
                                          var49 = var63.first_name;
                                          if (var49 == null) {
                                             var49 = var46;
                                          }

                                          var20 = var63.last_name;
                                          if (var20 == null) {
                                             var20 = var46;
                                          }

                                          if (var49.equals(var27.first_name)) {
                                             var68 = var19;
                                             var23 = var25;
                                             if (var20.equals(var27.last_name)) {
                                                break label394;
                                             }
                                          }

                                          var68 = var19;
                                          var23 = var25;
                                          if (TextUtils.isEmpty(var27.first_name)) {
                                             var68 = var19;
                                             var23 = var25;
                                             if (TextUtils.isEmpty(var27.last_name)) {
                                                var68 = var19;
                                                var23 = var25;
                                                break label394;
                                             }
                                          }
                                       } else {
                                          var68 = var19 + 1;
                                          var23 = var25;
                                       }
                                    } else {
                                       var68 = var19;
                                       var23 = var25;
                                       if (var26.contactsByShortPhone.containsKey(var49)) {
                                          var23 = var25 + 1;
                                          var68 = var19;
                                       }
                                    }
                                 }

                                 var52 = new TLRPC.TL_inputPhoneContact();
                                 var52.client_id = (long)var27.contact_id;
                                 var32 = var52.client_id;
                                 var52.client_id = (long)var28 << 32 | var32;
                                 var52.first_name = var27.first_name;
                                 var52.last_name = var27.last_name;
                                 var52.phone = (String)var27.phones.get(var28);
                                 var24.add(var52);
                              }
                           }
                        } else {
                           var27.phoneDeleted.set(var28, var18.phoneDeleted.get(var23));
                           var18.phones.remove(var23);
                           var18.shortPhones.remove(var23);
                           var18.phoneDeleted.remove(var23);
                           var18.phoneTypes.remove(var23);
                           var23 = var25;
                           var68 = var19;
                        }

                        ++var28;
                        var19 = var68;
                        var25 = var23;
                     }
                  } else {
                     var41 = var49;
                     Object var66 = var42;
                     Object var30 = var48;
                     var28 = 0;
                     boolean var29 = var65;

                     while(true) {
                        if (var28 >= var27.phones.size()) {
                           var28 = var12;
                           var23 = var19;
                           var12 = var12;
                           var68 = var25;
                           var42 = var42;
                           var49 = var49;
                           var50 = var48;
                           var47 = var16;
                           if (var18 != null) {
                              var1.remove(var51);
                              var47 = var16;
                              var50 = var30;
                              var49 = var41;
                              var42 = var66;
                              var68 = var25;
                              var12 = var28;
                              var23 = var19;
                           }
                           break;
                        }

                        label311: {
                           label386: {
                              var42 = (String)var27.shortPhones.get(var28);
                              ((String)var42).substring(Math.max(0, ((String)var42).length() - 7));
                              var22.put(var42, var27);
                              if (var18 != null) {
                                 var23 = var18.shortPhones.indexOf(var42);
                                 if (var23 != -1) {
                                    var48 = (Integer)var18.phoneDeleted.get(var23);
                                    var27.phoneDeleted.set(var28, var48);
                                    if (var48 == 1) {
                                       var23 = var25;
                                       break label386;
                                    }
                                 }
                              }

                              var23 = var25;
                              if (var3) {
                                 label395: {
                                    var23 = var19;
                                    if (!var29) {
                                       if (this.contactsByPhone.containsKey(var42)) {
                                          var23 = var25 + 1;
                                          break label395;
                                       }

                                       var23 = var19 + 1;
                                    }

                                    var48 = new TLRPC.TL_inputPhoneContact();
                                    ((TLRPC.TL_inputPhoneContact)var48).client_id = (long)var27.contact_id;
                                    var32 = ((TLRPC.TL_inputPhoneContact)var48).client_id;
                                    ((TLRPC.TL_inputPhoneContact)var48).client_id = (long)var28 << 32 | var32;
                                    ((TLRPC.TL_inputPhoneContact)var48).first_name = var27.first_name;
                                    ((TLRPC.TL_inputPhoneContact)var48).last_name = var27.last_name;
                                    ((TLRPC.TL_inputPhoneContact)var48).phone = (String)var27.phones.get(var28);
                                    var24.add(var48);
                                    var19 = var23;
                                    break label311;
                                 }
                              }
                           }

                           var25 = var23;
                        }

                        ++var28;
                     }
                  }

                  var19 = var23;
                  var16 = var47;
                  var48 = var50;
                  var25 = var68;
               }
            } else {
               var47 = var16;
               var50 = var15;
               if (var3) {
                  var61 = var14.entrySet().iterator();
                  var12 = 0;

                  while(var61.hasNext()) {
                     Entry var55 = (Entry)var61.next();
                     var18 = (ContactsController.Contact)var55.getValue();
                     var49 = (String)var55.getKey();
                     var25 = 0;
                     var19 = var12;

                     while(true) {
                        var12 = var19;
                        if (var25 >= var18.phones.size()) {
                           break;
                        }

                        label400: {
                           var12 = var19;
                           if (!var5) {
                              var41 = (String)var18.shortPhones.get(var25);
                              var49 = var41.substring(Math.max(0, var41.length() - 7));
                              TLRPC.TL_contact var44 = (TLRPC.TL_contact)var8.contactsByPhone.get(var41);
                              if (var44 != null) {
                                 TLRPC.User var45 = MessagesController.getInstance(var8.currentAccount).getUser(var44.user_id);
                                 var12 = var19;
                                 if (var45 != null) {
                                    label404: {
                                       ++var19;
                                       var49 = var45.first_name;
                                       if (var49 == null) {
                                          var49 = "";
                                       }

                                       var41 = var45.last_name;
                                       if (var41 == null) {
                                          var41 = "";
                                       }

                                       if (!var49.equals(var18.first_name) || !var41.equals(var18.last_name)) {
                                          var12 = var19;
                                          if (!TextUtils.isEmpty(var18.first_name)) {
                                             break label404;
                                          }

                                          var12 = var19;
                                          if (!TextUtils.isEmpty(var18.last_name)) {
                                             break label404;
                                          }
                                       }

                                       var12 = var19;
                                       break label400;
                                    }
                                 }
                              } else {
                                 var12 = var19;
                                 if (var8.contactsByShortPhone.containsKey(var49)) {
                                    var12 = var19 + 1;
                                 }
                              }
                           }

                           var52 = new TLRPC.TL_inputPhoneContact();
                           var52.client_id = (long)var18.contact_id;
                           var52.client_id |= (long)var25 << 32;
                           var52.first_name = var18.first_name;
                           var52.last_name = var18.last_name;
                           var52.phone = (String)var18.phones.get(var25);
                           var24.add(var52);
                        }

                        ++var25;
                        var19 = var12;
                     }
                  }

                  var19 = 0;
               } else {
                  var19 = 0;
                  var12 = 0;
               }
            }

            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("done processing contacts");
            }

            if (var3) {
               if (!var24.isEmpty()) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("start import contacts");
                  }

                  byte var67;
                  label223: {
                     if (var6 && var19 != 0) {
                        if (var19 >= 30) {
                           var67 = 1;
                           break label223;
                        }

                        if (var4 && var23 == 0 && this.contactsByPhone.size() - var12 > this.contactsByPhone.size() / 3 * 2) {
                           var67 = 2;
                           break label223;
                        }
                     }

                     var67 = 0;
                  }

                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var59 = new StringBuilder();
                     var59.append("new phone book contacts ");
                     var59.append(var19);
                     var59.append(" serverContactsInPhonebook ");
                     var59.append(var12);
                     var59.append(" totalContacts ");
                     var59.append(this.contactsByPhone.size());
                     FileLog.d(var59.toString());
                  }

                  if (var67 != 0) {
                     AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$1bnNRpKN4G2RMftCjRi6zZdnM_I(this, var67, var1, var4, var2));
                     return;
                  }

                  if (var7) {
                     Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$GNntUmuXLL6W1VYySyoHwi7w0_8(this, var22, var14, var4, (HashMap)var50, var47, (HashMap)var42));
                     return;
                  }

                  boolean[] var64 = new boolean[]{false};
                  var1 = new HashMap(var14);
                  SparseArray var62 = new SparseArray();
                  Iterator var56 = var1.entrySet().iterator();

                  while(var56.hasNext()) {
                     ContactsController.Contact var37 = (ContactsController.Contact)((Entry)var56.next()).getValue();
                     var62.put(var37.contact_id, var37.key);
                  }

                  this.completedRequestsCount = 0;
                  double var34 = (double)var24.size();
                  Double.isNaN(var34);
                  var12 = (int)Math.ceil(var34 / 500.0D);
                  var19 = 0;
                  var9 = var22;

                  for(ArrayList var38 = var24; var19 < var12; ++var19) {
                     TLRPC.TL_contacts_importContacts var58 = new TLRPC.TL_contacts_importContacts();
                     var25 = var19 * 500;
                     var58.contacts = new ArrayList(var38.subList(var25, Math.min(var25 + 500, var38.size())));
                     ConnectionsManager.getInstance(this.currentAccount).sendRequest(var58, new _$$Lambda$ContactsController$nCB3Aueb5LjSTAeo_ZnP4JYneR4(this, var1, var62, var64, var14, var58, var12, var9, var4, (HashMap)var50, var47, (HashMap)var42), 6);
                  }
               } else {
                  Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$_jMNdC01NspFJ8mldMYkB_XJE88(this, var22, var14, var4, (HashMap)var50, var47, (HashMap)var42));
               }
            } else {
               Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$VZyJqVQUXu54Ctl7i8Dn_JZ0oQE(this, var22, var14, var4, (HashMap)var50, var47, (HashMap)var42));
               if (!var14.isEmpty()) {
                  MessagesStorage.getInstance(this.currentAccount).putCachedPhoneBook(var14, false, false);
               }
            }

            return;
         }

         for(ContactsController.Contact var13 = (ContactsController.Contact)((Entry)var10.next()).getValue(); var12 < var13.shortPhones.size(); ++var12) {
            var9.put(var13.shortPhones.get(var12), var13);
         }
      }
   }

   // $FF: synthetic method
   public void lambda$performWriteContactsToPhoneBook$43$ContactsController(ArrayList var1) {
      this.performWriteContactsToPhoneBookInternal(var1);
   }

   // $FF: synthetic method
   public void lambda$processLoadedContacts$35$ContactsController(ArrayList var1, int var2, ArrayList var3) {
      MessagesController var4 = MessagesController.getInstance(this.currentAccount);
      byte var5 = 0;
      boolean var6;
      if (var2 == 1) {
         var6 = true;
      } else {
         var6 = false;
      }

      var4.putUsers(var1, var6);
      SparseArray var10 = new SparseArray();
      var6 = var3.isEmpty();
      int var7 = var5;
      if (!this.contacts.isEmpty()) {
         int var9;
         for(var7 = 0; var7 < var3.size(); var7 = var9 + 1) {
            TLRPC.TL_contact var8 = (TLRPC.TL_contact)var3.get(var7);
            var9 = var7;
            if (this.contactsDict.get(var8.user_id) != null) {
               var3.remove(var7);
               var9 = var7 - 1;
            }
         }

         var3.addAll(this.contacts);
         var7 = var5;
      }

      for(; var7 < var3.size(); ++var7) {
         TLRPC.User var11 = MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)var3.get(var7)).user_id);
         if (var11 != null) {
            var10.put(var11.id, var11);
         }
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$FhL_9umLAIuYNOniVSTFnjIHfVA(this, var2, var3, var10, var1, var6));
   }

   // $FF: synthetic method
   public void lambda$readContacts$10$ContactsController() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$reloadContactsStatuses$55$ContactsController(Editor var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$gWEMQfmNPp3nFPxuDjbs9cZtdo4(this, var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$syncPhoneBookByAlert$6$ContactsController(HashMap var1, boolean var2, boolean var3, boolean var4) {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("sync contacts by alert");
      }

      this.performSyncPhoneBook(var1, true, var2, var3, false, false, var4);
   }

   public void loadContacts(boolean param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void loadPrivacySettings() {
      if (this.loadingDeleteInfo == 0) {
         this.loadingDeleteInfo = 1;
         TLRPC.TL_account_getAccountTTL var1 = new TLRPC.TL_account_getAccountTTL();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ(this));
      }

      int var2 = 0;

      while(true) {
         int[] var3 = this.loadingPrivacyInfo;
         if (var2 >= var3.length) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated);
            return;
         }

         if (var3[var2] == 0) {
            var3[var2] = 1;
            TLRPC.TL_account_getPrivacy var4 = new TLRPC.TL_account_getPrivacy();
            if (var2 != 0) {
               if (var2 != 1) {
                  if (var2 != 2) {
                     if (var2 != 3) {
                        if (var2 != 4) {
                           if (var2 != 5) {
                              var4.key = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
                           } else {
                              var4.key = new TLRPC.TL_inputPrivacyKeyForwards();
                           }
                        } else {
                           var4.key = new TLRPC.TL_inputPrivacyKeyProfilePhoto();
                        }
                     } else {
                        var4.key = new TLRPC.TL_inputPrivacyKeyPhoneP2P();
                     }
                  } else {
                     var4.key = new TLRPC.TL_inputPrivacyKeyPhoneCall();
                  }
               } else {
                  var4.key = new TLRPC.TL_inputPrivacyKeyChatInvite();
               }
            } else {
               var4.key = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$ContactsController$AbTZFLT5AeZ8Xev0g7z_25zx9YM(this, var2));
         }

         ++var2;
      }
   }

   protected void markAsContacted(String var1) {
      if (var1 != null) {
         Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$ZSZ9C_4_dtPH1zqggEDwmuwv69A(var1));
      }
   }

   protected void migratePhoneBookToV7(SparseArray var1) {
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ(this, var1));
   }

   protected void performSyncPhoneBook(HashMap var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (var3 || this.contactsBookLoaded) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$bP_Wp_ENz0cZTKPV19hn5HbOllw(this, var1, var4, var2, var3, var5, var6, var7));
      }
   }

   public void processContactsUpdates(ArrayList var1, ConcurrentHashMap var2) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Integer var6 = (Integer)var5.next();
         int var8;
         if (var6 > 0) {
            TLRPC.TL_contact var7 = new TLRPC.TL_contact();
            var7.user_id = var6;
            var3.add(var7);
            if (!this.delayedContactsUpdate.isEmpty()) {
               var8 = this.delayedContactsUpdate.indexOf(-var6);
               if (var8 != -1) {
                  this.delayedContactsUpdate.remove(var8);
               }
            }
         } else if (var6 < 0) {
            var4.add(-var6);
            if (!this.delayedContactsUpdate.isEmpty()) {
               var8 = this.delayedContactsUpdate.indexOf(-var6);
               if (var8 != -1) {
                  this.delayedContactsUpdate.remove(var8);
               }
            }
         }
      }

      if (!var4.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).deleteContacts(var4);
      }

      if (!var3.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).putContacts(var3, false);
      }

      if (this.contactsLoaded && this.contactsBookLoaded) {
         this.applyContactsUpdates(var1, var2, var3, var4);
      } else {
         this.delayedContactsUpdate.addAll(var1);
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var9 = new StringBuilder();
            var9.append("delay update - contacts add = ");
            var9.append(var3.size());
            var9.append(" delete = ");
            var9.append(var4.size());
            FileLog.d(var9.toString());
         }
      }

   }

   public void processLoadedContacts(ArrayList var1, ArrayList var2, int var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$EZvJWephnINCisVyT9aioUlUdEQ(this, var2, var3, var1));
   }

   public void readContacts() {
      Object var1 = this.loadContactsSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.loadingContacts) {
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         try {
            this.loadingContacts = true;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$DgnS7Gvt4et5oNJCSlLnfIbm4Ag(this));
         return;
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void reloadContactsStatuses() {
      this.saveContactsLoadTime();
      MessagesController.getInstance(this.currentAccount).clearFullUsers();
      Editor var1 = MessagesController.getMainSettings(this.currentAccount).edit();
      var1.putBoolean("needGetStatuses", true).commit();
      TLRPC.TL_contacts_getStatuses var2 = new TLRPC.TL_contacts_getStatuses();
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$ContactsController$h5vCC_HpjKgEFkNX9o79KFdTfVk(this, var1));
   }

   public void resetImportedContacts() {
      TLRPC.TL_contacts_resetSaved var1 = new TLRPC.TL_contacts_resetSaved();
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, _$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM.INSTANCE);
   }

   public void setDeleteAccountTTL(int var1) {
      this.deleteAccountTTL = var1;
   }

   public void setPrivacyRules(ArrayList var1, int var2) {
      switch(var2) {
      case 0:
         this.lastseenPrivacyRules = var1;
         break;
      case 1:
         this.groupPrivacyRules = var1;
         break;
      case 2:
         this.callPrivacyRules = var1;
         break;
      case 3:
         this.p2pPrivacyRules = var1;
         break;
      case 4:
         this.profilePhotoPrivacyRules = var1;
         break;
      case 5:
         this.forwardsPrivacyRules = var1;
         break;
      case 6:
         this.phonePrivacyRules = var1;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated);
      this.reloadContactsStatuses();
   }

   public void syncPhoneBookByAlert(HashMap var1, boolean var2, boolean var3, boolean var4) {
      Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$zqe6opQgPyDWpCXT4MV8TEtpNRg(this, var1, var2, var3, var4));
   }

   public static class Contact {
      public int contact_id;
      public String first_name;
      public int imported;
      public boolean isGoodProvider;
      public String key;
      public String last_name;
      public boolean namesFilled;
      public ArrayList phoneDeleted = new ArrayList(4);
      public ArrayList phoneTypes = new ArrayList(4);
      public ArrayList phones = new ArrayList(4);
      public String provider;
      public ArrayList shortPhones = new ArrayList(4);
      public TLRPC.User user;

      public static String getLetter(String var0, String var1) {
         if (!TextUtils.isEmpty(var0)) {
            return var0.substring(0, 1);
         } else {
            return !TextUtils.isEmpty(var1) ? var1.substring(0, 1) : "#";
         }
      }

      public String getLetter() {
         return getLetter(this.first_name, this.last_name);
      }
   }

   private class MyContentObserver extends ContentObserver {
      private Runnable checkRunnable;

      public MyContentObserver() {
         super((Handler)null);
         this.checkRunnable = _$$Lambda$ContactsController$MyContentObserver$VmhFqLMqh0tD4jEQWkPIR_W56Bc.INSTANCE;
      }

      // $FF: synthetic method
      static void lambda$new$0() {
         for(int var0 = 0; var0 < 3; ++var0) {
            if (UserConfig.getInstance(var0).isClientActivated()) {
               ConnectionsManager.getInstance(var0).resumeNetworkMaybe();
               ContactsController.getInstance(var0).checkContacts();
            }
         }

      }

      public boolean deliverSelfNotifications() {
         return false;
      }

      public void onChange(boolean var1) {
         super.onChange(var1);
         Object var2 = ContactsController.this.observerLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label137: {
            try {
               if (ContactsController.this.ignoreChanges) {
                  return;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label137;
            }

            try {
               ;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label137;
            }

            Utilities.globalQueue.cancelRunnable(this.checkRunnable);
            Utilities.globalQueue.postRunnable(this.checkRunnable, 500L);
            return;
         }

         while(true) {
            Throwable var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
