package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PrivacyUsersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ContactsActivity.ContactsActivityDelegate {
   private int blockUserDetailRow;
   private int blockUserRow;
   private boolean blockedUsersActivity;
   private PrivacyUsersActivity.PrivacyActivityDelegate delegate;
   private EmptyTextProgressView emptyView;
   private boolean isAlwaysShare;
   private boolean isGroup;
   private RecyclerListView listView;
   private PrivacyUsersActivity.ListAdapter listViewAdapter;
   private int rowCount;
   private ArrayList uidArray;
   private int usersDetailRow;
   private int usersEndRow;
   private int usersHeaderRow;
   private int usersStartRow;

   public PrivacyUsersActivity() {
      this.blockedUsersActivity = true;
   }

   public PrivacyUsersActivity(ArrayList var1, boolean var2, boolean var3) {
      this.uidArray = var1;
      this.isAlwaysShare = var3;
      this.isGroup = var2;
      this.blockedUsersActivity = false;
   }

   private void showUnblockAlert(int var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         CharSequence[] var3;
         if (this.blockedUsersActivity) {
            var3 = new CharSequence[]{LocaleController.getString("Unblock", 2131560932)};
         } else {
            var3 = new CharSequence[]{LocaleController.getString("Delete", 2131559227)};
         }

         var2.setItems(var3, new _$$Lambda$PrivacyUsersActivity$8Pgo4HS6PGjX_d7DerbhGOGCfXc(this, var1));
         this.showDialog(var2.create());
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      if (!this.blockedUsersActivity || !this.getMessagesController().loadingBlockedUsers) {
         int var1 = this.rowCount++;
         this.blockUserRow = var1;
         var1 = this.rowCount++;
         this.blockUserDetailRow = var1;
         if (this.blockedUsersActivity) {
            var1 = this.getMessagesController().blockedUsers.size();
         } else {
            var1 = this.uidArray.size();
         }

         if (var1 != 0) {
            int var2 = this.rowCount++;
            this.usersHeaderRow = var2;
            var2 = this.rowCount;
            this.usersStartRow = var2;
            this.rowCount = var2 + var1;
            var1 = this.rowCount;
            this.usersEndRow = var1;
            this.rowCount = var1 + 1;
            this.usersDetailRow = var1;
         } else {
            this.usersHeaderRow = -1;
            this.usersStartRow = -1;
            this.usersEndRow = -1;
            this.usersDetailRow = -1;
         }
      }

      PrivacyUsersActivity.ListAdapter var3 = this.listViewAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

   }

   private void updateVisibleRows(int var1) {
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = this.listView.getChildAt(var4);
            if (var5 instanceof ManageChatUserCell) {
               ((ManageChatUserCell)var5).update(var1);
            }
         }

      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      if (this.blockedUsersActivity) {
         super.actionBar.setTitle(LocaleController.getString("BlockedUsers", 2131558835));
      } else if (this.isGroup) {
         if (this.isAlwaysShare) {
            super.actionBar.setTitle(LocaleController.getString("AlwaysAllow", 2131558611));
         } else {
            super.actionBar.setTitle(LocaleController.getString("NeverAllow", 2131559894));
         }
      } else if (this.isAlwaysShare) {
         super.actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", 2131558613));
      } else {
         super.actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", 2131559896));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PrivacyUsersActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var7 = (FrameLayout)super.fragmentView;
      var7.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.emptyView = new EmptyTextProgressView(var1);
      if (this.blockedUsersActivity) {
         this.emptyView.setText(LocaleController.getString("NoBlocked", 2131559913));
      } else {
         this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
      }

      var7.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var4 = this.listView;
      PrivacyUsersActivity.ListAdapter var5 = new PrivacyUsersActivity.ListAdapter(var1);
      this.listViewAdapter = var5;
      var4.setAdapter(var5);
      RecyclerListView var6 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var6.setVerticalScrollbarPosition(var3);
      var7.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PrivacyUsersActivity$D3bLTU7NAbbHWcoiH45oxyckkb4(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$PrivacyUsersActivity$r_SA004H7_gb5NSQvdo4AkX5yCs(this)));
      if (this.getMessagesController().loadingBlockedUsers) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      this.updateRows();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.updateInterfaces) {
         var1 = (Integer)var3[0];
         if ((var1 & 2) != 0 || (var1 & 1) != 0) {
            this.updateVisibleRows(var1);
         }
      } else if (var1 == NotificationCenter.blockedUsersDidLoad) {
         this.emptyView.showTextView();
         this.updateRows();
      }

   }

   public void didSelectContact(TLRPC.User var1, String var2, ContactsActivity var3) {
      if (var1 != null) {
         this.getMessagesController().blockUser(var1.id);
      }
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw var1 = new _$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw(this);
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ManageChatUserCell.class, ManageChatTextCell.class, HeaderCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      ThemeDescription var10 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var11 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var13 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var16 = this.listView;
      Drawable var17 = Theme.avatar_broadcastDrawable;
      Drawable var18 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, new ThemeDescription(var16, 0, new Class[]{ManageChatUserCell.class}, (Paint)null, new Drawable[]{var17, var18}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueButton"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$PrivacyUsersActivity(View var1, int var2) {
      Bundle var3;
      Bundle var4;
      if (var2 == this.blockUserRow) {
         if (this.blockedUsersActivity) {
            var4 = new Bundle();
            var4.putBoolean("onlyUsers", true);
            var4.putBoolean("destroyAfterSelect", true);
            var4.putBoolean("returnAsResult", true);
            ContactsActivity var5 = new ContactsActivity(var4);
            var5.setDelegate(this);
            this.presentFragment(var5);
         } else {
            var3 = new Bundle();
            String var6;
            if (this.isAlwaysShare) {
               var6 = "isAlwaysShare";
            } else {
               var6 = "isNeverShare";
            }

            var3.putBoolean(var6, true);
            var3.putBoolean("isGroup", this.isGroup);
            GroupCreateActivity var7 = new GroupCreateActivity(var3);
            var7.setDelegate((GroupCreateActivity.GroupCreateActivityDelegate)(new _$$Lambda$PrivacyUsersActivity$SVOOA5DnLR2m99VL4tIuB7GhsBs(this)));
            this.presentFragment(var7);
         }
      } else if (var2 >= this.usersStartRow && var2 < this.usersEndRow) {
         if (this.blockedUsersActivity) {
            var4 = new Bundle();
            int var10003 = var2 - this.usersStartRow;
            var4.putInt("user_id", this.getMessagesController().blockedUsers.keyAt(var10003));
            this.presentFragment(new ProfileActivity(var4));
         } else {
            var3 = new Bundle();
            Integer var8 = (Integer)this.uidArray.get(var2 - this.usersStartRow);
            if (var8 > 0) {
               var3.putInt("user_id", var8);
            } else {
               var3.putInt("chat_id", -var8);
            }

            this.presentFragment(new ProfileActivity(var3));
         }
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$2$PrivacyUsersActivity(View var1, int var2) {
      int var3 = this.usersStartRow;
      if (var2 >= var3 && var2 < this.usersEndRow) {
         if (this.blockedUsersActivity) {
            int var10002 = var2 - this.usersStartRow;
            this.showUnblockAlert(this.getMessagesController().blockedUsers.keyAt(var10002));
         } else {
            this.showUnblockAlert((Integer)this.uidArray.get(var2 - var3));
         }

         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$4$PrivacyUsersActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof ManageChatUserCell) {
               ((ManageChatUserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$PrivacyUsersActivity(ArrayList var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Integer var2 = (Integer)var3.next();
         if (!this.uidArray.contains(var2)) {
            this.uidArray.add(var2);
         }
      }

      this.updateRows();
      PrivacyUsersActivity.PrivacyActivityDelegate var4 = this.delegate;
      if (var4 != null) {
         var4.didUpdateUserList(this.uidArray, true);
      }

   }

   // $FF: synthetic method
   public void lambda$showUnblockAlert$3$PrivacyUsersActivity(int var1, DialogInterface var2, int var3) {
      if (var3 == 0) {
         if (this.blockedUsersActivity) {
            this.getMessagesController().unblockUser(var1);
         } else {
            this.uidArray.remove(var1);
            this.updateRows();
            PrivacyUsersActivity.PrivacyActivityDelegate var4 = this.delegate;
            if (var4 != null) {
               var4.didUpdateUserList(this.uidArray, false);
            }

            if (this.uidArray.isEmpty()) {
               this.finishFragment();
            }
         }
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      if (this.blockedUsersActivity) {
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoad);
         this.getMessagesController().getBlockedUsers(false);
      }

      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      if (this.blockedUsersActivity) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoad);
      }

   }

   public void onResume() {
      super.onResume();
      PrivacyUsersActivity.ListAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public void setDelegate(PrivacyUsersActivity.PrivacyActivityDelegate var1) {
      this.delegate = var1;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return PrivacyUsersActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == PrivacyUsersActivity.this.usersHeaderRow) {
            return 3;
         } else if (var1 == PrivacyUsersActivity.this.blockUserRow) {
            return 2;
         } else {
            return var1 != PrivacyUsersActivity.this.blockUserDetailRow && var1 != PrivacyUsersActivity.this.usersDetailRow ? 0 : 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3;
         if (var2 != 0 && var2 != 2) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      // $FF: synthetic method
      public boolean lambda$onCreateViewHolder$0$PrivacyUsersActivity$ListAdapter(ManageChatUserCell var1, boolean var2) {
         if (var2) {
            PrivacyUsersActivity.this.showUnblockAlert((Integer)var1.getTag());
         }

         return true;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     HeaderCell var9 = (HeaderCell)var1.itemView;
                     if (var2 == PrivacyUsersActivity.this.usersHeaderRow) {
                        if (PrivacyUsersActivity.this.blockedUsersActivity) {
                           var9.setText(LocaleController.formatPluralString("BlockedUsersCount", PrivacyUsersActivity.this.getMessagesController().blockedUsers.size()));
                        } else {
                           var9.setText(LocaleController.getString("PrivacyExceptions", 2131560482));
                        }
                     }
                  }
               } else {
                  ManageChatTextCell var10 = (ManageChatTextCell)var1.itemView;
                  var10.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                  if (PrivacyUsersActivity.this.blockedUsersActivity) {
                     var10.setText(LocaleController.getString("BlockUser", 2131558834), (String)null, 2131165272, false);
                  } else {
                     var10.setText(LocaleController.getString("PrivacyAddAnException", 2131560474), (String)null, 2131165272, false);
                  }
               }
            } else {
               TextInfoPrivacyCell var11 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == PrivacyUsersActivity.this.blockUserDetailRow) {
                  if (PrivacyUsersActivity.this.blockedUsersActivity) {
                     var11.setText(LocaleController.getString("BlockedUsersInfo", 2131558842));
                  } else {
                     var11.setText((CharSequence)null);
                  }

                  if (PrivacyUsersActivity.this.usersStartRow == -1) {
                     var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  } else {
                     var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                  }
               } else if (var2 == PrivacyUsersActivity.this.usersDetailRow) {
                  var11.setText("");
                  var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            ManageChatUserCell var6 = (ManageChatUserCell)var1.itemView;
            if (PrivacyUsersActivity.this.blockedUsersActivity) {
               var3 = PrivacyUsersActivity.this.getMessagesController().blockedUsers.keyAt(var2 - PrivacyUsersActivity.this.usersStartRow);
            } else {
               var3 = (Integer)PrivacyUsersActivity.this.uidArray.get(var2 - PrivacyUsersActivity.this.usersStartRow);
            }

            var6.setTag(var3);
            String var12;
            if (var3 > 0) {
               TLRPC.User var7 = PrivacyUsersActivity.this.getMessagesController().getUser(var3);
               if (var7 != null) {
                  StringBuilder var13;
                  if (var7.bot) {
                     var13 = new StringBuilder();
                     var13.append(LocaleController.getString("Bot", 2131558848).substring(0, 1).toUpperCase());
                     var13.append(LocaleController.getString("Bot", 2131558848).substring(1));
                     var12 = var13.toString();
                  } else {
                     var12 = var7.phone;
                     if (var12 != null && var12.length() != 0) {
                        PhoneFormat var8 = PhoneFormat.getInstance();
                        var13 = new StringBuilder();
                        var13.append("+");
                        var13.append(var7.phone);
                        var12 = var8.format(var13.toString());
                     } else {
                        var12 = LocaleController.getString("NumberUnknown", 2131560096);
                     }
                  }

                  if (var2 != PrivacyUsersActivity.this.usersEndRow - 1) {
                     var5 = true;
                  }

                  var6.setData(var7, (CharSequence)null, var12, var5);
               }
            } else {
               TLRPC.Chat var14 = PrivacyUsersActivity.this.getMessagesController().getChat(-var3);
               if (var14 != null) {
                  var3 = var14.participants_count;
                  if (var3 != 0) {
                     var12 = LocaleController.formatPluralString("Members", var3);
                  } else if (TextUtils.isEmpty(var14.username)) {
                     var12 = LocaleController.getString("MegaPrivate", 2131559831);
                  } else {
                     var12 = LocaleController.getString("MegaPublic", 2131559834);
                  }

                  var5 = var4;
                  if (var2 != PrivacyUsersActivity.this.usersEndRow - 1) {
                     var5 = true;
                  }

                  var6.setData(var14, (CharSequence)null, var12, var5);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = new HeaderCell(this.mContext, false, 21, 11, false);
                  ((FrameLayout)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  ((HeaderCell)var3).setHeight(43);
               } else {
                  var3 = new ManageChatTextCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
            }
         } else {
            var3 = new ManageChatUserCell(this.mContext, 7, 6, true);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((ManageChatUserCell)var3).setDelegate(new _$$Lambda$PrivacyUsersActivity$ListAdapter$ah_jQyMOHlRewlEcZgEQccTwPTg(this));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public interface PrivacyActivityDelegate {
      void didUpdateUserList(ArrayList var1, boolean var2);
   }
}
