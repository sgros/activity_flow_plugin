// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

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
import android.support.v7.widget.AppCompatImageView;

public class PrivateHomeScreenBackground extends AppCompatImageView
{
    private Paint paint;
    
    public PrivateHomeScreenBackground(final Context context) {
        super(context, null);
        this.init();
    }
    
    public PrivateHomeScreenBackground(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.init();
    }
    
    public PrivateHomeScreenBackground(final Context context, final AttributeSet set, final int n) {
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
        canvas.drawRect(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight(), this.paint);
    }
}
