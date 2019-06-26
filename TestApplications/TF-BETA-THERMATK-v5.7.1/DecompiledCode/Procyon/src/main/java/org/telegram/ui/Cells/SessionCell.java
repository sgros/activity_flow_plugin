// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesController;
import java.util.Locale;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.widget.LinearLayout;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.Components.BackupImageView;
import android.widget.TextView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class SessionCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private int currentAccount;
    private TextView detailExTextView;
    private TextView detailTextView;
    private BackupImageView imageView;
    private TextView nameTextView;
    private boolean needDivider;
    private TextView onlineTextView;
    
    public SessionCell(final Context context, int n) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        final LinearLayout linearLayout = new LinearLayout(context);
        final int n2 = 0;
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        final int n3 = 15;
        final int n4 = 5;
        if (n == 1) {
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            final boolean isRTL = LocaleController.isRTL;
            final int n5 = 49;
            int n6;
            if (isRTL) {
                n6 = 15;
            }
            else {
                n6 = 49;
            }
            final float n7 = (float)n6;
            int n8;
            if (LocaleController.isRTL) {
                n8 = n5;
            }
            else {
                n8 = 15;
            }
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, n | 0x30, n7, 11.0f, (float)n8, 0.0f));
            (this.avatarDrawable = new AvatarDrawable()).setTextSize(AndroidUtilities.dp(10.0f));
            (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(10.0f));
            final BackupImageView imageView = this.imageView;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            int n9;
            if (LocaleController.isRTL) {
                n9 = 0;
            }
            else {
                n9 = 21;
            }
            final float n10 = (float)n9;
            int n11 = n2;
            if (LocaleController.isRTL) {
                n11 = 21;
            }
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(20, 20.0f, n | 0x30, n10, 13.0f, (float)n11, 0.0f));
        }
        else {
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            int n12;
            if (LocaleController.isRTL) {
                n12 = 15;
            }
            else {
                n12 = 21;
            }
            final float n13 = (float)n12;
            int n14 = n3;
            if (LocaleController.isRTL) {
                n14 = 21;
            }
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, n | 0x30, n13, 11.0f, (float)n14, 0.0f));
        }
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 16.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView nameTextView = this.nameTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        nameTextView.setGravity(n | 0x30);
        (this.onlineTextView = new TextView(context)).setTextSize(1, 14.0f);
        final TextView onlineTextView = this.onlineTextView;
        if (LocaleController.isRTL) {
            n = 3;
        }
        else {
            n = 5;
        }
        onlineTextView.setGravity(n | 0x30);
        if (LocaleController.isRTL) {
            linearLayout.addView((View)this.onlineTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 51, 0, 2, 0, 0));
            linearLayout.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 53, 10, 0, 0, 0));
        }
        else {
            linearLayout.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 51, 0, 0, 10, 0));
            linearLayout.addView((View)this.onlineTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 53, 0, 2, 0, 0));
        }
        (this.detailTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.detailTextView.setTextSize(1, 14.0f);
        this.detailTextView.setLines(1);
        this.detailTextView.setMaxLines(1);
        this.detailTextView.setSingleLine(true);
        this.detailTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView detailTextView = this.detailTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        detailTextView.setGravity(n | 0x30);
        final TextView detailTextView2 = this.detailTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        this.addView((View)detailTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n | 0x30, 21.0f, 36.0f, 21.0f, 0.0f));
        (this.detailExTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.detailExTextView.setTextSize(1, 14.0f);
        this.detailExTextView.setLines(1);
        this.detailExTextView.setMaxLines(1);
        this.detailExTextView.setSingleLine(true);
        this.detailExTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView detailExTextView = this.detailExTextView;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        detailExTextView.setGravity(n | 0x30);
        final TextView detailExTextView2 = this.detailExTextView;
        if (LocaleController.isRTL) {
            n = n4;
        }
        else {
            n = 3;
        }
        this.addView((View)detailExTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n | 0x30, 21.0f, 59.0f, 21.0f, 0.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(90.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setSession(final TLObject tlObject, final boolean needDivider) {
        this.needDivider = needDivider;
        if (tlObject instanceof TLRPC.TL_authorization) {
            final TLRPC.TL_authorization tl_authorization = (TLRPC.TL_authorization)tlObject;
            this.nameTextView.setText((CharSequence)String.format(Locale.US, "%s %s", tl_authorization.app_name, tl_authorization.app_version));
            if ((tl_authorization.flags & 0x1) != 0x0) {
                this.setTag((Object)"windowBackgroundWhiteValueText");
                this.onlineTextView.setText((CharSequence)LocaleController.getString("Online", 2131560100));
                this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
            }
            else {
                this.setTag((Object)"windowBackgroundWhiteGrayText3");
                this.onlineTextView.setText((CharSequence)LocaleController.stringForMessageListDate(tl_authorization.date_active));
                this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
            }
            final StringBuilder text = new StringBuilder();
            if (tl_authorization.ip.length() != 0) {
                text.append(tl_authorization.ip);
            }
            if (tl_authorization.country.length() != 0) {
                if (text.length() != 0) {
                    text.append(" ");
                }
                text.append("\u2014 ");
                text.append(tl_authorization.country);
            }
            this.detailExTextView.setText((CharSequence)text);
            final StringBuilder text2 = new StringBuilder();
            if (tl_authorization.device_model.length() != 0) {
                if (text2.length() != 0) {
                    text2.append(", ");
                }
                text2.append(tl_authorization.device_model);
            }
            if (tl_authorization.system_version.length() != 0 || tl_authorization.platform.length() != 0) {
                if (text2.length() != 0) {
                    text2.append(", ");
                }
                if (tl_authorization.platform.length() != 0) {
                    text2.append(tl_authorization.platform);
                }
                if (tl_authorization.system_version.length() != 0) {
                    if (tl_authorization.platform.length() != 0) {
                        text2.append(" ");
                    }
                    text2.append(tl_authorization.system_version);
                }
            }
            if (!tl_authorization.official_app) {
                if (text2.length() != 0) {
                    text2.append(", ");
                }
                text2.append(LocaleController.getString("UnofficialApp", 2131560940));
                text2.append(" (ID: ");
                text2.append(tl_authorization.api_id);
                text2.append(")");
            }
            this.detailTextView.setText((CharSequence)text2);
        }
        else if (tlObject instanceof TLRPC.TL_webAuthorization) {
            final TLRPC.TL_webAuthorization tl_webAuthorization = (TLRPC.TL_webAuthorization)tlObject;
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(tl_webAuthorization.bot_id);
            this.nameTextView.setText((CharSequence)tl_webAuthorization.domain);
            String firstName;
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                firstName = UserObject.getFirstName(user);
                this.imageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
            }
            else {
                firstName = "";
            }
            this.setTag((Object)"windowBackgroundWhiteGrayText3");
            this.onlineTextView.setText((CharSequence)LocaleController.stringForMessageListDate(tl_webAuthorization.date_active));
            this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
            final StringBuilder text3 = new StringBuilder();
            if (tl_webAuthorization.ip.length() != 0) {
                text3.append(tl_webAuthorization.ip);
            }
            if (tl_webAuthorization.region.length() != 0) {
                if (text3.length() != 0) {
                    text3.append(" ");
                }
                text3.append("\u2014 ");
                text3.append(tl_webAuthorization.region);
            }
            this.detailExTextView.setText((CharSequence)text3);
            final StringBuilder text4 = new StringBuilder();
            if (!TextUtils.isEmpty((CharSequence)firstName)) {
                text4.append(firstName);
            }
            if (tl_webAuthorization.browser.length() != 0) {
                if (text4.length() != 0) {
                    text4.append(", ");
                }
                text4.append(tl_webAuthorization.browser);
            }
            if (tl_webAuthorization.platform.length() != 0) {
                if (text4.length() != 0) {
                    text4.append(", ");
                }
                text4.append(tl_webAuthorization.platform);
            }
            this.detailTextView.setText((CharSequence)text4);
        }
    }
}
