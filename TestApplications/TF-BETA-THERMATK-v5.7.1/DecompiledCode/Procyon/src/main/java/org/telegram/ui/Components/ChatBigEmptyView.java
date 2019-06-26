// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.ArrayList;
import android.widget.LinearLayout;

public class ChatBigEmptyView extends LinearLayout
{
    public static final int EMPTY_VIEW_TYPE_GROUP = 1;
    public static final int EMPTY_VIEW_TYPE_SAVED = 2;
    public static final int EMPTY_VIEW_TYPE_SECRET = 0;
    private ArrayList<ImageView> imageViews;
    private TextView statusTextView;
    private ArrayList<TextView> textViews;
    
    public ChatBigEmptyView(final Context context, final int n) {
        super(context);
        this.textViews = new ArrayList<TextView>();
        this.imageViews = new ArrayList<ImageView>();
        this.setBackgroundResource(2131165871);
        this.getBackground().setColorFilter((ColorFilter)Theme.colorFilter);
        this.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        this.setOrientation(1);
        if (n == 0) {
            (this.statusTextView = new TextView(context)).setTextSize(1, 15.0f);
            this.statusTextView.setTextColor(Theme.getColor("chat_serviceText"));
            this.statusTextView.setGravity(1);
            this.statusTextView.setMaxWidth(AndroidUtilities.dp(210.0f));
            this.textViews.add(this.statusTextView);
            this.addView((View)this.statusTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
        }
        else if (n == 1) {
            (this.statusTextView = new TextView(context)).setTextSize(1, 15.0f);
            this.statusTextView.setTextColor(Theme.getColor("chat_serviceText"));
            this.statusTextView.setGravity(1);
            this.statusTextView.setMaxWidth(AndroidUtilities.dp(210.0f));
            this.textViews.add(this.statusTextView);
            this.addView((View)this.statusTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
        }
        else {
            final ImageView imageView = new ImageView(context);
            imageView.setImageResource(2131165358);
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 2, 0, 0));
        }
        final TextView e = new TextView(context);
        if (n == 0) {
            e.setText((CharSequence)LocaleController.getString("EncryptedDescriptionTitle", 2131559357));
            e.setTextSize(1, 15.0f);
        }
        else if (n == 1) {
            e.setText((CharSequence)LocaleController.getString("GroupEmptyTitle2", 2131559608));
            e.setTextSize(1, 15.0f);
        }
        else {
            e.setText((CharSequence)LocaleController.getString("ChatYourSelfTitle", 2131559051));
            e.setTextSize(1, 16.0f);
            e.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            e.setGravity(1);
        }
        e.setTextColor(Theme.getColor("chat_serviceText"));
        this.textViews.add(e);
        e.setMaxWidth(AndroidUtilities.dp(260.0f));
        int n2;
        if (n != 2) {
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
        }
        else {
            n2 = 1;
        }
        int n3;
        if (n != 2) {
            n3 = 0;
        }
        else {
            n3 = 8;
        }
        this.addView((View)e, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2 | 0x30, 0, 8, 0, n3));
        for (int i = 0; i < 4; ++i) {
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n4, 0, 8, 0, 0));
            final ImageView e2 = new ImageView(context);
            e2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff$Mode.MULTIPLY));
            if (n == 0) {
                e2.setImageResource(2131165448);
            }
            else if (n == 2) {
                e2.setImageResource(2131165525);
            }
            else {
                e2.setImageResource(2131165406);
            }
            this.imageViews.add(e2);
            final TextView e3 = new TextView(context);
            e3.setTextSize(1, 15.0f);
            e3.setTextColor(Theme.getColor("chat_serviceText"));
            this.textViews.add(e3);
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            e3.setGravity(n5 | 0x10);
            e3.setMaxWidth(AndroidUtilities.dp(260.0f));
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i == 3) {
                            if (n == 0) {
                                e3.setText((CharSequence)LocaleController.getString("EncryptedDescription4", 2131559356));
                            }
                            else if (n == 2) {
                                e3.setText((CharSequence)LocaleController.getString("ChatYourSelfDescription4", 2131559049));
                            }
                            else {
                                e3.setText((CharSequence)LocaleController.getString("GroupDescription4", 2131559606));
                            }
                        }
                    }
                    else if (n == 0) {
                        e3.setText((CharSequence)LocaleController.getString("EncryptedDescription3", 2131559355));
                    }
                    else if (n == 2) {
                        e3.setText((CharSequence)LocaleController.getString("ChatYourSelfDescription3", 2131559048));
                    }
                    else {
                        e3.setText((CharSequence)LocaleController.getString("GroupDescription3", 2131559605));
                    }
                }
                else if (n == 0) {
                    e3.setText((CharSequence)LocaleController.getString("EncryptedDescription2", 2131559354));
                }
                else if (n == 2) {
                    e3.setText((CharSequence)LocaleController.getString("ChatYourSelfDescription2", 2131559047));
                }
                else {
                    e3.setText((CharSequence)LocaleController.getString("GroupDescription2", 2131559604));
                }
            }
            else if (n == 0) {
                e3.setText((CharSequence)LocaleController.getString("EncryptedDescription1", 2131559353));
            }
            else if (n == 2) {
                e3.setText((CharSequence)LocaleController.getString("ChatYourSelfDescription1", 2131559046));
            }
            else {
                e3.setText((CharSequence)LocaleController.getString("GroupDescription1", 2131559603));
            }
            if (LocaleController.isRTL) {
                linearLayout.addView((View)e3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
                if (n == 0) {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 8.0f, 3.0f, 0.0f, 0.0f));
                }
                else if (n == 2) {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 8.0f, 7.0f, 0.0f, 0.0f));
                }
                else {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 8.0f, 3.0f, 0.0f, 0.0f));
                }
            }
            else {
                if (n == 0) {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 0.0f, 4.0f, 8.0f, 0.0f));
                }
                else if (n == 2) {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 0.0f, 8.0f, 8.0f, 0.0f));
                }
                else {
                    linearLayout.addView((View)e2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 0.0f, 4.0f, 8.0f, 0.0f));
                }
                linearLayout.addView((View)e3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
            }
        }
    }
    
    public void setStatusText(final CharSequence text) {
        this.statusTextView.setText(text);
    }
    
    public void setTextColor(final int textColor) {
        final int n = 0;
        int index = 0;
        int i;
        while (true) {
            i = n;
            if (index >= this.textViews.size()) {
                break;
            }
            this.textViews.get(index).setTextColor(textColor);
            ++index;
        }
        while (i < this.imageViews.size()) {
            this.imageViews.get(i).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff$Mode.MULTIPLY));
            ++i;
        }
    }
}
