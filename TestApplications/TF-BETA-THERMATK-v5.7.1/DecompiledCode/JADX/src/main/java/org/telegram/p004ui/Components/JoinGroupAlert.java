package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Cells.JoinSheetUserCell;
import org.telegram.p004ui.ChatActivity;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatInvite;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Components.JoinGroupAlert */
public class JoinGroupAlert extends BottomSheet {
    private ChatInvite chatInvite;
    private BaseFragment fragment;
    private String hash;

    /* renamed from: org.telegram.ui.Components.JoinGroupAlert$UsersAdapter */
    private class UsersAdapter extends SelectionAdapter {
        private Context context;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return false;
        }

        public UsersAdapter(Context context) {
            this.context = context;
        }

        public int getItemCount() {
            int i;
            int size = JoinGroupAlert.this.chatInvite.participants.size();
            if (JoinGroupAlert.this.chatInvite.chat != null) {
                i = JoinGroupAlert.this.chatInvite.chat.participants_count;
            } else {
                i = JoinGroupAlert.this.chatInvite.participants_count;
            }
            return size != i ? size + 1 : size;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            JoinSheetUserCell joinSheetUserCell = new JoinSheetUserCell(this.context);
            joinSheetUserCell.setLayoutParams(new LayoutParams(AndroidUtilities.m26dp(100.0f), AndroidUtilities.m26dp(90.0f)));
            return new Holder(joinSheetUserCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            JoinSheetUserCell joinSheetUserCell = (JoinSheetUserCell) viewHolder.itemView;
            if (i < JoinGroupAlert.this.chatInvite.participants.size()) {
                joinSheetUserCell.setUser((User) JoinGroupAlert.this.chatInvite.participants.get(i));
                return;
            }
            if (JoinGroupAlert.this.chatInvite.chat != null) {
                i = JoinGroupAlert.this.chatInvite.chat.participants_count;
            } else {
                i = JoinGroupAlert.this.chatInvite.participants_count;
            }
            joinSheetUserCell.setCount(i - JoinGroupAlert.this.chatInvite.participants.size());
        }
    }

    public JoinGroupAlert(Context context, ChatInvite chatInvite, String str, BaseFragment baseFragment) {
        CharSequence charSequence;
        int i;
        Context context2 = context;
        Object obj = chatInvite;
        super(context2, false, 0);
        setApplyBottomPadding(false);
        setApplyTopPadding(false);
        this.fragment = baseFragment;
        this.chatInvite = obj;
        this.hash = str;
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setOrientation(1);
        linearLayout.setClickable(true);
        setCustomView(linearLayout);
        BackupImageView backupImageView = new BackupImageView(context2);
        backupImageView.setRoundRadius(AndroidUtilities.m26dp(35.0f));
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(70, 70, 49, 0, 12, 0, 0));
        Chat chat = obj.chat;
        String str2 = "50_50";
        if (chat != null) {
            Drawable avatarDrawable = new AvatarDrawable(chat);
            chat = obj.chat;
            charSequence = chat.title;
            i = chat.participants_count;
            backupImageView.setImage(ImageLocation.getForChat(chat, false), str2, avatarDrawable, obj);
        } else {
            Drawable avatarDrawable2 = new AvatarDrawable();
            avatarDrawable2.setInfo(0, obj.title, null, false);
            charSequence = obj.title;
            i = obj.participants_count;
            backupImageView.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(obj.photo.sizes, 50), obj.photo), str2, avatarDrawable2, obj);
        }
        TextView textView = new TextView(context2);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setText(charSequence);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.END);
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, i > 0 ? 0 : 10));
        if (i > 0) {
            textView = new TextView(context2);
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(Theme.getColor(Theme.key_dialogTextGray3));
            textView.setSingleLine(true);
            textView.setEllipsize(TruncateAt.END);
            textView.setText(LocaleController.formatPluralString("Members", i));
            linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 49, 10, 4, 10, 10));
        }
        if (!obj.participants.isEmpty()) {
            RecyclerListView recyclerListView = new RecyclerListView(context2);
            recyclerListView.setPadding(0, 0, 0, AndroidUtilities.m26dp(8.0f));
            recyclerListView.setNestedScrollingEnabled(false);
            recyclerListView.setClipToPadding(false);
            recyclerListView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
            recyclerListView.setHorizontalScrollBarEnabled(false);
            recyclerListView.setVerticalScrollBarEnabled(false);
            recyclerListView.setAdapter(new UsersAdapter(context2));
            recyclerListView.setGlowColor(Theme.getColor(Theme.key_dialogScrollGlow));
            linearLayout.addView(recyclerListView, LayoutHelper.createLinear(-2, 90, 49, 0, 0, 0, 0));
        }
        View view = new View(context2);
        view.setBackgroundColor(Theme.getColor(Theme.key_dialogShadowLine));
        linearLayout.addView(view, new LinearLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight()));
        PickerBottomLayout pickerBottomLayout = new PickerBottomLayout(context2, false);
        linearLayout.addView(pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
        pickerBottomLayout.cancelButton.setPadding(AndroidUtilities.m26dp(18.0f), 0, AndroidUtilities.m26dp(18.0f), 0);
        TextView textView2 = pickerBottomLayout.cancelButton;
        String str3 = Theme.key_dialogTextBlue2;
        textView2.setTextColor(Theme.getColor(str3));
        pickerBottomLayout.cancelButton.setText(LocaleController.getString("Cancel", C1067R.string.Cancel).toUpperCase());
        pickerBottomLayout.cancelButton.setOnClickListener(new C2604-$$Lambda$JoinGroupAlert$HWwkTLyyGhrlSGGzuTHLEOoFKaE(this));
        pickerBottomLayout.doneButton.setPadding(AndroidUtilities.m26dp(18.0f), 0, AndroidUtilities.m26dp(18.0f), 0);
        pickerBottomLayout.doneButton.setVisibility(0);
        pickerBottomLayout.doneButtonBadgeTextView.setVisibility(8);
        pickerBottomLayout.doneButtonTextView.setTextColor(Theme.getColor(str3));
        pickerBottomLayout.doneButtonTextView.setText(LocaleController.getString("JoinGroup", C1067R.string.JoinGroup));
        pickerBottomLayout.doneButton.setOnClickListener(new C2606-$$Lambda$JoinGroupAlert$MfRcTjxTXiGmJvnuSHx7GPUtAhw(this));
    }

    public /* synthetic */ void lambda$new$0$JoinGroupAlert(View view) {
        dismiss();
    }

    public /* synthetic */ void lambda$new$3$JoinGroupAlert(View view) {
        dismiss();
        TL_messages_importChatInvite tL_messages_importChatInvite = new TL_messages_importChatInvite();
        tL_messages_importChatInvite.hash = this.hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_importChatInvite, new C4048-$$Lambda$JoinGroupAlert$mdXFwxXoZ3zIuXYK2Qsi9b4N8JY(this, tL_messages_importChatInvite), 2);
    }

    public /* synthetic */ void lambda$null$2$JoinGroupAlert(TL_messages_importChatInvite tL_messages_importChatInvite, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            MessagesController.getInstance(this.currentAccount).processUpdates((Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new C2605-$$Lambda$JoinGroupAlert$IWx-squ4_I5Qyjo-N06x9CUQNvc(this, tL_error, tLObject, tL_messages_importChatInvite));
    }

    public /* synthetic */ void lambda$null$1$JoinGroupAlert(TL_error tL_error, TLObject tLObject, TL_messages_importChatInvite tL_messages_importChatInvite) {
        BaseFragment baseFragment = this.fragment;
        if (baseFragment != null && baseFragment.getParentActivity() != null) {
            if (tL_error == null) {
                Updates updates = (Updates) tLObject;
                if (!updates.chats.isEmpty()) {
                    Chat chat = (Chat) updates.chats.get(0);
                    chat.left = false;
                    chat.kicked = false;
                    MessagesController.getInstance(this.currentAccount).putUsers(updates.users, false);
                    MessagesController.getInstance(this.currentAccount).putChats(updates.chats, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("chat_id", chat.f434id);
                    if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, this.fragment)) {
                        ChatActivity chatActivity = new ChatActivity(bundle);
                        BaseFragment baseFragment2 = this.fragment;
                        baseFragment2.presentFragment(chatActivity, baseFragment2 instanceof ChatActivity);
                        return;
                    }
                    return;
                }
                return;
            }
            AlertsCreator.processError(this.currentAccount, tL_error, this.fragment, tL_messages_importChatInvite, new Object[0]);
        }
    }
}
