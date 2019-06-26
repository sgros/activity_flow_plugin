// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.osmdroid.util.GeoPoint;
import org.telegram.messenger.UserObject;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils;
import org.telegram.messenger.MessageObject;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import android.view.View$MeasureSpec;
import org.telegram.tgnet.TLRPC;
import android.graphics.Paint;
import org.telegram.tgnet.ConnectionsManager;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.RectF;
import android.location.Location;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.messenger.LocationController;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class SharingLiveLocationCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private int currentAccount;
    private LocationController.SharingLocationInfo currentInfo;
    private SimpleTextView distanceTextView;
    private Runnable invalidateRunnable;
    private LocationActivity.LiveLocation liveLocation;
    private Location location;
    private SimpleTextView nameTextView;
    private RectF rect;
    
    public SharingLiveLocationCell(final Context context, final boolean b) {
        super(context);
        this.rect = new RectF();
        this.location = new Location("network");
        this.invalidateRunnable = new Runnable() {
            @Override
            public void run() {
                final SharingLiveLocationCell this$0 = SharingLiveLocationCell.this;
                this$0.invalidate((int)this$0.rect.left - 5, (int)SharingLiveLocationCell.this.rect.top - 5, (int)SharingLiveLocationCell.this.rect.right + 5, (int)SharingLiveLocationCell.this.rect.bottom + 5);
                AndroidUtilities.runOnUIThread(SharingLiveLocationCell.this.invalidateRunnable, 1000L);
            }
        };
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(20.0f));
        this.avatarDrawable = new AvatarDrawable();
        (this.nameTextView = new SimpleTextView(context)).setTextSize(16);
        this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        final SimpleTextView nameTextView = this.nameTextView;
        final boolean isRTL = LocaleController.isRTL;
        int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        nameTextView.setGravity(gravity);
        if (b) {
            final BackupImageView avatarImageView = this.avatarImageView;
            int n2;
            if (LocaleController.isRTL) {
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
                n3 = 17.0f;
            }
            float n4;
            if (LocaleController.isRTL) {
                n4 = 17.0f;
            }
            else {
                n4 = 0.0f;
            }
            this.addView((View)avatarImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, n3, 13.0f, n4, 0.0f));
            final SimpleTextView nameTextView2 = this.nameTextView;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            float n6;
            if (LocaleController.isRTL) {
                n6 = 54.0f;
            }
            else {
                n6 = 73.0f;
            }
            float n7;
            if (LocaleController.isRTL) {
                n7 = 73.0f;
            }
            else {
                n7 = 54.0f;
            }
            this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n5 | 0x30, n6, 12.0f, n7, 0.0f));
            (this.distanceTextView = new SimpleTextView(context)).setTextSize(14);
            this.distanceTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            final SimpleTextView distanceTextView = this.distanceTextView;
            int gravity2;
            if (LocaleController.isRTL) {
                gravity2 = 5;
            }
            else {
                gravity2 = 3;
            }
            distanceTextView.setGravity(gravity2);
            final SimpleTextView distanceTextView2 = this.distanceTextView;
            if (!LocaleController.isRTL) {
                n = 3;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 54.0f;
            }
            else {
                n8 = 73.0f;
            }
            float n9;
            if (LocaleController.isRTL) {
                n9 = 73.0f;
            }
            else {
                n9 = 54.0f;
            }
            this.addView((View)distanceTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n | 0x30, n8, 37.0f, n9, 0.0f));
        }
        else {
            final BackupImageView avatarImageView2 = this.avatarImageView;
            int n10;
            if (LocaleController.isRTL) {
                n10 = 5;
            }
            else {
                n10 = 3;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 0.0f;
            }
            else {
                n11 = 17.0f;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 17.0f;
            }
            else {
                n12 = 0.0f;
            }
            this.addView((View)avatarImageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n10 | 0x30, n11, 7.0f, n12, 0.0f));
            final SimpleTextView nameTextView3 = this.nameTextView;
            if (!LocaleController.isRTL) {
                n = 3;
            }
            float n13;
            if (LocaleController.isRTL) {
                n13 = 54.0f;
            }
            else {
                n13 = 74.0f;
            }
            float n14;
            if (LocaleController.isRTL) {
                n14 = 74.0f;
            }
            else {
                n14 = 54.0f;
            }
            this.addView((View)nameTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n | 0x30, n13, 17.0f, n14, 0.0f));
        }
        this.setWillNotDraw(false);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AndroidUtilities.runOnUIThread(this.invalidateRunnable);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.currentInfo == null && this.liveLocation == null) {
            return;
        }
        final LocationController.SharingLocationInfo currentInfo = this.currentInfo;
        int stopTime;
        int n;
        if (currentInfo != null) {
            stopTime = currentInfo.stopTime;
            n = currentInfo.period;
        }
        else {
            final TLRPC.Message object = this.liveLocation.object;
            final int date = object.date;
            n = object.media.period;
            stopTime = date + n;
        }
        final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        if (stopTime < currentTime) {
            return;
        }
        final int a = stopTime - currentTime;
        final float n2 = Math.abs(a) / (float)n;
        final boolean isRTL = LocaleController.isRTL;
        float n3 = 48.0f;
        float n4 = 18.0f;
        if (isRTL) {
            final RectF rect = this.rect;
            final float n5 = (float)AndroidUtilities.dp(13.0f);
            if (this.distanceTextView == null) {
                n4 = 12.0f;
            }
            final float n6 = (float)AndroidUtilities.dp(n4);
            final float n7 = (float)AndroidUtilities.dp(43.0f);
            if (this.distanceTextView == null) {
                n3 = 42.0f;
            }
            rect.set(n5, n6, n7, (float)AndroidUtilities.dp(n3));
        }
        else {
            final RectF rect2 = this.rect;
            final float n8 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0f));
            if (this.distanceTextView == null) {
                n4 = 12.0f;
            }
            final float n9 = (float)AndroidUtilities.dp(n4);
            final float n10 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(13.0f));
            if (this.distanceTextView == null) {
                n3 = 42.0f;
            }
            rect2.set(n8, n9, n10, (float)AndroidUtilities.dp(n3));
        }
        int n11;
        if (this.distanceTextView == null) {
            n11 = Theme.getColor("dialog_liveLocationProgress");
        }
        else {
            n11 = Theme.getColor("location_liveLocationProgress");
        }
        Theme.chat_radialProgress2Paint.setColor(n11);
        Theme.chat_livePaint.setColor(n11);
        canvas.drawArc(this.rect, -90.0f, n2 * -360.0f, false, Theme.chat_radialProgress2Paint);
        final String formatLocationLeftTime = LocaleController.formatLocationLeftTime(a);
        final float measureText = Theme.chat_livePaint.measureText(formatLocationLeftTime);
        final float centerX = this.rect.centerX();
        final float n12 = measureText / 2.0f;
        float n13;
        if (this.distanceTextView != null) {
            n13 = 37.0f;
        }
        else {
            n13 = 31.0f;
        }
        canvas.drawText(formatLocationLeftTime, centerX - n12, (float)AndroidUtilities.dp(n13), (Paint)Theme.chat_livePaint);
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
        float n2;
        if (this.distanceTextView != null) {
            n2 = 66.0f;
        }
        else {
            n2 = 54.0f;
        }
        super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2), 1073741824));
    }
    
    public void setDialog(final LocationController.SharingLocationInfo currentInfo) {
        this.currentInfo = currentInfo;
        final int i = (int)currentInfo.did;
        if (i > 0) {
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(i);
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                this.nameTextView.setText(ContactsController.formatName(user.first_name, user.last_name));
                this.avatarImageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
            }
        }
        else {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
            if (chat != null) {
                this.avatarDrawable.setInfo(chat);
                this.nameTextView.setText(chat.title);
                this.avatarImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
            }
        }
    }
    
    public void setDialog(final MessageObject messageObject, final Location location) {
        int i = messageObject.messageOwner.from_id;
        if (messageObject.isForwarded()) {
            final TLRPC.MessageFwdHeader fwd_from = messageObject.messageOwner.fwd_from;
            final int channel_id = fwd_from.channel_id;
            if (channel_id != 0) {
                i = -channel_id;
            }
            else {
                i = fwd_from.from_id;
            }
        }
        this.currentAccount = messageObject.currentAccount;
        String address;
        if (!TextUtils.isEmpty((CharSequence)messageObject.messageOwner.media.address)) {
            address = messageObject.messageOwner.media.address;
        }
        else {
            address = null;
        }
        String text;
        if (!TextUtils.isEmpty((CharSequence)messageObject.messageOwner.media.title)) {
            text = messageObject.messageOwner.media.title;
            final Drawable drawable = this.getResources().getDrawable(2131165762);
            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("location_sendLocationIcon"), PorterDuff$Mode.MULTIPLY));
            final int color = Theme.getColor("location_placeLocationBackground");
            final CombinedDrawable imageDrawable = new CombinedDrawable(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), color, color), drawable);
            imageDrawable.setCustomSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            imageDrawable.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            this.avatarImageView.setImageDrawable(imageDrawable);
        }
        else {
            text = "";
            this.avatarDrawable = null;
            if (i > 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(i);
                if (user != null) {
                    this.avatarDrawable = new AvatarDrawable(user);
                    text = UserObject.getUserName(user);
                    this.avatarImageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
                }
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                if (chat != null) {
                    this.avatarDrawable = new AvatarDrawable(chat);
                    text = chat.title;
                    this.avatarImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
                }
            }
        }
        this.nameTextView.setText(text);
        this.location.setLatitude(messageObject.messageOwner.media.geo.lat);
        this.location.setLongitude(messageObject.messageOwner.media.geo._long);
        if (location != null) {
            final float distanceTo = this.location.distanceTo(location);
            if (address != null) {
                if (distanceTo < 1000.0f) {
                    this.distanceTextView.setText(String.format("%s - %d %s", address, (int)distanceTo, LocaleController.getString("MetersAway", 2131559857)));
                }
                else {
                    this.distanceTextView.setText(String.format("%s - %.2f %s", address, distanceTo / 1000.0f, LocaleController.getString("KMetersAway", 2131559709)));
                }
            }
            else if (distanceTo < 1000.0f) {
                this.distanceTextView.setText(String.format("%d %s", (int)distanceTo, LocaleController.getString("MetersAway", 2131559857)));
            }
            else {
                this.distanceTextView.setText(String.format("%.2f %s", distanceTo / 1000.0f, LocaleController.getString("KMetersAway", 2131559709)));
            }
        }
        else if (address != null) {
            this.distanceTextView.setText(address);
        }
        else {
            this.distanceTextView.setText(LocaleController.getString("Loading", 2131559768));
        }
    }
    
    public void setDialog(final LocationActivity.LiveLocation liveLocation, final Location location) {
        this.liveLocation = liveLocation;
        final int id = liveLocation.id;
        if (id > 0) {
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(id);
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                this.nameTextView.setText(ContactsController.formatName(user.first_name, user.last_name));
                this.avatarImageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
            }
        }
        else {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-id);
            if (chat != null) {
                this.avatarDrawable.setInfo(chat);
                this.nameTextView.setText(chat.title);
                this.avatarImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
            }
        }
        final GeoPoint position = liveLocation.marker.getPosition();
        this.location.setLatitude(position.getLatitude());
        this.location.setLongitude(position.getLongitude());
        final TLRPC.Message object = liveLocation.object;
        final int edit_date = object.edit_date;
        long n;
        if (edit_date != 0) {
            n = edit_date;
        }
        else {
            n = object.date;
        }
        final String formatLocationUpdateDate = LocaleController.formatLocationUpdateDate(n);
        if (location != null) {
            final float distanceTo = this.location.distanceTo(location);
            if (distanceTo < 1000.0f) {
                this.distanceTextView.setText(String.format("%s - %d %s", formatLocationUpdateDate, (int)distanceTo, LocaleController.getString("MetersAway", 2131559857)));
            }
            else {
                this.distanceTextView.setText(String.format("%s - %.2f %s", formatLocationUpdateDate, distanceTo / 1000.0f, LocaleController.getString("KMetersAway", 2131559709)));
            }
        }
        else {
            this.distanceTextView.setText(formatLocationUpdateDate);
        }
    }
}
