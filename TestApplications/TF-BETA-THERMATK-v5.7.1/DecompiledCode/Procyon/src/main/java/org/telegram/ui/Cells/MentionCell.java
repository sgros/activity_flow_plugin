// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.UserObject;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.LinearLayout;

public class MentionCell extends LinearLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView imageView;
    private TextView nameTextView;
    private TextView usernameTextView;
    
    public MentionCell(final Context context) {
        super(context);
        this.setOrientation(0);
        (this.avatarDrawable = new AvatarDrawable()).setTextSize(AndroidUtilities.dp(12.0f));
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(14.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(28, 28, 12.0f, 4.0f, 0.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 15.0f);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setGravity(3);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16, 12, 0, 0, 0));
        (this.usernameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.usernameTextView.setTextSize(1, 15.0f);
        this.usernameTextView.setSingleLine(true);
        this.usernameTextView.setGravity(3);
        this.usernameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.usernameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16, 12, 0, 8, 0));
    }
    
    public void invalidate() {
        super.invalidate();
        this.nameTextView.invalidate();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36.0f), 1073741824));
    }
    
    public void setBotCommand(final String text, final String s, final TLRPC.User info) {
        if (info != null) {
            this.imageView.setVisibility(0);
            this.avatarDrawable.setInfo(info);
            final TLRPC.UserProfilePhoto photo = info.photo;
            if (photo != null && photo.photo_small != null) {
                this.imageView.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
            }
            else {
                this.imageView.setImageDrawable(this.avatarDrawable);
            }
        }
        else {
            this.imageView.setVisibility(4);
        }
        this.usernameTextView.setVisibility(0);
        this.nameTextView.setText((CharSequence)text);
        final TextView usernameTextView = this.usernameTextView;
        usernameTextView.setText(Emoji.replaceEmoji(s, usernameTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
    }
    
    public void setEmojiSuggestion(final DataQuery.KeywordResult keywordResult) {
        this.imageView.setVisibility(4);
        this.usernameTextView.setVisibility(4);
        final StringBuilder sb = new StringBuilder(keywordResult.emoji.length() + keywordResult.keyword.length() + 4);
        sb.append(keywordResult.emoji);
        sb.append("   :");
        sb.append(keywordResult.keyword);
        final TextView nameTextView = this.nameTextView;
        nameTextView.setText(Emoji.replaceEmoji(sb, nameTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
    }
    
    public void setIsDarkTheme(final boolean b) {
        if (b) {
            this.nameTextView.setTextColor(-1);
            this.usernameTextView.setTextColor(-4473925);
        }
        else {
            this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        }
    }
    
    public void setText(final String text) {
        this.imageView.setVisibility(4);
        this.usernameTextView.setVisibility(4);
        this.nameTextView.setText((CharSequence)text);
    }
    
    public void setUser(final TLRPC.User info) {
        if (info == null) {
            this.nameTextView.setText((CharSequence)"");
            this.usernameTextView.setText((CharSequence)"");
            this.imageView.setImageDrawable(null);
            return;
        }
        this.avatarDrawable.setInfo(info);
        final TLRPC.UserProfilePhoto photo = info.photo;
        if (photo != null && photo.photo_small != null) {
            this.imageView.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
        }
        else {
            this.imageView.setImageDrawable(this.avatarDrawable);
        }
        this.nameTextView.setText((CharSequence)UserObject.getUserName(info));
        if (info.username != null) {
            final TextView usernameTextView = this.usernameTextView;
            final StringBuilder sb = new StringBuilder();
            sb.append("@");
            sb.append(info.username);
            usernameTextView.setText((CharSequence)sb.toString());
        }
        else {
            this.usernameTextView.setText((CharSequence)"");
        }
        this.imageView.setVisibility(0);
        this.usernameTextView.setVisibility(0);
    }
}
