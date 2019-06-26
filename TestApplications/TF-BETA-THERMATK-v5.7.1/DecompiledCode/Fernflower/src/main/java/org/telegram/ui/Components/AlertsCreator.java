package org.telegram.ui.Components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.LanguageSelectActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
import org.telegram.ui.ReportOtherActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextColorCell;

public class AlertsCreator {
   private static void checkPickerDate(NumberPicker var0, NumberPicker var1, NumberPicker var2) {
      Calendar var3 = Calendar.getInstance();
      var3.setTimeInMillis(System.currentTimeMillis());
      int var4 = var3.get(1);
      int var5 = var3.get(2);
      int var6 = var3.get(5);
      if (var4 > var2.getValue()) {
         var2.setValue(var4);
      }

      if (var2.getValue() == var4) {
         if (var5 > var1.getValue()) {
            var1.setValue(var5);
         }

         if (var5 == var1.getValue() && var6 > var0.getValue()) {
            var0.setValue(var6);
         }
      }

   }

   public static AlertDialog createAccountSelectDialog(Activity var0, AlertsCreator.AccountSelectDelegate var1) {
      if (UserConfig.getActivatedAccountsCount() < 2) {
         return null;
      } else {
         AlertDialog.Builder var2 = new AlertDialog.Builder(var0);
         Runnable var3 = var2.getDismissRunnable();
         AlertDialog[] var4 = new AlertDialog[1];
         LinearLayout var5 = new LinearLayout(var0);
         var5.setOrientation(1);

         for(int var6 = 0; var6 < 3; ++var6) {
            if (UserConfig.getInstance(var6).getCurrentUser() != null) {
               AccountSelectCell var7 = new AccountSelectCell(var0);
               var7.setAccount(var6, false);
               var7.setPadding(AndroidUtilities.dp(14.0F), 0, AndroidUtilities.dp(14.0F), 0);
               var7.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var5.addView(var7, LayoutHelper.createLinear(-1, 50));
               var7.setOnClickListener(new _$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA(var4, var3, var1));
            }
         }

         var2.setTitle(LocaleController.getString("SelectAccount", 2131560676));
         var2.setView(var5);
         var2.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         AlertDialog var8 = var2.create();
         var4[0] = var8;
         return var8;
      }
   }

   public static void createClearOrDeleteDialogAlert(BaseFragment var0, boolean var1, TLRPC.Chat var2, TLRPC.User var3, boolean var4, MessagesStorage.BooleanCallback var5) {
      createClearOrDeleteDialogAlert(var0, var1, false, false, var2, var3, var4, var5);
   }

   public static void createClearOrDeleteDialogAlert(BaseFragment var0, boolean var1, boolean var2, boolean var3, TLRPC.Chat var4, TLRPC.User var5, boolean var6, MessagesStorage.BooleanCallback var7) {
      if (var0 != null && var0.getParentActivity() != null && (var4 != null || var5 != null)) {
         int var8 = var0.getCurrentAccount();
         Activity var9 = var0.getParentActivity();
         AlertDialog.Builder var10 = new AlertDialog.Builder(var9);
         int var11 = UserConfig.getInstance(var8).getClientUserId();
         final CheckBoxCell[] var12 = new CheckBoxCell[1];
         TextView var13 = new TextView(var9);
         var13.setTextColor(Theme.getColor("dialogTextBlack"));
         var13.setTextSize(1, 16.0F);
         byte var14;
         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         var13.setGravity(var14 | 48);
         boolean var15;
         if (ChatObject.isChannel(var4) && !TextUtils.isEmpty(var4.username)) {
            var15 = true;
         } else {
            var15 = false;
         }

         FrameLayout var16 = new FrameLayout(var9) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, var2);
               if (var12[0] != null) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight() + var12[0].getMeasuredHeight() + AndroidUtilities.dp(7.0F));
               }

            }
         };
         var10.setView(var16);
         AvatarDrawable var17 = new AvatarDrawable();
         var17.setTextSize(AndroidUtilities.dp(12.0F));
         BackupImageView var18 = new BackupImageView(var9);
         var18.setRoundRadius(AndroidUtilities.dp(20.0F));
         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         var16.addView(var18, LayoutHelper.createFrame(40, 40.0F, var14 | 48, 22.0F, 5.0F, 22.0F, 0.0F));
         TextView var19 = new TextView(var9);
         var19.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
         var19.setTextSize(1, 20.0F);
         var19.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var19.setLines(1);
         var19.setMaxLines(1);
         var19.setSingleLine(true);
         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         var19.setGravity(var14 | 16);
         var19.setEllipsize(TruncateAt.END);
         if (var1) {
            if (var15) {
               var19.setText(LocaleController.getString("ClearHistoryCache", 2131559108));
            } else {
               var19.setText(LocaleController.getString("ClearHistory", 2131559107));
            }
         } else if (var2) {
            if (ChatObject.isChannel(var4)) {
               if (var4.megagroup) {
                  var19.setText(LocaleController.getString("DeleteMegaMenu", 2131559249));
               } else {
                  var19.setText(LocaleController.getString("ChannelDeleteMenu", 2131558947));
               }
            } else {
               var19.setText(LocaleController.getString("DeleteMegaMenu", 2131559249));
            }
         } else {
            var19.setText(LocaleController.getString("DeleteChatUser", 2131559240));
         }

         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         byte var20;
         if (LocaleController.isRTL) {
            var20 = 21;
         } else {
            var20 = 76;
         }

         float var21 = (float)var20;
         if (LocaleController.isRTL) {
            var20 = 76;
         } else {
            var20 = 21;
         }

         var16.addView(var19, LayoutHelper.createFrame(-1, -2.0F, var14 | 48, var21, 11.0F, (float)var20, 0.0F));
         if (LocaleController.isRTL) {
            var14 = 5;
         } else {
            var14 = 3;
         }

         var16.addView(var13, LayoutHelper.createFrame(-2, -2.0F, var14 | 48, 24.0F, 57.0F, 24.0F, 9.0F));
         boolean var26;
         if (var5 != null && !var5.bot && var5.id != var11 && MessagesController.getInstance(var8).canRevokePmInbox) {
            var26 = true;
         } else {
            var26 = false;
         }

         int var29;
         if (var5 != null) {
            var29 = MessagesController.getInstance(var8).revokeTimePmLimit;
         } else {
            var29 = MessagesController.getInstance(var8).revokeTimeLimit;
         }

         if (!var6 && var5 != null && var26 && var29 == Integer.MAX_VALUE) {
            var26 = true;
         } else {
            var26 = false;
         }

         boolean[] var27 = new boolean[1];
         if (!var3 && var26) {
            var12[0] = new CheckBoxCell(var9, 1);
            var12[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (var1) {
               var12[0].setText(LocaleController.formatString("ClearHistoryOptionAlso", 2131559109, UserObject.getFirstName(var5)), "", false, false);
            } else {
               var12[0].setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131559251, UserObject.getFirstName(var5)), "", false, false);
            }

            CheckBoxCell var24 = var12[0];
            int var28;
            if (LocaleController.isRTL) {
               var28 = AndroidUtilities.dp(16.0F);
            } else {
               var28 = AndroidUtilities.dp(8.0F);
            }

            var21 = 16.0F;
            if (LocaleController.isRTL) {
               var21 = 8.0F;
            }

            var24.setPadding(var28, 0, AndroidUtilities.dp(var21), 0);
            var16.addView(var12[0], LayoutHelper.createFrame(-1, 48.0F, 83, 0.0F, 0.0F, 0.0F, 0.0F));
            var12[0].setOnClickListener(new _$$Lambda$AlertsCreator$yLbyuHJw0N3q2V_bASpmXNgLM5I(var27));
         }

         if (var5 != null) {
            if (var5.id == var11) {
               var17.setAvatarType(2);
               var18.setImage((ImageLocation)null, (String)null, (Drawable)var17, (Object)var5);
            } else {
               var17.setInfo(var5);
               var18.setImage((ImageLocation)ImageLocation.getForUser(var5, false), "50_50", (Drawable)var17, (Object)var5);
            }
         } else if (var4 != null) {
            var17.setInfo(var4);
            var18.setImage((ImageLocation)ImageLocation.getForChat(var4, false), "50_50", (Drawable)var17, (Object)var4);
         }

         if (var3) {
            var13.setText(AndroidUtilities.replaceTags(LocaleController.getString("DeleteAllMessagesAlert", 2131559233)));
         } else if (!var1) {
            if (var2) {
               if (ChatObject.isChannel(var4)) {
                  if (var4.megagroup) {
                     var13.setText(LocaleController.getString("MegaDeleteAlert", 2131559827));
                  } else {
                     var13.setText(LocaleController.getString("ChannelDeleteAlert", 2131558944));
                  }
               } else {
                  var13.setText(LocaleController.getString("AreYouSureDeleteAndExit", 2131558678));
               }
            } else if (var5 != null) {
               if (var6) {
                  var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithSecretUser", 2131558691, UserObject.getUserName(var5))));
               } else if (var5.id == var11) {
                  var13.setText(AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureDeleteThisChatSavedMessages", 2131558689)));
               } else {
                  var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithUser", 2131558692, UserObject.getUserName(var5))));
               }
            } else if (ChatObject.isChannel(var4)) {
               if (var4.megagroup) {
                  var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("MegaLeaveAlertWithName", 2131559830, var4.title)));
               } else {
                  var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ChannelLeaveAlertWithName", 2131558958, var4.title)));
               }
            } else {
               var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteAndExitName", 2131558679, var4.title)));
            }
         } else if (var5 != null) {
            if (var6) {
               var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithSecretUser", 2131558676, UserObject.getUserName(var5))));
            } else if (var5.id == var11) {
               var13.setText(AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureClearHistorySavedMessages", 2131558674)));
            } else {
               var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", 2131558677, UserObject.getUserName(var5))));
            }
         } else if (var4 != null) {
            if (!ChatObject.isChannel(var4) || var4.megagroup && TextUtils.isEmpty(var4.username)) {
               var13.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", 2131558675, var4.title)));
            } else if (var4.megagroup) {
               var13.setText(LocaleController.getString("AreYouSureClearHistoryGroup", 2131558673));
            } else {
               var13.setText(LocaleController.getString("AreYouSureClearHistoryChannel", 2131558671));
            }
         }

         String var25;
         if (var3) {
            var25 = LocaleController.getString("DeleteAll", 2131559231);
         } else if (var1) {
            if (var15) {
               var25 = LocaleController.getString("ClearHistoryCache", 2131559108);
            } else {
               var25 = LocaleController.getString("ClearHistory", 2131559107);
            }
         } else if (var2) {
            if (ChatObject.isChannel(var4)) {
               if (var4.megagroup) {
                  var25 = LocaleController.getString("DeleteMega", 2131559248);
               } else {
                  var25 = LocaleController.getString("ChannelDelete", 2131558943);
               }
            } else {
               var25 = LocaleController.getString("DeleteMega", 2131559248);
            }
         } else if (ChatObject.isChannel(var4)) {
            if (var4.megagroup) {
               var25 = LocaleController.getString("LeaveMegaMenu", 2131559746);
            } else {
               var25 = LocaleController.getString("LeaveChannelMenu", 2131559745);
            }
         } else {
            var25 = LocaleController.getString("DeleteChatUser", 2131559240);
         }

         var10.setPositiveButton(var25, new _$$Lambda$AlertsCreator$g2LSO2I4rkrZ9986IhA1PoJ9pZ0(var5, var15, var3, var27, var0, var1, var2, var4, var6, var7));
         var10.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         AlertDialog var23 = var10.create();
         var0.showDialog(var23);
         TextView var22 = (TextView)var23.getButton(-1);
         if (var22 != null) {
            var22.setTextColor(Theme.getColor("dialogTextRed2"));
         }
      }

   }

   public static Dialog createColorSelectDialog(Activity var0, long var1, int var3, Runnable var4) {
      SharedPreferences var5 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
      int var7;
      if (var1 != 0L) {
         StringBuilder var6 = new StringBuilder();
         var6.append("color_");
         var6.append(var1);
         if (var5.contains(var6.toString())) {
            var6 = new StringBuilder();
            var6.append("color_");
            var6.append(var1);
            var7 = var5.getInt(var6.toString(), -16776961);
         } else if ((int)var1 < 0) {
            var7 = var5.getInt("GroupLed", -16776961);
         } else {
            var7 = var5.getInt("MessagesLed", -16776961);
         }
      } else if (var3 == 1) {
         var7 = var5.getInt("MessagesLed", -16776961);
      } else if (var3 == 0) {
         var7 = var5.getInt("GroupLed", -16776961);
      } else {
         var7 = var5.getInt("ChannelLed", -16776961);
      }

      LinearLayout var23 = new LinearLayout(var0);
      var23.setOrientation(1);
      String var8 = LocaleController.getString("ColorRed", 2131559127);
      String var9 = LocaleController.getString("ColorOrange", 2131559125);
      String var10 = LocaleController.getString("ColorYellow", 2131559133);
      String var11 = LocaleController.getString("ColorGreen", 2131559124);
      String var12 = LocaleController.getString("ColorCyan", 2131559122);
      String var13 = LocaleController.getString("ColorBlue", 2131559121);
      String var14 = LocaleController.getString("ColorViolet", 2131559131);
      String var15 = LocaleController.getString("ColorPink", 2131559126);
      String var16 = LocaleController.getString("ColorWhite", 2131559132);
      int[] var22 = new int[]{var7};

      for(int var17 = 0; var17 < 9; ++var17) {
         RadioColorCell var18 = new RadioColorCell(var0);
         var18.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var18.setTag(var17);
         int[] var19 = TextColorCell.colors;
         var18.setCheckColor(var19[var17], var19[var17]);
         String var24 = (new String[]{var8, var9, var10, var11, var12, var13, var14, var15, var16})[var17];
         boolean var20;
         if (var7 == TextColorCell.colorsToSave[var17]) {
            var20 = true;
         } else {
            var20 = false;
         }

         var18.setTextAndValue(var24, var20);
         var23.addView(var18);
         var18.setOnClickListener(new _$$Lambda$AlertsCreator$EEBJwM4EJMpcSWE5sZNxj3sr1GA(var23, var22));
      }

      AlertDialog.Builder var21 = new AlertDialog.Builder(var0);
      var21.setTitle(LocaleController.getString("LedColor", 2131559747));
      var21.setView(var23);
      var21.setPositiveButton(LocaleController.getString("Set", 2131560727), new _$$Lambda$AlertsCreator$Rj9lP7vArvQH1Tiv_8DxDTBh7S8(var1, var22, var3, var4));
      var21.setNeutralButton(LocaleController.getString("LedDisabled", 2131559748), new _$$Lambda$AlertsCreator$kEfeT_ztr5ipao3BeuOGHHEiQuA(var1, var3, var4));
      if (var1 != 0L) {
         var21.setNegativeButton(LocaleController.getString("Default", 2131559225), new _$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd_I(var1, var4));
      }

      return var21.create();
   }

   public static AlertDialog.Builder createContactsPermissionDialog(Activity var0, MessagesStorage.IntCallback var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(var0);
      var2.setTopImage(2131165737, Theme.getColor("dialogTopBackground"));
      var2.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("ContactsPermissionAlert", 2131559150)));
      var2.setPositiveButton(LocaleController.getString("ContactsPermissionAlertContinue", 2131559151), new _$$Lambda$AlertsCreator$mK_3N6UsQGpxwpYn5q6EG9jwH5M(var1));
      var2.setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131559152), new _$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE(var1));
      return var2;
   }

   public static AlertDialog.Builder createDatePickerDialog(Context var0, int var1, int var2, int var3, int var4, int var5, int var6, String var7, boolean var8, AlertsCreator.DatePickerDelegate var9) {
      if (var0 == null) {
         return null;
      } else {
         LinearLayout var10 = new LinearLayout(var0);
         var10.setOrientation(0);
         var10.setWeightSum(1.0F);
         NumberPicker var11 = new NumberPicker(var0);
         NumberPicker var12 = new NumberPicker(var0);
         NumberPicker var13 = new NumberPicker(var0);
         var10.addView(var12, LayoutHelper.createLinear(0, -2, 0.3F));
         var12.setOnScrollListener(new _$$Lambda$AlertsCreator$gs8o89qyKUnWVnMUXk35_sRm5SI(var8, var12, var11, var13));
         var11.setMinValue(0);
         var11.setMaxValue(11);
         var10.addView(var11, LayoutHelper.createLinear(0, -2, 0.3F));
         var11.setFormatter(_$$Lambda$AlertsCreator$OhyVjdpGLg_SrRDIQ3KPC_TfuKY.INSTANCE);
         var11.setOnValueChangedListener(new _$$Lambda$AlertsCreator$8JhkY0KQHgsrUGNU82e_Gw1etjI(var12, var11, var13));
         var11.setOnScrollListener(new _$$Lambda$AlertsCreator$CdjKbi3tjQh9DAk90YIRTCSaBIs(var8, var12, var11, var13));
         Calendar var14 = Calendar.getInstance();
         var14.setTimeInMillis(System.currentTimeMillis());
         int var15 = var14.get(1);
         var13.setMinValue(var15 + var1);
         var13.setMaxValue(var15 + var2);
         var13.setValue(var15 + var3);
         var10.addView(var13, LayoutHelper.createLinear(0, -2, 0.4F));
         var13.setOnValueChangedListener(new _$$Lambda$AlertsCreator$mo66TgHnBRhv_TioolPkrpTopo0(var12, var11, var13));
         var13.setOnScrollListener(new _$$Lambda$AlertsCreator$X9eAz_vGDgt2Lf0L8benu7NuJlM(var8, var12, var11, var13));
         updateDayPicker(var12, var11, var13);
         if (var8) {
            checkPickerDate(var12, var11, var13);
         }

         if (var4 != -1) {
            var12.setValue(var4);
            var11.setValue(var5);
            var13.setValue(var6);
         }

         AlertDialog.Builder var16 = new AlertDialog.Builder(var0);
         var16.setTitle(var7);
         var16.setView(var10);
         var16.setPositiveButton(LocaleController.getString("Set", 2131560727), new _$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU(var8, var12, var11, var13, var9));
         var16.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         return var16;
      }
   }

   public static void createDeleteMessagesAlert(BaseFragment var0, TLRPC.User var1, TLRPC.Chat var2, TLRPC.EncryptedChat var3, TLRPC.ChatFull var4, long var5, MessageObject var7, SparseArray[] var8, MessageObject.GroupedMessages var9, int var10, Runnable var11) {
      if (var0 != null && (var1 != null || var2 != null || var3 != null)) {
         Activity var12 = var0.getParentActivity();
         if (var12 == null) {
            return;
         }

         int var13 = var0.getCurrentAccount();
         AlertDialog.Builder var14 = new AlertDialog.Builder(var12);
         int var15;
         if (var9 != null) {
            var15 = var9.messages.size();
         } else if (var7 != null) {
            var15 = 1;
         } else {
            var15 = var8[0].size() + var8[1].size();
         }

         boolean[] var16 = new boolean[3];
         boolean[] var17 = new boolean[1];
         boolean var18;
         if (var1 != null && MessagesController.getInstance(var13).canRevokePmInbox) {
            var18 = true;
         } else {
            var18 = false;
         }

         int var19;
         if (var1 != null) {
            var19 = MessagesController.getInstance(var13).revokeTimePmLimit;
         } else {
            var19 = MessagesController.getInstance(var13).revokeTimeLimit;
         }

         boolean var20;
         if (var3 == null && var1 != null && var18 && var19 == Integer.MAX_VALUE) {
            var20 = true;
         } else {
            var20 = false;
         }

         TLRPC.MessageAction var23;
         int var25;
         int var26;
         int var27;
         int var28;
         boolean var33;
         boolean var37;
         int var42;
         TLRPC.User var47;
         boolean var53;
         if (var2 != null && var2.megagroup) {
            boolean var21 = ChatObject.canBlockUsers(var2);
            int var46 = ConnectionsManager.getInstance(var13).getCurrentTime();
            TLRPC.User var38;
            boolean[] var49;
            if (var7 != null) {
               var23 = var7.messageOwner.action;
               TLRPC.User var44;
               if (var23 != null && !(var23 instanceof TLRPC.TL_messageActionEmpty) && !(var23 instanceof TLRPC.TL_messageActionChatDeleteUser) && !(var23 instanceof TLRPC.TL_messageActionChatJoinedByLink) && !(var23 instanceof TLRPC.TL_messageActionChatAddUser)) {
                  var44 = null;
               } else {
                  var44 = MessagesController.getInstance(var13).getUser(var7.messageOwner.from_id);
               }

               label294: {
                  if (!var7.isSendError() && var7.getDialogId() == var5) {
                     TLRPC.MessageAction var48 = var7.messageOwner.action;
                     if ((var48 == null || var48 instanceof TLRPC.TL_messageActionEmpty) && var7.isOut() && var46 - var7.messageOwner.date <= var19) {
                        var53 = true;
                        break label294;
                     }
                  }

                  var53 = false;
               }

               if (var53) {
                  var25 = 1;
               } else {
                  var25 = 0;
               }

               var38 = var44;
               var49 = var17;
            } else {
               var42 = 1;
               var25 = -1;

               while(true) {
                  if (var42 < 0) {
                     var49 = var17;
                     var42 = var25;
                     break;
                  }

                  var26 = 0;

                  while(true) {
                     var27 = var8[var42].size();
                     if (var26 >= var27) {
                        break;
                     }

                     MessageObject var51 = (MessageObject)var8[var42].valueAt(var26);
                     var27 = var25;
                     if (var25 == -1) {
                        var27 = var51.messageOwner.from_id;
                     }

                     if (var27 < 0 || var27 != var51.messageOwner.from_id) {
                        var25 = -2;
                        break;
                     }

                     ++var26;
                     var25 = var27;
                  }

                  if (var25 == -2) {
                     var42 = var25;
                     var49 = var17;
                     break;
                  }

                  --var42;
               }

               var26 = 1;

               for(var25 = 0; var26 >= 0; --var26) {
                  for(var27 = 0; var27 < var8[var26].size(); var25 = var28) {
                     MessageObject var39 = (MessageObject)var8[var26].valueAt(var27);
                     var28 = var25;
                     if (var26 == 1) {
                        var28 = var25;
                        if (var39.isOut()) {
                           TLRPC.Message var40 = var39.messageOwner;
                           var28 = var25;
                           if (var40.action == null) {
                              var28 = var25;
                              if (var46 - var40.date <= var19) {
                                 var28 = var25 + 1;
                              }
                           }
                        }
                     }

                     ++var27;
                  }
               }

               if (var42 != -1) {
                  var38 = MessagesController.getInstance(var13).getUser(var42);
               } else {
                  var38 = null;
               }
            }

            if (var38 != null && var38.id != UserConfig.getInstance(var13).getClientUserId()) {
               if (var10 == 1 && !var2.creator) {
                  AlertDialog[] var36 = new AlertDialog[]{new AlertDialog(var12, 3)};
                  TLRPC.TL_channels_getParticipant var54 = new TLRPC.TL_channels_getParticipant();
                  var54.channel = MessagesController.getInputChannel(var2);
                  var54.user_id = MessagesController.getInstance(var13).getInputUser(var38);
                  AndroidUtilities.runOnUIThread(new _$$Lambda$AlertsCreator$D1rCqnpmmZ_i7oeXROOeLU5F9Lw(var36, var13, ConnectionsManager.getInstance(var13).sendRequest(var54, new _$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60(var36, var0, var1, var2, var3, var4, var5, var7, var8, var9, var11)), var0), 1000L);
                  return;
               }

               FrameLayout var52 = new FrameLayout(var12);
               var19 = 0;

               for(var42 = 0; var19 < 3; ++var19) {
                  if (var10 != 2 && var21 || var19 != 0) {
                     CheckBoxCell var55 = new CheckBoxCell(var12, 1);
                     var55.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                     var55.setTag(var19);
                     if (var19 == 0) {
                        var55.setText(LocaleController.getString("DeleteBanUser", 2131559237), "", false, false);
                     } else if (var19 == 1) {
                        var55.setText(LocaleController.getString("DeleteReportSpam", 2131559258), "", false, false);
                     } else if (var19 == 2) {
                        var55.setText(LocaleController.formatString("DeleteAllFrom", 2131559232, ContactsController.formatName(var38.first_name, var38.last_name)), "", false, false);
                     }

                     if (LocaleController.isRTL) {
                        var26 = AndroidUtilities.dp(16.0F);
                     } else {
                        var26 = AndroidUtilities.dp(8.0F);
                     }

                     if (LocaleController.isRTL) {
                        var27 = AndroidUtilities.dp(8.0F);
                     } else {
                        var27 = AndroidUtilities.dp(16.0F);
                     }

                     var55.setPadding(var26, 0, var27, 0);
                     var52.addView(var55, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, (float)(var42 * 48), 0.0F, 0.0F));
                     var55.setOnClickListener(new _$$Lambda$AlertsCreator$8lY3m8434m5TxXZClUU2olEbfQc(var16));
                     ++var42;
                  }
               }

               var14.setView(var52);
               var33 = false;
            } else if (var25 > 0) {
               FrameLayout var29 = new FrameLayout(var12);
               CheckBoxCell var34 = new CheckBoxCell(var12, 1);
               var34.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var34.setText(LocaleController.getString("DeleteMessagesOption", 2131559250), "", false, false);
               if (LocaleController.isRTL) {
                  var10 = AndroidUtilities.dp(16.0F);
               } else {
                  var10 = AndroidUtilities.dp(8.0F);
               }

               if (LocaleController.isRTL) {
                  var19 = AndroidUtilities.dp(8.0F);
               } else {
                  var19 = AndroidUtilities.dp(16.0F);
               }

               var34.setPadding(var10, 0, var19, 0);
               var29.addView(var34, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               var34.setOnClickListener(new _$$Lambda$AlertsCreator$cEBDgpO_FSHMoQA0xI0BgbUin8I(var49));
               var14.setView(var29);
               var14.setCustomViewOffset(9);
               var33 = true;
            } else {
               var33 = false;
               var38 = null;
            }

            var47 = var38;
            var42 = var15;
            var37 = false;
            var26 = var25;
            var17 = var49;
            var25 = var13;
         } else {
            var10 = var13;
            boolean var41;
            if (!ChatObject.isChannel(var2) && var3 == null) {
               int var30 = ConnectionsManager.getInstance(var13).getCurrentTime();
               if ((var1 == null || var1.id == UserConfig.getInstance(var13).getClientUserId() || var1.bot) && var2 == null) {
                  var13 = 0;
                  var41 = false;
                  var25 = var10;
                  var33 = var41;
               } else if (var7 != null) {
                  label371: {
                     if (!var7.isSendError()) {
                        var23 = var7.messageOwner.action;
                        if ((var23 == null || var23 instanceof TLRPC.TL_messageActionEmpty || var23 instanceof TLRPC.TL_messageActionPhoneCall) && (var7.isOut() || var18 || ChatObject.hasAdminRights(var2)) && var30 - var7.messageOwner.date <= var19) {
                           var53 = true;
                           break label371;
                        }
                     }

                     var53 = false;
                  }

                  if (var53) {
                     var13 = 1;
                  } else {
                     var13 = 0;
                  }

                  var41 = var7.isOut() ^ true;
                  var25 = var10;
                  var33 = var41;
               } else {
                  var26 = 1;
                  boolean var35 = false;
                  var25 = 0;

                  while(true) {
                     if (var26 < 0) {
                        var19 = var13;
                        var33 = var35;
                        var13 = var25;
                        var25 = var19;
                        break;
                     }

                     for(var28 = 0; var28 < var8[var26].size(); var25 = var27) {
                        MessageObject var24 = (MessageObject)var8[var26].valueAt(var28);
                        var23 = var24.messageOwner.action;
                        boolean var22;
                        if (var23 != null && !(var23 instanceof TLRPC.TL_messageActionEmpty) && !(var23 instanceof TLRPC.TL_messageActionPhoneCall)) {
                           var22 = var35;
                           var27 = var25;
                        } else {
                           label460: {
                              if (!var24.isOut() && !var18) {
                                 var22 = var35;
                                 var27 = var25;
                                 if (var2 == null) {
                                    break label460;
                                 }

                                 var22 = var35;
                                 var27 = var25;
                                 if (!ChatObject.canBlockUsers(var2)) {
                                    break label460;
                                 }
                              }

                              var22 = var35;
                              var27 = var25;
                              if (var30 - var24.messageOwner.date <= var19) {
                                 ++var25;
                                 var22 = var35;
                                 var27 = var25;
                                 if (!var35) {
                                    var22 = var35;
                                    var27 = var25;
                                    if (!var24.isOut()) {
                                       var22 = true;
                                       var27 = var25;
                                    }
                                 }
                              }
                           }
                        }

                        ++var28;
                        var35 = var22;
                     }

                     --var26;
                  }
               }

               if (var13 > 0) {
                  FrameLayout var43 = new FrameLayout(var12);
                  CheckBoxCell var45 = new CheckBoxCell(var12, 1);
                  var45.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                  if (var20) {
                     var45.setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131559251, UserObject.getFirstName(var1)), "", false, false);
                  } else if (var2 == null || !var33 && var13 != var15) {
                     var45.setText(LocaleController.getString("DeleteMessagesOption", 2131559250), "", false, false);
                  } else {
                     var45.setText(LocaleController.getString("DeleteForAll", 2131559243), "", false, false);
                  }

                  if (LocaleController.isRTL) {
                     var19 = AndroidUtilities.dp(16.0F);
                  } else {
                     var19 = AndroidUtilities.dp(8.0F);
                  }

                  if (LocaleController.isRTL) {
                     var42 = AndroidUtilities.dp(8.0F);
                  } else {
                     var42 = AndroidUtilities.dp(16.0F);
                  }

                  var45.setPadding(var19, 0, var42, 0);
                  var43.addView(var45, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
                  var45.setOnClickListener(new _$$Lambda$AlertsCreator$394GBGS7wRI0qRQjkwqQ4GWlXk8(var17));
                  var14.setView(var43);
                  var14.setCustomViewOffset(9);
                  var41 = true;
               } else {
                  var41 = false;
               }
            } else {
               var41 = false;
               var13 = 0;
               var18 = false;
               var25 = var10;
               var33 = var18;
            }

            var47 = null;
            var42 = var15;
            var37 = var33;
            var26 = var13;
            var33 = var41;
         }

         var14.setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$AlertsCreator$OgrqERKmCgdU4Q6tzVkBNsq_WCY(var7, var9, var3, var25, var17, var8, var47, var16, var2, var4, var11));
         if (var42 == 1) {
            var14.setTitle(LocaleController.getString("DeleteSingleMessagesTitle", 2131559259));
         } else {
            var14.setTitle(LocaleController.formatString("DeleteMessagesTitle", 2131559255, LocaleController.formatPluralString("messages", var42)));
         }

         if (var2 != null && var37) {
            if (var33 && var26 != var42) {
               var14.setMessage(LocaleController.formatString("DeleteMessagesTextGroupPart", 2131559254, LocaleController.formatPluralString("messages", var26)));
            } else if (var42 == 1) {
               var14.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131558686));
            } else {
               var14.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131558682));
            }
         } else if (var33 && !var20 && var26 != var42) {
            if (var2 != null) {
               var14.setMessage(LocaleController.formatString("DeleteMessagesTextGroup", 2131559253, LocaleController.formatPluralString("messages", var26)));
            } else {
               var14.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DeleteMessagesText", 2131559252, LocaleController.formatPluralString("messages", var26), UserObject.getFirstName(var1))));
            }
         } else if (var2 != null && var2.megagroup) {
            if (var42 == 1) {
               var14.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessageMega", 2131558687));
            } else {
               var14.setMessage(LocaleController.getString("AreYouSureDeleteFewMessagesMega", 2131558683));
            }
         } else if (var42 == 1) {
            var14.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131558686));
         } else {
            var14.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131558682));
         }

         var14.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         AlertDialog var32 = var14.create();
         var0.showDialog(var32);
         TextView var31 = (TextView)var32.getButton(-1);
         if (var31 != null) {
            var31.setTextColor(Theme.getColor("dialogTextRed2"));
         }
      }

   }

   public static Dialog createFreeSpaceDialog(LaunchActivity var0) {
      int[] var1 = new int[1];
      int var2 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
      byte var3 = 3;
      if (var2 == 2) {
         var1[0] = 3;
      } else if (var2 == 0) {
         var1[0] = 1;
      } else if (var2 == 1) {
         var1[0] = 2;
      } else if (var2 == 3) {
         var1[0] = 0;
      }

      String[] var4 = new String[]{LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("LowDiskSpaceNeverRemove", 2131559792)};
      LinearLayout var5 = new LinearLayout(var0);
      var5.setOrientation(1);
      TextView var6 = new TextView(var0);
      var6.setText(LocaleController.getString("LowDiskSpaceTitle2", 2131559794));
      var6.setTextColor(Theme.getColor("dialogTextBlack"));
      var6.setTextSize(1, 16.0F);
      var6.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      byte var9;
      if (LocaleController.isRTL) {
         var9 = 5;
      } else {
         var9 = 3;
      }

      var6.setGravity(var9 | 48);
      var9 = var3;
      if (LocaleController.isRTL) {
         var9 = 5;
      }

      var5.addView(var6, LayoutHelper.createLinear(-2, -2, var9 | 48, 24, 0, 24, 8));

      for(var2 = 0; var2 < var4.length; ++var2) {
         RadioColorCell var11 = new RadioColorCell(var0);
         var11.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var11.setTag(var2);
         var11.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var7 = var4[var2];
         boolean var8;
         if (var1[0] == var2) {
            var8 = true;
         } else {
            var8 = false;
         }

         var11.setTextAndValue(var7, var8);
         var5.addView(var11);
         var11.setOnClickListener(new _$$Lambda$AlertsCreator$MUScg_WxzdOfhXudgtHT1hpQ36U(var1, var5));
      }

      AlertDialog.Builder var10 = new AlertDialog.Builder(var0);
      var10.setTitle(LocaleController.getString("LowDiskSpaceTitle", 2131559793));
      var10.setMessage(LocaleController.getString("LowDiskSpaceMessage", 2131559791));
      var10.setView(var5);
      var10.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$AlertsCreator$k_sgLR2K0GmG2e1LcJ2h3j2LTL4(var1));
      var10.setNeutralButton(LocaleController.getString("ClearMediaCache", 2131559110), new _$$Lambda$AlertsCreator$xGMadtvSKvLVwUxwu6lfN7pshfU(var0));
      return var10.create();
   }

   public static AlertDialog.Builder createLanguageAlert(LaunchActivity var0, TLRPC.TL_langPackLanguage var1) {
      if (var1 == null) {
         return null;
      } else {
         var1.lang_code = var1.lang_code.replace('-', '_').toLowerCase();
         var1.plural_code = var1.plural_code.replace('-', '_').toLowerCase();
         String var2 = var1.base_lang_code;
         if (var2 != null) {
            var1.base_lang_code = var2.replace('-', '_').toLowerCase();
         }

         final AlertDialog.Builder var3 = new AlertDialog.Builder(var0);
         if (LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals(var1.lang_code)) {
            var3.setTitle(LocaleController.getString("Language", 2131559715));
            var2 = LocaleController.formatString("LanguageSame", 2131559722, var1.name);
            var3.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            var3.setNeutralButton(LocaleController.getString("SETTINGS", 2131560623), new _$$Lambda$AlertsCreator$LEkyy2uvzoVwagVSlDPe5F3R2jI(var0));
         } else if (var1.strings_count == 0) {
            var3.setTitle(LocaleController.getString("LanguageUnknownTitle", 2131559725));
            var2 = LocaleController.formatString("LanguageUnknownCustomAlert", 2131559724, var1.name);
            var3.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         } else {
            var3.setTitle(LocaleController.getString("LanguageTitle", 2131559723));
            if (var1.official) {
               var2 = LocaleController.formatString("LanguageAlert", 2131559716, var1.name, (int)Math.ceil((double)((float)var1.translated_count / (float)var1.strings_count * 100.0F)));
            } else {
               var2 = LocaleController.formatString("LanguageCustomAlert", 2131559719, var1.name, (int)Math.ceil((double)((float)var1.translated_count / (float)var1.strings_count * 100.0F)));
            }

            var3.setPositiveButton(LocaleController.getString("Change", 2131558905), new _$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg(var1, var0));
            var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         }

         SpannableStringBuilder var9 = new SpannableStringBuilder(AndroidUtilities.replaceTags(var2));
         int var4 = TextUtils.indexOf(var9, '[');
         int var7;
         if (var4 != -1) {
            int var5 = var4 + 1;
            int var6 = TextUtils.indexOf(var9, ']', var5);
            var7 = var6;
            if (var4 != -1) {
               var7 = var6;
               if (var6 != -1) {
                  var9.delete(var6, var6 + 1);
                  var9.delete(var4, var5);
                  var7 = var6;
               }
            }
         } else {
            var7 = -1;
         }

         if (var4 != -1 && var7 != -1) {
            var9.setSpan(new URLSpanNoUnderline(var1.translations_url) {
               public void onClick(View var1) {
                  var3.getDismissRunnable().run();
                  super.onClick(var1);
               }
            }, var4, var7 - 1, 33);
         }

         TextView var8 = new TextView(var0);
         var8.setText(var9);
         var8.setTextSize(1, 16.0F);
         var8.setLinkTextColor(Theme.getColor("dialogTextLink"));
         var8.setHighlightColor(Theme.getColor("dialogLinkSelection"));
         var8.setPadding(AndroidUtilities.dp(23.0F), 0, AndroidUtilities.dp(23.0F), 0);
         var8.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
         var8.setTextColor(Theme.getColor("dialogTextBlack"));
         var3.setView(var8);
         return var3;
      }
   }

   public static Dialog createLocationUpdateDialog(Activity var0, TLRPC.User var1, MessagesStorage.IntCallback var2) {
      int[] var3 = new int[1];
      byte var4 = 3;
      String[] var5 = new String[]{LocaleController.getString("SendLiveLocationFor15m", 2131560696), LocaleController.getString("SendLiveLocationFor1h", 2131560697), LocaleController.getString("SendLiveLocationFor8h", 2131560698)};
      LinearLayout var6 = new LinearLayout(var0);
      var6.setOrientation(1);
      TextView var7 = new TextView(var0);
      if (var1 != null) {
         var7.setText(LocaleController.formatString("LiveLocationAlertPrivate", 2131559766, UserObject.getFirstName(var1)));
      } else {
         var7.setText(LocaleController.getString("LiveLocationAlertGroup", 2131559765));
      }

      var7.setTextColor(Theme.getColor("dialogTextBlack"));
      var7.setTextSize(1, 16.0F);
      byte var8;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      var7.setGravity(var8 | 48);
      var8 = var4;
      if (LocaleController.isRTL) {
         var8 = 5;
      }

      var6.addView(var7, LayoutHelper.createLinear(-2, -2, var8 | 48, 24, 0, 24, 8));

      for(int var13 = 0; var13 < var5.length; ++var13) {
         RadioColorCell var12 = new RadioColorCell(var0);
         var12.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var12.setTag(var13);
         var12.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var10 = var5[var13];
         boolean var9;
         if (var3[0] == var13) {
            var9 = true;
         } else {
            var9 = false;
         }

         var12.setTextAndValue(var10, var9);
         var6.addView(var12);
         var12.setOnClickListener(new _$$Lambda$AlertsCreator$KrxSVo5KH_kGkjf6bCbY8P17cD4(var3, var6));
      }

      AlertDialog.Builder var11 = new AlertDialog.Builder(var0);
      var11.setTopImage(new ShareLocationDrawable(var0, false), Theme.getColor("dialogTopBackground"));
      var11.setView(var6);
      var11.setPositiveButton(LocaleController.getString("ShareFile", 2131560748), new _$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU(var3, var2));
      var11.setNeutralButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      return var11.create();
   }

   public static Dialog createMuteAlert(Context var0, long var1) {
      if (var0 == null) {
         return null;
      } else {
         BottomSheet.Builder var3 = new BottomSheet.Builder(var0);
         var3.setTitle(LocaleController.getString("Notifications", 2131560055));
         String var4 = LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 1));
         String var5 = LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 8));
         String var8 = LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Days", 2));
         String var6 = LocaleController.getString("MuteDisable", 2131559885);
         _$$Lambda$AlertsCreator$V32PpNrShc6YGjfaIJjE0yrRz3k var7 = new _$$Lambda$AlertsCreator$V32PpNrShc6YGjfaIJjE0yrRz3k(var1);
         var3.setItems(new CharSequence[]{var4, var5, var8, var6}, var7);
         return var3.create();
      }
   }

   public static Dialog createPopupSelectDialog(Activity var0, int var1, Runnable var2) {
      SharedPreferences var3 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
      int[] var4 = new int[1];
      if (var1 == 1) {
         var4[0] = var3.getInt("popupAll", 0);
      } else if (var1 == 0) {
         var4[0] = var3.getInt("popupGroup", 0);
      } else {
         var4[0] = var3.getInt("popupChannel", 0);
      }

      String[] var5 = new String[]{LocaleController.getString("NoPopup", 2131559939), LocaleController.getString("OnlyWhenScreenOn", 2131560109), LocaleController.getString("OnlyWhenScreenOff", 2131560108), LocaleController.getString("AlwaysShowPopup", 2131558614)};
      LinearLayout var6 = new LinearLayout(var0);
      var6.setOrientation(1);
      AlertDialog.Builder var7 = new AlertDialog.Builder(var0);

      for(int var8 = 0; var8 < var5.length; ++var8) {
         RadioColorCell var11 = new RadioColorCell(var0);
         var11.setTag(var8);
         var11.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var11.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var9 = var5[var8];
         boolean var10;
         if (var4[0] == var8) {
            var10 = true;
         } else {
            var10 = false;
         }

         var11.setTextAndValue(var9, var10);
         var6.addView(var11);
         var11.setOnClickListener(new _$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns(var4, var1, var7, var2));
      }

      var7.setTitle(LocaleController.getString("PopupNotification", 2131560471));
      var7.setView(var6);
      var7.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      return var7.create();
   }

   public static Dialog createPrioritySelectDialog(Activity var0, long var1, int var3, Runnable var4) {
      SharedPreferences var6 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
      int[] var7 = new int[1];
      String[] var14;
      if (var1 != 0L) {
         StringBuilder var8 = new StringBuilder();
         var8.append("priority_");
         var8.append(var1);
         var7[0] = var6.getInt(var8.toString(), 3);
         if (var7[0] == 3) {
            var7[0] = 0;
         } else if (var7[0] == 4) {
            var7[0] = 1;
         } else if (var7[0] == 5) {
            var7[0] = 2;
         } else if (var7[0] == 0) {
            var7[0] = 3;
         } else {
            var7[0] = 4;
         }

         var14 = new String[]{LocaleController.getString("NotificationsPrioritySettings", 2131560085), LocaleController.getString("NotificationsPriorityLow", 2131560083), LocaleController.getString("NotificationsPriorityMedium", 2131560084), LocaleController.getString("NotificationsPriorityHigh", 2131560082), LocaleController.getString("NotificationsPriorityUrgent", 2131560086)};
      } else {
         if (var1 == 0L) {
            if (var3 == 1) {
               var7[0] = var6.getInt("priority_messages", 1);
            } else if (var3 == 0) {
               var7[0] = var6.getInt("priority_group", 1);
            } else if (var3 == 2) {
               var7[0] = var6.getInt("priority_channel", 1);
            }
         }

         if (var7[0] == 4) {
            var7[0] = 0;
         } else if (var7[0] == 5) {
            var7[0] = 1;
         } else if (var7[0] == 0) {
            var7[0] = 2;
         } else {
            var7[0] = 3;
         }

         var14 = new String[]{LocaleController.getString("NotificationsPriorityLow", 2131560083), LocaleController.getString("NotificationsPriorityMedium", 2131560084), LocaleController.getString("NotificationsPriorityHigh", 2131560082), LocaleController.getString("NotificationsPriorityUrgent", 2131560086)};
      }

      LinearLayout var9 = new LinearLayout(var0);
      var9.setOrientation(1);
      AlertDialog.Builder var10 = new AlertDialog.Builder(var0);
      int var11 = 0;

      LinearLayout var5;
      for(var5 = var9; var11 < var14.length; ++var11) {
         RadioColorCell var12 = new RadioColorCell(var0);
         var12.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var12.setTag(var11);
         var12.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var15 = var14[var11];
         boolean var13;
         if (var7[0] == var11) {
            var13 = true;
         } else {
            var13 = false;
         }

         var12.setTextAndValue(var15, var13);
         var5.addView(var12);
         var12.setOnClickListener(new _$$Lambda$AlertsCreator$DVGlkIX7hR4sQs3b_3TsrRLkHrw(var7, var1, var3, var6, var10, var4));
      }

      var10.setTitle(LocaleController.getString("NotificationsImportance", 2131560072));
      var10.setView(var5);
      var10.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      return var10.create();
   }

   public static void createReportAlert(Context var0, long var1, int var3, BaseFragment var4) {
      if (var0 != null && var4 != null) {
         BottomSheet.Builder var5 = new BottomSheet.Builder(var0);
         var5.setTitle(LocaleController.getString("ReportChat", 2131560568));
         String var6 = LocaleController.getString("ReportChatSpam", 2131560574);
         String var7 = LocaleController.getString("ReportChatViolence", 2131560575);
         String var8 = LocaleController.getString("ReportChatChild", 2131560569);
         String var9 = LocaleController.getString("ReportChatPornography", 2131560572);
         String var10 = LocaleController.getString("ReportChatOther", 2131560571);
         _$$Lambda$AlertsCreator$72Ijnyr0wy8R08YUf_bNvhf4MNU var11 = new _$$Lambda$AlertsCreator$72Ijnyr0wy8R08YUf_bNvhf4MNU(var1, var3, var4, var0);
         var5.setItems(new CharSequence[]{var6, var7, var8, var9, var10}, var11);
         var4.showDialog(var5.create());
      }

   }

   public static AlertDialog.Builder createSimpleAlert(Context var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         AlertDialog.Builder var2 = new AlertDialog.Builder(var0);
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         var2.setMessage(var1);
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         return var2;
      }
   }

   public static Dialog createSingleChoiceDialog(Activity var0, String[] var1, String var2, int var3, OnClickListener var4) {
      LinearLayout var5 = new LinearLayout(var0);
      var5.setOrientation(1);
      AlertDialog.Builder var6 = new AlertDialog.Builder(var0);

      for(int var7 = 0; var7 < var1.length; ++var7) {
         RadioColorCell var8 = new RadioColorCell(var0);
         var8.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var8.setTag(var7);
         var8.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var9 = var1[var7];
         boolean var10;
         if (var3 == var7) {
            var10 = true;
         } else {
            var10 = false;
         }

         var8.setTextAndValue(var9, var10);
         var5.addView(var8);
         var8.setOnClickListener(new _$$Lambda$AlertsCreator$NzxH3UcpchY_thDrzzdwxnXbOG4(var6, var4));
      }

      var6.setTitle(var2);
      var6.setView(var5);
      var6.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      return var6.create();
   }

   public static AlertDialog createSupportAlert(final BaseFragment var0) {
      if (var0 != null && var0.getParentActivity() != null) {
         TextView var1 = new TextView(var0.getParentActivity());
         SpannableString var2 = new SpannableString(Html.fromHtml(LocaleController.getString("AskAQuestionInfo", 2131558707).replace("\n", "<br>")));
         URLSpan[] var3 = (URLSpan[])var2.getSpans(0, var2.length(), URLSpan.class);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            URLSpan var5 = var3[var4];
            int var6 = var2.getSpanStart(var5);
            int var7 = var2.getSpanEnd(var5);
            var2.removeSpan(var5);
            var2.setSpan(new URLSpanNoUnderline(var5.getURL()) {
               public void onClick(View var1) {
                  var0.dismissCurrentDialig();
                  super.onClick(var1);
               }
            }, var6, var7, 0);
         }

         var1.setText(var2);
         var1.setTextSize(1, 16.0F);
         var1.setLinkTextColor(Theme.getColor("dialogTextLink"));
         var1.setHighlightColor(Theme.getColor("dialogLinkSelection"));
         var1.setPadding(AndroidUtilities.dp(23.0F), 0, AndroidUtilities.dp(23.0F), 0);
         var1.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
         var1.setTextColor(Theme.getColor("dialogTextBlack"));
         AlertDialog.Builder var8 = new AlertDialog.Builder(var0.getParentActivity());
         var8.setView(var1);
         var8.setTitle(LocaleController.getString("AskAQuestion", 2131558706));
         var8.setPositiveButton(LocaleController.getString("AskButton", 2131558708), new _$$Lambda$AlertsCreator$KOZG0pVJG6nMdk1njo_1lCbwC_Q(var0));
         var8.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         return var8.create();
      } else {
         return null;
      }
   }

   public static AlertDialog.Builder createTTLAlert(Context var0, TLRPC.EncryptedChat var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(var0);
      var2.setTitle(LocaleController.getString("MessageLifetime", 2131559846));
      NumberPicker var4 = new NumberPicker(var0);
      var4.setMinValue(0);
      var4.setMaxValue(20);
      int var3 = var1.ttl;
      if (var3 > 0 && var3 < 16) {
         var4.setValue(var3);
      } else {
         var3 = var1.ttl;
         if (var3 == 30) {
            var4.setValue(16);
         } else if (var3 == 60) {
            var4.setValue(17);
         } else if (var3 == 3600) {
            var4.setValue(18);
         } else if (var3 == 86400) {
            var4.setValue(19);
         } else if (var3 == 604800) {
            var4.setValue(20);
         } else if (var3 == 0) {
            var4.setValue(0);
         }
      }

      var4.setFormatter(_$$Lambda$AlertsCreator$vV37v3yPGSQ3dc7MqZN0Qqbwa20.INSTANCE);
      var2.setView(var4);
      var2.setNegativeButton(LocaleController.getString("Done", 2131559299), new _$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g(var1, var4));
      return var2;
   }

   public static Dialog createVibrationSelectDialog(Activity var0, long var1, String var3, Runnable var4) {
      SharedPreferences var6 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
      int[] var7 = new int[1];
      String[] var13;
      if (var1 != 0L) {
         StringBuilder var8 = new StringBuilder();
         var8.append(var3);
         var8.append(var1);
         var7[0] = var6.getInt(var8.toString(), 0);
         if (var7[0] == 3) {
            var7[0] = 2;
         } else if (var7[0] == 2) {
            var7[0] = 3;
         }

         var13 = new String[]{LocaleController.getString("VibrationDefault", 2131561041), LocaleController.getString("Short", 2131560775), LocaleController.getString("Long", 2131559790), LocaleController.getString("VibrationDisabled", 2131561042)};
      } else {
         var7[0] = var6.getInt(var3, 0);
         if (var7[0] == 0) {
            var7[0] = 1;
         } else if (var7[0] == 1) {
            var7[0] = 2;
         } else if (var7[0] == 2) {
            var7[0] = 0;
         }

         var13 = new String[]{LocaleController.getString("VibrationDisabled", 2131561042), LocaleController.getString("VibrationDefault", 2131561041), LocaleController.getString("Short", 2131560775), LocaleController.getString("Long", 2131559790), LocaleController.getString("OnlyIfSilent", 2131560107)};
      }

      LinearLayout var14 = new LinearLayout(var0);
      var14.setOrientation(1);
      AlertDialog.Builder var5 = new AlertDialog.Builder(var0);

      for(int var9 = 0; var9 < var13.length; ++var9) {
         RadioColorCell var10 = new RadioColorCell(var0);
         var10.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
         var10.setTag(var9);
         var10.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
         String var11 = var13[var9];
         boolean var12;
         if (var7[0] == var9) {
            var12 = true;
         } else {
            var12 = false;
         }

         var10.setTextAndValue(var11, var12);
         var14.addView(var10);
         var10.setOnClickListener(new _$$Lambda$AlertsCreator$hGz1nBNKJB7FgFrku7OBrjp5nlM(var7, var1, var3, var5, var4));
      }

      var5.setTitle(LocaleController.getString("Vibrate", 2131561040));
      var5.setView(var14);
      var5.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      return var5.create();
   }

   public static Dialog createVibrationSelectDialog(Activity var0, long var1, boolean var3, boolean var4, Runnable var5) {
      String var6;
      if (var1 != 0L) {
         var6 = "vibrate_";
      } else if (var3) {
         var6 = "vibrate_group";
      } else {
         var6 = "vibrate_messages";
      }

      return createVibrationSelectDialog(var0, var1, var6, var5);
   }

   private static String getFloodWaitString(String var0) {
      int var1 = Utilities.parseInt(var0);
      if (var1 < 60) {
         var0 = LocaleController.formatPluralString("Seconds", var1);
      } else {
         var0 = LocaleController.formatPluralString("Minutes", var1 / 60);
      }

      return LocaleController.formatString("FloodWaitTime", 2131559496, var0);
   }

   // $FF: synthetic method
   static void lambda$createAccountSelectDialog$40(AlertDialog[] var0, Runnable var1, AlertsCreator.AccountSelectDelegate var2, View var3) {
      if (var0[0] != null) {
         var0[0].setOnDismissListener((OnDismissListener)null);
      }

      var1.run();
      var2.didSelectAccount(((AccountSelectCell)var3).getAccountNumber());
   }

   // $FF: synthetic method
   static void lambda$createClearOrDeleteDialogAlert$11(TLRPC.User var0, boolean var1, boolean var2, boolean[] var3, BaseFragment var4, boolean var5, boolean var6, TLRPC.Chat var7, boolean var8, MessagesStorage.BooleanCallback var9, DialogInterface var10, int var11) {
      boolean var12 = false;
      if (var0 != null && !var1 && !var2 && var3[0]) {
         MessagesStorage.getInstance(var4.getCurrentAccount()).getMessagesCount((long)var0.id, new _$$Lambda$AlertsCreator$HtvI_xJz1C_hh9jpL5MD54Y4MBc(var4, var5, var6, var7, var0, var8, var9, var3));
      } else {
         if (var9 != null) {
            label22: {
               if (!var2) {
                  var1 = var12;
                  if (!var3[0]) {
                     break label22;
                  }
               }

               var1 = true;
            }

            var9.run(var1);
         }

      }
   }

   // $FF: synthetic method
   static void lambda$createClearOrDeleteDialogAlert$9(boolean[] var0, View var1) {
      CheckBoxCell var2 = (CheckBoxCell)var1;
      var0[0] ^= true;
      var2.setChecked(var0[0], true);
   }

   // $FF: synthetic method
   static void lambda$createColorSelectDialog$23(LinearLayout var0, int[] var1, View var2) {
      int var3 = var0.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         RadioColorCell var5 = (RadioColorCell)var0.getChildAt(var4);
         boolean var6;
         if (var5 == var2) {
            var6 = true;
         } else {
            var6 = false;
         }

         var5.setChecked(var6, true);
      }

      var1[0] = TextColorCell.colorsToSave[(Integer)var2.getTag()];
   }

   // $FF: synthetic method
   static void lambda$createColorSelectDialog$24(long var0, int[] var2, int var3, Runnable var4, DialogInterface var5, int var6) {
      Editor var7 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      if (var0 != 0L) {
         StringBuilder var8 = new StringBuilder();
         var8.append("color_");
         var8.append(var0);
         var7.putInt(var8.toString(), var2[0]);
      } else if (var3 == 1) {
         var7.putInt("MessagesLed", var2[0]);
      } else if (var3 == 0) {
         var7.putInt("GroupLed", var2[0]);
      } else {
         var7.putInt("ChannelLed", var2[0]);
      }

      var7.commit();
      if (var4 != null) {
         var4.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createColorSelectDialog$25(long var0, int var2, Runnable var3, DialogInterface var4, int var5) {
      Editor var6 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      if (var0 != 0L) {
         StringBuilder var7 = new StringBuilder();
         var7.append("color_");
         var7.append(var0);
         var6.putInt(var7.toString(), 0);
      } else if (var2 == 1) {
         var6.putInt("MessagesLed", 0);
      } else if (var2 == 0) {
         var6.putInt("GroupLed", 0);
      } else {
         var6.putInt("ChannelLed", 0);
      }

      var6.commit();
      if (var3 != null) {
         var3.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createColorSelectDialog$26(long var0, Runnable var2, DialogInterface var3, int var4) {
      Editor var5 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      StringBuilder var6 = new StringBuilder();
      var6.append("color_");
      var6.append(var0);
      var5.remove(var6.toString());
      var5.commit();
      if (var2 != null) {
         var2.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createContactsPermissionDialog$30(MessagesStorage.IntCallback var0, DialogInterface var1, int var2) {
      var0.run(1);
   }

   // $FF: synthetic method
   static void lambda$createContactsPermissionDialog$31(MessagesStorage.IntCallback var0, DialogInterface var1, int var2) {
      var0.run(0);
   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$12(boolean var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, NumberPicker var4, int var5) {
      if (var0 && var5 == 0) {
         checkPickerDate(var1, var2, var3);
      }

   }

   // $FF: synthetic method
   static String lambda$createDatePickerDialog$13(int var0) {
      Calendar var1 = Calendar.getInstance();
      var1.set(5, 1);
      var1.set(2, var0);
      return var1.getDisplayName(2, 1, Locale.getDefault());
   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$14(NumberPicker var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, int var4, int var5) {
      updateDayPicker(var0, var1, var2);
   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$15(boolean var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, NumberPicker var4, int var5) {
      if (var0 && var5 == 0) {
         checkPickerDate(var1, var2, var3);
      }

   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$16(NumberPicker var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, int var4, int var5) {
      updateDayPicker(var0, var1, var2);
   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$17(boolean var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, NumberPicker var4, int var5) {
      if (var0 && var5 == 0) {
         checkPickerDate(var1, var2, var3);
      }

   }

   // $FF: synthetic method
   static void lambda$createDatePickerDialog$18(boolean var0, NumberPicker var1, NumberPicker var2, NumberPicker var3, AlertsCreator.DatePickerDelegate var4, DialogInterface var5, int var6) {
      if (var0) {
         checkPickerDate(var1, var2, var3);
      }

      var4.didSelectDate(var3.getValue(), var2.getValue(), var1.getValue());
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$42(AlertDialog[] var0, BaseFragment var1, TLRPC.User var2, TLRPC.Chat var3, TLRPC.EncryptedChat var4, TLRPC.ChatFull var5, long var6, MessageObject var8, SparseArray[] var9, MessageObject.GroupedMessages var10, Runnable var11, TLObject var12, TLRPC.TL_error var13) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$AlertsCreator$qKrVsguRKMu04xHOh5j6MH_69nE(var0, var12, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11));
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$44(AlertDialog[] var0, int var1, int var2, BaseFragment var3) {
      if (var0[0] != null) {
         var0[0].setOnCancelListener(new _$$Lambda$AlertsCreator$5X2t5ruqXcfyHHT0cc_ZZigoOb8(var1, var2));
         var3.showDialog(var0[0]);
      }
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$45(boolean[] var0, View var1) {
      if (var1.isEnabled()) {
         CheckBoxCell var2 = (CheckBoxCell)var1;
         Integer var3 = (Integer)var2.getTag();
         var0[var3] ^= true;
         var2.setChecked(var0[var3], true);
      }
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$46(boolean[] var0, View var1) {
      CheckBoxCell var2 = (CheckBoxCell)var1;
      var0[0] ^= true;
      var2.setChecked(var0[0], true);
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$47(boolean[] var0, View var1) {
      CheckBoxCell var2 = (CheckBoxCell)var1;
      var0[0] ^= true;
      var2.setChecked(var0[0], true);
   }

   // $FF: synthetic method
   static void lambda$createDeleteMessagesAlert$49(MessageObject var0, MessageObject.GroupedMessages var1, TLRPC.EncryptedChat var2, int var3, boolean[] var4, SparseArray[] var5, TLRPC.User var6, boolean[] var7, TLRPC.Chat var8, TLRPC.ChatFull var9, Runnable var10, DialogInterface var11, int var12) {
      ArrayList var19;
      ArrayList var20;
      if (var0 != null) {
         ArrayList var13 = new ArrayList();
         if (var1 == null) {
            var13.add(var0.getId());
            if (var2 != null && var0.messageOwner.random_id != 0L && var0.type != 10) {
               var20 = new ArrayList();
               var20.add(var0.messageOwner.random_id);
            } else {
               var20 = null;
            }
         } else {
            var12 = 0;

            ArrayList var22;
            ArrayList var23;
            for(var22 = null; var12 < var1.messages.size(); var22 = var23) {
               MessageObject var14 = (MessageObject)var1.messages.get(var12);
               var13.add(var14.getId());
               var23 = var22;
               if (var2 != null) {
                  var23 = var22;
                  if (var14.messageOwner.random_id != 0L) {
                     var23 = var22;
                     if (var14.type != 10) {
                        var23 = var22;
                        if (var22 == null) {
                           var23 = new ArrayList();
                        }

                        var23.add(var14.messageOwner.random_id);
                     }
                  }
               }

               ++var12;
            }

            var20 = var22;
         }

         MessagesController.getInstance(var3).deleteMessages(var13, var20, var2, var0.messageOwner.to_id.channel_id, var4[0]);
         var19 = var13;
      } else {
         var19 = null;

         for(var12 = 1; var12 >= 0; var19 = var20) {
            var20 = new ArrayList();

            int var15;
            for(var15 = 0; var15 < var5[var12].size(); ++var15) {
               var20.add(var5[var12].keyAt(var15));
            }

            label80: {
               if (!var20.isEmpty()) {
                  var15 = ((MessageObject)var5[var12].get((Integer)var20.get(0))).messageOwner.to_id.channel_id;
                  if (var15 != 0) {
                     break label80;
                  }
               }

               var15 = 0;
            }

            if (var2 != null) {
               var19 = new ArrayList();

               for(int var16 = 0; var16 < var5[var12].size(); ++var16) {
                  MessageObject var24 = (MessageObject)var5[var12].valueAt(var16);
                  long var17 = var24.messageOwner.random_id;
                  if (var17 != 0L && var24.type != 10) {
                     var19.add(var17);
                  }
               }
            } else {
               var19 = null;
            }

            MessagesController.getInstance(var3).deleteMessages(var20, var19, var2, var15, var4[0]);
            var5[var12].clear();
            --var12;
         }
      }

      if (var6 != null) {
         if (var7[0]) {
            MessagesController.getInstance(var3).deleteUserFromChat(var8.id, var6, var9);
         }

         if (var7[1]) {
            TLRPC.TL_channels_reportSpam var21 = new TLRPC.TL_channels_reportSpam();
            var21.channel = MessagesController.getInputChannel(var8);
            var21.user_id = MessagesController.getInstance(var3).getInputUser(var6);
            var21.id = var19;
            ConnectionsManager.getInstance(var3).sendRequest(var21, _$$Lambda$AlertsCreator$x9yu8yszpq4SQsRILojlhcHWn4I.INSTANCE);
         }

         if (var7[2]) {
            MessagesController.getInstance(var3).deleteUserChannelHistory(var8, var6, 0);
         }
      }

      if (var10 != null) {
         var10.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createFreeSpaceDialog$32(int[] var0, LinearLayout var1, View var2) {
      int var3 = (Integer)var2.getTag();
      if (var3 == 0) {
         var0[0] = 3;
      } else if (var3 == 1) {
         var0[0] = 0;
      } else if (var3 == 2) {
         var0[0] = 1;
      } else if (var3 == 3) {
         var0[0] = 2;
      }

      int var4 = var1.getChildCount();

      for(var3 = 0; var3 < var4; ++var3) {
         View var7 = var1.getChildAt(var3);
         if (var7 instanceof RadioColorCell) {
            RadioColorCell var5 = (RadioColorCell)var7;
            boolean var6;
            if (var7 == var2) {
               var6 = true;
            } else {
               var6 = false;
            }

            var5.setChecked(var6, true);
         }
      }

   }

   // $FF: synthetic method
   static void lambda$createFreeSpaceDialog$33(int[] var0, DialogInterface var1, int var2) {
      MessagesController.getGlobalMainSettings().edit().putInt("keep_media", var0[0]).commit();
   }

   // $FF: synthetic method
   static void lambda$createFreeSpaceDialog$34(LaunchActivity var0, DialogInterface var1, int var2) {
      var0.presentFragment(new CacheControlActivity());
   }

   // $FF: synthetic method
   static void lambda$createLanguageAlert$1(LaunchActivity var0, DialogInterface var1, int var2) {
      var0.presentFragment(new LanguageSelectActivity());
   }

   // $FF: synthetic method
   static void lambda$createLanguageAlert$2(TLRPC.TL_langPackLanguage var0, LaunchActivity var1, DialogInterface var2, int var3) {
      StringBuilder var5;
      String var6;
      if (var0.official) {
         var5 = new StringBuilder();
         var5.append("remote_");
         var5.append(var0.lang_code);
         var6 = var5.toString();
      } else {
         var5 = new StringBuilder();
         var5.append("unofficial_");
         var5.append(var0.lang_code);
         var6 = var5.toString();
      }

      LocaleController.LocaleInfo var4 = LocaleController.getInstance().getLanguageFromDict(var6);
      LocaleController.LocaleInfo var7 = var4;
      if (var4 == null) {
         var7 = new LocaleController.LocaleInfo();
         var7.name = var0.native_name;
         var7.nameEnglish = var0.name;
         var7.shortName = var0.lang_code;
         var7.baseLangCode = var0.base_lang_code;
         var7.pluralLangCode = var0.plural_code;
         var7.isRtl = var0.rtl;
         if (var0.official) {
            var7.pathToFile = "remote";
         } else {
            var7.pathToFile = "unofficial";
         }
      }

      LocaleController.getInstance().applyLanguage(var7, true, false, false, true, UserConfig.selectedAccount);
      var1.rebuildAllFragments(true);
   }

   // $FF: synthetic method
   static void lambda$createLocationUpdateDialog$28(int[] var0, LinearLayout var1, View var2) {
      var0[0] = (Integer)var2.getTag();
      int var3 = var1.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = var1.getChildAt(var4);
         if (var5 instanceof RadioColorCell) {
            RadioColorCell var7 = (RadioColorCell)var5;
            boolean var6;
            if (var5 == var2) {
               var6 = true;
            } else {
               var6 = false;
            }

            var7.setChecked(var6, true);
         }
      }

   }

   // $FF: synthetic method
   static void lambda$createLocationUpdateDialog$29(int[] var0, MessagesStorage.IntCallback var1, DialogInterface var2, int var3) {
      short var4;
      if (var0[0] == 0) {
         var4 = 900;
      } else if (var0[0] == 1) {
         var4 = 3600;
      } else {
         var4 = 28800;
      }

      var1.run(var4);
   }

   // $FF: synthetic method
   static void lambda$createMuteAlert$19(long var0, DialogInterface var2, int var3) {
      byte var4 = 2;
      byte var5;
      if (var3 == 0) {
         var5 = 0;
      } else if (var3 == 1) {
         var5 = 1;
      } else if (var3 == 2) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(var0, var5);
   }

   // $FF: synthetic method
   static void lambda$createPopupSelectDialog$36(int[] var0, int var1, AlertDialog.Builder var2, Runnable var3, View var4) {
      var0[0] = (Integer)var4.getTag();
      Editor var5 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      if (var1 == 1) {
         var5.putInt("popupAll", var0[0]);
      } else if (var1 == 0) {
         var5.putInt("popupGroup", var0[0]);
      } else {
         var5.putInt("popupChannel", var0[0]);
      }

      var5.commit();
      var2.getDismissRunnable().run();
      if (var3 != null) {
         var3.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createPrioritySelectDialog$35(int[] var0, long var1, int var3, SharedPreferences var4, AlertDialog.Builder var5, Runnable var6, View var7) {
      var0[0] = (Integer)var7.getTag();
      Editor var11 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      byte var8 = 5;
      if (var1 != 0L) {
         int var12 = var0[0];
         byte var10 = 3;
         if (var12 != 0) {
            if (var0[0] == 1) {
               var10 = 4;
            } else if (var0[0] == 2) {
               var10 = 5;
            } else if (var0[0] == 3) {
               var10 = 0;
            } else {
               var10 = 1;
            }
         }

         StringBuilder var9 = new StringBuilder();
         var9.append("priority_");
         var9.append(var1);
         var11.putInt(var9.toString(), var10);
      } else {
         if (var0[0] == 0) {
            var8 = 4;
         } else if (var0[0] != 1) {
            if (var0[0] == 2) {
               var8 = 0;
            } else {
               var8 = 1;
            }
         }

         if (var3 == 1) {
            var11.putInt("priority_messages", var8);
            var0[0] = var4.getInt("priority_messages", 1);
         } else if (var3 == 0) {
            var11.putInt("priority_group", var8);
            var0[0] = var4.getInt("priority_group", 1);
         } else if (var3 == 2) {
            var11.putInt("priority_channel", var8);
            var0[0] = var4.getInt("priority_channel", 1);
         }
      }

      var11.commit();
      var5.getDismissRunnable().run();
      if (var6 != null) {
         var6.run();
      }

   }

   // $FF: synthetic method
   static void lambda$createReportAlert$21(long var0, int var2, BaseFragment var3, Context var4, DialogInterface var5, int var6) {
      if (var6 == 4) {
         Bundle var9 = new Bundle();
         var9.putLong("dialog_id", var0);
         var9.putLong("message_id", (long)var2);
         var3.presentFragment(new ReportOtherActivity(var9));
      } else {
         TLRPC.InputPeer var7 = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int)var0);
         Object var8;
         if (var2 != 0) {
            TLRPC.TL_messages_report var10 = new TLRPC.TL_messages_report();
            var10.peer = var7;
            var10.id.add(var2);
            if (var6 == 0) {
               var10.reason = new TLRPC.TL_inputReportReasonSpam();
               var8 = var10;
            } else if (var6 == 1) {
               var10.reason = new TLRPC.TL_inputReportReasonViolence();
               var8 = var10;
            } else if (var6 == 2) {
               var10.reason = new TLRPC.TL_inputReportReasonChildAbuse();
               var8 = var10;
            } else {
               var8 = var10;
               if (var6 == 3) {
                  var10.reason = new TLRPC.TL_inputReportReasonPornography();
                  var8 = var10;
               }
            }
         } else {
            TLRPC.TL_account_reportPeer var11 = new TLRPC.TL_account_reportPeer();
            var11.peer = var7;
            if (var6 == 0) {
               var11.reason = new TLRPC.TL_inputReportReasonSpam();
               var8 = var11;
            } else if (var6 == 1) {
               var11.reason = new TLRPC.TL_inputReportReasonViolence();
               var8 = var11;
            } else if (var6 == 2) {
               var11.reason = new TLRPC.TL_inputReportReasonChildAbuse();
               var8 = var11;
            } else {
               var8 = var11;
               if (var6 == 3) {
                  var11.reason = new TLRPC.TL_inputReportReasonPornography();
                  var8 = var11;
               }
            }
         }

         ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest((TLObject)var8, _$$Lambda$AlertsCreator$4L4L25hz_aafbABoeRNXpU6nEL0.INSTANCE);
         Toast.makeText(var4, LocaleController.getString("ReportChatSent", 2131560573), 0).show();
      }
   }

   // $FF: synthetic method
   static void lambda$createSingleChoiceDialog$37(AlertDialog.Builder var0, OnClickListener var1, View var2) {
      int var3 = (Integer)var2.getTag();
      var0.getDismissRunnable().run();
      var1.onClick((DialogInterface)null, var3);
   }

   // $FF: synthetic method
   static void lambda$createSupportAlert$5(BaseFragment var0, DialogInterface var1, int var2) {
      performAskAQuestion(var0);
   }

   // $FF: synthetic method
   static String lambda$createTTLAlert$38(int var0) {
      if (var0 == 0) {
         return LocaleController.getString("ShortMessageLifetimeForever", 2131560776);
      } else if (var0 >= 1 && var0 < 16) {
         return LocaleController.formatTTLString(var0);
      } else if (var0 == 16) {
         return LocaleController.formatTTLString(30);
      } else if (var0 == 17) {
         return LocaleController.formatTTLString(60);
      } else if (var0 == 18) {
         return LocaleController.formatTTLString(3600);
      } else if (var0 == 19) {
         return LocaleController.formatTTLString(86400);
      } else {
         return var0 == 20 ? LocaleController.formatTTLString(604800) : "";
      }
   }

   // $FF: synthetic method
   static void lambda$createTTLAlert$39(TLRPC.EncryptedChat var0, NumberPicker var1, DialogInterface var2, int var3) {
      var3 = var0.ttl;
      int var4 = var1.getValue();
      if (var4 >= 0 && var4 < 16) {
         var0.ttl = var4;
      } else if (var4 == 16) {
         var0.ttl = 30;
      } else if (var4 == 17) {
         var0.ttl = 60;
      } else if (var4 == 18) {
         var0.ttl = 3600;
      } else if (var4 == 19) {
         var0.ttl = 86400;
      } else if (var4 == 20) {
         var0.ttl = 604800;
      }

      if (var3 != var0.ttl) {
         SecretChatHelper.getInstance(UserConfig.selectedAccount).sendTTLMessage(var0, (TLRPC.Message)null);
         MessagesStorage.getInstance(UserConfig.selectedAccount).updateEncryptedChatTTL(var0);
      }

   }

   // $FF: synthetic method
   static void lambda$createVibrationSelectDialog$27(int[] var0, long var1, String var3, AlertDialog.Builder var4, Runnable var5, View var6) {
      var0[0] = (Integer)var6.getTag();
      Editor var8 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
      if (var1 != 0L) {
         StringBuilder var7;
         if (var0[0] == 0) {
            var7 = new StringBuilder();
            var7.append(var3);
            var7.append(var1);
            var8.putInt(var7.toString(), 0);
         } else if (var0[0] == 1) {
            var7 = new StringBuilder();
            var7.append(var3);
            var7.append(var1);
            var8.putInt(var7.toString(), 1);
         } else if (var0[0] == 2) {
            var7 = new StringBuilder();
            var7.append(var3);
            var7.append(var1);
            var8.putInt(var7.toString(), 3);
         } else if (var0[0] == 3) {
            var7 = new StringBuilder();
            var7.append(var3);
            var7.append(var1);
            var8.putInt(var7.toString(), 2);
         }
      } else if (var0[0] == 0) {
         var8.putInt(var3, 2);
      } else if (var0[0] == 1) {
         var8.putInt(var3, 0);
      } else if (var0[0] == 2) {
         var8.putInt(var3, 1);
      } else if (var0[0] == 3) {
         var8.putInt(var3, 3);
      } else if (var0[0] == 4) {
         var8.putInt(var3, 4);
      }

      var8.commit();
      var4.getDismissRunnable().run();
      if (var5 != null) {
         var5.run();
      }

   }

   // $FF: synthetic method
   static void lambda$null$10(BaseFragment var0, boolean var1, boolean var2, TLRPC.Chat var3, TLRPC.User var4, boolean var5, MessagesStorage.BooleanCallback var6, boolean[] var7, int var8) {
      if (var8 >= 50) {
         createClearOrDeleteDialogAlert(var0, var1, var2, true, var3, var4, var5, var6);
      } else if (var6 != null) {
         var6.run(var7[0]);
      }

   }

   // $FF: synthetic method
   static void lambda$null$20(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$null$41(AlertDialog[] var0, TLObject var1, BaseFragment var2, TLRPC.User var3, TLRPC.Chat var4, TLRPC.EncryptedChat var5, TLRPC.ChatFull var6, long var7, MessageObject var9, SparseArray[] var10, MessageObject.GroupedMessages var11, Runnable var12) {
      try {
         var0[0].dismiss();
      } catch (Throwable var15) {
      }

      byte var13;
      label19: {
         var0[0] = null;
         if (var1 != null) {
            TLRPC.ChannelParticipant var16 = ((TLRPC.TL_channels_channelParticipant)var1).participant;
            if (!(var16 instanceof TLRPC.TL_channelParticipantAdmin) && !(var16 instanceof TLRPC.TL_channelParticipantCreator)) {
               var13 = 0;
               break label19;
            }
         }

         var13 = 2;
      }

      createDeleteMessagesAlert(var2, var3, var4, var5, var6, var7, var9, var10, var11, var13, var12);
   }

   // $FF: synthetic method
   static void lambda$null$43(int var0, int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(var0).cancelRequest(var1, true);
   }

   // $FF: synthetic method
   static void lambda$null$48(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$null$6(SharedPreferences var0, TLRPC.TL_help_support var1, AlertDialog var2, int var3, BaseFragment var4) {
      Editor var5 = var0.edit();
      var5.putInt("support_id", var1.user.id);
      SerializedData var7 = new SerializedData();
      var1.user.serializeToStream(var7);
      var5.putString("support_user", Base64.encodeToString(var7.toByteArray(), 0));
      var5.commit();
      var7.cleanup();

      try {
         var2.dismiss();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      ArrayList var8 = new ArrayList();
      var8.add(var1.user);
      MessagesStorage.getInstance(var3).putUsersAndChats(var8, (ArrayList)null, true, true);
      MessagesController.getInstance(var3).putUser(var1.user, false);
      Bundle var9 = new Bundle();
      var9.putInt("user_id", var1.user.id);
      var4.presentFragment(new ChatActivity(var9));
   }

   // $FF: synthetic method
   static void lambda$null$7(AlertDialog var0) {
      try {
         var0.dismiss();
      } catch (Exception var1) {
         FileLog.e((Throwable)var1);
      }

   }

   // $FF: synthetic method
   static void lambda$performAskAQuestion$8(SharedPreferences var0, AlertDialog var1, int var2, BaseFragment var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$AlertsCreator$M0pJapDgZt4nkaop5tg0yx7_Iys(var0, (TLRPC.TL_help_support)var4, var1, var2, var3));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$AlertsCreator$XCUL6c7pryEJnEpg_lg3gFCPTMw(var1));
      }

   }

   // $FF: synthetic method
   static void lambda$showAddUserAlert$22(BaseFragment var0, DialogInterface var1, int var2) {
      MessagesController.getInstance(var0.getCurrentAccount()).openByUserName("spambot", var0, 1);
   }

   // $FF: synthetic method
   static void lambda$showCustomNotificationsDialog$3(long var0, int var2, boolean var3, MessagesStorage.IntCallback var4, int var5, BaseFragment var6, ArrayList var7, MessagesStorage.IntCallback var8, AlertDialog.Builder var9, View var10) {
      int var11 = (Integer)var10.getTag();
      Editor var16;
      TLRPC.Dialog var17;
      StringBuilder var18;
      if (var11 == 0) {
         if (var0 != 0L) {
            var16 = MessagesController.getNotificationsSettings(var2).edit();
            if (var3) {
               var18 = new StringBuilder();
               var18.append("notify2_");
               var18.append(var0);
               var16.remove(var18.toString());
            } else {
               var18 = new StringBuilder();
               var18.append("notify2_");
               var18.append(var0);
               var16.putInt(var18.toString(), 0);
            }

            MessagesStorage.getInstance(var2).setDialogFlags(var0, 0L);
            var16.commit();
            var17 = (TLRPC.Dialog)MessagesController.getInstance(var2).dialogs_dict.get(var0);
            if (var17 != null) {
               var17.notify_settings = new TLRPC.TL_peerNotifySettings();
            }

            NotificationsController.getInstance(var2).updateServerNotificationsSettings(var0);
            if (var4 != null) {
               if (var3) {
                  var4.run(0);
               } else {
                  var4.run(1);
               }
            }
         } else {
            NotificationsController.getInstance(var2).setGlobalNotificationsEnabled(var5, 0);
         }
      } else if (var11 == 3) {
         if (var0 != 0L) {
            Bundle var15 = new Bundle();
            var15.putLong("dialog_id", var0);
            var6.presentFragment(new ProfileNotificationsActivity(var15));
         } else {
            var6.presentFragment(new NotificationsCustomSettingsActivity(var5, var7));
         }
      } else {
         int var12 = ConnectionsManager.getInstance(var2).getCurrentTime();
         if (var11 == 1) {
            var12 += 3600;
         } else if (var11 == 2) {
            var12 += 172800;
         } else if (var11 == 4) {
            var12 = Integer.MAX_VALUE;
         }

         if (var0 != 0L) {
            var16 = MessagesController.getNotificationsSettings(var2).edit();
            long var13;
            if (var11 == 4) {
               if (!var3) {
                  var18 = new StringBuilder();
                  var18.append("notify2_");
                  var18.append(var0);
                  var16.remove(var18.toString());
                  var13 = 0L;
               } else {
                  var18 = new StringBuilder();
                  var18.append("notify2_");
                  var18.append(var0);
                  var16.putInt(var18.toString(), 2);
                  var13 = 1L;
               }
            } else {
               var18 = new StringBuilder();
               var18.append("notify2_");
               var18.append(var0);
               var16.putInt(var18.toString(), 3);
               var18 = new StringBuilder();
               var18.append("notifyuntil_");
               var18.append(var0);
               var16.putInt(var18.toString(), var12);
               var13 = (long)var12 << 32 | 1L;
            }

            NotificationsController.getInstance(var2).removeNotificationsForDialog(var0);
            MessagesStorage.getInstance(var2).setDialogFlags(var0, var13);
            var16.commit();
            var17 = (TLRPC.Dialog)MessagesController.getInstance(var2).dialogs_dict.get(var0);
            if (var17 != null) {
               var17.notify_settings = new TLRPC.TL_peerNotifySettings();
               if (var11 != 4 || var3) {
                  var17.notify_settings.mute_until = var12;
               }
            }

            NotificationsController.getInstance(var2).updateServerNotificationsSettings(var0);
            if (var4 != null) {
               if (var11 == 4 && !var3) {
                  var4.run(0);
               } else {
                  var4.run(1);
               }
            }
         } else if (var11 == 4) {
            NotificationsController.getInstance(var2).setGlobalNotificationsEnabled(var5, Integer.MAX_VALUE);
         } else {
            NotificationsController.getInstance(var2).setGlobalNotificationsEnabled(var5, var12);
         }
      }

      if (var8 != null) {
         var8.run(var11);
      }

      var9.getDismissRunnable().run();
   }

   // $FF: synthetic method
   static void lambda$showSecretLocationAlert$4(Runnable var0, DialogInterface var1, int var2) {
      SharedConfig.setSecretMapPreviewType(var2);
      if (var0 != null) {
         var0.run();
      }

   }

   // $FF: synthetic method
   static void lambda$showUpdateAppAlert$0(Context var0, DialogInterface var1, int var2) {
      Browser.openUrl(var0, BuildVars.PLAYSTORE_APP_URL);
   }

   private static void performAskAQuestion(BaseFragment var0) {
      int var1 = var0.getCurrentAccount();
      SharedPreferences var2 = MessagesController.getMainSettings(var1);
      int var3 = var2.getInt("support_id", 0);
      AlertDialog var4 = null;
      TLRPC.User var5 = var4;
      if (var3 != 0) {
         var5 = MessagesController.getInstance(var1).getUser(var3);
         if (var5 == null) {
            String var6 = var2.getString("support_user", (String)null);
            if (var6 != null) {
               label51: {
                  Exception var10000;
                  label57: {
                     byte[] var13;
                     boolean var10001;
                     try {
                        var13 = Base64.decode(var6, 0);
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label57;
                     }

                     if (var13 == null) {
                        break label51;
                     }

                     SerializedData var7;
                     TLRPC.User var15;
                     try {
                        var7 = new SerializedData(var13);
                        var15 = TLRPC.User.TLdeserialize(var7, var7.readInt32(false), false);
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label57;
                     }

                     var5 = var15;
                     if (var15 != null) {
                        label58: {
                           var5 = var15;

                           try {
                              if (var15.id != 333000) {
                                 break label58;
                              }
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                              break label57;
                           }

                           var5 = null;
                        }
                     }

                     try {
                        var7.cleanup();
                        break label51;
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }

                  Exception var14 = var10000;
                  FileLog.e((Throwable)var14);
                  var5 = var4;
               }
            }
         }
      }

      if (var5 == null) {
         var4 = new AlertDialog(var0.getParentActivity(), 3);
         var4.setCanCacnel(false);
         var4.show();
         TLRPC.TL_help_getSupport var16 = new TLRPC.TL_help_getSupport();
         ConnectionsManager.getInstance(var1).sendRequest(var16, new _$$Lambda$AlertsCreator$vTt7GpWddJNaVuBDzHfJsdiKdl4(var2, var4, var1, var0));
      } else {
         MessagesController.getInstance(var1).putUser(var5, true);
         Bundle var12 = new Bundle();
         var12.putInt("user_id", var5.id);
         var0.presentFragment(new ChatActivity(var12));
      }

   }

   public static Dialog processError(int var0, TLRPC.TL_error var1, BaseFragment var2, TLObject var3, Object... var4) {
      int var5 = var1.code;
      if (var5 != 406) {
         String var6 = var1.text;
         if (var6 != null) {
            StringBuilder var9;
            if (!(var3 instanceof TLRPC.TL_account_saveSecureValue) && !(var3 instanceof TLRPC.TL_account_getAuthorizationForm)) {
               if (!(var3 instanceof TLRPC.TL_channels_joinChannel) && !(var3 instanceof TLRPC.TL_channels_editAdmin) && !(var3 instanceof TLRPC.TL_channels_inviteToChannel) && !(var3 instanceof TLRPC.TL_messages_addChatUser) && !(var3 instanceof TLRPC.TL_messages_startBot) && !(var3 instanceof TLRPC.TL_channels_editBanned) && !(var3 instanceof TLRPC.TL_messages_editChatDefaultBannedRights) && !(var3 instanceof TLRPC.TL_messages_editChatAdmin)) {
                  if (var3 instanceof TLRPC.TL_messages_createChat) {
                     if (var6.startsWith("FLOOD_WAIT")) {
                        showFloodWaitAlert(var1.text, var2);
                     } else {
                        showAddUserAlert(var1.text, var2, false);
                     }
                  } else if (var3 instanceof TLRPC.TL_channels_createChannel) {
                     if (var6.startsWith("FLOOD_WAIT")) {
                        showFloodWaitAlert(var1.text, var2);
                     } else {
                        showAddUserAlert(var1.text, var2, false);
                     }
                  } else if (var3 instanceof TLRPC.TL_messages_editMessage) {
                     if (!var6.equals("MESSAGE_NOT_MODIFIED")) {
                        if (var2 != null) {
                           showSimpleAlert(var2, LocaleController.getString("EditMessageError", 2131559324));
                        } else {
                           showSimpleToast(var2, LocaleController.getString("EditMessageError", 2131559324));
                        }
                     }
                  } else if (!(var3 instanceof TLRPC.TL_messages_sendMessage) && !(var3 instanceof TLRPC.TL_messages_sendMedia) && !(var3 instanceof TLRPC.TL_messages_sendBroadcast) && !(var3 instanceof TLRPC.TL_messages_sendInlineBotResult) && !(var3 instanceof TLRPC.TL_messages_forwardMessages) && !(var3 instanceof TLRPC.TL_messages_sendMultiMedia)) {
                     if (var3 instanceof TLRPC.TL_messages_importChatInvite) {
                        if (var6.startsWith("FLOOD_WAIT")) {
                           showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                        } else if (var1.text.equals("USERS_TOO_MUCH")) {
                           showSimpleAlert(var2, LocaleController.getString("JoinToGroupErrorFull", 2131559704));
                        } else {
                           showSimpleAlert(var2, LocaleController.getString("JoinToGroupErrorNotExist", 2131559705));
                        }
                     } else if (var3 instanceof TLRPC.TL_messages_getAttachedStickers) {
                        if (var2 != null && var2.getParentActivity() != null) {
                           Activity var10 = var2.getParentActivity();
                           StringBuilder var8 = new StringBuilder();
                           var8.append(LocaleController.getString("ErrorOccurred", 2131559375));
                           var8.append("\n");
                           var8.append(var1.text);
                           Toast.makeText(var10, var8.toString(), 0).show();
                        }
                     } else {
                        if (var3 instanceof TLRPC.TL_account_confirmPhone || var3 instanceof TLRPC.TL_account_verifyPhone || var3 instanceof TLRPC.TL_account_verifyEmail) {
                           if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID") && !var1.text.contains("CODE_INVALID") && !var1.text.contains("CODE_EMPTY")) {
                              if (!var1.text.contains("PHONE_CODE_EXPIRED") && !var1.text.contains("EMAIL_VERIFY_EXPIRED")) {
                                 if (var1.text.startsWith("FLOOD_WAIT")) {
                                    return showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                                 }

                                 return showSimpleAlert(var2, var1.text);
                              }

                              return showSimpleAlert(var2, LocaleController.getString("CodeExpired", 2131559120));
                           }

                           return showSimpleAlert(var2, LocaleController.getString("InvalidCode", 2131559671));
                        }

                        if (var3 instanceof TLRPC.TL_auth_resendCode) {
                           if (var6.contains("PHONE_NUMBER_INVALID")) {
                              return showSimpleAlert(var2, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                           }

                           if (var1.text.contains("PHONE_CODE_EMPTY") || var1.text.contains("PHONE_CODE_INVALID")) {
                              return showSimpleAlert(var2, LocaleController.getString("InvalidCode", 2131559671));
                           }

                           if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                              return showSimpleAlert(var2, LocaleController.getString("CodeExpired", 2131559120));
                           }

                           if (var1.text.startsWith("FLOOD_WAIT")) {
                              return showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                           }

                           if (var1.code != -1000) {
                              var9 = new StringBuilder();
                              var9.append(LocaleController.getString("ErrorOccurred", 2131559375));
                              var9.append("\n");
                              var9.append(var1.text);
                              return showSimpleAlert(var2, var9.toString());
                           }
                        } else if (var3 instanceof TLRPC.TL_account_sendConfirmPhoneCode) {
                           if (var5 == 400) {
                              return showSimpleAlert(var2, LocaleController.getString("CancelLinkExpired", 2131558895));
                           }

                           if (var6 != null) {
                              if (var6.startsWith("FLOOD_WAIT")) {
                                 return showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                              }

                              return showSimpleAlert(var2, LocaleController.getString("ErrorOccurred", 2131559375));
                           }
                        } else if (var3 instanceof TLRPC.TL_account_changePhone) {
                           if (var6.contains("PHONE_NUMBER_INVALID")) {
                              showSimpleAlert(var2, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                           } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
                              if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                                 showSimpleAlert(var2, LocaleController.getString("CodeExpired", 2131559120));
                              } else if (var1.text.startsWith("FLOOD_WAIT")) {
                                 showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                              } else {
                                 showSimpleAlert(var2, var1.text);
                              }
                           } else {
                              showSimpleAlert(var2, LocaleController.getString("InvalidCode", 2131559671));
                           }
                        } else if (var3 instanceof TLRPC.TL_account_sendChangePhoneCode) {
                           if (var6.contains("PHONE_NUMBER_INVALID")) {
                              showSimpleAlert(var2, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                           } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
                              if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                                 showSimpleAlert(var2, LocaleController.getString("CodeExpired", 2131559120));
                              } else if (var1.text.startsWith("FLOOD_WAIT")) {
                                 showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                              } else if (var1.text.startsWith("PHONE_NUMBER_OCCUPIED")) {
                                 showSimpleAlert(var2, LocaleController.formatString("ChangePhoneNumberOccupied", 2131558915, (String)var4[0]));
                              } else {
                                 showSimpleAlert(var2, LocaleController.getString("ErrorOccurred", 2131559375));
                              }
                           } else {
                              showSimpleAlert(var2, LocaleController.getString("InvalidCode", 2131559671));
                           }
                        } else {
                           byte var7;
                           if (var3 instanceof TLRPC.TL_updateUserName) {
                              var7 = -1;
                              var5 = var6.hashCode();
                              if (var5 != 288843630) {
                                 if (var5 == 533175271 && var6.equals("USERNAME_OCCUPIED")) {
                                    var7 = 1;
                                 }
                              } else if (var6.equals("USERNAME_INVALID")) {
                                 var7 = 0;
                              }

                              if (var7 != 0) {
                                 if (var7 != 1) {
                                    showSimpleAlert(var2, LocaleController.getString("ErrorOccurred", 2131559375));
                                 } else {
                                    showSimpleAlert(var2, LocaleController.getString("UsernameInUse", 2131561027));
                                 }
                              } else {
                                 showSimpleAlert(var2, LocaleController.getString("UsernameInvalid", 2131561028));
                              }
                           } else if (var3 instanceof TLRPC.TL_contacts_importContacts) {
                              if (var1 != null && !var6.startsWith("FLOOD_WAIT")) {
                                 var9 = new StringBuilder();
                                 var9.append(LocaleController.getString("ErrorOccurred", 2131559375));
                                 var9.append("\n");
                                 var9.append(var1.text);
                                 showSimpleAlert(var2, var9.toString());
                              } else {
                                 showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
                              }
                           } else if (!(var3 instanceof TLRPC.TL_account_getPassword) && !(var3 instanceof TLRPC.TL_account_getTmpPassword)) {
                              if (var3 instanceof TLRPC.TL_payments_sendPaymentForm) {
                                 var7 = -1;
                                 var5 = var6.hashCode();
                                 if (var5 != -1144062453) {
                                    if (var5 == -784238410 && var6.equals("PAYMENT_FAILED")) {
                                       var7 = 1;
                                    }
                                 } else if (var6.equals("BOT_PRECHECKOUT_FAILED")) {
                                    var7 = 0;
                                 }

                                 if (var7 != 0) {
                                    if (var7 != 1) {
                                       showSimpleToast(var2, var1.text);
                                    } else {
                                       showSimpleToast(var2, LocaleController.getString("PaymentFailed", 2131560374));
                                    }
                                 } else {
                                    showSimpleToast(var2, LocaleController.getString("PaymentPrecheckoutFailed", 2131560387));
                                 }
                              } else if (var3 instanceof TLRPC.TL_payments_validateRequestedInfo) {
                                 var7 = -1;
                                 if (var6.hashCode() == 1758025548 && var6.equals("SHIPPING_NOT_AVAILABLE")) {
                                    var7 = 0;
                                 }

                                 if (var7 != 0) {
                                    showSimpleToast(var2, var1.text);
                                 } else {
                                    showSimpleToast(var2, LocaleController.getString("PaymentNoShippingMethod", 2131560376));
                                 }
                              }
                           } else if (var1.text.startsWith("FLOOD_WAIT")) {
                              showSimpleToast(var2, getFloodWaitString(var1.text));
                           } else {
                              showSimpleToast(var2, var1.text);
                           }
                        }
                     }
                  } else if (var1.text.equals("PEER_FLOOD")) {
                     NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.needShowAlert, 0);
                  } else if (var1.text.equals("USER_BANNED_IN_CHANNEL")) {
                     NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.needShowAlert, 5);
                  }
               } else if (var2 != null) {
                  showAddUserAlert(var1.text, var2, (Boolean)var4[0]);
               } else if (var1.text.equals("PEER_FLOOD")) {
                  NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.needShowAlert, 1);
               }
            } else if (var1.text.contains("PHONE_NUMBER_INVALID")) {
               showSimpleAlert(var2, LocaleController.getString("InvalidPhoneNumber", 2131559674));
            } else if (var1.text.startsWith("FLOOD_WAIT")) {
               showSimpleAlert(var2, LocaleController.getString("FloodWait", 2131559495));
            } else if ("APP_VERSION_OUTDATED".equals(var1.text)) {
               showUpdateAppAlert(var2.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
            } else {
               var9 = new StringBuilder();
               var9.append(LocaleController.getString("ErrorOccurred", 2131559375));
               var9.append("\n");
               var9.append(var1.text);
               showSimpleAlert(var2, var9.toString());
            }

            return null;
         }
      }

      return null;
   }

   public static void showAddUserAlert(String var0, BaseFragment var1, boolean var2) {
      if (var0 != null && var1 != null && var1.getParentActivity() != null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(var1.getParentActivity());
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         byte var4 = -1;
         switch(var0.hashCode()) {
         case -1763467626:
            if (var0.equals("USERS_TOO_FEW")) {
               var4 = 9;
            }
            break;
         case -538116776:
            if (var0.equals("USER_BLOCKED")) {
               var4 = 1;
            }
            break;
         case -512775857:
            if (var0.equals("USER_RESTRICTED")) {
               var4 = 10;
            }
            break;
         case -454039871:
            if (var0.equals("PEER_FLOOD")) {
               var4 = 0;
            }
            break;
         case -420079733:
            if (var0.equals("BOTS_TOO_MUCH")) {
               var4 = 7;
            }
            break;
         case 98635865:
            if (var0.equals("USER_KICKED")) {
               var4 = 13;
            }
            break;
         case 517420851:
            if (var0.equals("USER_BOT")) {
               var4 = 2;
            }
            break;
         case 845559454:
            if (var0.equals("YOU_BLOCKED_USER")) {
               var4 = 11;
            }
            break;
         case 916342611:
            if (var0.equals("USER_ADMIN_INVALID")) {
               var4 = 15;
            }
            break;
         case 1047173446:
            if (var0.equals("CHAT_ADMIN_BAN_REQUIRED")) {
               var4 = 12;
            }
            break;
         case 1167301807:
            if (var0.equals("USERS_TOO_MUCH")) {
               var4 = 4;
            }
            break;
         case 1227003815:
            if (var0.equals("USER_ID_INVALID")) {
               var4 = 3;
            }
            break;
         case 1253103379:
            if (var0.equals("ADMINS_TOO_MUCH")) {
               var4 = 6;
            }
            break;
         case 1623167701:
            if (var0.equals("USER_NOT_MUTUAL_CONTACT")) {
               var4 = 5;
            }
            break;
         case 1754587486:
            if (var0.equals("CHAT_ADMIN_INVITE_REQUIRED")) {
               var4 = 14;
            }
            break;
         case 1916725894:
            if (var0.equals("USER_PRIVACY_RESTRICTED")) {
               var4 = 8;
            }
         }

         switch(var4) {
         case 0:
            var3.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131559958));
            var3.setNegativeButton(LocaleController.getString("MoreInfo", 2131559883), new _$$Lambda$AlertsCreator$6u18oE86Tvr4pJTZ_ZJYwdHJU6U(var1));
            break;
         case 1:
         case 2:
         case 3:
            if (var2) {
               var3.setMessage(LocaleController.getString("ChannelUserCantAdd", 2131559009));
            } else {
               var3.setMessage(LocaleController.getString("GroupUserCantAdd", 2131559620));
            }
            break;
         case 4:
            if (var2) {
               var3.setMessage(LocaleController.getString("ChannelUserAddLimit", 2131559008));
            } else {
               var3.setMessage(LocaleController.getString("GroupUserAddLimit", 2131559619));
            }
            break;
         case 5:
            if (var2) {
               var3.setMessage(LocaleController.getString("ChannelUserLeftError", 2131559012));
            } else {
               var3.setMessage(LocaleController.getString("GroupUserLeftError", 2131559623));
            }
            break;
         case 6:
            if (var2) {
               var3.setMessage(LocaleController.getString("ChannelUserCantAdmin", 2131559010));
            } else {
               var3.setMessage(LocaleController.getString("GroupUserCantAdmin", 2131559621));
            }
            break;
         case 7:
            if (var2) {
               var3.setMessage(LocaleController.getString("ChannelUserCantBot", 2131559011));
            } else {
               var3.setMessage(LocaleController.getString("GroupUserCantBot", 2131559622));
            }
            break;
         case 8:
            if (var2) {
               var3.setMessage(LocaleController.getString("InviteToChannelError", 2131559687));
            } else {
               var3.setMessage(LocaleController.getString("InviteToGroupError", 2131559689));
            }
            break;
         case 9:
            var3.setMessage(LocaleController.getString("CreateGroupError", 2131559168));
            break;
         case 10:
            var3.setMessage(LocaleController.getString("UserRestricted", 2131560993));
            break;
         case 11:
            var3.setMessage(LocaleController.getString("YouBlockedUser", 2131561137));
            break;
         case 12:
         case 13:
            var3.setMessage(LocaleController.getString("AddAdminErrorBlacklisted", 2131558557));
            break;
         case 14:
            var3.setMessage(LocaleController.getString("AddAdminErrorNotAMember", 2131558558));
            break;
         case 15:
            var3.setMessage(LocaleController.getString("AddBannedErrorAdmin", 2131558564));
            break;
         default:
            StringBuilder var5 = new StringBuilder();
            var5.append(LocaleController.getString("ErrorOccurred", 2131559375));
            var5.append("\n");
            var5.append(var0);
            var3.setMessage(var5.toString());
         }

         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var1.showDialog(var3.create(), true, (OnDismissListener)null);
      }

   }

   public static void showCustomNotificationsDialog(BaseFragment var0, long var1, int var3, ArrayList var4, int var5, MessagesStorage.IntCallback var6) {
      showCustomNotificationsDialog(var0, var1, var3, var4, var5, var6, (MessagesStorage.IntCallback)null);
   }

   public static void showCustomNotificationsDialog(BaseFragment var0, long var1, int var3, ArrayList var4, int var5, MessagesStorage.IntCallback var6, MessagesStorage.IntCallback var7) {
      if (var0 != null && var0.getParentActivity() != null) {
         boolean var8 = NotificationsController.getInstance(var5).isGlobalNotificationsEnabled(var1);
         String[] var9 = new String[]{LocaleController.getString("NotificationsTurnOn", 2131560093), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 1)), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Days", 2)), null, null};
         Object var10 = null;
         String var11;
         if (var1 == 0L && var0 instanceof NotificationsCustomSettingsActivity) {
            var11 = null;
         } else {
            var11 = LocaleController.getString("NotificationsCustomize", 2131560060);
         }

         var9[3] = var11;
         var9[4] = LocaleController.getString("NotificationsTurnOff", 2131560092);
         int[] var12 = new int[]{2131165713, 2131165710, 2131165711, 2131165714, 2131165712};
         LinearLayout var13 = new LinearLayout(var0.getParentActivity());
         var13.setOrientation(1);
         AlertDialog.Builder var14 = new AlertDialog.Builder(var0.getParentActivity());
         int var15 = 0;
         var11 = (String)var10;

         for(int[] var17 = var12; var15 < var9.length; ++var15) {
            if (var9[var15] != null) {
               TextView var16 = new TextView(var0.getParentActivity());
               Drawable var18 = var0.getParentActivity().getResources().getDrawable(var17[var15]);
               if (var15 == var9.length - 1) {
                  var16.setTextColor(Theme.getColor("dialogTextRed"));
                  var18.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogRedIcon"), Mode.MULTIPLY));
               } else {
                  var16.setTextColor(Theme.getColor("dialogTextBlack"));
                  var18.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogIcon"), Mode.MULTIPLY));
               }

               var16.setTextSize(1, 16.0F);
               var16.setLines(1);
               var16.setMaxLines(1);
               var16.setCompoundDrawablesWithIntrinsicBounds(var18, var11, var11, var11);
               var16.setTag(var15);
               var16.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var16.setPadding(AndroidUtilities.dp(24.0F), 0, AndroidUtilities.dp(24.0F), 0);
               var16.setSingleLine(true);
               var16.setGravity(19);
               var16.setCompoundDrawablePadding(AndroidUtilities.dp(26.0F));
               var16.setText(var9[var15]);
               var13.addView(var16, LayoutHelper.createLinear(-1, 48, 51));
               var16.setOnClickListener(new _$$Lambda$AlertsCreator$Z4kCfZq2Csg4JqBOo8whPkThoNo(var1, var5, var8, var7, var3, var0, var4, var6, var14));
            }
         }

         var14.setTitle(LocaleController.getString("Notifications", 2131560055));
         var14.setView(var13);
         var0.showDialog(var14.create());
      }

   }

   public static void showFloodWaitAlert(String var0, BaseFragment var1) {
      if (var0 != null && var0.startsWith("FLOOD_WAIT") && var1 != null && var1.getParentActivity() != null) {
         int var2 = Utilities.parseInt(var0);
         if (var2 < 60) {
            var0 = LocaleController.formatPluralString("Seconds", var2);
         } else {
            var0 = LocaleController.formatPluralString("Minutes", var2 / 60);
         }

         AlertDialog.Builder var3 = new AlertDialog.Builder(var1.getParentActivity());
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setMessage(LocaleController.formatString("FloodWaitTime", 2131559496, var0));
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var1.showDialog(var3.create(), true, (OnDismissListener)null);
      }

   }

   public static AlertDialog showSecretLocationAlert(Context var0, int var1, Runnable var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var1 = MessagesController.getInstance(var1).availableMapProviders;
      if ((var1 & 1) != 0) {
         var4.add(LocaleController.getString("MapPreviewProviderTelegram", 2131559804));
      }

      if ((var1 & 2) != 0) {
         var4.add(LocaleController.getString("MapPreviewProviderGoogle", 2131559802));
      }

      if ((var1 & 4) != 0) {
         var4.add(LocaleController.getString("MapPreviewProviderYandex", 2131559805));
      }

      var4.add(LocaleController.getString("MapPreviewProviderNobody", 2131559803));
      AlertDialog.Builder var5 = (new AlertDialog.Builder(var0)).setTitle(LocaleController.getString("ChooseMapPreviewProvider", 2131559090)).setItems((CharSequence[])var4.toArray(new String[0]), new _$$Lambda$AlertsCreator$RtoxnZeC0UJlx6OiaOAo9lMRs7E(var2));
      if (!var3) {
         var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      }

      AlertDialog var6 = var5.show();
      if (var3) {
         var6.setCanceledOnTouchOutside(false);
      }

      return var6;
   }

   public static void showSendMediaAlert(int var0, BaseFragment var1) {
      if (var0 != 0) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(var1.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         if (var0 == 1) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedStickers", 2131559380));
         } else if (var0 == 2) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedMedia", 2131559376));
         } else if (var0 == 3) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedPolls", 2131559378));
         } else if (var0 == 4) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedStickersAll", 2131559381));
         } else if (var0 == 5) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedMediaAll", 2131559377));
         } else if (var0 == 6) {
            var2.setMessage(LocaleController.getString("ErrorSendRestrictedPollsAll", 2131559379));
         }

         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var1.showDialog(var2.create(), true, (OnDismissListener)null);
      }
   }

   public static Dialog showSimpleAlert(BaseFragment var0, String var1) {
      if (var1 != null && var0 != null && var0.getParentActivity() != null) {
         AlertDialog var2 = createSimpleAlert(var0.getParentActivity(), var1).create();
         var0.showDialog(var2);
         return var2;
      } else {
         return null;
      }
   }

   public static Toast showSimpleToast(BaseFragment var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2;
         if (var0 != null && var0.getParentActivity() != null) {
            var2 = var0.getParentActivity();
         } else {
            var2 = ApplicationLoader.applicationContext;
         }

         Toast var3 = Toast.makeText((Context)var2, var1, 1);
         var3.show();
         return var3;
      }
   }

   public static AlertDialog showUpdateAppAlert(Context var0, String var1, boolean var2) {
      if (var0 != null && var1 != null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(var0);
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setMessage(var1);
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         if (var2) {
            var3.setNegativeButton(LocaleController.getString("UpdateApp", 2131560950), new _$$Lambda$AlertsCreator$msGS4QN_R2Ivdo98cFI__iWFJUI(var0));
         }

         return var3.show();
      } else {
         return null;
      }
   }

   private static void updateDayPicker(NumberPicker var0, NumberPicker var1, NumberPicker var2) {
      Calendar var3 = Calendar.getInstance();
      var3.set(2, var1.getValue());
      var3.set(1, var2.getValue());
      var0.setMinValue(1);
      var0.setMaxValue(var3.getActualMaximum(5));
   }

   public interface AccountSelectDelegate {
      void didSelectAccount(int var1);
   }

   public interface DatePickerDelegate {
      void didSelectDate(int var1, int var2, int var3);
   }

   public interface PaymentAlertDelegate {
      void didPressedNewCard();
   }
}
