package org.telegram.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;

class PhonebookShareActivity$5 implements OnClickListener {
   // $FF: synthetic field
   final PhonebookShareActivity this$0;

   PhonebookShareActivity$5(PhonebookShareActivity var1) {
      this.this$0 = var1;
   }

   private void fillRowWithType(String var1, ContentValues var2) {
      boolean var3 = var1.startsWith("X-");
      Integer var4 = 0;
      if (var3) {
         var2.put("data2", var4);
         var2.put("data3", var1.substring(2));
      } else if ("PREF".equalsIgnoreCase(var1)) {
         var2.put("data2", 12);
      } else if ("HOME".equalsIgnoreCase(var1)) {
         var2.put("data2", 1);
      } else if (!"MOBILE".equalsIgnoreCase(var1) && !"CELL".equalsIgnoreCase(var1)) {
         if ("OTHER".equalsIgnoreCase(var1)) {
            var2.put("data2", 7);
         } else if ("WORK".equalsIgnoreCase(var1)) {
            var2.put("data2", 3);
         } else if (!"RADIO".equalsIgnoreCase(var1) && !"VOICE".equalsIgnoreCase(var1)) {
            if ("PAGER".equalsIgnoreCase(var1)) {
               var2.put("data2", 6);
            } else if ("CALLBACK".equalsIgnoreCase(var1)) {
               var2.put("data2", 8);
            } else if ("CAR".equalsIgnoreCase(var1)) {
               var2.put("data2", 9);
            } else if ("ASSISTANT".equalsIgnoreCase(var1)) {
               var2.put("data2", 19);
            } else if ("MMS".equalsIgnoreCase(var1)) {
               var2.put("data2", 20);
            } else if (var1.startsWith("FAX")) {
               var2.put("data2", 4);
            } else {
               var2.put("data2", var4);
               var2.put("data3", var1);
            }
         } else {
            var2.put("data2", 14);
         }
      } else {
         var2.put("data2", 2);
      }

   }

   private void fillUrlRowWithType(String var1, ContentValues var2) {
      boolean var3 = var1.startsWith("X-");
      Integer var4 = 0;
      if (var3) {
         var2.put("data2", var4);
         var2.put("data3", var1.substring(2));
      } else if ("HOMEPAGE".equalsIgnoreCase(var1)) {
         var2.put("data2", 1);
      } else if ("BLOG".equalsIgnoreCase(var1)) {
         var2.put("data2", 2);
      } else if ("PROFILE".equalsIgnoreCase(var1)) {
         var2.put("data2", 3);
      } else if ("HOME".equalsIgnoreCase(var1)) {
         var2.put("data2", 4);
      } else if ("WORK".equalsIgnoreCase(var1)) {
         var2.put("data2", 5);
      } else if ("FTP".equalsIgnoreCase(var1)) {
         var2.put("data2", 6);
      } else if ("OTHER".equalsIgnoreCase(var1)) {
         var2.put("data2", 7);
      } else {
         var2.put("data2", var4);
         var2.put("data3", var1);
      }

   }

   public void onClick(DialogInterface var1, int var2) {
      if (this.this$0.getParentActivity() != null) {
         Intent var11 = null;
         if (var2 == 0) {
            var11 = new Intent("android.intent.action.INSERT");
            var11.setType("vnd.android.cursor.dir/raw_contact");
         } else if (var2 == 1) {
            var11 = new Intent("android.intent.action.INSERT_OR_EDIT");
            var11.setType("vnd.android.cursor.item/contact");
         }

         var11.putExtra("name", ContactsController.formatName(PhonebookShareActivity.access$2000(this.this$0).first_name, PhonebookShareActivity.access$2000(this.this$0).last_name));
         ArrayList var3 = new ArrayList();

         AndroidUtilities.VcardItem var4;
         ContentValues var5;
         for(var2 = 0; var2 < PhonebookShareActivity.access$1300(this.this$0).size(); ++var2) {
            var4 = (AndroidUtilities.VcardItem)PhonebookShareActivity.access$1300(this.this$0).get(var2);
            var5 = new ContentValues();
            var5.put("mimetype", "vnd.android.cursor.item/phone_v2");
            var5.put("data1", var4.getValue(false));
            this.fillRowWithType(var4.getRawType(false), var5);
            var3.add(var5);
         }

         var2 = 0;

         for(boolean var6 = false; var2 < PhonebookShareActivity.access$1500(this.this$0).size(); ++var2) {
            var4 = (AndroidUtilities.VcardItem)PhonebookShareActivity.access$1500(this.this$0).get(var2);
            int var7 = var4.type;
            if (var7 == 1) {
               var5 = new ContentValues();
               var5.put("mimetype", "vnd.android.cursor.item/email_v2");
               var5.put("data1", var4.getValue(false));
               this.fillRowWithType(var4.getRawType(false), var5);
               var3.add(var5);
            } else if (var7 == 3) {
               var5 = new ContentValues();
               var5.put("mimetype", "vnd.android.cursor.item/website");
               var5.put("data1", var4.getValue(false));
               this.fillUrlRowWithType(var4.getRawType(false), var5);
               var3.add(var5);
            } else if (var7 == 4) {
               var5 = new ContentValues();
               var5.put("mimetype", "vnd.android.cursor.item/note");
               var5.put("data1", var4.getValue(false));
               var3.add(var5);
            } else if (var7 == 5) {
               var5 = new ContentValues();
               var5.put("mimetype", "vnd.android.cursor.item/contact_event");
               var5.put("data1", var4.getValue(false));
               var5.put("data2", 3);
               var3.add(var5);
            } else {
               String[] var17;
               if (var7 == 2) {
                  var5 = new ContentValues();
                  var5.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
                  var17 = var4.getRawValue();
                  if (var17.length > 0) {
                     var5.put("data5", var17[0]);
                  }

                  if (var17.length > 1) {
                     var5.put("data6", var17[1]);
                  }

                  if (var17.length > 2) {
                     var5.put("data4", var17[2]);
                  }

                  if (var17.length > 3) {
                     var5.put("data7", var17[3]);
                  }

                  if (var17.length > 4) {
                     var5.put("data8", var17[4]);
                  }

                  if (var17.length > 5) {
                     var5.put("data9", var17[5]);
                  }

                  if (var17.length > 6) {
                     var5.put("data10", var17[6]);
                  }

                  String var13 = var4.getRawType(false);
                  if ("HOME".equalsIgnoreCase(var13)) {
                     var5.put("data2", 1);
                  } else if ("WORK".equalsIgnoreCase(var13)) {
                     var5.put("data2", 2);
                  } else if ("OTHER".equalsIgnoreCase(var13)) {
                     var5.put("data2", 3);
                  }

                  var3.add(var5);
               } else {
                  String var15;
                  if (var7 == 20) {
                     ContentValues var18 = new ContentValues();
                     var18.put("mimetype", "vnd.android.cursor.item/im");
                     String var9 = var4.getRawType(true);
                     var15 = var4.getRawType(false);
                     var18.put("data1", var4.getValue(false));
                     if ("AIM".equalsIgnoreCase(var9)) {
                        var18.put("data5", 0);
                     } else if ("MSN".equalsIgnoreCase(var9)) {
                        var18.put("data5", 1);
                     } else if ("YAHOO".equalsIgnoreCase(var9)) {
                        var18.put("data5", 2);
                     } else if ("SKYPE".equalsIgnoreCase(var9)) {
                        var18.put("data5", 3);
                     } else if ("QQ".equalsIgnoreCase(var9)) {
                        var18.put("data5", 4);
                     } else if ("GOOGLE-TALK".equalsIgnoreCase(var9)) {
                        var18.put("data5", 5);
                     } else if ("ICQ".equalsIgnoreCase(var9)) {
                        var18.put("data5", 6);
                     } else if ("JABBER".equalsIgnoreCase(var9)) {
                        var18.put("data5", 7);
                     } else if ("NETMEETING".equalsIgnoreCase(var9)) {
                        var18.put("data5", 8);
                     } else {
                        var18.put("data5", -1);
                        var18.put("data6", var4.getRawType(true));
                     }

                     if ("HOME".equalsIgnoreCase(var15)) {
                        var18.put("data2", 1);
                     } else if ("WORK".equalsIgnoreCase(var15)) {
                        var18.put("data2", 2);
                     } else if ("OTHER".equalsIgnoreCase(var15)) {
                        var18.put("data2", 3);
                     }

                     var3.add(var18);
                  } else if (var7 == 6 && !var6) {
                     ContentValues var12 = new ContentValues();
                     var12.put("mimetype", "vnd.android.cursor.item/organization");

                     for(int var16 = var2; var16 < PhonebookShareActivity.access$1500(this.this$0).size(); ++var16) {
                        AndroidUtilities.VcardItem var14 = (AndroidUtilities.VcardItem)PhonebookShareActivity.access$1500(this.this$0).get(var16);
                        if (var14.type == 6) {
                           String var8 = var14.getRawType(true);
                           if ("ORG".equalsIgnoreCase(var8)) {
                              var17 = var14.getRawValue();
                              if (var17.length == 0) {
                                 continue;
                              }

                              if (var17.length >= 1) {
                                 var12.put("data1", var17[0]);
                              }

                              if (var17.length >= 2) {
                                 var12.put("data5", var17[1]);
                              }
                           } else if ("TITLE".equalsIgnoreCase(var8)) {
                              var12.put("data4", var14.getValue(false));
                           } else if ("ROLE".equalsIgnoreCase(var8)) {
                              var12.put("data4", var14.getValue(false));
                           }

                           var15 = var14.getRawType(true);
                           if ("WORK".equalsIgnoreCase(var15)) {
                              var12.put("data2", 1);
                           } else if ("OTHER".equalsIgnoreCase(var15)) {
                              var12.put("data2", 2);
                           }
                        }
                     }

                     var3.add(var12);
                     var6 = true;
                  }
               }
            }
         }

         var11.putExtra("finishActivityOnSaveCompleted", true);
         var11.putParcelableArrayListExtra("data", var3);

         try {
            this.this$0.getParentActivity().startActivity(var11);
            this.this$0.finishFragment();
         } catch (Exception var10) {
            FileLog.e((Throwable)var10);
         }

      }
   }
}
