// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.ui.Components.URLSpanNoUnderline;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.MessagesController;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.view.View$OnClickListener;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import android.widget.ImageView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class AdminedChannelCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private int currentAccount;
    private TLRPC.Chat currentChannel;
    private ImageView deleteButton;
    private boolean isLast;
    private SimpleTextView nameTextView;
    private SimpleTextView statusTextView;
    
    public AdminedChannelCell(final Context context, final View$OnClickListener onClickListener) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
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
            n3 = 12.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 12.0f;
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n2 | 0x30, n3, 12.0f, n4, 0.0f));
        (this.nameTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
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
            n7 = 62.0f;
        }
        else {
            n7 = 73.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 73.0f;
        }
        else {
            n8 = 62.0f;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n6 | 0x30, n7, 15.5f, n8, 0.0f));
        (this.statusTextView = new SimpleTextView(context)).setTextSize(14);
        this.statusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.statusTextView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
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
            n11 = 62.0f;
        }
        else {
            n11 = 73.0f;
        }
        float n12;
        if (LocaleController.isRTL) {
            n12 = 73.0f;
        }
        else {
            n12 = 62.0f;
        }
        this.addView((View)statusTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n10 | 0x30, n11, 38.5f, n12, 0.0f));
        (this.deleteButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.deleteButton.setImageResource(2131165652);
        this.deleteButton.setOnClickListener(onClickListener);
        this.deleteButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText"), PorterDuff$Mode.MULTIPLY));
        final ImageView deleteButton = this.deleteButton;
        int n13 = n;
        if (LocaleController.isRTL) {
            n13 = 3;
        }
        float n14;
        if (LocaleController.isRTL) {
            n14 = 7.0f;
        }
        else {
            n14 = 0.0f;
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = 0.0f;
        }
        else {
            n15 = 7.0f;
        }
        this.addView((View)deleteButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n13 | 0x30, n14, 12.0f, n15, 0.0f));
    }
    
    public TLRPC.Chat getCurrentChannel() {
        return this.currentChannel;
    }
    
    public ImageView getDeleteButton() {
        return this.deleteButton;
    }
    
    public SimpleTextView getNameTextView() {
        return this.nameTextView;
    }
    
    public SimpleTextView getStatusTextView() {
        return this.statusTextView;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onMeasure(int n, int measureSpec) {
        measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824);
        if (this.isLast) {
            n = 12;
        }
        else {
            n = 0;
        }
        super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(n + 60)), 1073741824));
    }
    
    public void setChannel(final TLRPC.Chat chat, final boolean isLast) {
        final StringBuilder sb = new StringBuilder();
        sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
        sb.append("/");
        final String string = sb.toString();
        this.currentChannel = chat;
        this.avatarDrawable.setInfo(chat);
        this.nameTextView.setText(chat.title);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(chat.username);
        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)sb2.toString());
        text.setSpan((Object)new URLSpanNoUnderline(""), string.length(), text.length(), 33);
        this.statusTextView.setText((CharSequence)text);
        this.avatarImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, this.currentChannel);
        this.isLast = isLast;
    }
    
    public void update() {
        this.avatarDrawable.setInfo(this.currentChannel);
        this.avatarImageView.invalidate();
    }
}
