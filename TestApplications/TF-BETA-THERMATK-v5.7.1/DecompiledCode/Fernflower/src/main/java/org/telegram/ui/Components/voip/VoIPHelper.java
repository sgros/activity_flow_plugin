package org.telegram.ui.Components.voip;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.VoIPActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BetterRatingView;
import org.telegram.ui.Components.LayoutHelper;

public class VoIPHelper {
   private static final int VOIP_SUPPORT_ID = 4244000;
   public static long lastCallTime;

   public static boolean canRateCall(TLRPC.TL_messageActionPhoneCall var0) {
      TLRPC.PhoneCallDiscardReason var1 = var0.reason;
      if (!(var1 instanceof TLRPC.TL_phoneCallDiscardReasonBusy) && !(var1 instanceof TLRPC.TL_phoneCallDiscardReasonMissed)) {
         Iterator var4 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).getStringSet("calls_access_hashes", Collections.EMPTY_SET).iterator();

         while(var4.hasNext()) {
            String[] var2 = ((String)var4.next()).split(" ");
            if (var2.length >= 2) {
               String var3 = var2[0];
               StringBuilder var5 = new StringBuilder();
               var5.append(var0.call_id);
               var5.append("");
               if (var3.equals(var5.toString())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static void doInitiateCall(TLRPC.User var0, Activity var1) {
      if (var1 != null && var0 != null) {
         if (System.currentTimeMillis() - lastCallTime < 2000L) {
            return;
         }

         lastCallTime = System.currentTimeMillis();
         Intent var2 = new Intent(var1, VoIPService.class);
         var2.putExtra("user_id", var0.id);
         var2.putExtra("is_outgoing", true);
         var2.putExtra("start_incall_activity", true);
         var2.putExtra("account", UserConfig.selectedAccount);

         try {
            var1.startService(var2);
         } catch (Throwable var3) {
            FileLog.e(var3);
         }
      }

   }

   public static int getDataSavingDefault() {
      boolean var0 = DownloadController.getInstance(0).lowPreset.lessCallData;
      boolean var1 = DownloadController.getInstance(0).mediumPreset.lessCallData;
      boolean var2 = DownloadController.getInstance(0).highPreset.lessCallData;
      if (!var0 && !var1 && !var2) {
         return 0;
      } else if (var0 && !var1 && !var2) {
         return 3;
      } else if (var0 && var1 && !var2) {
         return 1;
      } else if (var0 && var1 && var2) {
         return 2;
      } else {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Invalid call data saving preset configuration: ");
            var3.append(var0);
            var3.append("/");
            var3.append(var1);
            var3.append("/");
            var3.append(var2);
            FileLog.w(var3.toString());
         }

         return 0;
      }
   }

   private static File getLogFile(long var0) {
      if (BuildVars.DEBUG_VERSION) {
         File var2 = new File(ApplicationLoader.applicationContext.getExternalFilesDir((String)null), "logs");
         String[] var3 = var2.list();
         if (var3 != null) {
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               StringBuilder var7 = new StringBuilder();
               var7.append("voip");
               var7.append(var0);
               var7.append(".txt");
               if (var6.endsWith(var7.toString())) {
                  return new File(var2, var6);
               }
            }
         }
      }

      File var9 = getLogsDir();
      StringBuilder var8 = new StringBuilder();
      var8.append(var0);
      var8.append(".log");
      return new File(var9, var8.toString());
   }

   public static File getLogsDir() {
      File var0 = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_logs");
      if (!var0.exists()) {
         var0.mkdirs();
      }

      return var0;
   }

   private static void initiateCall(final TLRPC.User var0, final Activity var1) {
      if (var1 != null && var0 != null) {
         if (VoIPService.getSharedInstance() != null) {
            TLRPC.User var2 = VoIPService.getSharedInstance().getUser();
            if (var2.id != var0.id) {
               (new AlertDialog.Builder(var1)).setTitle(LocaleController.getString("VoipOngoingAlertTitle", 2131561082)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("VoipOngoingAlert", 2131561081, ContactsController.formatName(var2.first_name, var2.last_name), ContactsController.formatName(var0.first_name, var0.last_name)))).setPositiveButton(LocaleController.getString("OK", 2131560097), new OnClickListener() {
                  public void onClick(DialogInterface var1x, int var2) {
                     if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().hangUp(new Runnable() {
                           public void run() {
                              <undefinedtype> var1x = <VAR_NAMELESS_ENCLOSURE>;
                              VoIPHelper.doInitiateCall(var0, var1);
                           }
                        });
                     } else {
                        VoIPHelper.doInitiateCall(var0, var1);
                     }

                  }
               }).setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null).show();
            } else {
               var1.startActivity((new Intent(var1, VoIPActivity.class)).addFlags(268435456));
            }
         } else if (VoIPService.callIShouldHavePutIntoIntent == null) {
            doInitiateCall(var0, var1);
         }
      }

   }

   @TargetApi(23)
   public static void permissionDenied(final Activity var0, final Runnable var1) {
      if (!var0.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
         (new AlertDialog.Builder(var0)).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("VoipNeedMicPermission", 2131561074)).setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null).setNegativeButton(LocaleController.getString("Settings", 2131560738), new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               Intent var3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
               var3.setData(Uri.fromParts("package", var0.getPackageName(), (String)null));
               var0.startActivity(var3);
            }
         }).show().setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface var1x) {
               Runnable var2 = var1;
               if (var2 != null) {
                  var2.run();
               }

            }
         });
      }

   }

   public static void showCallDebugSettings(Context var0) {
      final SharedPreferences var1 = MessagesController.getGlobalMainSettings();
      LinearLayout var2 = new LinearLayout(var0);
      var2.setOrientation(1);
      TextView var3 = new TextView(var0);
      var3.setTextSize(1, 15.0F);
      var3.setText("Please only change these settings if you know exactly what they do.");
      var3.setTextColor(Theme.getColor("dialogTextBlack"));
      var2.addView(var3, LayoutHelper.createLinear(-1, -2, 16.0F, 8.0F, 16.0F, 8.0F));
      final TextCheckCell var4 = new TextCheckCell(var0);
      var4.setTextAndCheck("Force TCP", var1.getBoolean("dbg_force_tcp_in_calls", false), false);
      var4.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1x) {
            boolean var2 = var1.getBoolean("dbg_force_tcp_in_calls", false);
            Editor var3 = var1.edit();
            var3.putBoolean("dbg_force_tcp_in_calls", var2 ^ true);
            var3.commit();
            var4.setChecked(var2 ^ true);
         }
      });
      var2.addView(var4);
      if (BuildVars.DEBUG_VERSION && BuildVars.LOGS_ENABLED) {
         var4 = new TextCheckCell(var0);
         var4.setTextAndCheck("Dump detailed stats", var1.getBoolean("dbg_dump_call_stats", false), false);
         var4.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View var1x) {
               boolean var2 = var1.getBoolean("dbg_dump_call_stats", false);
               Editor var3 = var1.edit();
               var3.putBoolean("dbg_dump_call_stats", var2 ^ true);
               var3.commit();
               var4.setChecked(var2 ^ true);
            }
         });
         var2.addView(var4);
      }

      if (VERSION.SDK_INT >= 26) {
         var4 = new TextCheckCell(var0);
         var4.setTextAndCheck("Enable ConnectionService", var1.getBoolean("dbg_force_connection_service", false), false);
         var4.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View var1x) {
               boolean var2 = var1.getBoolean("dbg_force_connection_service", false);
               Editor var3 = var1.edit();
               var3.putBoolean("dbg_force_connection_service", var2 ^ true);
               var3.commit();
               var4.setChecked(var2 ^ true);
            }
         });
         var2.addView(var4);
      }

      (new AlertDialog.Builder(var0)).setTitle(LocaleController.getString("DebugMenuCallSettings", 2131559210)).setView(var2).show();
   }

   public static void showRateAlert(final Context var0, final Runnable var1, final long var2, final long var4, final int var6, final boolean var7) {
      final File var8 = getLogFile(var2);
      LinearLayout var9 = new LinearLayout(var0);
      var9.setOrientation(1);
      int var10 = AndroidUtilities.dp(16.0F);
      var9.setPadding(var10, var10, var10, 0);
      final TextView var11 = new TextView(var0);
      var11.setTextSize(2, 16.0F);
      var11.setTextColor(Theme.getColor("dialogTextBlack"));
      var11.setGravity(17);
      var11.setText(LocaleController.getString("VoipRateCallAlert", 2131561088));
      var9.addView(var11);
      final BetterRatingView var12 = new BetterRatingView(var0);
      var9.addView(var12, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
      final LinearLayout var13 = new LinearLayout(var0);
      var13.setOrientation(1);
      android.view.View.OnClickListener var14 = new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            CheckBoxCell var2 = (CheckBoxCell)var1;
            var2.setChecked(var2.isChecked() ^ true, true);
         }
      };
      String[] var15 = new String[]{"echo", "noise", "interruptions", "distorted_speech", "silent_local", "silent_remote", "dropped"};

      final CheckBoxCell var16;
      for(var10 = 0; var10 < var15.length; ++var10) {
         var16 = new CheckBoxCell(var0, 1);
         var16.setClipToPadding(false);
         var16.setTag(var15[var10]);
         String var17;
         switch(var10) {
         case 0:
            var17 = LocaleController.getString("RateCallEcho", 2131560531);
            break;
         case 1:
            var17 = LocaleController.getString("RateCallNoise", 2131560533);
            break;
         case 2:
            var17 = LocaleController.getString("RateCallInterruptions", 2131560532);
            break;
         case 3:
            var17 = LocaleController.getString("RateCallDistorted", 2131560529);
            break;
         case 4:
            var17 = LocaleController.getString("RateCallSilentLocal", 2131560534);
            break;
         case 5:
            var17 = LocaleController.getString("RateCallSilentRemote", 2131560535);
            break;
         case 6:
            var17 = LocaleController.getString("RateCallDropped", 2131560530);
            break;
         default:
            var17 = null;
         }

         var16.setText(var17, (String)null, false, false);
         var16.setOnClickListener(var14);
         var16.setTag(var15[var10]);
         var13.addView(var16);
      }

      var9.addView(var13, LayoutHelper.createLinear(-1, -2, -8.0F, 0.0F, -8.0F, 0.0F));
      var13.setVisibility(8);
      final EditText var21 = new EditText(var0);
      var21.setHint(LocaleController.getString("VoipFeedbackCommentHint", 2131561069));
      var21.setInputType(147457);
      var21.setTextColor(Theme.getColor("dialogTextBlack"));
      var21.setHintTextColor(Theme.getColor("dialogTextHint"));
      var21.setBackgroundDrawable(Theme.createEditTextDrawable(var0, true));
      var21.setPadding(0, AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F));
      var21.setTextSize(18.0F);
      var21.setVisibility(8);
      var9.addView(var21, LayoutHelper.createLinear(-1, -2, 8.0F, 8.0F, 8.0F, 0.0F));
      final boolean[] var22 = new boolean[]{true};
      var16 = new CheckBoxCell(var0, 1);
      android.view.View.OnClickListener var18 = new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            boolean[] var2 = var22;
            var2[0] ^= true;
            var16.setChecked(var2[0], true);
         }
      };
      var16.setText(LocaleController.getString("CallReportIncludeLogs", 2131558882), (String)null, true, false);
      var16.setClipToPadding(false);
      var16.setOnClickListener(var18);
      var9.addView(var16, LayoutHelper.createLinear(-1, -2, -8.0F, 0.0F, -8.0F, 0.0F));
      final TextView var23 = new TextView(var0);
      var23.setTextSize(2, 14.0F);
      var23.setTextColor(Theme.getColor("dialogTextGray3"));
      var23.setText(LocaleController.getString("CallReportLogsExplain", 2131558883));
      var23.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
      var23.setOnClickListener(var18);
      var9.addView(var23);
      var16.setVisibility(8);
      var23.setVisibility(8);
      if (!var8.exists()) {
         var22[0] = false;
      }

      final AlertDialog var20 = (new AlertDialog.Builder(var0)).setTitle(LocaleController.getString("CallMessageReportProblem", 2131558878)).setView(var9).setPositiveButton(LocaleController.getString("Send", 2131560685), new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
         }
      }).setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null).setOnDismissListener(new OnDismissListener() {
         public void onDismiss(DialogInterface var1x) {
            Runnable var2 = var1;
            if (var2 != null) {
               var2.run();
            }

         }
      }).create();
      if (BuildVars.DEBUG_VERSION && var8.exists()) {
         var20.setNeutralButton("Send log", new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               Intent var3 = new Intent(var0, LaunchActivity.class);
               var3.setAction("android.intent.action.SEND");
               var3.putExtra("android.intent.extra.STREAM", Uri.fromFile(var8));
               var0.startActivity(var3);
            }
         });
      }

      var20.show();
      var20.getWindow().setSoftInputMode(3);
      final View var19 = var20.getButton(-1);
      var19.setEnabled(false);
      var12.setOnRatingChangeListener(new BetterRatingView.OnRatingChangeListener() {
         public void onRatingChanged(int var1) {
            View var2 = var19;
            boolean var3;
            if (var1 > 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var2.setEnabled(var3);
            TextView var4 = (TextView)var19;
            String var5;
            if (var1 < 4) {
               var1 = 2131559911;
               var5 = "Next";
            } else {
               var1 = 2131560685;
               var5 = "Send";
            }

            var4.setText(LocaleController.getString(var5, var1).toUpperCase());
         }
      });
      var19.setOnClickListener(new android.view.View.OnClickListener(new int[]{0}) {
         // $FF: synthetic field
         final int[] val$page;

         {
            this.val$page = var2x;
         }

         public void onClick(View var1) {
            if (var12.getRating() < 4) {
               int[] var7x = this.val$page;
               if (var7x[0] != 1) {
                  var7x[0] = 1;
                  var12.setVisibility(8);
                  var11.setVisibility(8);
                  var20.setTitle(LocaleController.getString("CallReportHint", 2131558881));
                  var21.setVisibility(0);
                  if (var8.exists()) {
                     var16.setVisibility(0);
                     var23.setVisibility(0);
                  }

                  var13.setVisibility(0);
                  ((TextView)var19).setText(LocaleController.getString("Send", 2131560685).toUpperCase());
                  return;
               }
            }

            final int var2x = UserConfig.selectedAccount;
            final TLRPC.TL_phone_setCallRating var8x = new TLRPC.TL_phone_setCallRating();
            var8x.rating = var12.getRating();
            final ArrayList var3 = new ArrayList();

            for(int var4x = 0; var4x < var13.getChildCount(); ++var4x) {
               CheckBoxCell var5 = (CheckBoxCell)var13.getChildAt(var4x);
               if (var5.isChecked()) {
                  StringBuilder var6x = new StringBuilder();
                  var6x.append("#");
                  var6x.append(var5.getTag());
                  var3.add(var6x.toString());
               }
            }

            if (var8x.rating < 5) {
               var8x.comment = var21.getText().toString();
            } else {
               var8x.comment = "";
            }

            if (!var3.isEmpty() && !var22[0]) {
               StringBuilder var9 = new StringBuilder();
               var9.append(var8x.comment);
               var9.append(" ");
               var9.append(TextUtils.join(" ", var3));
               var8x.comment = var9.toString();
            }

            var8x.peer = new TLRPC.TL_inputPhoneCall();
            TLRPC.TL_inputPhoneCall var10 = var8x.peer;
            var10.access_hash = var4;
            var10.id = var2;
            var8x.user_initiative = var7;
            ConnectionsManager.getInstance(var6).sendRequest(var8x, new RequestDelegate() {
               public void run(TLObject var1, TLRPC.TL_error var2xx) {
                  if (var1 instanceof TLRPC.TL_updates) {
                     TLRPC.TL_updates var3x = (TLRPC.TL_updates)var1;
                     MessagesController.getInstance(var2x).processUpdates(var3x, false);
                  }

                  <undefinedtype> var4x = <VAR_NAMELESS_ENCLOSURE>;
                  if (var22[0] && var8.exists() && var8x.rating < 4) {
                     SendMessagesHelper.prepareSendingDocument(var8.getAbsolutePath(), var8.getAbsolutePath(), (Uri)null, TextUtils.join(" ", var3), "text/plain", 4244000L, (MessageObject)null, (InputContentInfoCompat)null, (MessageObject)null);
                     Toast.makeText(var0, LocaleController.getString("CallReportSent", 2131558884), 1).show();
                  }

               }
            });
            var20.dismiss();
         }
      });
   }

   public static void showRateAlert(Context var0, TLRPC.TL_messageActionPhoneCall var1) {
      Iterator var2 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).getStringSet("calls_access_hashes", Collections.EMPTY_SET).iterator();

      while(var2.hasNext()) {
         String[] var3 = ((String)var2.next()).split(" ");
         if (var3.length >= 2) {
            String var4 = var3[0];
            StringBuilder var5 = new StringBuilder();
            var5.append(var1.call_id);
            var5.append("");
            if (var4.equals(var5.toString())) {
               try {
                  long var6 = Long.parseLong(var3[1]);
                  showRateAlert(var0, (Runnable)null, var1.call_id, var6, UserConfig.selectedAccount, true);
               } catch (Exception var8) {
               }
               break;
            }
         }
      }

   }

   public static void startCall(TLRPC.User var0, final Activity var1, TLRPC.UserFull var2) {
      boolean var3 = true;
      if (var2 != null && var2.phone_calls_private) {
         (new AlertDialog.Builder(var1)).setTitle(LocaleController.getString("VoipFailed", 2131561068)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("CallNotAvailable", 2131558880, ContactsController.formatName(var0.first_name, var0.last_name)))).setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null).show();
      } else if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getConnectionState() != 3) {
         if (android.provider.Settings.System.getInt(var1.getContentResolver(), "airplane_mode_on", 0) == 0) {
            var3 = false;
         }

         AlertDialog.Builder var6 = new AlertDialog.Builder(var1);
         int var4;
         String var5;
         if (var3) {
            var4 = 2131561078;
            var5 = "VoipOfflineAirplaneTitle";
         } else {
            var4 = 2131561080;
            var5 = "VoipOfflineTitle";
         }

         var6 = var6.setTitle(LocaleController.getString(var5, var4));
         if (var3) {
            var4 = 2131561077;
            var5 = "VoipOfflineAirplane";
         } else {
            var4 = 2131561076;
            var5 = "VoipOffline";
         }

         var6 = var6.setMessage(LocaleController.getString(var5, var4)).setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         if (var3) {
            final Intent var7 = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
            if (var7.resolveActivity(var1.getPackageManager()) != null) {
               var6.setNeutralButton(LocaleController.getString("VoipOfflineOpenSettings", 2131561079), new OnClickListener() {
                  public void onClick(DialogInterface var1x, int var2) {
                     var1.startActivity(var7);
                  }
               });
            }
         }

         var6.show();
      } else {
         if (VERSION.SDK_INT >= 23 && var1.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            var1.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 101);
         } else {
            initiateCall(var0, var1);
         }

      }
   }
}
