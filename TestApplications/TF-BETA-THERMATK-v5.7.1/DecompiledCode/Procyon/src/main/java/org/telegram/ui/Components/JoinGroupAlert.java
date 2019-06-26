// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewGroup;
import org.telegram.ui.Cells.JoinSheetUserCell;
import org.telegram.ui.ChatActivity;
import android.os.Bundle;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.view.View$OnClickListener;
import android.widget.LinearLayout$LayoutParams;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import org.telegram.messenger.FileLoader;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Context;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;

public class JoinGroupAlert extends BottomSheet
{
    private TLRPC.ChatInvite chatInvite;
    private BaseFragment fragment;
    private String hash;
    
    public JoinGroupAlert(final Context context, final TLRPC.ChatInvite chatInvite, String s, final BaseFragment fragment) {
        super(context, false, 0);
        this.setApplyBottomPadding(false);
        this.setApplyTopPadding(false);
        this.fragment = fragment;
        this.chatInvite = chatInvite;
        this.hash = s;
        final LinearLayout customView = new LinearLayout(context);
        customView.setOrientation(1);
        customView.setClickable(true);
        this.setCustomView((View)customView);
        final BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(35.0f));
        customView.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(70, 70, 49, 0, 12, 0, 0));
        final TLRPC.Chat chat = chatInvite.chat;
        int n;
        if (chat != null) {
            final AvatarDrawable avatarDrawable = new AvatarDrawable(chat);
            final TLRPC.Chat chat2 = chatInvite.chat;
            s = chat2.title;
            n = chat2.participants_count;
            backupImageView.setImage(ImageLocation.getForChat(chat2, false), "50_50", avatarDrawable, chatInvite);
        }
        else {
            final AvatarDrawable avatarDrawable2 = new AvatarDrawable();
            avatarDrawable2.setInfo(0, chatInvite.title, null, false);
            s = chatInvite.title;
            n = chatInvite.participants_count;
            backupImageView.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(chatInvite.photo.sizes, 50), chatInvite.photo), "50_50", avatarDrawable2, chatInvite);
        }
        final TextView textView = new TextView(context);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setText((CharSequence)s);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils$TruncateAt.END);
        int n2;
        if (n > 0) {
            n2 = 0;
        }
        else {
            n2 = 10;
        }
        customView.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, n2));
        if (n > 0) {
            final TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 14.0f);
            textView2.setTextColor(Theme.getColor("dialogTextGray3"));
            textView2.setSingleLine(true);
            textView2.setEllipsize(TextUtils$TruncateAt.END);
            textView2.setText((CharSequence)LocaleController.formatPluralString("Members", n));
            customView.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 10, 4, 10, 10));
        }
        if (!chatInvite.participants.isEmpty()) {
            final RecyclerListView recyclerListView = new RecyclerListView(context);
            recyclerListView.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
            recyclerListView.setNestedScrollingEnabled(false);
            recyclerListView.setClipToPadding(false);
            recyclerListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.getContext(), 0, false));
            recyclerListView.setHorizontalScrollBarEnabled(false);
            recyclerListView.setVerticalScrollBarEnabled(false);
            recyclerListView.setAdapter(new UsersAdapter(context));
            recyclerListView.setGlowColor(Theme.getColor("dialogScrollGlow"));
            customView.addView((View)recyclerListView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 90, 49, 0, 0, 0, 0));
        }
        final View view = new View(context);
        view.setBackgroundColor(Theme.getColor("dialogShadowLine"));
        customView.addView(view, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight()));
        final PickerBottomLayout pickerBottomLayout = new PickerBottomLayout(context, false);
        customView.addView((View)pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        pickerBottomLayout.cancelButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        pickerBottomLayout.cancelButton.setTextColor(Theme.getColor("dialogTextBlue2"));
        pickerBottomLayout.cancelButton.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        pickerBottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$JoinGroupAlert$HWwkTLyyGhrlSGGzuTHLEOoFKaE(this));
        pickerBottomLayout.doneButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        pickerBottomLayout.doneButton.setVisibility(0);
        pickerBottomLayout.doneButtonBadgeTextView.setVisibility(8);
        pickerBottomLayout.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
        pickerBottomLayout.doneButtonTextView.setText((CharSequence)LocaleController.getString("JoinGroup", 2131559703));
        pickerBottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$JoinGroupAlert$MfRcTjxTXiGmJvnuSHx7GPUtAhw(this));
    }
    
    private class UsersAdapter extends SelectionAdapter
    {
        private Context context;
        
        public UsersAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            final int size = JoinGroupAlert.this.chatInvite.participants.size();
            int n;
            if (JoinGroupAlert.this.chatInvite.chat != null) {
                n = JoinGroupAlert.this.chatInvite.chat.participants_count;
            }
            else {
                n = JoinGroupAlert.this.chatInvite.participants_count;
            }
            int n2 = size;
            if (size != n) {
                n2 = size + 1;
            }
            return n2;
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int index) {
            final JoinSheetUserCell joinSheetUserCell = (JoinSheetUserCell)viewHolder.itemView;
            if (index < JoinGroupAlert.this.chatInvite.participants.size()) {
                joinSheetUserCell.setUser(JoinGroupAlert.this.chatInvite.participants.get(index));
            }
            else {
                if (JoinGroupAlert.this.chatInvite.chat != null) {
                    index = JoinGroupAlert.this.chatInvite.chat.participants_count;
                }
                else {
                    index = JoinGroupAlert.this.chatInvite.participants_count;
                }
                joinSheetUserCell.setCount(index - JoinGroupAlert.this.chatInvite.participants.size());
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final JoinSheetUserCell joinSheetUserCell = new JoinSheetUserCell(this.context);
            ((View)joinSheetUserCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(90.0f)));
            return new RecyclerListView.Holder((View)joinSheetUserCell);
        }
    }
}
