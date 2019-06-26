package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class GroupInviteActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int chat_id;
   private int copyLinkRow;
   private EmptyTextProgressView emptyView;
   private TLRPC.ExportedChatInvite invite;
   private int linkInfoRow;
   private int linkRow;
   private GroupInviteActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loading;
   private int revokeLinkRow;
   private int rowCount;
   private int shadowRow;
   private int shareLinkRow;

   public GroupInviteActivity(int var1) {
      this.chat_id = var1;
   }

   // $FF: synthetic method
   static int access$900(GroupInviteActivity var0) {
      return var0.currentAccount;
   }

   private void generateLink(boolean var1) {
      this.loading = true;
      TLRPC.TL_messages_exportChatInvite var2 = new TLRPC.TL_messages_exportChatInvite();
      var2.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chat_id);
      int var3 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$GroupInviteActivity$GRgS5oE61g396ll3j_eBPQt5fnk(this, var1));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var3, super.classGuid);
      GroupInviteActivity.ListAdapter var4 = this.listAdapter;
      if (var4 != null) {
         var4.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("InviteLink", 2131559679));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               GroupInviteActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new GroupInviteActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showProgress();
      var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView = new RecyclerListView(var1);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setEmptyView(this.emptyView);
      this.listView.setVerticalScrollBarEnabled(false);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$GroupInviteActivity$0RYe3qGmyjz46oz7qfkeGrq_SgU(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatInfoDidLoad) {
         TLRPC.ChatFull var4 = (TLRPC.ChatFull)var3[0];
         var1 = (Integer)var3[1];
         if (var4.id == this.chat_id && var1 == super.classGuid) {
            this.invite = MessagesController.getInstance(super.currentAccount).getExportedInvite(this.chat_id);
            if (!(this.invite instanceof TLRPC.TL_chatInviteExported)) {
               this.generateLink(false);
            } else {
               this.loading = false;
               GroupInviteActivity.ListAdapter var5 = this.listAdapter;
               if (var5 != null) {
                  var5.notifyDataSetChanged();
               }
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextBlockCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$GroupInviteActivity(View var1, int var2) {
      if (this.getParentActivity() != null) {
         if (var2 != this.copyLinkRow && var2 != this.linkRow) {
            if (var2 == this.shareLinkRow) {
               if (this.invite == null) {
                  return;
               }

               try {
                  Intent var5 = new Intent("android.intent.action.SEND");
                  var5.setType("text/plain");
                  var5.putExtra("android.intent.extra.TEXT", this.invite.link);
                  this.getParentActivity().startActivityForResult(Intent.createChooser(var5, LocaleController.getString("InviteToGroupByLink", 2131559688)), 500);
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            } else if (var2 == this.revokeLinkRow) {
               AlertDialog.Builder var6 = new AlertDialog.Builder(this.getParentActivity());
               var6.setMessage(LocaleController.getString("RevokeAlert", 2131560617));
               var6.setTitle(LocaleController.getString("RevokeLink", 2131560620));
               var6.setPositiveButton(LocaleController.getString("RevokeButton", 2131560619), new _$$Lambda$GroupInviteActivity$6ivlxIl3wzffUm1tdYZ92_Ta_UA(this));
               var6.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var6.create());
            }
         } else {
            if (this.invite == null) {
               return;
            }

            try {
               ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
               Toast.makeText(this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$generateLink$3$GroupInviteActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$GroupInviteActivity$Kxy8dT4YrMqik2lxA2i8M0Px_2A(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$0$GroupInviteActivity(DialogInterface var1, int var2) {
      this.generateLink(true);
   }

   // $FF: synthetic method
   public void lambda$null$2$GroupInviteActivity(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      if (var1 == null) {
         this.invite = (TLRPC.ExportedChatInvite)var2;
         if (var3) {
            if (this.getParentActivity() == null) {
               return;
            }

            AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
            var4.setMessage(LocaleController.getString("RevokeAlertNewLink", 2131560618));
            var4.setTitle(LocaleController.getString("RevokeLink", 2131560620));
            var4.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            this.showDialog(var4.create());
         }
      }

      this.loading = false;
      this.listAdapter.notifyDataSetChanged();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
      MessagesController.getInstance(super.currentAccount).loadFullChat(this.chat_id, super.classGuid, true);
      this.loading = true;
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.linkRow = var1;
      var1 = this.rowCount++;
      this.linkInfoRow = var1;
      var1 = this.rowCount++;
      this.copyLinkRow = var1;
      var1 = this.rowCount++;
      this.revokeLinkRow = var1;
      var1 = this.rowCount++;
      this.shareLinkRow = var1;
      var1 = this.rowCount++;
      this.shadowRow = var1;
      return true;
   }

   public void onFragmentDestroy() {
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
   }

   public void onResume() {
      super.onResume();
      GroupInviteActivity.ListAdapter var1 = this.listAdapter;
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
         if (GroupInviteActivity.this.loading) {
            var1 = 0;
         } else {
            var1 = GroupInviteActivity.this.rowCount;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         if (var1 != GroupInviteActivity.this.copyLinkRow && var1 != GroupInviteActivity.this.shareLinkRow && var1 != GroupInviteActivity.this.revokeLinkRow) {
            if (var1 != GroupInviteActivity.this.shadowRow && var1 != GroupInviteActivity.this.linkInfoRow) {
               return var1 == GroupInviteActivity.this.linkRow ? 2 : 0;
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
         if (var2 != GroupInviteActivity.this.revokeLinkRow && var2 != GroupInviteActivity.this.copyLinkRow && var2 != GroupInviteActivity.this.shareLinkRow && var2 != GroupInviteActivity.this.linkRow) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  TextBlockCell var4 = (TextBlockCell)var1.itemView;
                  String var5;
                  if (GroupInviteActivity.this.invite != null) {
                     var5 = GroupInviteActivity.this.invite.link;
                  } else {
                     var5 = "error";
                  }

                  var4.setText(var5, false);
               }
            } else {
               TextInfoPrivacyCell var8 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == GroupInviteActivity.this.shadowRow) {
                  var8.setText("");
                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               } else if (var2 == GroupInviteActivity.this.linkInfoRow) {
                  TLRPC.Chat var6 = MessagesController.getInstance(GroupInviteActivity.access$900(GroupInviteActivity.this)).getChat(GroupInviteActivity.this.chat_id);
                  if (ChatObject.isChannel(var6) && !var6.megagroup) {
                     var8.setText(LocaleController.getString("ChannelLinkInfo", 2131558959));
                  } else {
                     var8.setText(LocaleController.getString("LinkInfo", 2131559754));
                  }

                  var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            TextSettingsCell var7 = (TextSettingsCell)var1.itemView;
            if (var2 == GroupInviteActivity.this.copyLinkRow) {
               var7.setText(LocaleController.getString("CopyLink", 2131559164), true);
            } else if (var2 == GroupInviteActivity.this.shareLinkRow) {
               var7.setText(LocaleController.getString("ShareLink", 2131560749), false);
            } else if (var2 == GroupInviteActivity.this.revokeLinkRow) {
               var7.setText(LocaleController.getString("RevokeLink", 2131560620), true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new TextBlockCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
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
