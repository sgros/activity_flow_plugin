// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.text.TextUtils;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserObject;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class ManageChatUserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private int currentAccount;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currrntStatus;
    private ManageChatUserCellDelegate delegate;
    private boolean isAdmin;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private int namePadding;
    private SimpleTextView nameTextView;
    private boolean needDivider;
    private ImageView optionsButton;
    private int statusColor;
    private int statusOnlineColor;
    private SimpleTextView statusTextView;
    
    public ManageChatUserCell(final Context context, int n, int namePadding, final boolean b) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
        this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
        this.namePadding = namePadding;
        this.avatarDrawable = new AvatarDrawable();
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(23.0f));
        final BackupImageView avatarImageView = this.avatarImageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        if (isRTL) {
            namePadding = 5;
        }
        else {
            namePadding = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = (float)(n + 7);
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = (float)(n + 7);
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(46, 46.0f, namePadding | 0x30, n3, 8.0f, n4, 0.0f));
        (this.nameTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(17);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        final SimpleTextView nameTextView = this.nameTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        nameTextView.setGravity(n | 0x30);
        final SimpleTextView nameTextView2 = this.nameTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 46.0f;
        }
        else {
            n5 = (float)(this.namePadding + 68);
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = (float)(this.namePadding + 68);
        }
        else {
            n6 = 46.0f;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n | 0x30, n5, 11.5f, n6, 0.0f));
        (this.statusTextView = new SimpleTextView(context)).setTextSize(14);
        final SimpleTextView statusTextView = this.statusTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        statusTextView.setGravity(n | 0x30);
        final SimpleTextView statusTextView2 = this.statusTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 28.0f;
        }
        else {
            n7 = (float)(this.namePadding + 68);
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = (float)(this.namePadding + 68);
        }
        else {
            n8 = 28.0f;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n | 0x30, n7, 34.5f, n8, 0.0f));
        if (b) {
            (this.optionsButton = new ImageView(context)).setFocusable(false);
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
            this.optionsButton.setImageResource(2131165416);
            this.optionsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff$Mode.MULTIPLY));
            this.optionsButton.setScaleType(ImageView$ScaleType.CENTER);
            final ImageView optionsButton = this.optionsButton;
            n = n2;
            if (LocaleController.isRTL) {
                n = 3;
            }
            this.addView((View)optionsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(52, 64, n | 0x30));
            this.optionsButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ManageChatUserCell$oJTkyKgCBYt9FR4kYNNgwqbXXuY(this));
            this.optionsButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrUserOptions", 2131558480));
        }
    }
    
    public TLObject getCurrentObject() {
        return this.currentObject;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(68.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(68.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }
    
    public void setData(final TLObject currentObject, final CharSequence currentName, final CharSequence currrntStatus, final boolean needDivider) {
        if (currentObject == null) {
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
        if (this.optionsButton != null) {
            final boolean onOptionsButtonCheck = this.delegate.onOptionsButtonCheck(this, false);
            final ImageView optionsButton = this.optionsButton;
            int visibility;
            if (onOptionsButtonCheck) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            optionsButton.setVisibility(visibility);
            final SimpleTextView nameTextView = this.nameTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n3 = 46;
            int n4;
            if (isRTL2) {
                if (onOptionsButtonCheck) {
                    n4 = 46;
                }
                else {
                    n4 = 28;
                }
            }
            else {
                n4 = this.namePadding + 68;
            }
            final float n5 = (float)n4;
            float n6;
            if (currrntStatus != null && currrntStatus.length() <= 0) {
                n6 = 20.5f;
            }
            else {
                n6 = 11.5f;
            }
            int n7;
            if (LocaleController.isRTL) {
                n7 = this.namePadding + 68;
            }
            else if (onOptionsButtonCheck) {
                n7 = 46;
            }
            else {
                n7 = 28;
            }
            nameTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n2 | 0x30, n5, n6, (float)n7, 0.0f));
            final SimpleTextView statusTextView = this.statusTextView;
            int n8;
            if (LocaleController.isRTL) {
                n8 = n;
            }
            else {
                n8 = 3;
            }
            int n9;
            if (LocaleController.isRTL) {
                if (onOptionsButtonCheck) {
                    n9 = 46;
                }
                else {
                    n9 = 28;
                }
            }
            else {
                n9 = this.namePadding + 68;
            }
            final float n10 = (float)n9;
            float n11;
            if (LocaleController.isRTL) {
                n11 = (float)(this.namePadding + 68);
            }
            else {
                int n12;
                if (onOptionsButtonCheck) {
                    n12 = n3;
                }
                else {
                    n12 = 28;
                }
                n11 = (float)n12;
            }
            statusTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n8 | 0x30, n10, 34.5f, n11, 0.0f));
        }
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
        this.update(0);
    }
    
    public void setDelegate(final ManageChatUserCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIsAdmin(final boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public void setStatusColors(final int statusColor, final int statusOnlineColor) {
        this.statusColor = statusColor;
        this.statusOnlineColor = statusOnlineColor;
    }
    
    public void update(int participants_count) {
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
            if (participants_count != 0) {
                boolean b = false;
                Label_0125: {
                    Label_0122: {
                        if ((participants_count & 0x2) != 0x0) {
                            if ((this.lastAvatar == null || photo_small != null) && (this.lastAvatar != null || photo_small == null)) {
                                final TLRPC.FileLocation lastAvatar = this.lastAvatar;
                                if (lastAvatar == null || photo_small == null || (lastAvatar.volume_id == photo_small.volume_id && lastAvatar.local_id == photo_small.local_id)) {
                                    break Label_0122;
                                }
                            }
                            b = true;
                            break Label_0125;
                        }
                    }
                    b = false;
                }
                int n = b ? 1 : 0;
                if (info != null && (n = (b ? 1 : 0)) == 0) {
                    n = (b ? 1 : 0);
                    if ((participants_count & 0x4) != 0x0) {
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
                if (n == 0 && this.currentName == null && this.lastName != null && (participants_count & 0x1) != 0x0) {
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
                this.lastStatus = status2.expires;
            }
            else {
                this.lastStatus = 0;
            }
            final CharSequence currentName = this.currentName;
            if (currentName != null) {
                this.lastName = null;
                this.nameTextView.setText(currentName);
            }
            else {
                String userName2;
                if ((userName2 = userName) == null) {
                    userName2 = UserObject.getUserName(info);
                }
                this.lastName = userName2;
                this.nameTextView.setText(this.lastName);
            }
            Label_0585: {
                if (this.currrntStatus != null) {
                    this.statusTextView.setTextColor(this.statusColor);
                    this.statusTextView.setText(this.currrntStatus);
                }
                else if (info.bot) {
                    this.statusTextView.setTextColor(this.statusColor);
                    if (!info.bot_chat_history && !this.isAdmin) {
                        this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
                    }
                    else {
                        this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
                    }
                }
                else {
                    if (info.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        final TLRPC.UserStatus status3 = info.status;
                        if (status3 == null || status3.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                            if (!MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(info.id)) {
                                this.statusTextView.setTextColor(this.statusColor);
                                this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, info));
                                break Label_0585;
                            }
                        }
                    }
                    this.statusTextView.setTextColor(this.statusOnlineColor);
                    this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
                }
            }
            this.lastAvatar = photo_small;
            this.avatarImageView.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
        }
        else if (currentObject instanceof TLRPC.Chat) {
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
            if (participants_count != 0) {
                int n2 = 0;
                Label_0729: {
                    Label_0726: {
                        if ((participants_count & 0x2) != 0x0) {
                            if ((this.lastAvatar == null || photo_small2 != null) && (this.lastAvatar != null || photo_small2 == null)) {
                                final TLRPC.FileLocation lastAvatar2 = this.lastAvatar;
                                if (lastAvatar2 == null || photo_small2 == null || (lastAvatar2.volume_id == photo_small2.volume_id && lastAvatar2.local_id == photo_small2.local_id)) {
                                    break Label_0726;
                                }
                            }
                            n2 = 1;
                            break Label_0729;
                        }
                    }
                    n2 = 0;
                }
                Label_0788: {
                    if (n2 == 0 && this.currentName == null) {
                        final String lastName = this.lastName;
                        if (lastName != null && (participants_count & 0x1) != 0x0) {
                            final String s2 = title = info2.title;
                            if (!s2.equals(lastName)) {
                                n2 = 1;
                                title = s2;
                            }
                            break Label_0788;
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
                this.nameTextView.setText(currentName2);
            }
            else {
                String title2;
                if ((title2 = title) == null) {
                    title2 = info2.title;
                }
                this.lastName = title2;
                this.nameTextView.setText(this.lastName);
            }
            if (this.currrntStatus != null) {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(this.currrntStatus);
            }
            else {
                this.statusTextView.setTextColor(this.statusColor);
                participants_count = info2.participants_count;
                if (participants_count != 0) {
                    this.statusTextView.setText(LocaleController.formatPluralString("Members", participants_count));
                }
                else if (TextUtils.isEmpty((CharSequence)info2.username)) {
                    this.statusTextView.setText(LocaleController.getString("MegaPrivate", 2131559831));
                }
                else {
                    this.statusTextView.setText(LocaleController.getString("MegaPublic", 2131559834));
                }
            }
            this.lastAvatar = photo_small2;
            this.avatarImageView.setImage(ImageLocation.getForChat(info2, false), "50_50", this.avatarDrawable, info2);
        }
    }
    
    public interface ManageChatUserCellDelegate
    {
        boolean onOptionsButtonCheck(final ManageChatUserCell p0, final boolean p1);
    }
}
