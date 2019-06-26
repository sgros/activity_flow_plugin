package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
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
import android.os.Bundle;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells2.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ProfileNotificationsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private ProfileNotificationsActivity.ListAdapter adapter;
   private boolean addingException;
   private AnimatorSet animatorSet;
   private int avatarRow;
   private int avatarSectionRow;
   private int callsRow;
   private int callsVibrateRow;
   private int colorRow;
   private boolean customEnabled;
   private int customInfoRow;
   private int customRow;
   private ProfileNotificationsActivity.ProfileNotificationsActivityDelegate delegate;
   private long dialog_id;
   private int enableRow;
   private int generalRow;
   private int ledInfoRow;
   private int ledRow;
   private RecyclerListView listView;
   private boolean notificationsEnabled;
   private int popupDisabledRow;
   private int popupEnabledRow;
   private int popupInfoRow;
   private int popupRow;
   private int priorityInfoRow;
   private int priorityRow;
   private int ringtoneInfoRow;
   private int ringtoneRow;
   private int rowCount;
   private int smartRow;
   private int soundRow;
   private int vibrateRow;

   public ProfileNotificationsActivity(Bundle var1) {
      super(var1);
      this.dialog_id = var1.getLong("dialog_id");
      this.addingException = var1.getBoolean("exception", false);
   }

   // $FF: synthetic method
   static int access$1000(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1300(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1700(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2800(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$300(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3000(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3900(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4500(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4600(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4700(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4800(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4900(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$500(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5000(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$600(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(ProfileNotificationsActivity var0) {
      return var0.currentAccount;
   }

   public View createView(final Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            Editor var2;
            if (var1 == -1) {
               if (!ProfileNotificationsActivity.this.addingException && ProfileNotificationsActivity.this.notificationsEnabled && ProfileNotificationsActivity.this.customEnabled) {
                  var2 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$300(ProfileNotificationsActivity.this)).edit();
                  StringBuilder var3 = new StringBuilder();
                  var3.append("notify2_");
                  var3.append(ProfileNotificationsActivity.this.dialog_id);
                  var2.putInt(var3.toString(), 0).commit();
               }
            } else if (var1 == 1) {
               SharedPreferences var7 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$500(ProfileNotificationsActivity.this));
               var2 = var7.edit();
               StringBuilder var4 = new StringBuilder();
               var4.append("custom_");
               var4.append(ProfileNotificationsActivity.this.dialog_id);
               var2.putBoolean(var4.toString(), true);
               TLRPC.Dialog var8 = (TLRPC.Dialog)MessagesController.getInstance(ProfileNotificationsActivity.access$600(ProfileNotificationsActivity.this)).dialogs_dict.get(ProfileNotificationsActivity.this.dialog_id);
               StringBuilder var5;
               if (ProfileNotificationsActivity.this.notificationsEnabled) {
                  var5 = new StringBuilder();
                  var5.append("notify2_");
                  var5.append(ProfileNotificationsActivity.this.dialog_id);
                  var2.putInt(var5.toString(), 0);
                  MessagesStorage.getInstance(ProfileNotificationsActivity.access$700(ProfileNotificationsActivity.this)).setDialogFlags(ProfileNotificationsActivity.this.dialog_id, 0L);
                  if (var8 != null) {
                     var8.notify_settings = new TLRPC.TL_peerNotifySettings();
                  }
               } else {
                  var5 = new StringBuilder();
                  var5.append("notify2_");
                  var5.append(ProfileNotificationsActivity.this.dialog_id);
                  var2.putInt(var5.toString(), 2);
                  NotificationsController.getInstance(ProfileNotificationsActivity.access$800(ProfileNotificationsActivity.this)).removeNotificationsForDialog(ProfileNotificationsActivity.this.dialog_id);
                  MessagesStorage.getInstance(ProfileNotificationsActivity.access$900(ProfileNotificationsActivity.this)).setDialogFlags(ProfileNotificationsActivity.this.dialog_id, 1L);
                  if (var8 != null) {
                     var8.notify_settings = new TLRPC.TL_peerNotifySettings();
                     var8.notify_settings.mute_until = Integer.MAX_VALUE;
                  }
               }

               var2.commit();
               NotificationsController.getInstance(ProfileNotificationsActivity.access$1000(ProfileNotificationsActivity.this)).updateServerNotificationsSettings(ProfileNotificationsActivity.this.dialog_id);
               if (ProfileNotificationsActivity.this.delegate != null) {
                  NotificationsSettingsActivity.NotificationException var6 = new NotificationsSettingsActivity.NotificationException();
                  var6.did = ProfileNotificationsActivity.this.dialog_id;
                  var6.hasCustom = true;
                  var4 = new StringBuilder();
                  var4.append("notify2_");
                  var4.append(ProfileNotificationsActivity.this.dialog_id);
                  var6.notify = var7.getInt(var4.toString(), 0);
                  if (var6.notify != 0) {
                     var4 = new StringBuilder();
                     var4.append("notifyuntil_");
                     var4.append(ProfileNotificationsActivity.this.dialog_id);
                     var6.muteUntil = var7.getInt(var4.toString(), 0);
                  }

                  ProfileNotificationsActivity.this.delegate.didCreateNewException(var6);
               }
            }

            ProfileNotificationsActivity.this.finishFragment();
         }
      });
      if (this.addingException) {
         super.actionBar.setTitle(LocaleController.getString("NotificationsNewException", 2131560077));
         super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      } else {
         super.actionBar.setTitle(LocaleController.getString("CustomNotifications", 2131559188));
      }

      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      RecyclerListView var3 = this.listView;
      ProfileNotificationsActivity.ListAdapter var4 = new ProfileNotificationsActivity.ListAdapter(var1);
      this.adapter = var4;
      var3.setAdapter(var4);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setLayoutManager(new LinearLayoutManager(var1) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      });
      this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() {
         // $FF: synthetic method
         public void lambda$onItemClick$0$ProfileNotificationsActivity$3() {
            if (ProfileNotificationsActivity.this.adapter != null) {
               ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.vibrateRow);
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$1$ProfileNotificationsActivity$3() {
            if (ProfileNotificationsActivity.this.adapter != null) {
               ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.callsVibrateRow);
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$2$ProfileNotificationsActivity$3() {
            if (ProfileNotificationsActivity.this.adapter != null) {
               ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.priorityRow);
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$3$ProfileNotificationsActivity$3(View var1x, int var2) {
            if (var2 >= 0 && var2 < 100) {
               int var3 = var2 / 10;
               SharedPreferences var6 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$3300(ProfileNotificationsActivity.this));
               Editor var4 = var6.edit();
               StringBuilder var5 = new StringBuilder();
               var5.append("smart_max_count_");
               var5.append(ProfileNotificationsActivity.this.dialog_id);
               var4.putInt(var5.toString(), var2 % 10 + 1).commit();
               Editor var7 = var6.edit();
               StringBuilder var8 = new StringBuilder();
               var8.append("smart_delay_");
               var8.append(ProfileNotificationsActivity.this.dialog_id);
               var7.putInt(var8.toString(), (var3 + 1) * 60).commit();
               if (ProfileNotificationsActivity.this.adapter != null) {
                  ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.smartRow);
               }

               ProfileNotificationsActivity.this.dismissCurrentDialig();
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$4$ProfileNotificationsActivity$3(DialogInterface var1x, int var2) {
            Editor var3 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$3200(ProfileNotificationsActivity.this)).edit();
            StringBuilder var4 = new StringBuilder();
            var4.append("smart_max_count_");
            var4.append(ProfileNotificationsActivity.this.dialog_id);
            var3.putInt(var4.toString(), 0).commit();
            if (ProfileNotificationsActivity.this.adapter != null) {
               ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.smartRow);
            }

            ProfileNotificationsActivity.this.dismissCurrentDialig();
         }

         // $FF: synthetic method
         public void lambda$onItemClick$5$ProfileNotificationsActivity$3() {
            if (ProfileNotificationsActivity.this.adapter != null) {
               ProfileNotificationsActivity.this.adapter.notifyItemChanged(ProfileNotificationsActivity.this.colorRow);
            }

         }

         public void onItemClick(View var1x, int var2) {
            int var3 = ProfileNotificationsActivity.this.customRow;
            byte var4 = 0;
            int var25;
            StringBuilder var28;
            SharedPreferences var29;
            Editor var36;
            if (var2 == var3 && var1x instanceof TextCheckBoxCell) {
               var29 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$1300(ProfileNotificationsActivity.this));
               ProfileNotificationsActivity var35 = ProfileNotificationsActivity.this;
               var35.customEnabled = var35.customEnabled ^ true;
               var35 = ProfileNotificationsActivity.this;
               var35.notificationsEnabled = var35.customEnabled;
               var36 = var29.edit();
               var28 = new StringBuilder();
               var28.append("custom_");
               var28.append(ProfileNotificationsActivity.this.dialog_id);
               var36.putBoolean(var28.toString(), ProfileNotificationsActivity.this.customEnabled).commit();
               ((TextCheckBoxCell)var1x).setChecked(ProfileNotificationsActivity.this.customEnabled);
               var3 = ProfileNotificationsActivity.this.listView.getChildCount();
               ArrayList var37 = new ArrayList();

               for(var2 = var4; var2 < var3; ++var2) {
                  View var38 = ProfileNotificationsActivity.this.listView.getChildAt(var2);
                  RecyclerListView.Holder var39 = (RecyclerListView.Holder)ProfileNotificationsActivity.this.listView.getChildViewHolder(var38);
                  var25 = var39.getItemViewType();
                  if (var39.getAdapterPosition() != ProfileNotificationsActivity.this.customRow && var25 != 0) {
                     if (var25 != 1) {
                        if (var25 != 2) {
                           if (var25 != 3) {
                              if (var25 == 4) {
                                 ((RadioCell)var39.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, var37);
                              }
                           } else {
                              ((TextColorCell)var39.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, var37);
                           }
                        } else {
                           ((TextInfoPrivacyCell)var39.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, var37);
                        }
                     } else {
                        ((TextSettingsCell)var39.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, var37);
                     }
                  }
               }

               if (!var37.isEmpty()) {
                  if (ProfileNotificationsActivity.this.animatorSet != null) {
                     ProfileNotificationsActivity.this.animatorSet.cancel();
                  }

                  ProfileNotificationsActivity.this.animatorSet = new AnimatorSet();
                  ProfileNotificationsActivity.this.animatorSet.playTogether(var37);
                  ProfileNotificationsActivity.this.animatorSet.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        if (var1x.equals(ProfileNotificationsActivity.this.animatorSet)) {
                           ProfileNotificationsActivity.this.animatorSet = null;
                        }

                     }
                  });
                  ProfileNotificationsActivity.this.animatorSet.setDuration(150L);
                  ProfileNotificationsActivity.this.animatorSet.start();
               }
            } else if (ProfileNotificationsActivity.this.customEnabled) {
               var25 = ProfileNotificationsActivity.this.soundRow;
               Uri var7 = null;
               Uri var6 = null;
               String var5;
               Intent var8;
               StringBuilder var9;
               Exception var10000;
               boolean var10001;
               SharedPreferences var22;
               Uri var23;
               Exception var24;
               String var40;
               if (var2 == var25) {
                  label205: {
                     try {
                        var8 = new Intent("android.intent.action.RINGTONE_PICKER");
                        var8.putExtra("android.intent.extra.ringtone.TYPE", 2);
                        var8.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                        var8.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                        var8.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(2));
                        var22 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$1700(ProfileNotificationsActivity.this));
                        var7 = System.DEFAULT_NOTIFICATION_URI;
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label205;
                     }

                     if (var7 != null) {
                        try {
                           var5 = var7.getPath();
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label205;
                        }
                     } else {
                        var5 = null;
                     }

                     try {
                        var9 = new StringBuilder();
                        var9.append("sound_path_");
                        var9.append(ProfileNotificationsActivity.this.dialog_id);
                        var40 = var22.getString(var9.toString(), var5);
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label205;
                     }

                     var23 = var6;
                     if (var40 != null) {
                        label206: {
                           var23 = var6;

                           label179: {
                              try {
                                 if (var40.equals("NoSound")) {
                                    break label206;
                                 }

                                 if (var40.equals(var5)) {
                                    break label179;
                                 }
                              } catch (Exception var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label205;
                              }

                              try {
                                 var23 = Uri.parse(var40);
                                 break label206;
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label205;
                              }
                           }

                           var23 = var7;
                        }
                     }

                     try {
                        var8.putExtra("android.intent.extra.ringtone.EXISTING_URI", var23);
                        ProfileNotificationsActivity.this.startActivityForResult(var8, 12);
                        return;
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                     }
                  }

                  var24 = var10000;
                  FileLog.e((Throwable)var24);
               } else if (var2 == ProfileNotificationsActivity.this.ringtoneRow) {
                  label207: {
                     try {
                        var8 = new Intent("android.intent.action.RINGTONE_PICKER");
                        var8.putExtra("android.intent.extra.ringtone.TYPE", 1);
                        var8.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                        var8.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                        var8.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                        var22 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$1900(ProfileNotificationsActivity.this));
                        var6 = System.DEFAULT_NOTIFICATION_URI;
                     } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label207;
                     }

                     if (var6 != null) {
                        try {
                           var5 = var6.getPath();
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label207;
                        }
                     } else {
                        var5 = null;
                     }

                     try {
                        var9 = new StringBuilder();
                        var9.append("ringtone_path_");
                        var9.append(ProfileNotificationsActivity.this.dialog_id);
                        var40 = var22.getString(var9.toString(), var5);
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label207;
                     }

                     var23 = var7;
                     if (var40 != null) {
                        label208: {
                           var23 = var7;

                           label150: {
                              try {
                                 if (var40.equals("NoSound")) {
                                    break label208;
                                 }

                                 if (var40.equals(var5)) {
                                    break label150;
                                 }
                              } catch (Exception var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break label207;
                              }

                              try {
                                 var23 = Uri.parse(var40);
                                 break label208;
                              } catch (Exception var11) {
                                 var10000 = var11;
                                 var10001 = false;
                                 break label207;
                              }
                           }

                           var23 = var6;
                        }
                     }

                     try {
                        var8.putExtra("android.intent.extra.ringtone.EXISTING_URI", var23);
                        ProfileNotificationsActivity.this.startActivityForResult(var8, 13);
                        return;
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                     }
                  }

                  var24 = var10000;
                  FileLog.e((Throwable)var24);
               } else {
                  ProfileNotificationsActivity var26;
                  if (var2 == ProfileNotificationsActivity.this.vibrateRow) {
                     var26 = ProfileNotificationsActivity.this;
                     var26.showDialog(AlertsCreator.createVibrationSelectDialog(var26.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, false, false, new _$$Lambda$ProfileNotificationsActivity$3$2IDon9XZ3wORVvesLoqeJ8GuKLA(this)));
                  } else if (var2 == ProfileNotificationsActivity.this.enableRow) {
                     TextCheckCell var27 = (TextCheckCell)var1x;
                     ProfileNotificationsActivity.this.notificationsEnabled = var27.isChecked() ^ true;
                     var27.setChecked(ProfileNotificationsActivity.this.notificationsEnabled);
                  } else if (var2 == ProfileNotificationsActivity.this.callsVibrateRow) {
                     var26 = ProfileNotificationsActivity.this;
                     var26.showDialog(AlertsCreator.createVibrationSelectDialog(var26.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, "calls_vibrate_", new _$$Lambda$ProfileNotificationsActivity$3$PrZXv2W2aGcQ_SHFSnntK6Tp69w(this)));
                  } else if (var2 == ProfileNotificationsActivity.this.priorityRow) {
                     var26 = ProfileNotificationsActivity.this;
                     var26.showDialog(AlertsCreator.createPrioritySelectDialog(var26.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, -1, new _$$Lambda$ProfileNotificationsActivity$3$mwfzE5pr44nJotHZxBv3FvA5wOc(this)));
                  } else if (var2 == ProfileNotificationsActivity.this.smartRow) {
                     if (ProfileNotificationsActivity.this.getParentActivity() == null) {
                        return;
                     }

                     final Activity var30 = ProfileNotificationsActivity.this.getParentActivity();
                     var29 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$2500(ProfileNotificationsActivity.this));
                     var28 = new StringBuilder();
                     var28.append("smart_max_count_");
                     var28.append(ProfileNotificationsActivity.this.dialog_id);
                     var25 = var29.getInt(var28.toString(), 2);
                     var28 = new StringBuilder();
                     var28.append("smart_delay_");
                     var28.append(ProfileNotificationsActivity.this.dialog_id);
                     var3 = var29.getInt(var28.toString(), 180);
                     var2 = var25;
                     if (var25 == 0) {
                        var2 = 2;
                     }

                     var25 = var3 / 60;
                     RecyclerListView var32 = new RecyclerListView(ProfileNotificationsActivity.this.getParentActivity());
                     var32.setLayoutManager(new LinearLayoutManager(var1, 1, false));
                     var32.setClipToPadding(true);
                     var32.setAdapter(new RecyclerListView.SelectionAdapter((var25 - 1) * 10 + var2 - 1) {
                        // $FF: synthetic field
                        final int val$selected;

                        {
                           this.val$selected = var3;
                        }

                        public int getItemCount() {
                           return 100;
                        }

                        public boolean isEnabled(RecyclerView.ViewHolder var1x) {
                           return true;
                        }

                        public void onBindViewHolder(RecyclerView.ViewHolder var1x, int var2) {
                           TextView var3 = (TextView)var1x.itemView;
                           String var5;
                           if (var2 == this.val$selected) {
                              var5 = "dialogTextGray";
                           } else {
                              var5 = "dialogTextBlack";
                           }

                           var3.setTextColor(Theme.getColor(var5));
                           int var4 = var2 / 10;
                           var3.setText(LocaleController.formatString("SmartNotificationsDetail", 2131560790, LocaleController.formatPluralString("Times", var2 % 10 + 1), LocaleController.formatPluralString("Minutes", var4 + 1)));
                        }

                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1x, int var2) {
                           TextView var3 = new TextView(var30) {
                              protected void onMeasure(int var1x, int var2) {
                                 super.onMeasure(MeasureSpec.makeMeasureSpec(var1x, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
                              }
                           };
                           var3.setGravity(17);
                           var3.setTextSize(1, 18.0F);
                           var3.setSingleLine(true);
                           var3.setEllipsize(TruncateAt.END);
                           var3.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                           return new RecyclerListView.Holder(var3);
                        }
                     });
                     var32.setPadding(0, AndroidUtilities.dp(12.0F), 0, AndroidUtilities.dp(8.0F));
                     var32.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ProfileNotificationsActivity$3$NllPggjTkVFXgR__HalhnaOhR0w(this)));
                     AlertDialog.Builder var34 = new AlertDialog.Builder(ProfileNotificationsActivity.this.getParentActivity());
                     var34.setTitle(LocaleController.getString("SmartNotificationsAlert", 2131560789));
                     var34.setView(var32);
                     var34.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     var34.setNegativeButton(LocaleController.getString("SmartNotificationsDisabled", 2131560791), new _$$Lambda$ProfileNotificationsActivity$3$C5kBfrMiInE1eRjHc3NVVjNb77Q(this));
                     ProfileNotificationsActivity.this.showDialog(var34.create());
                  } else if (var2 == ProfileNotificationsActivity.this.colorRow) {
                     if (ProfileNotificationsActivity.this.getParentActivity() == null) {
                        return;
                     }

                     var26 = ProfileNotificationsActivity.this;
                     var26.showDialog(AlertsCreator.createColorSelectDialog(var26.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, -1, new _$$Lambda$ProfileNotificationsActivity$3$9hbVVL7iBMMo3IfpTFOUhufLFBM(this)));
                  } else if (var2 == ProfileNotificationsActivity.this.popupEnabledRow) {
                     Editor var31 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$2800(ProfileNotificationsActivity.this)).edit();
                     StringBuilder var33 = new StringBuilder();
                     var33.append("popup_");
                     var33.append(ProfileNotificationsActivity.this.dialog_id);
                     var31.putInt(var33.toString(), 1).commit();
                     ((RadioCell)var1x).setChecked(true, true);
                     var1x = ProfileNotificationsActivity.this.listView.findViewWithTag(2);
                     if (var1x != null) {
                        ((RadioCell)var1x).setChecked(false, true);
                     }
                  } else if (var2 == ProfileNotificationsActivity.this.popupDisabledRow) {
                     var36 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$3000(ProfileNotificationsActivity.this)).edit();
                     var28 = new StringBuilder();
                     var28.append("popup_");
                     var28.append(ProfileNotificationsActivity.this.dialog_id);
                     var36.putInt(var28.toString(), 2).commit();
                     ((RadioCell)var1x).setChecked(true, true);
                     var1x = ProfileNotificationsActivity.this.listView.findViewWithTag(1);
                     if (var1x != null) {
                        ((RadioCell)var1x).setChecked(false, true);
                     }
                  }
               }
            }

         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.notificationsSettingsUpdated) {
         this.adapter.notifyDataSetChanged();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M var1 = new _$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextSettingsCell.class, TextColorCell.class, RadioCell.class, UserCell.class, TextCheckCell.class, TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var12 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var32 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var13 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText");
      ThemeDescription var16 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var17 = new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var18 = new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var19 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground");
      ThemeDescription var20 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked");
      ThemeDescription var21 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var22 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var31 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var23 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack");
      ThemeDescription var24 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked");
      ThemeDescription var25 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var26 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var27 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var28 = this.listView;
      Drawable var29 = Theme.avatar_broadcastDrawable;
      Drawable var30 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var12, var32, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var31, var23, var24, var25, var26, var27, new ThemeDescription(var28, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var29, var30}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckBoxCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareUnchecked"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareDisabled"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareBackground"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareCheck")};
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$0$ProfileNotificationsActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            }
         }
      }

   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      if (var2 == -1) {
         if (var3 == null) {
            return;
         }

         Uri var4 = (Uri)var3.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
         Editor var5 = null;
         String var7 = var5;
         if (var4 != null) {
            Ringtone var6 = RingtoneManager.getRingtone(ApplicationLoader.applicationContext, var4);
            var7 = var5;
            if (var6 != null) {
               if (var1 == 13) {
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
         StringBuilder var8;
         StringBuilder var9;
         if (var1 == 12) {
            if (var7 != null) {
               var9 = new StringBuilder();
               var9.append("sound_");
               var9.append(this.dialog_id);
               var5.putString(var9.toString(), var7);
               var8 = new StringBuilder();
               var8.append("sound_path_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), var4.toString());
            } else {
               var8 = new StringBuilder();
               var8.append("sound_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), "NoSound");
               var8 = new StringBuilder();
               var8.append("sound_path_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), "NoSound");
            }
         } else if (var1 == 13) {
            if (var7 != null) {
               var9 = new StringBuilder();
               var9.append("ringtone_");
               var9.append(this.dialog_id);
               var5.putString(var9.toString(), var7);
               var8 = new StringBuilder();
               var8.append("ringtone_path_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), var4.toString());
            } else {
               var8 = new StringBuilder();
               var8.append("ringtone_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), "NoSound");
               var8 = new StringBuilder();
               var8.append("ringtone_path_");
               var8.append(this.dialog_id);
               var5.putString(var8.toString(), "NoSound");
            }
         }

         var5.commit();
         ProfileNotificationsActivity.ListAdapter var10 = this.adapter;
         if (var10 != null) {
            if (var1 == 13) {
               var1 = this.ringtoneRow;
            } else {
               var1 = this.soundRow;
            }

            var10.notifyItemChanged(var1);
         }
      }

   }

   public boolean onFragmentCreate() {
      this.rowCount = 0;
      int var1;
      if (this.addingException) {
         var1 = this.rowCount++;
         this.avatarRow = var1;
         var1 = this.rowCount++;
         this.avatarSectionRow = var1;
         this.customRow = -1;
         this.customInfoRow = -1;
      } else {
         this.avatarRow = -1;
         this.avatarSectionRow = -1;
         var1 = this.rowCount++;
         this.customRow = var1;
         var1 = this.rowCount++;
         this.customInfoRow = var1;
      }

      var1 = this.rowCount++;
      this.generalRow = var1;
      if (this.addingException) {
         var1 = this.rowCount++;
         this.enableRow = var1;
      } else {
         this.enableRow = -1;
      }

      var1 = this.rowCount++;
      this.soundRow = var1;
      var1 = this.rowCount++;
      this.vibrateRow = var1;
      if ((int)this.dialog_id < 0) {
         var1 = this.rowCount++;
         this.smartRow = var1;
      } else {
         this.smartRow = -1;
      }

      if (VERSION.SDK_INT >= 21) {
         var1 = this.rowCount++;
         this.priorityRow = var1;
      } else {
         this.priorityRow = -1;
      }

      int var2;
      boolean var7;
      label71: {
         var1 = this.rowCount++;
         this.priorityInfoRow = var1;
         var2 = (int)this.dialog_id;
         if (var2 < 0) {
            TLRPC.Chat var3 = MessagesController.getInstance(super.currentAccount).getChat(-var2);
            if (ChatObject.isChannel(var3) && !var3.megagroup) {
               var7 = true;
               break label71;
            }
         }

         var7 = false;
      }

      if (var2 != 0 && !var7) {
         var1 = this.rowCount++;
         this.popupRow = var1;
         var1 = this.rowCount++;
         this.popupEnabledRow = var1;
         var1 = this.rowCount++;
         this.popupDisabledRow = var1;
         var1 = this.rowCount++;
         this.popupInfoRow = var1;
      } else {
         this.popupRow = -1;
         this.popupEnabledRow = -1;
         this.popupDisabledRow = -1;
         this.popupInfoRow = -1;
      }

      if (var2 > 0) {
         var1 = this.rowCount++;
         this.callsRow = var1;
         var1 = this.rowCount++;
         this.callsVibrateRow = var1;
         var1 = this.rowCount++;
         this.ringtoneRow = var1;
         var1 = this.rowCount++;
         this.ringtoneInfoRow = var1;
      } else {
         this.callsRow = -1;
         this.callsVibrateRow = -1;
         this.ringtoneRow = -1;
         this.ringtoneInfoRow = -1;
      }

      var1 = this.rowCount++;
      this.ledRow = var1;
      var1 = this.rowCount++;
      this.colorRow = var1;
      var1 = this.rowCount++;
      this.ledInfoRow = var1;
      SharedPreferences var6 = MessagesController.getNotificationsSettings(super.currentAccount);
      StringBuilder var4 = new StringBuilder();
      var4.append("custom_");
      var4.append(this.dialog_id);
      boolean var5;
      if (!var6.getBoolean(var4.toString(), false) && !this.addingException) {
         var5 = false;
      } else {
         var5 = true;
      }

      this.customEnabled = var5;
      var4 = new StringBuilder();
      var4.append("notify2_");
      var4.append(this.dialog_id);
      var5 = var6.contains(var4.toString());
      var4 = new StringBuilder();
      var4.append("notify2_");
      var4.append(this.dialog_id);
      var1 = var6.getInt(var4.toString(), 0);
      if (var1 == 0) {
         if (var5) {
            this.notificationsEnabled = true;
         } else {
            this.notificationsEnabled = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.dialog_id);
         }
      } else if (var1 == 1) {
         this.notificationsEnabled = true;
      } else if (var1 == 2) {
         this.notificationsEnabled = false;
      } else {
         this.notificationsEnabled = false;
      }

      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
   }

   public void setDelegate(ProfileNotificationsActivity.ProfileNotificationsActivityDelegate var1) {
      this.delegate = var1;
   }

   private class ListAdapter extends RecyclerView.Adapter {
      private Context context;

      public ListAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         return ProfileNotificationsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != ProfileNotificationsActivity.this.generalRow && var1 != ProfileNotificationsActivity.this.popupRow && var1 != ProfileNotificationsActivity.this.ledRow && var1 != ProfileNotificationsActivity.this.callsRow) {
            if (var1 != ProfileNotificationsActivity.this.soundRow && var1 != ProfileNotificationsActivity.this.vibrateRow && var1 != ProfileNotificationsActivity.this.priorityRow && var1 != ProfileNotificationsActivity.this.smartRow && var1 != ProfileNotificationsActivity.this.ringtoneRow && var1 != ProfileNotificationsActivity.this.callsVibrateRow) {
               if (var1 != ProfileNotificationsActivity.this.popupInfoRow && var1 != ProfileNotificationsActivity.this.ledInfoRow && var1 != ProfileNotificationsActivity.this.priorityInfoRow && var1 != ProfileNotificationsActivity.this.customInfoRow && var1 != ProfileNotificationsActivity.this.ringtoneInfoRow) {
                  if (var1 == ProfileNotificationsActivity.this.colorRow) {
                     return 3;
                  } else if (var1 != ProfileNotificationsActivity.this.popupEnabledRow && var1 != ProfileNotificationsActivity.this.popupDisabledRow) {
                     if (var1 == ProfileNotificationsActivity.this.customRow) {
                        return 5;
                     } else if (var1 == ProfileNotificationsActivity.this.avatarRow) {
                        return 6;
                     } else if (var1 == ProfileNotificationsActivity.this.avatarSectionRow) {
                        return 7;
                     } else {
                        return var1 == ProfileNotificationsActivity.this.enableRow ? 8 : 0;
                     }
                  } else {
                     return 4;
                  }
               } else {
                  return 2;
               }
            } else {
               return 1;
            }
         } else {
            return 0;
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         boolean var5 = true;
         boolean var6 = true;
         boolean var7 = true;
         boolean var8 = true;
         boolean var9 = true;
         boolean var10 = true;
         boolean var11 = true;
         boolean var12 = false;
         int var15;
         String var20;
         String var25;
         switch(var3) {
         case 0:
            HeaderCell var24 = (HeaderCell)var1.itemView;
            if (var2 == ProfileNotificationsActivity.this.generalRow) {
               var24.setText(LocaleController.getString("General", 2131559587));
            } else if (var2 == ProfileNotificationsActivity.this.popupRow) {
               var24.setText(LocaleController.getString("ProfilePopupNotification", 2131560514));
            } else if (var2 == ProfileNotificationsActivity.this.ledRow) {
               var24.setText(LocaleController.getString("NotificationsLed", 2131560073));
            } else if (var2 == ProfileNotificationsActivity.this.callsRow) {
               var24.setText(LocaleController.getString("VoipNotificationSettings", 2131561075));
            }
            break;
         case 1:
            TextSettingsCell var30 = (TextSettingsCell)var1.itemView;
            SharedPreferences var23 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$3900(ProfileNotificationsActivity.this));
            StringBuilder var29;
            if (var2 == ProfileNotificationsActivity.this.soundRow) {
               var29 = new StringBuilder();
               var29.append("sound_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var25 = var23.getString(var29.toString(), LocaleController.getString("SoundDefault", 2131560801));
               var20 = var25;
               if (var25.equals("NoSound")) {
                  var20 = LocaleController.getString("NoSound", 2131559952);
               }

               var30.setTextAndValue(LocaleController.getString("Sound", 2131560800), var20, true);
            } else if (var2 == ProfileNotificationsActivity.this.ringtoneRow) {
               var29 = new StringBuilder();
               var29.append("ringtone_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var25 = var23.getString(var29.toString(), LocaleController.getString("DefaultRingtone", 2131559226));
               var20 = var25;
               if (var25.equals("NoSound")) {
                  var20 = LocaleController.getString("NoSound", 2131559952);
               }

               var30.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131561092), var20, false);
            } else if (var2 == ProfileNotificationsActivity.this.vibrateRow) {
               var29 = new StringBuilder();
               var29.append("vibrate_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var2 = var23.getInt(var29.toString(), 0);
               if (var2 != 0 && var2 != 4) {
                  if (var2 == 1) {
                     var25 = LocaleController.getString("Vibrate", 2131561040);
                     var20 = LocaleController.getString("Short", 2131560775);
                     var12 = var5;
                     if (ProfileNotificationsActivity.this.smartRow == -1) {
                        if (ProfileNotificationsActivity.this.priorityRow != -1) {
                           var12 = var5;
                        } else {
                           var12 = false;
                        }
                     }

                     var30.setTextAndValue(var25, var20, var12);
                  } else if (var2 == 2) {
                     var20 = LocaleController.getString("Vibrate", 2131561040);
                     var25 = LocaleController.getString("VibrationDisabled", 2131561042);
                     var12 = var6;
                     if (ProfileNotificationsActivity.this.smartRow == -1) {
                        if (ProfileNotificationsActivity.this.priorityRow != -1) {
                           var12 = var6;
                        } else {
                           var12 = false;
                        }
                     }

                     var30.setTextAndValue(var20, var25, var12);
                  } else if (var2 == 3) {
                     var20 = LocaleController.getString("Vibrate", 2131561040);
                     var25 = LocaleController.getString("Long", 2131559790);
                     var12 = var7;
                     if (ProfileNotificationsActivity.this.smartRow == -1) {
                        if (ProfileNotificationsActivity.this.priorityRow != -1) {
                           var12 = var7;
                        } else {
                           var12 = false;
                        }
                     }

                     var30.setTextAndValue(var20, var25, var12);
                  }
               } else {
                  var25 = LocaleController.getString("Vibrate", 2131561040);
                  var20 = LocaleController.getString("VibrationDefault", 2131561041);
                  var12 = var8;
                  if (ProfileNotificationsActivity.this.smartRow == -1) {
                     if (ProfileNotificationsActivity.this.priorityRow != -1) {
                        var12 = var8;
                     } else {
                        var12 = false;
                     }
                  }

                  var30.setTextAndValue(var25, var20, var12);
               }
            } else if (var2 == ProfileNotificationsActivity.this.priorityRow) {
               var29 = new StringBuilder();
               var29.append("priority_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var2 = var23.getInt(var29.toString(), 3);
               if (var2 == 0) {
                  var30.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityHigh", 2131560082), false);
               } else if (var2 != 1 && var2 != 2) {
                  if (var2 == 3) {
                     var30.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPrioritySettings", 2131560085), false);
                  } else if (var2 == 4) {
                     var30.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityLow", 2131560083), false);
                  } else if (var2 == 5) {
                     var30.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityMedium", 2131560084), false);
                  }
               } else {
                  var30.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityUrgent", 2131560086), false);
               }
            } else if (var2 == ProfileNotificationsActivity.this.smartRow) {
               var29 = new StringBuilder();
               var29.append("smart_max_count_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var3 = var23.getInt(var29.toString(), 2);
               var29 = new StringBuilder();
               var29.append("smart_delay_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var2 = var23.getInt(var29.toString(), 180);
               if (var3 == 0) {
                  var20 = LocaleController.getString("SmartNotifications", 2131560788);
                  var25 = LocaleController.getString("SmartNotificationsDisabled", 2131560791);
                  if (ProfileNotificationsActivity.this.priorityRow != -1) {
                     var12 = var9;
                  } else {
                     var12 = false;
                  }

                  var30.setTextAndValue(var20, var25, var12);
               } else {
                  var25 = LocaleController.formatPluralString("Minutes", var2 / 60);
                  var20 = LocaleController.getString("SmartNotifications", 2131560788);
                  var25 = LocaleController.formatString("SmartNotificationsInfo", 2131560792, var3, var25);
                  if (ProfileNotificationsActivity.this.priorityRow != -1) {
                     var12 = var10;
                  } else {
                     var12 = false;
                  }

                  var30.setTextAndValue(var20, var25, var12);
               }
            } else if (var2 == ProfileNotificationsActivity.this.callsVibrateRow) {
               var29 = new StringBuilder();
               var29.append("calls_vibrate_");
               var29.append(ProfileNotificationsActivity.this.dialog_id);
               var2 = var23.getInt(var29.toString(), 0);
               if (var2 != 0 && var2 != 4) {
                  if (var2 == 1) {
                     var30.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
                  } else if (var2 == 2) {
                     var30.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
                  } else if (var2 == 3) {
                     var30.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
                  }
               } else {
                  var30.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
               }
            }
            break;
         case 2:
            TextInfoPrivacyCell var22 = (TextInfoPrivacyCell)var1.itemView;
            if (var2 == ProfileNotificationsActivity.this.popupInfoRow) {
               var22.setText(LocaleController.getString("ProfilePopupNotificationInfo", 2131560515));
               var22.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
            } else if (var2 == ProfileNotificationsActivity.this.ledInfoRow) {
               var22.setText(LocaleController.getString("NotificationsLedInfo", 2131560075));
               var22.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165395, "windowBackgroundGrayShadow"));
            } else if (var2 == ProfileNotificationsActivity.this.priorityInfoRow) {
               if (ProfileNotificationsActivity.this.priorityRow == -1) {
                  var22.setText("");
               } else {
                  var22.setText(LocaleController.getString("PriorityInfo", 2131560473));
               }

               var22.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
            } else if (var2 == ProfileNotificationsActivity.this.customInfoRow) {
               var22.setText((CharSequence)null);
               var22.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
            } else if (var2 == ProfileNotificationsActivity.this.ringtoneInfoRow) {
               var22.setText(LocaleController.getString("VoipRingtoneInfo", 2131561091));
               var22.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
            }
            break;
         case 3:
            TextColorCell var21 = (TextColorCell)var1.itemView;
            SharedPreferences var27 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$4500(ProfileNotificationsActivity.this));
            StringBuilder var28 = new StringBuilder();
            var28.append("color_");
            var28.append(ProfileNotificationsActivity.this.dialog_id);
            if (var27.contains(var28.toString())) {
               var28 = new StringBuilder();
               var28.append("color_");
               var28.append(ProfileNotificationsActivity.this.dialog_id);
               var2 = var27.getInt(var28.toString(), -16776961);
            } else if ((int)ProfileNotificationsActivity.this.dialog_id < 0) {
               var2 = var27.getInt("GroupLed", -16776961);
            } else {
               var2 = var27.getInt("MessagesLed", -16776961);
            }

            var3 = 0;

            while(true) {
               var15 = var2;
               if (var3 >= 9) {
                  break;
               }

               if (TextColorCell.colorsToSave[var3] == var2) {
                  var15 = TextColorCell.colors[var3];
                  break;
               }

               ++var3;
            }

            var21.setTextAndColor(LocaleController.getString("NotificationsLedColor", 2131560074), var15, false);
            break;
         case 4:
            RadioCell var26 = (RadioCell)var1.itemView;
            SharedPreferences var14 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$4600(ProfileNotificationsActivity.this));
            StringBuilder var19 = new StringBuilder();
            var19.append("popup_");
            var19.append(ProfileNotificationsActivity.this.dialog_id);
            var15 = var14.getInt(var19.toString(), 0);
            var3 = var15;
            if (var15 == 0) {
               if ((int)ProfileNotificationsActivity.this.dialog_id < 0) {
                  var20 = "popupGroup";
               } else {
                  var20 = "popupAll";
               }

               if (var14.getInt(var20, 0) != 0) {
                  var3 = 1;
               } else {
                  var3 = 2;
               }
            }

            if (var2 == ProfileNotificationsActivity.this.popupEnabledRow) {
               var20 = LocaleController.getString("PopupEnabled", 2131560470);
               if (var3 == 1) {
                  var12 = true;
               }

               var26.setText(var20, var12, true);
               var26.setTag(1);
            } else if (var2 == ProfileNotificationsActivity.this.popupDisabledRow) {
               var20 = LocaleController.getString("PopupDisabled", 2131560469);
               if (var3 == 2) {
                  var12 = var4;
               } else {
                  var12 = false;
               }

               var26.setText(var20, var12, false);
               var26.setTag(2);
            }
            break;
         case 5:
            TextCheckBoxCell var18 = (TextCheckBoxCell)var1.itemView;
            MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$4700(ProfileNotificationsActivity.this));
            var25 = LocaleController.getString("NotificationsEnableCustom", 2131560063);
            if (ProfileNotificationsActivity.this.customEnabled && ProfileNotificationsActivity.this.notificationsEnabled) {
               var12 = var11;
            } else {
               var12 = false;
            }

            var18.setTextAndCheck(var25, var12, false);
            break;
         case 6:
            UserCell var13 = (UserCell)var1.itemView;
            var2 = (int)ProfileNotificationsActivity.this.dialog_id;
            Object var17;
            if (var2 > 0) {
               var17 = MessagesController.getInstance(ProfileNotificationsActivity.access$4800(ProfileNotificationsActivity.this)).getUser(var2);
            } else {
               var17 = MessagesController.getInstance(ProfileNotificationsActivity.access$4900(ProfileNotificationsActivity.this)).getChat(-var2);
            }

            var13.setData((TLObject)var17, (CharSequence)null, (CharSequence)null, 0);
         case 7:
         default:
            break;
         case 8:
            TextCheckCell var16 = (TextCheckCell)var1.itemView;
            MessagesController.getNotificationsSettings(ProfileNotificationsActivity.access$5000(ProfileNotificationsActivity.this));
            if (var2 == ProfileNotificationsActivity.this.enableRow) {
               var16.setTextAndCheck(LocaleController.getString("Notifications", 2131560055), ProfileNotificationsActivity.this.notificationsEnabled, true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         switch(var2) {
         case 0:
            var3 = new HeaderCell(this.context);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 1:
            var3 = new TextSettingsCell(this.context);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 2:
            var3 = new TextInfoPrivacyCell(this.context);
            break;
         case 3:
            var3 = new TextColorCell(this.context);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 4:
            var3 = new RadioCell(this.context);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 5:
            var3 = new TextCheckBoxCell(this.context);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 6:
            var3 = new UserCell(this.context, 4, 0);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 7:
            var3 = new ShadowSectionCell(this.context);
            break;
         default:
            var3 = new TextCheckCell(this.context);
            ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         if (var1.getItemViewType() != 0) {
            int var2 = var1.getItemViewType();
            boolean var3 = false;
            boolean var4 = false;
            boolean var5 = false;
            boolean var6 = false;
            boolean var7;
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 == 4) {
                        RadioCell var8 = (RadioCell)var1.itemView;
                        var7 = var6;
                        if (ProfileNotificationsActivity.this.customEnabled) {
                           var7 = var6;
                           if (ProfileNotificationsActivity.this.notificationsEnabled) {
                              var7 = true;
                           }
                        }

                        var8.setEnabled(var7, (ArrayList)null);
                     }
                  } else {
                     TextColorCell var9 = (TextColorCell)var1.itemView;
                     var7 = var3;
                     if (ProfileNotificationsActivity.this.customEnabled) {
                        var7 = var3;
                        if (ProfileNotificationsActivity.this.notificationsEnabled) {
                           var7 = true;
                        }
                     }

                     var9.setEnabled(var7, (ArrayList)null);
                  }
               } else {
                  TextInfoPrivacyCell var10 = (TextInfoPrivacyCell)var1.itemView;
                  var7 = var4;
                  if (ProfileNotificationsActivity.this.customEnabled) {
                     var7 = var4;
                     if (ProfileNotificationsActivity.this.notificationsEnabled) {
                        var7 = true;
                     }
                  }

                  var10.setEnabled(var7, (ArrayList)null);
               }
            } else {
               TextSettingsCell var11 = (TextSettingsCell)var1.itemView;
               var7 = var5;
               if (ProfileNotificationsActivity.this.customEnabled) {
                  var7 = var5;
                  if (ProfileNotificationsActivity.this.notificationsEnabled) {
                     var7 = true;
                  }
               }

               var11.setEnabled(var7, (ArrayList)null);
            }
         }

      }
   }

   public interface ProfileNotificationsActivityDelegate {
      void didCreateNewException(NotificationsSettingsActivity.NotificationException var1);
   }
}
