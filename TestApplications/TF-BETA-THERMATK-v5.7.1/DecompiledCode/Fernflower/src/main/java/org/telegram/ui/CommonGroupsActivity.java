package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class CommonGroupsActivity extends BaseFragment {
   private ArrayList chats = new ArrayList();
   private EmptyTextProgressView emptyView;
   private boolean endReached;
   private boolean firstLoaded;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private CommonGroupsActivity.ListAdapter listViewAdapter;
   private boolean loading;
   private int userId;

   public CommonGroupsActivity(int var1) {
      this.userId = var1;
   }

   private void getChats(int var1, int var2) {
      if (!this.loading) {
         this.loading = true;
         EmptyTextProgressView var3 = this.emptyView;
         if (var3 != null && !this.firstLoaded) {
            var3.showProgress();
         }

         CommonGroupsActivity.ListAdapter var4 = this.listViewAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }

         TLRPC.TL_messages_getCommonChats var5 = new TLRPC.TL_messages_getCommonChats();
         var5.user_id = MessagesController.getInstance(super.currentAccount).getInputUser(this.userId);
         if (!(var5.user_id instanceof TLRPC.TL_inputUserEmpty)) {
            var5.limit = var2;
            var5.max_id = var1;
            var1 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$CommonGroupsActivity$uEpDyQSDsMXTcD9ZAD3h6fxSQJA(this, var2));
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var1, super.classGuid);
         }
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("GroupsInCommonTitle", 2131559627));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               CommonGroupsActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var8 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setText(LocaleController.getString("NoGroupsInCommon", 2131559925));
      var8.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var5 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var5;
      var4.setLayoutManager(var5);
      var4 = this.listView;
      CommonGroupsActivity.ListAdapter var6 = new CommonGroupsActivity.ListAdapter(var1);
      this.listViewAdapter = var6;
      var4.setAdapter(var6);
      RecyclerListView var7 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var7.setVerticalScrollbarPosition(var3);
      var8.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$CommonGroupsActivity$J3Yy_YOVYpXyEU8UkIqEdngAT6I(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            var3 = CommonGroupsActivity.this.layoutManager.findFirstVisibleItemPosition();
            if (var3 == -1) {
               var2 = 0;
            } else {
               var2 = Math.abs(CommonGroupsActivity.this.layoutManager.findLastVisibleItemPosition() - var3) + 1;
            }

            if (var2 > 0) {
               int var4 = CommonGroupsActivity.this.listViewAdapter.getItemCount();
               if (!CommonGroupsActivity.this.endReached && !CommonGroupsActivity.this.loading && !CommonGroupsActivity.this.chats.isEmpty() && var3 + var2 >= var4 - 5) {
                  CommonGroupsActivity var5 = CommonGroupsActivity.this;
                  var5.getChats(((TLRPC.Chat)var5.chats.get(CommonGroupsActivity.this.chats.size() - 1)).id, 100);
               }
            }

         }
      });
      if (this.loading) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw var1 = new _$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LoadingCell.class, ProfileSearchCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var21 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var12 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var13 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      ThemeDescription var22 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      RecyclerListView var16 = this.listView;
      TextPaint var17 = Theme.dialogs_namePaint;
      TextPaint var18 = Theme.dialogs_searchNamePaint;
      ThemeDescription var24 = new ThemeDescription(var16, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var17, var18}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_name");
      var16 = this.listView;
      TextPaint var19 = Theme.dialogs_nameEncryptedPaint;
      var18 = Theme.dialogs_searchNameEncryptedPaint;
      ThemeDescription var20 = new ThemeDescription(var16, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var19, var18}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_secretName");
      RecyclerListView var26 = this.listView;
      Drawable var25 = Theme.avatar_broadcastDrawable;
      Drawable var23 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var21, var12, var13, var22, var14, var15, var24, var20, new ThemeDescription(var26, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var25, var23}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$CommonGroupsActivity(View var1, int var2) {
      if (var2 >= 0 && var2 < this.chats.size()) {
         TLRPC.Chat var3 = (TLRPC.Chat)this.chats.get(var2);
         Bundle var4 = new Bundle();
         var4.putInt("chat_id", var3.id);
         if (!MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var4, this)) {
            return;
         }

         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
         this.presentFragment(new ChatActivity(var4), true);
      }

   }

   // $FF: synthetic method
   public void lambda$getChats$2$CommonGroupsActivity(int var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$CommonGroupsActivity$khNmAu1RhPThZuRoaDt1rnPjqQQ(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$3$CommonGroupsActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof ProfileSearchCell) {
               ((ProfileSearchCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$CommonGroupsActivity(TLRPC.TL_error var1, TLObject var2, int var3) {
      if (var1 == null) {
         TLRPC.messages_Chats var5 = (TLRPC.messages_Chats)var2;
         MessagesController.getInstance(super.currentAccount).putChats(var5.chats, false);
         boolean var4;
         if (!var5.chats.isEmpty() && var5.chats.size() == var3) {
            var4 = false;
         } else {
            var4 = true;
         }

         this.endReached = var4;
         this.chats.addAll(var5.chats);
      } else {
         this.endReached = true;
      }

      this.loading = false;
      this.firstLoaded = true;
      EmptyTextProgressView var6 = this.emptyView;
      if (var6 != null) {
         var6.showTextView();
      }

      CommonGroupsActivity.ListAdapter var7 = this.listViewAdapter;
      if (var7 != null) {
         var7.notifyDataSetChanged();
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.getChats(0, 50);
      return true;
   }

   public void onResume() {
      super.onResume();
      CommonGroupsActivity.ListAdapter var1 = this.listViewAdapter;
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
         int var1 = CommonGroupsActivity.this.chats.size();
         int var2 = var1;
         if (!CommonGroupsActivity.this.chats.isEmpty()) {
            ++var1;
            var2 = var1;
            if (!CommonGroupsActivity.this.endReached) {
               var2 = var1 + 1;
            }
         }

         return var2;
      }

      public int getItemViewType(int var1) {
         if (var1 < CommonGroupsActivity.this.chats.size()) {
            return 0;
         } else {
            return !CommonGroupsActivity.this.endReached && var1 == CommonGroupsActivity.this.chats.size() ? 1 : 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getAdapterPosition() != CommonGroupsActivity.this.chats.size()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            ProfileSearchCell var6 = (ProfileSearchCell)var1.itemView;
            var6.setData((TLRPC.Chat)CommonGroupsActivity.this.chats.get(var2), (TLRPC.EncryptedChat)null, (CharSequence)null, (CharSequence)null, false, false);
            int var3 = CommonGroupsActivity.this.chats.size();
            boolean var4 = true;
            boolean var5 = var4;
            if (var2 == var3 - 1) {
               if (!CommonGroupsActivity.this.endReached) {
                  var5 = var4;
               } else {
                  var5 = false;
               }
            }

            var6.useSeparator = var5;
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new TextInfoPrivacyCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            } else {
               var3 = new LoadingCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new ProfileSearchCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
