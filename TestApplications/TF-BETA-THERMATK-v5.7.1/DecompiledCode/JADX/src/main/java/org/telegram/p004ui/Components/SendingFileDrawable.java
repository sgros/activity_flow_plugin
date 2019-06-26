package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import com.google.android.exoplayer2.util.NalUnitUtil;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.SendingFileDrawable */
public class SendingFileDrawable extends StatusDrawable {
    private boolean isChat = false;
    private long lastUpdateTime = 0;
    private float progress;
    private boolean started = false;

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setIsChat(boolean z) {
        this.isChat = z;
    }

    private void update() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        currentTimeMillis = 50;
        if (j <= 50) {
            currentTimeMillis = j;
        }
        this.progress += ((float) currentTimeMillis) / 500.0f;
        while (true) {
            float f = this.progress;
            if (f > 1.0f) {
                this.progress = f - 1.0f;
            } else {
                invalidateSelf();
                return;
            }
        }
    }

    public void start() {
        this.lastUpdateTime = System.currentTimeMillis();
        this.started = true;
        invalidateSelf();
    }

    public void stop() {
        this.started = false;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                Theme.chat_statusRecordPaint.setAlpha((int) (this.progress * 255.0f));
            } else if (i == 2) {
                Theme.chat_statusRecordPaint.setAlpha((int) ((1.0f - this.progress) * 255.0f));
            } else {
                Theme.chat_statusRecordPaint.setAlpha(NalUnitUtil.EXTENDED_SAR);
            }
            float dp = (((float) AndroidUtilities.m26dp(5.0f)) * this.progress) + ((float) (AndroidUtilities.m26dp(5.0f) * i));
            float f = 7.0f;
            canvas.drawLine(dp, (float) AndroidUtilities.m26dp(this.isChat ? 3.0f : 4.0f), dp + ((float) AndroidUtilities.m26dp(4.0f)), (float) AndroidUtilities.m26dp(this.isChat ? 7.0f : 8.0f), Theme.chat_statusRecordPaint);
            float dp2 = (float) AndroidUtilities.m26dp(this.isChat ? 11.0f : 12.0f);
            float dp3 = dp + ((float) AndroidUtilities.m26dp(4.0f));
            if (!this.isChat) {
                f = 8.0f;
            }
            canvas.drawLine(dp, dp2, dp3, (float) AndroidUtilities.m26dp(f), Theme.chat_statusRecordPaint);
        }
        if (this.started) {
            update();
        }
    }

    public int getIntrinsicWidth() {
        return AndroidUtilities.m26dp(18.0f);
    }

    public int getIntrinsicHeight() {
        return AndroidUtilities.m26dp(14.0f);
    }
}