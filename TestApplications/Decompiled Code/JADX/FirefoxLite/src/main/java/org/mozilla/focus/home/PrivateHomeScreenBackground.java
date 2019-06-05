package org.mozilla.focus.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.support.p004v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class PrivateHomeScreenBackground extends AppCompatImageView {
    private Paint paint;

    public PrivateHomeScreenBackground(Context context) {
        super(context, null);
        init();
    }

    public PrivateHomeScreenBackground(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        init();
    }

    public PrivateHomeScreenBackground(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* Access modifiers changed, original: 0000 */
    public void init() {
        Rect rect = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), 2131230871);
        this.paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(decodeResource, TileMode.REPEAT, TileMode.REPEAT);
        this.paint.setShader(new ComposeShader(new LinearGradient(0.0f, (float) rect.top, 0.0f, (float) rect.bottom, new int[]{Color.parseColor("#99FFFFFF"), Color.parseColor("#4dFFFFFF"), Color.parseColor("#1aFFFFFF"), Color.parseColor("#00FFFFFF")}, new float[]{0.0f, 0.4f, 0.7f, 1.0f}, TileMode.CLAMP), bitmapShader, Mode.MULTIPLY));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.paint);
    }
}
