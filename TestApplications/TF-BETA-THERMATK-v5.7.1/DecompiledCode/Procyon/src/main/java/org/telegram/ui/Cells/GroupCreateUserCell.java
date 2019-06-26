// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserObject;
import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class GroupCreateUserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox2 checkBox;
    private int currentAccount;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currentStatus;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private SimpleTextView statusTextView;
    
    public GroupCreateUserCell(final Context context, final boolean b, int n) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.avatarDrawable = new AvatarDrawable();
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(24.0f));
        final BackupImageView avatarImageView = this.avatarImageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        int n3;
        if (isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 0.0f;
        }
        else {
            n4 = (float)(n + 13);
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = (float)(n + 13);
        }
        else {
            n5 = 0.0f;
        }
        this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(46, 46.0f, n3 | 0x30, n4, 6.0f, n5, 0.0f));
        (this.nameTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setTextSize(16);
        final SimpleTextView nameTextView = this.nameTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        nameTextView.setGravity(n6 | 0x30);
        final SimpleTextView nameTextView2 = this.nameTextView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n8 = 28;
        int n9;
        if (isRTL2) {
            n9 = 28;
        }
        else {
            n9 = 72;
        }
        final float n10 = (float)(n9 + n);
        int n11;
        if (LocaleController.isRTL) {
            n11 = 72;
        }
        else {
            n11 = 28;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n7 | 0x30, n10, 10.0f, (float)(n11 + n), 0.0f));
        (this.statusTextView = new SimpleTextView(context)).setTextSize(15);
        final SimpleTextView statusTextView = this.statusTextView;
        int n12;
        if (LocaleController.isRTL) {
            n12 = 5;
        }
        else {
            n12 = 3;
        }
        statusTextView.setGravity(n12 | 0x30);
        final SimpleTextView statusTextView2 = this.statusTextView;
        int n13;
        if (LocaleController.isRTL) {
            n13 = 5;
        }
        else {
            n13 = 3;
        }
        int n14;
        if (LocaleController.isRTL) {
            n14 = 28;
        }
        else {
            n14 = 72;
        }
        final float n15 = (float)(n14 + n);
        int n16 = n8;
        if (LocaleController.isRTL) {
            n16 = 72;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n13 | 0x30, n15, 32.0f, (float)(n16 + n), 0.0f));
        if (b) {
            (this.checkBox = new CheckBox2(context)).setColor(null, "windowBackgroundWhite", "checkboxCheck");
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            final CheckBox2 checkBox = this.checkBox;
            if (LocaleController.isRTL) {
                n = n2;
            }
            else {
                n = 3;
            }
            float n17;
            if (LocaleController.isRTL) {
                n17 = 0.0f;
            }
            else {
                n17 = 40.0f;
            }
            float n18;
            if (LocaleController.isRTL) {
                n18 = 39.0f;
            }
            else {
                n18 = 0.0f;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, n | 0x30, n17, 33.0f, n18, 0.0f));
        }
    }
    
    public TLObject getObject() {
        return this.currentObject;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f), 1073741824));
    }
    
    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }
    
    public void setCheckBoxEnabled(final boolean enabled) {
        this.checkBox.setEnabled(enabled);
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBox.setChecked(b, b2);
    }
    
    public void setObject(final TLObject currentObject, final CharSequence currentName, final CharSequence currentStatus) {
        this.currentObject = currentObject;
        this.currentStatus = currentStatus;
        this.currentName = currentName;
        this.update(0);
    }
    
    public void update(int lastStatus) {
        final TLObject currentObject = this.currentObject;
        if (currentObject == null) {
            return;
        }
        if (currentObject instanceof TLRPC.User) {
            final TLRPC.User info = (TLRPC.User)currentObject;
            final TLRPC.UserProfilePhoto photo = info.photo;
            TLRPC.FileLocation photo_small;
            if (photo != null) {
                photo_small = photo.photo_small;
            }
            else {
                photo_small = null;
            }
            String userName;
            if (lastStatus != 0) {
                boolean b = false;
                Label_0122: {
                    Label_0119: {
                        if ((lastStatus & 0x2) != 0x0) {
                            if ((this.lastAvatar == null || photo_small != null) && (this.lastAvatar != null || photo_small == null)) {
                                final TLRPC.FileLocation lastAvatar = this.lastAvatar;
                                if (lastAvatar == null || photo_small == null || (lastAvatar.volume_id == photo_small.volume_id && lastAvatar.local_id == photo_small.local_id)) {
                                    break Label_0119;
                                }
                            }
                            b = true;
                            break Label_0122;
                        }
                    }
                    b = false;
                }
                int n = b ? 1 : 0;
                if (info != null) {
                    n = (b ? 1 : 0);
                    if (this.currentStatus == null && (n = (b ? 1 : 0)) == 0) {
                        n = (b ? 1 : 0);
                        if ((lastStatus & 0x4) != 0x0) {
                            final TLRPC.UserStatus status = info.status;
                            int expires;
                            if (status != null) {
                                expires = status.expires;
                            }
                            else {
                                expires = 0;
                            }
                            n = (b ? 1 : 0);
                            if (expires != this.lastStatus) {
                                n = 1;
                            }
                        }
                    }
                }
                if (n == 0 && this.currentName == null && this.lastName != null && (lastStatus & 0x1) != 0x0) {
                    final String s = userName = UserObject.getUserName(info);
                    if (!s.equals(this.lastName)) {
                        n = 1;
                        userName = s;
                    }
                }
                else {
                    userName = null;
                }
                if (n == 0) {
                    return;
                }
            }
            else {
                userName = null;
            }
            this.avatarDrawable.setInfo(info);
            final TLRPC.UserStatus status2 = info.status;
            if (status2 != null) {
                lastStatus = status2.expires;
            }
            else {
                lastStatus = 0;
            }
            this.lastStatus = lastStatus;
            final CharSequence currentName = this.currentName;
            if (currentName != null) {
                this.lastName = null;
                this.nameTextView.setText(currentName, true);
            }
            else {
                String userName2;
                if ((userName2 = userName) == null) {
                    userName2 = UserObject.getUserName(info);
                }
                this.lastName = userName2;
                this.nameTextView.setText(this.lastName);
            }
            Label_0562: {
                if (this.currentStatus == null) {
                    if (info.bot) {
                        this.statusTextView.setTag((Object)"windowBackgroundWhiteGrayText");
                        this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                        this.statusTextView.setText(LocaleController.getString("Bot", 2131558848));
                    }
                    else {
                        if (info.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            final TLRPC.UserStatus status3 = info.status;
                            if (status3 == null || status3.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                                if (!MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(info.id)) {
                                    this.statusTextView.setTag((Object)"windowBackgroundWhiteGrayText");
                                    this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                                    this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, info));
                                    break Label_0562;
                                }
                            }
                        }
                        this.statusTextView.setTag((Object)"windowBackgroundWhiteBlueText");
                        this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
                        this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
                    }
                }
            }
            this.avatarImageView.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
        }
        else {
            final TLRPC.Chat info2 = (TLRPC.Chat)currentObject;
            final TLRPC.ChatPhoto photo2 = info2.photo;
            TLRPC.FileLocation photo_small2;
            if (photo2 != null) {
                photo_small2 = photo2.photo_small;
            }
            else {
                photo_small2 = null;
            }
            String title;
            if (lastStatus != 0) {
                int n2 = 0;
                Label_0690: {
                    Label_0687: {
                        if ((lastStatus & 0x2) != 0x0) {
                            if ((this.lastAvatar == null || photo_small2 != null) && (this.lastAvatar != null || photo_small2 == null)) {
                                final TLRPC.FileLocation lastAvatar2 = this.lastAvatar;
                                if (lastAvatar2 == null || photo_small2 == null || (lastAvatar2.volume_id == photo_small2.volume_id && lastAvatar2.local_id == photo_small2.local_id)) {
                                    break Label_0687;
                                }
                            }
                            n2 = 1;
                            break Label_0690;
                        }
                    }
                    n2 = 0;
                }
                Label_0749: {
                    if (n2 == 0 && this.currentName == null) {
                        final String lastName = this.lastName;
                        if (lastName != null && (lastStatus & 0x1) != 0x0) {
                            final String s2 = title = info2.title;
                            if (!s2.equals(lastName)) {
                                n2 = 1;
                                title = s2;
                            }
                            break Label_0749;
                        }
                    }
                    title = null;
                }
                if (n2 == 0) {
                    return;
                }
            }
            else {
                title = null;
            }
            this.avatarDrawable.setInfo(info2);
            final CharSequence currentName2 = this.currentName;
            if (currentName2 != null) {
                this.lastName = null;
                this.nameTextView.setText(currentName2, true);
            }
            else {
                String title2;
                if ((title2 = title) == null) {
                    title2 = info2.title;
                }
                this.lastName = title2;
                this.nameTextView.setText(this.lastName);
            }
            if (this.currentStatus == null) {
                this.statusTextView.setTag((Object)"windowBackgroundWhiteGrayText");
                this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                lastStatus = info2.participants_count;
                if (lastStatus != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Members", lastStatus));
                }
                else if (TextUtils.isEmpty((CharSequence)info2.username)) {
                    this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
                }
                else {
                    this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
                }
            }
            this.avatarImageView.setImage(ImageLocation.getForChat(info2, false), "50_50", this.avatarDrawable, info2);
        }
        final CharSequence currentStatus = this.currentStatus;
        if (currentStatus != null) {
            this.statusTextView.setText(currentStatus, true);
            this.statusTextView.setTag((Object)"windowBackgroundWhiteGrayText");
            this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        }
    }
}
