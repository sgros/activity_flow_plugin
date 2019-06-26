package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PrivacyControlActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   public static final int PRIVACY_RULES_TYPE_CALLS = 2;
   public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
   public static final int PRIVACY_RULES_TYPE_INVITE = 1;
   public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
   public static final int PRIVACY_RULES_TYPE_P2P = 3;
   public static final int PRIVACY_RULES_TYPE_PHONE = 6;
   public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
   private static final int done_button = 1;
   private int alwaysShareRow;
   private ArrayList currentMinus;
   private ArrayList currentPlus;
   private int currentType;
   private int detailRow;
   private View doneButton;
   private boolean enableAnimation;
   private int everybodyRow;
   private ArrayList initialMinus;
   private ArrayList initialPlus;
   private int initialRulesType;
   private int lastCheckedType;
   private PrivacyControlActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private PrivacyControlActivity.MessageCell messageCell;
   private int messageRow;
   private int myContactsRow;
   private int neverShareRow;
   private int nobodyRow;
   private int p2pDetailRow;
   private int p2pRow;
   private int p2pSectionRow;
   private int rowCount;
   private int rulesType;
   private int sectionRow;
   private int shareDetailRow;
   private int shareSectionRow;

   public PrivacyControlActivity(int var1) {
      this(var1, false);
   }

   public PrivacyControlActivity(int var1, boolean var2) {
      this.initialPlus = new ArrayList();
      this.initialMinus = new ArrayList();
      this.lastCheckedType = -1;
      this.rulesType = var1;
      if (var2) {
         ContactsController.getInstance(super.currentAccount).loadPrivacySettings();
      }

   }

   // $FF: synthetic method
   static int access$000(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$100(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1500(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$200(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2200(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$300(PrivacyControlActivity var0) {
      return var0.currentAccount;
   }

   private void applyCurrentPrivacySettings() {
      TLRPC.TL_account_setPrivacy var1 = new TLRPC.TL_account_setPrivacy();
      int var2 = this.rulesType;
      if (var2 == 6) {
         var1.key = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
      } else if (var2 == 5) {
         var1.key = new TLRPC.TL_inputPrivacyKeyForwards();
      } else if (var2 == 4) {
         var1.key = new TLRPC.TL_inputPrivacyKeyProfilePhoto();
      } else if (var2 == 3) {
         var1.key = new TLRPC.TL_inputPrivacyKeyPhoneP2P();
      } else if (var2 == 2) {
         var1.key = new TLRPC.TL_inputPrivacyKeyPhoneCall();
      } else if (var2 == 1) {
         var1.key = new TLRPC.TL_inputPrivacyKeyChatInvite();
      } else {
         var1.key = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
      }

      int var5;
      TLRPC.User var6;
      TLRPC.InputUser var10;
      if (this.currentType != 0 && this.currentPlus.size() > 0) {
         TLRPC.TL_inputPrivacyValueAllowUsers var3 = new TLRPC.TL_inputPrivacyValueAllowUsers();
         TLRPC.TL_inputPrivacyValueAllowChatParticipants var4 = new TLRPC.TL_inputPrivacyValueAllowChatParticipants();

         for(var2 = 0; var2 < this.currentPlus.size(); ++var2) {
            var5 = (Integer)this.currentPlus.get(var2);
            if (var5 > 0) {
               var6 = MessagesController.getInstance(super.currentAccount).getUser(var5);
               if (var6 != null) {
                  var10 = MessagesController.getInstance(super.currentAccount).getInputUser(var6);
                  if (var10 != null) {
                     var3.users.add(var10);
                  }
               }
            } else {
               var4.chats.add(-var5);
            }
         }

         var1.rules.add(var3);
         var1.rules.add(var4);
      }

      if (this.currentType != 1 && this.currentMinus.size() > 0) {
         TLRPC.TL_inputPrivacyValueDisallowUsers var9 = new TLRPC.TL_inputPrivacyValueDisallowUsers();
         TLRPC.TL_inputPrivacyValueDisallowChatParticipants var7 = new TLRPC.TL_inputPrivacyValueDisallowChatParticipants();

         for(var2 = 0; var2 < this.currentMinus.size(); ++var2) {
            var5 = (Integer)this.currentMinus.get(var2);
            if (var5 > 0) {
               var6 = this.getMessagesController().getUser(var5);
               if (var6 != null) {
                  var10 = this.getMessagesController().getInputUser(var6);
                  if (var10 != null) {
                     var9.users.add(var10);
                  }
               }
            } else {
               var7.chats.add(-var5);
            }
         }

         var1.rules.add(var9);
         var1.rules.add(var7);
      }

      var2 = this.currentType;
      if (var2 == 0) {
         var1.rules.add(new TLRPC.TL_inputPrivacyValueAllowAll());
      } else if (var2 == 1) {
         var1.rules.add(new TLRPC.TL_inputPrivacyValueDisallowAll());
      } else if (var2 == 2) {
         var1.rules.add(new TLRPC.TL_inputPrivacyValueAllowContacts());
      }

      AlertDialog var8 = null;
      if (this.getParentActivity() != null) {
         var8 = new AlertDialog(this.getParentActivity(), 3);
         var8.setCanCacnel(false);
         var8.show();
      }

      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PrivacyControlActivity$8u1Pr_pdaGnQbppD7jDw_8yxFb4(this, var8), 2);
   }

   private boolean checkDiscard() {
      if (this.doneButton.getVisibility() == 0) {
         AlertDialog.Builder var1 = new AlertDialog.Builder(this.getParentActivity());
         var1.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
         var1.setMessage(LocaleController.getString("PrivacySettingsChangedAlert", 2131560510));
         var1.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), new _$$Lambda$PrivacyControlActivity$5im2Vf3cKm0s4wzjCS8m_xwDFXs(this));
         var1.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), new _$$Lambda$PrivacyControlActivity$jMiVlh9zo_Od8OHpA_uuLslJQzk(this));
         this.showDialog(var1.create());
         return false;
      } else {
         return true;
      }
   }

   private void checkPrivacy() {
      this.currentPlus = new ArrayList();
      this.currentMinus = new ArrayList();
      ArrayList var1 = ContactsController.getInstance(super.currentAccount).getPrivacyRules(this.rulesType);
      if (var1 != null && var1.size() != 0) {
         int var2 = 0;

         byte var3;
         byte var7;
         for(var3 = -1; var2 < var1.size(); var3 = var7) {
            TLRPC.PrivacyRule var4 = (TLRPC.PrivacyRule)var1.get(var2);
            int var5;
            int var6;
            if (var4 instanceof TLRPC.TL_privacyValueAllowChatParticipants) {
               TLRPC.TL_privacyValueAllowChatParticipants var12 = (TLRPC.TL_privacyValueAllowChatParticipants)var4;
               var5 = var12.chats.size();
               var6 = 0;

               while(true) {
                  var7 = var3;
                  if (var6 >= var5) {
                     break;
                  }

                  this.currentPlus.add(-(Integer)var12.chats.get(var6));
                  ++var6;
               }
            } else if (var4 instanceof TLRPC.TL_privacyValueDisallowChatParticipants) {
               TLRPC.TL_privacyValueDisallowChatParticipants var11 = (TLRPC.TL_privacyValueDisallowChatParticipants)var4;
               var5 = var11.chats.size();
               var6 = 0;

               while(true) {
                  var7 = var3;
                  if (var6 >= var5) {
                     break;
                  }

                  this.currentMinus.add(-(Integer)var11.chats.get(var6));
                  ++var6;
               }
            } else if (var4 instanceof TLRPC.TL_privacyValueAllowUsers) {
               TLRPC.TL_privacyValueAllowUsers var9 = (TLRPC.TL_privacyValueAllowUsers)var4;
               this.currentPlus.addAll(var9.users);
               var7 = var3;
            } else if (var4 instanceof TLRPC.TL_privacyValueDisallowUsers) {
               TLRPC.TL_privacyValueDisallowUsers var10 = (TLRPC.TL_privacyValueDisallowUsers)var4;
               this.currentMinus.addAll(var10.users);
               var7 = var3;
            } else {
               var7 = var3;
               if (var3 == -1) {
                  if (var4 instanceof TLRPC.TL_privacyValueAllowAll) {
                     var7 = 0;
                  } else if (var4 instanceof TLRPC.TL_privacyValueDisallowAll) {
                     var7 = 1;
                  } else {
                     var7 = 2;
                  }
               }
            }

            ++var2;
         }

         if (var3 == 0 || var3 == -1 && this.currentMinus.size() > 0) {
            this.currentType = 0;
         } else if (var3 != 2 && (var3 != -1 || this.currentMinus.size() <= 0 || this.currentPlus.size() <= 0)) {
            if (var3 == 1 || var3 == -1 && this.currentPlus.size() > 0) {
               this.currentType = 1;
            }
         } else {
            this.currentType = 2;
         }

         View var8 = this.doneButton;
         if (var8 != null) {
            var8.setVisibility(8);
         }
      } else {
         this.currentType = 1;
      }

      this.initialPlus.clear();
      this.initialMinus.clear();
      this.initialRulesType = this.currentType;
      this.initialPlus.addAll(this.currentPlus);
      this.initialMinus.addAll(this.currentMinus);
      this.updateRows();
   }

   private boolean hasChanges() {
      if (this.initialRulesType != this.currentType) {
         return true;
      } else if (this.initialMinus.size() != this.currentMinus.size()) {
         return true;
      } else if (this.initialPlus.size() != this.currentPlus.size()) {
         return true;
      } else {
         Collections.sort(this.initialPlus);
         Collections.sort(this.currentPlus);
         if (!this.initialPlus.equals(this.currentPlus)) {
            return true;
         } else {
            Collections.sort(this.initialMinus);
            Collections.sort(this.currentMinus);
            return !this.initialMinus.equals(this.currentMinus);
         }
      }
   }

   private void processDone() {
      if (this.getParentActivity() != null) {
         if (this.currentType != 0 && this.rulesType == 0) {
            SharedPreferences var1 = MessagesController.getGlobalMainSettings();
            if (!var1.getBoolean("privacyAlertShowed", false)) {
               AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
               if (this.rulesType == 1) {
                  var2.setMessage(LocaleController.getString("WhoCanAddMeInfo", 2131561116));
               } else {
                  var2.setMessage(LocaleController.getString("CustomHelp", 2131559187));
               }

               var2.setTitle(LocaleController.getString("AppName", 2131558635));
               var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw(this, var1));
               var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var2.create());
               return;
            }
         }

         this.applyCurrentPrivacySettings();
      }
   }

   private void setMessageText() {
      PrivacyControlActivity.MessageCell var1 = this.messageCell;
      if (var1 != null) {
         int var2 = this.currentType;
         if (var2 == 0) {
            var1.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsEverybody", 2131560486));
            this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
         } else if (var2 == 1) {
            var1.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsNobody", 2131560490));
            this.messageCell.messageObject.messageOwner.fwd_from.from_id = 0;
         } else {
            var1.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsContacts", 2131560485));
            this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
         }

         this.messageCell.cell.forceResetMessageObject();
      }

   }

   private void showErrorAlert() {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var1 = new AlertDialog.Builder(this.getParentActivity());
         var1.setTitle(LocaleController.getString("AppName", 2131558635));
         var1.setMessage(LocaleController.getString("PrivacyFloodControlError", 2131560483));
         var1.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var1.create());
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1;
      if (this.rulesType == 5) {
         var1 = this.rowCount++;
         this.messageRow = var1;
      } else {
         this.messageRow = -1;
      }

      var1 = this.rowCount++;
      this.sectionRow = var1;
      var1 = this.rowCount++;
      this.everybodyRow = var1;
      var1 = this.rowCount++;
      this.myContactsRow = var1;
      var1 = this.rulesType;
      if (var1 != 0 && var1 != 2 && var1 != 3 && var1 != 5 && var1 != 6) {
         this.nobodyRow = -1;
      } else {
         var1 = this.rowCount++;
         this.nobodyRow = var1;
      }

      var1 = this.rowCount++;
      this.detailRow = var1;
      var1 = this.rowCount++;
      this.shareSectionRow = var1;
      var1 = this.currentType;
      if (var1 != 1 && var1 != 2) {
         this.alwaysShareRow = -1;
      } else {
         var1 = this.rowCount++;
         this.alwaysShareRow = var1;
      }

      var1 = this.currentType;
      if (var1 != 0 && var1 != 2) {
         this.neverShareRow = -1;
      } else {
         var1 = this.rowCount++;
         this.neverShareRow = var1;
      }

      var1 = this.rowCount++;
      this.shareDetailRow = var1;
      if (this.rulesType == 2) {
         var1 = this.rowCount++;
         this.p2pSectionRow = var1;
         var1 = this.rowCount++;
         this.p2pRow = var1;
         var1 = this.rowCount++;
         this.p2pDetailRow = var1;
      } else {
         this.p2pSectionRow = -1;
         this.p2pRow = -1;
         this.p2pDetailRow = -1;
      }

      this.setMessageText();
      PrivacyControlActivity.ListAdapter var2 = this.listAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   public boolean canBeginSlide() {
      return this.checkDiscard();
   }

   public View createView(Context var1) {
      if (this.rulesType == 5) {
         this.messageCell = new PrivacyControlActivity.MessageCell(var1);
      }

      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      int var2 = this.rulesType;
      if (var2 == 6) {
         super.actionBar.setTitle(LocaleController.getString("PrivacyPhone", 2131560498));
      } else if (var2 == 5) {
         super.actionBar.setTitle(LocaleController.getString("PrivacyForwards", 2131560484));
      } else if (var2 == 4) {
         super.actionBar.setTitle(LocaleController.getString("PrivacyProfilePhoto", 2131560505));
      } else if (var2 == 3) {
         super.actionBar.setTitle(LocaleController.getString("PrivacyP2P", 2131560493));
      } else if (var2 == 2) {
         super.actionBar.setTitle(LocaleController.getString("Calls", 2131558888));
      } else if (var2 == 1) {
         super.actionBar.setTitle(LocaleController.getString("GroupsAndChannels", 2131559624));
      } else {
         super.actionBar.setTitle(LocaleController.getString("PrivacyLastSeen", 2131560492));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (PrivacyControlActivity.this.checkDiscard()) {
                  PrivacyControlActivity.this.finishFragment();
               }
            } else if (var1 == 1) {
               PrivacyControlActivity.this.processDone();
            }

         }
      });
      View var3 = this.doneButton;
      if (var3 != null) {
         var2 = var3.getVisibility();
      } else {
         var2 = 8;
      }

      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.doneButton.setVisibility(var2);
      this.listAdapter = new PrivacyControlActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var4 = (FrameLayout)super.fragmentView;
      var4.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setVerticalScrollBarEnabled(false);
      var4.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PrivacyControlActivity$wxF_vl2Ux3ukEJDYev7VlVBgIRk(this)));
      this.setMessageText();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.privacyRulesUpdated) {
         this.checkPrivacy();
      } else if (var1 == NotificationCenter.emojiDidLoad) {
         this.listView.invalidateViews();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, RadioCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubble"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleShadow"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubble"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleShadow"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextIn"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextOut"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheck"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheckSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaSentCheck"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyLine"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyLine"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyNameText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyNameText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMessageText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMessageText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMediaMessageSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMediaMessageSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeSelectedText")};
   }

   // $FF: synthetic method
   public void lambda$applyCurrentPrivacySettings$4$PrivacyControlActivity(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PrivacyControlActivity$wCT7rGu451A16C1KVTM9KhVzZrc(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$6$PrivacyControlActivity(DialogInterface var1, int var2) {
      this.processDone();
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$7$PrivacyControlActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$2$PrivacyControlActivity(View var1, int var2) {
      int var3 = this.nobodyRow;
      boolean var4 = false;
      byte var5 = 0;
      boolean var6 = false;
      if (var2 != var3 && var2 != this.everybodyRow && var2 != this.myContactsRow) {
         if (var2 != this.neverShareRow && var2 != this.alwaysShareRow) {
            if (var2 == this.p2pRow) {
               this.presentFragment(new PrivacyControlActivity(3));
            }
         } else {
            ArrayList var8;
            if (var2 == this.neverShareRow) {
               var8 = this.currentMinus;
            } else {
               var8 = this.currentPlus;
            }

            if (var8.isEmpty()) {
               Bundle var7 = new Bundle();
               String var10;
               if (var2 == this.neverShareRow) {
                  var10 = "isNeverShare";
               } else {
                  var10 = "isAlwaysShare";
               }

               var7.putBoolean(var10, true);
               if (this.rulesType != 0) {
                  var6 = true;
               }

               var7.putBoolean("isGroup", var6);
               GroupCreateActivity var11 = new GroupCreateActivity(var7);
               var11.setDelegate((GroupCreateActivity.GroupCreateActivityDelegate)(new _$$Lambda$PrivacyControlActivity$gyVCUgP7_dxVWuYDNVL7ikSQ_s4(this, var2)));
               this.presentFragment(var11);
            } else {
               if (this.rulesType != 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               if (var2 == this.alwaysShareRow) {
                  var4 = true;
               }

               PrivacyUsersActivity var12 = new PrivacyUsersActivity(var8, var6, var4);
               var12.setDelegate(new _$$Lambda$PrivacyControlActivity$Optb0rMT99Nhw1X8nsWqhFLsnHs(this, var2));
               this.presentFragment(var12);
            }
         }
      } else {
         var3 = this.currentType;
         if (var2 == this.nobodyRow) {
            var3 = 1;
         } else if (var2 == this.everybodyRow) {
            var3 = 0;
         } else if (var2 == this.myContactsRow) {
            var3 = 2;
         }

         var2 = this.currentType;
         if (var3 == var2) {
            return;
         }

         this.enableAnimation = true;
         this.lastCheckedType = var2;
         this.currentType = var3;
         var1 = this.doneButton;
         byte var9;
         if (this.hasChanges()) {
            var9 = var5;
         } else {
            var9 = 8;
         }

         var1.setVisibility(var9);
         this.updateRows();
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$PrivacyControlActivity(int var1, ArrayList var2) {
      int var3 = this.neverShareRow;
      byte var4 = 0;
      if (var1 == var3) {
         this.currentMinus = var2;

         for(var1 = 0; var1 < this.currentMinus.size(); ++var1) {
            this.currentPlus.remove(this.currentMinus.get(var1));
         }
      } else {
         this.currentPlus = var2;

         for(var1 = 0; var1 < this.currentPlus.size(); ++var1) {
            this.currentMinus.remove(this.currentPlus.get(var1));
         }
      }

      this.lastCheckedType = -1;
      View var5 = this.doneButton;
      byte var6;
      if (this.hasChanges()) {
         var6 = var4;
      } else {
         var6 = 8;
      }

      var5.setVisibility(var6);
      this.listAdapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$null$1$PrivacyControlActivity(int var1, ArrayList var2, boolean var3) {
      int var4 = this.neverShareRow;
      byte var5 = 0;
      if (var1 == var4) {
         this.currentMinus = var2;
         if (var3) {
            for(var1 = 0; var1 < this.currentMinus.size(); ++var1) {
               this.currentPlus.remove(this.currentMinus.get(var1));
            }
         }
      } else {
         this.currentPlus = var2;
         if (var3) {
            for(var1 = 0; var1 < this.currentPlus.size(); ++var1) {
               this.currentMinus.remove(this.currentPlus.get(var1));
            }
         }
      }

      View var6 = this.doneButton;
      byte var7;
      if (this.hasChanges()) {
         var7 = var5;
      } else {
         var7 = 8;
      }

      var6.setVisibility(var7);
      this.listAdapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$null$3$PrivacyControlActivity(AlertDialog var1, TLRPC.TL_error var2, TLObject var3) {
      if (var1 != null) {
         try {
            var1.dismiss();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

      if (var2 == null) {
         TLRPC.TL_account_privacyRules var5 = (TLRPC.TL_account_privacyRules)var3;
         MessagesController.getInstance(super.currentAccount).putUsers(var5.users, false);
         MessagesController.getInstance(super.currentAccount).putChats(var5.chats, false);
         ContactsController.getInstance(super.currentAccount).setPrivacyRules(var5.rules, this.rulesType);
         this.finishFragment();
      } else {
         this.showErrorAlert();
      }

   }

   // $FF: synthetic method
   public void lambda$processDone$5$PrivacyControlActivity(SharedPreferences var1, DialogInterface var2, int var3) {
      this.applyCurrentPrivacySettings();
      var1.edit().putBoolean("privacyAlertShowed", true).commit();
   }

   public boolean onBackPressed() {
      return this.checkDiscard();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.checkPrivacy();
      this.updateRows();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
   }

   public void onResume() {
      super.onResume();
      this.lastCheckedType = -1;
      this.enableAnimation = false;
   }

   private static class LinkMovementMethodMy extends LinkMovementMethod {
      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      private int getUsersCount(ArrayList var1) {
         int var2 = 0;

         int var3;
         int var4;
         for(var3 = 0; var2 < var1.size(); var3 = var4) {
            var4 = (Integer)var1.get(var2);
            if (var4 > 0) {
               var4 = var3 + 1;
            } else {
               TLRPC.Chat var5 = PrivacyControlActivity.this.getMessagesController().getChat(-var4);
               var4 = var3;
               if (var5 != null) {
                  var4 = var3 + var5.participants_count;
               }
            }

            ++var2;
         }

         return var3;
      }

      public int getItemCount() {
         return PrivacyControlActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != PrivacyControlActivity.this.alwaysShareRow && var1 != PrivacyControlActivity.this.neverShareRow && var1 != PrivacyControlActivity.this.p2pRow) {
            if (var1 != PrivacyControlActivity.this.shareDetailRow && var1 != PrivacyControlActivity.this.detailRow && var1 != PrivacyControlActivity.this.p2pDetailRow) {
               if (var1 != PrivacyControlActivity.this.sectionRow && var1 != PrivacyControlActivity.this.shareSectionRow && var1 != PrivacyControlActivity.this.p2pSectionRow) {
                  if (var1 != PrivacyControlActivity.this.everybodyRow && var1 != PrivacyControlActivity.this.myContactsRow && var1 != PrivacyControlActivity.this.nobodyRow) {
                     return var1 == PrivacyControlActivity.this.messageRow ? 4 : 0;
                  } else {
                     return 3;
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

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 == PrivacyControlActivity.this.nobodyRow || var2 == PrivacyControlActivity.this.everybodyRow || var2 == PrivacyControlActivity.this.myContactsRow || var2 == PrivacyControlActivity.this.neverShareRow || var2 == PrivacyControlActivity.this.alwaysShareRow || var2 == PrivacyControlActivity.this.p2pRow && !ContactsController.getInstance(PrivacyControlActivity.access$1500(PrivacyControlActivity.this)).getLoadingPrivicyInfo(3)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         if (var3 != 0) {
            byte var6 = 2;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     RadioCell var9;
                     byte var13;
                     label196: {
                        var9 = (RadioCell)var1.itemView;
                        String var7;
                        if (var2 == PrivacyControlActivity.this.everybodyRow) {
                           if (PrivacyControlActivity.this.rulesType == 3) {
                              var7 = LocaleController.getString("P2PEverybody", 2131560139);
                              if (PrivacyControlActivity.this.lastCheckedType == 0) {
                                 var5 = true;
                              } else {
                                 var5 = false;
                              }

                              var9.setText(var7, var5, true);
                           } else {
                              var7 = LocaleController.getString("LastSeenEverybody", 2131559736);
                              if (PrivacyControlActivity.this.lastCheckedType == 0) {
                                 var5 = true;
                              } else {
                                 var5 = false;
                              }

                              var9.setText(var7, var5, true);
                           }
                        } else {
                           if (var2 == PrivacyControlActivity.this.myContactsRow) {
                              if (PrivacyControlActivity.this.rulesType == 3) {
                                 var7 = LocaleController.getString("P2PContacts", 2131560134);
                                 if (PrivacyControlActivity.this.lastCheckedType == 2) {
                                    var5 = true;
                                 } else {
                                    var5 = false;
                                 }

                                 if (PrivacyControlActivity.this.nobodyRow != -1) {
                                    var4 = true;
                                 } else {
                                    var4 = false;
                                 }

                                 var9.setText(var7, var5, var4);
                                 var13 = var6;
                              } else {
                                 var7 = LocaleController.getString("LastSeenContacts", 2131559730);
                                 if (PrivacyControlActivity.this.lastCheckedType == 2) {
                                    var5 = true;
                                 } else {
                                    var5 = false;
                                 }

                                 if (PrivacyControlActivity.this.nobodyRow != -1) {
                                    var4 = true;
                                 } else {
                                    var4 = false;
                                 }

                                 var9.setText(var7, var5, var4);
                                 var13 = var6;
                              }
                              break label196;
                           }

                           if (var2 == PrivacyControlActivity.this.nobodyRow) {
                              if (PrivacyControlActivity.this.rulesType == 3) {
                                 var7 = LocaleController.getString("P2PNobody", 2131560141);
                                 if (PrivacyControlActivity.this.lastCheckedType == 1) {
                                    var5 = true;
                                 } else {
                                    var5 = false;
                                 }

                                 var9.setText(var7, var5, false);
                              } else {
                                 var7 = LocaleController.getString("LastSeenNobody", 2131559739);
                                 if (PrivacyControlActivity.this.lastCheckedType == 1) {
                                    var5 = true;
                                 } else {
                                    var5 = false;
                                 }

                                 var9.setText(var7, var5, false);
                              }

                              var13 = 1;
                              break label196;
                           }
                        }

                        var13 = 0;
                     }

                     if (PrivacyControlActivity.this.lastCheckedType == var13) {
                        var9.setChecked(false, PrivacyControlActivity.this.enableAnimation);
                     } else if (PrivacyControlActivity.this.currentType == var13) {
                        var9.setChecked(true, PrivacyControlActivity.this.enableAnimation);
                     }
                  }
               } else {
                  HeaderCell var10 = (HeaderCell)var1.itemView;
                  if (var2 == PrivacyControlActivity.this.sectionRow) {
                     if (PrivacyControlActivity.this.rulesType == 6) {
                        var10.setText(LocaleController.getString("PrivacyPhoneTitle", 2131560501));
                     } else if (PrivacyControlActivity.this.rulesType == 5) {
                        var10.setText(LocaleController.getString("PrivacyForwardsTitle", 2131560491));
                     } else if (PrivacyControlActivity.this.rulesType == 4) {
                        var10.setText(LocaleController.getString("PrivacyProfilePhotoTitle", 2131560508));
                     } else if (PrivacyControlActivity.this.rulesType == 3) {
                        var10.setText(LocaleController.getString("P2PEnabledWith", 2131560138));
                     } else if (PrivacyControlActivity.this.rulesType == 2) {
                        var10.setText(LocaleController.getString("WhoCanCallMe", 2131561120));
                     } else if (PrivacyControlActivity.this.rulesType == 1) {
                        var10.setText(LocaleController.getString("WhoCanAddMe", 2131561115));
                     } else {
                        var10.setText(LocaleController.getString("LastSeenTitle", 2131559741));
                     }
                  } else if (var2 == PrivacyControlActivity.this.shareSectionRow) {
                     var10.setText(LocaleController.getString("AddExceptions", 2131558570));
                  } else if (var2 == PrivacyControlActivity.this.p2pSectionRow) {
                     var10.setText(LocaleController.getString("PrivacyP2PHeader", 2131560495));
                  }
               }
            } else {
               TextInfoPrivacyCell var11 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == PrivacyControlActivity.this.detailRow) {
                  if (PrivacyControlActivity.this.rulesType == 6) {
                     var11.setText(LocaleController.getString("PrivacyPhoneInfo", 2131560499));
                  } else if (PrivacyControlActivity.this.rulesType == 5) {
                     var11.setText(LocaleController.getString("PrivacyForwardsInfo", 2131560487));
                  } else if (PrivacyControlActivity.this.rulesType == 4) {
                     var11.setText(LocaleController.getString("PrivacyProfilePhotoInfo", 2131560506));
                  } else if (PrivacyControlActivity.this.rulesType == 3) {
                     var11.setText(LocaleController.getString("PrivacyCallsP2PHelp", 2131560478));
                  } else if (PrivacyControlActivity.this.rulesType == 2) {
                     var11.setText(LocaleController.getString("WhoCanCallMeInfo", 2131561121));
                  } else if (PrivacyControlActivity.this.rulesType == 1) {
                     var11.setText(LocaleController.getString("WhoCanAddMeInfo", 2131561116));
                  } else {
                     var11.setText(LocaleController.getString("CustomHelp", 2131559187));
                  }

                  var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == PrivacyControlActivity.this.shareDetailRow) {
                  if (PrivacyControlActivity.this.rulesType == 6) {
                     var11.setText(LocaleController.getString("PrivacyPhoneInfo2", 2131560500));
                  } else if (PrivacyControlActivity.this.rulesType == 5) {
                     var11.setText(LocaleController.getString("PrivacyForwardsInfo2", 2131560488));
                  } else if (PrivacyControlActivity.this.rulesType == 4) {
                     var11.setText(LocaleController.getString("PrivacyProfilePhotoInfo2", 2131560507));
                  } else if (PrivacyControlActivity.this.rulesType == 3) {
                     var11.setText(LocaleController.getString("CustomP2PInfo", 2131559189));
                  } else if (PrivacyControlActivity.this.rulesType == 2) {
                     var11.setText(LocaleController.getString("CustomCallInfo", 2131559186));
                  } else if (PrivacyControlActivity.this.rulesType == 1) {
                     var11.setText(LocaleController.getString("CustomShareInfo", 2131559190));
                  } else {
                     var11.setText(LocaleController.getString("CustomShareSettingsHelp", 2131559191));
                  }

                  if (PrivacyControlActivity.this.rulesType == 2) {
                     var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                  } else {
                     var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  }
               } else if (var2 == PrivacyControlActivity.this.p2pDetailRow) {
                  var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            TextSettingsCell var14 = (TextSettingsCell)var1.itemView;
            String var12;
            if (var2 == PrivacyControlActivity.this.alwaysShareRow) {
               if (PrivacyControlActivity.this.currentPlus.size() != 0) {
                  var12 = LocaleController.formatPluralString("Users", this.getUsersCount(PrivacyControlActivity.this.currentPlus));
               } else {
                  var12 = LocaleController.getString("EmpryUsersPlaceholder", 2131559345);
               }

               String var8;
               if (PrivacyControlActivity.this.rulesType != 0) {
                  var8 = LocaleController.getString("AlwaysAllow", 2131558611);
                  if (PrivacyControlActivity.this.neverShareRow != -1) {
                     var5 = true;
                  }

                  var14.setTextAndValue(var8, var12, var5);
               } else {
                  var8 = LocaleController.getString("AlwaysShareWith", 2131558612);
                  var5 = var4;
                  if (PrivacyControlActivity.this.neverShareRow != -1) {
                     var5 = true;
                  }

                  var14.setTextAndValue(var8, var12, var5);
               }
            } else if (var2 == PrivacyControlActivity.this.neverShareRow) {
               if (PrivacyControlActivity.this.currentMinus.size() != 0) {
                  var12 = LocaleController.formatPluralString("Users", this.getUsersCount(PrivacyControlActivity.this.currentMinus));
               } else {
                  var12 = LocaleController.getString("EmpryUsersPlaceholder", 2131559345);
               }

               if (PrivacyControlActivity.this.rulesType != 0) {
                  var14.setTextAndValue(LocaleController.getString("NeverAllow", 2131559894), var12, false);
               } else {
                  var14.setTextAndValue(LocaleController.getString("NeverShareWith", 2131559895), var12, false);
               }
            } else if (var2 == PrivacyControlActivity.this.p2pRow) {
               if (ContactsController.getInstance(PrivacyControlActivity.access$2200(PrivacyControlActivity.this)).getLoadingPrivicyInfo(3)) {
                  var12 = LocaleController.getString("Loading", 2131559768);
               } else {
                  var12 = PrivacySettingsActivity.formatRulesString(PrivacyControlActivity.access$2300(PrivacyControlActivity.this), 3);
               }

               var14.setTextAndValue(LocaleController.getString("PrivacyP2P2", 2131560494), var12, false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = PrivacyControlActivity.this.messageCell;
                  } else {
                     var3 = new RadioCell(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
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

   private class MessageCell extends FrameLayout {
      private Drawable backgroundDrawable;
      private ChatMessageCell cell;
      private HintView hintView;
      private MessageObject messageObject;
      private Drawable shadowDrawable;

      public MessageCell(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.setClipToPadding(false);
         this.shadowDrawable = Theme.getThemedDrawable(var2, 2131165395, "windowBackgroundGrayShadow");
         this.setPadding(0, AndroidUtilities.dp(11.0F), 0, AndroidUtilities.dp(11.0F));
         int var3 = (int)(System.currentTimeMillis() / 1000L);
         TLRPC.User var4 = MessagesController.getInstance(PrivacyControlActivity.access$100(PrivacyControlActivity.this)).getUser(UserConfig.getInstance(PrivacyControlActivity.access$000(PrivacyControlActivity.this)).getClientUserId());
         TLRPC.TL_message var5 = new TLRPC.TL_message();
         var5.message = LocaleController.getString("PrivacyForwardsMessageLine", 2131560489);
         var5.date = var3 - 3600 + 60;
         var5.dialog_id = 1L;
         var5.flags = 261;
         var5.from_id = 0;
         var5.id = 1;
         var5.fwd_from = new TLRPC.TL_messageFwdHeader();
         var5.fwd_from.from_name = ContactsController.formatName(var4.first_name, var4.last_name);
         var5.media = new TLRPC.TL_messageMediaEmpty();
         var5.out = false;
         var5.to_id = new TLRPC.TL_peerUser();
         var5.to_id.user_id = UserConfig.getInstance(PrivacyControlActivity.access$200(PrivacyControlActivity.this)).getClientUserId();
         this.messageObject = new MessageObject(PrivacyControlActivity.access$300(PrivacyControlActivity.this), var5, true);
         MessageObject var7 = this.messageObject;
         var7.eventId = 1L;
         var7.resetLayout();
         this.cell = new ChatMessageCell(var2);
         this.cell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
            // $FF: synthetic method
            public boolean canPerformActions() {
               return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$canPerformActions(this);
            }

            // $FF: synthetic method
            public void didLongPress(ChatMessageCell var1, float var2, float var3) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
            }

            // $FF: synthetic method
            public void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
            }

            // $FF: synthetic method
            public void didPressCancelSendButton(ChatMessageCell var1) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressCancelSendButton(this, var1);
            }

            // $FF: synthetic method
            public void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressChannelAvatar(this, var1, var2, var3, var4, var5);
            }

            // $FF: synthetic method
            public void didPressHiddenForward(ChatMessageCell var1) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressHiddenForward(this, var1);
            }

            // $FF: synthetic method
            public void didPressImage(ChatMessageCell var1, float var2, float var3) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressImage(this, var1, var2, var3);
            }

            // $FF: synthetic method
            public void didPressInstantButton(ChatMessageCell var1, int var2) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressInstantButton(this, var1, var2);
            }

            // $FF: synthetic method
            public void didPressOther(ChatMessageCell var1, float var2, float var3) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressOther(this, var1, var2, var3);
            }

            // $FF: synthetic method
            public void didPressReplyMessage(ChatMessageCell var1, int var2) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
            }

            // $FF: synthetic method
            public void didPressShare(ChatMessageCell var1) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressShare(this, var1);
            }

            // $FF: synthetic method
            public void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUrl(this, var1, var2, var3);
            }

            // $FF: synthetic method
            public void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUserAvatar(this, var1, var2, var3, var4);
            }

            // $FF: synthetic method
            public void didPressViaBot(ChatMessageCell var1, String var2) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressViaBot(this, var1, var2);
            }

            // $FF: synthetic method
            public void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressVoteButton(this, var1, var2);
            }

            // $FF: synthetic method
            public void didStartVideoStream(MessageObject var1) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didStartVideoStream(this, var1);
            }

            // $FF: synthetic method
            public boolean isChatAdminCell(int var1) {
               return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$isChatAdminCell(this, var1);
            }

            // $FF: synthetic method
            public void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6) {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needOpenWebView(this, var1, var2, var3, var4, var5, var6);
            }

            // $FF: synthetic method
            public boolean needPlayMessage(MessageObject var1) {
               return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needPlayMessage(this, var1);
            }

            // $FF: synthetic method
            public void videoTimerReached() {
               ChatMessageCell$ChatMessageCellDelegate$_CC.$default$videoTimerReached(this);
            }
         });
         ChatMessageCell var6 = this.cell;
         var6.isChat = false;
         var6.setFullyDraw(true);
         this.cell.setMessageObject(this.messageObject, (MessageObject.GroupedMessages)null, false, false);
         this.addView(this.cell, LayoutHelper.createLinear(-1, -2));
         this.hintView = new HintView(var2, 1, true);
         this.addView(this.hintView, LayoutHelper.createFrame(-2, -2.0F, 51, 19.0F, 0.0F, 19.0F, 0.0F));
      }

      protected void dispatchDraw(Canvas var1) {
         super.dispatchDraw(var1);
         this.hintView.showForMessageCell(this.cell, false);
      }

      protected void dispatchSetPressed(boolean var1) {
      }

      public boolean dispatchTouchEvent(MotionEvent var1) {
         return false;
      }

      public void invalidate() {
         super.invalidate();
         this.cell.invalidate();
      }

      protected void onDraw(Canvas var1) {
         Drawable var2 = Theme.getCachedWallpaperNonBlocking();
         if (var2 != null) {
            this.backgroundDrawable = var2;
         }

         var2 = this.backgroundDrawable;
         if (var2 instanceof ColorDrawable) {
            var2.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            this.backgroundDrawable.draw(var1);
         } else if (var2 instanceof BitmapDrawable) {
            float var3;
            if (((BitmapDrawable)var2).getTileModeX() == TileMode.REPEAT) {
               var1.save();
               var3 = 2.0F / AndroidUtilities.density;
               var1.scale(var3, var3);
               this.backgroundDrawable.setBounds(0, 0, (int)Math.ceil((double)((float)this.getMeasuredWidth() / var3)), (int)Math.ceil((double)((float)this.getMeasuredHeight() / var3)));
               this.backgroundDrawable.draw(var1);
               var1.restore();
            } else {
               int var4 = this.getMeasuredHeight();
               float var5 = (float)this.getMeasuredWidth() / (float)this.backgroundDrawable.getIntrinsicWidth();
               float var6 = (float)var4 / (float)this.backgroundDrawable.getIntrinsicHeight();
               var3 = var5;
               if (var5 < var6) {
                  var3 = var6;
               }

               int var7 = (int)Math.ceil((double)((float)this.backgroundDrawable.getIntrinsicWidth() * var3));
               int var8 = (int)Math.ceil((double)((float)this.backgroundDrawable.getIntrinsicHeight() * var3));
               int var9 = (this.getMeasuredWidth() - var7) / 2;
               var4 = (var4 - var8) / 2;
               var1.save();
               var1.clipRect(0, 0, var7, this.getMeasuredHeight());
               this.backgroundDrawable.setBounds(var9, var4, var7 + var9, var8 + var4);
               this.backgroundDrawable.draw(var1);
               var1.restore();
            }
         } else {
            super.onDraw(var1);
         }

         this.shadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
         this.shadowDrawable.draw(var1);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         return false;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         return false;
      }
   }
}
