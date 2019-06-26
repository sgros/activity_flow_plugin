// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Crop;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.RectF;
import android.widget.TextView;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class CropRotationWheel extends FrameLayout
{
    private static final int DELTA_ANGLE = 5;
    private static final int MAX_ANGLE = 45;
    private ImageView aspectRatioButton;
    private Paint bluePaint;
    private TextView degreesLabel;
    private float prevX;
    protected float rotation;
    private RotationWheelListener rotationListener;
    private RectF tempRect;
    private Paint whitePaint;
    
    public CropRotationWheel(final Context context) {
        super(context);
        this.tempRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        (this.whitePaint = new Paint()).setStyle(Paint$Style.FILL);
        this.whitePaint.setColor(-1);
        this.whitePaint.setAlpha(255);
        this.whitePaint.setAntiAlias(true);
        (this.bluePaint = new Paint()).setStyle(Paint$Style.FILL);
        this.bluePaint.setColor(-11420173);
        this.bluePaint.setAlpha(255);
        this.bluePaint.setAntiAlias(true);
        (this.aspectRatioButton = new ImageView(context)).setImageResource(2131165883);
        this.aspectRatioButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.aspectRatioButton.setScaleType(ImageView$ScaleType.CENTER);
        this.aspectRatioButton.setOnClickListener((View$OnClickListener)new _$$Lambda$CropRotationWheel$o9DV_6J5Q1lFFifNNTKEl8cNF9w(this));
        this.aspectRatioButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrAspectRatio", 2131558412));
        this.addView((View)this.aspectRatioButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(70, 64, 19));
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165885);
        imageView.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$CropRotationWheel$Is9w1zkokBjEYWq9bkepljuCiac(this));
        imageView.setContentDescription((CharSequence)LocaleController.getString("AccDescrRotate", 2131558466));
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(70, 64, 21));
        (this.degreesLabel = new TextView(context)).setTextColor(-1);
        this.addView((View)this.degreesLabel, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 49));
        this.setWillNotDraw(false);
        this.setRotation(0.0f, false);
    }
    
    protected void drawLine(final Canvas canvas, int min, float n, int n2, final int n3, final boolean b, Paint bluePaint) {
        final int n4 = (int)(n2 / 2.0f - AndroidUtilities.dp(70.0f));
        final float n5 = (float)(min * 5);
        final double v = n4;
        final double cos = Math.cos(Math.toRadians(90.0f - (n5 + n)));
        Double.isNaN(v);
        min = (int)(v * cos);
        n2 = n2 / 2 + min;
        n = Math.abs(min) / (float)n4;
        min = Math.min(255, Math.max(0, (int)((1.0f - n * n) * 255.0f)));
        if (b) {
            bluePaint = this.bluePaint;
        }
        bluePaint.setAlpha(min);
        if (b) {
            min = 4;
        }
        else {
            min = 2;
        }
        if (b) {
            n = 16.0f;
        }
        else {
            n = 12.0f;
        }
        final int dp = AndroidUtilities.dp(n);
        min /= 2;
        canvas.drawRect((float)(n2 - min), (float)((n3 - dp) / 2), (float)(n2 + min), (float)((n3 + dp) / 2), bluePaint);
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int width = this.getWidth();
        final int height = this.getHeight();
        final float n = -this.rotation * 2.0f;
        final float n2 = n % 5.0f;
        final int n3 = (int)Math.floor(n / 5.0f);
        for (int i = 0; i < 16; ++i) {
            final Paint whitePaint = this.whitePaint;
            Paint bluePaint = null;
            Label_0092: {
                if (i >= n3) {
                    bluePaint = whitePaint;
                    if (i != 0) {
                        break Label_0092;
                    }
                    bluePaint = whitePaint;
                    if (n2 >= 0.0f) {
                        break Label_0092;
                    }
                }
                bluePaint = this.bluePaint;
            }
            this.drawLine(canvas, i, n2, width, height, i == n3 || (i == 0 && n3 == -1), bluePaint);
            if (i != 0) {
                final int n4 = -i;
                Paint paint;
                if (n4 > n3) {
                    paint = this.bluePaint;
                }
                else {
                    paint = this.whitePaint;
                }
                this.drawLine(canvas, n4, n2, width, height, n4 == n3 + 1, paint);
            }
        }
        this.bluePaint.setAlpha(255);
        this.tempRect.left = (float)((width - AndroidUtilities.dp(2.5f)) / 2);
        this.tempRect.top = (float)((height - AndroidUtilities.dp(22.0f)) / 2);
        this.tempRect.right = (float)((width + AndroidUtilities.dp(2.5f)) / 2);
        this.tempRect.bottom = (float)((height + AndroidUtilities.dp(22.0f)) / 2);
        canvas.drawRoundRect(this.tempRect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.bluePaint);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(Math.min(View$MeasureSpec.getSize(n), AndroidUtilities.dp(400.0f)), 1073741824), n2);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final float x = motionEvent.getX();
        if (actionMasked == 0) {
            this.prevX = x;
            final RotationWheelListener rotationListener = this.rotationListener;
            if (rotationListener != null) {
                rotationListener.onStart();
            }
        }
        else if (actionMasked != 1 && actionMasked != 3) {
            if (actionMasked == 2) {
                final float prevX = this.prevX;
                final float rotation = this.rotation;
                final double v = (prevX - x) / AndroidUtilities.density;
                Double.isNaN(v);
                final float max = Math.max(-45.0f, Math.min(45.0f, rotation + (float)(v / 3.141592653589793 / 1.649999976158142)));
                if (Math.abs(max - this.rotation) > 0.001) {
                    float n = max;
                    if (Math.abs(max) < 0.05) {
                        n = 0.0f;
                    }
                    this.setRotation(n, false);
                    final RotationWheelListener rotationListener2 = this.rotationListener;
                    if (rotationListener2 != null) {
                        rotationListener2.onChange(this.rotation);
                    }
                    this.prevX = x;
                }
            }
        }
        else {
            final RotationWheelListener rotationListener3 = this.rotationListener;
            if (rotationListener3 != null) {
                rotationListener3.onEnd(this.rotation);
            }
            AndroidUtilities.makeAccessibilityAnnouncement(String.format("%.1f°", this.rotation));
        }
        return true;
    }
    
    public void reset() {
        this.setRotation(0.0f, false);
    }
    
    public void setAspectLock(final boolean b) {
        final ImageView aspectRatioButton = this.aspectRatioButton;
        Object colorFilter;
        if (b) {
            colorFilter = new PorterDuffColorFilter(-11420173, PorterDuff$Mode.MULTIPLY);
        }
        else {
            colorFilter = null;
        }
        aspectRatioButton.setColorFilter((ColorFilter)colorFilter);
    }
    
    public void setFreeform(final boolean b) {
        final ImageView aspectRatioButton = this.aspectRatioButton;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        aspectRatioButton.setVisibility(visibility);
    }
    
    public void setListener(final RotationWheelListener rotationListener) {
        this.rotationListener = rotationListener;
    }
    
    public void setRotation(float n, final boolean b) {
        this.rotation = n;
        final float n2 = n = this.rotation;
        if (Math.abs(n2) < 0.099) {
            n = Math.abs(n2);
        }
        this.degreesLabel.setText((CharSequence)String.format("%.1fº", n));
        this.invalidate();
    }
    
    public interface RotationWheelListener
    {
        void aspectRatioPressed();
        
        void onChange(final float p0);
        
        void onEnd(final float p0);
        
        void onStart();
        
        void rotate90Pressed();
    }
}
