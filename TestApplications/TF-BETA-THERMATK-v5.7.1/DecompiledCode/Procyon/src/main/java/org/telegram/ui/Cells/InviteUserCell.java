// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class InviteUserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox2 checkBox;
    private ContactsController.Contact currentContact;
    private CharSequence currentName;
    private SimpleTextView nameTextView;
    private SimpleTextView statusTextView;
    
    public InviteUserCell(final Context context, final boolean b) {
        super(context);
        this.avatarDrawable = new AvatarDrawable();
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(24.0f));
        final BackupImageView avatarImageView = this.avatarImageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = 11.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 11.0f;
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(50, 50.0f, n2 | 0x30, n3, 11.0f, n4, 0.0f));
        (this.nameTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setTextSize(17);
        final SimpleTextView nameTextView = this.nameTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        nameTextView.setGravity(n5 | 0x30);
        final SimpleTextView nameTextView2 = this.nameTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 28.0f;
        }
        else {
            n7 = 72.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 72.0f;
        }
        else {
            n8 = 28.0f;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n6 | 0x30, n7, 14.0f, n8, 0.0f));
        (this.statusTextView = new SimpleTextView(context)).setTextSize(16);
        final SimpleTextView statusTextView = this.statusTextView;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        statusTextView.setGravity(n9 | 0x30);
        final SimpleTextView statusTextView2 = this.statusTextView;
        int n10;
        if (LocaleController.isRTL) {
            n10 = 5;
        }
        else {
            n10 = 3;
        }
        float n11;
        if (LocaleController.isRTL) {
            n11 = 28.0f;
        }
        else {
            n11 = 72.0f;
        }
        float n12;
        if (LocaleController.isRTL) {
            n12 = 72.0f;
        }
        else {
            n12 = 28.0f;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n10 | 0x30, n11, 39.0f, n12, 0.0f));
        if (b) {
            (this.checkBox = new CheckBox2(context)).setColor(null, "windowBackgroundWhite", "checkboxCheck");
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            final CheckBox2 checkBox = this.checkBox;
            int n13;
            if (LocaleController.isRTL) {
                n13 = n;
            }
            else {
                n13 = 3;
            }
            float n14;
            if (LocaleController.isRTL) {
                n14 = 0.0f;
            }
            else {
                n14 = 40.0f;
            }
            float n15;
            if (LocaleController.isRTL) {
                n15 = 39.0f;
            }
            else {
                n15 = 0.0f;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, n13 | 0x30, n14, 40.0f, n15, 0.0f));
        }
    }
    
    public ContactsController.Contact getContact() {
        return this.currentContact;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(72.0f), 1073741824));
    }
    
    public void recycle() {
        this.avatarImageView.getImageReceiver().cancelLoadImage();
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBox.setChecked(b, b2);
    }
    
    public void setUser(final ContactsController.Contact currentContact, final CharSequence currentName) {
        this.currentContact = currentContact;
        this.currentName = currentName;
        this.update(0);
    }
    
    public void update(int imported) {
        final ContactsController.Contact currentContact = this.currentContact;
        if (currentContact == null) {
            return;
        }
        this.avatarDrawable.setInfo(currentContact.contact_id, currentContact.first_name, currentContact.last_name, false);
        final CharSequence currentName = this.currentName;
        if (currentName != null) {
            this.nameTextView.setText(currentName, true);
        }
        else {
            final SimpleTextView nameTextView = this.nameTextView;
            final ContactsController.Contact currentContact2 = this.currentContact;
            nameTextView.setText(ContactsController.formatName(currentContact2.first_name, currentContact2.last_name));
        }
        this.statusTextView.setTag((Object)"windowBackgroundWhiteGrayText");
        this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        final ContactsController.Contact currentContact3 = this.currentContact;
        imported = currentContact3.imported;
        if (imported > 0) {
            this.statusTextView.setText(LocaleController.formatPluralString("TelegramContacts", imported));
        }
        else {
            this.statusTextView.setText(currentContact3.phones.get(0));
        }
        this.avatarImageView.setImageDrawable(this.avatarDrawable);
    }
}
