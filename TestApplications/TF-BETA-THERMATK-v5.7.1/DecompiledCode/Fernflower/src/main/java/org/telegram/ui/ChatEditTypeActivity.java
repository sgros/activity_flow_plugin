package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ChatEditTypeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private ArrayList adminedChannelCells = new ArrayList();
   private ShadowSectionCell adminedInfoCell;
   private LinearLayout adminnedChannelsLayout;
   private boolean canCreatePublic = true;
   private int chatId;
   private int checkReqId;
   private Runnable checkRunnable;
   private TextInfoPrivacyCell checkTextView;
   private TextSettingsCell copyCell;
   private TLRPC.Chat currentChat;
   private EditText editText;
   private HeaderCell headerCell;
   private HeaderCell headerCell2;
   private TLRPC.ChatFull info;
   private TextInfoPrivacyCell infoCell;
   private TLRPC.ExportedChatInvite invite;
   private boolean isChannel;
   private boolean isPrivate;
   private String lastCheckName;
   private boolean lastNameAvailable;
   private LinearLayout linearLayout;
   private LinearLayout linearLayoutTypeContainer;
   private LinearLayout linkContainer;
   private LoadingCell loadingAdminedCell;
   private boolean loadingAdminedChannels;
   private boolean loadingInvite;
   private LinearLayout privateContainer;
   private TextBlockCell privateTextView;
   private LinearLayout publicContainer;
   private RadioButtonCell radioButtonCell1;
   private RadioButtonCell radioButtonCell2;
   private TextSettingsCell revokeCell;
   private ShadowSectionCell sectionCell2;
   private TextSettingsCell shareCell;
   private TextSettingsCell textCell;
   private TextSettingsCell textCell2;
   private TextInfoPrivacyCell typeInfoCell;
   private EditTextBoldCursor usernameTextView;

   public ChatEditTypeActivity(int var1) {
      this.chatId = var1;
   }

   private boolean checkUserName(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.checkTextView.setVisibility(0);
      } else {
         this.checkTextView.setVisibility(8);
      }

      TextInfoPrivacyCell var2 = this.typeInfoCell;
      Drawable var3;
      if (this.checkTextView.getVisibility() == 0) {
         var3 = null;
      } else {
         var3 = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
      }

      var2.setBackgroundDrawable(var3);
      Runnable var6 = this.checkRunnable;
      if (var6 != null) {
         AndroidUtilities.cancelRunOnUIThread(var6);
         this.checkRunnable = null;
         this.lastCheckName = null;
         if (this.checkReqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.checkReqId, true);
         }
      }

      this.lastNameAvailable = false;
      if (var1 != null) {
         if (var1.startsWith("_") || var1.endsWith("_")) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
         }

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            char var5 = var1.charAt(var4);
            if (var4 == 0 && var5 >= '0' && var5 <= '9') {
               if (this.isChannel) {
                  this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", 2131559759));
               } else {
                  this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumberMega", 2131559760));
               }

               this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
               return false;
            }

            if ((var5 < '0' || var5 > '9') && (var5 < 'a' || var5 > 'z') && (var5 < 'A' || var5 > 'Z') && var5 != '_') {
               this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
               this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
               return false;
            }
         }
      }

      if (var1 != null && var1.length() >= 5) {
         if (var1.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", 2131559756));
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
         } else {
            this.checkTextView.setText(LocaleController.getString("LinkChecking", 2131559750));
            this.checkTextView.setTextColor("windowBackgroundWhiteGrayText8");
            this.lastCheckName = var1;
            this.checkRunnable = new _$$Lambda$ChatEditTypeActivity$kFJ66e_ln7abTbKYBYoWFyd6BAI(this, var1);
            AndroidUtilities.runOnUIThread(this.checkRunnable, 300L);
            return true;
         }
      } else {
         if (this.isChannel) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", 2131559757));
         } else {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidShortMega", 2131559758));
         }

         this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
         return false;
      }
   }

   private void generateLink(boolean var1) {
      this.loadingInvite = true;
      TLRPC.TL_messages_exportChatInvite var2 = new TLRPC.TL_messages_exportChatInvite();
      var2.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chatId);
      int var3 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As(this, var1));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var3, super.classGuid);
   }

   private void loadAdminedChannels() {
      if (!this.loadingAdminedChannels && this.adminnedChannelsLayout != null) {
         this.loadingAdminedChannels = true;
         this.updatePrivatePublic();
         TLRPC.TL_channels_getAdminedPublicChannels var1 = new TLRPC.TL_channels_getAdminedPublicChannels();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$ChatEditTypeActivity$kDy8MSXJZPphT1ROvE66SmVIgsc(this));
      }

   }

   private void processDone() {
      String var1;
      if (!this.isPrivate) {
         label51: {
            if (this.currentChat.username != null || this.usernameTextView.length() == 0) {
               var1 = this.currentChat.username;
               if (var1 == null || var1.equalsIgnoreCase(this.usernameTextView.getText().toString())) {
                  break label51;
               }
            }

            if (this.usernameTextView.length() != 0 && !this.lastNameAvailable) {
               Vibrator var3 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
               if (var3 != null) {
                  var3.vibrate(200L);
               }

               AndroidUtilities.shakeView(this.checkTextView, 2.0F, 0);
               return;
            }
         }
      }

      var1 = this.currentChat.username;
      String var2 = "";
      if (var1 == null) {
         var1 = "";
      }

      if (!this.isPrivate) {
         var2 = this.usernameTextView.getText().toString();
      }

      if (!var1.equals(var2)) {
         if (!ChatObject.isChannel(this.currentChat)) {
            MessagesController.getInstance(super.currentAccount).convertToMegaGroup(this.getParentActivity(), this.chatId, new _$$Lambda$ChatEditTypeActivity$wzFYEdqnqyRZtP7rz7ySevzOMoo(this));
            return;
         }

         MessagesController.getInstance(super.currentAccount).updateChannelUserName(this.chatId, var2);
         this.currentChat.username = var2;
      }

      this.finishFragment();
   }

   private void updatePrivatePublic() {
      if (this.sectionCell2 != null) {
         boolean var1 = this.isPrivate;
         TextInfoPrivacyCell var2 = null;
         byte var3 = 8;
         TextInfoPrivacyCell var4;
         Drawable var13;
         if (!var1 && !this.canCreatePublic) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131558916));
            this.typeInfoCell.setTag("windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.sectionCell2.setVisibility(8);
            this.adminedInfoCell.setVisibility(0);
            if (this.loadingAdminedChannels) {
               this.loadingAdminedCell.setVisibility(0);
               this.adminnedChannelsLayout.setVisibility(8);
               var2 = this.typeInfoCell;
               if (this.checkTextView.getVisibility() == 0) {
                  var13 = null;
               } else {
                  var13 = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
               }

               var2.setBackgroundDrawable(var13);
               this.adminedInfoCell.setBackgroundDrawable((Drawable)null);
            } else {
               ShadowSectionCell var14 = this.adminedInfoCell;
               var14.setBackgroundDrawable(Theme.getThemedDrawable(var14.getContext(), 2131165395, "windowBackgroundGrayShadow"));
               var4 = this.typeInfoCell;
               var4.setBackgroundDrawable(Theme.getThemedDrawable(var4.getContext(), 2131165396, "windowBackgroundGrayShadow"));
               this.loadingAdminedCell.setVisibility(8);
               this.adminnedChannelsLayout.setVisibility(0);
            }
         } else {
            this.typeInfoCell.setTag("windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.sectionCell2.setVisibility(0);
            this.adminedInfoCell.setVisibility(8);
            var4 = this.typeInfoCell;
            var4.setBackgroundDrawable(Theme.getThemedDrawable(var4.getContext(), 2131165395, "windowBackgroundGrayShadow"));
            this.adminnedChannelsLayout.setVisibility(8);
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            TextInfoPrivacyCell var5;
            int var6;
            String var7;
            HeaderCell var8;
            if (this.isChannel) {
               var5 = this.typeInfoCell;
               if (this.isPrivate) {
                  var6 = 2131558990;
                  var7 = "ChannelPrivateLinkHelp";
               } else {
                  var6 = 2131559013;
                  var7 = "ChannelUsernameHelp";
               }

               var5.setText(LocaleController.getString(var7, var6));
               var8 = this.headerCell;
               if (this.isPrivate) {
                  var7 = LocaleController.getString("ChannelInviteLinkTitle", 2131558952);
               } else {
                  var7 = LocaleController.getString("ChannelLinkTitle", 2131558960);
               }

               var8.setText(var7);
            } else {
               var5 = this.typeInfoCell;
               if (this.isPrivate) {
                  var6 = 2131559833;
                  var7 = "MegaPrivateLinkHelp";
               } else {
                  var6 = 2131559836;
                  var7 = "MegaUsernameHelp";
               }

               var5.setText(LocaleController.getString(var7, var6));
               var8 = this.headerCell;
               if (this.isPrivate) {
                  var7 = LocaleController.getString("ChannelInviteLinkTitle", 2131558952);
               } else {
                  var7 = LocaleController.getString("ChannelLinkTitle", 2131558960);
               }

               var8.setText(var7);
            }

            LinearLayout var11 = this.publicContainer;
            byte var10;
            if (this.isPrivate) {
               var10 = 8;
            } else {
               var10 = 0;
            }

            var11.setVisibility(var10);
            var11 = this.privateContainer;
            if (this.isPrivate) {
               var10 = 0;
            } else {
               var10 = 8;
            }

            var11.setVisibility(var10);
            var11 = this.linkContainer;
            if (this.isPrivate) {
               var6 = 0;
            } else {
               var6 = AndroidUtilities.dp(7.0F);
            }

            var11.setPadding(0, 0, 0, var6);
            TextBlockCell var9 = this.privateTextView;
            TLRPC.ExportedChatInvite var12 = this.invite;
            if (var12 != null) {
               var7 = var12.link;
            } else {
               var7 = LocaleController.getString("Loading", 2131559768);
            }

            var9.setText(var7, true);
            var4 = this.checkTextView;
            var10 = var3;
            if (!this.isPrivate) {
               var10 = var3;
               if (var4.length() != 0) {
                  var10 = 0;
               }
            }

            var4.setVisibility(var10);
            var5 = this.typeInfoCell;
            if (this.checkTextView.getVisibility() == 0) {
               var13 = var2;
            } else {
               var13 = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
            }

            var5.setBackgroundDrawable(var13);
         }

         this.radioButtonCell1.setChecked(this.isPrivate ^ true, true);
         this.radioButtonCell2.setChecked(this.isPrivate, true);
         this.usernameTextView.clearFocus();
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChatEditTypeActivity.this.finishFragment();
            } else if (var1 == 1) {
               ChatEditTypeActivity.this.processDone();
            }

         }
      });
      super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      super.fragmentView = new ScrollView(var1) {
         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            var2.bottom += AndroidUtilities.dp(60.0F);
            return super.requestChildRectangleOnScreen(var1, var2, var3);
         }
      };
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      ScrollView var2 = (ScrollView)super.fragmentView;
      var2.setFillViewport(true);
      this.linearLayout = new LinearLayout(var1);
      var2.addView(this.linearLayout, new LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
      if (this.isChannel) {
         super.actionBar.setTitle(LocaleController.getString("ChannelSettingsTitle", 2131559000));
      } else {
         super.actionBar.setTitle(LocaleController.getString("GroupSettingsTitle", 2131559614));
      }

      this.linearLayoutTypeContainer = new LinearLayout(var1);
      this.linearLayoutTypeContainer.setOrientation(1);
      this.linearLayoutTypeContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout.addView(this.linearLayoutTypeContainer, LayoutHelper.createLinear(-1, -2));
      this.headerCell2 = new HeaderCell(var1, 23);
      this.headerCell2.setHeight(46);
      if (this.isChannel) {
         this.headerCell2.setText(LocaleController.getString("ChannelTypeHeader", 2131559006));
      } else {
         this.headerCell2.setText(LocaleController.getString("GroupTypeHeader", 2131559618));
      }

      this.linearLayoutTypeContainer.addView(this.headerCell2);
      this.radioButtonCell2 = new RadioButtonCell(var1);
      this.radioButtonCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      if (this.isChannel) {
         this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131558988), LocaleController.getString("ChannelPrivateInfo", 2131558989), false, this.isPrivate);
      } else {
         this.radioButtonCell2.setTextAndValue(LocaleController.getString("MegaPrivate", 2131559831), LocaleController.getString("MegaPrivateInfo", 2131559832), false, this.isPrivate);
      }

      this.linearLayoutTypeContainer.addView(this.radioButtonCell2, LayoutHelper.createLinear(-1, -2));
      this.radioButtonCell2.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$ocPS9yaO8oX3caNaqb3brf0F0tM(this));
      this.radioButtonCell1 = new RadioButtonCell(var1);
      this.radioButtonCell1.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      if (this.isChannel) {
         this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131558991), LocaleController.getString("ChannelPublicInfo", 2131558993), false, this.isPrivate ^ true);
      } else {
         this.radioButtonCell1.setTextAndValue(LocaleController.getString("MegaPublic", 2131559834), LocaleController.getString("MegaPublicInfo", 2131559835), false, this.isPrivate ^ true);
      }

      this.linearLayoutTypeContainer.addView(this.radioButtonCell1, LayoutHelper.createLinear(-1, -2));
      this.radioButtonCell1.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$IpgElbmOCajv_apzoxCqnnTdt5I(this));
      this.sectionCell2 = new ShadowSectionCell(var1);
      this.linearLayout.addView(this.sectionCell2, LayoutHelper.createLinear(-1, -2));
      this.linkContainer = new LinearLayout(var1);
      this.linkContainer.setOrientation(1);
      this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout.addView(this.linkContainer, LayoutHelper.createLinear(-1, -2));
      this.headerCell = new HeaderCell(var1, 23);
      this.linkContainer.addView(this.headerCell);
      this.publicContainer = new LinearLayout(var1);
      this.publicContainer.setOrientation(0);
      this.linkContainer.addView(this.publicContainer, LayoutHelper.createLinear(-1, 36, 23.0F, 7.0F, 23.0F, 0.0F));
      this.editText = new EditText(var1);
      EditText var3 = this.editText;
      StringBuilder var4 = new StringBuilder();
      var4.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
      var4.append("/");
      var3.setText(var4.toString());
      this.editText.setTextSize(1, 18.0F);
      this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.editText.setMaxLines(1);
      this.editText.setLines(1);
      this.editText.setEnabled(false);
      this.editText.setBackgroundDrawable((Drawable)null);
      this.editText.setPadding(0, 0, 0, 0);
      this.editText.setSingleLine(true);
      this.editText.setInputType(163840);
      this.editText.setImeOptions(6);
      this.publicContainer.addView(this.editText, LayoutHelper.createLinear(-2, 36));
      this.usernameTextView = new EditTextBoldCursor(var1);
      this.usernameTextView.setTextSize(1, 18.0F);
      if (!this.isPrivate) {
         this.usernameTextView.setText(this.currentChat.username);
      }

      this.usernameTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.usernameTextView.setMaxLines(1);
      this.usernameTextView.setLines(1);
      this.usernameTextView.setBackgroundDrawable((Drawable)null);
      this.usernameTextView.setPadding(0, 0, 0, 0);
      this.usernameTextView.setSingleLine(true);
      this.usernameTextView.setInputType(163872);
      this.usernameTextView.setImeOptions(6);
      this.usernameTextView.setHint(LocaleController.getString("ChannelUsernamePlaceholder", 2131559014));
      this.usernameTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.usernameTextView.setCursorSize(AndroidUtilities.dp(20.0F));
      this.usernameTextView.setCursorWidth(1.5F);
      this.publicContainer.addView(this.usernameTextView, LayoutHelper.createLinear(-1, 36));
      this.usernameTextView.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            ChatEditTypeActivity var5 = ChatEditTypeActivity.this;
            var5.checkUserName(var5.usernameTextView.getText().toString());
         }
      });
      this.privateContainer = new LinearLayout(var1);
      this.privateContainer.setOrientation(1);
      this.linkContainer.addView(this.privateContainer, LayoutHelper.createLinear(-1, -2));
      this.privateTextView = new TextBlockCell(var1);
      this.privateTextView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.privateContainer.addView(this.privateTextView);
      this.privateTextView.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$7JS5wQ6nvw2zxnxic24e587d6Z8(this));
      this.copyCell = new TextSettingsCell(var1);
      this.copyCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.copyCell.setText(LocaleController.getString("CopyLink", 2131559164), true);
      this.privateContainer.addView(this.copyCell, LayoutHelper.createLinear(-1, -2));
      this.copyCell.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$TZcp0ymcWTiUlpFxGrOZVdeBPZM(this));
      this.revokeCell = new TextSettingsCell(var1);
      this.revokeCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.revokeCell.setText(LocaleController.getString("RevokeLink", 2131560620), true);
      this.privateContainer.addView(this.revokeCell, LayoutHelper.createLinear(-1, -2));
      this.revokeCell.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$MZJTil1g1UVkSx0GMskvwQvMJ0k(this));
      this.shareCell = new TextSettingsCell(var1);
      this.shareCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.shareCell.setText(LocaleController.getString("ShareLink", 2131560749), false);
      this.privateContainer.addView(this.shareCell, LayoutHelper.createLinear(-1, -2));
      this.shareCell.setOnClickListener(new _$$Lambda$ChatEditTypeActivity$3aw4OO93rb5NA_LBByFsperljoU(this));
      this.checkTextView = new TextInfoPrivacyCell(var1);
      this.checkTextView.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.checkTextView.setBottomPadding(6);
      this.linearLayout.addView(this.checkTextView, LayoutHelper.createLinear(-2, -2));
      this.typeInfoCell = new TextInfoPrivacyCell(var1);
      this.linearLayout.addView(this.typeInfoCell, LayoutHelper.createLinear(-1, -2));
      this.loadingAdminedCell = new LoadingCell(var1);
      this.linearLayout.addView(this.loadingAdminedCell, LayoutHelper.createLinear(-1, -2));
      this.adminnedChannelsLayout = new LinearLayout(var1);
      this.adminnedChannelsLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.adminnedChannelsLayout.setOrientation(1);
      this.linearLayout.addView(this.adminnedChannelsLayout, LayoutHelper.createLinear(-1, -2));
      this.adminedInfoCell = new ShadowSectionCell(var1);
      this.linearLayout.addView(this.adminedInfoCell, LayoutHelper.createLinear(-1, -2));
      this.updatePrivatePublic();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatInfoDidLoad) {
         TLRPC.ChatFull var4 = (TLRPC.ChatFull)var3[0];
         if (var4.id == this.chatId) {
            this.info = var4;
            this.invite = var4.exported_invite;
            this.updatePrivatePublic();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM var1 = new _$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM(this);
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.sectionCell2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.infoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.infoCell, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.textCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.textCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5"), new ThemeDescription(this.textCell2, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.textCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.linearLayoutTypeContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.linkContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.headerCell2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGreenText"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.privateTextView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.privateTextView, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.loadingAdminedCell, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.copyCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.copyCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.revokeCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.revokeCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.shareCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.shareCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"deleteButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public void lambda$checkUserName$19$ChatEditTypeActivity(String var1) {
      TLRPC.TL_channels_checkUsername var2 = new TLRPC.TL_channels_checkUsername();
      var2.username = var1;
      var2.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
      this.checkReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$ChatEditTypeActivity$tnmJrzBKMOfOJQl31MjjEIQidqw(this, var1), 2);
   }

   // $FF: synthetic method
   public void lambda$createView$3$ChatEditTypeActivity(View var1) {
      if (!this.isPrivate) {
         this.isPrivate = true;
         this.updatePrivatePublic();
      }
   }

   // $FF: synthetic method
   public void lambda$createView$4$ChatEditTypeActivity(View var1) {
      if (this.isPrivate) {
         this.isPrivate = false;
         this.updatePrivatePublic();
      }
   }

   // $FF: synthetic method
   public void lambda$createView$5$ChatEditTypeActivity(View var1) {
      if (this.invite != null) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$createView$6$ChatEditTypeActivity(View var1) {
      if (this.invite != null) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$createView$8$ChatEditTypeActivity(View var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setMessage(LocaleController.getString("RevokeAlert", 2131560617));
      var2.setTitle(LocaleController.getString("RevokeLink", 2131560620));
      var2.setPositiveButton(LocaleController.getString("RevokeButton", 2131560619), new _$$Lambda$ChatEditTypeActivity$QEz_zw_4wfdKKBHW3Kn6oNi5pu8(this));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      this.showDialog(var2.create());
   }

   // $FF: synthetic method
   public void lambda$createView$9$ChatEditTypeActivity(View var1) {
      if (this.invite != null) {
         try {
            Intent var3 = new Intent("android.intent.action.SEND");
            var3.setType("text/plain");
            var3.putExtra("android.intent.extra.TEXT", this.invite.link);
            this.getParentActivity().startActivityForResult(Intent.createChooser(var3, LocaleController.getString("InviteToGroupByLink", 2131559688)), 500);
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$generateLink$21$ChatEditTypeActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditTypeActivity$QJca9ThplCpxIMzhhJYYsX0k5YM(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$22$ChatEditTypeActivity() {
      LinearLayout var1 = this.adminnedChannelsLayout;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.adminnedChannelsLayout.getChildAt(var3);
            if (var4 instanceof AdminedChannelCell) {
               ((AdminedChannelCell)var4).update();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadAdminedChannels$16$ChatEditTypeActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditTypeActivity$QRTtp_Z_W9ScH4dQ0r3b172yIRQ(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$1$ChatEditTypeActivity(TLRPC.TL_error var1) {
      boolean var2;
      if (var1 != null && var1.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.canCreatePublic = var2;
      if (!this.canCreatePublic) {
         this.loadAdminedChannels();
      }

   }

   // $FF: synthetic method
   public void lambda$null$11$ChatEditTypeActivity() {
      this.canCreatePublic = true;
      if (this.usernameTextView.length() > 0) {
         this.checkUserName(this.usernameTextView.getText().toString());
      }

      this.updatePrivatePublic();
   }

   // $FF: synthetic method
   public void lambda$null$12$ChatEditTypeActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 instanceof TLRPC.TL_boolTrue) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditTypeActivity$RD9_5i_WVZsRLoq50iXWE1Pepqg(this));
      }

   }

   // $FF: synthetic method
   public void lambda$null$13$ChatEditTypeActivity(TLRPC.Chat var1, DialogInterface var2, int var3) {
      TLRPC.TL_channels_updateUsername var4 = new TLRPC.TL_channels_updateUsername();
      var4.channel = MessagesController.getInputChannel(var1);
      var4.username = "";
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$ChatEditTypeActivity$16BX7QJDGBSQnfTPIEFvTHPHhfQ(this), 64);
   }

   // $FF: synthetic method
   public void lambda$null$14$ChatEditTypeActivity(View var1) {
      TLRPC.Chat var4 = ((AdminedChannelCell)var1.getParent()).getCurrentChannel();
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setTitle(LocaleController.getString("AppName", 2131558635));
      StringBuilder var3;
      if (this.isChannel) {
         var3 = new StringBuilder();
         var3.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var3.append("/");
         var3.append(var4.username);
         var2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", 2131560622, var3.toString(), var4.title)));
      } else {
         var3 = new StringBuilder();
         var3.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var3.append("/");
         var3.append(var4.username);
         var2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", 2131560621, var3.toString(), var4.title)));
      }

      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var2.setPositiveButton(LocaleController.getString("RevokeButton", 2131560619), new _$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg(this, var4));
      this.showDialog(var2.create());
   }

   // $FF: synthetic method
   public void lambda$null$15$ChatEditTypeActivity(TLObject var1) {
      this.loadingAdminedChannels = false;
      if (var1 != null) {
         if (this.getParentActivity() == null) {
            return;
         }

         int var2;
         for(var2 = 0; var2 < this.adminedChannelCells.size(); ++var2) {
            this.linearLayout.removeView((View)this.adminedChannelCells.get(var2));
         }

         this.adminedChannelCells.clear();
         TLRPC.TL_messages_chats var7 = (TLRPC.TL_messages_chats)var1;

         for(var2 = 0; var2 < var7.chats.size(); ++var2) {
            AdminedChannelCell var3 = new AdminedChannelCell(this.getParentActivity(), new _$$Lambda$ChatEditTypeActivity$20mFPaWfDYXXR9Gd87amavGwrLI(this));
            TLRPC.Chat var4 = (TLRPC.Chat)var7.chats.get(var2);
            int var5 = var7.chats.size();
            boolean var6 = true;
            if (var2 != var5 - 1) {
               var6 = false;
            }

            var3.setChannel(var4, var6);
            this.adminedChannelCells.add(var3);
            this.adminnedChannelsLayout.addView(var3, LayoutHelper.createLinear(-1, 72));
         }

         this.updatePrivatePublic();
      }

   }

   // $FF: synthetic method
   public void lambda$null$17$ChatEditTypeActivity(String var1, TLRPC.TL_error var2, TLObject var3) {
      this.checkReqId = 0;
      String var4 = this.lastCheckName;
      if (var4 != null && var4.equals(var1)) {
         if (var2 == null && var3 instanceof TLRPC.TL_boolTrue) {
            this.checkTextView.setText(LocaleController.formatString("LinkAvailable", 2131559749, var1));
            this.checkTextView.setTextColor("windowBackgroundWhiteGreenText");
            this.lastNameAvailable = true;
         } else {
            if (var2 != null && var2.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
               this.canCreatePublic = false;
               this.loadAdminedChannels();
            } else {
               this.checkTextView.setText(LocaleController.getString("LinkInUse", 2131559753));
            }

            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            this.lastNameAvailable = false;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$18$ChatEditTypeActivity(String var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditTypeActivity$IqTkR4fY_wDnaSet6exFaZwpQVw(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$null$20$ChatEditTypeActivity(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      if (var1 == null) {
         this.invite = (TLRPC.ExportedChatInvite)var2;
         TLRPC.ChatFull var4 = this.info;
         if (var4 != null) {
            var4.exported_invite = this.invite;
         }

         if (var3) {
            if (this.getParentActivity() == null) {
               return;
            }

            AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
            var5.setMessage(LocaleController.getString("RevokeAlertNewLink", 2131560618));
            var5.setTitle(LocaleController.getString("RevokeLink", 2131560620));
            var5.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            this.showDialog(var5.create());
         }
      }

      this.loadingInvite = false;
      TextBlockCell var7 = this.privateTextView;
      if (var7 != null) {
         TLRPC.ExportedChatInvite var6 = this.invite;
         String var8;
         if (var6 != null) {
            var8 = var6.link;
         } else {
            var8 = LocaleController.getString("Loading", 2131559768);
         }

         var7.setText(var8, true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$ChatEditTypeActivity(DialogInterface var1, int var2) {
      this.generateLink(true);
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$0$ChatEditTypeActivity(CountDownLatch var1) {
      this.currentChat = MessagesStorage.getInstance(super.currentAccount).getChat(this.chatId);
      var1.countDown();
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$2$ChatEditTypeActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditTypeActivity$YaiiLlvDLGglsjOvYPurrXC0wDs(this, var2));
   }

   // $FF: synthetic method
   public void lambda$processDone$10$ChatEditTypeActivity(int var1) {
      this.chatId = var1;
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(var1);
      this.processDone();
   }

   public boolean onFragmentCreate() {
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
      TLRPC.Chat var1 = this.currentChat;
      boolean var2 = true;
      if (var1 == null) {
         label54: {
            CountDownLatch var3 = new CountDownLatch(1);
            MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE(this, var3));

            try {
               var3.await();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }

            if (this.currentChat != null) {
               MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
               if (this.info != null) {
                  break label54;
               }

               MessagesStorage.getInstance(super.currentAccount).loadChatInfo(this.chatId, var3, false, false);

               try {
                  var3.await();
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }

               if (this.info != null) {
                  break label54;
               }
            }

            return false;
         }
      }

      this.isPrivate = TextUtils.isEmpty(this.currentChat.username);
      if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
         var2 = false;
      }

      this.isChannel = var2;
      if (this.isPrivate && this.currentChat.creator) {
         TLRPC.TL_channels_checkUsername var6 = new TLRPC.TL_channels_checkUsername();
         var6.username = "1";
         var6.channel = new TLRPC.TL_inputChannelEmpty();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var6, new _$$Lambda$ChatEditTypeActivity$cvbA8EN1qfRN8RDcsasuoHRxqE8(this));
      }

      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
      TextSettingsCell var1 = this.textCell2;
      if (var1 != null) {
         TLRPC.ChatFull var2 = this.info;
         if (var2 != null) {
            if (var2.stickerset != null) {
               var1.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
            } else {
               var1.setText(LocaleController.getString("GroupStickers", 2131559615), false);
            }
         }
      }

   }

   public void setInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      if (var1 != null) {
         TLRPC.ExportedChatInvite var2 = var1.exported_invite;
         if (var2 instanceof TLRPC.TL_chatInviteExported) {
            this.invite = var2;
         } else {
            this.generateLink(false);
         }
      }

   }
}
