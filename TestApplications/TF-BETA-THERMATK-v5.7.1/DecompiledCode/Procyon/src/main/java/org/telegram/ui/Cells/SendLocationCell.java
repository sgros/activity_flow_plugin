// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Paint;
import org.telegram.tgnet.ConnectionsManager;
import android.graphics.Canvas;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.LocationController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.graphics.RectF;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.SimpleTextView;
import android.widget.FrameLayout;

public class SendLocationCell extends FrameLayout
{
    private SimpleTextView accurateTextView;
    private int currentAccount;
    private long dialogId;
    private ImageView imageView;
    private Runnable invalidateRunnable;
    private RectF rect;
    private SimpleTextView titleTextView;
    
    public SendLocationCell(final Context context, final boolean b) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.invalidateRunnable = new Runnable() {
            @Override
            public void run() {
                SendLocationCell.this.checkText();
                final SendLocationCell this$0 = SendLocationCell.this;
                this$0.invalidate((int)this$0.rect.left - 5, (int)SendLocationCell.this.rect.top - 5, (int)SendLocationCell.this.rect.right + 5, (int)SendLocationCell.this.rect.bottom + 5);
                AndroidUtilities.runOnUIThread(SendLocationCell.this.invalidateRunnable, 1000L);
            }
        };
        this.imageView = new ImageView(context);
        final ImageView imageView = this.imageView;
        String tag;
        if (b) {
            tag = "location_sendLiveLocationBackgroundlocation_sendLiveLocationIcon";
        }
        else {
            tag = "location_sendLocationBackgroundlocation_sendLocationIcon";
        }
        imageView.setTag((Object)tag);
        final int dp = AndroidUtilities.dp(40.0f);
        final String s = "location_sendLiveLocationBackground";
        String s2;
        if (b) {
            s2 = "location_sendLiveLocationBackground";
        }
        else {
            s2 = "location_sendLocationBackground";
        }
        final int color = Theme.getColor(s2);
        String s3;
        if (b) {
            s3 = s;
        }
        else {
            s3 = "location_sendLocationBackground";
        }
        final Drawable simpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, color, Theme.getColor(s3));
        if (b) {
            this.rect = new RectF();
            final Drawable drawable = this.getResources().getDrawable(2131165535);
            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("location_sendLiveLocationIcon"), PorterDuff$Mode.MULTIPLY));
            final CombinedDrawable backgroundDrawable = new CombinedDrawable(simpleSelectorCircleDrawable, drawable);
            backgroundDrawable.setCustomSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            this.imageView.setBackgroundDrawable((Drawable)backgroundDrawable);
            AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
            this.setWillNotDraw(false);
        }
        else {
            final Drawable drawable2 = this.getResources().getDrawable(2131165762);
            drawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("location_sendLocationIcon"), PorterDuff$Mode.MULTIPLY));
            final CombinedDrawable backgroundDrawable2 = new CombinedDrawable(simpleSelectorCircleDrawable, drawable2);
            backgroundDrawable2.setCustomSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            backgroundDrawable2.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            this.imageView.setBackgroundDrawable((Drawable)backgroundDrawable2);
        }
        final ImageView imageView2 = this.imageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        float n3 = 17.0f;
        float n4;
        if (isRTL2) {
            n4 = 0.0f;
        }
        else {
            n4 = 17.0f;
        }
        if (!LocaleController.isRTL) {
            n3 = 0.0f;
        }
        this.addView((View)imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, n4, 13.0f, n3, 0.0f));
        (this.titleTextView = new SimpleTextView(context)).setTextSize(16);
        final SimpleTextView titleTextView = this.titleTextView;
        final String s4 = "windowBackgroundWhiteRedText2";
        String tag2;
        if (b) {
            tag2 = "windowBackgroundWhiteRedText2";
        }
        else {
            tag2 = "windowBackgroundWhiteBlueText7";
        }
        titleTextView.setTag((Object)tag2);
        final SimpleTextView titleTextView2 = this.titleTextView;
        String s5;
        if (b) {
            s5 = s4;
        }
        else {
            s5 = "windowBackgroundWhiteBlueText7";
        }
        titleTextView2.setTextColor(Theme.getColor(s5));
        final SimpleTextView titleTextView3 = this.titleTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        titleTextView3.setGravity(gravity);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        final SimpleTextView titleTextView4 = this.titleTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 16.0f;
        }
        else {
            n6 = 73.0f;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 73.0f;
        }
        else {
            n7 = 16.0f;
        }
        this.addView((View)titleTextView4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n5 | 0x30, n6, 12.0f, n7, 0.0f));
        (this.accurateTextView = new SimpleTextView(context)).setTextSize(14);
        this.accurateTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        final SimpleTextView accurateTextView = this.accurateTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        accurateTextView.setGravity(gravity2);
        final SimpleTextView accurateTextView2 = this.accurateTextView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = n;
        }
        else {
            n8 = 3;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = 16.0f;
        }
        else {
            n9 = 73.0f;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 73.0f;
        }
        else {
            n10 = 16.0f;
        }
        this.addView((View)accurateTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 20.0f, n8 | 0x30, n9, 37.0f, n10, 0.0f));
    }
    
    private void checkText() {
        final LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo != null) {
            final String string = LocaleController.getString("StopLiveLocation", 2131560823);
            final TLRPC.Message messageOwner = sharingLocationInfo.messageObject.messageOwner;
            final int edit_date = messageOwner.edit_date;
            long n;
            if (edit_date != 0) {
                n = edit_date;
            }
            else {
                n = messageOwner.date;
            }
            this.setText(string, LocaleController.formatLocationUpdateDate(n));
        }
        else {
            this.setText(LocaleController.getString("SendLiveLocation", 2131560695), LocaleController.getString("SendLiveLocationInfo", 2131560699));
        }
    }
    
    private ImageView getImageView() {
        return this.imageView;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.rect != null) {
            AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
    }
    
    protected void onDraw(final Canvas canvas) {
        final LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo == null) {
            return;
        }
        final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        final int stopTime = sharingLocationInfo.stopTime;
        if (stopTime < currentTime) {
            return;
        }
        final float n = Math.abs(stopTime - currentTime) / (float)sharingLocationInfo.period;
        if (LocaleController.isRTL) {
            this.rect.set((float)AndroidUtilities.dp(13.0f), (float)AndroidUtilities.dp(18.0f), (float)AndroidUtilities.dp(43.0f), (float)AndroidUtilities.dp(48.0f));
        }
        else {
            this.rect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0f)), (float)AndroidUtilities.dp(18.0f), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(13.0f)), (float)AndroidUtilities.dp(48.0f));
        }
        final int color = Theme.getColor("location_liveLocationProgress");
        Theme.chat_radialProgress2Paint.setColor(color);
        Theme.chat_livePaint.setColor(color);
        canvas.drawArc(this.rect, -90.0f, n * -360.0f, false, Theme.chat_radialProgress2Paint);
        final String formatLocationLeftTime = LocaleController.formatLocationLeftTime(Math.abs(sharingLocationInfo.stopTime - currentTime));
        canvas.drawText(formatLocationLeftTime, this.rect.centerX() - Theme.chat_livePaint.measureText(formatLocationLeftTime) / 2.0f, (float)AndroidUtilities.dp(37.0f), (Paint)Theme.chat_livePaint);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(66.0f), 1073741824));
    }
    
    public void setDialogId(final long dialogId) {
        this.dialogId = dialogId;
        this.checkText();
    }
    
    public void setHasLocation(final boolean b) {
        if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) == null) {
            final SimpleTextView titleTextView = this.titleTextView;
            final float n = 1.0f;
            float alpha;
            if (b) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.5f;
            }
            titleTextView.setAlpha(alpha);
            final SimpleTextView accurateTextView = this.accurateTextView;
            float alpha2;
            if (b) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.5f;
            }
            accurateTextView.setAlpha(alpha2);
            final ImageView imageView = this.imageView;
            float alpha3;
            if (b) {
                alpha3 = n;
            }
            else {
                alpha3 = 0.5f;
            }
            imageView.setAlpha(alpha3);
        }
    }
    
    public void setText(final String text, final String text2) {
        this.titleTextView.setText(text);
        this.accurateTextView.setText(text2);
    }
}
