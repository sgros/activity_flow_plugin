// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import org.mozilla.focus.utils.ViewUtils;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.ComposeShader;
import android.graphics.PorterDuff$Mode;
import android.graphics.BitmapShader;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Color;
import android.app.Activity;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;

public class HomeScreenBackground extends ThemedImageView implements Themeable
{
    private boolean isNight;
    private Paint paint;
    
    public HomeScreenBackground(final Context context) {
        super(context, null);
        this.init();
    }
    
    public HomeScreenBackground(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.init();
    }
    
    public HomeScreenBackground(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    void init() {
        final Rect rect = new Rect();
        ((Activity)this.getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        (this.paint = new Paint()).setShader((Shader)new ComposeShader((Shader)new LinearGradient(0.0f, (float)rect.top, 0.0f, (float)rect.bottom, new int[] { Color.parseColor("#99FFFFFF"), Color.parseColor("#4dFFFFFF"), Color.parseColor("#1aFFFFFF"), Color.parseColor("#00FFFFFF") }, new float[] { 0.0f, 0.4f, 0.7f, 1.0f }, Shader$TileMode.CLAMP), (Shader)new BitmapShader(BitmapFactory.decodeResource(this.getResources(), 2131230871), Shader$TileMode.REPEAT, Shader$TileMode.REPEAT), PorterDuff$Mode.MULTIPLY));
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.isNight) {
            this.setPadding(0, ViewUtils.getStatusBarHeight((Activity)this.getContext()), 0, 0);
        }
        else {
            this.setPadding(0, 0, 0, 0);
        }
    }
    
    @Override
    public void onThemeChanged() {
        this.setBackground(this.getContext().getTheme().getDrawable(2131230837));
    }
    
    @Override
    public void setNightMode(final boolean b) {
        super.setNightMode(b);
        this.isNight = b;
        if (this.isNight) {
            this.setImageResource(2131230968);
        }
        else {
            this.setImageResource(2131230866);
        }
    }
}
