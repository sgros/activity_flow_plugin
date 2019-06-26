package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Cells.AccountSelectCell */
public class AccountSelectCell extends FrameLayout {
    private int accountNumber;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageView checkImageView;
    private BackupImageView imageView;
    private TextView textView;

    public AccountSelectCell(Context context) {
        super(context);
        this.avatarDrawable.setTextSize(AndroidUtilities.m26dp(12.0f));
        this.imageView = new BackupImageView(context);
        this.imageView.setRoundRadius(AndroidUtilities.m26dp(18.0f));
        addView(this.imageView, LayoutHelper.createFrame(36, 36.0f, 51, 10.0f, 10.0f, 0.0f, 0.0f));
        this.textView = new TextView(context);
        this.textView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(19);
        this.textView.setEllipsize(TruncateAt.END);
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, 51, 61.0f, 0.0f, 56.0f, 0.0f));
        this.checkImageView = new ImageView(context);
        this.checkImageView.setImageResource(C1067R.C1065drawable.account_check);
        this.checkImageView.setScaleType(ScaleType.CENTER);
        this.checkImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_menuItemCheck), Mode.MULTIPLY));
        addView(this.checkImageView, LayoutHelper.createFrame(40, -1.0f, 53, 0.0f, 0.0f, 6.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(56.0f), 1073741824));
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.textView.setTextColor(Theme.getColor(Theme.key_chats_menuItemText));
    }

    public void setAccount(int i, boolean z) {
        this.accountNumber = i;
        Object currentUser = UserConfig.getInstance(this.accountNumber).getCurrentUser();
        this.avatarDrawable.setInfo((User) currentUser);
        this.textView.setText(ContactsController.formatName(currentUser.first_name, currentUser.last_name));
        this.imageView.getImageReceiver().setCurrentAccount(i);
        int i2 = 0;
        this.imageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", this.avatarDrawable, currentUser);
        ImageView imageView = this.checkImageView;
        if (!(z && i == UserConfig.selectedAccount)) {
            i2 = 4;
        }
        imageView.setVisibility(i2);
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }
}