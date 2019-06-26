package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.SessionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class SessionsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private TLRPC.TL_authorization currentSession;
   private int currentSessionRow;
   private int currentSessionSectionRow;
   private int currentType;
   private LinearLayout emptyLayout;
   private EmptyTextProgressView emptyView;
   private ImageView imageView;
   private SessionsActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loading;
   private int noOtherSessionsRow;
   private int otherSessionsEndRow;
   private int otherSessionsSectionRow;
   private int otherSessionsStartRow;
   private int otherSessionsTerminateDetail;
   private ArrayList passwordSessions = new ArrayList();
   private int passwordSessionsDetailRow;
   private int passwordSessionsEndRow;
   private int passwordSessionsSectionRow;
   private int passwordSessionsStartRow;
   private int rowCount;
   private ArrayList sessions = new ArrayList();
   private int terminateAllSessionsDetailRow;
   private int terminateAllSessionsRow;
   private TextView textView1;
   private TextView textView2;

   public SessionsActivity(int var1) {
      this.currentType = var1;
   }

   // $FF: synthetic method
   static void lambda$null$5(boolean[] var0, View var1) {
      if (var1.isEnabled()) {
         CheckBoxCell var2 = (CheckBoxCell)var1;
         var0[0] ^= true;
         var2.setChecked(var0[0], true);
      }
   }

   private void loadSessions(boolean var1) {
      if (!this.loading) {
         if (!var1) {
            this.loading = true;
         }

         int var3;
         if (this.currentType == 0) {
            TLRPC.TL_account_getAuthorizations var2 = new TLRPC.TL_account_getAuthorizations();
            var3 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$SessionsActivity$89ZRjxSgVCBTgysCrEBIMz4rP7Q(this));
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var3, super.classGuid);
         } else {
            TLRPC.TL_account_getWebAuthorizations var4 = new TLRPC.TL_account_getWebAuthorizations();
            var3 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4(this));
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var3, super.classGuid);
         }

      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1;
      if (this.currentSession != null) {
         var1 = this.rowCount++;
         this.currentSessionSectionRow = var1;
         var1 = this.rowCount++;
         this.currentSessionRow = var1;
      } else {
         this.currentSessionRow = -1;
         this.currentSessionSectionRow = -1;
      }

      if (this.passwordSessions.isEmpty() && this.sessions.isEmpty()) {
         this.terminateAllSessionsRow = -1;
         this.terminateAllSessionsDetailRow = -1;
         if (this.currentType != 1 && this.currentSession == null) {
            this.noOtherSessionsRow = -1;
         } else {
            var1 = this.rowCount++;
            this.noOtherSessionsRow = var1;
         }
      } else {
         var1 = this.rowCount++;
         this.terminateAllSessionsRow = var1;
         var1 = this.rowCount++;
         this.terminateAllSessionsDetailRow = var1;
         this.noOtherSessionsRow = -1;
      }

      if (this.passwordSessions.isEmpty()) {
         this.passwordSessionsDetailRow = -1;
         this.passwordSessionsEndRow = -1;
         this.passwordSessionsStartRow = -1;
         this.passwordSessionsSectionRow = -1;
      } else {
         var1 = this.rowCount++;
         this.passwordSessionsSectionRow = var1;
         var1 = this.rowCount;
         this.passwordSessionsStartRow = var1;
         this.rowCount = var1 + this.passwordSessions.size();
         var1 = this.rowCount;
         this.passwordSessionsEndRow = var1;
         this.rowCount = var1 + 1;
         this.passwordSessionsDetailRow = var1;
      }

      if (this.sessions.isEmpty()) {
         this.otherSessionsSectionRow = -1;
         this.otherSessionsStartRow = -1;
         this.otherSessionsEndRow = -1;
         this.otherSessionsTerminateDetail = -1;
      } else {
         var1 = this.rowCount++;
         this.otherSessionsSectionRow = var1;
         this.otherSessionsStartRow = this.otherSessionsSectionRow + 1;
         this.otherSessionsEndRow = this.otherSessionsStartRow + this.sessions.size();
         this.rowCount += this.sessions.size();
         var1 = this.rowCount++;
         this.otherSessionsTerminateDetail = var1;
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.currentType == 0) {
         super.actionBar.setTitle(LocaleController.getString("SessionsTitle", 2131560726));
      } else {
         super.actionBar.setTitle(LocaleController.getString("WebSessionsTitle", 2131561104));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               SessionsActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new SessionsActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.emptyLayout = new LinearLayout(var1);
      this.emptyLayout.setOrientation(1);
      this.emptyLayout.setGravity(17);
      this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.emptyLayout.setLayoutParams(new LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()));
      this.imageView = new ImageView(var1);
      if (this.currentType == 0) {
         this.imageView.setImageResource(2131165373);
      } else {
         this.imageView.setImageResource(2131165691);
      }

      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("sessions_devicesImage"), Mode.MULTIPLY));
      this.emptyLayout.addView(this.imageView, LayoutHelper.createLinear(-2, -2));
      this.textView1 = new TextView(var1);
      this.textView1.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.textView1.setGravity(17);
      this.textView1.setTextSize(1, 17.0F);
      this.textView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      if (this.currentType == 0) {
         this.textView1.setText(LocaleController.getString("NoOtherSessions", 2131559933));
      } else {
         this.textView1.setText(LocaleController.getString("NoOtherWebSessions", 2131559935));
      }

      this.emptyLayout.addView(this.textView1, LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
      this.textView2 = new TextView(var1);
      this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.textView2.setGravity(17);
      this.textView2.setTextSize(1, 17.0F);
      this.textView2.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      if (this.currentType == 0) {
         this.textView2.setText(LocaleController.getString("NoOtherSessionsInfo", 2131559934));
      } else {
         this.textView2.setText(LocaleController.getString("NoOtherWebSessionsInfo", 2131559936));
      }

      this.emptyLayout.addView(this.textView2, LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showProgress();
      var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1, 17));
      this.listView = new RecyclerListView(var1);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setEmptyView(this.emptyView);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$SessionsActivity$nGXAGNwZRPb8yKcWdHCTunuNa5s(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.newSessionReceived) {
         this.loadSessions(true);
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, SessionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.imageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sessions_devicesImage"), new ThemeDescription(this.textView1, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.textView2, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SessionCell.class}, new String[]{"onlineTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SessionCell.class}, new String[]{"onlineTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"detailTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"detailExTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3")};
   }

   // $FF: synthetic method
   public void lambda$createView$11$SessionsActivity(View var1, int var2) {
      if (var2 == this.terminateAllSessionsRow) {
         if (this.getParentActivity() == null) {
            return;
         }

         AlertDialog.Builder var9 = new AlertDialog.Builder(this.getParentActivity());
         if (this.currentType == 0) {
            var9.setMessage(LocaleController.getString("AreYouSureSessions", 2131558698));
         } else {
            var9.setMessage(LocaleController.getString("AreYouSureWebSessions", 2131558703));
         }

         var9.setTitle(LocaleController.getString("AppName", 2131558635));
         var9.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$SessionsActivity$oaaWYlgt897yPsbKGRVtpZZB0Zw(this));
         var9.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var9.create());
      } else if (var2 >= this.otherSessionsStartRow && var2 < this.otherSessionsEndRow || var2 >= this.passwordSessionsStartRow && var2 < this.passwordSessionsEndRow) {
         if (this.getParentActivity() == null) {
            return;
         }

         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         boolean[] var4 = new boolean[1];
         if (this.currentType == 0) {
            var3.setMessage(LocaleController.getString("TerminateSessionQuestion", 2131560881));
         } else {
            TLRPC.TL_webAuthorization var10 = (TLRPC.TL_webAuthorization)this.sessions.get(var2 - this.otherSessionsStartRow);
            var3.setMessage(LocaleController.formatString("TerminateWebSessionQuestion", 2131560883, var10.domain));
            FrameLayout var5 = new FrameLayout(this.getParentActivity());
            TLRPC.User var11 = MessagesController.getInstance(super.currentAccount).getUser(var10.bot_id);
            String var12;
            if (var11 != null) {
               var12 = UserObject.getFirstName(var11);
            } else {
               var12 = "";
            }

            CheckBoxCell var6 = new CheckBoxCell(this.getParentActivity(), 1);
            var6.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            var6.setText(LocaleController.formatString("TerminateWebSessionStop", 2131560884, var12), "", false, false);
            int var7;
            if (LocaleController.isRTL) {
               var7 = AndroidUtilities.dp(16.0F);
            } else {
               var7 = AndroidUtilities.dp(8.0F);
            }

            int var8;
            if (LocaleController.isRTL) {
               var8 = AndroidUtilities.dp(8.0F);
            } else {
               var8 = AndroidUtilities.dp(16.0F);
            }

            var6.setPadding(var7, 0, var8, 0);
            var5.addView(var6, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
            var6.setOnClickListener(new _$$Lambda$SessionsActivity$fTHr4kOeCS_36_P5G_qrJ_UGaNE(var4));
            var3.setCustomViewOffset(16);
            var3.setView(var5);
         }

         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$SessionsActivity$Rp5LHvaMpc39OI5FKrDiSKjtcrQ(this, var2, var4));
         var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var3.create());
      }

   }

   // $FF: synthetic method
   public void lambda$loadSessions$13$SessionsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$Zt74L2MQ_s9swKF0srb1gpKxots(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadSessions$15$SessionsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$VzziGibbmz1VChBvlPo1LDLCXbs(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$0$SessionsActivity(TLRPC.TL_error var1, TLObject var2) {
      if (this.getParentActivity() != null) {
         if (var1 == null && var2 instanceof TLRPC.TL_boolTrue) {
            Toast.makeText(this.getParentActivity(), LocaleController.getString("TerminateAllSessions", 2131560878), 0).show();
            this.finishFragment();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$1$SessionsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$DgWzs1LwXUgkxX8mgzP9nfgTbVA(this, var2, var1));

      for(int var3 = 0; var3 < 3; ++var3) {
         UserConfig var4 = UserConfig.getInstance(var3);
         if (var4.isClientActivated()) {
            var4.registeredForPush = false;
            var4.saveConfig(false);
            MessagesController.getInstance(var3).registerForPush(SharedConfig.pushString);
            ConnectionsManager.getInstance(var3).setUserId(var4.getClientUserId());
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$10$SessionsActivity(int var1, boolean[] var2, DialogInterface var3, int var4) {
      if (this.getParentActivity() != null) {
         AlertDialog var8 = new AlertDialog(this.getParentActivity(), 3);
         var8.setCanCacnel(false);
         var8.show();
         if (this.currentType == 0) {
            var4 = this.otherSessionsStartRow;
            TLRPC.TL_authorization var7;
            if (var1 >= var4 && var1 < this.otherSessionsEndRow) {
               var7 = (TLRPC.TL_authorization)this.sessions.get(var1 - var4);
            } else {
               var7 = (TLRPC.TL_authorization)this.passwordSessions.get(var1 - this.passwordSessionsStartRow);
            }

            TLRPC.TL_account_resetAuthorization var5 = new TLRPC.TL_account_resetAuthorization();
            var5.hash = var7.hash;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$SessionsActivity$uCzzVW_FF8MV8J_oXGgpzlkkOI4(this, var8, var7));
         } else {
            TLRPC.TL_webAuthorization var9 = (TLRPC.TL_webAuthorization)this.sessions.get(var1 - this.otherSessionsStartRow);
            TLRPC.TL_account_resetWebAuthorization var6 = new TLRPC.TL_account_resetWebAuthorization();
            var6.hash = var9.hash;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var6, new _$$Lambda$SessionsActivity$F3jY3_Lk4fRyFUz5zR6es4a8E6M(this, var8, var9));
            if (var2[0]) {
               MessagesController.getInstance(super.currentAccount).blockUser(var9.bot_id);
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$12$SessionsActivity(TLRPC.TL_error var1, TLObject var2) {
      int var3 = 0;
      this.loading = false;
      if (var1 == null) {
         this.sessions.clear();
         this.passwordSessions.clear();
         TLRPC.TL_account_authorizations var7 = (TLRPC.TL_account_authorizations)var2;

         for(int var4 = var7.authorizations.size(); var3 < var4; ++var3) {
            TLRPC.TL_authorization var5 = (TLRPC.TL_authorization)var7.authorizations.get(var3);
            if ((var5.flags & 1) != 0) {
               this.currentSession = var5;
            } else if (var5.password_pending) {
               this.passwordSessions.add(var5);
            } else {
               this.sessions.add(var5);
            }
         }

         this.updateRows();
      }

      SessionsActivity.ListAdapter var6 = this.listAdapter;
      if (var6 != null) {
         var6.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$14$SessionsActivity(TLRPC.TL_error var1, TLObject var2) {
      this.loading = false;
      if (var1 == null) {
         this.sessions.clear();
         TLRPC.TL_account_webAuthorizations var3 = (TLRPC.TL_account_webAuthorizations)var2;
         MessagesController.getInstance(super.currentAccount).putUsers(var3.users, false);
         this.sessions.addAll(var3.authorizations);
         this.updateRows();
      }

      SessionsActivity.ListAdapter var4 = this.listAdapter;
      if (var4 != null) {
         var4.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$SessionsActivity(TLRPC.TL_error var1, TLObject var2) {
      if (this.getParentActivity() != null) {
         if (var1 == null && var2 instanceof TLRPC.TL_boolTrue) {
            Toast.makeText(this.getParentActivity(), LocaleController.getString("TerminateAllWebSessions", 2131560879), 0).show();
         } else {
            Toast.makeText(this.getParentActivity(), LocaleController.getString("UnknownError", 2131560937), 0).show();
         }

         this.finishFragment();
      }
   }

   // $FF: synthetic method
   public void lambda$null$3$SessionsActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$Az0uYHbPXgbOODawLpn12NZhF5M(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$4$SessionsActivity(DialogInterface var1, int var2) {
      if (this.currentType == 0) {
         TLRPC.TL_auth_resetAuthorizations var3 = new TLRPC.TL_auth_resetAuthorizations();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$SessionsActivity$dxKKwoZ08ejJ_JgokkvdiYJTtHg(this));
      } else {
         TLRPC.TL_account_resetWebAuthorizations var4 = new TLRPC.TL_account_resetWebAuthorizations();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$SessionsActivity$msYb4DTCxuG4mkTvoMcr_IVJivE(this));
      }

   }

   // $FF: synthetic method
   public void lambda$null$6$SessionsActivity(AlertDialog var1, TLRPC.TL_error var2, TLRPC.TL_authorization var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (var2 == null) {
         this.sessions.remove(var3);
         this.passwordSessions.remove(var3);
         this.updateRows();
         SessionsActivity.ListAdapter var5 = this.listAdapter;
         if (var5 != null) {
            var5.notifyDataSetChanged();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$SessionsActivity(AlertDialog var1, TLRPC.TL_authorization var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ(this, var1, var4, var2));
   }

   // $FF: synthetic method
   public void lambda$null$8$SessionsActivity(AlertDialog var1, TLRPC.TL_error var2, TLRPC.TL_webAuthorization var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (var2 == null) {
         this.sessions.remove(var3);
         this.updateRows();
         SessionsActivity.ListAdapter var5 = this.listAdapter;
         if (var5 != null) {
            var5.notifyDataSetChanged();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$SessionsActivity(AlertDialog var1, TLRPC.TL_webAuthorization var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SessionsActivity$sjBDbKRuMyWfdQovBWSND8F1Gd4(this, var1, var4, var2));
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.updateRows();
      this.loadSessions(false);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.newSessionReceived);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.newSessionReceived);
   }

   public void onResume() {
      super.onResume();
      SessionsActivity.ListAdapter var1 = this.listAdapter;
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
         int var1;
         if (SessionsActivity.this.loading) {
            var1 = 0;
         } else {
            var1 = SessionsActivity.this.rowCount;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         if (var1 == SessionsActivity.this.terminateAllSessionsRow) {
            return 0;
         } else if (var1 != SessionsActivity.this.terminateAllSessionsDetailRow && var1 != SessionsActivity.this.otherSessionsTerminateDetail && var1 != SessionsActivity.this.passwordSessionsDetailRow) {
            if (var1 != SessionsActivity.this.currentSessionSectionRow && var1 != SessionsActivity.this.otherSessionsSectionRow && var1 != SessionsActivity.this.passwordSessionsSectionRow) {
               if (var1 == SessionsActivity.this.noOtherSessionsRow) {
                  return 3;
               } else {
                  return var1 == SessionsActivity.this.currentSessionRow || var1 >= SessionsActivity.this.otherSessionsStartRow && var1 < SessionsActivity.this.otherSessionsEndRow || var1 >= SessionsActivity.this.passwordSessionsStartRow && var1 < SessionsActivity.this.passwordSessionsEndRow ? 4 : 0;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 == SessionsActivity.this.terminateAllSessionsRow || var2 >= SessionsActivity.this.otherSessionsStartRow && var2 < SessionsActivity.this.otherSessionsEndRow || var2 >= SessionsActivity.this.passwordSessionsStartRow && var2 < SessionsActivity.this.passwordSessionsEndRow) {
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
         byte var6 = 0;
         boolean var7 = false;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     SessionCell var12 = (SessionCell)var1.itemView;
                     if (var2 == SessionsActivity.this.currentSessionRow) {
                        TLRPC.TL_authorization var8 = SessionsActivity.this.currentSession;
                        if (!SessionsActivity.this.sessions.isEmpty() || !SessionsActivity.this.passwordSessions.isEmpty()) {
                           var7 = true;
                        }

                        var12.setSession(var8, var7);
                     } else {
                        TLObject var17;
                        if (var2 >= SessionsActivity.this.otherSessionsStartRow && var2 < SessionsActivity.this.otherSessionsEndRow) {
                           var17 = (TLObject)SessionsActivity.this.sessions.get(var2 - SessionsActivity.this.otherSessionsStartRow);
                           var7 = var4;
                           if (var2 != SessionsActivity.this.otherSessionsEndRow - 1) {
                              var7 = true;
                           }

                           var12.setSession(var17, var7);
                        } else if (var2 >= SessionsActivity.this.passwordSessionsStartRow && var2 < SessionsActivity.this.passwordSessionsEndRow) {
                           var17 = (TLObject)SessionsActivity.this.passwordSessions.get(var2 - SessionsActivity.this.passwordSessionsStartRow);
                           var7 = var5;
                           if (var2 != SessionsActivity.this.passwordSessionsEndRow - 1) {
                              var7 = true;
                           }

                           var12.setSession(var17, var7);
                        }
                     }
                  } else {
                     android.view.ViewGroup.LayoutParams var13 = SessionsActivity.this.emptyLayout.getLayoutParams();
                     if (var13 != null) {
                        var3 = AndroidUtilities.dp(220.0F);
                        int var9 = AndroidUtilities.displaySize.y;
                        int var10 = ActionBar.getCurrentActionBarHeight();
                        int var11 = AndroidUtilities.dp(128.0F);
                        var2 = var6;
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight;
                        }

                        var13.height = Math.max(var3, var9 - var10 - var11 - var2);
                        SessionsActivity.this.emptyLayout.setLayoutParams(var13);
                     }
                  }
               } else {
                  HeaderCell var14 = (HeaderCell)var1.itemView;
                  if (var2 == SessionsActivity.this.currentSessionSectionRow) {
                     var14.setText(LocaleController.getString("CurrentSession", 2131559181));
                  } else if (var2 == SessionsActivity.this.otherSessionsSectionRow) {
                     if (SessionsActivity.this.currentType == 0) {
                        var14.setText(LocaleController.getString("OtherSessions", 2131560131));
                     } else {
                        var14.setText(LocaleController.getString("OtherWebSessions", 2131560132));
                     }
                  } else if (var2 == SessionsActivity.this.passwordSessionsSectionRow) {
                     var14.setText(LocaleController.getString("LoginAttempts", 2131559786));
                  }
               }
            } else {
               TextInfoPrivacyCell var15 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == SessionsActivity.this.terminateAllSessionsDetailRow) {
                  if (SessionsActivity.this.currentType == 0) {
                     var15.setText(LocaleController.getString("ClearOtherSessionsHelp", 2131559111));
                  } else {
                     var15.setText(LocaleController.getString("ClearOtherWebSessionsHelp", 2131559112));
                  }

                  var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == SessionsActivity.this.otherSessionsTerminateDetail) {
                  if (SessionsActivity.this.currentType == 0) {
                     var15.setText(LocaleController.getString("TerminateSessionInfo", 2131560880));
                  } else {
                     var15.setText(LocaleController.getString("TerminateWebSessionInfo", 2131560882));
                  }

                  var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               } else if (var2 == SessionsActivity.this.passwordSessionsDetailRow) {
                  var15.setText(LocaleController.getString("LoginAttemptsInfo", 2131559787));
                  if (SessionsActivity.this.otherSessionsTerminateDetail == -1) {
                     var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  } else {
                     var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                  }
               }
            }
         } else {
            TextSettingsCell var16 = (TextSettingsCell)var1.itemView;
            if (var2 == SessionsActivity.this.terminateAllSessionsRow) {
               var16.setTextColor(Theme.getColor("windowBackgroundWhiteRedText2"));
               if (SessionsActivity.this.currentType == 0) {
                  var16.setText(LocaleController.getString("TerminateAllSessions", 2131560878), false);
               } else {
                  var16.setText(LocaleController.getString("TerminateAllWebSessions", 2131560879), false);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = new SessionCell(this.mContext, SessionsActivity.this.currentType);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  } else {
                     var3 = SessionsActivity.this.emptyLayout;
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
}
