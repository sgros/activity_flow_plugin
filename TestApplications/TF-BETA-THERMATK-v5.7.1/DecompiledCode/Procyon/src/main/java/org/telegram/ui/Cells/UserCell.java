// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import android.graphics.Typeface;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.NotificationsSettingsActivity;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
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
import android.widget.ImageView;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.TextView;
import android.widget.FrameLayout;

public class UserCell extends FrameLayout
{
    private TextView adminTextView;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox checkBox;
    private CheckBoxSquare checkBoxBig;
    private int currentAccount;
    private int currentDrawable;
    private int currentId;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currentStatus;
    private TLRPC.EncryptedChat encryptedChat;
    private ImageView imageView;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private boolean needDivider;
    private int statusColor;
    private int statusOnlineColor;
    private SimpleTextView statusTextView;
    
    public UserCell(final Context context, int n, int n2, final boolean b) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.statusColor = Theme.getColor("windowBackgroundWhiteGrayText");
        this.statusOnlineColor = Theme.getColor("windowBackgroundWhiteBlueText");
        this.avatarDrawable = new AvatarDrawable();
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(24.0f));
        final BackupImageView avatarImageView = this.avatarImageView;
        int n3;
        if (LocaleController.isRTL) {
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
            n4 = (float)(n + 7);
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = (float)(n + 7);
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
        final boolean isRTL = LocaleController.isRTL;
        final int n8 = 18;
        int n9;
        if (isRTL) {
            if (n2 == 2) {
                n9 = 18;
            }
            else {
                n9 = 0;
            }
            n9 += 28;
        }
        else {
            n9 = n + 64;
        }
        final float n10 = (float)n9;
        int n11;
        if (LocaleController.isRTL) {
            n11 = n + 64;
        }
        else {
            if (n2 == 2) {
                n11 = n8;
            }
            else {
                n11 = 0;
            }
            n11 += 28;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n7 | 0x30, n10, 10.0f, (float)n11, 0.0f));
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
        float n14;
        if (LocaleController.isRTL) {
            n14 = 28.0f;
        }
        else {
            n14 = (float)(n + 64);
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = (float)(n + 64);
        }
        else {
            n15 = 28.0f;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n13 | 0x30, n14, 32.0f, n15, 0.0f));
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        this.imageView.setVisibility(8);
        final ImageView imageView = this.imageView;
        int n16;
        if (LocaleController.isRTL) {
            n16 = 5;
        }
        else {
            n16 = 3;
        }
        float n17;
        if (LocaleController.isRTL) {
            n17 = 0.0f;
        }
        else {
            n17 = 16.0f;
        }
        float n18;
        if (LocaleController.isRTL) {
            n18 = 16.0f;
        }
        else {
            n18 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n16 | 0x10, n17, 0.0f, n18, 0.0f));
        if (n2 == 2) {
            this.checkBoxBig = new CheckBoxSquare(context, false);
            final CheckBoxSquare checkBoxBig = this.checkBoxBig;
            if (LocaleController.isRTL) {
                n = 3;
            }
            else {
                n = 5;
            }
            float n19;
            if (LocaleController.isRTL) {
                n19 = 19.0f;
            }
            else {
                n19 = 0.0f;
            }
            float n20;
            if (LocaleController.isRTL) {
                n20 = 0.0f;
            }
            else {
                n20 = 19.0f;
            }
            this.addView((View)checkBoxBig, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n | 0x10, n19, 0.0f, n20, 0.0f));
        }
        else if (n2 == 1) {
            (this.checkBox = new CheckBox(context, 2131165802)).setVisibility(4);
            this.checkBox.setColor(Theme.getColor("checkbox"), Theme.getColor("checkboxCheck"));
            final CheckBox checkBox = this.checkBox;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            float n21;
            if (LocaleController.isRTL) {
                n21 = 0.0f;
            }
            else {
                n21 = (float)(n + 37);
            }
            float n22;
            if (LocaleController.isRTL) {
                n22 = (float)(n + 37);
            }
            else {
                n22 = 0.0f;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n2 | 0x30, n21, 40.0f, n22, 0.0f));
        }
        if (b) {
            (this.adminTextView = new TextView(context)).setTextSize(1, 14.0f);
            this.adminTextView.setTextColor(Theme.getColor("profile_creatorIcon"));
            final TextView adminTextView = this.adminTextView;
            if (LocaleController.isRTL) {
                n = 3;
            }
            else {
                n = 5;
            }
            float n23;
            if (LocaleController.isRTL) {
                n23 = 23.0f;
            }
            else {
                n23 = 0.0f;
            }
            float n24;
            if (LocaleController.isRTL) {
                n24 = 0.0f;
            }
            else {
                n24 = 23.0f;
            }
            this.addView((View)adminTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n | 0x30, n23, 10.0f, n24, 0.0f));
        }
        this.setFocusable(true);
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
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        final CheckBoxSquare checkBoxBig = this.checkBoxBig;
        if (checkBoxBig != null && checkBoxBig.getVisibility() == 0) {
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setChecked(this.checkBoxBig.isChecked());
            accessibilityNodeInfo.setClassName((CharSequence)"android.widget.CheckBox");
        }
        else {
            final CheckBox checkBox = this.checkBox;
            if (checkBox != null && checkBox.getVisibility() == 0) {
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(this.checkBox.isChecked());
                accessibilityNodeInfo.setClassName((CharSequence)"android.widget.CheckBox");
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setAvatarPadding(final int n) {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.avatarImageView.getLayoutParams();
        final boolean isRTL = LocaleController.isRTL;
        final float n2 = 0.0f;
        float n3;
        if (isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = (float)(n + 7);
        }
        layoutParams.leftMargin = AndroidUtilities.dp(n3);
        float n4;
        if (LocaleController.isRTL) {
            n4 = (float)(n + 7);
        }
        else {
            n4 = 0.0f;
        }
        layoutParams.rightMargin = AndroidUtilities.dp(n4);
        this.avatarImageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.nameTextView.getLayoutParams();
        final boolean isRTL2 = LocaleController.isRTL;
        final int n5 = 18;
        int n6;
        if (isRTL2) {
            if (this.checkBoxBig != null) {
                n6 = 18;
            }
            else {
                n6 = 0;
            }
            n6 += 28;
        }
        else {
            n6 = n + 64;
        }
        frameLayout$LayoutParams.leftMargin = AndroidUtilities.dp((float)n6);
        float n7;
        if (LocaleController.isRTL) {
            n7 = (float)(n + 64);
        }
        else {
            int n8;
            if (this.checkBoxBig != null) {
                n8 = n5;
            }
            else {
                n8 = 0;
            }
            n7 = (float)(n8 + 28);
        }
        frameLayout$LayoutParams.rightMargin = AndroidUtilities.dp(n7);
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.statusTextView.getLayoutParams();
        final boolean isRTL3 = LocaleController.isRTL;
        final float n9 = 28.0f;
        float n10;
        if (isRTL3) {
            n10 = 28.0f;
        }
        else {
            n10 = (float)(n + 64);
        }
        frameLayout$LayoutParams2.leftMargin = AndroidUtilities.dp(n10);
        float n11 = n9;
        if (LocaleController.isRTL) {
            n11 = (float)(n + 64);
        }
        frameLayout$LayoutParams2.rightMargin = AndroidUtilities.dp(n11);
        final CheckBox checkBox = this.checkBox;
        if (checkBox != null) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams3 = (FrameLayout$LayoutParams)checkBox.getLayoutParams();
            float n12;
            if (LocaleController.isRTL) {
                n12 = 0.0f;
            }
            else {
                n12 = (float)(n + 37);
            }
            frameLayout$LayoutParams3.leftMargin = AndroidUtilities.dp(n12);
            float n13 = n2;
            if (LocaleController.isRTL) {
                n13 = (float)(n + 37);
            }
            frameLayout$LayoutParams3.rightMargin = AndroidUtilities.dp(n13);
        }
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
    
    public void setData(final TLObject tlObject, final CharSequence charSequence, final CharSequence charSequence2, final int n) {
        this.setData(tlObject, null, charSequence, charSequence2, n, false);
    }
    
    public void setData(final TLObject tlObject, final CharSequence charSequence, final CharSequence charSequence2, final int n, final boolean b) {
        this.setData(tlObject, null, charSequence, charSequence2, n, b);
    }
    
    public void setData(final TLObject currentObject, final TLRPC.EncryptedChat encryptedChat, final CharSequence currentName, final CharSequence currentStatus, final int currentDrawable, final boolean needDivider) {
        if (currentObject == null && currentName == null && currentStatus == null) {
            this.currentStatus = null;
            this.currentName = null;
            this.currentObject = null;
            this.nameTextView.setText("");
            this.statusTextView.setText("");
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.encryptedChat = encryptedChat;
        this.currentStatus = currentStatus;
        this.currentName = currentName;
        this.currentObject = currentObject;
        this.currentDrawable = currentDrawable;
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
        this.update(0);
    }
    
    public void setException(final NotificationsSettingsActivity.NotificationException ex, final CharSequence charSequence, final boolean b) {
        final boolean hasCustom = ex.hasCustom;
        final int notify = ex.notify;
        final int muteUntil = ex.muteUntil;
        boolean b2 = false;
        String s;
        if (notify == 3 && muteUntil != Integer.MAX_VALUE) {
            final int n = muteUntil - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            if (n <= 0) {
                if (hasCustom) {
                    s = LocaleController.getString("NotificationsCustom", 2131560059);
                }
                else {
                    s = LocaleController.getString("NotificationsUnmuted", 2131560094);
                }
            }
            else if (n < 3600) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", n / 60));
            }
            else if (n < 86400) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil(n / 60.0f / 60.0f)));
            }
            else if (n < 31536000) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil(n / 60.0f / 60.0f / 24.0f)));
            }
            else {
                s = null;
            }
        }
        else {
            if (notify == 0 || notify == 1) {
                b2 = true;
            }
            if (b2 && hasCustom) {
                s = LocaleController.getString("NotificationsCustom", 2131560059);
            }
            else if (b2) {
                s = LocaleController.getString("NotificationsUnmuted", 2131560094);
            }
            else {
                s = LocaleController.getString("NotificationsMuted", 2131560076);
            }
        }
        String string = s;
        if (s == null) {
            string = LocaleController.getString("NotificationsOff", 2131560078);
        }
        final long did = ex.did;
        final int i = (int)did;
        final int j = (int)(did >> 32);
        if (i != 0) {
            if (i > 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(i);
                if (user != null) {
                    this.setData(user, null, charSequence, string, 0, b);
                }
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                if (chat != null) {
                    this.setData(chat, null, charSequence, string, 0, b);
                }
            }
        }
        else {
            final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(j);
            if (encryptedChat != null) {
                final TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                if (user2 != null) {
                    this.setData(user2, encryptedChat, charSequence, string, 0, false);
                }
            }
        }
    }
    
    public void setIsAdmin(int n) {
        final TextView adminTextView = this.adminTextView;
        if (adminTextView == null) {
            return;
        }
        int visibility;
        if (n != 0) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        adminTextView.setVisibility(visibility);
        if (n == 1) {
            this.adminTextView.setText((CharSequence)LocaleController.getString("ChannelCreator", 2131558942));
        }
        else if (n == 2) {
            this.adminTextView.setText((CharSequence)LocaleController.getString("ChannelAdmin", 2131558926));
        }
        if (n != 0) {
            final CharSequence text = this.adminTextView.getText();
            final int n2 = (int)Math.ceil(this.adminTextView.getPaint().measureText(text, 0, text.length()));
            final SimpleTextView nameTextView = this.nameTextView;
            if (LocaleController.isRTL) {
                n = AndroidUtilities.dp(6.0f) + n2;
            }
            else {
                n = 0;
            }
            int n3;
            if (!LocaleController.isRTL) {
                n3 = n2 + AndroidUtilities.dp(6.0f);
            }
            else {
                n3 = 0;
            }
            nameTextView.setPadding(n, 0, n3, 0);
        }
        else {
            this.nameTextView.setPadding(0, 0, 0, 0);
        }
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
        String s2;
        if (visibility != 0) {
            boolean b = false;
            Label_0166: {
                Label_0163: {
                    if ((visibility & 0x2) != 0x0) {
                        if (this.lastAvatar == null || lastAvatar != null) {
                            final TLRPC.FileLocation lastAvatar2 = this.lastAvatar;
                            if (lastAvatar2 != null || lastAvatar == null || lastAvatar2 == null || lastAvatar == null || (lastAvatar2.volume_id == lastAvatar.volume_id && lastAvatar2.local_id == lastAvatar.local_id)) {
                                break Label_0163;
                            }
                        }
                        b = true;
                        break Label_0166;
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
            if (n == 0) {
                return;
            }
        }
        else {
            s2 = null;
        }
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
                if ((userName = s2) == null) {
                    userName = UserObject.getUserName((TLRPC.User)info);
                }
                this.lastName = userName;
            }
            else {
                String title;
                if ((title = s2) == null) {
                    title = info2.title;
                }
                this.lastName = title;
            }
            this.nameTextView.setText(this.lastName);
        }
        Label_0771: {
            if (this.currentStatus != null) {
                this.statusTextView.setTextColor(this.statusColor);
                this.statusTextView.setText(this.currentStatus);
            }
            else if (info != null) {
                if (((TLRPC.User)info).bot) {
                    this.statusTextView.setTextColor(this.statusColor);
                    if (!((TLRPC.User)info).bot_chat_history) {
                        final TextView adminTextView = this.adminTextView;
                        if (adminTextView == null || adminTextView.getVisibility() != 0) {
                            this.statusTextView.setText(LocaleController.getString("BotStatusCantRead", 2131558858));
                            break Label_0771;
                        }
                    }
                    this.statusTextView.setText(LocaleController.getString("BotStatusRead", 2131558859));
                }
                else {
                    if (((TLRPC.User)info).id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        final TLRPC.UserStatus status3 = ((TLRPC.User)info).status;
                        if (status3 == null || status3.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                            if (!MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(((TLRPC.User)info).id)) {
                                this.statusTextView.setTextColor(this.statusColor);
                                this.statusTextView.setText(LocaleController.formatUserStatus(this.currentAccount, (TLRPC.User)info));
                                break Label_0771;
                            }
                        }
                    }
                    this.statusTextView.setTextColor(this.statusOnlineColor);
                    this.statusTextView.setText(LocaleController.getString("Online", 2131560100));
                }
            }
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
        this.lastAvatar = lastAvatar;
        if (info != null) {
            this.avatarImageView.setImage(ImageLocation.getForUser((TLRPC.User)info, false), "50_50", this.avatarDrawable, info);
        }
        else if (info2 != null) {
            this.avatarImageView.setImage(ImageLocation.getForChat(info2, false), "50_50", this.avatarDrawable, info2);
        }
    }
}
