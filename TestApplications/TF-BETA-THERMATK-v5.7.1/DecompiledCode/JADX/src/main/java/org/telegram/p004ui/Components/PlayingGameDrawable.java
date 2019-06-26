package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import com.google.android.exoplayer2.util.NalUnitUtil;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.PlayingGameDrawable */
public class PlayingGameDrawable extends StatusDrawable {
    private int currentAccount = UserConfig.selectedAccount;
    private boolean isChat = false;
    private long lastUpdateTime = 0;
    private Paint paint = new Paint(1);
    private float progress;
    private RectF rect = new RectF();
    private boolean started = false;

    public int getOpacity() {
        return -2;
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
        currentTimeMillis = 16;
        if (j <= 16) {
            currentTimeMillis = j;
        }
        if (this.progress >= 1.0f) {
            this.progress = 0.0f;
        }
        this.progress += ((float) currentTimeMillis) / 300.0f;
        if (this.progress > 1.0f) {
            this.progress = 1.0f;
        }
        invalidateSelf();
    }

    public void start() {
        this.lastUpdateTime = System.currentTimeMillis();
        this.started = true;
        invalidateSelf();
    }

    public void stop() {
        this.progress = 0.0f;
        this.started = false;
    }

    public void draw(Canvas canvas) {
        int dp = AndroidUtilities.m26dp(10.0f);
        int intrinsicHeight = getBounds().top + ((getIntrinsicHeight() - dp) / 2);
        if (!this.isChat) {
            intrinsicHeight += AndroidUtilities.m26dp(1.0f);
        }
        int i = intrinsicHeight;
        this.paint.setColor(Theme.getColor(Theme.key_chat_status));
        this.rect.set(0.0f, (float) i, (float) dp, (float) (i + dp));
        float f = this.progress;
        intrinsicHeight = (int) (f < 0.5f ? (1.0f - (f / 0.5f)) * 35.0f : ((f - 0.5f) * 35.0f) / 0.5f);
        for (int i2 = 0; i2 < 3; i2++) {
            float dp2 = (float) ((AndroidUtilities.m26dp(5.0f) * i2) + AndroidUtilities.m26dp(9.2f));
            float dp3 = (float) AndroidUtilities.m26dp(5.0f);
            float f2 = this.progress;
            dp2 -= dp3 * f2;
            if (i2 == 2) {
                this.paint.setAlpha(Math.min(NalUnitUtil.EXTENDED_SAR, (int) ((f2 * 255.0f) / 0.5f)));
            } else if (i2 != 0) {
                this.paint.setAlpha(NalUnitUtil.EXTENDED_SAR);
            } else if (f2 > 0.5f) {
                this.paint.setAlpha((int) ((1.0f - ((f2 - 0.5f) / 0.5f)) * 255.0f));
            } else {
                this.paint.setAlpha(NalUnitUtil.EXTENDED_SAR);
            }
            canvas.drawCircle(dp2, (float) ((dp / 2) + i), (float) AndroidUtilities.m26dp(1.2f), this.paint);
        }
        this.paint.setAlpha(NalUnitUtil.EXTENDED_SAR);
        canvas.drawArc(this.rect, (float) intrinsicHeight, (float) (360 - (intrinsicHeight * 2)), true, this.paint);
        this.paint.setColor(Theme.getColor(Theme.key_actionBarDefault));
        canvas.drawCircle((float) AndroidUtilities.m26dp(4.0f), (float) ((i + (dp / 2)) - AndroidUtilities.m26dp(2.0f)), (float) AndroidUtilities.m26dp(1.0f), this.paint);
        checkUpdate();
    }

    private void checkUpdate() {
        if (!this.started) {
            return;
        }
        if (NotificationCenter.getInstance(this.currentAccount).isAnimationInProgress()) {
            AndroidUtilities.runOnUIThread(new C2653-$$Lambda$PlayingGameDrawable$65ulwJDGFoIbDH0sYp9eXDBVUBc(this), 100);
        } else {
            update();
        }
    }

    public int getIntrinsicWidth() {
        return AndroidUtilities.m26dp(20.0f);
    }

    public int getIntrinsicHeight() {
        return AndroidUtilities.m26dp(18.0f);
    }
}