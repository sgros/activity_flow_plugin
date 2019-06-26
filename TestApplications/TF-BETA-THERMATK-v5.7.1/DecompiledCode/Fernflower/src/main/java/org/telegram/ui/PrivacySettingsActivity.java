package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PrivacySettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int advancedSectionRow;
   private int blockedRow;
   private int botsDetailRow;
   private int botsSectionRow;
   private int callsRow;
   private boolean[] clear = new boolean[2];
   private int clearDraftsRow;
   private int contactsDeleteRow;
   private int contactsDetailRow;
   private int contactsSectionRow;
   private int contactsSuggestRow;
   private int contactsSyncRow;
   private boolean currentSuggest;
   private boolean currentSync;
   private int deleteAccountDetailRow;
   private int deleteAccountRow;
   private int forwardsRow;
   private int groupsDetailRow;
   private int groupsRow;
   private int lastSeenRow;
   private LinearLayoutManager layoutManager;
   private PrivacySettingsActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean newSuggest;
   private boolean newSync;
   private int passcodeRow;
   private int passportRow;
   private int passwordRow;
   private int paymentsClearRow;
   private int phoneNumberRow;
   private int privacySectionRow;
   private int profilePhotoRow;
   private AlertDialog progressDialog;
   private int rowCount;
   private int secretDetailRow;
   private int secretMapRow;
   private int secretSectionRow;
   private int secretWebpageRow;
   private int securitySectionRow;
   private int sessionsDetailRow;
   private int sessionsRow;
   private int webSessionsRow;

   // $FF: synthetic method
   static int access$1000(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1200(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1400(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1600(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2000(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3700(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3800(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3900(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4000(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4100(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4200(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4300(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4400(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5700(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(PrivacySettingsActivity var0) {
      return var0.currentAccount;
   }

   public static String formatRulesString(int var0, int var1) {
      ArrayList var2 = ContactsController.getInstance(var0).getPrivacyRules(var1);
      if (var2.size() == 0) {
         return var1 == 3 ? LocaleController.getString("P2PNobody", 2131560141) : LocaleController.getString("LastSeenNobody", 2131559739);
      } else {
         int var3 = 0;
         byte var4 = -1;
         int var5 = 0;

         int var6;
         int var11;
         for(var6 = 0; var3 < var2.size(); var6 = var11) {
            TLRPC.PrivacyRule var7 = (TLRPC.PrivacyRule)var2.get(var3);
            int var9;
            TLRPC.Chat var14;
            byte var16;
            if (!(var7 instanceof TLRPC.TL_privacyValueAllowChatParticipants)) {
               if (var7 instanceof TLRPC.TL_privacyValueDisallowChatParticipants) {
                  TLRPC.TL_privacyValueDisallowChatParticipants var15 = (TLRPC.TL_privacyValueDisallowChatParticipants)var7;
                  int var12 = var15.chats.size();
                  int var13 = 0;

                  while(true) {
                     var16 = var4;
                     var9 = var5;
                     var11 = var6;
                     if (var13 >= var12) {
                        break;
                     }

                     var14 = MessagesController.getInstance(var0).getChat((Integer)var15.chats.get(var13));
                     if (var14 != null) {
                        var5 += var14.participants_count;
                     }

                     ++var13;
                  }
               } else if (var7 instanceof TLRPC.TL_privacyValueAllowUsers) {
                  var11 = var6 + ((TLRPC.TL_privacyValueAllowUsers)var7).users.size();
                  var16 = var4;
                  var9 = var5;
               } else if (var7 instanceof TLRPC.TL_privacyValueDisallowUsers) {
                  var9 = var5 + ((TLRPC.TL_privacyValueDisallowUsers)var7).users.size();
                  var16 = var4;
                  var11 = var6;
               } else {
                  var16 = var4;
                  var9 = var5;
                  var11 = var6;
                  if (var4 == -1) {
                     if (var7 instanceof TLRPC.TL_privacyValueAllowAll) {
                        var16 = 0;
                        var9 = var5;
                        var11 = var6;
                     } else if (var7 instanceof TLRPC.TL_privacyValueDisallowAll) {
                        var16 = 1;
                        var9 = var5;
                        var11 = var6;
                     } else {
                        var16 = 2;
                        var11 = var6;
                        var9 = var5;
                     }
                  }
               }
            } else {
               TLRPC.TL_privacyValueAllowChatParticipants var8 = (TLRPC.TL_privacyValueAllowChatParticipants)var7;
               var9 = var8.chats.size();
               int var10 = var6;

               for(var6 = 0; var6 < var9; ++var6) {
                  var14 = MessagesController.getInstance(var0).getChat((Integer)var8.chats.get(var6));
                  if (var14 != null) {
                     var10 += var14.participants_count;
                  }
               }

               var11 = var10;
               var16 = var4;
               var9 = var5;
            }

            ++var3;
            var4 = var16;
            var5 = var9;
         }

         if (var4 != 0 && (var4 != -1 || var5 <= 0)) {
            if (var4 == 2 || var4 == -1 && var5 > 0 && var6 > 0) {
               if (var1 == 3) {
                  if (var6 == 0 && var5 == 0) {
                     return LocaleController.getString("P2PContacts", 2131560134);
                  } else if (var6 != 0 && var5 != 0) {
                     return LocaleController.formatString("P2PContactsMinusPlus", 2131560136, var5, var6);
                  } else if (var5 != 0) {
                     return LocaleController.formatString("P2PContactsMinus", 2131560135, var5);
                  } else {
                     return LocaleController.formatString("P2PContactsPlus", 2131560137, var6);
                  }
               } else if (var6 == 0 && var5 == 0) {
                  return LocaleController.getString("LastSeenContacts", 2131559730);
               } else if (var6 != 0 && var5 != 0) {
                  return LocaleController.formatString("LastSeenContactsMinusPlus", 2131559732, var5, var6);
               } else if (var5 != 0) {
                  return LocaleController.formatString("LastSeenContactsMinus", 2131559731, var5);
               } else {
                  return LocaleController.formatString("LastSeenContactsPlus", 2131559733, var6);
               }
            } else if (var4 != 1 && var6 <= 0) {
               return "unknown";
            } else if (var1 == 3) {
               if (var6 == 0) {
                  return LocaleController.getString("P2PNobody", 2131560141);
               } else {
                  return LocaleController.formatString("P2PNobodyPlus", 2131560142, var6);
               }
            } else if (var6 == 0) {
               return LocaleController.getString("LastSeenNobody", 2131559739);
            } else {
               return LocaleController.formatString("LastSeenNobodyPlus", 2131559740, var6);
            }
         } else if (var1 == 3) {
            if (var5 == 0) {
               return LocaleController.getString("P2PEverybody", 2131560139);
            } else {
               return LocaleController.formatString("P2PEverybodyMinus", 2131560140, var5);
            }
         } else if (var5 == 0) {
            return LocaleController.getString("LastSeenEverybody", 2131559736);
         } else {
            return LocaleController.formatString("LastSeenEverybodyMinus", 2131559737, var5);
         }
      }
   }

   // $FF: synthetic method
   static void lambda$null$14(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$onFragmentDestroy$0(TLObject var0, TLRPC.TL_error var1) {
   }

   private void loadPasswordSettings() {
      if (!UserConfig.getInstance(super.currentAccount).hasSecureData) {
         TLRPC.TL_account_getPassword var1 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY(this), 10);
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.privacySectionRow = var1;
      var1 = this.rowCount++;
      this.blockedRow = var1;
      var1 = this.rowCount++;
      this.phoneNumberRow = var1;
      var1 = this.rowCount++;
      this.lastSeenRow = var1;
      var1 = this.rowCount++;
      this.profilePhotoRow = var1;
      var1 = this.rowCount++;
      this.forwardsRow = var1;
      var1 = this.rowCount++;
      this.callsRow = var1;
      var1 = this.rowCount++;
      this.groupsRow = var1;
      var1 = this.rowCount++;
      this.groupsDetailRow = var1;
      var1 = this.rowCount++;
      this.securitySectionRow = var1;
      var1 = this.rowCount++;
      this.passcodeRow = var1;
      var1 = this.rowCount++;
      this.passwordRow = var1;
      var1 = this.rowCount++;
      this.sessionsRow = var1;
      var1 = this.rowCount++;
      this.sessionsDetailRow = var1;
      var1 = this.rowCount++;
      this.advancedSectionRow = var1;
      var1 = this.rowCount++;
      this.clearDraftsRow = var1;
      var1 = this.rowCount++;
      this.deleteAccountRow = var1;
      var1 = this.rowCount++;
      this.deleteAccountDetailRow = var1;
      var1 = this.rowCount++;
      this.botsSectionRow = var1;
      if (UserConfig.getInstance(super.currentAccount).hasSecureData) {
         var1 = this.rowCount++;
         this.passportRow = var1;
      } else {
         this.passportRow = -1;
      }

      var1 = this.rowCount++;
      this.paymentsClearRow = var1;
      var1 = this.rowCount++;
      this.webSessionsRow = var1;
      var1 = this.rowCount++;
      this.botsDetailRow = var1;
      var1 = this.rowCount++;
      this.contactsSectionRow = var1;
      var1 = this.rowCount++;
      this.contactsDeleteRow = var1;
      var1 = this.rowCount++;
      this.contactsSyncRow = var1;
      var1 = this.rowCount++;
      this.contactsSuggestRow = var1;
      var1 = this.rowCount++;
      this.contactsDetailRow = var1;
      var1 = this.rowCount++;
      this.secretSectionRow = var1;
      var1 = this.rowCount++;
      this.secretMapRow = var1;
      var1 = this.rowCount++;
      this.secretWebpageRow = var1;
      var1 = this.rowCount++;
      this.secretDetailRow = var1;
      PrivacySettingsActivity.ListAdapter var2 = this.listAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("PrivacySettings", 2131560509));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PrivacySettingsActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new PrivacySettingsActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PrivacySettingsActivity$4NW4D3yhnbANs4O1uZu09FeFwHI(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.privacyRulesUpdated) {
         PrivacySettingsActivity.ListAdapter var4 = this.listAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked")};
   }

   // $FF: synthetic method
   public void lambda$createView$17$PrivacySettingsActivity(View var1, int var2) {
      if (var1.isEnabled()) {
         if (var2 == this.blockedRow) {
            this.presentFragment(new PrivacyUsersActivity());
         } else {
            int var3 = this.sessionsRow;
            boolean var4 = false;
            if (var2 == var3) {
               this.presentFragment(new SessionsActivity(0));
            } else if (var2 == this.webSessionsRow) {
               this.presentFragment(new SessionsActivity(1));
            } else {
               AlertDialog.Builder var10;
               if (var2 == this.clearDraftsRow) {
                  var10 = new AlertDialog.Builder(this.getParentActivity());
                  var10.setTitle(LocaleController.getString("AppName", 2131558635));
                  var10.setMessage(LocaleController.getString("AreYouSureClearDrafts", 2131558668));
                  var10.setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$PrivacySettingsActivity$ylpEMcd3Q_fRxhVvfyDGjrZOk0Q(this));
                  var10.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  this.showDialog(var10.create());
               } else {
                  String var11;
                  if (var2 == this.deleteAccountRow) {
                     if (this.getParentActivity() == null) {
                        return;
                     }

                     AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
                     var5.setTitle(LocaleController.getString("DeleteAccountTitle", 2131559230));
                     String var6 = LocaleController.formatPluralString("Months", 1);
                     String var7 = LocaleController.formatPluralString("Months", 3);
                     var11 = LocaleController.formatPluralString("Months", 6);
                     String var8 = LocaleController.formatPluralString("Years", 1);
                     _$$Lambda$PrivacySettingsActivity$Ec6kRVXNO1Ei2HcTWVQ5_P5hUGc var9 = new _$$Lambda$PrivacySettingsActivity$Ec6kRVXNO1Ei2HcTWVQ5_P5hUGc(this);
                     var5.setItems(new CharSequence[]{var6, var7, var11, var8}, var9);
                     var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     this.showDialog(var5.create());
                  } else if (var2 == this.lastSeenRow) {
                     this.presentFragment(new PrivacyControlActivity(0));
                  } else if (var2 == this.phoneNumberRow) {
                     this.presentFragment(new PrivacyControlActivity(6));
                  } else if (var2 == this.groupsRow) {
                     this.presentFragment(new PrivacyControlActivity(1));
                  } else if (var2 == this.callsRow) {
                     this.presentFragment(new PrivacyControlActivity(2));
                  } else if (var2 == this.profilePhotoRow) {
                     this.presentFragment(new PrivacyControlActivity(4));
                  } else if (var2 == this.forwardsRow) {
                     this.presentFragment(new PrivacyControlActivity(5));
                  } else if (var2 == this.passwordRow) {
                     this.presentFragment(new TwoStepVerificationActivity(0));
                  } else if (var2 == this.passcodeRow) {
                     if (SharedConfig.passcodeHash.length() > 0) {
                        this.presentFragment(new PasscodeActivity(2));
                     } else {
                        this.presentFragment(new PasscodeActivity(0));
                     }
                  } else if (var2 == this.secretWebpageRow) {
                     if (MessagesController.getInstance(super.currentAccount).secretWebpagePreview == 1) {
                        MessagesController.getInstance(super.currentAccount).secretWebpagePreview = 0;
                     } else {
                        MessagesController.getInstance(super.currentAccount).secretWebpagePreview = 1;
                     }

                     MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", MessagesController.getInstance(super.currentAccount).secretWebpagePreview).commit();
                     if (var1 instanceof TextCheckCell) {
                        TextCheckCell var12 = (TextCheckCell)var1;
                        if (MessagesController.getInstance(super.currentAccount).secretWebpagePreview == 1) {
                           var4 = true;
                        }

                        var12.setChecked(var4);
                     }
                  } else if (var2 == this.contactsDeleteRow) {
                     if (this.getParentActivity() == null) {
                        return;
                     }

                     var10 = new AlertDialog.Builder(this.getParentActivity());
                     var10.setTitle(LocaleController.getString("Contacts", 2131559149));
                     var10.setMessage(LocaleController.getString("SyncContactsDeleteInfo", 2131560852));
                     var10.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     var10.setNegativeButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PrivacySettingsActivity$XTkqLHKOC0QMU4JQevFxzR8pfWg(this));
                     this.showDialog(var10.create());
                  } else if (var2 == this.contactsSuggestRow) {
                     TextCheckCell var15 = (TextCheckCell)var1;
                     var4 = this.newSuggest;
                     if (var4) {
                        var10 = new AlertDialog.Builder(this.getParentActivity());
                        var10.setTitle(LocaleController.getString("AppName", 2131558635));
                        var10.setMessage(LocaleController.getString("SuggestContactsAlert", 2131560841));
                        var10.setPositiveButton(LocaleController.getString("MuteDisable", 2131559885), new _$$Lambda$PrivacySettingsActivity$5uWMtQO_U72VaNWgF2qczAQ3ehw(this, var15));
                        var10.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                        this.showDialog(var10.create());
                     } else {
                        this.newSuggest = var4 ^ true;
                        var15.setChecked(this.newSuggest);
                     }
                  } else if (var2 == this.contactsSyncRow) {
                     this.newSync ^= true;
                     if (var1 instanceof TextCheckCell) {
                        ((TextCheckCell)var1).setChecked(this.newSync);
                     }
                  } else if (var2 == this.secretMapRow) {
                     AlertsCreator.showSecretLocationAlert(this.getParentActivity(), super.currentAccount, new _$$Lambda$PrivacySettingsActivity$UDnLY7DIuRnfm37P1EPe2aUKeCM(this), false);
                  } else if (var2 == this.paymentsClearRow) {
                     BottomSheet.Builder var16 = new BottomSheet.Builder(this.getParentActivity());
                     var16.setApplyTopPadding(false);
                     var16.setApplyBottomPadding(false);
                     LinearLayout var17 = new LinearLayout(this.getParentActivity());
                     var17.setOrientation(1);

                     for(var2 = 0; var2 < 2; ++var2) {
                        if (var2 == 0) {
                           var11 = LocaleController.getString("PrivacyClearShipping", 2131560480);
                        } else if (var2 == 1) {
                           var11 = LocaleController.getString("PrivacyClearPayment", 2131560479);
                        } else {
                           var11 = null;
                        }

                        this.clear[var2] = true;
                        CheckBoxCell var14 = new CheckBoxCell(this.getParentActivity(), 1, 21);
                        var14.setTag(var2);
                        var14.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        var17.addView(var14, LayoutHelper.createLinear(-1, 50));
                        var14.setText(var11, (String)null, true, true);
                        var14.setTextColor(Theme.getColor("dialogTextBlack"));
                        var14.setOnClickListener(new _$$Lambda$PrivacySettingsActivity$Z17JwQmal4wl3EV9bgEY12pZChE(this));
                     }

                     BottomSheet.BottomSheetCell var13 = new BottomSheet.BottomSheetCell(this.getParentActivity(), 1);
                     var13.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                     var13.setTextAndIcon(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), 0);
                     var13.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
                     var13.setOnClickListener(new _$$Lambda$PrivacySettingsActivity$GAEKbcOA8F2xyTKmSV5lsU1BRLk(this));
                     var17.addView(var13, LayoutHelper.createLinear(-1, 50));
                     var16.setCustomView(var17);
                     this.showDialog(var16.create());
                  } else if (var2 == this.passportRow) {
                     this.presentFragment(new PassportActivity(5, 0, "", "", (String)null, (String)null, (String)null, (TLRPC.TL_account_authorizationForm)null, (TLRPC.TL_account_password)null));
                  }
               }
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$loadPasswordSettings$19$PrivacySettingsActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null && ((TLRPC.TL_account_password)var1).has_secure_values) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PrivacySettingsActivity$IncnhooBIVbvbnrTjiT1GT2RRI0(this));
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$PrivacySettingsActivity() {
      DataQuery.getInstance(super.currentAccount).clearAllDrafts();
   }

   // $FF: synthetic method
   public void lambda$null$10$PrivacySettingsActivity(TextCheckCell var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PrivacySettingsActivity$DlGV63A9ypZdVnkR6Qd7Km_hz_I(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$11$PrivacySettingsActivity(TextCheckCell var1, DialogInterface var2, int var3) {
      TLRPC.TL_payments_clearSavedInfo var4 = new TLRPC.TL_payments_clearSavedInfo();
      boolean[] var5 = this.clear;
      var4.credentials = var5[1];
      var4.info = var5[0];
      UserConfig.getInstance(super.currentAccount).tmpPassword = null;
      UserConfig.getInstance(super.currentAccount).saveConfig(false);
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$PrivacySettingsActivity$9W_WSazbZdTZMbEaunliHg9yKU0(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$12$PrivacySettingsActivity() {
      this.listAdapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$null$13$PrivacySettingsActivity(View var1) {
      CheckBoxCell var2 = (CheckBoxCell)var1;
      int var3 = (Integer)var2.getTag();
      boolean[] var4 = this.clear;
      var4[var3] ^= true;
      var2.setChecked(var4[var3], true);
   }

   // $FF: synthetic method
   public void lambda$null$15$PrivacySettingsActivity(DialogInterface var1, int var2) {
      TLRPC.TL_payments_clearSavedInfo var4 = new TLRPC.TL_payments_clearSavedInfo();
      boolean[] var3 = this.clear;
      var4.credentials = var3[1];
      var4.info = var3[0];
      UserConfig.getInstance(super.currentAccount).tmpPassword = null;
      UserConfig.getInstance(super.currentAccount).saveConfig(false);
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, _$$Lambda$PrivacySettingsActivity$RbXVegSlA75NxctCB1pTrsCmwsU.INSTANCE);
   }

   // $FF: synthetic method
   public void lambda$null$16$PrivacySettingsActivity(View var1) {
      try {
         if (super.visibleDialog != null) {
            super.visibleDialog.dismiss();
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
      var3.setTitle(LocaleController.getString("AppName", 2131558635));
      var3.setMessage(LocaleController.getString("PrivacyPaymentsClearAlert", 2131560497));
      var3.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PrivacySettingsActivity$deqj30NjdD0kcQ7DKtdHwkANODM(this));
      var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      this.showDialog(var3.create());
   }

   // $FF: synthetic method
   public void lambda$null$18$PrivacySettingsActivity() {
      UserConfig.getInstance(super.currentAccount).hasSecureData = true;
      UserConfig.getInstance(super.currentAccount).saveConfig(false);
      this.updateRows();
   }

   // $FF: synthetic method
   public void lambda$null$2$PrivacySettingsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PrivacySettingsActivity$f1z5m_pXWI9fMMWGfuS4tAirj68(this));
   }

   // $FF: synthetic method
   public void lambda$null$3$PrivacySettingsActivity(DialogInterface var1, int var2) {
      TLRPC.TL_messages_clearAllDrafts var3 = new TLRPC.TL_messages_clearAllDrafts();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$PrivacySettingsActivity$s_IxNGqvJf4sWGFlV1kwOiRILw0(this));
   }

   // $FF: synthetic method
   public void lambda$null$4$PrivacySettingsActivity(AlertDialog var1, TLObject var2, TLRPC.TL_account_setAccountTTL var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (var2 instanceof TLRPC.TL_boolTrue) {
         ContactsController.getInstance(super.currentAccount).setDeleteAccountTTL(var3.ttl.days);
         this.listAdapter.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$PrivacySettingsActivity(AlertDialog var1, TLRPC.TL_account_setAccountTTL var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PrivacySettingsActivity$LsF3KI7j3KNxTTiwgalmqj7HqKo(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$null$6$PrivacySettingsActivity(DialogInterface var1, int var2) {
      short var5;
      if (var2 == 0) {
         var5 = 30;
      } else if (var2 == 1) {
         var5 = 90;
      } else if (var2 == 2) {
         var5 = 182;
      } else if (var2 == 3) {
         var5 = 365;
      } else {
         var5 = 0;
      }

      AlertDialog var4 = new AlertDialog(this.getParentActivity(), 3);
      var4.setCanCacnel(false);
      var4.show();
      TLRPC.TL_account_setAccountTTL var3 = new TLRPC.TL_account_setAccountTTL();
      var3.ttl = new TLRPC.TL_accountDaysTTL();
      var3.ttl.days = var5;
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$PrivacySettingsActivity$J8W4oBcdXleAkqcvNpMiNXEJRwc(this, var4, var3));
   }

   // $FF: synthetic method
   public void lambda$null$7$PrivacySettingsActivity() {
      this.progressDialog.dismiss();
   }

   // $FF: synthetic method
   public void lambda$null$8$PrivacySettingsActivity(DialogInterface var1, int var2) {
      this.progressDialog = (new AlertDialog.Builder(this.getParentActivity(), 3)).show();
      this.progressDialog.setCanCacnel(false);
      if (this.currentSync != this.newSync) {
         UserConfig var4 = UserConfig.getInstance(super.currentAccount);
         boolean var3 = this.newSync;
         var4.syncContacts = var3;
         this.currentSync = var3;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
      }

      ContactsController.getInstance(super.currentAccount).deleteAllContacts(new _$$Lambda$PrivacySettingsActivity$AceH1btqmuD9dCVsGDT9w8V0zXo(this));
   }

   // $FF: synthetic method
   public void lambda$null$9$PrivacySettingsActivity(TextCheckCell var1) {
      this.newSuggest ^= true;
      var1.setChecked(this.newSuggest);
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      ContactsController.getInstance(super.currentAccount).loadPrivacySettings();
      boolean var1 = UserConfig.getInstance(super.currentAccount).syncContacts;
      this.newSync = var1;
      this.currentSync = var1;
      var1 = UserConfig.getInstance(super.currentAccount).suggestContacts;
      this.newSuggest = var1;
      this.currentSuggest = var1;
      this.updateRows();
      this.loadPasswordSettings();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
      if (this.currentSync != this.newSync) {
         UserConfig.getInstance(super.currentAccount).syncContacts = this.newSync;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
         if (this.newSync) {
            ContactsController.getInstance(super.currentAccount).forceImportContacts();
            if (this.getParentActivity() != null) {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("SyncContactsAdded", 2131560850), 0).show();
            }
         }
      }

      boolean var1 = this.newSuggest;
      if (var1 != this.currentSuggest) {
         if (!var1) {
            DataQuery.getInstance(super.currentAccount).clearTopPeers();
         }

         UserConfig.getInstance(super.currentAccount).suggestContacts = this.newSuggest;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
         TLRPC.TL_contacts_toggleTopPeers var2 = new TLRPC.TL_contacts_toggleTopPeers();
         var2.enabled = this.newSuggest;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, _$$Lambda$PrivacySettingsActivity$IBy4A3n5R5n7oI7wDCjl___b3rk.INSTANCE);
      }

   }

   public void onResume() {
      super.onResume();
      PrivacySettingsActivity.ListAdapter var1 = this.listAdapter;
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
         return PrivacySettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != PrivacySettingsActivity.this.passportRow && var1 != PrivacySettingsActivity.this.lastSeenRow && var1 != PrivacySettingsActivity.this.phoneNumberRow && var1 != PrivacySettingsActivity.this.blockedRow && var1 != PrivacySettingsActivity.this.deleteAccountRow && var1 != PrivacySettingsActivity.this.sessionsRow && var1 != PrivacySettingsActivity.this.webSessionsRow && var1 != PrivacySettingsActivity.this.passwordRow && var1 != PrivacySettingsActivity.this.passcodeRow && var1 != PrivacySettingsActivity.this.groupsRow && var1 != PrivacySettingsActivity.this.paymentsClearRow && var1 != PrivacySettingsActivity.this.secretMapRow && var1 != PrivacySettingsActivity.this.contactsDeleteRow && var1 != PrivacySettingsActivity.this.clearDraftsRow) {
            if (var1 != PrivacySettingsActivity.this.deleteAccountDetailRow && var1 != PrivacySettingsActivity.this.groupsDetailRow && var1 != PrivacySettingsActivity.this.sessionsDetailRow && var1 != PrivacySettingsActivity.this.secretDetailRow && var1 != PrivacySettingsActivity.this.botsDetailRow && var1 != PrivacySettingsActivity.this.contactsDetailRow) {
               if (var1 != PrivacySettingsActivity.this.securitySectionRow && var1 != PrivacySettingsActivity.this.advancedSectionRow && var1 != PrivacySettingsActivity.this.privacySectionRow && var1 != PrivacySettingsActivity.this.secretSectionRow && var1 != PrivacySettingsActivity.this.botsSectionRow && var1 != PrivacySettingsActivity.this.contactsSectionRow) {
                  return var1 != PrivacySettingsActivity.this.secretWebpageRow && var1 != PrivacySettingsActivity.this.contactsSyncRow && var1 != PrivacySettingsActivity.this.contactsSuggestRow ? 0 : 3;
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

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         int var3 = PrivacySettingsActivity.this.passcodeRow;
         boolean var4 = false;
         if (var2 == var3 || var2 == PrivacySettingsActivity.this.passwordRow || var2 == PrivacySettingsActivity.this.blockedRow || var2 == PrivacySettingsActivity.this.sessionsRow || var2 == PrivacySettingsActivity.this.secretWebpageRow || var2 == PrivacySettingsActivity.this.webSessionsRow || var2 == PrivacySettingsActivity.this.clearDraftsRow || var2 == PrivacySettingsActivity.this.groupsRow && !ContactsController.getInstance(PrivacySettingsActivity.access$800(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(1) || var2 == PrivacySettingsActivity.this.lastSeenRow && !ContactsController.getInstance(PrivacySettingsActivity.access$1000(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(0) || var2 == PrivacySettingsActivity.this.callsRow && !ContactsController.getInstance(PrivacySettingsActivity.access$1200(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(2) || var2 == PrivacySettingsActivity.this.profilePhotoRow && !ContactsController.getInstance(PrivacySettingsActivity.access$1400(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(4) || var2 == PrivacySettingsActivity.this.forwardsRow && !ContactsController.getInstance(PrivacySettingsActivity.access$1600(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(5) || var2 == PrivacySettingsActivity.this.phoneNumberRow && !ContactsController.getInstance(PrivacySettingsActivity.access$1800(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(6) || var2 == PrivacySettingsActivity.this.deleteAccountRow && !ContactsController.getInstance(PrivacySettingsActivity.access$2000(PrivacySettingsActivity.this)).getLoadingDeleteInfo() || var2 == PrivacySettingsActivity.this.paymentsClearRow || var2 == PrivacySettingsActivity.this.secretMapRow || var2 == PrivacySettingsActivity.this.contactsSyncRow || var2 == PrivacySettingsActivity.this.passportRow || var2 == PrivacySettingsActivity.this.contactsDeleteRow || var2 == PrivacySettingsActivity.this.contactsSuggestRow) {
            var4 = true;
         }

         return var4;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     TextCheckCell var6 = (TextCheckCell)var1.itemView;
                     if (var2 == PrivacySettingsActivity.this.secretWebpageRow) {
                        String var5 = LocaleController.getString("SecretWebPage", 2131560673);
                        if (MessagesController.getInstance(PrivacySettingsActivity.access$5700(PrivacySettingsActivity.this)).secretWebpagePreview != 1) {
                           var4 = false;
                        }

                        var6.setTextAndCheck(var5, var4, false);
                     } else if (var2 == PrivacySettingsActivity.this.contactsSyncRow) {
                        var6.setTextAndCheck(LocaleController.getString("SyncContacts", 2131560849), PrivacySettingsActivity.this.newSync, true);
                     } else if (var2 == PrivacySettingsActivity.this.contactsSuggestRow) {
                        var6.setTextAndCheck(LocaleController.getString("SuggestContacts", 2131560840), PrivacySettingsActivity.this.newSuggest, false);
                     }
                  }
               } else {
                  HeaderCell var7 = (HeaderCell)var1.itemView;
                  if (var2 == PrivacySettingsActivity.this.privacySectionRow) {
                     var7.setText(LocaleController.getString("PrivacyTitle", 2131560511));
                  } else if (var2 == PrivacySettingsActivity.this.securitySectionRow) {
                     var7.setText(LocaleController.getString("SecurityTitle", 2131560675));
                  } else if (var2 == PrivacySettingsActivity.this.advancedSectionRow) {
                     var7.setText(LocaleController.getString("PrivacyAdvanced", 2131560475));
                  } else if (var2 == PrivacySettingsActivity.this.secretSectionRow) {
                     var7.setText(LocaleController.getString("SecretChat", 2131560669));
                  } else if (var2 == PrivacySettingsActivity.this.botsSectionRow) {
                     var7.setText(LocaleController.getString("PrivacyBots", 2131560476));
                  } else if (var2 == PrivacySettingsActivity.this.contactsSectionRow) {
                     var7.setText(LocaleController.getString("Contacts", 2131559149));
                  }
               }
            } else {
               TextInfoPrivacyCell var8 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == PrivacySettingsActivity.this.deleteAccountDetailRow) {
                  var8.setText(LocaleController.getString("DeleteAccountHelp", 2131559228));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacySettingsActivity.this.groupsDetailRow) {
                  var8.setText(LocaleController.getString("GroupsAndChannelsHelp", 2131559625));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacySettingsActivity.this.sessionsDetailRow) {
                  var8.setText(LocaleController.getString("SessionsInfo", 2131560725));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacySettingsActivity.this.secretDetailRow) {
                  var8.setText(LocaleController.getString("SecretWebPageInfo", 2131560674));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacySettingsActivity.this.botsDetailRow) {
                  var8.setText(LocaleController.getString("PrivacyBotsInfo", 2131560477));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacySettingsActivity.this.contactsDetailRow) {
                  var8.setText(LocaleController.getString("SuggestContactsInfo", 2131560842));
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            TextSettingsCell var10 = (TextSettingsCell)var1.itemView;
            if (var2 == PrivacySettingsActivity.this.blockedRow) {
               if (!PrivacySettingsActivity.this.getMessagesController().loadingBlockedUsers) {
                  if (PrivacySettingsActivity.this.getMessagesController().blockedUsers.size() == 0) {
                     var10.setTextAndValue(LocaleController.getString("BlockedUsers", 2131558835), LocaleController.getString("EmptyExceptions", 2131559346), true);
                  } else {
                     var10.setTextAndValue(LocaleController.getString("BlockedUsers", 2131558835), String.format("%d", PrivacySettingsActivity.this.getMessagesController().blockedUsers.size()), true);
                  }
               } else {
                  var10.setText(LocaleController.getString("BlockedUsers", 2131558835), true);
               }
            } else if (var2 == PrivacySettingsActivity.this.sessionsRow) {
               var10.setText(LocaleController.getString("SessionsTitle", 2131560726), false);
            } else if (var2 == PrivacySettingsActivity.this.webSessionsRow) {
               var10.setText(LocaleController.getString("WebSessionsTitle", 2131561104), false);
            } else if (var2 == PrivacySettingsActivity.this.passwordRow) {
               var10.setText(LocaleController.getString("TwoStepVerification", 2131560919), true);
            } else if (var2 == PrivacySettingsActivity.this.passcodeRow) {
               var10.setText(LocaleController.getString("Passcode", 2131560160), true);
            } else {
               String var9;
               if (var2 == PrivacySettingsActivity.this.phoneNumberRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$3100(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(6)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$3200(PrivacySettingsActivity.this), 6);
                  }

                  var10.setTextAndValue(LocaleController.getString("PrivacyPhone", 2131560498), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.lastSeenRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$3300(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(0)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$3400(PrivacySettingsActivity.this), 0);
                  }

                  var10.setTextAndValue(LocaleController.getString("PrivacyLastSeen", 2131560492), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.groupsRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$3500(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(1)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$3600(PrivacySettingsActivity.this), 1);
                  }

                  var10.setTextAndValue(LocaleController.getString("GroupsAndChannels", 2131559624), var9, false);
               } else if (var2 == PrivacySettingsActivity.this.callsRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$3700(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(2)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$3800(PrivacySettingsActivity.this), 2);
                  }

                  var10.setTextAndValue(LocaleController.getString("Calls", 2131558888), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.profilePhotoRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$3900(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(4)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$4000(PrivacySettingsActivity.this), 4);
                  }

                  var10.setTextAndValue(LocaleController.getString("PrivacyProfilePhoto", 2131560505), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.forwardsRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$4100(PrivacySettingsActivity.this)).getLoadingPrivicyInfo(5)) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var9 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.access$4200(PrivacySettingsActivity.this), 5);
                  }

                  var10.setTextAndValue(LocaleController.getString("PrivacyForwards", 2131560484), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.passportRow) {
                  var10.setText(LocaleController.getString("TelegramPassport", 2131560872), true);
               } else if (var2 == PrivacySettingsActivity.this.deleteAccountRow) {
                  if (ContactsController.getInstance(PrivacySettingsActivity.access$4300(PrivacySettingsActivity.this)).getLoadingDeleteInfo()) {
                     var9 = LocaleController.getString("Loading", 2131559768);
                  } else {
                     var2 = ContactsController.getInstance(PrivacySettingsActivity.access$4400(PrivacySettingsActivity.this)).getDeleteAccountTTL();
                     if (var2 <= 182) {
                        var9 = LocaleController.formatPluralString("Months", var2 / 30);
                     } else if (var2 == 365) {
                        var9 = LocaleController.formatPluralString("Years", var2 / 365);
                     } else {
                        var9 = LocaleController.formatPluralString("Days", var2);
                     }
                  }

                  var10.setTextAndValue(LocaleController.getString("DeleteAccountIfAwayFor2", 2131559229), var9, false);
               } else if (var2 == PrivacySettingsActivity.this.clearDraftsRow) {
                  var10.setText(LocaleController.getString("PrivacyDeleteCloudDrafts", 2131560481), true);
               } else if (var2 == PrivacySettingsActivity.this.paymentsClearRow) {
                  var10.setText(LocaleController.getString("PrivacyPaymentsClear", 2131560496), true);
               } else if (var2 == PrivacySettingsActivity.this.secretMapRow) {
                  var2 = SharedConfig.mapPreviewType;
                  if (var2 != 0) {
                     if (var2 != 1) {
                        var9 = LocaleController.getString("MapPreviewProviderNobody", 2131559803);
                     } else {
                        var9 = LocaleController.getString("MapPreviewProviderGoogle", 2131559802);
                     }
                  } else {
                     var9 = LocaleController.getString("MapPreviewProviderTelegram", 2131559804);
                  }

                  var10.setTextAndValue(LocaleController.getString("MapPreviewProvider", 2131559801), var9, true);
               } else if (var2 == PrivacySettingsActivity.this.contactsDeleteRow) {
                  var10.setText(LocaleController.getString("SyncContactsDelete", 2131560851), true);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = new TextCheckCell(this.mContext);
                  ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               } else {
                  var3 = new HeaderCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
            }
         } else {
            var3 = new TextSettingsCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
