package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int accountsAllRow;
   private int accountsInfoRow;
   private int accountsSectionRow;
   private NotificationsSettingsActivity.ListAdapter adapter;
   private int androidAutoAlertRow;
   private int badgeNumberMessagesRow;
   private int badgeNumberMutedRow;
   private int badgeNumberSection;
   private int badgeNumberSection2Row;
   private int badgeNumberShowRow;
   private int callsRingtoneRow;
   private int callsSection2Row;
   private int callsSectionRow;
   private int callsVibrateRow;
   private int channelsRow;
   private int contactJoinedRow;
   private int eventsSection2Row;
   private int eventsSectionRow;
   private ArrayList exceptionChannels = null;
   private ArrayList exceptionChats = null;
   private ArrayList exceptionUsers = null;
   private int groupRow;
   private int inappPreviewRow;
   private int inappPriorityRow;
   private int inappSectionRow;
   private int inappSoundRow;
   private int inappVibrateRow;
   private int inchatSoundRow;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private int notificationsSection2Row;
   private int notificationsSectionRow;
   private int notificationsServiceConnectionRow;
   private int notificationsServiceRow;
   private int otherSection2Row;
   private int otherSectionRow;
   private int pinnedMessageRow;
   private int privateRow;
   private int repeatRow;
   private int resetNotificationsRow;
   private int resetSection2Row;
   private int resetSectionRow;
   private boolean reseting = false;
   private int rowCount = 0;

   // $FF: synthetic method
   static int access$1500(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2900(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4200(NotificationsSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static void lambda$null$5(TLObject var0, TLRPC.TL_error var1) {
   }

   private void loadExceptions() {
      MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$NotificationsSettingsActivity$uXiQpIA5YYdlg9kVau5gYRsTuCM(this));
   }

   private void showExceptionsAlert(int var1) {
      ArrayList var3;
      String var4;
      label35: {
         ArrayList var2;
         if (var1 == this.privateRow) {
            var2 = this.exceptionUsers;
            var3 = var2;
            if (var2 != null) {
               var3 = var2;
               if (!var2.isEmpty()) {
                  var4 = LocaleController.formatPluralString("ChatsException", var2.size());
                  var3 = var2;
                  break label35;
               }
            }
         } else if (var1 == this.groupRow) {
            var2 = this.exceptionChats;
            var3 = var2;
            if (var2 != null) {
               var3 = var2;
               if (!var2.isEmpty()) {
                  var4 = LocaleController.formatPluralString("Groups", var2.size());
                  var3 = var2;
                  break label35;
               }
            }
         } else {
            ArrayList var7 = this.exceptionChannels;
            var3 = var7;
            if (var7 != null) {
               var3 = var7;
               if (!var7.isEmpty()) {
                  String var5 = LocaleController.formatPluralString("Channels", var7.size());
                  var3 = var7;
                  var4 = var5;
                  break label35;
               }
            }
         }

         var4 = null;
      }

      if (var4 != null) {
         AlertDialog.Builder var6 = new AlertDialog.Builder(this.getParentActivity());
         if (var3.size() == 1) {
            var6.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsSingleAlert", 2131560066, var4)));
         } else {
            var6.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsAlert", 2131560065, var4)));
         }

         var6.setTitle(LocaleController.getString("NotificationsExceptions", 2131560064));
         var6.setNeutralButton(LocaleController.getString("ViewExceptions", 2131561053), new _$$Lambda$NotificationsSettingsActivity$9FhV71oy8_vyXyR3LWFGjX_RReE(this, var3));
         var6.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var6.create());
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("NotificationsAndSounds", 2131560057));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               NotificationsSettingsActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      this.listView.setVerticalScrollBarEnabled(false);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      var3 = this.listView;
      NotificationsSettingsActivity.ListAdapter var5 = new NotificationsSettingsActivity.ListAdapter(var1);
      this.adapter = var5;
      var3.setAdapter(var5);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$NotificationsSettingsActivity$g4GVhVtYkD5_YPczTHGsTY58dkg(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.notificationsSettingsUpdated) {
         this.adapter.notifyDataSetChanged();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextSettingsCell.class, NotificationsCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText")};
   }

   // $FF: synthetic method
   public void lambda$createView$8$NotificationsSettingsActivity(View var1, int var2, float var3, float var4) {
      if (this.getParentActivity() != null) {
         int var5 = this.privateRow;
         byte var6 = 2;
         boolean var7 = false;
         boolean var14;
         if (var2 != var5 && var2 != this.groupRow && var2 != this.channelsRow) {
            label198: {
               int var26 = this.callsRingtoneRow;
               String var28 = null;
               String var9 = null;
               String var13;
               SharedPreferences var29;
               String var35;
               if (var2 == var26) {
                  label196: {
                     Exception var10000;
                     label208: {
                        Intent var10;
                        Uri var11;
                        boolean var10001;
                        try {
                           var29 = MessagesController.getNotificationsSettings(super.currentAccount);
                           var10 = new Intent("android.intent.action.RINGTONE_PICKER");
                           var10.putExtra("android.intent.extra.ringtone.TYPE", 1);
                           var10.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                           var10.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                           var10.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                           var11 = System.DEFAULT_RINGTONE_URI;
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label208;
                        }

                        if (var11 != null) {
                           try {
                              var35 = var11.getPath();
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break label208;
                           }
                        } else {
                           var35 = null;
                        }

                        try {
                           var13 = var29.getString("CallsRingtonePath", var35);
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label208;
                        }

                        Uri var30 = var9;
                        if (var13 != null) {
                           label209: {
                              var30 = var9;

                              label180: {
                                 try {
                                    if (var13.equals("NoSound")) {
                                       break label209;
                                    }

                                    if (var13.equals(var35)) {
                                       break label180;
                                    }
                                 } catch (Exception var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                    break label208;
                                 }

                                 try {
                                    var30 = Uri.parse(var13);
                                    break label209;
                                 } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                    break label208;
                                 }
                              }

                              var30 = var11;
                           }
                        }

                        try {
                           var10.putExtra("android.intent.extra.ringtone.EXISTING_URI", var30);
                           this.startActivityForResult(var10, var2);
                           break label196;
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                        }
                     }

                     Exception var33 = var10000;
                     FileLog.e((Throwable)var33);
                  }
               } else if (var2 == this.resetNotificationsRow) {
                  AlertDialog.Builder var34 = new AlertDialog.Builder(this.getParentActivity());
                  var34.setMessage(LocaleController.getString("ResetNotificationsAlert", 2131560602));
                  var34.setTitle(LocaleController.getString("AppName", 2131558635));
                  var34.setPositiveButton(LocaleController.getString("Reset", 2131560583), new _$$Lambda$NotificationsSettingsActivity$yCH91Gy9ARU8yn1KTl14GsaHDf4(this));
                  var34.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  this.showDialog(var34.create());
               } else {
                  SharedPreferences var36;
                  Editor var37;
                  if (var2 == this.inappSoundRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("EnableInAppSounds", true);
                     var37.putBoolean("EnableInAppSounds", var14 ^ true);
                     var37.commit();
                     break label198;
                  }

                  if (var2 == this.inappVibrateRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("EnableInAppVibrate", true);
                     var37.putBoolean("EnableInAppVibrate", var14 ^ true);
                     var37.commit();
                     break label198;
                  }

                  Editor var38;
                  if (var2 == this.inappPreviewRow) {
                     var29 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var38 = var29.edit();
                     var14 = var29.getBoolean("EnableInAppPreview", true);
                     var38.putBoolean("EnableInAppPreview", var14 ^ true);
                     var38.commit();
                     break label198;
                  }

                  if (var2 == this.inchatSoundRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("EnableInChatSound", true);
                     var37.putBoolean("EnableInChatSound", var14 ^ true);
                     var37.commit();
                     NotificationsController.getInstance(super.currentAccount).setInChatSoundEnabled(var14 ^ true);
                     break label198;
                  }

                  if (var2 == this.inappPriorityRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("EnableInAppPriority", false);
                     var37.putBoolean("EnableInAppPriority", var14 ^ true);
                     var37.commit();
                     break label198;
                  }

                  if (var2 == this.contactJoinedRow) {
                     var29 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var38 = var29.edit();
                     var14 = var29.getBoolean("EnableContactJoined", true);
                     MessagesController.getInstance(super.currentAccount).enableJoined = var14 ^ true;
                     var38.putBoolean("EnableContactJoined", var14 ^ true);
                     var38.commit();
                     TLRPC.TL_account_setContactSignUpNotification var39 = new TLRPC.TL_account_setContactSignUpNotification();
                     var39.silent = var14;
                     ConnectionsManager.getInstance(super.currentAccount).sendRequest(var39, _$$Lambda$NotificationsSettingsActivity$viFpXODmAg_Q4M_X6ggvpEc5GAg.INSTANCE);
                     break label198;
                  }

                  if (var2 == this.pinnedMessageRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("PinnedMessages", true);
                     var37.putBoolean("PinnedMessages", var14 ^ true);
                     var37.commit();
                     break label198;
                  }

                  if (var2 == this.androidAutoAlertRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var37 = var36.edit();
                     var14 = var36.getBoolean("EnableAutoNotifications", false);
                     var37.putBoolean("EnableAutoNotifications", var14 ^ true);
                     var37.commit();
                     break label198;
                  }

                  if (var2 == this.badgeNumberShowRow) {
                     var37 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
                     var14 = NotificationsController.getInstance(super.currentAccount).showBadgeNumber;
                     NotificationsController.getInstance(super.currentAccount).showBadgeNumber = var14 ^ true;
                     var37.putBoolean("badgeNumber", NotificationsController.getInstance(super.currentAccount).showBadgeNumber);
                     var37.commit();
                     NotificationsController.getInstance(super.currentAccount).updateBadge();
                     break label198;
                  }

                  if (var2 == this.badgeNumberMutedRow) {
                     var37 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
                     var14 = NotificationsController.getInstance(super.currentAccount).showBadgeMuted;
                     NotificationsController.getInstance(super.currentAccount).showBadgeMuted = var14 ^ true;
                     var37.putBoolean("badgeNumberMuted", NotificationsController.getInstance(super.currentAccount).showBadgeMuted);
                     var37.commit();
                     NotificationsController.getInstance(super.currentAccount).updateBadge();
                     break label198;
                  }

                  if (var2 == this.badgeNumberMessagesRow) {
                     var37 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
                     var14 = NotificationsController.getInstance(super.currentAccount).showBadgeMessages;
                     NotificationsController.getInstance(super.currentAccount).showBadgeMessages = var14 ^ true;
                     var37.putBoolean("badgeNumberMessages", NotificationsController.getInstance(super.currentAccount).showBadgeMessages);
                     var37.commit();
                     NotificationsController.getInstance(super.currentAccount).updateBadge();
                     break label198;
                  }

                  if (var2 == this.notificationsServiceConnectionRow) {
                     var36 = MessagesController.getNotificationsSettings(super.currentAccount);
                     var14 = var36.getBoolean("pushConnection", true);
                     var37 = var36.edit();
                     var37.putBoolean("pushConnection", var14 ^ true);
                     var14 = var36.getBoolean("pushService", true);
                     var37.putBoolean("pushService", var14 ^ true);
                     var37.commit();
                     if (!var14) {
                        ConnectionsManager.getInstance(super.currentAccount).setPushConnectionEnabled(true);
                        ApplicationLoader.startPushService();
                     } else {
                        ConnectionsManager.getInstance(super.currentAccount).setPushConnectionEnabled(false);
                        ApplicationLoader.stopPushService();
                     }
                     break label198;
                  }

                  if (var2 == this.accountsAllRow) {
                     var29 = MessagesController.getGlobalNotificationsSettings();
                     boolean var15 = var29.getBoolean("AllAccounts", true);
                     var37 = var29.edit();
                     var37.putBoolean("AllAccounts", var15 ^ true);
                     var37.commit();
                     SharedConfig.showNotificationsForAllAccounts = var15 ^ true;
                     var2 = 0;

                     while(true) {
                        var14 = var15;
                        if (var2 >= 3) {
                           break label198;
                        }

                        if (SharedConfig.showNotificationsForAllAccounts) {
                           NotificationsController.getInstance(var2).showNotifications();
                        } else if (var2 == super.currentAccount) {
                           NotificationsController.getInstance(var2).showNotifications();
                        } else {
                           NotificationsController.getInstance(var2).hideNotifications();
                        }

                        ++var2;
                     }
                  }

                  if (var2 == this.callsVibrateRow) {
                     if (this.getParentActivity() == null) {
                        return;
                     }

                     if (var2 == this.callsVibrateRow) {
                        var28 = "vibrate_calls";
                     }

                     this.showDialog(AlertsCreator.createVibrationSelectDialog(this.getParentActivity(), 0L, var28, new _$$Lambda$NotificationsSettingsActivity$7IXN7L8E_cyofxGsKJruA7N2DeY(this, var2)));
                  } else if (var2 == this.repeatRow) {
                     AlertDialog.Builder var32 = new AlertDialog.Builder(this.getParentActivity());
                     var32.setTitle(LocaleController.getString("RepeatNotifications", 2131560563));
                     var35 = LocaleController.getString("RepeatDisabled", 2131560562);
                     String var16 = LocaleController.formatPluralString("Minutes", 5);
                     String var17 = LocaleController.formatPluralString("Minutes", 10);
                     var28 = LocaleController.formatPluralString("Minutes", 30);
                     var13 = LocaleController.formatPluralString("Hours", 1);
                     var9 = LocaleController.formatPluralString("Hours", 2);
                     String var18 = LocaleController.formatPluralString("Hours", 4);
                     _$$Lambda$NotificationsSettingsActivity$KVxXWyv_zLmmyeu95JQljLmRuOE var31 = new _$$Lambda$NotificationsSettingsActivity$KVxXWyv_zLmmyeu95JQljLmRuOE(this, var2);
                     var32.setItems(new CharSequence[]{var35, var16, var17, var28, var13, var9, var18}, var31);
                     var32.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     this.showDialog(var32.create());
                  }
               }

               var14 = false;
            }
         } else {
            ArrayList var8;
            if (var2 == this.privateRow) {
               var8 = this.exceptionUsers;
               var6 = 1;
            } else if (var2 == this.groupRow) {
               var8 = this.exceptionChats;
               var6 = 0;
            } else {
               var8 = this.exceptionChannels;
            }

            NotificationsCheckCell var12 = (NotificationsCheckCell)var1;
            var14 = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(var6);
            if (LocaleController.isRTL && var3 <= (float)AndroidUtilities.dp(76.0F) || !LocaleController.isRTL && var3 >= (float)(var1.getMeasuredWidth() - AndroidUtilities.dp(76.0F))) {
               NotificationsController var27 = NotificationsController.getInstance(super.currentAccount);
               if (!var14) {
                  var5 = 0;
               } else {
                  var5 = Integer.MAX_VALUE;
               }

               var27.setGlobalNotificationsEnabled(var6, var5);
               this.showExceptionsAlert(var2);
               var12.setChecked(var14 ^ true, 0);
               this.adapter.notifyItemChanged(var2);
            } else {
               this.presentFragment(new NotificationsCustomSettingsActivity(var6, var8));
            }
         }

         if (var1 instanceof TextCheckCell) {
            TextCheckCell var25 = (TextCheckCell)var1;
            if (!var14) {
               var7 = true;
            }

            var25.setChecked(var7);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$loadExceptions$1$NotificationsSettingsActivity() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      LongSparseArray var4 = new LongSparseArray();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();
      int var11 = UserConfig.getInstance(super.currentAccount).clientUserId;
      SharedPreferences var12 = MessagesController.getNotificationsSettings(super.currentAccount);
      Map var13 = var12.getAll();
      Iterator var14 = var13.entrySet().iterator();

      while(true) {
         NotificationsSettingsActivity.NotificationException var21;
         label218:
         while(true) {
            while(true) {
               Entry var15;
               String var17;
               long var19;
               int var24;
               do {
                  do {
                     String var16;
                     do {
                        if (!var14.hasNext()) {
                           byte var48 = 0;
                           if (var4.size() != 0) {
                              label175: {
                                 Exception var33;
                                 label225: {
                                    Exception var10000;
                                    boolean var10001;
                                    boolean var25;
                                    label173: {
                                       label226: {
                                          try {
                                             var25 = var7.isEmpty();
                                          } catch (Exception var32) {
                                             var10000 = var32;
                                             var10001 = false;
                                             break label226;
                                          }

                                          if (!var25) {
                                             try {
                                                MessagesStorage.getInstance(super.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", var7), var10, var5);
                                             } catch (Exception var31) {
                                                var10000 = var31;
                                                var10001 = false;
                                                break label226;
                                             }
                                          }

                                          try {
                                             var25 = var5.isEmpty();
                                             break label173;
                                          } catch (Exception var30) {
                                             var10000 = var30;
                                             var10001 = false;
                                          }
                                       }

                                       var33 = var10000;
                                       break label225;
                                    }

                                    String var36;
                                    MessagesStorage var38;
                                    label160: {
                                       label159: {
                                          if (!var25) {
                                             String var34;
                                             try {
                                                var38 = MessagesStorage.getInstance(super.currentAccount);
                                                var34 = TextUtils.join(",", var5);
                                             } catch (Exception var27) {
                                                var33 = var27;
                                                break label225;
                                             }

                                             try {
                                                var38.getUsersInternal(var34, var8);
                                             } catch (Exception var29) {
                                                var10000 = var29;
                                                var10001 = false;
                                                break label159;
                                             }
                                          }

                                          try {
                                             if (var6.isEmpty()) {
                                                break label175;
                                             }

                                             var38 = MessagesStorage.getInstance(super.currentAccount);
                                             var36 = TextUtils.join(",", var6);
                                             break label160;
                                          } catch (Exception var28) {
                                             var10000 = var28;
                                             var10001 = false;
                                          }
                                       }

                                       var33 = var10000;
                                       break label225;
                                    }

                                    try {
                                       var38.getChatsInternal(var36, var9);
                                       break label175;
                                    } catch (Exception var26) {
                                       var33 = var26;
                                    }
                                 }

                                 FileLog.e((Throwable)var33);
                              }

                              ArrayList var39 = var9;
                              var6 = var8;
                              var24 = var9.size();

                              for(var11 = 0; var11 < var24; ++var11) {
                                 TLRPC.Chat var37 = (TLRPC.Chat)var39.get(var11);
                                 if (!var37.left && !var37.kicked && var37.migrated_to == null) {
                                    NotificationsSettingsActivity.NotificationException var35 = (NotificationsSettingsActivity.NotificationException)var4.get((long)(-var37.id));
                                    var4.remove((long)(-var37.id));
                                    if (var35 != null) {
                                       if (ChatObject.isChannel(var37) && !var37.megagroup) {
                                          var3.add(var35);
                                       } else {
                                          var2.add(var35);
                                       }
                                    }
                                 }
                              }

                              var24 = var8.size();

                              for(var11 = 0; var11 < var24; ++var11) {
                                 TLRPC.User var41 = (TLRPC.User)var6.get(var11);
                                 if (!var41.deleted) {
                                    var4.remove((long)var41.id);
                                 }
                              }

                              var24 = var10.size();

                              for(var11 = 0; var11 < var24; ++var11) {
                                 var4.remove((long)((TLRPC.EncryptedChat)var10.get(var11)).id << 32);
                              }

                              var24 = var4.size();

                              for(var11 = var48; var11 < var24; ++var11) {
                                 if ((int)var4.keyAt(var11) < 0) {
                                    var2.remove(var4.valueAt(var11));
                                    var3.remove(var4.valueAt(var11));
                                 } else {
                                    var1.remove(var4.valueAt(var11));
                                 }
                              }
                           }

                           AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsSettingsActivity$tpC7zsNM6CXmGWZ2kbXQvv8Lbxg(this, var8, var9, var10, var1, var2, var3));
                           return;
                        }

                        var15 = (Entry)var14.next();
                        var16 = (String)var15.getKey();
                     } while(!var16.startsWith("notify2_"));

                     var17 = var16.replace("notify2_", "");
                     Long var18 = Utilities.parseLong(var17);
                     var19 = var18;
                  } while(var19 == 0L);
               } while(var19 == (long)var11);

               var21 = new NotificationsSettingsActivity.NotificationException();
               var21.did = var19;
               StringBuilder var22 = new StringBuilder();
               var22.append("custom_");
               var22.append(var19);
               var21.hasCustom = var12.getBoolean(var22.toString(), false);
               var21.notify = (Integer)var15.getValue();
               if (var21.notify != 0) {
                  StringBuilder var40 = new StringBuilder();
                  var40.append("notifyuntil_");
                  var40.append(var17);
                  Integer var42 = (Integer)var13.get(var40.toString());
                  if (var42 != null) {
                     var21.muteUntil = var42;
                  }
               }

               int var23 = (int)var19;
               var24 = (int)(var19 << 32);
               if (var23 != 0) {
                  if (var23 > 0) {
                     TLRPC.User var43 = MessagesController.getInstance(super.currentAccount).getUser(var23);
                     if (var43 == null) {
                        var5.add(var23);
                        var4.put(var19, var21);
                        break label218;
                     }

                     if (!var43.deleted) {
                        break label218;
                     }
                  } else {
                     MessagesController var44 = MessagesController.getInstance(super.currentAccount);
                     var23 = -var23;
                     TLRPC.Chat var46 = var44.getChat(var23);
                     if (var46 == null) {
                        var6.add(var23);
                        var4.put(var19, var21);
                     } else if (!var46.left && !var46.kicked && var46.migrated_to == null) {
                        if (ChatObject.isChannel(var46) && !var46.megagroup) {
                           var3.add(var21);
                           continue;
                        }

                        var2.add(var21);
                        continue;
                     }
                  }

                  var6 = var6;
                  var2 = var2;
               } else if (var24 != 0) {
                  TLRPC.EncryptedChat var45 = MessagesController.getInstance(super.currentAccount).getEncryptedChat(var24);
                  if (var45 == null) {
                     var7.add(var24);
                     var4.put(var19, var21);
                  } else {
                     TLRPC.User var47 = MessagesController.getInstance(super.currentAccount).getUser(var45.user_id);
                     if (var47 == null) {
                        var5.add(var45.user_id);
                        var4.put((long)var45.user_id, var21);
                     } else if (var47.deleted) {
                        continue;
                     }
                  }

                  var1.add(var21);
               }
            }
         }

         var1.add(var21);
      }
   }

   // $FF: synthetic method
   public void lambda$null$0$NotificationsSettingsActivity(ArrayList var1, ArrayList var2, ArrayList var3, ArrayList var4, ArrayList var5, ArrayList var6) {
      MessagesController.getInstance(super.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(super.currentAccount).putChats(var2, true);
      MessagesController.getInstance(super.currentAccount).putEncryptedChats(var3, true);
      this.exceptionUsers = var4;
      this.exceptionChats = var5;
      this.exceptionChannels = var6;
      this.adapter.notifyItemChanged(this.privateRow);
      this.adapter.notifyItemChanged(this.groupRow);
      this.adapter.notifyItemChanged(this.channelsRow);
   }

   // $FF: synthetic method
   public void lambda$null$2$NotificationsSettingsActivity() {
      MessagesController.getInstance(super.currentAccount).enableJoined = true;
      this.reseting = false;
      Editor var1 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
      var1.clear();
      var1.commit();
      this.exceptionChats.clear();
      this.exceptionUsers.clear();
      this.adapter.notifyDataSetChanged();
      if (this.getParentActivity() != null) {
         Toast.makeText(this.getParentActivity(), LocaleController.getString("ResetNotificationsText", 2131560603), 0).show();
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$NotificationsSettingsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsSettingsActivity$CxAkqO4VuKiAX9yDzFMx1BE_9aM(this));
   }

   // $FF: synthetic method
   public void lambda$null$4$NotificationsSettingsActivity(DialogInterface var1, int var2) {
      if (!this.reseting) {
         this.reseting = true;
         TLRPC.TL_account_resetNotifySettings var3 = new TLRPC.TL_account_resetNotifySettings();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$NotificationsSettingsActivity$aJyIXj_uVdoHSGnj_u9gTZci7Yo(this));
      }
   }

   // $FF: synthetic method
   public void lambda$null$6$NotificationsSettingsActivity(int var1) {
      this.adapter.notifyItemChanged(var1);
   }

   // $FF: synthetic method
   public void lambda$null$7$NotificationsSettingsActivity(int var1, DialogInterface var2, int var3) {
      byte var4 = 5;
      short var5;
      if (var3 == 1) {
         var5 = var4;
      } else if (var3 == 2) {
         var5 = 10;
      } else if (var3 == 3) {
         var5 = 30;
      } else if (var3 == 4) {
         var5 = 60;
      } else if (var3 == 5) {
         var5 = 120;
      } else if (var3 == 6) {
         var5 = 240;
      } else {
         var5 = 0;
      }

      MessagesController.getNotificationsSettings(super.currentAccount).edit().putInt("repeat_messages", var5).commit();
      this.adapter.notifyItemChanged(var1);
   }

   // $FF: synthetic method
   public void lambda$showExceptionsAlert$9$NotificationsSettingsActivity(ArrayList var1, DialogInterface var2, int var3) {
      this.presentFragment(new NotificationsCustomSettingsActivity(-1, var1));
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      if (var2 == -1) {
         Uri var4 = (Uri)var3.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
         Editor var5 = null;
         String var7 = var5;
         if (var4 != null) {
            Ringtone var6 = RingtoneManager.getRingtone(this.getParentActivity(), var4);
            var7 = var5;
            if (var6 != null) {
               if (var1 == this.callsRingtoneRow) {
                  if (var4.equals(System.DEFAULT_RINGTONE_URI)) {
                     var7 = LocaleController.getString("DefaultRingtone", 2131559226);
                  } else {
                     var7 = var6.getTitle(this.getParentActivity());
                  }
               } else if (var4.equals(System.DEFAULT_NOTIFICATION_URI)) {
                  var7 = LocaleController.getString("SoundDefault", 2131560801);
               } else {
                  var7 = var6.getTitle(this.getParentActivity());
               }

               var6.stop();
            }
         }

         var5 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
         if (var1 == this.callsRingtoneRow) {
            if (var7 != null && var4 != null) {
               var5.putString("CallsRingtone", var7);
               var5.putString("CallsRingtonePath", var4.toString());
            } else {
               var5.putString("CallsRingtone", "NoSound");
               var5.putString("CallsRingtonePath", "NoSound");
            }
         }

         var5.commit();
         this.adapter.notifyItemChanged(var1);
      }

   }

   public boolean onFragmentCreate() {
      MessagesController.getInstance(super.currentAccount).loadSignUpNotificationsSettings();
      this.loadExceptions();
      int var1;
      if (UserConfig.getActivatedAccountsCount() > 1) {
         var1 = this.rowCount++;
         this.accountsSectionRow = var1;
         var1 = this.rowCount++;
         this.accountsAllRow = var1;
         var1 = this.rowCount++;
         this.accountsInfoRow = var1;
      } else {
         this.accountsSectionRow = -1;
         this.accountsAllRow = -1;
         this.accountsInfoRow = -1;
      }

      var1 = this.rowCount++;
      this.notificationsSectionRow = var1;
      var1 = this.rowCount++;
      this.privateRow = var1;
      var1 = this.rowCount++;
      this.groupRow = var1;
      var1 = this.rowCount++;
      this.channelsRow = var1;
      var1 = this.rowCount++;
      this.notificationsSection2Row = var1;
      var1 = this.rowCount++;
      this.callsSectionRow = var1;
      var1 = this.rowCount++;
      this.callsVibrateRow = var1;
      var1 = this.rowCount++;
      this.callsRingtoneRow = var1;
      var1 = this.rowCount++;
      this.eventsSection2Row = var1;
      var1 = this.rowCount++;
      this.badgeNumberSection = var1;
      var1 = this.rowCount++;
      this.badgeNumberShowRow = var1;
      var1 = this.rowCount++;
      this.badgeNumberMutedRow = var1;
      var1 = this.rowCount++;
      this.badgeNumberMessagesRow = var1;
      var1 = this.rowCount++;
      this.badgeNumberSection2Row = var1;
      var1 = this.rowCount++;
      this.inappSectionRow = var1;
      var1 = this.rowCount++;
      this.inappSoundRow = var1;
      var1 = this.rowCount++;
      this.inappVibrateRow = var1;
      var1 = this.rowCount++;
      this.inappPreviewRow = var1;
      var1 = this.rowCount++;
      this.inchatSoundRow = var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = this.rowCount++;
         this.inappPriorityRow = var1;
      } else {
         this.inappPriorityRow = -1;
      }

      var1 = this.rowCount++;
      this.callsSection2Row = var1;
      var1 = this.rowCount++;
      this.eventsSectionRow = var1;
      var1 = this.rowCount++;
      this.contactJoinedRow = var1;
      var1 = this.rowCount++;
      this.pinnedMessageRow = var1;
      var1 = this.rowCount++;
      this.otherSection2Row = var1;
      var1 = this.rowCount++;
      this.otherSectionRow = var1;
      var1 = this.rowCount++;
      this.notificationsServiceConnectionRow = var1;
      this.androidAutoAlertRow = -1;
      var1 = this.rowCount++;
      this.repeatRow = var1;
      var1 = this.rowCount++;
      this.resetSection2Row = var1;
      var1 = this.rowCount++;
      this.resetSectionRow = var1;
      var1 = this.rowCount++;
      this.resetNotificationsRow = var1;
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
   }

   public void onResume() {
      super.onResume();
      NotificationsSettingsActivity.ListAdapter var1 = this.adapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return NotificationsSettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != NotificationsSettingsActivity.this.eventsSectionRow && var1 != NotificationsSettingsActivity.this.otherSectionRow && var1 != NotificationsSettingsActivity.this.resetSectionRow && var1 != NotificationsSettingsActivity.this.callsSectionRow && var1 != NotificationsSettingsActivity.this.badgeNumberSection && var1 != NotificationsSettingsActivity.this.inappSectionRow && var1 != NotificationsSettingsActivity.this.notificationsSectionRow && var1 != NotificationsSettingsActivity.this.accountsSectionRow) {
            if (var1 != NotificationsSettingsActivity.this.inappSoundRow && var1 != NotificationsSettingsActivity.this.inappVibrateRow && var1 != NotificationsSettingsActivity.this.notificationsServiceConnectionRow && var1 != NotificationsSettingsActivity.this.inappPreviewRow && var1 != NotificationsSettingsActivity.this.contactJoinedRow && var1 != NotificationsSettingsActivity.this.pinnedMessageRow && var1 != NotificationsSettingsActivity.this.notificationsServiceRow && var1 != NotificationsSettingsActivity.this.badgeNumberMutedRow && var1 != NotificationsSettingsActivity.this.badgeNumberMessagesRow && var1 != NotificationsSettingsActivity.this.badgeNumberShowRow && var1 != NotificationsSettingsActivity.this.inappPriorityRow && var1 != NotificationsSettingsActivity.this.inchatSoundRow && var1 != NotificationsSettingsActivity.this.androidAutoAlertRow && var1 != NotificationsSettingsActivity.this.accountsAllRow) {
               if (var1 == NotificationsSettingsActivity.this.resetNotificationsRow) {
                  return 2;
               } else if (var1 != NotificationsSettingsActivity.this.privateRow && var1 != NotificationsSettingsActivity.this.groupRow && var1 != NotificationsSettingsActivity.this.channelsRow) {
                  if (var1 != NotificationsSettingsActivity.this.eventsSection2Row && var1 != NotificationsSettingsActivity.this.notificationsSection2Row && var1 != NotificationsSettingsActivity.this.otherSection2Row && var1 != NotificationsSettingsActivity.this.resetSection2Row && var1 != NotificationsSettingsActivity.this.callsSection2Row && var1 != NotificationsSettingsActivity.this.badgeNumberSection2Row) {
                     return var1 == NotificationsSettingsActivity.this.accountsInfoRow ? 6 : 5;
                  } else {
                     return 4;
                  }
               } else {
                  return 3;
               }
            } else {
               return 1;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != NotificationsSettingsActivity.this.notificationsSectionRow && var2 != NotificationsSettingsActivity.this.notificationsSection2Row && var2 != NotificationsSettingsActivity.this.inappSectionRow && var2 != NotificationsSettingsActivity.this.eventsSectionRow && var2 != NotificationsSettingsActivity.this.otherSectionRow && var2 != NotificationsSettingsActivity.this.resetSectionRow && var2 != NotificationsSettingsActivity.this.badgeNumberSection && var2 != NotificationsSettingsActivity.this.otherSection2Row && var2 != NotificationsSettingsActivity.this.resetSection2Row && var2 != NotificationsSettingsActivity.this.callsSection2Row && var2 != NotificationsSettingsActivity.this.callsSectionRow && var2 != NotificationsSettingsActivity.this.badgeNumberSection2Row && var2 != NotificationsSettingsActivity.this.accountsSectionRow && var2 != NotificationsSettingsActivity.this.accountsInfoRow) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            int var4 = 0;
            if (var3 != 1) {
               if (var3 != 2) {
                  String var13;
                  if (var3 != 3) {
                     if (var3 != 5) {
                        if (var3 == 6) {
                           TextInfoPrivacyCell var11 = (TextInfoPrivacyCell)var1.itemView;
                           if (var2 == NotificationsSettingsActivity.this.accountsInfoRow) {
                              var11.setText(LocaleController.getString("ShowNotificationsForInfo", 2131560783));
                           }
                        }
                     } else {
                        TextSettingsCell var5 = (TextSettingsCell)var1.itemView;
                        SharedPreferences var12 = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.access$4200(NotificationsSettingsActivity.this));
                        if (var2 == NotificationsSettingsActivity.this.callsRingtoneRow) {
                           String var6 = var12.getString("CallsRingtone", LocaleController.getString("DefaultRingtone", 2131559226));
                           var13 = var6;
                           if (var6.equals("NoSound")) {
                              var13 = LocaleController.getString("NoSound", 2131559952);
                           }

                           var5.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131561092), var13, false);
                        } else if (var2 == NotificationsSettingsActivity.this.callsVibrateRow) {
                           if (var2 == NotificationsSettingsActivity.this.callsVibrateRow) {
                              var4 = var12.getInt("vibrate_calls", 0);
                           }

                           if (var4 == 0) {
                              var5.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
                           } else if (var4 == 1) {
                              var5.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
                           } else if (var4 == 2) {
                              var5.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
                           } else if (var4 == 3) {
                              var5.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
                           } else if (var4 == 4) {
                              var5.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("OnlyIfSilent", 2131560107), true);
                           }
                        } else if (var2 == NotificationsSettingsActivity.this.repeatRow) {
                           var2 = var12.getInt("repeat_messages", 60);
                           if (var2 == 0) {
                              var13 = LocaleController.getString("RepeatNotificationsNever", 2131560564);
                           } else if (var2 < 60) {
                              var13 = LocaleController.formatPluralString("Minutes", var2);
                           } else {
                              var13 = LocaleController.formatPluralString("Hours", var2 / 60);
                           }

                           var5.setTextAndValue(LocaleController.getString("RepeatNotifications", 2131560563), var13, false);
                        }
                     }
                  } else {
                     NotificationsCheckCell var15 = (NotificationsCheckCell)var1.itemView;
                     SharedPreferences var7 = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.access$3400(NotificationsSettingsActivity.this));
                     int var8 = ConnectionsManager.getInstance(NotificationsSettingsActivity.access$3500(NotificationsSettingsActivity.this)).getCurrentTime();
                     ArrayList var19;
                     if (var2 == NotificationsSettingsActivity.this.privateRow) {
                        var13 = LocaleController.getString("NotificationsPrivateChats", 2131560087);
                        var19 = NotificationsSettingsActivity.this.exceptionUsers;
                        var4 = var7.getInt("EnableAll2", 0);
                     } else if (var2 == NotificationsSettingsActivity.this.groupRow) {
                        var13 = LocaleController.getString("NotificationsGroups", 2131560071);
                        var19 = NotificationsSettingsActivity.this.exceptionChats;
                        var4 = var7.getInt("EnableGroup2", 0);
                     } else {
                        var13 = LocaleController.getString("NotificationsChannels", 2131560058);
                        var19 = NotificationsSettingsActivity.this.exceptionChannels;
                        var4 = var7.getInt("EnableChannel2", 0);
                     }

                     boolean var9;
                     if (var4 < var8) {
                        var9 = true;
                     } else {
                        var9 = false;
                     }

                     byte var14;
                     if (!var9 && var4 - 31536000 < var8) {
                        var14 = 2;
                     } else {
                        var14 = 0;
                     }

                     StringBuilder var20 = new StringBuilder();
                     if (var19 != null && !var19.isEmpty()) {
                        if (var4 < var8) {
                           var9 = true;
                        } else {
                           var9 = false;
                        }

                        if (var9) {
                           var20.append(LocaleController.getString("NotificationsOn", 2131560080));
                        } else if (var4 - 31536000 >= var8) {
                           var20.append(LocaleController.getString("NotificationsOff", 2131560078));
                        } else {
                           var20.append(LocaleController.formatString("NotificationsOffUntil", 2131560079, LocaleController.stringForMessageListDate((long)var4)));
                        }

                        if (var20.length() != 0) {
                           var20.append(", ");
                        }

                        var20.append(LocaleController.formatPluralString("Exception", var19.size()));
                     } else {
                        var20.append(LocaleController.getString("TapToChange", 2131560859));
                     }

                     boolean var10;
                     if (var2 != NotificationsSettingsActivity.this.channelsRow) {
                        var10 = true;
                     } else {
                        var10 = false;
                     }

                     var15.setTextAndValueAndCheck(var13, var20, var9, var14, var10);
                  }
               } else {
                  TextDetailSettingsCell var16 = (TextDetailSettingsCell)var1.itemView;
                  var16.setMultilineDetail(true);
                  if (var2 == NotificationsSettingsActivity.this.resetNotificationsRow) {
                     var16.setTextAndValue(LocaleController.getString("ResetAllNotifications", 2131560589), LocaleController.getString("UndoAllCustom", 2131560935), false);
                  }
               }
            } else {
               TextCheckCell var17 = (TextCheckCell)var1.itemView;
               SharedPreferences var21 = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.access$1500(NotificationsSettingsActivity.this));
               if (var2 == NotificationsSettingsActivity.this.inappSoundRow) {
                  var17.setTextAndCheck(LocaleController.getString("InAppSounds", 2131559659), var21.getBoolean("EnableInAppSounds", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.inappVibrateRow) {
                  var17.setTextAndCheck(LocaleController.getString("InAppVibrate", 2131559660), var21.getBoolean("EnableInAppVibrate", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.inappPreviewRow) {
                  var17.setTextAndCheck(LocaleController.getString("InAppPreview", 2131559658), var21.getBoolean("EnableInAppPreview", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.inappPriorityRow) {
                  var17.setTextAndCheck(LocaleController.getString("NotificationsImportance", 2131560072), var21.getBoolean("EnableInAppPriority", false), false);
               } else if (var2 == NotificationsSettingsActivity.this.contactJoinedRow) {
                  var17.setTextAndCheck(LocaleController.getString("ContactJoined", 2131559144), var21.getBoolean("EnableContactJoined", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.pinnedMessageRow) {
                  var17.setTextAndCheck(LocaleController.getString("PinnedMessages", 2131560452), var21.getBoolean("PinnedMessages", true), false);
               } else if (var2 == NotificationsSettingsActivity.this.androidAutoAlertRow) {
                  var17.setTextAndCheck("Android Auto", var21.getBoolean("EnableAutoNotifications", false), true);
               } else if (var2 == NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                  var17.setTextAndValueAndCheck(LocaleController.getString("NotificationsServiceConnection", 2131560089), "You won't be notified of new messages, if you disable this", var21.getBoolean("pushConnection", true), true, true);
               } else if (var2 == NotificationsSettingsActivity.this.badgeNumberShowRow) {
                  var17.setTextAndCheck(LocaleController.getString("BadgeNumberShow", 2131558828), NotificationsController.getInstance(NotificationsSettingsActivity.access$2500(NotificationsSettingsActivity.this)).showBadgeNumber, true);
               } else if (var2 == NotificationsSettingsActivity.this.badgeNumberMutedRow) {
                  var17.setTextAndCheck(LocaleController.getString("BadgeNumberMutedChats", 2131558827), NotificationsController.getInstance(NotificationsSettingsActivity.access$2700(NotificationsSettingsActivity.this)).showBadgeMuted, true);
               } else if (var2 == NotificationsSettingsActivity.this.badgeNumberMessagesRow) {
                  var17.setTextAndCheck(LocaleController.getString("BadgeNumberUnread", 2131558829), NotificationsController.getInstance(NotificationsSettingsActivity.access$2900(NotificationsSettingsActivity.this)).showBadgeMessages, false);
               } else if (var2 == NotificationsSettingsActivity.this.inchatSoundRow) {
                  var17.setTextAndCheck(LocaleController.getString("InChatSound", 2131559661), var21.getBoolean("EnableInChatSound", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.callsVibrateRow) {
                  var17.setTextAndCheck(LocaleController.getString("Vibrate", 2131561040), var21.getBoolean("EnableCallVibrate", true), true);
               } else if (var2 == NotificationsSettingsActivity.this.accountsAllRow) {
                  var17.setTextAndCheck(LocaleController.getString("AllAccounts", 2131558601), MessagesController.getGlobalNotificationsSettings().getBoolean("AllAccounts", true), false);
               }
            }
         } else {
            HeaderCell var18 = (HeaderCell)var1.itemView;
            if (var2 == NotificationsSettingsActivity.this.notificationsSectionRow) {
               var18.setText(LocaleController.getString("NotificationsForChats", 2131560068));
            } else if (var2 == NotificationsSettingsActivity.this.inappSectionRow) {
               var18.setText(LocaleController.getString("InAppNotifications", 2131559657));
            } else if (var2 == NotificationsSettingsActivity.this.eventsSectionRow) {
               var18.setText(LocaleController.getString("Events", 2131559468));
            } else if (var2 == NotificationsSettingsActivity.this.otherSectionRow) {
               var18.setText(LocaleController.getString("NotificationsOther", 2131560081));
            } else if (var2 == NotificationsSettingsActivity.this.resetSectionRow) {
               var18.setText(LocaleController.getString("Reset", 2131560583));
            } else if (var2 == NotificationsSettingsActivity.this.callsSectionRow) {
               var18.setText(LocaleController.getString("VoipNotificationSettings", 2131561075));
            } else if (var2 == NotificationsSettingsActivity.this.badgeNumberSection) {
               var18.setText(LocaleController.getString("BadgeNumber", 2131558826));
            } else if (var2 == NotificationsSettingsActivity.this.accountsSectionRow) {
               var18.setText(LocaleController.getString("ShowNotificationsFor", 2131560782));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 != 5) {
                           var3 = new TextInfoPrivacyCell(this.mContext);
                           ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        } else {
                           var3 = new TextSettingsCell(this.mContext);
                           ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                     } else {
                        var3 = new ShadowSectionCell(this.mContext);
                     }
                  } else {
                     var3 = new NotificationsCheckCell(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new TextDetailSettingsCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextCheckCell(this.mContext);
               ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new HeaderCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public static class NotificationException {
      public long did;
      public boolean hasCustom;
      public int muteUntil;
      public int notify;
   }
}
