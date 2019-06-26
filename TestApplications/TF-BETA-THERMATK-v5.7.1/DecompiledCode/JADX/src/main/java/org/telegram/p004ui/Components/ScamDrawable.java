package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;

/* renamed from: org.telegram.ui.Components.ScamDrawable */
public class ScamDrawable extends Drawable {
    private Paint paint = new Paint(1);
    private RectF rect = new RectF();
    private String text;
    private TextPaint textPaint = new TextPaint(1);
    private int textWidth;

    public int getOpacity() {
        return -2;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ScamDrawable(int i) {
        this.textPaint.setTextSize((float) AndroidUtilities.m26dp((float) i));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth((float) AndroidUtilities.m26dp(1.0f));
        this.text = LocaleController.getString("ScamMessage", C1067R.string.ScamMessage);
        this.textWidth = (int) Math.ceil((double) this.textPaint.measureText(this.text));
    }

    public void checkText() {
        String string = LocaleController.getString("ScamMessage", C1067R.string.ScamMessage);
        if (!string.equals(this.text)) {
            this.text = string;
            this.textWidth = (int) Math.ceil((double) this.textPaint.measureText(this.text));
        }
    }

    public void setColor(int i) {
        this.textPaint.setColor(i);
        this.paint.setColor(i);
    }

    public int getIntrinsicWidth() {
        return this.textWidth + AndroidUtilities.m26dp(10.0f);
    }

    public int getIntrinsicHeight() {
        return AndroidUtilities.m26dp(16.0f);
    }

    public void draw(Canvas canvas) {
        this.rect.set(getBounds());
        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(2.0f), (float) AndroidUtilities.m26dp(2.0f), this.paint);
        canvas.drawText(this.text, this.rect.left + ((float) AndroidUtilities.m26dp(5.0f)), this.rect.top + ((float) AndroidUtilities.m26dp(12.0f)), this.textPaint);
    }
}
