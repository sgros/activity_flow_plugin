// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.tgnet.TLRPC;
import android.view.View$OnClickListener;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.widget.LinearLayout;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.ScrollView;
import android.view.View$OnTouchListener;
import android.widget.RelativeLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChangePhoneHelpActivity extends BaseFragment
{
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        final TLRPC.User currentUser = UserConfig.getInstance(super.currentAccount).getCurrentUser();
        String title = null;
        Label_0100: {
            if (currentUser != null) {
                final String phone = currentUser.phone;
                if (phone != null && phone.length() != 0) {
                    final PhoneFormat instance = PhoneFormat.getInstance();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(currentUser.phone);
                    title = instance.format(sb.toString());
                    break Label_0100;
                }
            }
            title = LocaleController.getString("NumberUnknown", 2131560096);
        }
        super.actionBar.setTitle(title);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChangePhoneHelpActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new RelativeLayout(context)).setOnTouchListener((View$OnTouchListener)_$$Lambda$ChangePhoneHelpActivity$At2SQoCuyPKWr2c7pksPfUQl31M.INSTANCE);
        final RelativeLayout relativeLayout = (RelativeLayout)super.fragmentView;
        final ScrollView scrollView = new ScrollView(context);
        relativeLayout.addView((View)scrollView);
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)scrollView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -2;
        layoutParams.addRule(15, -1);
        scrollView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f));
        scrollView.addView((View)linearLayout);
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)linearLayout.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -2;
        linearLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        (this.imageView = new ImageView(context)).setImageResource(2131165740);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("changephoneinfo_image"), PorterDuff$Mode.MULTIPLY));
        linearLayout.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1));
        (this.textView1 = new TextView(context)).setTextSize(1, 16.0f);
        this.textView1.setGravity(1);
        this.textView1.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        try {
            this.textView1.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("PhoneNumberHelp", 2131560431)));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.textView1.setText((CharSequence)LocaleController.getString("PhoneNumberHelp", 2131560431));
        }
        linearLayout.addView((View)this.textView1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 20, 56, 20, 0));
        (this.textView2 = new TextView(context)).setTextSize(1, 18.0f);
        this.textView2.setGravity(1);
        this.textView2.setTextColor(Theme.getColor("key_changephoneinfo_changeText"));
        this.textView2.setText((CharSequence)LocaleController.getString("PhoneNumberChange", 2131560429));
        this.textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView2.setPadding(0, AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f));
        linearLayout.addView((View)this.textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 20, 46, 20, 0));
        this.textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$ChangePhoneHelpActivity$qVZMrZGI0M_ckM3yEp535Q8q_vk(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.textView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "key_changephoneinfo_changeText"), new ThemeDescription((View)this.imageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "changephoneinfo_image") };
    }
}
