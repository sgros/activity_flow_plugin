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
import android.util.AttributeSet;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.theme.ThemeManager.Themeable;

public class HomeScreenBackground extends ThemedImageView implements Themeable {
    private boolean isNight;
    private Paint paint;

    public HomeScreenBackground(Context context) {
        super(context, null);
        init();
    }

    public HomeScreenBackground(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        init();
    }

    public HomeScreenBackground(Context context, AttributeSet attributeSet, int i) {
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
        if (this.isNight) {
            setPadding(0, ViewUtils.getStatusBarHeight((Activity) getContext()), 0, 0);
        } else {
            setPadding(0, 0, 0, 0);
        }
    }

    public void onThemeChanged() {
        setBackground(getContext().getTheme().getDrawable(2131230837));
    }

    public void setNightMode(boolean z) {
        super.setNightMode(z);
        this.isNight = z;
        if (this.isNight) {
            setImageResource(2131230968);
        } else {
            setImageResource(2131230866);
        }
    }
}
