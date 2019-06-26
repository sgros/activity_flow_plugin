package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;

/* renamed from: org.telegram.ui.Cells.LocationCell */
public class LocationCell extends FrameLayout {
    private TextView addressTextView;
    private BackupImageView imageView;
    private TextView nameTextView;
    private boolean needDivider;

    public LocationCell(Context context) {
        Context context2 = context;
        super(context);
        this.imageView = new BackupImageView(context2);
        this.imageView.setBackgroundResource(C1067R.C1065drawable.round_grey);
        this.imageView.setSize(AndroidUtilities.m26dp(30.0f), AndroidUtilities.m26dp(30.0f));
        ImageReceiver imageReceiver = this.imageView.getImageReceiver();
        String str = Theme.key_windowBackgroundWhiteGrayText3;
        imageReceiver.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        int i = 5;
        addView(this.imageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 17.0f, 8.0f, LocaleController.isRTL ? 17.0f : 0.0f, 0.0f));
        this.nameTextView = new TextView(context2);
        this.nameTextView.setTextSize(1, 16.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setEllipsize(TruncateAt.END);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        int i2 = 16;
        addView(this.nameTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, (float) (LocaleController.isRTL ? 16 : 72), 5.0f, (float) (LocaleController.isRTL ? 72 : 16), 0.0f));
        this.addressTextView = new TextView(context2);
        this.addressTextView.setTextSize(1, 14.0f);
        this.addressTextView.setMaxLines(1);
        this.addressTextView.setEllipsize(TruncateAt.END);
        this.addressTextView.setSingleLine(true);
        this.addressTextView.setTextColor(Theme.getColor(str));
        this.addressTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        TextView textView = this.addressTextView;
        if (!LocaleController.isRTL) {
            i = 3;
        }
        int i3 = i | 48;
        float f = (float) (LocaleController.isRTL ? 16 : 72);
        if (LocaleController.isRTL) {
            i2 = 72;
        }
        addView(textView, LayoutHelper.createFrame(-2, -2.0f, i3, f, 30.0f, (float) i2, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(56.0f) + this.needDivider, 1073741824));
    }

    public void setLocation(TL_messageMediaVenue tL_messageMediaVenue, String str, boolean z) {
        this.needDivider = z;
        this.nameTextView.setText(tL_messageMediaVenue.title);
        this.addressTextView.setText(tL_messageMediaVenue.address);
        this.imageView.setImage(str, null, null);
        setWillNotDraw(z ^ 1);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float) AndroidUtilities.m26dp(72.0f), (float) (getHeight() - 1), (float) getWidth(), (float) (getHeight() - 1), Theme.dividerPaint);
        }
    }
}