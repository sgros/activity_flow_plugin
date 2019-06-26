// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class TimerDrawable extends Drawable
{
    private Paint linePaint;
    private Paint paint;
    private int time;
    private int timeHeight;
    private StaticLayout timeLayout;
    private TextPaint timePaint;
    private float timeWidth;
    
    public TimerDrawable(final Context context) {
        this.timePaint = new TextPaint(1);
        this.paint = new Paint(1);
        this.linePaint = new Paint(1);
        this.timeWidth = 0.0f;
        this.timeHeight = 0;
        this.time = 0;
        this.timePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.timePaint.setTextSize((float)AndroidUtilities.dp(11.0f));
        this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        this.linePaint.setStyle(Paint$Style.STROKE);
    }
    
    public void draw(final Canvas canvas) {
        final int intrinsicWidth = this.getIntrinsicWidth();
        final int intrinsicHeight = this.getIntrinsicHeight();
        if (this.time == 0) {
            this.paint.setColor(Theme.getColor("chat_secretTimerBackground"));
            this.linePaint.setColor(Theme.getColor("chat_secretTimerText"));
            canvas.drawCircle(AndroidUtilities.dpf2(9.0f), AndroidUtilities.dpf2(9.0f), AndroidUtilities.dpf2(7.5f), this.paint);
            canvas.drawCircle(AndroidUtilities.dpf2(9.0f), AndroidUtilities.dpf2(9.0f), AndroidUtilities.dpf2(8.0f), this.linePaint);
            this.paint.setColor(Theme.getColor("chat_secretTimerText"));
            canvas.drawLine((float)AndroidUtilities.dp(9.0f), (float)AndroidUtilities.dp(9.0f), (float)AndroidUtilities.dp(13.0f), (float)AndroidUtilities.dp(9.0f), this.linePaint);
            canvas.drawLine((float)AndroidUtilities.dp(9.0f), (float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(9.0f), (float)AndroidUtilities.dp(9.5f), this.linePaint);
            canvas.drawRect(AndroidUtilities.dpf2(7.0f), AndroidUtilities.dpf2(0.0f), AndroidUtilities.dpf2(11.0f), AndroidUtilities.dpf2(1.5f), this.paint);
        }
        else {
            this.paint.setColor(Theme.getColor("chat_secretTimerBackground"));
            this.timePaint.setColor(Theme.getColor("chat_secretTimerText"));
            canvas.drawCircle((float)AndroidUtilities.dp(9.5f), (float)AndroidUtilities.dp(9.5f), (float)AndroidUtilities.dp(9.5f), this.paint);
        }
        if (this.time != 0 && this.timeLayout != null) {
            int n = 0;
            if (AndroidUtilities.density == 3.0f) {
                n = -1;
            }
            final double v = intrinsicWidth / 2;
            final double ceil = Math.ceil(this.timeWidth / 2.0f);
            Double.isNaN(v);
            canvas.translate((float)((int)(v - ceil) + n), (float)((intrinsicHeight - this.timeHeight) / 2));
            this.timeLayout.draw(canvas);
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(19.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(19.0f);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setTime(final int n) {
        this.time = n;
        final int time = this.time;
        String s;
        if (time >= 1 && time < 60) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(n);
            final String str = s = sb.toString();
            if (str.length() < 2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append("s");
                s = sb2.toString();
            }
        }
        else {
            final int time2 = this.time;
            if (time2 >= 60 && time2 < 3600) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("");
                sb3.append(n / 60);
                final String str2 = s = sb3.toString();
                if (str2.length() < 2) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(str2);
                    sb4.append("m");
                    s = sb4.toString();
                }
            }
            else {
                final int time3 = this.time;
                if (time3 >= 3600 && time3 < 86400) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("");
                    sb5.append(n / 60 / 60);
                    final String str3 = s = sb5.toString();
                    if (str3.length() < 2) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append(str3);
                        sb6.append("h");
                        s = sb6.toString();
                    }
                }
                else {
                    final int time4 = this.time;
                    if (time4 >= 86400 && time4 < 604800) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("");
                        sb7.append(n / 60 / 60 / 24);
                        final String str4 = s = sb7.toString();
                        if (str4.length() < 2) {
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append(str4);
                            sb8.append("d");
                            s = sb8.toString();
                        }
                    }
                    else {
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append("");
                        sb9.append(n / 60 / 60 / 24 / 7);
                        final String string = sb9.toString();
                        if (string.length() < 2) {
                            final StringBuilder sb10 = new StringBuilder();
                            sb10.append(string);
                            sb10.append("w");
                            s = sb10.toString();
                        }
                        else {
                            s = string;
                            if (string.length() > 2) {
                                s = "c";
                            }
                        }
                    }
                }
            }
        }
        this.timeWidth = this.timePaint.measureText(s);
        try {
            this.timeLayout = new StaticLayout((CharSequence)s, this.timePaint, (int)Math.ceil(this.timeWidth), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.timeHeight = this.timeLayout.getHeight();
        }
        catch (Exception ex) {
            this.timeLayout = null;
            FileLog.e(ex);
        }
        this.invalidateSelf();
    }
}
