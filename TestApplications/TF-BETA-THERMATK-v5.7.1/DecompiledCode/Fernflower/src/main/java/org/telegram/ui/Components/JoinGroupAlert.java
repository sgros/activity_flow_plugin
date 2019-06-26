package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.JoinSheetUserCell;

public class JoinGroupAlert extends BottomSheet {
   private TLRPC.ChatInvite chatInvite;
   private BaseFragment fragment;
   private String hash;

   public JoinGroupAlert(Context var1, TLRPC.ChatInvite var2, String var3, BaseFragment var4) {
      super(var1, false, 0);
      this.setApplyBottomPadding(false);
      this.setApplyTopPadding(false);
      this.fragment = var4;
      this.chatInvite = var2;
      this.hash = var3;
      LinearLayout var14 = new LinearLayout(var1);
      var14.setOrientation(1);
      var14.setClickable(true);
      this.setCustomView(var14);
      BackupImageView var5 = new BackupImageView(var1);
      var5.setRoundRadius(AndroidUtilities.dp(35.0F));
      var14.addView(var5, LayoutHelper.createLinear(70, 70, 49, 0, 12, 0, 0));
      TLRPC.Chat var13 = var2.chat;
      int var8;
      if (var13 != null) {
         AvatarDrawable var6 = new AvatarDrawable(var13);
         TLRPC.Chat var7 = var2.chat;
         var3 = var7.title;
         var8 = var7.participants_count;
         var5.setImage((ImageLocation)ImageLocation.getForChat(var7, false), "50_50", (Drawable)var6, (Object)var2);
      } else {
         AvatarDrawable var17 = new AvatarDrawable();
         var17.setInfo(0, var2.title, (String)null, false);
         var3 = var2.title;
         var8 = var2.participants_count;
         var5.setImage((ImageLocation)ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(var2.photo.sizes, 50), var2.photo), "50_50", (Drawable)var17, (Object)var2);
      }

      TextView var16 = new TextView(var1);
      var16.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var16.setTextSize(1, 17.0F);
      var16.setTextColor(Theme.getColor("dialogTextBlack"));
      var16.setText(var3);
      var16.setSingleLine(true);
      var16.setEllipsize(TruncateAt.END);
      byte var9;
      if (var8 > 0) {
         var9 = 0;
      } else {
         var9 = 10;
      }

      var14.addView(var16, LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, var9));
      if (var8 > 0) {
         TextView var15 = new TextView(var1);
         var15.setTextSize(1, 14.0F);
         var15.setTextColor(Theme.getColor("dialogTextGray3"));
         var15.setSingleLine(true);
         var15.setEllipsize(TruncateAt.END);
         var15.setText(LocaleController.formatPluralString("Members", var8));
         var14.addView(var15, LayoutHelper.createLinear(-2, -2, 49, 10, 4, 10, 10));
      }

      if (!var2.participants.isEmpty()) {
         RecyclerListView var11 = new RecyclerListView(var1);
         var11.setPadding(0, 0, 0, AndroidUtilities.dp(8.0F));
         var11.setNestedScrollingEnabled(false);
         var11.setClipToPadding(false);
         var11.setLayoutManager(new LinearLayoutManager(this.getContext(), 0, false));
         var11.setHorizontalScrollBarEnabled(false);
         var11.setVerticalScrollBarEnabled(false);
         var11.setAdapter(new JoinGroupAlert.UsersAdapter(var1));
         var11.setGlowColor(Theme.getColor("dialogScrollGlow"));
         var14.addView(var11, LayoutHelper.createLinear(-2, 90, 49, 0, 0, 0, 0));
      }

      View var12 = new View(var1);
      var12.setBackgroundColor(Theme.getColor("dialogShadowLine"));
      var14.addView(var12, new LayoutParams(-1, AndroidUtilities.getShadowHeight()));
      PickerBottomLayout var10 = new PickerBottomLayout(var1, false);
      var14.addView(var10, LayoutHelper.createFrame(-1, 48, 83));
      var10.cancelButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      var10.cancelButton.setTextColor(Theme.getColor("dialogTextBlue2"));
      var10.cancelButton.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      var10.cancelButton.setOnClickListener(new _$$Lambda$JoinGroupAlert$HWwkTLyyGhrlSGGzuTHLEOoFKaE(this));
      var10.doneButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      var10.doneButton.setVisibility(0);
      var10.doneButtonBadgeTextView.setVisibility(8);
      var10.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
      var10.doneButtonTextView.setText(LocaleController.getString("JoinGroup", 2131559703));
      var10.doneButton.setOnClickListener(new _$$Lambda$JoinGroupAlert$MfRcTjxTXiGmJvnuSHx7GPUtAhw(this));
   }

   // $FF: synthetic method
   public void lambda$new$0$JoinGroupAlert(View var1) {
      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$new$3$JoinGroupAlert(View var1) {
      this.dismiss();
      TLRPC.TL_messages_importChatInvite var2 = new TLRPC.TL_messages_importChatInvite();
      var2.hash = this.hash;
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$JoinGroupAlert$mdXFwxXoZ3zIuXYK2Qsi9b4N8JY(this, var2), 2);
   }

   // $FF: synthetic method
   public void lambda$null$1$JoinGroupAlert(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_messages_importChatInvite var3) {
      BaseFragment var4 = this.fragment;
      if (var4 != null && var4.getParentActivity() != null) {
         if (var1 == null) {
            TLRPC.Updates var7 = (TLRPC.Updates)var2;
            if (!var7.chats.isEmpty()) {
               TLRPC.Chat var5 = (TLRPC.Chat)var7.chats.get(0);
               var5.left = false;
               var5.kicked = false;
               MessagesController.getInstance(super.currentAccount).putUsers(var7.users, false);
               MessagesController.getInstance(super.currentAccount).putChats(var7.chats, false);
               Bundle var8 = new Bundle();
               var8.putInt("chat_id", var5.id);
               if (MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var8, this.fragment)) {
                  ChatActivity var9 = new ChatActivity(var8);
                  BaseFragment var6 = this.fragment;
                  var6.presentFragment(var9, var6 instanceof ChatActivity);
               }
            }
         } else {
            AlertsCreator.processError(super.currentAccount, var1, this.fragment, var3);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$JoinGroupAlert(TLRPC.TL_messages_importChatInvite var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         TLRPC.Updates var4 = (TLRPC.Updates)var2;
         MessagesController.getInstance(super.currentAccount).processUpdates(var4, false);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$JoinGroupAlert$IWx_squ4_I5Qyjo_N06x9CUQNvc(this, var3, var2, var1));
   }

   private class UsersAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;

      public UsersAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         int var1 = JoinGroupAlert.this.chatInvite.participants.size();
         int var2;
         if (JoinGroupAlert.this.chatInvite.chat != null) {
            var2 = JoinGroupAlert.this.chatInvite.chat.participants_count;
         } else {
            var2 = JoinGroupAlert.this.chatInvite.participants_count;
         }

         int var3 = var1;
         if (var1 != var2) {
            var3 = var1 + 1;
         }

         return var3;
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         JoinSheetUserCell var3 = (JoinSheetUserCell)var1.itemView;
         if (var2 < JoinGroupAlert.this.chatInvite.participants.size()) {
            var3.setUser((TLRPC.User)JoinGroupAlert.this.chatInvite.participants.get(var2));
         } else {
            if (JoinGroupAlert.this.chatInvite.chat != null) {
               var2 = JoinGroupAlert.this.chatInvite.chat.participants_count;
            } else {
               var2 = JoinGroupAlert.this.chatInvite.participants_count;
            }

            var3.setCount(var2 - JoinGroupAlert.this.chatInvite.participants.size());
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         JoinSheetUserCell var3 = new JoinSheetUserCell(this.context);
         var3.setLayoutParams(new RecyclerView.LayoutParams(AndroidUtilities.dp(100.0F), AndroidUtilities.dp(90.0F)));
         return new RecyclerListView.Holder(var3);
      }
   }
}
