// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageLocation;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.FileLog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Canvas;
import android.view.View$OnClickListener;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import org.telegram.ui.Components.SnowflakesEffect;
import android.graphics.Paint;
import android.widget.TextView;
import android.graphics.Rect;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class DrawerProfileCell extends FrameLayout
{
    private boolean accountsShowed;
    private ImageView arrowView;
    private BackupImageView avatarImageView;
    private Integer currentColor;
    private Rect destRect;
    private TextView nameTextView;
    private Paint paint;
    private TextView phoneTextView;
    private ImageView shadowView;
    private SnowflakesEffect snowflakesEffect;
    private Rect srcRect;
    
    public DrawerProfileCell(final Context context) {
        super(context);
        this.srcRect = new Rect();
        this.destRect = new Rect();
        this.paint = new Paint();
        (this.shadowView = new ImageView(context)).setVisibility(4);
        this.shadowView.setScaleType(ImageView$ScaleType.FIT_XY);
        this.shadowView.setImageResource(2131165321);
        this.addView((View)this.shadowView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 70, 83));
        this.avatarImageView = new BackupImageView(context);
        this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(32.0f));
        this.addView((View)this.avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, 83, 16.0f, 0.0f, 0.0f, 67.0f));
        (this.nameTextView = new TextView(context)).setTextSize(1, 15.0f);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setLines(1);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setGravity(3);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 16.0f, 0.0f, 76.0f, 28.0f));
        (this.phoneTextView = new TextView(context)).setTextSize(1, 13.0f);
        this.phoneTextView.setLines(1);
        this.phoneTextView.setMaxLines(1);
        this.phoneTextView.setSingleLine(true);
        this.phoneTextView.setGravity(3);
        this.addView((View)this.phoneTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 16.0f, 0.0f, 76.0f, 9.0f));
        (this.arrowView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        final ImageView arrowView = this.arrowView;
        int n;
        String s;
        if (this.accountsShowed) {
            n = 2131558438;
            s = "AccDescrHideAccounts";
        }
        else {
            n = 2131558472;
            s = "AccDescrShowAccounts";
        }
        arrowView.setContentDescription((CharSequence)LocaleController.getString(s, n));
        this.addView((View)this.arrowView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(59, 59, 85));
        if (Theme.getEventType() == 0) {
            this.snowflakesEffect = new SnowflakesEffect();
        }
    }
    
    public String applyBackground() {
        final String anObject = (String)this.getTag();
        String tag = "chats_menuTopBackground";
        if (!Theme.hasThemeKey("chats_menuTopBackground") || Theme.getColor("chats_menuTopBackground") == 0) {
            tag = "chats_menuTopBackgroundCats";
        }
        if (!tag.equals(anObject)) {
            this.setBackgroundColor(Theme.getColor(tag));
            this.setTag((Object)tag);
        }
        return tag;
    }
    
    public boolean isAccountsShowed() {
        return this.accountsShowed;
    }
    
    protected void onDraw(final Canvas canvas) {
        final Drawable cachedWallpaper = Theme.getCachedWallpaper();
        final boolean equals = this.applyBackground().equals("chats_menuTopBackground");
        boolean b = true;
        final int n = 0;
        final boolean b2 = !equals && Theme.isCustomTheme() && !Theme.isPatternWallpaper() && cachedWallpaper != null;
        int i;
        if (!b2 && Theme.hasThemeKey("chats_menuTopShadowCats")) {
            i = Theme.getColor("chats_menuTopShadowCats");
        }
        else {
            if (Theme.hasThemeKey("chats_menuTopShadow")) {
                i = Theme.getColor("chats_menuTopShadow");
            }
            else {
                i = (0xFF000000 | Theme.getServiceMessageColor());
            }
            b = false;
        }
        final Integer currentColor = this.currentColor;
        if (currentColor == null || currentColor != i) {
            this.currentColor = i;
            this.shadowView.getDrawable().setColorFilter((ColorFilter)new PorterDuffColorFilter(i, PorterDuff$Mode.MULTIPLY));
        }
        this.nameTextView.setTextColor(Theme.getColor("chats_menuName"));
        if (b2) {
            this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhone"));
            if (this.shadowView.getVisibility() != 0) {
                this.shadowView.setVisibility(0);
            }
            if (cachedWallpaper instanceof ColorDrawable) {
                cachedWallpaper.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                cachedWallpaper.draw(canvas);
            }
            else if (cachedWallpaper instanceof BitmapDrawable) {
                final Bitmap bitmap = ((BitmapDrawable)cachedWallpaper).getBitmap();
                final float n2 = this.getMeasuredWidth() / (float)bitmap.getWidth();
                final float n3 = this.getMeasuredHeight() / (float)bitmap.getHeight();
                float n4 = n2;
                if (n2 < n3) {
                    n4 = n3;
                }
                final int n5 = (int)(this.getMeasuredWidth() / n4);
                final int n6 = (int)(this.getMeasuredHeight() / n4);
                final int n7 = (bitmap.getWidth() - n5) / 2;
                final int n8 = (bitmap.getHeight() - n6) / 2;
                this.srcRect.set(n7, n8, n5 + n7, n6 + n8);
                this.destRect.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                try {
                    canvas.drawBitmap(bitmap, this.srcRect, this.destRect, this.paint);
                }
                catch (Throwable t) {
                    FileLog.e(t);
                }
            }
        }
        else {
            int visibility;
            if (b) {
                visibility = n;
            }
            else {
                visibility = 4;
            }
            if (this.shadowView.getVisibility() != visibility) {
                this.shadowView.setVisibility(visibility);
            }
            this.phoneTextView.setTextColor(Theme.getColor("chats_menuPhoneCats"));
            super.onDraw(canvas);
        }
        final SnowflakesEffect snowflakesEffect = this.snowflakesEffect;
        if (snowflakesEffect != null) {
            snowflakesEffect.onDraw((View)this, canvas);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f) + AndroidUtilities.statusBarHeight, 1073741824));
        }
        else {
            try {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f), 1073741824));
            }
            catch (Exception ex) {
                this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(148.0f));
                FileLog.e(ex);
            }
        }
    }
    
    public void setAccountsShowed(final boolean accountsShowed) {
        if (this.accountsShowed == accountsShowed) {
            return;
        }
        this.accountsShowed = accountsShowed;
        final ImageView arrowView = this.arrowView;
        int imageResource;
        if (this.accountsShowed) {
            imageResource = 2131165360;
        }
        else {
            imageResource = 2131165359;
        }
        arrowView.setImageResource(imageResource);
    }
    
    public void setOnArrowClickListener(final View$OnClickListener view$OnClickListener) {
        this.arrowView.setOnClickListener((View$OnClickListener)new _$$Lambda$DrawerProfileCell$E00gMmT74biKthBWyKI7QNe_uk4(this, view$OnClickListener));
    }
    
    public void setUser(final TLRPC.User user, final boolean accountsShowed) {
        if (user == null) {
            return;
        }
        this.accountsShowed = accountsShowed;
        final ImageView arrowView = this.arrowView;
        int imageResource;
        if (this.accountsShowed) {
            imageResource = 2131165360;
        }
        else {
            imageResource = 2131165359;
        }
        arrowView.setImageResource(imageResource);
        this.nameTextView.setText((CharSequence)UserObject.getUserName(user));
        final TextView phoneTextView = this.phoneTextView;
        final PhoneFormat instance = PhoneFormat.getInstance();
        final StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(user.phone);
        phoneTextView.setText((CharSequence)instance.format(sb.toString()));
        final AvatarDrawable avatarDrawable = new AvatarDrawable(user);
        avatarDrawable.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
        this.avatarImageView.setImage(ImageLocation.getForUser(user, false), "50_50", avatarDrawable, user);
        this.applyBackground();
    }
}
