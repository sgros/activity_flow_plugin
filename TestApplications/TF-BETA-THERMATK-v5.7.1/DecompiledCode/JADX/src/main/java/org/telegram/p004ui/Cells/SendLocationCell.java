package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.SimpleTextView;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.CombinedDrawable;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.Message;

/* renamed from: org.telegram.ui.Cells.SendLocationCell */
public class SendLocationCell extends FrameLayout {
    private SimpleTextView accurateTextView;
    private int currentAccount = UserConfig.selectedAccount;
    private long dialogId;
    private ImageView imageView;
    private Runnable invalidateRunnable = new C23671();
    private RectF rect;
    private SimpleTextView titleTextView;

    /* renamed from: org.telegram.ui.Cells.SendLocationCell$1 */
    class C23671 implements Runnable {
        C23671() {
        }

        public void run() {
            SendLocationCell.this.checkText();
            SendLocationCell sendLocationCell = SendLocationCell.this;
            sendLocationCell.invalidate(((int) sendLocationCell.rect.left) - 5, ((int) SendLocationCell.this.rect.top) - 5, ((int) SendLocationCell.this.rect.right) + 5, ((int) SendLocationCell.this.rect.bottom) + 5);
            AndroidUtilities.runOnUIThread(SendLocationCell.this.invalidateRunnable, 1000);
        }
    }

    public SendLocationCell(Context context, boolean z) {
        super(context);
        this.imageView = new ImageView(context);
        this.imageView.setTag(z ? "location_sendLiveLocationBackgroundlocation_sendLiveLocationIcon" : "location_sendLocationBackgroundlocation_sendLocationIcon");
        int dp = AndroidUtilities.m26dp(40.0f);
        String str = Theme.key_location_sendLiveLocationBackground;
        String str2 = Theme.key_location_sendLocationBackground;
        int color = Theme.getColor(z ? str : str2);
        if (!z) {
            str = str2;
        }
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, color, Theme.getColor(str));
        Drawable drawable;
        CombinedDrawable combinedDrawable;
        if (z) {
            this.rect = new RectF();
            drawable = getResources().getDrawable(C1067R.C1065drawable.livelocationpin);
            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_location_sendLiveLocationIcon), Mode.MULTIPLY));
            combinedDrawable = new CombinedDrawable(createSimpleSelectorCircleDrawable, drawable);
            combinedDrawable.setCustomSize(AndroidUtilities.m26dp(40.0f), AndroidUtilities.m26dp(40.0f));
            this.imageView.setBackgroundDrawable(combinedDrawable);
            AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000);
            setWillNotDraw(false);
        } else {
            drawable = getResources().getDrawable(C1067R.C1065drawable.pin);
            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_location_sendLocationIcon), Mode.MULTIPLY));
            combinedDrawable = new CombinedDrawable(createSimpleSelectorCircleDrawable, drawable);
            combinedDrawable.setCustomSize(AndroidUtilities.m26dp(40.0f), AndroidUtilities.m26dp(40.0f));
            combinedDrawable.setIconSize(AndroidUtilities.m26dp(24.0f), AndroidUtilities.m26dp(24.0f));
            this.imageView.setBackgroundDrawable(combinedDrawable);
        }
        ImageView imageView = this.imageView;
        int i = 5;
        int i2 = (LocaleController.isRTL ? 5 : 3) | 48;
        float f = 17.0f;
        float f2 = LocaleController.isRTL ? 0.0f : 17.0f;
        if (!LocaleController.isRTL) {
            f = 0.0f;
        }
        addView(imageView, LayoutHelper.createFrame(40, 40.0f, i2, f2, 13.0f, f, 0.0f));
        this.titleTextView = new SimpleTextView(context);
        this.titleTextView.setTextSize(16);
        SimpleTextView simpleTextView = this.titleTextView;
        String str3 = Theme.key_windowBackgroundWhiteRedText2;
        str = Theme.key_windowBackgroundWhiteBlueText7;
        simpleTextView.setTag(z ? str3 : str);
        simpleTextView = this.titleTextView;
        if (!z) {
            str3 = str;
        }
        simpleTextView.setTextColor(Theme.getColor(str3));
        this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        addView(this.titleTextView, LayoutHelper.createFrame(-1, 20.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 16.0f : 73.0f, 12.0f, LocaleController.isRTL ? 73.0f : 16.0f, 0.0f));
        this.accurateTextView = new SimpleTextView(context);
        this.accurateTextView.setTextSize(14);
        this.accurateTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.accurateTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        SimpleTextView simpleTextView2 = this.accurateTextView;
        if (!LocaleController.isRTL) {
            i = 3;
        }
        addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, i | 48, LocaleController.isRTL ? 16.0f : 73.0f, 37.0f, LocaleController.isRTL ? 73.0f : 16.0f, 0.0f));
    }

    private ImageView getImageView() {
        return this.imageView;
    }

    public void setHasLocation(boolean z) {
        if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) == null) {
            float f = 1.0f;
            this.titleTextView.setAlpha(z ? 1.0f : 0.5f);
            this.accurateTextView.setAlpha(z ? 1.0f : 0.5f);
            ImageView imageView = this.imageView;
            if (!z) {
                f = 0.5f;
            }
            imageView.setAlpha(f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(66.0f), 1073741824));
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.rect != null) {
            AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000);
        }
    }

    public void setText(String str, String str2) {
        this.titleTextView.setText(str);
        this.accurateTextView.setText(str2);
    }

    public void setDialogId(long j) {
        this.dialogId = j;
        checkText();
    }

    private void checkText() {
        SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo != null) {
            String string = LocaleController.getString("StopLiveLocation", C1067R.string.StopLiveLocation);
            Message message = sharingLocationInfo.messageObject.messageOwner;
            int i = message.edit_date;
            setText(string, LocaleController.formatLocationUpdateDate(i != 0 ? (long) i : (long) message.date));
            return;
        }
        setText(LocaleController.getString("SendLiveLocation", C1067R.string.SendLiveLocation), LocaleController.getString("SendLiveLocationInfo", C1067R.string.SendLiveLocationInfo));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo != null) {
            int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            int i = sharingLocationInfo.stopTime;
            if (i >= currentTime) {
                float abs = ((float) Math.abs(i - currentTime)) / ((float) sharingLocationInfo.period);
                if (LocaleController.isRTL) {
                    this.rect.set((float) AndroidUtilities.m26dp(13.0f), (float) AndroidUtilities.m26dp(18.0f), (float) AndroidUtilities.m26dp(43.0f), (float) AndroidUtilities.m26dp(48.0f));
                } else {
                    this.rect.set((float) (getMeasuredWidth() - AndroidUtilities.m26dp(43.0f)), (float) AndroidUtilities.m26dp(18.0f), (float) (getMeasuredWidth() - AndroidUtilities.m26dp(13.0f)), (float) AndroidUtilities.m26dp(48.0f));
                }
                int color = Theme.getColor(Theme.key_location_liveLocationProgress);
                Theme.chat_radialProgress2Paint.setColor(color);
                Theme.chat_livePaint.setColor(color);
                canvas.drawArc(this.rect, -90.0f, abs * -360.0f, false, Theme.chat_radialProgress2Paint);
                String formatLocationLeftTime = LocaleController.formatLocationLeftTime(Math.abs(sharingLocationInfo.stopTime - currentTime));
                canvas.drawText(formatLocationLeftTime, this.rect.centerX() - (Theme.chat_livePaint.measureText(formatLocationLeftTime) / 2.0f), (float) AndroidUtilities.m26dp(37.0f), Theme.chat_livePaint);
            }
        }
    }
}