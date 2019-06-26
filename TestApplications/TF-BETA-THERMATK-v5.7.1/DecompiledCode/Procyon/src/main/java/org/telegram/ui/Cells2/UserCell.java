// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells2;

import android.text.TextUtils;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserObject;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.tgnet.TLRPC;
import android.widget.ImageView;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class UserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox checkBox;
    private CheckBoxSquare checkBoxBig;
    private int currentAccount;
    private int currentDrawable;
    private int currentId;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currrntStatus;
    private ImageView imageView;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private int statusColor;
    private int statusOnlineColor;
    private SimpleTextView statusTextView;
    
    public UserCell(final Context context, final int n, final int n2) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
        this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
        this.avatarDrawable = new AvatarDrawable();
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(24.0f));
        final BackupImageView avatarImageView = this.avatarImageView;
        final boolean isRTL = LocaleController.isRTL;
        int n3 = 5;
        int n4;
        if (isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 0.0f;
        }
        else {
            n5 = (float)(n + 7);
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = (float)(n + 7);
        }
        else {
            n6 = 0.0f;
        }
        this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n4 | 0x30, n5, 11.0f, n6, 0.0f));
        (this.nameTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(17);
        final SimpleTextView nameTextView = this.nameTextView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        nameTextView.setGravity(n7 | 0x30);
        final SimpleTextView nameTextView2 = this.nameTextView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n9 = 18;
        int n10;
        if (isRTL2) {
            if (n2 == 2) {
                n10 = 18;
            }
            else {
                n10 = 0;
            }
            n10 += 28;
        }
        else {
            n10 = n + 68;
        }
        final float n11 = (float)n10;
        int n12;
        if (LocaleController.isRTL) {
            n12 = n + 68;
        }
        else {
            if (n2 == 2) {
                n12 = n9;
            }
            else {
                n12 = 0;
            }
            n12 += 28;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n8 | 0x30, n11, 14.5f, (float)n12, 0.0f));
        (this.statusTextView = new SimpleTextView(context)).setTextSize(14);
        final SimpleTextView statusTextView = this.statusTextView;
        int n13;
        if (LocaleController.isRTL) {
            n13 = 5;
        }
        else {
            n13 = 3;
        }
        statusTextView.setGravity(n13 | 0x30);
        final SimpleTextView statusTextView2 = this.statusTextView;
        int n14;
        if (LocaleController.isRTL) {
            n14 = 5;
        }
        else {
            n14 = 3;
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = 28.0f;
        }
        else {
            n15 = (float)(n + 68);
        }
        float n16;
        if (LocaleController.isRTL) {
            n16 = (float)(n + 68);
        }
        else {
            n16 = 28.0f;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n14 | 0x30, n15, 37.5f, n16, 0.0f));
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        this.imageView.setVisibility(8);
        final ImageView imageView = this.imageView;
        int n17;
        if (LocaleController.isRTL) {
            n17 = 5;
        }
        else {
            n17 = 3;
        }
        float n18;
        if (LocaleController.isRTL) {
            n18 = 0.0f;
        }
        else {
            n18 = 16.0f;
        }
        float n19;
        if (LocaleController.isRTL) {
            n19 = 16.0f;
        }
        else {
            n19 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n17 | 0x10, n18, 0.0f, n19, 0.0f));
        if (n2 == 2) {
            this.checkBoxBig = new CheckBoxSquare(context, false);
            final CheckBoxSquare checkBoxBig = this.checkBoxBig;
            if (LocaleController.isRTL) {
                n3 = 3;
            }
            float n20;
            if (LocaleController.isRTL) {
                n20 = 19.0f;
            }
            else {
                n20 = 0.0f;
            }
            float n21;
            if (LocaleController.isRTL) {
                n21 = 0.0f;
            }
            else {
                n21 = 19.0f;
            }
            this.addView((View)checkBoxBig, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n3 | 0x10, n20, 0.0f, n21, 0.0f));
        }
        else if (n2 == 1) {
            (this.checkBox = new CheckBox(context, 2131165802)).setVisibility(4);
            this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
            final CheckBox checkBox = this.checkBox;
            if (!LocaleController.isRTL) {
                n3 = 3;
            }
            float n22;
            if (LocaleController.isRTL) {
                n22 = 0.0f;
            }
            else {
                n22 = (float)(n + 37);
            }
            float n23;
            if (LocaleController.isRTL) {
                n23 = (float)(n + 37);
            }
            else {
                n23 = 0.0f;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n3 | 0x30, n22, 41.0f, n23, 0.0f));
        }
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void invalidate() {
        super.invalidate();
        final CheckBoxSquare checkBoxBig = this.checkBoxBig;
        if (checkBoxBig != null) {
            checkBoxBig.invalidate();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(70.0f), 1073741824));
    }
    
    public void setCheckDisabled(final boolean disabled) {
        final CheckBoxSquare checkBoxBig = this.checkBoxBig;
        if (checkBoxBig != null) {
            checkBoxBig.setDisabled(disabled);
        }
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        final CheckBox checkBox = this.checkBox;
        if (checkBox != null) {
            if (checkBox.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(b, b2);
        }
        else {
            final CheckBoxSquare checkBoxBig = this.checkBoxBig;
            if (checkBoxBig != null) {
                if (checkBoxBig.getVisibility() != 0) {
                    this.checkBoxBig.setVisibility(0);
                }
                this.checkBoxBig.setChecked(b, b2);
            }
        }
    }
    
    public void setCurrentId(final int currentId) {
        this.currentId = currentId;
    }
    
    public void setData(final TLObject currentObject, final CharSequence currentName, final CharSequence currrntStatus, final int currentDrawable) {
        if (currentObject == null && currentName == null && currrntStatus == null) {
            this.currrntStatus = null;
            this.currentName = null;
            this.currentObject = null;
            this.nameTextView.setText("");
            this.statusTextView.setText("");
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.currrntStatus = currrntStatus;
        this.currentName = currentName;
        this.currentObject = currentObject;
        this.currentDrawable = currentDrawable;
        this.update(0);
    }
    
    public void setNameTypeface(final Typeface typeface) {
        this.nameTextView.setTypeface(typeface);
    }
    
    public void setStatusColors(final int statusColor, final int statusOnlineColor) {
        this.statusColor = statusColor;
        this.statusOnlineColor = statusOnlineColor;
    }
    
    public void update(int visibility) {
        final TLObject currentObject = this.currentObject;
        TLObject info = null;
        TLRPC.FileLocation lastAvatar = null;
        TLRPC.Chat info2 = null;
        Label_0087: {
            if (currentObject instanceof TLRPC.User) {
                info = currentObject;
                final TLRPC.UserProfilePhoto photo = ((TLRPC.User)info).photo;
                if (photo != null) {
                    lastAvatar = photo.photo_small;
                    info2 = null;
                    break Label_0087;
                }
            }
            else if (currentObject instanceof TLRPC.Chat) {
                info2 = (TLRPC.Chat)currentObject;
                final TLRPC.ChatPhoto photo2 = info2.photo;
                if (photo2 != null) {
                    lastAvatar = photo2.photo_small;
                    info = null;
                    break Label_0087;
                }
                info = (lastAvatar = null);
                break Label_0087;
            }
            else {
                info = null;
            }
            lastAvatar = null;
            info2 = null;
        }
        String s3;
        if (visibility != 0) {
            boolean b = false;
            Label_0171: {
                Label_0168: {
                    if ((visibility & 0x2) != 0x0) {
                        if (this.lastAvatar == null || lastAvatar != null) {
                            final TLRPC.FileLocation lastAvatar2 = this.lastAvatar;
                            if (lastAvatar2 != null || lastAvatar == null || lastAvatar2 == null || lastAvatar == null || (lastAvatar2.volume_id == lastAvatar.volume_id && lastAvatar2.local_id == lastAvatar.local_id)) {
                                break Label_0168;
                            }
                        }
                        b = true;
                        break Label_0171;
                    }
                }
                b = false;
            }
            int n = b ? 1 : 0;
            if (info != null && (n = (b ? 1 : 0)) == 0) {
                n = (b ? 1 : 0);
                if ((visibility & 0x4) != 0x0) {
                    final TLRPC.UserStatus status = ((TLRPC.User)info).status;
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
            String s2;
            if (n == 0 && this.currentName == null && this.lastName != null && (visibility & 0x1) != 0x0) {
                String s;
                if (info != null) {
                    s = UserObject.getUserName((TLRPC.User)info);
                }
                else {
                    s = info2.title;
                }
                s2 = s;
                if (!s.equals(this.lastName)) {
                    n = 1;
                    s2 = s;
                }
            }
            else {
                s2 = null;
            }
            s3 = s2;
            if (n == 0) {
                return;
            }
        }
        else {
            s3 = null;
        }
        this.lastAvatar = lastAvatar;
        if (info != null) {
            this.avatarDrawable.setInfo((TLRPC.User)info);
            final TLRPC.UserStatus status2 = ((TLRPC.User)info).status;
            if (status2 != null) {
                this.lastStatus = status2.expires;
            }
            else {
                this.lastStatus = 0;
            }
        }
        else if (info2 != null) {
            this.avatarDrawable.setInfo(info2);
        }
        else {
            final CharSequence currentName = this.currentName;
            if (currentName != null) {
                this.avatarDrawable.setInfo(this.currentId, currentName.toString(), null, false);
            }
            else {
                this.avatarDrawable.setInfo(this.currentId, "#", null, false);
            }
        }
        final CharSequence currentName2 = this.currentName;
        if (currentName2 != null) {
            this.lastName = null;
            this.nameTextView.setText(currentName2);
        }
        else {
            if (info != null) {
                String userName;
                if ((userName = s3) == null) {
                    userName = UserObject.getUserName((TLRPC.User)info);
                }
                this.lastName = userName;
            }
            else {
                String title;
                if ((title = s3) == null) {
                    title = info2.title;
                }
                this.lastName = title;
            }
            this.nameTextView.setText(this.lastName);
        }
        if (this.currrntStatus != null) {
            this.statusTextView.setTextColor(this.statusColor);
            this.statusTextView.setText(this.currrntStatus);
        }
        else if (info != null) {
            Label_0757: {
                if (((TLRPC.User)info).bot) {
                    this.statusTextView.setTextColor(this.statusColor);
                    if (((TLRPC.User)info).bot_chat_history) {
                        this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
                    }
                    else {
                        this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
                    }
                }
                else {
                    if (((TLRPC.User)info).id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        final TLRPC.UserStatus status3 = ((TLRPC.User)info).status;
                        if (status3 == null || status3.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                            if (!MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(((TLRPC.User)info).id)) {
                                this.statusTextView.setTextColor(this.statusColor);
                                this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, (TLRPC.User)info));
                                break Label_0757;
                            }
                        }
                    }
                    this.statusTextView.setTextColor(this.statusOnlineColor);
                    this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
                }
            }
            this.avatarImageView.setImage(ImageLocation.getForUser((TLRPC.User)info, false), "50_50", this.avatarDrawable, info);
        }
        else if (info2 != null) {
            this.statusTextView.setTextColor(this.statusColor);
            if (ChatObject.isChannel(info2) && !info2.megagroup) {
                visibility = info2.participants_count;
                if (visibility != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Subscribers", visibility));
                }
                else if (TextUtils.isEmpty((CharSequence)info2.username)) {
                    this.statusTextView.setText(LocaleController.getString("ChannelPrivate", 2131558988));
                }
                else {
                    this.statusTextView.setText(LocaleController.getString("ChannelPublic", 2131558991));
                }
            }
            else {
                visibility = info2.participants_count;
                if (visibility != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Members", visibility));
                }
                else if (TextUtils.isEmpty((CharSequence)info2.username)) {
                    this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
                }
                else {
                    this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
                }
            }
            this.avatarImageView.setImage(ImageLocation.getForChat(info2, false), "50_50", this.avatarDrawable, this.currentObject);
        }
        final int visibility2 = this.imageView.getVisibility();
        visibility = 8;
        if ((visibility2 == 0 && this.currentDrawable == 0) || (this.imageView.getVisibility() == 8 && this.currentDrawable != 0)) {
            final ImageView imageView = this.imageView;
            if (this.currentDrawable != 0) {
                visibility = 0;
            }
            imageView.setVisibility(visibility);
            this.imageView.setImageResource(this.currentDrawable);
        }
    }
}
