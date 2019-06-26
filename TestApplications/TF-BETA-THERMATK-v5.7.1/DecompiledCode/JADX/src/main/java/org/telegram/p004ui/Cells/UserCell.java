package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.SimpleTextView;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.CheckBox;
import org.telegram.p004ui.Components.CheckBoxSquare;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.NotificationsSettingsActivity.NotificationException;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Cells.UserCell */
public class UserCell extends FrameLayout {
    private TextView adminTextView;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private BackupImageView avatarImageView;
    private CheckBox checkBox;
    private CheckBoxSquare checkBoxBig;
    private int currentAccount = UserConfig.selectedAccount;
    private int currentDrawable;
    private int currentId;
    private CharSequence currentName;
    private TLObject currentObject;
    private CharSequence currentStatus;
    private EncryptedChat encryptedChat;
    private ImageView imageView;
    private FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private SimpleTextView nameTextView;
    private boolean needDivider;
    private int statusColor = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText);
    private int statusOnlineColor = Theme.getColor(Theme.key_windowBackgroundWhiteBlueText);
    private SimpleTextView statusTextView;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public UserCell(Context context, int i, int i2, boolean z) {
        int i3;
        Context context2 = context;
        int i4 = i2;
        super(context);
        this.avatarImageView = new BackupImageView(context2);
        this.avatarImageView.setRoundRadius(AndroidUtilities.m26dp(24.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(46, 46.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : (float) (i + 7), 6.0f, LocaleController.isRTL ? (float) (i + 7) : 0.0f, 0.0f));
        this.nameTextView = new SimpleTextView(context2);
        this.nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setTextSize(16);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        SimpleTextView simpleTextView = this.nameTextView;
        int i5 = (LocaleController.isRTL ? 5 : 3) | 48;
        int i6 = 18;
        if (LocaleController.isRTL) {
            i3 = (i4 == 2 ? 18 : 0) + 28;
        } else {
            i3 = i + 64;
        }
        float f = (float) i3;
        if (LocaleController.isRTL) {
            i6 = i + 64;
        } else {
            if (i4 != 2) {
                i6 = 0;
            }
            i6 += 28;
        }
        addView(simpleTextView, LayoutHelper.createFrame(-1, 20.0f, i5, f, 10.0f, (float) i6, 0.0f));
        this.statusTextView = new SimpleTextView(context2);
        this.statusTextView.setTextSize(15);
        this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.statusTextView, LayoutHelper.createFrame(-1, 20.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 28.0f : (float) (i + 64), 32.0f, LocaleController.isRTL ? (float) (i + 64) : 28.0f, 0.0f));
        this.imageView = new ImageView(context2);
        this.imageView.setScaleType(ScaleType.CENTER);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), Mode.MULTIPLY));
        this.imageView.setVisibility(8);
        addView(this.imageView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 16, LocaleController.isRTL ? 0.0f : 16.0f, 0.0f, LocaleController.isRTL ? 16.0f : 0.0f, 0.0f));
        if (i4 == 2) {
            this.checkBoxBig = new CheckBoxSquare(context2, false);
            addView(this.checkBoxBig, LayoutHelper.createFrame(18, 18.0f, (LocaleController.isRTL ? 3 : 5) | 16, LocaleController.isRTL ? 19.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 19.0f, 0.0f));
        } else if (i4 == 1) {
            this.checkBox = new CheckBox(context2, C1067R.C1065drawable.round_check2);
            this.checkBox.setVisibility(4);
            this.checkBox.setColor(Theme.getColor(Theme.key_checkbox), Theme.getColor(Theme.key_checkboxCheck));
            addView(this.checkBox, LayoutHelper.createFrame(22, 22.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : (float) (i + 37), 40.0f, LocaleController.isRTL ? (float) (i + 37) : 0.0f, 0.0f));
        }
        if (z) {
            this.adminTextView = new TextView(context2);
            this.adminTextView.setTextSize(1, 14.0f);
            this.adminTextView.setTextColor(Theme.getColor(Theme.key_profile_creatorIcon));
            addView(this.adminTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 48, LocaleController.isRTL ? 23.0f : 0.0f, 10.0f, LocaleController.isRTL ? 0.0f : 23.0f, 0.0f));
        }
        setFocusable(true);
    }

    public void setAvatarPadding(int i) {
        int i2;
        float f;
        LayoutParams layoutParams = (LayoutParams) this.avatarImageView.getLayoutParams();
        float f2 = 0.0f;
        layoutParams.leftMargin = AndroidUtilities.m26dp(LocaleController.isRTL ? 0.0f : (float) (i + 7));
        layoutParams.rightMargin = AndroidUtilities.m26dp(LocaleController.isRTL ? (float) (i + 7) : 0.0f);
        this.avatarImageView.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) this.nameTextView.getLayoutParams();
        int i3 = 18;
        if (LocaleController.isRTL) {
            i2 = (this.checkBoxBig != null ? 18 : 0) + 28;
        } else {
            i2 = i + 64;
        }
        layoutParams.leftMargin = AndroidUtilities.m26dp((float) i2);
        if (LocaleController.isRTL) {
            f = (float) (i + 64);
        } else {
            if (this.checkBoxBig == null) {
                i3 = 0;
            }
            f = (float) (i3 + 28);
        }
        layoutParams.rightMargin = AndroidUtilities.m26dp(f);
        layoutParams = (LayoutParams) this.statusTextView.getLayoutParams();
        float f3 = 28.0f;
        layoutParams.leftMargin = AndroidUtilities.m26dp(LocaleController.isRTL ? 28.0f : (float) (i + 64));
        if (LocaleController.isRTL) {
            f3 = (float) (i + 64);
        }
        layoutParams.rightMargin = AndroidUtilities.m26dp(f3);
        CheckBox checkBox = this.checkBox;
        if (checkBox != null) {
            layoutParams = (LayoutParams) checkBox.getLayoutParams();
            layoutParams.leftMargin = AndroidUtilities.m26dp(LocaleController.isRTL ? 0.0f : (float) (i + 37));
            if (LocaleController.isRTL) {
                f2 = (float) (i + 37);
            }
            layoutParams.rightMargin = AndroidUtilities.m26dp(f2);
        }
    }

    public void setIsAdmin(int i) {
        TextView textView = this.adminTextView;
        if (textView != null) {
            textView.setVisibility(i != 0 ? 0 : 8);
            if (i == 1) {
                this.adminTextView.setText(LocaleController.getString("ChannelCreator", C1067R.string.ChannelCreator));
            } else if (i == 2) {
                this.adminTextView.setText(LocaleController.getString("ChannelAdmin", C1067R.string.ChannelAdmin));
            }
            if (i != 0) {
                CharSequence text = this.adminTextView.getText();
                i = (int) Math.ceil((double) this.adminTextView.getPaint().measureText(text, 0, text.length()));
                this.nameTextView.setPadding(LocaleController.isRTL ? AndroidUtilities.m26dp(6.0f) + i : 0, 0, !LocaleController.isRTL ? i + AndroidUtilities.m26dp(6.0f) : 0, 0);
            } else {
                this.nameTextView.setPadding(0, 0, 0, 0);
            }
        }
    }

    public void setData(TLObject tLObject, CharSequence charSequence, CharSequence charSequence2, int i) {
        setData(tLObject, null, charSequence, charSequence2, i, false);
    }

    public void setData(TLObject tLObject, CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        setData(tLObject, null, charSequence, charSequence2, i, z);
    }

    public void setData(TLObject tLObject, EncryptedChat encryptedChat, CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        if (tLObject == null && charSequence == null && charSequence2 == null) {
            this.currentStatus = null;
            this.currentName = null;
            this.currentObject = null;
            String str = "";
            this.nameTextView.setText(str);
            this.statusTextView.setText(str);
            this.avatarImageView.setImageDrawable(null);
            return;
        }
        this.encryptedChat = encryptedChat;
        this.currentStatus = charSequence2;
        this.currentName = charSequence;
        this.currentObject = tLObject;
        this.currentDrawable = i;
        this.needDivider = z;
        setWillNotDraw(this.needDivider ^ 1);
        update(0);
    }

    public void setException(NotificationException notificationException, CharSequence charSequence, boolean z) {
        String string;
        boolean z2 = notificationException.hasCustom;
        int i = notificationException.notify;
        int i2 = notificationException.muteUntil;
        String str = "NotificationsCustom";
        String str2 = "NotificationsUnmuted";
        int i3 = 0;
        if (i != 3 || i2 == Integer.MAX_VALUE) {
            if (i == 0 || i == 1) {
                i3 = 1;
            }
            string = (i3 == 0 || !z2) ? i3 != 0 ? LocaleController.getString(str2, C1067R.string.NotificationsUnmuted) : LocaleController.getString("NotificationsMuted", C1067R.string.NotificationsMuted) : LocaleController.getString(str, C1067R.string.NotificationsCustom);
        } else {
            i2 -= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            if (i2 <= 0) {
                string = z2 ? LocaleController.getString(str, C1067R.string.NotificationsCustom) : LocaleController.getString(str2, C1067R.string.NotificationsUnmuted);
            } else {
                String str3 = "WillUnmuteIn";
                Object[] objArr;
                if (i2 < 3600) {
                    objArr = new Object[1];
                    objArr[0] = LocaleController.formatPluralString("Minutes", i2 / 60);
                    string = LocaleController.formatString(str3, C1067R.string.WillUnmuteIn, objArr);
                } else if (i2 < 86400) {
                    objArr = new Object[1];
                    objArr[0] = LocaleController.formatPluralString("Hours", (int) Math.ceil((double) ((((float) i2) / 60.0f) / 60.0f)));
                    string = LocaleController.formatString(str3, C1067R.string.WillUnmuteIn, objArr);
                } else if (i2 < 31536000) {
                    objArr = new Object[1];
                    objArr[0] = LocaleController.formatPluralString("Days", (int) Math.ceil((double) (((((float) i2) / 60.0f) / 60.0f) / 24.0f)));
                    string = LocaleController.formatString(str3, C1067R.string.WillUnmuteIn, objArr);
                } else {
                    string = null;
                }
            }
        }
        if (string == null) {
            string = LocaleController.getString("NotificationsOff", C1067R.string.NotificationsOff);
        }
        String str4 = string;
        long j = notificationException.did;
        int i4 = (int) j;
        i = (int) (j >> 32);
        User user;
        if (i4 == 0) {
            EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(i));
            if (encryptedChat != null) {
                user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(encryptedChat.user_id));
                if (user != null) {
                    setData(user, encryptedChat, charSequence, str4, 0, false);
                }
            }
        } else if (i4 > 0) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(i4));
            if (user != null) {
                setData(user, null, charSequence, str4, 0, z);
            }
        } else {
            Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-i4));
            if (chat != null) {
                setData(chat, null, charSequence, str4, 0, z);
            }
        }
    }

    public void setNameTypeface(Typeface typeface) {
        this.nameTextView.setTypeface(typeface);
    }

    public void setCurrentId(int i) {
        this.currentId = i;
    }

    public void setChecked(boolean z, boolean z2) {
        CheckBox checkBox = this.checkBox;
        if (checkBox != null) {
            if (checkBox.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(z, z2);
            return;
        }
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            if (checkBoxSquare.getVisibility() != 0) {
                this.checkBoxBig.setVisibility(0);
            }
            this.checkBoxBig.setChecked(z, z2);
        }
    }

    public void setCheckDisabled(boolean z) {
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.setDisabled(z);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(58.0f) + this.needDivider, 1073741824));
    }

    public void setStatusColors(int i, int i2) {
        this.statusColor = i;
        this.statusOnlineColor = i2;
    }

    public void invalidate() {
        super.invalidate();
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        if (checkBoxSquare != null) {
            checkBoxSquare.invalidate();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x008a A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01b0  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x01cd  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01b0  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x01cd  */
    /* JADX WARNING: Missing block: B:29:0x004d, code skipped:
            if (r5.local_id != r1.local_id) goto L_0x004f;
     */
    public void update(int r13) {
        /*
        r12 = this;
        r0 = r12.currentObject;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.User;
        r2 = 0;
        if (r1 == 0) goto L_0x0013;
    L_0x0007:
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
        r1 = r0.photo;
        if (r1 == 0) goto L_0x0011;
    L_0x000d:
        r1 = r1.photo_small;
        r3 = r2;
        goto L_0x0029;
    L_0x0011:
        r1 = r2;
        goto L_0x0028;
    L_0x0013:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.Chat;
        if (r1 == 0) goto L_0x0026;
    L_0x0017:
        r0 = (org.telegram.tgnet.TLRPC.Chat) r0;
        r1 = r0.photo;
        if (r1 == 0) goto L_0x0022;
    L_0x001d:
        r1 = r1.photo_small;
        r3 = r0;
        r0 = r2;
        goto L_0x0029;
    L_0x0022:
        r3 = r0;
        r0 = r2;
        r1 = r0;
        goto L_0x0029;
    L_0x0026:
        r0 = r2;
        r1 = r0;
    L_0x0028:
        r3 = r1;
    L_0x0029:
        r4 = 0;
        if (r13 == 0) goto L_0x008b;
    L_0x002c:
        r5 = r13 & 2;
        r6 = 1;
        if (r5 == 0) goto L_0x0051;
    L_0x0031:
        r5 = r12.lastAvatar;
        if (r5 == 0) goto L_0x0037;
    L_0x0035:
        if (r1 == 0) goto L_0x004f;
    L_0x0037:
        r5 = r12.lastAvatar;
        if (r5 != 0) goto L_0x0051;
    L_0x003b:
        if (r1 == 0) goto L_0x0051;
    L_0x003d:
        if (r5 == 0) goto L_0x0051;
    L_0x003f:
        if (r1 == 0) goto L_0x0051;
    L_0x0041:
        r7 = r5.volume_id;
        r9 = r1.volume_id;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 != 0) goto L_0x004f;
    L_0x0049:
        r5 = r5.local_id;
        r7 = r1.local_id;
        if (r5 == r7) goto L_0x0051;
    L_0x004f:
        r5 = 1;
        goto L_0x0052;
    L_0x0051:
        r5 = 0;
    L_0x0052:
        if (r0 == 0) goto L_0x0067;
    L_0x0054:
        if (r5 != 0) goto L_0x0067;
    L_0x0056:
        r7 = r13 & 4;
        if (r7 == 0) goto L_0x0067;
    L_0x005a:
        r7 = r0.status;
        if (r7 == 0) goto L_0x0061;
    L_0x005e:
        r7 = r7.expires;
        goto L_0x0062;
    L_0x0061:
        r7 = 0;
    L_0x0062:
        r8 = r12.lastStatus;
        if (r7 == r8) goto L_0x0067;
    L_0x0066:
        r5 = 1;
    L_0x0067:
        if (r5 != 0) goto L_0x0087;
    L_0x0069:
        r7 = r12.currentName;
        if (r7 != 0) goto L_0x0087;
    L_0x006d:
        r7 = r12.lastName;
        if (r7 == 0) goto L_0x0087;
    L_0x0071:
        r13 = r13 & r6;
        if (r13 == 0) goto L_0x0087;
    L_0x0074:
        if (r0 == 0) goto L_0x007b;
    L_0x0076:
        r13 = org.telegram.messenger.UserObject.getUserName(r0);
        goto L_0x007d;
    L_0x007b:
        r13 = r3.title;
    L_0x007d:
        r7 = r12.lastName;
        r7 = r13.equals(r7);
        if (r7 != 0) goto L_0x0088;
    L_0x0085:
        r5 = 1;
        goto L_0x0088;
    L_0x0087:
        r13 = r2;
    L_0x0088:
        if (r5 != 0) goto L_0x008c;
    L_0x008a:
        return;
    L_0x008b:
        r13 = r2;
    L_0x008c:
        if (r0 == 0) goto L_0x009f;
    L_0x008e:
        r5 = r12.avatarDrawable;
        r5.setInfo(r0);
        r5 = r0.status;
        if (r5 == 0) goto L_0x009c;
    L_0x0097:
        r5 = r5.expires;
        r12.lastStatus = r5;
        goto L_0x00c0;
    L_0x009c:
        r12.lastStatus = r4;
        goto L_0x00c0;
    L_0x009f:
        if (r3 == 0) goto L_0x00a7;
    L_0x00a1:
        r5 = r12.avatarDrawable;
        r5.setInfo(r3);
        goto L_0x00c0;
    L_0x00a7:
        r5 = r12.currentName;
        if (r5 == 0) goto L_0x00b7;
    L_0x00ab:
        r6 = r12.avatarDrawable;
        r7 = r12.currentId;
        r5 = r5.toString();
        r6.setInfo(r7, r5, r2, r4);
        goto L_0x00c0;
    L_0x00b7:
        r5 = r12.avatarDrawable;
        r6 = r12.currentId;
        r7 = "#";
        r5.setInfo(r6, r7, r2, r4);
    L_0x00c0:
        r5 = r12.currentName;
        if (r5 == 0) goto L_0x00cc;
    L_0x00c4:
        r12.lastName = r2;
        r13 = r12.nameTextView;
        r13.setText(r5);
        goto L_0x00e4;
    L_0x00cc:
        if (r0 == 0) goto L_0x00d7;
    L_0x00ce:
        if (r13 != 0) goto L_0x00d4;
    L_0x00d0:
        r13 = org.telegram.messenger.UserObject.getUserName(r0);
    L_0x00d4:
        r12.lastName = r13;
        goto L_0x00dd;
    L_0x00d7:
        if (r13 != 0) goto L_0x00db;
    L_0x00d9:
        r13 = r3.title;
    L_0x00db:
        r12.lastName = r13;
    L_0x00dd:
        r13 = r12.nameTextView;
        r2 = r12.lastName;
        r13.setText(r2);
    L_0x00e4:
        r13 = r12.currentStatus;
        if (r13 == 0) goto L_0x00f8;
    L_0x00e8:
        r13 = r12.statusTextView;
        r2 = r12.statusColor;
        r13.setTextColor(r2);
        r13 = r12.statusTextView;
        r2 = r12.currentStatus;
        r13.setText(r2);
        goto L_0x018f;
    L_0x00f8:
        if (r0 == 0) goto L_0x018f;
    L_0x00fa:
        r13 = r0.bot;
        if (r13 == 0) goto L_0x0132;
    L_0x00fe:
        r13 = r12.statusTextView;
        r2 = r12.statusColor;
        r13.setTextColor(r2);
        r13 = r0.bot_chat_history;
        if (r13 != 0) goto L_0x0123;
    L_0x0109:
        r13 = r12.adminTextView;
        if (r13 == 0) goto L_0x0114;
    L_0x010d:
        r13 = r13.getVisibility();
        if (r13 != 0) goto L_0x0114;
    L_0x0113:
        goto L_0x0123;
    L_0x0114:
        r13 = r12.statusTextView;
        r2 = 2131558858; // 0x7f0d01ca float:1.8743044E38 double:1.053130004E-314;
        r5 = "BotStatusCantRead";
        r2 = org.telegram.messenger.LocaleController.getString(r5, r2);
        r13.setText(r2);
        goto L_0x018f;
    L_0x0123:
        r13 = r12.statusTextView;
        r2 = 2131558859; // 0x7f0d01cb float:1.8743046E38 double:1.0531300043E-314;
        r5 = "BotStatusRead";
        r2 = org.telegram.messenger.LocaleController.getString(r5, r2);
        r13.setText(r2);
        goto L_0x018f;
    L_0x0132:
        r13 = r0.f534id;
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);
        r2 = r2.getClientUserId();
        if (r13 == r2) goto L_0x017a;
    L_0x0140:
        r13 = r0.status;
        if (r13 == 0) goto L_0x0152;
    L_0x0144:
        r13 = r13.expires;
        r2 = r12.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        if (r13 > r2) goto L_0x017a;
    L_0x0152:
        r13 = r12.currentAccount;
        r13 = org.telegram.messenger.MessagesController.getInstance(r13);
        r13 = r13.onlinePrivacy;
        r2 = r0.f534id;
        r2 = java.lang.Integer.valueOf(r2);
        r13 = r13.containsKey(r2);
        if (r13 == 0) goto L_0x0167;
    L_0x0166:
        goto L_0x017a;
    L_0x0167:
        r13 = r12.statusTextView;
        r2 = r12.statusColor;
        r13.setTextColor(r2);
        r13 = r12.statusTextView;
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.LocaleController.formatUserStatus(r2, r0);
        r13.setText(r2);
        goto L_0x018f;
    L_0x017a:
        r13 = r12.statusTextView;
        r2 = r12.statusOnlineColor;
        r13.setTextColor(r2);
        r13 = r12.statusTextView;
        r2 = 2131560100; // 0x7f0d06a4 float:1.8745563E38 double:1.0531306175E-314;
        r5 = "Online";
        r2 = org.telegram.messenger.LocaleController.getString(r5, r2);
        r13.setText(r2);
    L_0x018f:
        r13 = r12.imageView;
        r13 = r13.getVisibility();
        r2 = 8;
        if (r13 != 0) goto L_0x019d;
    L_0x0199:
        r13 = r12.currentDrawable;
        if (r13 == 0) goto L_0x01a9;
    L_0x019d:
        r13 = r12.imageView;
        r13 = r13.getVisibility();
        if (r13 != r2) goto L_0x01bb;
    L_0x01a5:
        r13 = r12.currentDrawable;
        if (r13 == 0) goto L_0x01bb;
    L_0x01a9:
        r13 = r12.imageView;
        r5 = r12.currentDrawable;
        if (r5 != 0) goto L_0x01b0;
    L_0x01af:
        goto L_0x01b1;
    L_0x01b0:
        r2 = 0;
    L_0x01b1:
        r13.setVisibility(r2);
        r13 = r12.imageView;
        r2 = r12.currentDrawable;
        r13.setImageResource(r2);
    L_0x01bb:
        r12.lastAvatar = r1;
        r13 = "50_50";
        if (r0 == 0) goto L_0x01cd;
    L_0x01c1:
        r1 = r12.avatarImageView;
        r2 = org.telegram.messenger.ImageLocation.getForUser(r0, r4);
        r3 = r12.avatarDrawable;
        r1.setImage(r2, r13, r3, r0);
        goto L_0x01da;
    L_0x01cd:
        if (r3 == 0) goto L_0x01da;
    L_0x01cf:
        r0 = r12.avatarImageView;
        r1 = org.telegram.messenger.ImageLocation.getForChat(r3, r4);
        r2 = r12.avatarDrawable;
        r0.setImage(r1, r13, r2, r3);
    L_0x01da:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.UserCell.update(int):void");
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : (float) AndroidUtilities.m26dp(68.0f), (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.m26dp(68.0f) : 0)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        CheckBoxSquare checkBoxSquare = this.checkBoxBig;
        String str = "android.widget.CheckBox";
        if (checkBoxSquare == null || checkBoxSquare.getVisibility() != 0) {
            CheckBox checkBox = this.checkBox;
            if (checkBox != null && checkBox.getVisibility() == 0) {
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(this.checkBox.isChecked());
                accessibilityNodeInfo.setClassName(str);
                return;
            }
            return;
        }
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.checkBoxBig.isChecked());
        accessibilityNodeInfo.setClassName(str);
    }
}